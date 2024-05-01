import AccessControlSystem.CMD_WAIT_TIME
import AccessControlSystem.on
import TUI.ALIGN
import TUI.CMD_ABORT_CODES
import TUI.ENTRY
import TUI.KBDTIMEOUT
import TUI.LINES
import kotlin.system.exitProcess

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

                        val key = KBD.waitKey(KBDTIMEOUT / 2)

                        if (key == '#') acsChangePin(user) // Change PIN after authorized access

                        if (key == '*') acsClearMessage(user) // Clear MSG if it exists

                        openDoor(user)

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

    /**
     * Validates the user's PIN for authentication.
     *
     * The user is given 3 attempts.
     *
     * @param user The user to validate.
     * @return true if the user is validated, false if all the 3 attempts fail or the command is aborted.
     *
     */
    private fun acsValidateUser(user: User): Boolean {

        var attempts = 1

        while (attempts <= 3) {

            val pin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            if (Users.isValidLogin(user, pin)) return true

            else {

                if (pin in CMD_ABORT_CODES) return false //Return false if aborted

                TUI.writeLine("INVALID PIN ($attempts)", ALIGN.Center, LINES.Second)

                attempts++

                waitTimeMilli(CMD_WAIT_TIME)

                if (attempts > 3) {
                    TUI.writeLine("Too many tries", ALIGN.Center, LINES.Second)
                    waitTimeMilli(CMD_WAIT_TIME)
                }

            }

        }

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

        if(user.message == null) return

        TUI.writeLines("Clear Message?", ALIGN.Center, "* To confirm ", ALIGN.Center)

        if (KBD.waitKey(KBDTIMEOUT) == '*') {

            user.message = null

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

        if (user.message != null) {
            TUI.writeLines(user.username, ALIGN.Center, user.message!!, ALIGN.Center)
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

    private fun openDoor(user: User) {

        TUI.writeLines(user.username, ALIGN.Center, "Opening Door", ALIGN.Center)
        waitTimeMilli(CMD_WAIT_TIME / 2)

        DoorMechanism.open(DOOR_OPEN_SPEED)

        while (!DoorMechanism.finished()) {
            waitTimeMilli(CHECK_INTERVAL)
        }

        TUI.writeLine("Door Open", ALIGN.Center, LINES.Second)
        waitTimeMilli(CMD_WAIT_TIME * 2)

        TUI.writeLine("Door Closing", ALIGN.Center, LINES.Second)
        DoorMechanism.close(DOOR_CLOSE_SPEED)

        while (!DoorMechanism.finished()) {
            waitTimeMilli(CHECK_INTERVAL)
        }

        TUI.writeLine("Door Closed", ALIGN.Center, LINES.Second)
        waitTimeMilli(CMD_WAIT_TIME)

    }

}


