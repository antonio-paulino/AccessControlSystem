import isel.leic.utils.Time

fun main(){
    KBD.init()
    while (true){
        println(KBD.waitKey(2000))
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
        //kackmask = 128, keyvalmask = 64, keycode mask = 15

        keyval = HAL.isBit(64)
        keycode = HAL.readBits(15)

        if (keyval) {
            val key = if (keycode in kbdmatrix.indices) kbdmatrix[keycode] else NONE.toChar()
            HAL.setBits(128)
            while (keyval) { 
                keyval = HAL.isBit(64)
                Thread.sleep(100)
            }
            HAL.clrBits(128)
            return key

        } else return NONE.toChar()
    }


    fun waitKey(timeout: Long): Char {

        val starttime = System.currentTimeMillis()

        while (System.currentTimeMillis() - starttime < timeout) {
            val key = getKey()
            if (key != NONE.toChar()) return key
        }
        return NONE.toChar()
    }
}

