fun main() {
    LCD.init()
    KBD.init()
    while (true) {
        val key = KBD.waitKey(2000)
        LCD.write(key)
        println(key)
    }

}

object LCD {
    private const val LINES = 2
    private const val COLS = 16
    const val RSBITMASK = 16 // Out4
    const val ENMASK = 32 // Out5
    const val SERIALMASK = 64  //I6
    const val DATAMASK = 15 //Out0 - Out3

     private fun waitTimeNano(time : Int) {
         val endtime = System.nanoTime() + time
         while (System.nanoTime() <= endtime) { }
     }
    private fun waitTimeMilli(time : Int) {
        val endtime = System.currentTimeMillis() + time
        while (System.currentTimeMillis() <= endtime) { }
    }
     private fun writeNibbleParallel(rs: Boolean, data: Int) {
        if (!rs) {
            HAL.clrBits(RSBITMASK)
            waitTimeNano(250)
        }
        else {
            HAL.setBits(RSBITMASK)
            waitTimeNano(250)
         }
        HAL.writeBits(DATAMASK,data)
        HAL.setBits(ENMASK)
        waitTimeNano(500)
        HAL.clrBits(ENMASK)
        waitTimeNano(500)
    }

      private fun writeNibbleSerial(rs: Boolean, data: Int) {
          if (!rs) {
              HAL.clrBits(RSBITMASK)
              waitTimeNano(250)
          }
          else {
              HAL.setBits(RSBITMASK)
              waitTimeNano(250)
          }
        for (i in 0..3) {
            HAL.writeBits(DATAMASK and (1 shl (3 - i)), data)
            HAL.setBits(ENMASK)
            waitTimeNano(500)
            HAL.clrBits(ENMASK)
            waitTimeNano(500)
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
        //  println(Integer.toBinaryString(dataHigh))
        val dataLow = data.and(15)
        // println(Integer.toBinaryString(dataLow))
        writeNibble(rs, dataHigh)
        writeNibble(rs, dataLow)
    }

    private fun writeCMD(data: Int) {
        writeByte(false,data)
        waitTimeMilli(1)
    }

    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }


    fun init() {
        waitTimeMilli(50)
        writeNibbleParallel(false,0b00000011)
        waitTimeMilli(15)
        writeNibbleParallel(false,0b00000011)
        waitTimeMilli(10)
        writeNibbleParallel(false,0b00000011)
        waitTimeMilli(5)
        writeNibbleParallel(false,0b00000010)

        writeCMD(0b00101000) // function set: 4-bit mode, 2 lines, 5x8 dots
        writeCMD(0b00001000) // display control : display off, cursor off, blink off
        writeCMD(0b00000001) // clear
        writeCMD(0b00000110) // entry mode set: increment cursor, no display shift


        writeCMD(0b00001110) // display control: display on, cursor on, blink off
        clear()
    }

    fun write(c: Char)  {
        val datalow = when(c) {
            in '0'..'9' -> c - '0'
            '#' -> 3
            '*' -> 10
            else -> 0
        }
        val datahigh = when (c) {
            in '0'..'9' -> 3
            else -> 2
        }
        writeDATA(datahigh.shl(4).or(datalow))
    }

    fun write(text: String) { for(char in text) { write(char) } }


    fun cursor(line: Int, column: Int) {

    }

    fun clear() {
        writeCMD(1) // clear display
    }
}