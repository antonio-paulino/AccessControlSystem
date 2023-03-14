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
    var keyval = 0
    val kbdmatrix = listOf('1','4','7','*','2','5','8','0','3','6','9','#')

    fun init() {
        keyval = 0
        keycode = NONE
    }

    fun getKey(): Char {
        //kackmask = 128, keyvalmask = 64, keycode mask = 15

        keyval = HAL.readBits(64)
        keycode = HAL.readBits(15)

        if (HAL.isBit(64)) {
            HAL.setBits(128)
            while (HAL.isBit(64)) {
                keyval = HAL.readBits(64)
            }
            HAL.clrBits(128)
            return if (keycode in kbdmatrix.indices) kbdmatrix[keycode] else NONE.toChar()

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

