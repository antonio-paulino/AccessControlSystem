import java.io.Serial
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main() {
    HAL.init()
    SerialEmitter.send(SerialEmitter.Destination.LCD, 0x15)
}


/**
 * Serial emitter for sending data to the door controller or the LCD
 * @property DELAY The wait time between sending bit signals to the hardware.
 * @property SDXMASK the bit mask of SDX output to the USB Port
 * @property SCLKMASK the bit mask of SCLK output to the USB Port
 * @property BUSYMASK the bit mask of BUSY input on the USB Port
 * @property isbusy variable to store whether the Serial Receiver is busy or not
 * @author Bernardo Pereira
 * @author António Paulino
 * @see LCD
 * @see DoorMechanism
 * @see HAL
 */
object SerialEmitter {


    /**
     * Serial emitter destinations, [mask] is the bit mask for the selector of the serial receivers.
     */
    enum class Destination(val mask: Int) { LCD(0b00000001), DOOR(0b00000100) }

    private const val DELAY = 200
    private const val SDXMASK = 0b00000010
    private const val SCLKMASK = 0b10000000
    private const val BUSYMASK = 0b00100000
    private var isbusy = false


    /**
     * Initializes the Door Mechanism Control and LCD Serial Receivers by deselecting them. Sets [isbusy] to false.
     */
    fun init() {
        isbusy = false

        HAL.setBits(Destination.LCD.mask)
        HAL.setBits(Destination.DOOR.mask)

        waitTimeNano(DELAY)
    }

    /**
     * Sends an SCLK pulse to the hardware through the USB Port output port
     */
    private fun clkPulse() {
        waitTimeNano(DELAY)

        HAL.setBits(SCLKMASK)
        waitTimeNano(DELAY)

        HAL.clrBits(SCLKMASK)
        waitTimeNano(DELAY)
    }

    /**
     * Sends data to the destination Serial Receiver according to the serial communication protocol for the [AccessControlSystem]
     * @param addr The destination
     * @param data The data to send
     */
    fun send(addr: Destination, data: Int) {

        HAL.clrBits(addr.mask) //Select the destination Serial Receiver
        waitTimeNano(DELAY)

        HAL.writeBits(SDXMASK, data.shr(4).shl(1)) //Initially write the RS/OC bit
        clkPulse()

        for (i in 0..3) { //Write the velocity/data bits
            val sdx = (1.shl(i) and data).shr(i)
            HAL.writeBits(SDXMASK, sdx.shl(1))
            clkPulse()
        }

        HAL.setBits(addr.mask) //Deselect the destination Serial Receiver

        waitTimeNano(DELAY)
    }

    /**
     * Checks if the Door Serial Receiver is busy
     * @return true if the Door Serial Receiver is busy, false otherwise
     */
    fun isBusy(): Boolean {

        isbusy = HAL.isBit(BUSYMASK)
        return isbusy

    }

}