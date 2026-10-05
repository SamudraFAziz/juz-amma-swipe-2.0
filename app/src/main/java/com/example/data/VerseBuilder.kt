package com.example.data

import com.example.model.TajweedRuleType
import com.example.model.Verse

object VerseBuilder {
    fun make(
        surahNum: Int,
        sName: String,
        vNum: Int,
        ar: String,
        taggedAr: String,
        translit: String,
        transl: String,
        rules: List<TajweedRuleType> = emptyList()
    ): Verse {
        return Verse(
            globalIndex = 0, // will be indexed properly in repository
            surahNumber = surahNum,
            surahNameTransliteration = sName,
            verseNumber = vNum,
            arabicText = ar,
            taggedArabicText = taggedAr,
            transliteration = translit,
            translation = transl,
            tajweedRules = rules,
            isBismillahNeeded = (vNum == 1 && surahNum != 1)
        )
    }
}
