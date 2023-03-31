fun main() {

}

object SerialEmitter {
    enum class Destination {LCD, DOOR}
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
        waitTimeNano(500)
        var sdx = (1.shl(4) and data).shr(4)
        HAL.writeBits(SDXMASK, sdx.shl(1))
        waitTimeNano(500)
        HAL.setBits(SCLKMASK)
        waitTimeNano(500)
        HAL.clrBits(SCLKMASK)
        waitTimeNano(500)
        for (i in 0 .. 3) {
            sdx = (1.shl(i) and data).shr(i)
            HAL.writeBits(SDXMASK, sdx.shl(1))
            waitTimeNano(500)
            HAL.setBits(SCLKMASK)
            waitTimeNano(500)
            HAL.clrBits(SCLKMASK)
            waitTimeNano(500)
        }
    }
    fun isBusy(): Boolean {
        isbusy = HAL.isBit(BUSYMASK)
        return isbusy
    }
}