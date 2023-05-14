import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {

    val time = measureTime {
        Users.init()
    for (i in 0 until Users.SIZE + 200) {
        //val uin = Users.addUser("${i * i}", (i + 1) * 1324)
        Users.removeUser(i)
    }
    //Users.addUser("12313", 23234)
    //Users.addUser("23145", 23231)

    Users.close()
    }
    println(time)
}


object Users {

    data class User(val UIN: Int, var pin: Int, var username : String, var message: String?)

    const val SIZE = 1000

    var userlist = HashMap<Int, User>()

    private const val key = 19259

    fun init() {
        val fileread = FileAccess.createReader("USERS.txt")
        var line: String? = fileread.readLine()
        while (line != null) {
            val lineargs = line.split(";")
            val (uin, pin, username, message) = lineargs
            val uinInt = uin.toInt()
            val pinInt = pin.decode()
            userlist[uinInt] = User(uinInt, pinInt, username, message)
            line = fileread.readLine()
        }
        fileread.close()
    }


    private fun Int.encode(): Int = (this.xor(key) * key)

    private fun String.decode() = (this.toInt() / key ) xor(key)

    fun addUser(username: String, pin: Int) : Int {
        var uin = 0
        while (userlist.containsKey(uin)) { uin++ }
        if (uin >= SIZE) return -1
        else {
            userlist[uin] = User(uin, pin, username, null)
            return uin
        }
    }

    fun removeUser(uin: Int) {
        userlist.remove(uin)
    }

    fun setMsg(message: String, uin: Int) : Int {
        if (userlist[uin] != null) {
            userlist[uin] = userlist[uin]!!.copy(message = message)
            return uin
        }
        else return -1
    }

    fun close() {
        val outputfile = FileAccess.createWriter("USERS.txt")

        for (uin in 0 until SIZE) {
            val user = userlist[uin]
            if (user != null) {
                outputfile.println("${user.UIN};${user.pin.encode()};${user.username};${user.message};")
            }
        }
        outputfile.close()
    }
}