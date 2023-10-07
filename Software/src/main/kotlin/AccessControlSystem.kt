import AccessControlSystem.CMD_WAIT_TIME
import AccessControlSystem.on
import TUI.ALIGN
import TUI.CMD_ABORT_CODES
import TUI.ENTRY
import TUI.KBDTIMEOUT
import TUI.LINES

fun main() {
    AccessControlSystem.init()
    AccessControlSystem.run()
}


/**
 *
 * 22/5/2023
 *
 *
 * Responsible for controlling access to restrict zones by means of a User Identification Number (UIN)
 * and a Personal Identification Number (PIN).
 *
 * The system allows access to restrict zone after correct input of a UIN and PIN pair. After a valid access,
 * the system allows the delivery of a text message addressed to the user.
 *
 * The AccessControlSystem is composed of a [LCD] for display, a [KBD] for reading user inputs,  a [DoorMechanism] for opening and closing the door,
 * as well as [M] for maintenance operations.
 * @property on Indicates whether the Access Control System is on or off. To turn off the system set to false.
 * @see HAL
 * @see SerialEmitter
 * @see LCD
 * @see TUI
 * @see KBD
 * @see LogFile
 * @see M
 * @see User
 * @see Users
 * @see DoorMechanism
 * @author Bernardo Pereira
 * @author António Paulino
 *

 */
object AccessControlSystem {

    /**
     * The time to wait between successive commands from the Access Control System.
     */
    private const val CMD_WAIT_TIME = 2000

    /**
     * The speed at which the door is opened after user authorized access.
     */
    const val DOOR_OPEN_SPEED = 0b1000

    /**
     * The speed at which the door is closed after user authorized access.
     */
    const val DOOR_CLOSE_SPEED = 0b0010


    /**
     * The max amount of login attempts before user gets locked
     */
    private const val MAX_ATTEMPTS = 5


    var on = false

    /**
     * Initializes the [AccessControlSystem] by initializing all the required components and loading user data.
     */
    fun init() {
        HAL.init()
        KBD.init()
        SerialEmitter.init()
        LCD.init()
        DoorMechanism.init()
        Users.init("USERS.txt")
        on = true
        M.init()
    }


    /**
     * Runs the [AccessControlSystem].
     *
     * Validates the user and processes the Open/Close door commands, as well as the Change PIN and Clear Msg commands.
     *
     */
    fun run() {

        while (on) { // run if on

            TUI.writeLine(LogFile.getDate(true), ALIGN.Center, LINES.First)

            val uin = TUI.query("UIN:", ALIGN.Center, LINES.Second, ENTRY.UIN)

            if (uin !in CMD_ABORT_CODES) { // If there wasn't a timeout or input abort by the user

                val user = Users.getUser(uin)

                if (user != null) { // If the user exists

                    val isValidUser = acsValidateUser(user)

                    if (isValidUser) {

                        LogFile.add(uin)

                        acsHello(user) // Greets the user

                        val key = TUI.getKey()

                        if (key == '#') acsChangePin(user) // Change PIN after authorized access

                        if (key == '*') acsClearMessage(user) // Clear MSG if it exists

                        openCloseDoor(user)

                    }

                } else {
                    TUI.writeLine("INVALID USER", ALIGN.Center, LINES.Second) // User doesn't exist
                    waitTimeMilli(CMD_WAIT_TIME)
                }
            }
            M.init() // checks the M key to enter maintenance mode
            // if after exiting maintenance mode on is set to false via the close command, then the program will close.

        }
        //Shuts down the system if on is false
        TUI.writeLines("Shutting Down", ALIGN.Center, "...", ALIGN.Center)

        waitTimeMilli(CMD_WAIT_TIME)

        LCD.clear()

    }

    fun enterMaintenance() = TUI.writeLines("OUT OF SERVICE", ALIGN.Center, "|MAINTENANCE|", ALIGN.Center)


    fun printUsersCommand() {
        for (i in 0 until Users.SIZE) {
            val user = Users.getUser(i)
            if (user != null) println(user)
        }
    }



    /**
     * Adds a user to the system if the user list is not full.
     * Assigns the first available UIN and requests the username and PIN of the user from the system manager.
     *
     * The name of the user must be at most 16 characters long.
     *
     * The command is aborted if no input is given.
     *
     * Gets the pin and username inputs by calling [getIntEntry] and [getStrEntry]
     */
    fun addUserCommand() {
        val username = getStrEntry(ENTRY.USERNAME)
        if (username == null) {
            println("Command aborted.")
            return
        }

        val pin = getIntEntry(ENTRY.PIN)
        if (pin == null) {
            println("Command aborted.")
            return
        }

        val uin = Users.addUser(username, pin)
        if (uin >= 0) {
            println("User $uin $username added successfully.")
        } else {
            println("The user list is full.")
        }
    }


    /**
     * Removes a user from the user list if there is a user for the given UIN.
     *
     * The system requests the system manager to input the
     * UIN, shows the name of the corresponding user, and asks for confirmation.
     *
     * The command is aborted if no input is given.
     * Gets the UIN input by calling [getIntEntry]
     */
    fun removeUserCommand() {
        val uin = getIntEntry(ENTRY.UIN)

        if (uin == null) {
            println("Command aborted.")
            return
        }

        val user = Users.getUser(uin)

        if (user == null) {
            println("User does not exist.")
            return
        }

        print("${user.username}. Are you sure you want to remove this user? (Y/N) ")

        val confirmation = readln().trim().lowercase()

        if (confirmation in "sy" && confirmation.isNotEmpty()) {
            Users.removeUser(uin)
            println("User $uin deleted successfully.")
        } else {
            println("User was not removed.")
        }
    }


    /**
     * Unlocks a user
     *
     * @param user the user to unlock
     */
    fun unlockUserCommand() {
        val uin = getIntEntry(ENTRY.UIN)

        if (uin == null) {
            println("Command aborted.")
            return
        }

        val res = Users.unlockUser(uin)

        if (res >= 0)
            println("User unlocked successfully")
        else
            println("User does not exist.")
    }

    /**
     * Allows associating an information message addressed to a particular user that is presented
     * during the process of authentication to the restricted zone.
     *
     * The command is aborted if no input is given.
     *
     * Gets the UIN and message inputs by calling [getIntEntry] and [getStrEntry]
     */
    fun addMessageCommand() {
        val uin = getIntEntry(ENTRY.UIN)

        if (uin == null) {
            println("Command aborted.")
            return
        }

        val msg = getStrEntry(ENTRY.MSG)

        if (msg == null) {
            println("Command aborted.")
            return
        }

        val res = Users.setMsg(msg, uin)

        if (res >= 0)
            println("Message delivered successfully")
        else
            println("User does not exist.")
    }

    /**
     * Allows shutting down the [AccessControlSystem]. The system asks the user to confirm the command and
     * writes the user information to a text file (one user per line).
     *
     * Updates the users using the [Users.close] function
     *
     *
     *
     */
    fun closeCommand() {
        print("Shut down the Access Control System? (Y/N): ")

        val confirmation = readln().trim().lowercase()

        if (confirmation in "sy" && confirmation.isNotEmpty()) {
            close()
            println("System updated. You can now shut the system down by exiting maintenance mode (Turn M off).")
        } else
            println("The system was not shut down.")
    }


    /**
     * Retrieves a string entry from the system manager.
     *
     * The entry must not exceed 16 characters, since that is the maximum length supported by the LCD.
     *
     * @param entry The type of entry (USERNAME, MSG)
     * @return The system manager provided string entry, or null if the entry was aborted.
     *
     *
     */
    private fun getStrEntry(entry: ENTRY): String? {
        var str = "                 "
        while (str.length > entry.len) {

            print("$entry : ")
            str = readln().trim()

            if (str.isEmpty()) return null

            if (str.length > entry.len)
                println("The $entry must not exceed ${entry.len} chars.")
        }

        return str
    }


    /**
     * Retrieves an integer entry from the system manager.
     *
     * The number value must not be larger than the number implicitly defined by entry length (e.g. 999 for UIN length 3).
     *
     * @param entry The type of entry (UIN, PIN).
     * @return The system manager provided integer entry, or null if the entry was aborted.
     *
     *
     */
    private fun getIntEntry(entry: ENTRY): Int? {
        var maxNum = 1
        for (i in 0 until entry.len) {
            maxNum *= 10
        }
        var num = -1
        while (num !in 0 until maxNum) {
            try {
                print("$entry: ")
                val numstr = readln()

                if (numstr.isEmpty()) return null
                num = numstr.toInt()

                if (num < 0) println("Value must be positive")
                else if (num >= maxNum) println("The $entry must not exceed ${entry.len} digits")

            } catch (e: NumberFormatException) {
                println("Value must be a number.")
            }
        }
        return num
    }


    /**
     * Validates the user's PIN for authentication.
     *
     * The user is given 3 attempts.
     *
     * @param user The user to validate.
     * @return true if the user is validated, false if all the 3 attempts fail or the command is aborted.
     *
     */
    fun acsValidateUser(user: User): Boolean {

        if(user.blocked) {
            TUI.writeLine("USER BLOCKED", ALIGN.Center, LINES.First)
            TUI.writeLine("CONTACT SYS MAN", ALIGN.Center, LINES.Second)
            waitTimeMilli(CMD_WAIT_TIME * 2)
            return false
        }

        var attempts = 0

        while (attempts < MAX_ATTEMPTS) {

            val pin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            if (Users.isValidLogin(user, pin)) return true
            else {

                if (pin in CMD_ABORT_CODES) return false //Return false if aborted

                TUI.writeLine("INVALID PIN (${attempts + 1})", ALIGN.Center, LINES.Second)

                attempts++

                waitTimeMilli(CMD_WAIT_TIME)

            }

        }
        Users.blockUser(user.UIN)
        TUI.writeLines("Too many tries", ALIGN.Center, "User Blocked", ALIGN.Center)
        waitTimeMilli(CMD_WAIT_TIME)

        return false //Return false if all attempts failed

    }




    /**
     * Changes the pin for the specified user after authorized access via the [AccessControlSystem]
     *
     * The [AccessControlSystem] queries the user two times, one for the new PIN and another for the confirmation of the new PIN.
     *
     * If both PIN inputs are the same, and the command was not aborted, the PIN is changed.
     *
     * @param user the user to change the pin for
     */
    private fun acsChangePin(user: User) {

        TUI.writeLines("Change PIN?", ALIGN.Center, "* to confirm ", ALIGN.Center)

        if (KBD.waitKey(KBDTIMEOUT) == '*') {

            TUI.writeLine("New PIN?", ALIGN.Center, LINES.First)

            val newPin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            TUI.writeLine("Confirm PIN:", ALIGN.Center, LINES.First)

            val confirmPin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            if (newPin == confirmPin && newPin !in CMD_ABORT_CODES) {
                user.pin = newPin
                TUI.writeLines("PIN", ALIGN.Center, "Changed", ALIGN.Center)
            } else {

                TUI.writeLines("PIN", ALIGN.Center, "Kept", ALIGN.Center)

            }

        } else {

            TUI.writeLines("PIN", ALIGN.Center, "Kept", ALIGN.Center)

        }

        waitTimeMilli(CMD_WAIT_TIME)

    }


    /**
     * Clears the set user message if it exists after authorized access via the [AccessControlSystem].
     *
     * The [AccessControlSystem] queries the user for confirmation to delete the message. If the user confirms
     * the deletion of the message, the message is deleted.
     *
     */
    private fun acsClearMessage(user: User) {

        if (user.message == "") return

        TUI.writeLines("Clear Message?", ALIGN.Center, "* To confirm ", ALIGN.Center)

        if (KBD.waitKey(KBDTIMEOUT) == '*') {

            user.message = ""

            TUI.writeLines("Message", ALIGN.Center, "Cleared", ALIGN.Center)

            waitTimeMilli(CMD_WAIT_TIME)

        } else {

            TUI.writeLines("Message", ALIGN.Center, "Kept", ALIGN.Center)

            waitTimeMilli(CMD_WAIT_TIME)
        }

    }

    /**
     * Displays a greeting message for the user after authentication via the [AccessControlSystem].
     *
     * Displays the set user message if there is one after waiting [CMD_WAIT_TIME].
     */
    private fun acsHello(user: User) {

        TUI.writeLines("Hello", ALIGN.Center, user.username, ALIGN.Center)

        waitTimeMilli(CMD_WAIT_TIME)

        if (user.message != "") {
            TUI.writeLines(user.username, ALIGN.Center, user.message, ALIGN.Center)
            waitTimeMilli(CMD_WAIT_TIME)
        }

    }


    /**
     * Opens the door for the authorized user after authentication via the [AccessControlSystem].
     *
     * The [AccessControlSystem] starts by informing the user that the door is being opened, and opens the door.
     * After the door is opened, the [AccessControlSystem] waits [CMD_WAIT_TIME] * 2 for the user to enter.
     *
     * After waiting, the [AccessControlSystem] informs the user that the door is being closed and closes the door.
     *
     * @param user The user the door is being opened for
     *
     */

    private fun openCloseDoor(user: User) {

        TUI.writeLines(user.username, ALIGN.Center, "Opening Door", ALIGN.Center)
        waitTimeMilli(CMD_WAIT_TIME / 2)

        DoorMechanism.open(DOOR_OPEN_SPEED)

        TUI.writeLine("Door Open", ALIGN.Center, LINES.Second)
        waitTimeMilli(CMD_WAIT_TIME * 2)

        TUI.writeLine("Door Closing", ALIGN.Center, LINES.Second)
        DoorMechanism.close(DOOR_CLOSE_SPEED)

        TUI.writeLine("Door Closed", ALIGN.Center, LINES.Second)
        waitTimeMilli(CMD_WAIT_TIME)

    }


    /**
     * Shuts down the [AccessControlSystem] by updating the users file and setting [on] to false.
     */
    private fun close() {
        Users.close("USERS.txt")
        on = false
    }


}


