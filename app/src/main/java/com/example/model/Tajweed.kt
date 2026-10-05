package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.TajweedGhunnahColor
import com.example.ui.theme.TajweedHamzatWaslColor
import com.example.ui.theme.TajweedIdghamColor
import com.example.ui.theme.TajweedIqlabColor
import com.example.ui.theme.TajweedMaddColor
import com.example.ui.theme.TajweedQalqalahColor
import com.example.ui.theme.TajweedTafkhimColor

enum class TajweedRuleType(
    val ruleName: String,
    val arabicName: String,
    val color: Color,
    val description: String,
    val studentTip: String,
    val exampleText: String
) {
    GHUNNAH(
        ruleName = "Ghunnah & Ikhfa",
        arabicName = "غُنَّة وإِخْفَاء",
        color = TajweedGhunnahColor,
        description = "Nasal sound held for 2 counts when pronouncing Noon (ن) or Meem (م) with Shaddah, or during Ikhfa (concealment).",
        studentTip = "Softly echo the sound through your nasal cavity without pressing the tongue firmly against the teeth.",
        exampleText = "عَمَّ"
    ),
    QALQALAH(
        ruleName = "Qalqalah (Echoing)",
        arabicName = "قَلْقَلَة",
        color = TajweedQalqalahColor,
        description = "A strong bouncing/echoing effect on letters (ق, ط, ب, ج, د) when they carry a Sukoon or when stopping.",
        studentTip = "Release the articulation point abruptly with a crisp, rebounding vibration.",
        exampleText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ"
    ),
    IDGHAM(
        ruleName = "Idgham (Assimilation)",
        arabicName = "إِدْغَام",
        color = TajweedIdghamColor,
        description = "Merging a silent Noon or Tanween into the following letter seamlessly (ي, ر, م, ل, و, ن).",
        studentTip = "Blend the two letters completely into one accented letter.",
        exampleText = "مَن يَقُولُ"
    ),
    MADD(
        ruleName = "Madd (Prolongation)",
        arabicName = "مَدّ",
        color = TajweedMaddColor,
        description = "Lengthening the vowel sound (A, I, U) for 2, 4, 5, or 6 counts when followed by Hamzah or Sukoon.",
        studentTip = "Hold the vowel sound smooth and steady without break.",
        exampleText = "إِذَا جَاءَ"
    ),
    IQLAB(
        ruleName = "Iqlab (Conversion)",
        arabicName = "إِقْلَاب",
        color = TajweedIqlabColor,
        description = "Converting a silent Noon or Tanween into a light Meem sound when followed by the letter Ba (ب).",
        studentTip = "Close lips gently as if pronouncing a Meem with a light 2-count Ghunnah.",
        exampleText = "مِن بَعْدِ"
    ),
    TAFKHIM(
        ruleName = "Tafkhim (Heavy Sound)",
        arabicName = "تَفْخِيم",
        color = TajweedTafkhimColor,
        description = "Pronouncing letters with a full, heavy, reverberating voice (خ, ص, ض, غ, ط, ق, ظ and Ra/Allah under heavy rules).",
        studentTip = "Elevate the back of the tongue toward the soft palate to fill the mouth with sound.",
        exampleText = "الْحَاقَّةُ"
    ),
    HAMZAT_WASL(
        ruleName = "Hamzat Al-Wasl",
        arabicName = "هَمْزَة الْوَصْل",
        color = TajweedHamzatWaslColor,
        description = "Connecting Hamzah that is pronounced only at the start of a sentence and dropped during continuous recitation.",
        studentTip = "Skip over this letter smoothly when connecting from the preceding word.",
        exampleText = "الرَّحْمَٰنِ"
    )
}

data class TajweedSpan(
    val startIndex: Int,
    val endIndex: Int,
    val ruleType: TajweedRuleType
)
