import TUI.ENTRY
import TUI.ALIGN

fun main() {
    M.run()
}

object M {

    const val MAINTENANCEMASK = 0b10000000

    var maintenance = HAL.isBit(MAINTENANCEMASK)
    var on = true
    fun init() {
        maintenance = HAL.isBit(MAINTENANCEMASK)
        if (maintenance) run()
    }

    fun run() {
        LCD.clear()
        TUI.writeLines("OUT OF SERVICE", ALIGN.Center, "|MAINTENANCE|", ALIGN.Center, true)
        println("Maintenance mode. Write help for a list of commands")

        while (maintenance) {
            print("Maintenance> ")
            val command = readln().trim().lowercase()

            when (command) {
                "help" -> printHelp()
                "adduser" -> addUserCommand()
                "removeuser" -> removeUserCommand()
                "addmsg" -> addMessageCommand()
                "close" -> closeCommand()
                else -> println("Invalid command")
            }

            maintenance = HAL.isBit(MAINTENANCEMASK)
        }

        println("Exiting maintenance mode...")
        LCD.clear()
    }

    fun printHelp() {
        println("ADDUSER                                                     | Adds a user to the system.")
        println("REMOVEUSER                                                  | Removes a user from the system.")
        println("ADDMSG                                                      | Adds a message to a specified user.")
        println("CLOSE                                                       | Updates the system, allowing it to be shut down.")
    }

    private fun addUserCommand() {
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

    private fun removeUserCommand() {
        val uin = getIntEntry(ENTRY.UIN)

        if (uin == null) {
            println("Command aborted.")
            return
        }

        val user = Users.userlist[uin]

        if (user == null) {
            println("User does not exist.")
            return
        }

        print("${user.username}. Are you sure you want to remove this user? (Y/N)")

        val confirmation = readln().trim().lowercase()

        if (confirmation in "sy" && confirmation.isNotEmpty()) {

            Users.removeUser(uin)
            println("User $uin deleted successfully.")

        }
        else {
            println("User was not removed.")
        }
    }

    private fun addMessageCommand() {
        val uin = getIntEntry(ENTRY.UIN)

        if (uin == null) {
            println("Command aborted.")
            return
        }

        if (Users.userlist[uin] == null) {
            println("User does not exist.")
            return
        }

        val msg = getStrEntry(ENTRY.MSG)
        if (msg == null) {
            println("Command aborted.")
            return
        }

        Users.setMsg(msg, uin)
        println("Message delivered successfully")
    }

    private fun closeCommand() {
        Users.close("USERS.TXT")
        AccessControlSystem.on = false
        println("System updated. You can now shut the system down by exiting maintenance mode (Turn M off).")
    }

    private fun getStrEntry(entry: ENTRY) : String? {
        var str = "                 "
        while (str.length > entry.len) {

            print("$entry : ")
            str = readln().trim()

            if (str.isEmpty()) return null

            if(str.length > entry.len)
                println("Your $entry must not exceed the char count supported by the LCD (${entry.len}).")
        }

        return str
    }

    private fun getIntEntry(entry: ENTRY) : Int? {
        var num = -1
        while (num < 0) {
            try  {
                print("$entry: ")
                val numstr = readln()

                if (numstr.isEmpty()) return null

                if (numstr.length > entry.len)
                    println("Your $entry must not exceeed ${entry.len} digits.")
                else
                    num = numstr.toInt()
            }
            catch (e : NumberFormatException) {
                println("Value must be a number.")
            }
        }
        return num
    }
}