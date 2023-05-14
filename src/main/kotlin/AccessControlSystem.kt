import Users.User

fun main() {
    AccessControlSystem.init()
    AccessControlSystem.run()
}

object AccessControlSystem {



    const val CMD_WAIT_TIME = 2000
    const val OPENSPEED = 8
    const val CLOSESPEED = 2

    val ABORTCODES = listOf(TUI.ABORTCODE, TUI.TIMEOUTCODE)

    fun init() {
        HAL.init()
        KBD.init()
        SerialEmitter.init()
        LCD.init()
        Users.init()
        M.init()
    }


    fun run() {

        while (true) {
            LCD.clear()
            M.init()
            TUI.write(LogFile.getDate(), TUI.ALIGN.Center, TUI.LINES.First)

            var isValidUser = false
            var user: User? = null

            val UIN = TUI.query("UIN:", TUI.ALIGN.Center, TUI.LINES.Second, TUI.ENTRY.UIN)

            if (UIN !in ABORTCODES) {
                user = Users.userlist[UIN]
                if (user != null) isValidUser = validateUser(user)
                else {
                    TUI.write("INVALID USER", TUI.ALIGN.Center, TUI.LINES.Second)
                    waitTimeMilli(CMD_WAIT_TIME)
                }
            }

            if (isValidUser) {
                LogFile.add(user!!.UIN)

                user.hello()

                val key = KBD.waitKey(TUI.KBDTIMEOUT)
                if (key == '#') user.changePin()
                else if (key == '*') user.clearMessage()

                openDoor(user)
            }


            TUI.clearline(TUI.LINES.Second)
        }
    }

    fun User.hello() {
        LCD.clear()

        TUI.write("Hello", TUI.ALIGN.Center, TUI.LINES.First)
        TUI.write(this.username, TUI.ALIGN.Center, TUI.LINES.Second)


        if (this.message != "null") {
            LCD.clear()
            TUI.write(this.username, TUI.ALIGN.Center, TUI.LINES.First)
            TUI.write(this.message!!, TUI.ALIGN.Center, TUI.LINES.Second)
        }

    }

    fun User.clearMessage() {
        if (message != "null") {

        LCD.clear()

        TUI.write("Clear Message?", TUI.ALIGN.Center, TUI.LINES.First)

        TUI.write("* To confirm ", TUI.ALIGN.Center, TUI.LINES.Second)

        if (KBD.waitKey(TUI.KBDTIMEOUT) == '*') {
            this.message = "null"
            LCD.clear()
            TUI.write("Message ", TUI.ALIGN.Center, TUI.LINES.First)
            TUI.write("Cleared ", TUI.ALIGN.Center, TUI.LINES.Second)
            waitTimeMilli(CMD_WAIT_TIME)
        }
        else {
            TUI.write("Message ", TUI.ALIGN.Center, TUI.LINES.First)
            TUI.write("Kept ", TUI.ALIGN.Center, TUI.LINES.Second)
            waitTimeMilli(CMD_WAIT_TIME)
        }
       }
        waitTimeMilli(CMD_WAIT_TIME)
    }

    fun User.changePin() {
        LCD.clear()

        TUI.write("Change PIN?", TUI.ALIGN.Center, TUI.LINES.First)

        TUI.write("* To Confirm", TUI.ALIGN.Center, TUI.LINES.Second)

        if (KBD.waitKey(TUI.KBDTIMEOUT) == '*') {

            val newpin = TUI.query(" New PIN? :", TUI.ALIGN.Center, TUI.LINES.Second, TUI.ENTRY.PIN)

            TUI.clearline(TUI.LINES.Second)

            val confirmPin = TUI.query("Confirm:", TUI.ALIGN.Center, TUI.LINES.Second, TUI.ENTRY.PIN)

            TUI.clearline(TUI.LINES.Second)

            if (newpin == confirmPin && newpin !in ABORTCODES) {
                pin = newpin
                TUI.write("PIN Changed", TUI.ALIGN.Center, TUI.LINES.Second)
            }
            else {
                TUI.write("PIN Kept", TUI.ALIGN.Center, TUI.LINES.Second)
            }
            waitTimeMilli(CMD_WAIT_TIME)
        }
    }

    fun validateUser(user: User): Boolean {

        var PIN = -999
        var attempts = 1

        while (attempts <= 3 && PIN != user.pin) {
            PIN = TUI.query("PIN:", TUI.ALIGN.Center, TUI.LINES.Second, TUI.ENTRY.PIN)
            if (PIN == user.pin) {
                return true
            }
            else {
                if (PIN in ABORTCODES) return false
                TUI.write("Failed login ($attempts)", TUI.ALIGN.Center, TUI.LINES.Second)
                waitTimeMilli(CMD_WAIT_TIME)
                TUI.clearline(TUI.LINES.Second)
                attempts++

                if (attempts > 3) {
                    TUI.write("Too many tries", TUI.ALIGN.Center, TUI.LINES.Second)
                    waitTimeMilli(CMD_WAIT_TIME)
                }
            }
        }
        return false
    }

    fun openDoor(user: User) {
        LCD.clear()

        TUI.write(user.username, TUI.ALIGN.Center, TUI.LINES.First)

        TUI.write("Opening Door", TUI.ALIGN.Center, TUI.LINES.Second)
        DoorMechanism.open(OPENSPEED)
        while (!DoorMechanism.finished());
        TUI.clearline(TUI.LINES.Second)
        TUI.write("Door Open", TUI.ALIGN.Center, TUI.LINES.Second)

        waitTimeMilli(CMD_WAIT_TIME * 2)

        TUI.clearline(TUI.LINES.Second)
        TUI.write("Door Closing", TUI.ALIGN.Center, TUI.LINES.Second)

        DoorMechanism.close(CLOSESPEED)
        while (!DoorMechanism.finished());
        TUI.clearline(TUI.LINES.Second)
        TUI.write("Door Closed", TUI.ALIGN.Center, TUI.LINES.Second)

        waitTimeMilli(CMD_WAIT_TIME / 2)
    }
}


