package zxc.mrdrag0nxyt.nightJoiner.util

import java.util.regex.Pattern

const val COLOR_CHAR = '§'
val HEX_PATTERN = Pattern.compile("&#([a-fA-F\\d]{6})")

fun isValidColorCharacter(char: Char): Boolean = when (char) {
    in 'a'..'f',
    in 'A'..'F',
    in '0'..'9',
    'r', 'R', 'k', 'K', 'l', 'L', 'm', 'M', 'n', 'N', 'o', 'O', 'x', 'X' -> true

    else -> false
}

fun translateAlternateColorCodes(altColorChar: Char, textToTranslate: String): String {
    val b = textToTranslate.toCharArray()

    var i = 0
    while (i < b.size - 1) {
        if (b[i] == altColorChar && isValidColorCharacter(b[i + 1])) {
            b[i] = COLOR_CHAR
            b[i + 1] = b[i + 1].lowercaseChar()

            i++
        }
        i++
    }

    return String(b)
}

fun colorize(str: String): String {
    if (str.isBlank()) return str

    var output = str
    val matcher = HEX_PATTERN.matcher(str)
    val stringBuilder = StringBuilder()

    while (matcher.find()) {
        val group = matcher.group(1)

        matcher.appendReplacement(
            stringBuilder,
            COLOR_CHAR + "x" +
                    COLOR_CHAR + group[0] +
                    COLOR_CHAR + group[1] +
                    COLOR_CHAR + group[2] +
                    COLOR_CHAR + group[3] +
                    COLOR_CHAR + group[4] +
                    COLOR_CHAR + group[5]
        )
    }

    output = matcher.appendTail(stringBuilder).toString()
    return translateAlternateColorCodes('&', output)
}