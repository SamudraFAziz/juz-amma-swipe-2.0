package com.example.data

import com.example.model.RevelationType
import com.example.model.Surah
import com.example.model.Verse

object JuzAmmaRepository {

    private val baseSurahMetadata: List<Surah> = listOf(
        Surah(78, "النبأ", "An-Naba", "The Tidings", 40, RevelationType.MECCAN, 0),
        Surah(79, "النازعات", "An-Nazi'at", "Those Who Drag Forth", 46, RevelationType.MECCAN, 0),
        Surah(80, "عبس", "Abasa", "He Frowned", 42, RevelationType.MECCAN, 0),
        Surah(81, "التكوير", "At-Takwir", "The Overthrowing", 29, RevelationType.MECCAN, 0),
        Surah(82, "الانفطار", "Al-Infitar", "The Cleaving", 19, RevelationType.MECCAN, 0),
        Surah(83, "المطففين", "Al-Mutaffifin", "Defrauding", 36, RevelationType.MECCAN, 0),
        Surah(84, "الانشقاق", "Al-Inshiqaq", "The Sundering", 25, RevelationType.MECCAN, 0),
        Surah(85, "البروج", "Al-Buruj", "The Mansions of the Stars", 22, RevelationType.MECCAN, 0),
        Surah(86, "الطارق", "At-Tariq", "The Morning Star", 17, RevelationType.MECCAN, 0),
        Surah(87, "الأعلى", "Al-A'la", "The Most High", 19, RevelationType.MECCAN, 0),
        Surah(88, "الغاشية", "Al-Ghashiyah", "The Overwhelming Event", 26, RevelationType.MECCAN, 0),
        Surah(89, "الفجر", "Al-Fajr", "The Dawn", 30, RevelationType.MECCAN, 0),
        Surah(90, "البلد", "Al-Balad", "The City", 20, RevelationType.MECCAN, 0),
        Surah(91, "الشمس", "Ash-Shams", "The Sun", 15, RevelationType.MECCAN, 0),
        Surah(92, "الليل", "Al-Layl", "The Night", 21, RevelationType.MECCAN, 0),
        Surah(93, "الضحى", "Ad-Duhaa", "The Morning Hours", 11, RevelationType.MECCAN, 0),
        Surah(94, "الشرح", "Ash-Sharh", "The Relief", 8, RevelationType.MECCAN, 0),
        Surah(95, "التين", "At-Tin", "The Fig", 8, RevelationType.MECCAN, 0),
        Surah(96, "العلق", "Al-'Alaq", "The Clot", 19, RevelationType.MECCAN, 0),
        Surah(97, "القدر", "Al-Qadr", "The Night of Decree", 5, RevelationType.MECCAN, 0),
        Surah(98, "البينة", "Al-Bayyinah", "The Clear Proof", 8, RevelationType.MEDINAN, 0),
        Surah(99, "الزلزلة", "Az-Zalzalah", "The Earthquake", 8, RevelationType.MEDINAN, 0),
        Surah(100, "العاديات", "Al-'Adiyat", "The Courser", 11, RevelationType.MECCAN, 0),
        Surah(101, "القارعة", "Al-Qari'ah", "The Calamity", 11, RevelationType.MECCAN, 0),
        Surah(102, "التكاثر", "At-Takathur", "Rivalry in Worldly Increase", 8, RevelationType.MECCAN, 0),
        Surah(103, "العصر", "Al-'Asr", "The Declining Day", 3, RevelationType.MECCAN, 0),
        Surah(104, "الهمزة", "Al-Humazah", "The Traducer", 9, RevelationType.MECCAN, 0),
        Surah(105, "الفيل", "Al-Fil", "The Elephant", 5, RevelationType.MECCAN, 0),
        Surah(106, "قريش", "Quraysh", "Quraysh", 4, RevelationType.MECCAN, 0),
        Surah(107, "الماعون", "Al-Ma'un", "Small Kindnesses", 7, RevelationType.MECCAN, 0),
        Surah(108, "الكوثر", "Al-Kawthar", "Abundance", 3, RevelationType.MECCAN, 0),
        Surah(109, "الكافرون", "Al-Kafirun", "The Disbelievers", 6, RevelationType.MECCAN, 0),
        Surah(110, "النصر", "An-Nasr", "The Divine Support", 3, RevelationType.MEDINAN, 0),
        Surah(111, "المسد", "Al-Masad", "The Palm Fiber", 5, RevelationType.MECCAN, 0),
        Surah(112, "الإخلاص", "Al-Ikhlas", "The Sincerity", 4, RevelationType.MECCAN, 0),
        Surah(113, "الفلق", "Al-Falaq", "The Daybreak", 5, RevelationType.MECCAN, 0),
        Surah(114, "الناس", "An-Nas", "Mankind", 6, RevelationType.MECCAN, 0)
    )

    val allVerses: List<Verse> by lazy {
        val rawVerses = JuzAmmaPart1.verses +
            JuzAmmaPart2.verses +
            JuzAmmaPart3.verses +
            JuzAmmaPart4.verses

        rawVerses.mapIndexed { index, verse ->
            verse.copy(globalIndex = index)
        }
    }

    val surahs: List<Surah> by lazy {
        baseSurahMetadata.map { base ->
            val firstIndex = allVerses.indexOfFirst { it.surahNumber == base.id }
            base.copy(startVerseIndex = if (firstIndex != -1) firstIndex else 0)
        }
    }

    fun getSurahByNumber(surahNumber: Int): Surah? {
        return surahs.find { it.id == surahNumber }
    }
}
