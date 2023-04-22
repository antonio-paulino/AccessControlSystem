object TUI {
    const val COLS = 16
    enum class LINES {First, Second}
    enum class ALIGN {None, Left, Center, Right}
    enum class ENTRY (val len : Int) {None(0), UIN(3), PIN(5)}

    const val KBDTIMEOUT = 5000.toLong()

    fun queryOrWrite(text: String, align: ALIGN, line: LINES, entry: ENTRY = ENTRY.None) : Int? {
        val lineval = line.ordinal + 1
        if (entry == ENTRY.None) {
            val coloffset = getColOffset(align, text)
            write(text, coloffset, lineval)
        }
        else {
            val entrytext = text.extend(entry.len)
            val coloffset = getColOffset(align, entrytext)
            return queryandread(entrytext, lineval, coloffset, entry)
        }
        return null
    }
    private fun write(text: String, colOffset: Int?,  line: Int) {
        if (colOffset != null) LCD.cursor(line, colOffset)
        LCD.write(text)
    }
    private fun getColOffset(align: ALIGN, text: String) : Int? {
        return when (align) {
            ALIGN.None -> null
            ALIGN.Center -> (COLS - text.length) / 2 + 1
            ALIGN.Left -> 1
            ALIGN.Right -> COLS - text.length
        }
    }
    private fun queryandread(text : String, line: Int, colOffset: Int?, entry : ENTRY) : Int? {
        write(text, colOffset, line)
        return read(line, text.length + colOffset!! - entry.len, entry)
    }
    private fun String.extend(length : Int) : String {
        return if (length == 0) this
        else {
            var text = this
            for(i in 0 until length) {
                text += '_'
            }
            text
        }
    }
    private fun read(line: Int, startcol : Int, entry : ENTRY) : Int? {
        var word = ""
        LCD.cursor(line, startcol)
        while (word.length < entry.len) {
            val key = KBD.waitKey(KBDTIMEOUT)
            if (key == KBD.NONE.toChar()) break
            else if (key == '*') {
                if (word.isEmpty()) break
                else clearDigits(line, startcol, entry.len)
                word = ""
            } else {
                word += key
                if (entry == ENTRY.PIN) LCD.write('*') else LCD.write(key)
            }
        }
        return if(word.length != entry.len) null else word.toInt()
    }

    private fun clearDigits(line : Int, col: Int, len : Int) {
        LCD.cursor(line, col)

        for (i in 1 until len){
            LCD.write("_")
        }

        LCD.cursor(line, col)
    }
}


