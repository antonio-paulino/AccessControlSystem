fun main() {
    HAL.init()
    SerialEmitter.init()
    LCD.init()
    KBD.init()
    Users.init()
    while (true) {
        M.init()
        TUI.write(LogFile.getDate(), TUI.ALIGN.Center, TUI.LINES.First)
        val UIN = TUI.query("UIN:", TUI.ALIGN.Center, TUI.LINES.Second, TUI.ENTRY.UIN)
        val abortErrors = listOf(TUI.ABORTCODE, TUI.TIMEOUTCODE)
        val user = if (UIN !in abortErrors) Users.userlist[UIN] else null

        if (user != null) {
            var PIN = -999
            var attempts = 1
            while (attempts <= 3 && PIN != user.pin) {
                PIN = TUI.query("PIN:", TUI.ALIGN.Center, TUI.LINES.Second, TUI.ENTRY.PIN)
                if (PIN == user.pin) {
                    LogFile.add(UIN)
                    LCD.clear()
                    TUI.write("Hello", TUI.ALIGN.Center, TUI.LINES.First)
                    TUI.write(user.username, TUI.ALIGN.Center, TUI.LINES.Second)
                    waitTimeMilli(2000)
                    DoorMechanism.open(5)
                    TUI.write("Porta a abrir", TUI.ALIGN.Center, TUI.LINES.First)
                    while (!DoorMechanism.finished());
                    waitTimeMilli(5000)
                    DoorMechanism.close(1)
                    TUI.write("Porta a fechar", TUI.ALIGN.Center, TUI.LINES.First)
                    while (!(DoorMechanism.finished()));
                    break
                } else {
                    if (PIN in abortErrors) break
                    TUI.write("Failed login ($attempts)", TUI.ALIGN.Center, TUI.LINES.Second)
                    waitTimeMilli(2000)
                    TUI.clearline(TUI.LINES.Second)
                    attempts++
                    if (attempts > 3) {
                        TUI.write("Too many tries", TUI.ALIGN.Center, TUI.LINES.Second)
                        waitTimeMilli(5000)
                    }
                }
            }

        } else if (UIN !in abortErrors) {
            TUI.write("INVALID USER", TUI.ALIGN.Center, TUI.LINES.Second)
            waitTimeMilli(5000)
        }

        TUI.clearline(TUI.LINES.Second)
    }
}

object LCD {
    private const val LINES = 2

    private const val COLS = 16

    private const val RSBITMASK = 16 // Out4

    private const val ENMASK = 32 // Out5

    private const val DATAMASK = 15 //Out0 - Out3

    private const val PULSEDELAY = 500

    private const val RISEDELAY = 100

    private const val INITDELAY = 30

    private const val CMDDELAY = 2

    private const val WRITEDELAY = 20

    private const val CMDNIBBLE = 0b00000

    private const val DATANIBBLE = 0b10000

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
        } else {
            HAL.setBits(RSBITMASK)
            waitTimeNano(RISEDELAY)
        }
        HAL.writeBits(DATAMASK, data)
        Pulse()
    }

    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        val datasend = if (rs) DATANIBBLE or data else CMDNIBBLE or data
        SerialEmitter.send(SerialEmitter.Destination.LCD, datasend)
    }

    private fun writeNibble(rs: Boolean, data: Int) {
        // if (HAL.isBit(SERIALMASK)) {
        //     writeNibbleSerial(rs, data)h
        // } else {
        //    writeNibbleParallel(rs, data)
        // }
        writeNibbleSerial(rs, data)
    }

    private fun writeByte(rs: Boolean, data: Int) {
        val dataHigh = data.shr(4)
        val dataLow = data.and(15)
        writeNibble(rs, dataHigh)
        writeNibble(rs, dataLow)
    }

    private fun writeCMD(data: Int) {
        writeByte(false, data)
        waitTimeMilli(CMDDELAY)
    }

    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }


    fun init() {
        waitTimeMilli(INITDELAY)
        writeNibble(false, 0b00000011)
        waitTimeMilli(INITDELAY)
        writeNibble(false, 0b00000011)
        waitTimeMilli(INITDELAY)
        writeNibble(false, 0b00000011)
        waitTimeMilli(INITDELAY)
        writeNibble(false, 0b00000010)

        writeCMD(0b00101000) // function set: 4-bit mode, 2 lines, 5x8 dots
        writeCMD(0b00001000) // display control : display off, cursor off, blink off
        writeCMD(0b00000001) // clear
        writeCMD(0b00000110) // entry mode set: increment cursor, no display shift


        writeCMD(0b00001100) // display control: display on, cursor off, blink off
    }

    fun write(c: Char) = writeDATA(c.code)

    fun write(text: String) {
        for (char in text) {
            write(char)
            waitTimeMilli(WRITEDELAY)
        }
    }

    fun cursor(line: Int, column: Int) = writeCMD(0b10000000 or (((line - 1) shl (6) and 0b01000000) + column - 1))
    fun clear() = writeCMD(1) // clear display
}