import isel.leic.utils.Time

fun main(){
    KBD.init()
    while (true){
        print(KBD.waitKey(2000))
    }
}


object KBD {
    const val NONE = 0;

    var keycode = NONE
    var keyval = false
    val kbdmatrix = listOf('1','4','7','*','2','5','8','0','3','6','9','#')

    fun init() {
        keyval = false
        keycode = NONE
    }

    fun getKey(): Char {

        val kvalmask = 64
        val kackmask = 128
        val kcodemask = 15
        keyval = HAL.isBit(kvalmask)
        keycode = HAL.readBits(kcodemask)

        if (keyval) {
            val key = if (keycode in kbdmatrix.indices) kbdmatrix[keycode] else NONE.toChar()
            HAL.setBits(kackmask)
            while (keyval) {
                keyval = HAL.isBit(kvalmask)
                Thread.sleep(10)
            }
            HAL.clrBits(kackmask)
            return key

        } else return NONE.toChar()
    }


    fun waitKey(timeout: Long): Char {

        val starttime = System.currentTimeMillis()

        while (System.currentTimeMillis()  < timeout + starttime) {
            val key = getKey()
            if (key != NONE.toChar()) return key
        }
        return NONE.toChar()
    }
}

