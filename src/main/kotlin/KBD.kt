import isel.leic.utils.Time

fun main(){

}


const val NONE = 0;

var key_code = NONE
var key_val = 0


fun init(){
    key_val = 0
    key_code = NONE
}

fun getKey(): Char{
    key_val = HAL.readBits(64)
    key_code = HAL.readBits(15)
    if(key_val == 1) {
        HAL.setBits(128)
        while (key_val == 1) {
            key_val = HAL.readBits(64)
        }
        HAL.clrBits(64)
        return when (key_code) {
            0 -> '1'
            1 -> '4'
            2 -> '7'
            3 -> '*'
            4 -> '2'
            5 -> '5'
            6 -> '8'
            7 -> '0'
            8 -> '3'
            9 -> '6'
            10 -> '9'
            11 -> '#'
            else -> ' '
        }
    }
    else return NONE.toChar()
}


fun waitKey(timeout: Long): Char{
    var count = 0
    while(timeout > count) {
        count++
        val key = getKey()
        if (key != NONE.toChar()) return key
    }
    return NONE.toChar()
}