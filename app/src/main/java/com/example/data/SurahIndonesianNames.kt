package com.example.data

import com.example.model.Surah

object SurahIndonesianNames {

    private val indonesianNames = mapOf(
        78 to "An-Naba'",
        79 to "An-Nazi'at",
        80 to "'Abasa",
        81 to "At-Takwir",
        82 to "Al-Infitar",
        83 to "Al-Mutaffifin",
        84 to "Al-Insyiqaq",
        85 to "Al-Buruj",
        86 to "At-Tariq",
        87 to "Al-A'la",
        88 to "Al-Ghasyiyah",
        89 to "Al-Fajr",
        90 to "Al-Balad",
        91 to "Asy-Syams",
        92 to "Al-Lail",
        93 to "Ad-Duha",
        94 to "Asy-Syarh",
        95 to "At-Tin",
        96 to "Al-'Alaq",
        97 to "Al-Qadr",
        98 to "Al-Bayyinah",
        99 to "Az-Zalzalah",
        100 to "Al-'Adiyat",
        101 to "Al-Qari'ah",
        102 to "At-Takasur",
        103 to "Al-'Asr",
        104 to "Al-Humazah",
        105 to "Al-Fil",
        106 to "Quraisy",
        107 to "Al-Ma'un",
        108 to "Al-Kausar",
        109 to "Al-Kafirun",
        110 to "An-Nasr",
        111 to "Al-Lahab",
        112 to "Al-Ikhlas",
        113 to "Al-Falaq",
        114 to "An-Nas"
    )

    private val indonesianMeanings = mapOf(
        78 to "Berita Besar",
        79 to "Malaikat-Malaikat Yang Mencabut",
        80 to "Ia Bermuka Masam",
        81 to "Menggulung",
        82 to "Terbelah",
        83 to "Orang-Orang Curang",
        84 to "Terbelah",
        85 to "Gugusan Bintang",
        86 to "Yang Datang di Malam Hari",
        87 to "Yang Paling Tinggi",
        88 to "Hari Pembalasan",
        89 to "Fajar",
        90 to "Negeri",
        91 to "Matahari",
        92 to "Malam",
        93 to "Waktu Dhuha",
        94 to "Kelapangan",
        95 to "Buah Tin",
        96 to "Segumpal Darah",
        97 to "Kemuliaan",
        98 to "Bukti Nyata",
        99 to "Kegoncangan",
        100 to "Kuda Perang Berlari Kencang",
        101 to "Hari Kiamat",
        102 to "Bermegah-Megahan",
        103 to "Masa",
        104 to "Pengumpat",
        105 to "Gajah",
        106 to "Suku Quraisy",
        107 to "Barang-Barang Berguna",
        108 to "Nikmat Berlimpah",
        109 to "Orang-Orang Kafir",
        110 to "Pertolongan",
        111 to "Gejolak Api",
        112 to "Kemurnian Keesaan Allah",
        113 to "Waktu Subuh",
        114 to "Manusia"
    )

    fun getName(surahId: Int): String {
        return indonesianNames[surahId] ?: JuzAmmaRepository.surahs.find { it.id == surahId }?.nameTransliteration ?: ""
    }

    fun getMeaning(surahId: Int, language: TranslationLanguage): String {
        return if (language == TranslationLanguage.INDONESIAN) {
            indonesianMeanings[surahId] ?: ""
        } else {
            JuzAmmaRepository.surahs.find { it.id == surahId }?.nameTranslation ?: ""
        }
    }
}

fun Surah.getLocalizedName(language: TranslationLanguage): String {
    return if (language == TranslationLanguage.INDONESIAN) {
        SurahIndonesianNames.getName(id)
    } else {
        nameTransliteration
    }
}

fun Surah.getLocalizedMeaning(language: TranslationLanguage): String {
    return if (language == TranslationLanguage.INDONESIAN) {
        SurahIndonesianNames.getMeaning(id, language)
    } else {
        nameTranslation
    }
}
