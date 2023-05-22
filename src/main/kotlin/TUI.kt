import LCD.COLS

/**
 * 22/5/2023
 *
 * Text User Interface (TUI) for displaying and interacting with text on an LCD screen.
 * Provides functionality for writing and querying text on the LCD with line selection and alignment type.
 *
 * @property KBDTIMEOUT the max amount of time to wait for a user input. Used when querying for information.
 * @property TIMEOUTCODE value to return when [KBDTIMEOUT] is reached during a query.
 * @property ABORTCODE value to return when a query operation is aborted.
 * @property CMD_ABORT_CODES list of possible abort codes for query operations.
 *
 * @author Bernardo Pereira
 * @author António Paulino
 * @see LCD
 */
object TUI {


    /**
     * The LCD lines.
     *
     */
    enum class LINES { First, Second }

    /**
     * The text alignment type. None means keep writing without repositioning the cursor
     */
    enum class ALIGN { None, Left, Center, Right }

    /**
     * Refers to a specific type of information to display on the LCD and its length.
     * [UIN] and [PIN] refer to the length of the input, while [MSG] and [USERNAME] refer to the max amount of characters
     * that can be displayed on the LCD.
     *
     * @see User
     */
    enum class ENTRY(val len: Int) { UIN(3), PIN(4), MSG(16), USERNAME(16) }

    const val KBDTIMEOUT = 5000.toLong()

    const val TIMEOUTCODE = -1

    const val ABORTCODE = -2

    val CMD_ABORT_CODES = listOf(ABORTCODE, TIMEOUTCODE)


    /**
     * Calculates the LCD start column position based on text length and alignment type.
     * @param alignment the alignment type
     * @param text the text to write on the LCD
     *
     * @return the column position
     */
    private fun getColOffset(align: ALIGN, text: String): Int {

        return when (align) {
            ALIGN.None -> 0
            ALIGN.Center -> (COLS - text.length) / 2 + 1
            ALIGN.Left -> 1
            ALIGN.Right -> COLS - text.length + 1
        }

    }


    /**
     * Rewrites the input field on the LCD when querying. Used for clearing the input field.
     * @param line The line to re-write on
     * @param column The first column to re-write
     * @param len The number of columns to re-write
     */
    private fun clearEntryDigits(line: Int, col: Int, len: Int) {

        LCD.cursor(line, col)

        for (i in 0 until len) {
            LCD.write("_") //writes '_' on the input field of the LCD.
        }

        LCD.cursor(line, col)

    }


    /**
     * Writes the given text on the LCD.
     * @param text The text to write
     * @param col The starting col position
     * @param line the line to write on
     */
    private fun write(text: String, col: Int, line: LINES) {

        if (col > 0) LCD.cursor(line.ordinal + 1, col) //reposition the cursor if the type of alignment is not none
        LCD.write(text)

    }


    /**
     * Reads user input from the LCD.
     * @param line The line where the input is on
     * @param col The starting col position of the input
     * @param entry The type of entry (UIN/PIN) to determine length.
     * @return The user input as integer, [TIMEOUTCODE] if the timeout is reached, or [ABORTCODE] if the command is aborted.
     */
    private fun read(line: LINES, startcol: Int, entry: ENTRY): Int {

        val linepos = line.ordinal + 1
        var userInput = "" //stores the user input

        LCD.cursor(linepos, startcol)   //position the cursor at the start of the input

        while (userInput.length < entry.len) {   //get the user input

            val key = KBD.waitKey(KBDTIMEOUT)

            if (key == KBD.NONE.toChar()) return TIMEOUTCODE  //abort if no key is pressed

            else if (key == '*') {

                if (userInput.isEmpty()) return ABORTCODE    //abort if the input field is empty

                clearEntryDigits(linepos, startcol, entry.len) //if the input field is not empty clear the input field
                userInput = ""

            } else if (key != '#') {

                userInput += key

                if (entry == ENTRY.PIN) LCD.write('*') //show the user input on the input field of the LCD. If the entry is PIN don't show it.
                else LCD.write(key)

            }

        }

        return userInput.toInt()
    }

    /**
     * Clears a line on the LCD
     * @param line the line to clear
     */
    private fun clearline(line: LINES) {
        write("                ", 1, line)
        LCD.cursor(line.ordinal + 1, 1)
    }


    /**
     * Writes a line on the LCD. Gets the column offset needed to align the text
     * and writes it on the LCD using [write]. Clears the line on the LCD before writing by default.
     * @param str the string to be written
     * @param align the alignment type
     * @param line the line to write on
     * @param clear whether to clear the line before writing. Default is yes but can be user set.
     *
     */
    fun writeLine(str: String, align: ALIGN, line: LINES, clear: Boolean = true) {
        if (clear) clearline(line)
        val coloffset = getColOffset(align, str)
        write(str, coloffset, line)
    }

    /**
     * Writes two lines on the LCD by calling [writeLine] two times.
     * @param str1 the string to write on the first line
     * @param str2 the string to write on the second line
     * @param align1 the alignment type for the first line
     * @param align2 the alignment type for the second line
     * @param clear whether to clear both lines before writing. Default is yes but can be user set.
     * If one line should be cleared and the other should not, use [writeLine] instead.
     */
    fun writeLines(str1: String, align1: ALIGN, str2: String, align2: ALIGN, clear: Boolean = true) {

        if (clear) LCD.clear()

        writeLine(str1, align1, LINES.First, false)
        writeLine(str2, align2, LINES.Second, false)

    }

    /**
     * Queries the user for an input using the LCD. Calls [read] to get the user input.
     * @param text the text prompt to be displayed
     * @param align the alignment of the text prompt
     * @param line the line to display the prompt on
     * @param line the type of input entry
     * @return The user input as integer, [TIMEOUTCODE] if the timeout is reached, or [ABORTCODE] if the command is aborted.
     */
    fun query(text: String, align: ALIGN, line: LINES, entry: ENTRY): Int {

        var entrytext = text
        for (i in 0 until entry.len) {
            entrytext += '_'
        }   //creates the input field of the given entry

        val coloffset = getColOffset(align, entrytext)
        clearline(line)
        write(entrytext, coloffset, line)


        return read(line, text.length + coloffset, entry)

    }

}


