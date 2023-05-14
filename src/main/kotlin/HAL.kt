import isel.leic.UsbPort

object HAL {
    var val_write = 0
    fun init() {
        val_write = 0
        UsbPort.write(val_write)
    }

    fun readBits(mask: Int): Int = UsbPort.read().and(mask)

    fun isBit(mask: Int): Boolean = readBits(mask) > 0

    fun setBits(mask: Int) {
        val_write = (mask.or(val_write))
        UsbPort.write(val_write)
    }

    fun clrBits(mask: Int) {
        val_write = mask.inv().and(val_write)
        UsbPort.write(val_write)
    }

    fun writeBits(mask: Int, value: Int) {
        val_write = mask.inv().and(val_write).or(value.and(mask))
        UsbPort.write(val_write)
    }
}
