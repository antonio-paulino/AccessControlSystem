object TUI {
    const val COLS = 16

    enum class LINES { First, Second }
    enum class ALIGN { None, Left, Center, Right }
    enum class ENTRY(val len: Int) { UIN(3), PIN(4), MSG(16), USERNAME(16)}

    const val KBDTIMEOUT = 5000.toLong()
    const val TIMEOUTCODE = -1
    const val ABORTCODE = -2


    fun write(text: String, align: ALIGN, line: LINES) {
        val lineval = line.ordinal + 1
        val coloffset = getColOffset(align, text)
        write(text, coloffset, lineval)
    }

    fun query(text: String, align: ALIGN, line: LINES, entry: ENTRY): Int {
        val lineval = line.ordinal + 1
        val entrytext = text.extend(entry.len)
        val coloffset = getColOffset(align, entrytext)
        return queryandread(entrytext, lineval, coloffset, entry)
    }

    fun clearline(line: LINES) {

        write("                ", ALIGN.Left, line)
    }

    private fun write(text: String, colOffset: Int?, line: Int) {
        if (colOffset != null) LCD.cursor(line, colOffset)
        LCD.write(text)
    }

    private fun getColOffset(align: ALIGN, text: String): Int? {
        return when (align) {
            ALIGN.None -> null
            ALIGN.Center -> (COLS - text.length) / 2 + 1
            ALIGN.Left -> 1
            ALIGN.Right -> COLS - text.length
        }
    }

    private fun queryandread(text: String, line: Int, colOffset: Int?, entry: ENTRY): Int {
        write(text, colOffset, line)
        return if (colOffset != null) read(line, text.length + colOffset - entry.len, entry)
        else read(line, text.length - entry.len, entry)
    }




    private fun String.extend(length: Int): String {
        if (length == 0) return this
        else {
            var text = this
            for (i in 0 until length) {
                text += '_'
            }
            return text
        }
    }

    private fun read(line: Int, startcol: Int, entry: ENTRY): Int {
        var word = ""
        LCD.cursor(line, startcol)
        while (word.length < entry.len) {

            val key = KBD.waitKey(KBDTIMEOUT)

            if (key == KBD.NONE.toChar()) return TIMEOUTCODE

            else if (key == '*') {
                if (word.isEmpty()) return ABORTCODE
                else clearDigits(line, startcol, entry.len)
                word = ""
            }

            else if (key != '#') {
                word += key
                if (entry == ENTRY.PIN) LCD.write('*') else LCD.write(key)
            }

        }
        return word.toInt()
    }

    private fun clearDigits(line: Int, col: Int, len: Int) {
        LCD.cursor(line, col)

        for (i in 1 until len) {
            LCD.write("_")
        }

        LCD.cursor(line, col)
    }
}


