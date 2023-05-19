import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import TUI.ALIGN
import TUI.LINES


@OptIn(ExperimentalTime::class)
fun main() {
    val arr = Array<String>(100) {""}
    val time = measureTime {
        Users.init("USERS.txt")
    for (i in 0 until Users.SIZE) {
        val uin = Users.addUser("${i * i}", (i + 1) * 1324)
        Users.removeUser(i)
    }
    Users.addUser("12313", 23234)
    Users.addUser("23145", 23231)

    Users.close("USERS.txt")
    }
    val ind = Users.userlist.indexOf(Users.userlist[0])
    val hash = HashMap<String, String>()


}

/**
 * Collection of user data, provides functionality for managing users.
 * @property SIZE The max number of users that can be stored.
 * @property key The key for pin encryption and decryption.
 * @property userlist The array of stored users with [SIZE] elements.
 * @see User
 * @see FileAccess
 * @author Bernardo Pereira
 * @author António Paulino
 */
object Users {

    const val SIZE = 1000

    var userlist = arrayOfNulls<User>(SIZE)

    private const val key = 19259


    /**
     * Initializes the list of users.
     * Reads from the file where users are stored.
     * @param fileName the file where users are stored
     */
    fun init(fileName : String) {
        val users = FileAccess.inFromFile(fileName)
        for (user in users) {

            val userargs = user.split(";") // User params are split in a single line using ';'
            val (uin, pin, username, message) = userargs

            val uinInt = uin.toInt()
            val pinInt = pin.decode() //decode the stored password
            val msgval = if (message == "null") null else message

            userlist[uinInt] = User(uinInt, pinInt, username, msgval)
        }
    }


    /**
     * Encodes the user PIN so it can be stored.
     */
    private fun Int.encode(): Int = (this.xor(key) * key)


    /**
     * Decodes the stored user PIN to use during program run time.
     */
    private fun String.decode() = (this.toInt() / key ) xor(key)


    /**
     * Adds a user to the current user list by
     * iterating through the user list and atributting the first available
     * UIN to the new user.
     *
     * @param username Username of the new user.
     * @param pin PIN of the new user.
     *
     * @return the new user's UIN or -1 if the user list is currently full.
     */
    fun addUser(username : String, pin: Int) : Int {
        for (uin in userlist.indices) {
            if (userlist[uin] == null) {
                userlist[uin] = User(uin, pin, username, null)
                return uin
            }
        }
        return -1
    }

    /**
     * Removes a user from the current user list.
     * @param uin The UIN of the user to remove.
     */
    fun removeUser(uin: Int) {
        userlist[uin] = null
    }

    /**
     * Adds a message to the specified user.
     * @param uin The UIN of the user to message.
     * @return the UIN of the user if the message was added successfully or -1 if the user does not exist.
     */
    fun setMsg(message: String, uin: Int): Int {
        if (userlist[uin] == null) return -1
        userlist[uin] = userlist[uin]!!.copy(message = message)
        return uin
    }

    /**
     * Stores the user list that was altered during program run time in the specified file.
     * One user per line, with each parameter separated by a semicolon.
     * @param fileName the name of the file to store users in.
     */
    fun close(filename : String) {
        val usersToStore = userlist.filterNotNull().map { "${it.UIN};${it.pin.encode()};${it.username};${it.message}" }
        FileAccess.outToFile(usersToStore, filename)
    }

}
