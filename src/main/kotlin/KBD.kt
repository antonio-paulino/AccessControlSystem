import isel.leic.utils.Time

fun main(){
    KBD.init()
    while (true){
        println(KBD.waitKey(2000))
    }
}


const val NONE = 0;
const val kackmask = 128
const val keyvalmask = 64
const val keycodemask = 15

var keycode = NONE
var keyval = 0
val kbdmatrix = listOf('1','4','7','*','2','5','8','0','3','6','9','#')

object KBD {
    fun init() {
        keyval = 0
        keycode = NONE
    }

    fun getKey(): Char {

        keyval = HAL.readBits(keyvalmask)
        keycode = HAL.readBits(keycodemask)

        if (HAL.isBit(keyvalmask)) {

            HAL.setBits(kackmask) //Set kack to true
            while (HAL.isBit(keyvalmask)) {
                keyval = HAL.readBits(keyvalmask)
            }
            HAL.clrBits(kackmask) //Set kack to false after keyval is read as false

            return if (keycode in kbdmatrix.indices) kbdmatrix[keycode]
            else NONE.toChar()

        } else return NONE.toChar()
    }


    fun waitKey(timeout: Long): Char {

        val starttime = System.currentTimeMillis()
        var currtime = starttime

        while (currtime - starttime < timeout) {
            currtime = System.currentTimeMillis()
            val key = getKey()
            if (key != NONE.toChar()) return key
        }

        return NONE.toChar()
    }
}

