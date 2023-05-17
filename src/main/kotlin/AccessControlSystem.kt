import TUI.LINES
import TUI.CMD_ABORT_CODES
import TUI.KBDTIMEOUT
import TUI.ALIGN
import TUI.ENTRY
import kotlin.system.exitProcess

fun main() {
    AccessControlSystem.init()
    AccessControlSystem.run()
    exitProcess(-1) // simulação
}

object AccessControlSystem {

    const val CMD_WAIT_TIME = 2000
    private const val DOOR_OPEN_SPEED = 0b1000
    private const val DOOR_CLOSE_SPEED = 0b0010


    var on = false

    fun init() {
        HAL.init()
        KBD.init()
        SerialEmitter.init()
        LCD.init()
        Users.init("USERS.txt")
        on = true
        M.init()
    }


    fun run() {

        while (on) {

                TUI.writeLine(LogFile.getDate(true), ALIGN.Center, LINES.First)

                var isValidUser = false
                var user: User? = null

                val UIN = TUI.query("UIN:", ALIGN.Center, LINES.Second, ENTRY.UIN)

                if (UIN !in CMD_ABORT_CODES) {

                    user = Users.userlist[UIN]

                    if (user != null) isValidUser = validateUser(user)

                    else {
                        TUI.writeLine("INVALID USER", ALIGN.Center, LINES.Second)
                        waitTimeMilli(CMD_WAIT_TIME)
                    }

                }

                if (isValidUser) {

                    LogFile.add(user!!.UIN)

                    ACSHello(user)

                    val key = KBD.waitKey(KBDTIMEOUT / 2)

                    if (key == '#') ACSChangePin(user)

                    else if (key == '*' && user.message != null) ACSClearMessage(user)

                    openDoor(user)

                }

            M.init()

        }


        TUI.writeLines("Shutting Down", ALIGN.Center,"...",  ALIGN.Center)

        waitTimeMilli(CMD_WAIT_TIME)

        LCD.clear()

    }


    private fun validateUser(user: User): Boolean {

        var PIN = -999
        var attempts = 1

        while (attempts <= 3 && PIN != user.pin) {

            PIN = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            if (PIN == user.pin) return true

            else {

                if (PIN in CMD_ABORT_CODES) return false

                TUI.writeLine("Failed login ($attempts)", ALIGN.Center, LINES.Second)

                waitTimeMilli(CMD_WAIT_TIME)

                attempts++

                if (attempts > 3) {
                    TUI.writeLine("Too many tries", ALIGN.Center, LINES.Second)
                    waitTimeMilli(CMD_WAIT_TIME)
                }

            }

        }

        return false

    }

    fun ACSChangePin(user : User) {

        TUI.writeLines("Change PIN?", ALIGN.Center, "* to confirm ", ALIGN.Center)

        if (KBD.waitKey(KBDTIMEOUT) == '*') {

            TUI.writeLine("New PIN?", ALIGN.Center, LINES.First)

            val newPin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            TUI.writeLine("Confirm PIN:", ALIGN.Center, LINES.First)

            val confirmPin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            if (newPin == confirmPin && newPin !in CMD_ABORT_CODES) {
                user.pin = newPin

                TUI.writeLines("PIN", ALIGN.Center, "Changed", ALIGN.Center)
            }

            else {

                TUI.writeLines("PIN", ALIGN.Center, "Kept", ALIGN.Center)

            }

        }

        else {

            TUI.writeLines("PIN", ALIGN.Center, "Kept", ALIGN.Center)

        }

        waitTimeMilli(CMD_WAIT_TIME)

    }


    /**
     * Clears the set user message after getting confirmation via the [AccessControlSystem].
     * Keeps the message if there is no confirmation.
     *
     */
    fun ACSClearMessage(user: User) {

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
     * Displays the set user message if there is one after waiting [CMD_WAIT_TIME].
     */
    fun ACSHello(user : User) {

        TUI.writeLines("Hello", ALIGN.Center, user.username, ALIGN.Center)

        waitTimeMilli(CMD_WAIT_TIME)

        if (user.message != null) {
            TUI.writeLines(user.username, ALIGN.Center, user.message!!, ALIGN.Center)
        }

    }

    private fun openDoor(user: User) {

        TUI.writeLines(user.username, ALIGN.Center, "Opening Door", ALIGN.Center)
        waitTimeMilli(CMD_WAIT_TIME / 2)

        DoorMechanism.open(DOOR_OPEN_SPEED)

        while(!DoorMechanism.finished());

        TUI.writeLine("Door Open", ALIGN.Center, LINES.Second)
        waitTimeMilli(CMD_WAIT_TIME * 2)

        TUI.writeLine("Door Closing", ALIGN.Center, LINES.Second)
        DoorMechanism.close(DOOR_CLOSE_SPEED)

        while(!DoorMechanism.finished());

        TUI.writeLine("Door Closed", ALIGN.Center, LINES.Second)
        waitTimeMilli(CMD_WAIT_TIME)

    }

}

