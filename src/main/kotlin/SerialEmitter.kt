import java.io.Serial

fun main() { HAL.init()
    SerialEmitter.send(SerialEmitter.Destination.LCD, 0x15)
}

object SerialEmitter {

    enum class Destination {LCD, DOOR}
    const val DELAY = 100
    const val LCDSELMASK = 1
    const val SDXMASK = 2
    const val SCLKMASK = 128
    const val BUSYMASK = 32
    var isbusy = false
    fun init() {
        isbusy = false
    }
    fun send(addr: Destination, data: Int) {
        HAL.writeBits(LCDSELMASK, addr.ordinal)
        waitTimeNano(DELAY)
        for (i in 4 downTo 0) {
            val sdx = (1.shl(i) and data).shr(i)
            HAL.writeBits(SDXMASK, sdx.shl(1))
            waitTimeNano(DELAY)
            HAL.setBits(SCLKMASK)
            waitTimeNano(DELAY)
            HAL.clrBits(SCLKMASK)
        }
    }
    fun isBusy(): Boolean {
        isbusy = HAL.isBit(BUSYMASK)
        return isbusy
    }
}