import java.io.Serial
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main() {
    HAL.init()
    SerialEmitter.send(SerialEmitter.Destination.LCD, 0x15)
}


/**
 * 22/5/2023
 *
 * Serial emitter for sending data to the door controller or the LCD of the [AccessControlSystem]
 * @property Destination Serial emitter destinations.
 * @author Bernardo Pereira
 * @author António Paulino
 * @see LCD
 * @see DoorMechanism
 * @see HAL
 */
object SerialEmitter {

    enum class Destination(val mask: Int) { LCD(0b00000010), DOOR(0b00000100) }

    /**
     * The wait time in nanoseconds between sending bit signals to the hardware
     */
    private const val DELAY = 200

    /**
     * The bit mask of the SDX output to the USB Port
     */
    private const val SDXMASK = 0b00000001

    /**
     * The bit mask of the SCLK output to the USB Port
     *
     */
    private const val SCLKMASK = 0b10000000

    /**
     * The bit mask of the BUSY input on the USB Port. Represents if the serial receiver is busy.
     *
     */
    private const val BUSYMASK = 0b00100000

    /**
     * Flag that represents if the Serial Receiver is busy.
     */
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

        for (i in 0..4) { //Write frame to LCD
            val sdx = (1.shl(i) and data).shr(i)
            HAL.writeBits(SDXMASK, sdx)
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