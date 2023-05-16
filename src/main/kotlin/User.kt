import TUI.LINES
import TUI.ALIGN
import TUI.ENTRY
import TUI.CMD_ABORT_CODES
import TUI.KBDTIMEOUT

import isel.leic.utils.Time


data class User(val UIN: Int, var pin: Int, var username: String, var message: String?) {

    private val waitTime = 2000

    fun hello() {

        TUI.writeLines("Hello", ALIGN.Center, username, ALIGN.Center)

        waitTimeMilli(waitTime)

        if (message != null) {
            TUI.writeLines(username, ALIGN.Center, message!!, ALIGN.Center)
        }

    }

    fun clearMessage() {

        if (message == null) return

            TUI.writeLines("Clear Message?", ALIGN.Center, "* To confirm ", ALIGN.Center)

            if (KBD.waitKey(KBDTIMEOUT) == '*') {

                this.message = null

                TUI.writeLines("Message", ALIGN.Center, "Cleared", ALIGN.Center)

                waitTimeMilli(waitTime)

            } else {

                TUI.writeLines("Message", ALIGN.Center, "Kept", ALIGN.Center)

                waitTimeMilli(waitTime)
            }

    }

    fun changePin() {

        TUI.writeLines("Change PIN?", ALIGN.Center, "* to confirm ", ALIGN.Center)

        if (KBD.waitKey(KBDTIMEOUT) == '*') {

            TUI.writeLine("New PIN?", ALIGN.Center, LINES.First)

            val newPin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            TUI.writeLine("Confirm PIN:", ALIGN.Center, LINES.First)

            val confirmPin = TUI.query("PIN:", ALIGN.Center, LINES.Second, ENTRY.PIN)

            if (newPin == confirmPin && newPin !in CMD_ABORT_CODES) {
                pin = newPin

                TUI.writeLines("PIN", ALIGN.Center, "Changed", ALIGN.Center)
            }

            else {

                TUI.writeLines("PIN", ALIGN.Center, "Kept", ALIGN.Center)

            }

        }

        else {

            TUI.writeLines("PIN", ALIGN.Center, "Kept", ALIGN.Center)

        }

        waitTimeMilli(waitTime)

    }

}