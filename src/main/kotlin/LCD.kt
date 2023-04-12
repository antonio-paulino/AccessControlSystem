fun main() {
    LCD.init()
    KBD.init()
    for (col in 1..16) {
        LCD.cursor(1,col)
        waitTimeMilli(50)
    }
    for (col in 1..16) {
        LCD.cursor(2,col)
        waitTimeMilli(50)
    }
    LCD.clear()
    while (true) {
        TUI.writeCenter("Insira o UIN", 1)
        val UIN = TUI.read(3, false)
        LCD.clear()
        TUI.writeCenter("Insira o PIN", 1)
        val PIN = TUI.read(5, true)
        LCD.clear()
    }
}

object LCD {
    private const val LINES = 2
    private const val COLS = 16
    const val RSBITMASK = 16 // Out4
    const val ENMASK = 32 // Out5
    const val SERIALMASK = 128 //O7
    const val DATAMASK = 15 //Out0 - Out3
    const val PULSEDELAY = 500
    const val RISEDELAY = 100
    const val INITDELAY = 30
    const val CMDDELAY = 2

    private fun Pulse() {
        waitTimeNano(PULSEDELAY)
        HAL.setBits(ENMASK)
        waitTimeNano(PULSEDELAY)
        HAL.clrBits(ENMASK)
        waitTimeNano(PULSEDELAY)
    }

    private fun writeNibbleParallel(rs: Boolean, data: Int) {
        if (!rs) {
            HAL.clrBits(RSBITMASK)
            waitTimeNano(RISEDELAY)
        }
        else {
            HAL.setBits(RSBITMASK)
            waitTimeNano(RISEDELAY)
        }
        HAL.writeBits(DATAMASK,data)
        Pulse()
    }

    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        val datasend = if(rs) 0b10000 or data else 0b00000 or data
        SerialEmitter.send(SerialEmitter.Destination.LCD, datasend)
    }

    private fun writeNibble(rs: Boolean, data: Int) {
       // if (HAL.isBit(SERIALMASK)) {
       //     writeNibbleSerial(rs, data)h
       // } else {
       //    writeNibbleParallel(rs, data)
       // }
        writeNibbleSerial(rs,data)
    }

    private fun writeByte(rs: Boolean, data: Int) {
        val dataHigh = data.shr(4)
        val dataLow = data.and(15)
        writeNibble(rs, dataHigh)
        writeNibble(rs, dataLow)
    }

    private fun writeCMD(data: Int) {
        writeByte(false,data)
        waitTimeMilli(CMDDELAY)
    }

    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }


    fun init() {
        waitTimeMilli(INITDELAY)
        writeNibble(false,0b00000011)
        waitTimeMilli(INITDELAY/2)
        writeNibble(false,0b00000011)
        waitTimeMilli(INITDELAY/2)
        writeNibble(false,0b00000011)
        waitTimeMilli(INITDELAY/6)
        writeNibble(false,0b00000010)

        writeCMD(0b00101000) // function set: 4-bit mode, 2 lines, 5x8 dots
        writeCMD(0b00001000) // display control : display off, cursor off, blink off
        writeCMD(0b00000001) // clear
        writeCMD(0b00000110) // entry mode set: increment cursor, no display shift


        writeCMD(0b00001111) // display control: display on, cursor on, blink on
        clear()

    }

    fun write(c: Char)  = writeDATA(c.code)

    fun write(text: String) { for(char in text) { write(char) } }


    fun cursor(line: Int, column: Int) = writeCMD(0b10000000 or (((line-1)shl(6) and 0b01000000) + column - 1))
    fun clear() = writeCMD(1) // clear display

}