import LCD.COLS

object TUI {


    enum class LINES { First, Second }
    enum class ALIGN { None, Left, Center, Right }
    enum class ENTRY(val len: Int) { UIN(3), PIN(4), MSG(16), USERNAME(16)}

    const val KBDTIMEOUT = 5000.toLong()

    const val TIMEOUTCODE = -1

    const val ABORTCODE = -2

    val CMD_ABORT_CODES = listOf(ABORTCODE, TIMEOUTCODE)


    private fun getColOffset(align: ALIGN, text: String): Int {

        return when (align) {
            ALIGN.None -> 0
            ALIGN.Center -> (COLS - text.length) / 2 + 1
            ALIGN.Left -> 1
            ALIGN.Right -> COLS - text.length
        }

    }

    private fun clearEntryDigits(line: Int, col: Int, len: Int) {

        LCD.cursor(line, col)

        for (i in 0 until len) {
            LCD.write("_")
        }

        LCD.cursor(line, col)

    }

    private fun write(text: String, col: Int, line: LINES) {

        if (col > 0) LCD.cursor(line.ordinal + 1, col)
        LCD.write(text)

    }


    private fun read(line: LINES, startcol: Int, entry: ENTRY): Int {

        val linepos = line.ordinal + 1
        var word = ""

        LCD.cursor(linepos, startcol)

        while (word.length < entry.len) {

            val key = KBD.waitKey(KBDTIMEOUT)

            if (key == KBD.NONE.toChar()) return TIMEOUTCODE

            else if (key == '*') {

                if (word.isEmpty()) return ABORTCODE

                clearEntryDigits(linepos, startcol, entry.len)
                word = ""

            }

            else if (key != '#') {

                word += key

                if (entry == ENTRY.PIN) LCD.write('*')
                else LCD.write(key)

            }

        }

        return word.toInt()
    }

    fun clearline(line: LINES) {
        write("                ", 1, line)
        LCD.cursor(line.ordinal + 1, 1)
    }
    fun writeLine(str : String, align: ALIGN, line: LINES, clear: Boolean = true) {

        if (clear) clearline(line)

        val coloffset = getColOffset(align, str)
        write(str, coloffset, line)

    }

    fun writeLines(line1: String, align1: ALIGN, line2: String, align2: ALIGN, clear: Boolean = true) {

        if (clear) LCD.clear()

        writeLine(line1, align1, LINES.First, false)
        writeLine(line2, align2, LINES.Second, false)

    }

    fun query(text: String, align: ALIGN, line: LINES, entry: ENTRY): Int {

        var entrytext = text
        for (i in 0 until entry.len) { entrytext += '_' }

        val coloffset = getColOffset(align, entrytext)
        clearline(line)
        write(entrytext, coloffset, line)

        return read(line, text.length + coloffset, entry)

    }

}


