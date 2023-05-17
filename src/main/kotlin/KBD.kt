import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main() {
    while(true) {
        val key = KBD.getKey()
        if(key != KBD.NONE.toChar()) print(key)
    }
    }

/**
 * Manages the keyboard input for the [AccessControlSystem].
 *
 * @property NONE A constant representing no key pressed.
 * @property KVALMASK The bit mask for the key value on the USB input port.
 * @property KACKMASK The bit mask for acknowledging the key input on the USB output port.
 * @property KCODEMASK The bit mask for the key code on the USB input port.
 *
 * @property keycode The current key code.
 * @property keyval A flag indicating if a key is pressed.
 * @property kbdmatrix A list of characters representing the keyboard matrix.
 *
 * @author Bernardo Pereira
 * @author António Paulino
 * @see HAL
 */
object KBD {
    const val NONE = 0

    private const val KVALMASK = 0b00010000

    private const val KACKMASK = 0b01000000

    private const val KCODEMASK = 0b00001111

    private var keycode = NONE

    private var keyval = false

    private val kbdmatrix = listOf('1', '4', '7', '*', '2', '5', '8', '0', '3', '6', '9', '#')

    /**
     * Initializes the keyboard by setting the key valid flag to false and the key code to NONE.
     */
    fun init() {
        keyval = false
        keycode = NONE
    }

    /**
     * Reads the current key from the keyboard.
     *
     * @return The character corresponding to the pressed key, or [NONE] if no key is pressed.
     */
    fun getKey(): Char {

        keyval = HAL.isBit(KVALMASK) // Check if a key is pressed


        if (keyval) {

            keycode = HAL.readBits(KCODEMASK) // Read the key

            val key = if (keycode in kbdmatrix.indices) kbdmatrix[keycode] else NONE.toChar()

            HAL.setBits(KACKMASK) // Send the ack signal flag

            while (keyval) {
                keyval = HAL.isBit(KVALMASK) // Wait for the hardware to lower the key valid flag
            }

            HAL.clrBits(KACKMASK)   // Clear the ack signal flag

            return key

        } else return NONE.toChar()
    }

    /**
     * Waits for a key input until the timeout is reached.
     *
     * @param timeout The timeout period in milliseconds.
     * @return The character corresponding to the pressed key, or [NONE] if no key is pressed within the timeout.
     */
    fun waitKey(timeout: Long): Char {

        val finaltime = System.currentTimeMillis() + timeout

        var key = getKey()

        while (System.currentTimeMillis() < finaltime && key == NONE.toChar()) {
            key = getKey()
        }

        return key
    }
}


