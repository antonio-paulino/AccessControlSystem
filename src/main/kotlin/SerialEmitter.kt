import java.io.Serial
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main() { HAL.init()
    SerialEmitter.send(SerialEmitter.Destination.LCD, 0x15)
}

object SerialEmitter {

    enum class Destination(val mask : Int) {LCD(1), DOOR(4)}
    const val DELAY = 200
    const val SDXMASK = 2
    const val SCLKMASK = 128
    const val BUSYMASK = 32
    var isbusy = false


    fun init() {
        isbusy = false
        HAL.setBits(Destination.LCD.mask)
        HAL.setBits(Destination.DOOR.mask)
        waitTimeNano(DELAY)
    }

    private fun clkPulse () {
        waitTimeNano(DELAY)
        HAL.setBits(SCLKMASK)
        waitTimeNano(DELAY)
        HAL.clrBits(SCLKMASK)
        waitTimeNano(DELAY)
    }

    fun send(addr: Destination, data: Int) {
            HAL.clrBits(addr.mask)
            waitTimeNano(DELAY)
            HAL.writeBits(SDXMASK, data.shr(4).shl(1))
            clkPulse()
            for (i in 0 .. 3) {
                val sdx = (1.shl(i) and data).shr(i)
                HAL.writeBits(SDXMASK, sdx.shl(1))
                clkPulse()
            }
            HAL.setBits(addr.mask)
            waitTimeNano(DELAY*100)
    }


    fun isBusy(): Boolean {
        isbusy = HAL.isBit(BUSYMASK)
        return isbusy
    }

}