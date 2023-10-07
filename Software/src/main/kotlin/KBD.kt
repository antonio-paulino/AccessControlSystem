fun main() {
    while (true) {
        val key = KBD.getKey()
        if (key != KBD.NONE.toChar()) print(key)
    }
}

/**
 * 22/5/2023
 *
 * Manages the keyboard input for the [AccessControlSystem].
 *
 * @property NONE A constant representing no key pressed. Returned if there was no key pressed
 * in the [getKey] and [waitKey] functions.
 *
 * @author Bernardo Pereira
 * @author António Paulino
 * @see HAL
 */
object KBD {
    const val NONE = 0

    /**
     * The bit mask for the key valid flag on the Usb input Port
     */
    private const val KVALMASK = 0b00010000

    /**
     * The bit mask for acknowledging the key input on the USB output port.
     */
    private const val KACKMASK = 0b01000000

    /**
     * The bit mask for the key code on the USB input port.
     */
    private const val KCODEMASK = 0b00001111

    /**
     * Stores the current key value.
     */
    private var keycode = NONE

    /**
     * Represents if there is a valid key to be read.
     */
    private var keyval = false

    /**
     * List of characters representing the keyboard matrix.
     */
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
     * This function sleeps for 50 ms after every key press check if there is no key pressed.
     * Since the system will spend most of its time waiting for key presses by the user,
     * this allows the system to use less resources.
     *
     * @param timeout The timeout period in milliseconds.
     * @return The character corresponding to the pressed key, or [NONE] if no key is pressed within the timeout.
     */
    fun waitKey(timeout: Long): Char {

        val finaltime = System.currentTimeMillis() + timeout

        var key = getKey()

        while (System.currentTimeMillis() < finaltime && key == NONE.toChar()) {
            key = getKey()
            if (key == NONE.toChar()) waitTimeMilli(CHECK_INTERVAL) //wait if there was no key pressed
        }

        return key
    }
}


