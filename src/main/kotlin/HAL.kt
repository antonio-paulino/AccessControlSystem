import isel.leic.UsbPort

/**
 * Hardware Abstraction Layer (HAL) for the [AccessControlSystem].
 *
 * Provides an interface to interact with the USB Port for reading and writing bits.
 *
 * @property val_write stores the current value written to the USB port
 *
 * @author Bernardo Pereira
 * @author António Paulino
 */

object HAL {
    var val_write = 0

    /**
     * Initializes the USB Output Port with the value 0
     */
    fun init() {
        val_write = 0
        UsbPort.write(val_write)
    }


    /**
     * Reads the bits from the USB port and applies [mask] to get the requested bits
     * @param mask The bit mask used to extract the requested bits.
     * @return The result of applying the mask to the bits read from the USB port.
     */
    fun readBits(mask: Int): Int = UsbPort.read().and(mask)

    /**
     * Checks if the specified bit by [mask] in the value read from the USB port is set or not.
     * @param mask The bit mask representing the bit.
     * @return true if the bit is set, false if the bit is not set.
     */
    fun isBit(mask: Int): Boolean = readBits(mask) > 0


    /**
     * Sets the specified bits by [mask] and writes them to the USB port
     * @param mask The bit mask representing the bits to be set.
     *
     */
    fun setBits(mask: Int) {
        val_write = (mask.or(val_write))
        UsbPort.write(val_write)
    }

    /**
     * Clears the specified bits by [mask] and writes them to the USB port
     * @param mask The bit mask representing the bits to be cleared.
     *
     */
    fun clrBits(mask: Int) {
        val_write = mask.inv().and(val_write)
        UsbPort.write(val_write)
    }

    /**
     * Writes the specified bits by [mask] with [value] to the USB port
     * @param mask The bit mask representing the bits to be written
     * @param value The value to write on the specified bits
     */
    fun writeBits(mask: Int, value: Int) {
        val_write = mask.inv().and(val_write).or(value.and(mask))
        UsbPort.write(val_write)
    }
}
