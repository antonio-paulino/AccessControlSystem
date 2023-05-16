import AccessControlSystem.clearMessage
import Users.User
import TUI.LINES
import TUI.ABORTCODE
import TUI.TIMEOUTCODE
import TUI.KBDTIMEOUT
import TUI.ALIGN
import TUI.ENTRY

fun main() {
    AccessControlSystem.init()
    AccessControlSystem.run()
}

object AccessControlSystem {

    private const val CMD_WAIT_TIME = 2000
    private const val DOOR_OPEN_SPEED = 0b1000
    private const val DOOR_CLOSE_SPEED = 0b0010

    private val CMD_ABORT_CODES = listOf(ABORTCODE, TIMEOUTCODE)

    var on = false

    fun init() {
        HAL.init()
        KBD.init()
        SerialEmitter.init()
        LCD.init()
        Users.init()
        M.init()
        on = true
    }


    fun run() {

        while (on) {
            M.init()
            if (on) {
                TUI.write(LogFile.getDate(true), ALIGN.Center, LINES.First)

                var isValidUser = false
                var user: User? = null

                val UIN = TUI.query("UIN:", ALIGN.Center, LINES.Second, ENTRY.UIN)

                if (UIN !in CMD_ABORT_CODES) {
                    user = Users.userlist[UIN]
                    if (user != null) isValidUser = validateUser(user)
                    else {
                        TUI.write("INVALID USER", ALIGN.Center, LINES.Second)
                        waitTimeMilli(CMD_WAIT_TIME)
                        LCD.clear()
                    }
                }

                if (isValidUser) {
                    LogFile.add(user!!.UIN)

                    user.hello()

                    if (user.message != null) waitTimeMilli(CMD_WAIT_TIME)

                    val key = KBD.waitKey(KBDTIMEOUT)
                    if (key == '#') user.changePin()
                    else if (key == '*') user.clearMessage()

                    openDoor(user)
                }
            }
        }
        LCD.clear()

        TUI.write("Shutting Down", ALIGN.Center, LINES.First)
        TUI.write("...", ALIGN.Center, LINES.Second)

        waitTimeMilli(CMD_WAIT_TIME)

        LCD.clear()
    }

    fun User.hello() {
        LCD.clear()

        TUI.write("Hello", ALIGN.Center, LINES.First)
        TUI.write(this.username, ALIGN.Center, LINES.Second)

        waitTimeMilli(CMD_WAIT_TIME)

        if (this.message != null) {
            LCD.clear()
            TUI.write(this.username, ALIGN.Center, LINES.Second)
            TUI.write(this.message!!, ALIGN.Center, LINES.First)
        }
    }

    fun User.clearMessage() {
        if (message != null) {

        LCD.clear()

        TUI.write("Clear Message?", ALIGN.Center, LINES.First)

        TUI.write("* To confirm ", ALIGN.Center, LINES.Second)

        if (KBD.waitKey(KBDTIMEOUT) == '*') {
            this.message = null
            LCD.clear()
            TUI.write("Message ", ALIGN.Center, LINES.First)
            TUI.write("Cleared ", ALIGN.Center, LINES.Second)
            waitTimeMilli(CMD_WAIT_TIME)
        }
        else {
            LCD.clear()
            TUI.write("Message ", ALIGN.Center, LINES.First)
            TUI.write("Kept ", ALIGN.Center, LINES.Second)
            waitTimeMilli(CMD_WAIT_TIME)
        }
       }

        waitTimeMilli(CMD_WAIT_TIME)

        LCD.clear()
    }

    fun User.changePin() {
        LCD.clear()

        TUI.write("Change PIN?", ALIGN.Center, LINES.First)

        TUI.write("* To Confirm", ALIGN.Center, LINES.Second)

        if (KBD.waitKey(KBDTIMEOUT) == '*') {
            TUI.clearline(LINES.Second)

            TUI.write("New PIN?", ALIGN.Center, LINES.Second)
            val newpin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            LCD.clear()

            TUI.write("Confirm PIN:", ALIGN.Center, LINES.First)
            val confirmPin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            LCD.clear()

            TUI.write("PIN", ALIGN.Center, LINES.First)

            if (newpin == confirmPin && newpin !in CMD_ABORT_CODES) {
                pin = newpin
                TUI.write("Changed", ALIGN.Center, LINES.Second)
            }
            else {
                TUI.write("Kept", ALIGN.Center, LINES.Second)
            }
            waitTimeMilli(CMD_WAIT_TIME)

            LCD.clear()
        }
    }

    fun validateUser(user: User): Boolean {

        var PIN = -999
        var attempts = 1

        while (attempts <= 3 && PIN != user.pin) {
            PIN = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)
            if (PIN == user.pin) {
                return true
            }
            else {
                if (PIN in CMD_ABORT_CODES) {
                    LCD.clear()
                    return false
                }

                TUI.write("Failed login ($attempts)", ALIGN.Center, LINES.Second)
                waitTimeMilli(CMD_WAIT_TIME)
                TUI.clearline(LINES.Second)
                attempts++

                if (attempts > 3) {
                    TUI.write("Too many tries", ALIGN.Center, LINES.Second)
                    waitTimeMilli(CMD_WAIT_TIME)
                    LCD.clear()
                }
            }
        }
        return false
    }

    fun openDoor(user: User) {
        LCD.clear()

        TUI.write(user.username, ALIGN.Center, LINES.First)

        TUI.write("Opening Door", ALIGN.Center, LINES.Second)
        DoorMechanism.open(DOOR_OPEN_SPEED)
        while (!DoorMechanism.finished());
        TUI.clearline(LINES.Second)
        TUI.write("Door Open", ALIGN.Center, LINES.Second)

        waitTimeMilli(CMD_WAIT_TIME * 2)

        TUI.clearline(LINES.Second)
        TUI.write("Door Closing", ALIGN.Center, LINES.Second)

        DoorMechanism.close(DOOR_CLOSE_SPEED)
        while (!DoorMechanism.finished());
        TUI.clearline(LINES.Second)
        TUI.write("Door Closed", ALIGN.Center, LINES.Second)

        waitTimeMilli(CMD_WAIT_TIME)

        LCD.clear()
    }
}


fun waitTimeMilli(waitTime: Int) {
    val endtime = System.currentTimeMillis() + waitTime
    while (System.currentTimeMillis() <= endtime);
}

fun waitTimeNano(waitTime: Int) {
    val endtime = System.nanoTime() + waitTime
    while (System.nanoTime() <= endtime);
}

