object TUI {
    const val COLS = 16
    const val LINES = 2
    const val KBDTIMEOUT = 5000.toLong()

    fun writeCenter(text : String, line : Int) {
        val coloffset = (COLS - text.length) / 2 + 1
        LCD.cursor(line, coloffset)
        LCD.write(text)
    }

    fun read(len : Int, pin : Boolean) : Int? {
        var word = ""
        val coloffset = (COLS - len) / 2 + 1
        LCD.cursor(2, coloffset)


        while (word.length < len) {
            val key = KBD.waitKey(KBDTIMEOUT)

            if (key == 0.toChar()) break

            else if(key == '*') {

                if (word.isEmpty()) break

                else clearDigits(coloffset, len)

                word = ""
            }
            else {
                word += key
                if (pin) LCD.write('*') else LCD.write(key)
            }
        }
        return if(word.length != len) null else word.toInt()
    }
}

private fun clearDigits(coloffset: Int, len : Int) {
    LCD.cursor(2, coloffset)

    for (i in 1..len){
        LCD.write(" ")
    }

    LCD.cursor(2, coloffset)
}