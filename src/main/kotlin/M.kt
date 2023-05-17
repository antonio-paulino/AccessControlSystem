import TUI.ENTRY
import TUI.ALIGN

fun main() {
    M.run()
}


/**
 * Maintenance mode for the [AccessControlSystem].
 * Handles maintenance operations and commands.
 * @property MAINTENANCEMASK the bit mask of the M input on the USB Port
 * @property maintenace flag indicating whether the system is in maintenance mode
 * @property on flag indicating whether the [AccessControlSystem] is on
 *
 * @author Bernardo Pereira
 * @author António Paulino
 *
 * @see AccessControlSystem
 * @see Users
 * @see User
 * @see TUI
 */
object M {

    const val MAINTENANCEMASK = 0b10000000

    var maintenance = HAL.isBit(MAINTENANCEMASK)
    var on = true

    /**
     * initializes the maintenance mode if the system is in maintenance mode, indicated by the M input.
     */
    fun init() {
        maintenance = HAL.isBit(MAINTENANCEMASK)
        if (maintenance) run()
    }

    /**
     * Executes the maintenance mode commands.
     * Displays the maintenance mode interface on the LCD.
     * Available commands: HELP, ADDUSER, REMOVEUSER, ADDMSG, CLOSE.
     */
    fun run() {
        LCD.clear()
        TUI.writeLines("OUT OF SERVICE", ALIGN.Center, "|MAINTENANCE|", ALIGN.Center)
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


    /**
     * Prints the help information for available maintenance commands.
     */
    private fun printHelp() {
        println("ADDUSER                                                     | Adds a user to the system.")
        println("REMOVEUSER                                                  | Removes a user from the system.")
        println("ADDMSG                                                      | Adds a message to a specified user.")
        println("CLOSE                                                       | Updates the system, allowing it to be shut down.")
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


    /**
     * Removes a user from the user list if there is a user for the given UIN.
     *
     * The system requests the system manager to input the
     * UIN, shows the name of the corresponding user, and asks for confirmation.
     *
     * The command is aborted if no input is given.
     * Gets the UIN input by calling [getIntEntry]
     */
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

    /**
     * Allows associating an information message addressed to a particular user that is presented
     * during the process of authentication to the restricted zone.
     *
     * The command is aborted if no input is given.
     *
     * Gets the UIN and message inputs by calling [getIntEntry] and [getStrEntry]
     */
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

    /**
     * Allows shutting down the [AccessControlSystem]. The system asks the user to confirm the command and
     * writes the user information to a text file (one user per line).
     *
     * Updates the users using the [Users.close] function
     *
     *
     *
     */
    private fun closeCommand() {
        print("Shut down the AccessControlSystem? (Y/N): ")
        val confirmation = readln().trim().lowercase()
        if (confirmation in "sy" && confirmation.isNotEmpty()) {
            Users.close("USERS.txt")
            AccessControlSystem.on = false
            println("System updated. You can now shut the system down by exiting maintenance mode (Turn M off).")
        }
        else
            println("The system was not shut down.")
    }


    /**
     * Retrieves a string entry from the user.
     *
     * The entry must not exceed the 16 characters, since that is the maximum length supported by the LCD.
     *
     * @param entry The type of entry (USERNAME, MSG)
     * @return The system manager provided string entry, or null if the entry was aborted.
     *
     *
     */
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


    /**
     * Retrieves an integer entry from the user.
     *
     * The entry must not exceed the amount of digits for the requested entry type, 3 for the UIN and 4 for PIN.
     *
     * @param entry The type of entry (UIN, PIN).
     * @return The system manager provided integer entry, or null if the entry was aborted.
     *
     *
     */
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