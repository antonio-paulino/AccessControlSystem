fun main(){
    KBD.init()
    while (true){
        print(KBD.waitKey(2000))
    }
}


object KBD {

    const val NONE = 0;
    const val KVALMASK = 16
    const val KACKMASK = 64
    const val KCODEMASK = 15
    const val CHECKDELAY = 25

    var keycode = NONE
    var keyval = false
    val kbdmatrix = listOf('1','4','7','*','2','5','8','0','3','6','9','#')

    fun init() {
        keyval = false
        keycode = NONE
    }

    fun getKey(): Char {

        keyval = HAL.isBit(KVALMASK)

        if (keyval) {
            keycode = HAL.readBits(KCODEMASK)

            val key = if (keycode in kbdmatrix.indices) kbdmatrix[keycode] else NONE.toChar()
            HAL.setBits(KACKMASK)

            while (keyval) {

                keyval = HAL.isBit(KVALMASK)
                waitTimeMilli(CHECKDELAY)

            }
            HAL.clrBits(KACKMASK)

            return key

        } else return NONE.toChar()
    }


    fun waitKey(timeout: Long): Char {
        val finaltime = System.currentTimeMillis() + timeout
        var key = getKey()

        while (System.currentTimeMillis() < finaltime && key == NONE.toChar()) {
            key = getKey()
        }

        return key
    }
}


