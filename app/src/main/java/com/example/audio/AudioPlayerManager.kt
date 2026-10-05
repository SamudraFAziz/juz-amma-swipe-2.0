package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import com.example.model.Verse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class AudioPlayerManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    private var mediaPlayer: MediaPlayer? = null
    private var progressUpdateJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentVerse = MutableStateFlow<Verse?>(null)
    val currentVerse: StateFlow<Verse?> = _currentVerse.asStateFlow()

    private val _progress = MutableStateFlow(0f) // 0.0 to 1.0
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _speed = MutableStateFlow(1.0f)
    val speed: StateFlow<Float> = _speed.asStateFlow()

    private val _isAutoNextEnabled = MutableStateFlow(false)
    val isAutoNextEnabled: StateFlow<Boolean> = _isAutoNextEnabled.asStateFlow()

    private val _isLoopingVerse = MutableStateFlow(false)
    val isLoopingVerse: StateFlow<Boolean> = _isLoopingVerse.asStateFlow()

    private val _isAutoPlayOnSwipe = MutableStateFlow(true)
    val isAutoPlayOnSwipe: StateFlow<Boolean> = _isAutoPlayOnSwipe.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _offlineDownloadStatus = MutableStateFlow<String?>(null)
    val offlineDownloadStatus: StateFlow<String?> = _offlineDownloadStatus.asStateFlow()

    private val _isDownloadingOffline = MutableStateFlow(false)
    val isDownloadingOffline: StateFlow<Boolean> = _isDownloadingOffline.asStateFlow()

    var onVerseCompletedCallback: (() -> Unit)? = null

    private val audioCacheDir: File by lazy {
        File(context.filesDir, "quran_audio_offline").apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    private fun getLocalAudioFile(verse: Verse): File {
        return File(audioCacheDir, "ayah_${verse.surahNumber}_${verse.verseNumber}.mp3")
    }

    fun isVerseCached(verse: Verse): Boolean {
        val file = getLocalAudioFile(verse)
        return file.exists() && file.length() > 2048
    }

    fun getCachedVersesCount(allVerses: List<Verse>): Int {
        return allVerses.count { isVerseCached(it) }
    }

    fun toggleAutoPlayOnSwipe() {
        _isAutoPlayOnSwipe.value = !_isAutoPlayOnSwipe.value
    }

    fun toggleAutoNext() {
        _isAutoNextEnabled.value = !_isAutoNextEnabled.value
    }

    fun toggleLoopingVerse() {
        _isLoopingVerse.value = !_isLoopingVerse.value
    }

    fun setPlaybackSpeed(newSpeed: Float) {
        _speed.value = newSpeed
        mediaPlayer?.let { player ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && _isPlaying.value) {
                try {
                    player.playbackParams = player.playbackParams.setSpeed(newSpeed)
                } catch (e: Exception) {
                    Log.e("AudioPlayer", "Error setting speed: ${e.message}")
                }
            }
        }
    }

    /**
     * Force replays the verse immediately from 00:00 without pausing.
     * Perfect for rapid memorization and repetition.
     */
    fun replayVerse(verse: Verse) {
        val current = _currentVerse.value
        val player = mediaPlayer
        if (current?.globalIndex == verse.globalIndex && player != null) {
            try {
                player.seekTo(0)
                if (!_isPlaying.value) {
                    player.start()
                    _isPlaying.value = true
                }
                _progress.value = 0f
                _errorMessage.value = null
                startProgressTracker()
                return
            } catch (e: Exception) {
                Log.w("AudioPlayer", "Replay seek failed, restarting player: ${e.message}")
            }
        }
        playVerse(verse, autoStart = true)
    }

    fun playVerse(verse: Verse, autoStart: Boolean = true) {
        _currentVerse.value = verse
        _errorMessage.value = null

        if (!autoStart && !_isAutoPlayOnSwipe.value) {
            stop()
            return
        }

        releasePlayer()

        _isLoading.value = true
        _isPlaying.value = false
        _progress.value = 0f

        val localFile = getLocalAudioFile(verse)

        if (localFile.exists() && localFile.length() > 2048) {
            // Play from local cache (100% offline, zero network latency)
            playFromLocalFile(localFile, verse)
        } else {
            // Stream online and cache locally in the background
            playFromUrlAndCache(verse)
        }
    }

    private fun playFromLocalFile(file: File, verse: Verse) {
        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(file.absolutePath)

                setOnPreparedListener { mp ->
                    _isLoading.value = false
                    _isPlaying.value = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            mp.playbackParams = mp.playbackParams.setSpeed(_speed.value)
                        } catch (e: Exception) {
                            Log.e("AudioPlayer", "Failed to set playback params: ${e.message}")
                        }
                    }
                    mp.start()
                    startProgressTracker()
                }

                setOnCompletionListener {
                    _isPlaying.value = false
                    _progress.value = 1f
                    stopProgressTracker()

                    if (_isLoopingVerse.value) {
                        replayVerse(verse)
                    } else if (_isAutoNextEnabled.value) {
                        onVerseCompletedCallback?.invoke()
                    }
                }

                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "Local audio playback error ($what, $extra), trying URL stream")
                    file.delete() // remove corrupted file
                    playFromUrlAndCache(verse)
                    true
                }

                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Exception playing local file: ${e.message}")
            playFromUrlAndCache(verse)
        }
    }

    private fun playFromUrlAndCache(verse: Verse) {
        val primaryUrl = verse.getAlafasyAudioUrl()

        // Background download for offline caching
        cacheVerseInBackground(verse, primaryUrl)

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(primaryUrl)

                setOnPreparedListener { mp ->
                    _isLoading.value = false
                    _isPlaying.value = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            mp.playbackParams = mp.playbackParams.setSpeed(_speed.value)
                        } catch (e: Exception) {
                            Log.e("AudioPlayer", "Failed to set playback params: ${e.message}")
                        }
                    }
                    mp.start()
                    startProgressTracker()
                }

                setOnCompletionListener {
                    _isPlaying.value = false
                    _progress.value = 1f
                    stopProgressTracker()

                    if (_isLoopingVerse.value) {
                        replayVerse(verse)
                    } else if (_isAutoNextEnabled.value) {
                        onVerseCompletedCallback?.invoke()
                    }
                }

                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "Error streaming primary URL ($what, $extra). Trying backup...")
                    tryFallbackUrl(verse)
                    true
                }

                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Exception initializing stream player: ${e.message}")
            tryFallbackUrl(verse)
        }
    }

    private fun cacheVerseInBackground(verse: Verse, urlString: String) {
        scope.launch(Dispatchers.IO) {
            val localFile = getLocalAudioFile(verse)
            if (localFile.exists() && localFile.length() > 2048) return@launch

            val tempFile = File(audioCacheDir, "temp_${verse.surahNumber}_${verse.verseNumber}.tmp")
            try {
                val connection = URL(urlString).openConnection() as HttpURLConnection
                connection.connectTimeout = 8000
                connection.readTimeout = 15000
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.use { input ->
                        FileOutputStream(tempFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    if (tempFile.length() > 2048) {
                        tempFile.renameTo(localFile)
                        Log.d("AudioPlayer", "Successfully cached offline audio for Surah ${verse.surahNumber}:${verse.verseNumber}")
                    } else {
                        tempFile.delete()
                    }
                }
            } catch (e: Exception) {
                tempFile.delete()
                Log.d("AudioPlayer", "Background audio caching note: ${e.message}")
            }
        }
    }

    private fun tryFallbackUrl(verse: Verse) {
        releasePlayer()
        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(verse.getBackupAudioUrl())
                setOnPreparedListener { mp ->
                    _isLoading.value = false
                    _isPlaying.value = true
                    mp.start()
                    startProgressTracker()
                }
                setOnCompletionListener {
                    _isPlaying.value = false
                    _progress.value = 1f
                    if (_isLoopingVerse.value) {
                        replayVerse(verse)
                    } else if (_isAutoNextEnabled.value) {
                        onVerseCompletedCallback?.invoke()
                    }
                }
                setOnErrorListener { _, _, _ ->
                    _isLoading.value = false
                    _isPlaying.value = false
                    _errorMessage.value = "Audio requires internet or cached offline file."
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            _isLoading.value = false
            _isPlaying.value = false
            _errorMessage.value = "Audio unavailable."
        }
    }

    /**
     * Download all verses of a Surah (or entire Juz 'Amma) for full offline use
     */
    fun downloadVersesForOffline(verses: List<Verse>) {
        if (_isDownloadingOffline.value) return
        _isDownloadingOffline.value = true

        scope.launch(Dispatchers.IO) {
            var downloaded = 0
            val total = verses.size

            verses.forEachIndexed { idx, verse ->
                val localFile = getLocalAudioFile(verse)
                if (!localFile.exists() || localFile.length() <= 2048) {
                    try {
                        val tempFile = File(audioCacheDir, "down_${verse.surahNumber}_${verse.verseNumber}.tmp")
                        val url = URL(verse.getAlafasyAudioUrl())
                        val connection = url.openConnection() as HttpURLConnection
                        connection.connectTimeout = 8000
                        connection.readTimeout = 15000
                        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                            connection.inputStream.use { input ->
                                FileOutputStream(tempFile).use { output ->
                                    input.copyTo(output)
                                }
                            }
                            if (tempFile.length() > 2048) {
                                tempFile.renameTo(localFile)
                            } else {
                                tempFile.delete()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("AudioPlayer", "Error downloading verse ${verse.verseNumber}: ${e.message}")
                    }
                }
                downloaded++
                _offlineDownloadStatus.value = "Saving for offline: $downloaded / $total verses"
            }

            delay(1500)
            _offlineDownloadStatus.value = "Offline audio ready (${verses.size} verses)"
            delay(2500)
            _offlineDownloadStatus.value = null
            _isDownloadingOffline.value = false
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: run {
            _currentVerse.value?.let { playVerse(it, autoStart = true) }
            return
        }

        if (_isPlaying.value) {
            player.pause()
            _isPlaying.value = false
            stopProgressTracker()
        } else {
            player.start()
            _isPlaying.value = true
            startProgressTracker()
        }
    }

    fun seekTo(positionFraction: Float) {
        mediaPlayer?.let { player ->
            if (player.duration > 0) {
                val newPos = (positionFraction * player.duration).toInt()
                player.seekTo(newPos)
                _progress.value = positionFraction
            }
        }
    }

    fun stop() {
        stopProgressTracker()
        releasePlayer()
        _isPlaying.value = false
        _isLoading.value = false
        _progress.value = 0f
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressUpdateJob = scope.launch {
            while (_isPlaying.value) {
                mediaPlayer?.let { player ->
                    if (player.duration > 0) {
                        _progress.value = player.currentPosition.toFloat() / player.duration.toFloat()
                    }
                }
                delay(200)
            }
        }
    }

    private fun stopProgressTracker() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }

    private fun releasePlayer() {
        mediaPlayer?.apply {
            try {
                if (isPlaying) {
                    stop()
                }
                reset()
                release()
            } catch (e: Exception) {
                Log.e("AudioPlayer", "Error releasing player: ${e.message}")
            }
        }
        mediaPlayer = null
    }

    fun clear() {
        stopProgressTracker()
        releasePlayer()
    }
}
