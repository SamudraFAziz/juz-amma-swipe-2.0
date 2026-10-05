package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TranslationLanguage
import com.example.data.getLocalizedMeaning
import com.example.model.RevelationType
import com.example.model.Surah
import com.example.ui.localization.AppStrings
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Authentic Islamic 8-Pointed Star Rosette Medallion (Rub el Hizb)
 * Faithfully matches the Ayah number rosettes from the classic Mushaf reference screenshots.
 */
@Composable
fun IslamicRosetteMedallion(
    number: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    primaryColor: Color = MaterialTheme.colorScheme.secondary,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    isHighlighted: Boolean = false
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = (this.size.minDimension / 2f) * 0.92f

            // 1. Draw 8-pointed star background (two rotated overlapping squares with curved points)
            val starPath = Path()
            val points = 8
            val outerRadius = radius
            val innerRadius = radius * 0.72f

            for (i in 0 until points * 2) {
                val r = if (i % 2 == 0) outerRadius else innerRadius
                val angle = (i * PI / points) - (PI / 2)
                val x = center.x + (r * cos(angle)).toFloat()
                val y = center.y + (r * sin(angle)).toFloat()
                if (i == 0) {
                    starPath.moveTo(x, y)
                } else {
                    starPath.lineTo(x, y)
                }
            }
            starPath.close()

            // Fill star container
            drawPath(
                path = starPath,
                color = if (isHighlighted) primaryColor.copy(alpha = 0.25f) else containerColor,
                style = Fill
            )

            // Outer star outline with decorative gold stroke
            drawPath(
                path = starPath,
                color = primaryColor.copy(alpha = if (isHighlighted) 1.0f else 0.8f),
                style = Stroke(
                    width = 1.4.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // 2. Inner concentric decorative circle with fine double ring
            val innerCircleRadius = radius * 0.62f
            drawCircle(
                color = primaryColor.copy(alpha = 0.4f),
                radius = innerCircleRadius,
                center = center,
                style = Stroke(width = 0.8.dp.toPx())
            )

            // 3. Four corner micro-accents around center
            val dotDist = innerCircleRadius * 0.9f
            for (angleDeg in listOf(0, 90, 180, 270)) {
                val rad = angleDeg * PI / 180.0
                val dotX = center.x + (dotDist * cos(rad)).toFloat()
                val dotY = center.y + (dotDist * sin(rad)).toFloat()
                drawCircle(
                    color = primaryColor.copy(alpha = 0.6f),
                    radius = 1.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
            }
        }

        Text(
            text = number,
            style = if (size > 36.dp) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = if (isHighlighted) primaryColor else textColor,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Traditional Illuminated Surah Header Cartouche (Unwan / Surah Banner)
 * Modeled after traditional Mushaf illumination (seen in reference screenshot 1):
 * - Arabesque golden double border with ornate corner brackets
 * - Left circular medallion for Surah number
 * - Center calligraphy & Surah name
 * - Right circular medallion for total verses
 * - Interactive: Tappable to open verse picker, with bookmark & ayah picker buttons
 */
@Composable
fun IlluminatedSurahCartouche(
    surah: Surah,
    currentVerseNumber: Int,
    isBookmarked: Boolean,
    onOpenVersePicker: () -> Unit,
    onBookmarkToggled: () -> Unit,
    language: TranslationLanguage = TranslationLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val goldColor = MaterialTheme.colorScheme.secondary
    val primaryColor = MaterialTheme.colorScheme.primary
    val cardSurface = MaterialTheme.colorScheme.surface

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onOpenVersePicker() }
            .testTag("surah_verse_menu_header"),
        shape = RoundedCornerShape(14.dp),
        color = cardSurface,
        shadowElevation = 3.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Ornate Arabesque Background Canvas
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height
                val cornerR = 14.dp.toPx()

                // Subtle gold gradient background wash
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            goldColor.copy(alpha = 0.08f),
                            goldColor.copy(alpha = 0.18f),
                            goldColor.copy(alpha = 0.08f)
                        )
                    ),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerR, cornerR)
                )

                // Double outer gold border
                drawRoundRect(
                    color = goldColor.copy(alpha = 0.65f),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerR, cornerR),
                    style = Stroke(width = 1.8.dp.toPx())
                )

                // Inner fine hairline inset border (traditional manuscript styling)
                val inset = 4.dp.toPx()
                if (w > inset * 2 && h > inset * 2) {
                    drawRoundRect(
                        color = goldColor.copy(alpha = 0.35f),
                        topLeft = Offset(inset, inset),
                        size = androidx.compose.ui.geometry.Size(w - inset * 2, h - inset * 2),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerR - inset, cornerR - inset),
                        style = Stroke(width = 0.8.dp.toPx())
                    )
                }

                // Decorative Arabesque flourishes at top and bottom center
                drawArabesqueFlourish(goldColor, w / 2f, 4.dp.toPx(), isTop = true)
                drawArabesqueFlourish(goldColor, w / 2f, h - 4.dp.toPx(), isTop = false)
            }

            // Foreground Content: Symmetrical Layout with 100% Centered Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            ) {
                // Left Medallion: Surah Number (Surah identifier rosette)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 6.dp)
                ) {
                    IslamicRosetteMedallion(
                        number = "${surah.id}",
                        size = 38.dp,
                        primaryColor = goldColor,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                    Text(
                        text = "Surah",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        fontWeight = FontWeight.Bold,
                        color = goldColor.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Center: 100% Mathematically Centered Calligraphic Title & Details
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 62.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Calligraphic Arabic Surah Heading
                    Text(
                        text = "سُورَةُ ${surah.nameArabic}",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${surah.nameTransliteration} (${surah.getLocalizedMeaning(language)})",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    // Centered Ayah Picker Pill & Bookmark Button Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = primaryColor.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(0.8.dp, primaryColor.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onOpenVersePicker() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val revTitle = if (surah.revelationType == RevelationType.MECCAN) AppStrings.revelationMeccan(language) else AppStrings.revelationMedinan(language)
                                val ayahWord = AppStrings.ayah(language)
                                val ofWord = if (language == TranslationLanguage.INDONESIAN) "dari" else "of"
                                Text(
                                    text = "$revTitle • $ayahWord $currentVerseNumber $ofWord ${surah.totalVerses}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = primaryColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Icon(
                                    imageVector = Icons.Default.FormatListNumbered,
                                    contentDescription = "Select Verse",
                                    tint = primaryColor,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "▾",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Bookmark Toggle Pill (unclipped and accessible)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isBookmarked) goldColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            border = androidx.compose.foundation.BorderStroke(
                                0.8.dp,
                                if (isBookmarked) goldColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onBookmarkToggled() }
                                .testTag("bookmark_button_${currentVerseNumber}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark Verse",
                                    tint = if (isBookmarked) goldColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                }

                // Right Medallion: Total Verses Count (completely unclipped with clear breathing room)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 6.dp)
                ) {
                    IslamicRosetteMedallion(
                        number = "${surah.totalVerses}",
                        size = 38.dp,
                        primaryColor = goldColor,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                    Text(
                        text = if (language == TranslationLanguage.INDONESIAN) "Ayat" else "Verses",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        fontWeight = FontWeight.Bold,
                        color = goldColor.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * Traditional Bismillah Calligraphic Cartouche with Arabesque side flourishes
 */
@Composable
fun BismillahIlluminatedCartouche(
    modifier: Modifier = Modifier
) {
    val goldColor = MaterialTheme.colorScheme.secondary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            // Center subtle radiant glow
            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        goldColor.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(w / 2f, h / 2f),
                    radius = w * 0.35f
                ),
                size = size
            )

            // Arabesque ornamental horizontal framing lines with floret ends
            val lineY = h / 2f
            val lineMargin = 24.dp.toPx()
            val textGap = 90.dp.toPx()

            // Left decorative line
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, goldColor.copy(alpha = 0.6f))
                ),
                start = Offset(lineMargin, lineY),
                end = Offset(w / 2f - textGap, lineY),
                strokeWidth = 1.2.dp.toPx()
            )

            // Right decorative line
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(goldColor.copy(alpha = 0.6f), Color.Transparent)
                ),
                start = Offset(w / 2f + textGap, lineY),
                end = Offset(w - lineMargin, lineY),
                strokeWidth = 1.2.dp.toPx()
            )

            // Florets at ends
            drawCircle(
                color = goldColor.copy(alpha = 0.7f),
                radius = 2.dp.toPx(),
                center = Offset(w / 2f - textGap, lineY)
            )
            drawCircle(
                color = goldColor.copy(alpha = 0.7f),
                radius = 2.dp.toPx(),
                center = Offset(w / 2f + textGap, lineY)
            )
        }

        Text(
            text = "﷽",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = goldColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

/**
 * Traditional Islamic Manuscript Divider with central 8-pointed star
 */
@Composable
fun IslamicOrnamentalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, color)
                    )
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "۞",
            fontSize = 14.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(color, Color.Transparent)
                    )
                )
        )
    }
}

/**
 * Bottom Tajweed Quick Legend Strip (directly mirroring Screenshot 1)
 */
@Composable
fun TajweedQuickLegendBar(
    onOpenTajweedGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val legendItems = listOf(
        Pair("QALQALAH", com.example.ui.theme.TajweedQalqalahColor),
        Pair("IQLAB", com.example.ui.theme.TajweedIqlabColor),
        Pair("IDGHAM", com.example.ui.theme.TajweedIdghamColor),
        Pair("IKHFA", com.example.ui.theme.TajweedGhunnahColor),
        Pair("GHUNNAH", com.example.ui.theme.TajweedIdghamColor)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onOpenTajweedGuide() },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(
            0.8.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            legendItems.forEach { (name, color) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(color, CircleShape)
                    )
                    Text(
                        text = name,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = color,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

/**
 * Helper to draw traditional arabesque leaf flourish
 */
internal fun DrawScope.drawArabesqueFlourish(color: Color, cx: Float, cy: Float, isTop: Boolean) {
    val path = Path()
    val dir = if (isTop) 1f else -1f
    path.moveTo(cx - 16.dp.toPx(), cy)
    path.cubicTo(
        cx - 8.dp.toPx(), cy + (6.dp.toPx() * dir),
        cx - 2.dp.toPx(), cy + (2.dp.toPx() * dir),
        cx, cy + (7.dp.toPx() * dir)
    )
    path.cubicTo(
        cx + 2.dp.toPx(), cy + (2.dp.toPx() * dir),
        cx + 8.dp.toPx(), cy + (6.dp.toPx() * dir),
        cx + 16.dp.toPx(), cy
    )
    drawPath(
        path = path,
        color = color.copy(alpha = 0.5f),
        style = Stroke(width = 1.dp.toPx())
    )
}

/**
 * Swipe gesture icon matching the requested user reference:
 * A stylized outline hand pointing upward with the index finger, extended thumb,
 * and knuckle curve, flanked on both sides by horizontal arrows pointing left and right.
 */
@Composable
fun StraightSwipeGestureIcon(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = (w * 0.08f).coerceIn(1.2.dp.toPx(), 2.5.dp.toPx())

        val scaleX = w / 100f
        val scaleY = h / 100f

        // Y-level for horizontal arrows
        val arrowY = 28f * scaleY

        // --- 1. Left Arrow (Points Left) ---
        val leftArrowTipX = 6f * scaleX
        val leftArrowBaseX = 22f * scaleX
        val leftArrowStemEndX = 33f * scaleX
        val arrowHalfH = 11f * scaleY

        // Left Triangle Outline (points left)
        val leftTriangle = Path().apply {
            moveTo(leftArrowTipX, arrowY)
            lineTo(leftArrowBaseX, arrowY - arrowHalfH)
            lineTo(leftArrowBaseX, arrowY + arrowHalfH)
            close()
        }
        drawPath(
            path = leftTriangle,
            color = tint,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Left horizontal stem
        drawLine(
            color = tint,
            start = Offset(leftArrowBaseX, arrowY),
            end = Offset(leftArrowStemEndX, arrowY),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // --- 2. Right Arrow (Points Right) ---
        val rightArrowStemStartX = 61f * scaleX
        val rightArrowBaseX = 72f * scaleX
        val rightArrowTipX = 88f * scaleX

        // Right horizontal stem
        drawLine(
            color = tint,
            start = Offset(rightArrowStemStartX, arrowY),
            end = Offset(rightArrowBaseX, arrowY),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Right Triangle Outline (points right)
        val rightTriangle = Path().apply {
            moveTo(rightArrowBaseX, arrowY - arrowHalfH)
            lineTo(rightArrowTipX, arrowY)
            lineTo(rightArrowBaseX, arrowY + arrowHalfH)
            close()
        }
        drawPath(
            path = rightTriangle,
            color = tint,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // --- 3. Hand Contour (Index Finger Up, Thumb to Left, Knuckle Curve to Right) ---
        val handPath = Path().apply {
            // Start at left wrist
            moveTo(41f * scaleX, 94f * scaleY)
            // Left wrist upward
            lineTo(41f * scaleX, 81f * scaleY)
            // Thumb projecting out-up-left
            lineTo(22f * scaleX, 59f * scaleY)
            // Rounded thumb tip
            cubicTo(
                17f * scaleX, 55f * scaleY,
                20f * scaleX, 47f * scaleY,
                27f * scaleX, 52f * scaleY
            )
            // Thumb cleft back to index finger base
            lineTo(40f * scaleX, 64f * scaleY)
            // Index finger left edge straight up
            lineTo(40f * scaleX, 28f * scaleY)
            // Index finger tip (smooth rounded dome)
            cubicTo(
                40f * scaleX, 19f * scaleY,
                54f * scaleX, 19f * scaleY,
                54f * scaleX, 28f * scaleY
            )
            // Index finger right edge straight down
            lineTo(54f * scaleX, 49f * scaleY)
            // Knuckle horizontal step to the right
            cubicTo(
                55f * scaleX, 53f * scaleY,
                61f * scaleX, 53f * scaleY,
                68f * scaleX, 55f * scaleY
            )
            // Curled fingers & outer palm arc
            cubicTo(
                81f * scaleX, 59f * scaleY,
                83f * scaleX, 74f * scaleY,
                78f * scaleX, 83f * scaleY
            )
            // Right wrist edge downward
            cubicTo(
                75f * scaleX, 88f * scaleY,
                71f * scaleX, 91f * scaleY,
                68f * scaleX, 94f * scaleY
            )
        }

        drawPath(
            path = handPath,
            color = tint,
            style = Stroke(
                width = strokeW,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
