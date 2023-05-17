/**
 * Manages the LCD for the [AccessControlSystem], using 4-bit interface.
 * @property LINES the number of lines on the LCD
 * @property COLS the number of columns on the LCD
 * @property RS_BIT_MASK the bit mask of the RS output on the USB output port
 * @property EN_MASK the bit mask of the EN output on the USB output port
 * @property DATA_MASK the bit mask of the DATA input on the USB output port
 * @property PULSE_DELAY the delay in nanoseconds between Enable pulses to the LCD
 * @property RISE_DELAY the delay in nanoseconds for the rise time of the bit signals
 * @property INIT_DELAY the delay between LCD initialization commands.
 * @property CMD_DELAY the delay in milliseconds between LCD commands.
 * @property CMD_NIBBLE A nibble with the RS bit set to 0, to send a command to the LCD
 * @property DATA_NIBBLE a nibble with the RS bit set to 1, to send data to the LCD
 *
 * @author Bernardo Pereira
 * @author António Paulino
 * @see SerialEmitter
 * @see HAL
 */

object LCD {

    const val LINES = 2

    const val COLS = 16

    private const val RS_BIT_MASK = 0b00010000

    private const val EN_MASK = 0b00100000

    private const val DATA_MASK = 0b00001111

    private const val PULSE_DELAY = 500

    private const val RISE_DELAY = 100

    private const val INIT_DELAY = 30

    private const val CMD_DELAY = 2

    private const val CMD_NIBBLE = 0b00000

    private const val DATA_NIBBLE = 0b10000


    /**
     * Sends an EN pulse to the LCD
     */
    private fun pulse() {
        waitTimeNano(PULSE_DELAY)
        HAL.setBits(EN_MASK)
        waitTimeNano(PULSE_DELAY)
        HAL.clrBits(EN_MASK)
        waitTimeNano(PULSE_DELAY)
    }

    /**
     * Sends a command or data nibble (4 bits) to the LCD in parallel mode.
     * @param rs Defines whether to send a command (false) or data (true)
     * @param data Data to send
     */
    private fun writeNibbleParallel(rs: Boolean, data: Int) {
        if (!rs) {
            HAL.clrBits(RS_BIT_MASK)
            waitTimeNano(RISE_DELAY)
        } else {
            HAL.setBits(RS_BIT_MASK)
            waitTimeNano(RISE_DELAY)
        }
        HAL.writeBits(DATA_MASK, data)
        pulse()
    }

    /**
     * Writes a command or data nibble (4 bits) to the LCD in serial mode.
     * @param rs Defines whether to send a command (false) or data (true)
     * @param data Data to send
     */
    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        val datasend = if (rs) DATA_NIBBLE or data else CMD_NIBBLE or data
        SerialEmitter.send(SerialEmitter.Destination.LCD, datasend)
    }


    /**
     * Writes a command or data nibble (4 bits) to the LCD.
     * @param rs Defines whether to send a command (false) or data (true)
     * @param data Data to send
     */
    private fun writeNibble(rs: Boolean, data: Int) {
        // if (HAL.isBit(SERIALMASK)) {
        //     writeNibbleSerial(rs, data)h
        // } else {
        //    writeNibbleParallel(rs, data)
        // }
        writeNibbleSerial(rs, data)
    }


    /**
     * Writes a command or data byte (8 bits) to the LCD by calling [writeNibble] two times. One for the 4 high bits
     * and another for the 4 low bits.
     *
     * The 4 High bits must be sent before the 4 Low bits according to the LCD specifications.
     *
     * @param rs Defines whether to send a command (false) or data (true)
     * @param data Data to send
     */
    private fun writeByte(rs: Boolean, data: Int) {
        val dataHigh = data.shr(4) // 4 high bits
        val dataLow = data.and(0b00001111) // 4 low bits
        writeNibble(rs, dataHigh)
        writeNibble(rs, dataLow)
    }


    /**
     * Writes a command to the LCD by calling [writeByte] with the RS flag set to false.
     * @param data Command data to send
     */
    private fun writeCMD(data: Int) {
        writeByte(false, data)
        waitTimeMilli(CMD_DELAY)
    }


    /**
     * Writes data to the LCD by calling [writeByte] with the RS flag set to true.
     * @param data Data to be sent
     */
    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }


    /**
     * Initializes the LCD for 4 bit communication mode by following the specified LCD initialization sequence.
     *
     * Uses the Function set, Display Control, and Entry Mode set commands.
     *
     * Calls the [writeNibble] and [writeCMD] functions.
     */
    fun init() {
        waitTimeMilli(INIT_DELAY)
        writeNibble(false, 0b00000011)
        waitTimeMilli(INIT_DELAY)
        writeNibble(false, 0b00000011)
        waitTimeMilli(INIT_DELAY)
        writeNibble(false, 0b00000011)
        waitTimeMilli(INIT_DELAY)
        writeNibble(false, 0b00000010)

        writeCMD(0b00101000) // function set: 4-bit mode, 2 lines, 5x8 dots
        writeCMD(0b00001000) // display control : display off, cursor off, blink off
        writeCMD(0b00000001) // clear
        writeCMD(0b00000110) // entry mode set: increment cursor, no display shift


        writeCMD(0b00001100) // display control: display on, cursor off, blink off
    }


    /**
     * Writes a character to the LCD by calling the [writeDATA] function.
     */
    fun write(c: Char) = writeDATA(c.code)

    /**
     * Writes a string to the LCD by calling the [write] function for every character in the string.
     */
    fun write(text: String) {
        for (char in text) {
            write(char)
        }
    }

    /**
     * Positions the cursor on the LCD by using the Write data to CG or DDRAM command.
     *
     * The 7th bit indicates the line position, and the 4 low bits indicate the column position.
     *
     * @param line the line position for the cursor, 1 for the first line, 2 for the second line
     * @param column the column position for the cursor, 1 for the first column, 16 for the last column
     */
    fun cursor(line: Int, column: Int) = writeCMD(0b10000000 or (((line - 1) shl (6) and 0b01000000) + column - 1))

    /**
     * Clears the LCD by using the Clear display command.
     *
     * The 7th bit indicates the line position, and the 4 low bits indicate the column position.
     */
    fun clear() = writeCMD(1)
}