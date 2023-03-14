import isel.leic.UsbPort

fun main() {
    val start = HAL.init()
    println(start)
    while (true) {
        HAL.setBits(15)
        Thread.sleep(500)
        HAL.clrBits(3)
        Thread.sleep(500)
        HAL.writeBits(192,64)
        Thread.sleep(500)
        println(HAL.readBits(255))
        Thread.sleep(500)
        HAL.clrBits(255)
        Thread.sleep(500)
        println(HAL.isBit(1))
        Thread.sleep(500)
    }
}
object HAL {
    var val_write = 0
    fun init() {
        val_write = 0
        UsbPort.write(val_write)
    }

    fun readBits(mask: Int): Int = UsbPort.read().and(mask)

    fun isBit(mask: Int): Boolean = readBits(mask).and(mask) > 0

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
