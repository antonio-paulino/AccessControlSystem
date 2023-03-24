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
    var keyArr = arrayOf<Char>()
    while (true) {
        val key = KBD.waitKey(5000)
        LCD.write(key)
        if (key == '*') {
            LCD.clear()
            keyArr = arrayOf()
        }
        else if(key != 0.toChar()) keyArr += key
        if (keyArr.size == 8) {
            LCD.clear()
            keyArr = arrayOf()
            LCD.write("Hello World")
            LCD.cursor(2,1)
            LCD.write("Good Morning")
            waitTimeMilli(2000)
            LCD.clear()
        }
    }
}

object LCD {
    private const val LINES = 2
    private const val COLS = 16
    const val RSBITMASK = 16 // Out4
    const val ENMASK = 32 // Out5
    const val SERIALMASK = 16  //I6
    const val DATAMASK = 15 //Out0 - Out3


    private fun Pulse() {
        waitTimeNano(500)
        HAL.setBits(ENMASK)
        waitTimeNano(500)
        HAL.clrBits(ENMASK)
        waitTimeNano(500)
    }

     private fun writeNibbleParallel(rs: Boolean, data: Int) {
        if (!rs) {
            HAL.clrBits(RSBITMASK)
            waitTimeNano(100)
        }
        else {
            HAL.setBits(RSBITMASK)
            waitTimeNano(100)
         }
        HAL.writeBits(DATAMASK,data)
        Pulse()
    }

      private fun writeNibbleSerial(rs: Boolean, data: Int) {
          if (!rs) {
              HAL.clrBits(RSBITMASK)
              waitTimeNano(100)
          }
          else {
              HAL.setBits(RSBITMASK)
              waitTimeNano(100)
          }
        for (i in 3 downTo 0) {
            HAL.writeBits(DATAMASK and (1.shl(i)), data)
            Pulse()
        }
    }

     private fun writeNibble(rs: Boolean, data: Int) {
        if (HAL.isBit(SERIALMASK)) {
            writeNibbleSerial(rs, data)
        } else {
            writeNibbleParallel(rs, data)
        }
    }

     private fun writeByte(rs: Boolean, data: Int) {
        val dataHigh = data.shr(4)
        val dataLow = data.and(15)
        writeNibble(rs, dataHigh)
        writeNibble(rs, dataLow)
    }

    private fun writeCMD(data: Int) {
        writeByte(false,data)
        waitTimeNano(100000)
    }

    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }


    fun init() {
        waitTimeMilli(15)
        writeNibbleParallel(false,0b00000011)
        waitTimeMilli(5)
        writeNibbleParallel(false,0b00000011)
        waitTimeMilli(5)
        writeNibbleParallel(false,0b00000011)
        waitTimeMilli(1)
        writeNibbleParallel(false,0b00000010)

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