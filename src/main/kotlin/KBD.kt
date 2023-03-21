fun main(){
    KBD.init()
    while (true){
        print(KBD.waitKey(2000))
    }
}


object KBD {

    const val NONE = 0;
    const val KVALMASK = 64
    const val KACKMASK = 128
    const val KCODEMASK = 15

    var keycode = NONE
    var keyval = false
    val kbdmatrix = listOf('1','4','7','*','2','5','8','0','3','6','9','#')

    fun init() {
        keyval = false
        keycode = NONE
    }

    fun getKey(): Char {

        keyval = HAL.isBit(KVALMASK)
        keycode = HAL.readBits(KCODEMASK)

        if (keyval) {

            val key = if (keycode in kbdmatrix.indices) kbdmatrix[keycode] else NONE.toChar()
            HAL.setBits(KACKMASK)

            while (keyval) {

                keyval = HAL.isBit(KVALMASK)
                Thread.sleep(50)

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


