package com.example.tajweed

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.example.model.TajweedRuleType

object TajweedParser {

    private data class OpenTag(
        val ruleType: TajweedRuleType?,
        val startIndex: Int
    )

    /**
     * Parses tagged text with single or nested tags like "<ghunnah>عَمَّ</ghunnah>" or
     * "<tafkhim><qalqalah>قُلْ</qalqalah></tafkhim>" into an AnnotatedString with
     * color styles and rule tags for student learning.
     */
    fun parseTaggedText(
        taggedText: String,
        enableTajweedColoring: Boolean = true
    ): AnnotatedString {
        val sanitizedText = taggedText.replace("ـ", "")

        if (!enableTajweedColoring) {
            return buildAnnotatedString {
                append(cleanTags(sanitizedText))
            }
        }

        return buildAnnotatedString {
            val tagRegex = Regex("""</?([a-zA-Z_]+)>""")
            var cursor = 0
            val tagStack = mutableListOf<OpenTag>()
            val matches = tagRegex.findAll(sanitizedText)

            for (match in matches) {
                val matchStart = match.range.first
                val matchEnd = match.range.last + 1

                // Append any plain text before the tag
                if (matchStart > cursor) {
                    val plainChunk = sanitizedText.substring(cursor, matchStart)
                    append(plainChunk)
                }

                val fullTag = match.value
                val tagName = match.groupValues[1].lowercase()
                val isClosing = fullTag.startsWith("</")

                if (!isClosing) {
                    // Opening tag
                    val rule = getRuleForTag(tagName)
                    tagStack.add(OpenTag(rule, length))
                } else {
                    // Closing tag: match the last open tag with this rule name or any open tag
                    val rule = getRuleForTag(tagName)
                    val openIndex = tagStack.indexOfLast { it.ruleType == rule }
                    val targetOpenTag = if (openIndex != -1) {
                        tagStack.removeAt(openIndex)
                    } else if (tagStack.isNotEmpty()) {
                        tagStack.removeAt(tagStack.size - 1)
                    } else {
                        null
                    }

                    if (targetOpenTag != null && targetOpenTag.ruleType != null) {
                        val start = targetOpenTag.startIndex
                        val end = length
                        if (end > start) {
                            addStyle(
                                style = SpanStyle(
                                    color = targetOpenTag.ruleType.color
                                ),
                                start = start,
                                end = end
                            )
                            addStringAnnotation(
                                tag = "TAJWEED_RULE",
                                annotation = targetOpenTag.ruleType.name,
                                start = start,
                                end = end
                            )
                        }
                    }
                }

                cursor = matchEnd
            }

            // Append any remaining text
            if (cursor < sanitizedText.length) {
                append(sanitizedText.substring(cursor))
            }

            // Clean up any unclosed tags
            while (tagStack.isNotEmpty()) {
                val unclosed = tagStack.removeAt(tagStack.size - 1)
                if (unclosed.ruleType != null && length > unclosed.startIndex) {
                    addStyle(
                        style = SpanStyle(
                            color = unclosed.ruleType.color
                        ),
                        start = unclosed.startIndex,
                        end = length
                    )
                }
            }
        }
    }

    fun cleanTags(text: String): String {
        return text.replace(Regex("""<[^>]*>"""), "").replace("ـ", "")
    }

    private fun getRuleForTag(tag: String): TajweedRuleType? {
        return when (tag.lowercase()) {
            "ghunnah", "ikhfa" -> TajweedRuleType.GHUNNAH
            "qalqalah" -> TajweedRuleType.QALQALAH
            "idgham" -> TajweedRuleType.IDGHAM
            "madd" -> TajweedRuleType.MADD
            "iqlab" -> TajweedRuleType.IQLAB
            "tafkhim" -> TajweedRuleType.TAFKHIM
            "hamzat_wasl", "wasl" -> TajweedRuleType.HAMZAT_WASL
            else -> null
        }
    }
}
