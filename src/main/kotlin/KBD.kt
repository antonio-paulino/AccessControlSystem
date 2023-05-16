import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main() {
    while(true) {
        val key = KBD.getKey()
        if(key != KBD.NONE.toChar()) print(key)
    }
    }


object KBD {
    const val NONE = 0

    private const val KVALMASK = 0b00010000

    private const val KACKMASK = 0b01000000

    private const val KCODEMASK = 0b00001111

    private var keycode = NONE

    private var keyval = false

    private val kbdmatrix = listOf('1', '4', '7', '*', '2', '5', '8', '0', '3', '6', '9', '#')

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


