import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import TUI.ALIGN
import TUI.LINES

/**
 * 22/5/2023
 *
 * Collection of user data, provides functionality for managing users.
 *
 * @property SIZE the max amount of users
 * @see User
 * @see FileAccess
 * @author Bernardo Pereira
 * @author António Paulino
 */
object Users {
    /**
     * The amount of users that can be stored
     */
     const val SIZE = 1000

    /**
     * The array where users are stored during program run time
     */
    private var userlist = arrayOfNulls<User>(SIZE)

    /**
     * The key for pin encryption and decryption.
     */
    private const val key = 0b0100101100111011


    /**
     * Initializes the list of users.
     * Reads from the file where users are stored.
     * @param fileName the file where users are stored
     */
    fun init(fileName: String) {
        val users = FileAccess.inFromFile(fileName)
        for (user in users) {

            val userargs = user.split(";") //User parameters are separated by ';'
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
    private fun String.decode() = (this.toInt() / key) xor (key)


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
    fun addUser(username: String, pin: Int): Int {
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
     * Gets a user from the user list.
     * @param uin The UIN of the user
     * @return The user or null if the user does not exist
     */
    fun getUser(uin : Int): User? = userlist[uin]


    /**
     * Checks if the given pin is the correct pin for the given user
     * @param user The user being validated
     * @param pin The pin to compare with the user pin
     * @return true if the given pin is the user pin, false otherwise
     */
    fun isValidLogin(user : User, pin : Int) : Boolean = user.pin == pin



    /**
     * Stores the user list that was altered during program run time in the specified file.
     * One user per line, with each parameter separated by a semicolon.
     * @param fileName the name of the file to store users in.
     */

    fun close(filename: String) {
        val usersToStore = userlist.filterNotNull().map { "${it.UIN};${it.pin.encode()};${it.username};${it.message}" }
        FileAccess.outToFile(usersToStore, filename)
    }

}
