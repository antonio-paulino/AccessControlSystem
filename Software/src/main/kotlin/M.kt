fun main() {
    M.run()
}


/**
 * 22/5/2023
 *
 * Maintenance mode for the [AccessControlSystem].
 * Handles maintenance operations and commands.
 * @author Bernardo Pereira
 * @author António Paulino
 *
 * @see AccessControlSystem
 * @see Users
 * @see User
 * @see TUI
 */
object M {
    /**
     * Bit mask for the M flag on the USB input port.
     */
    private const val MAINTENANCEMASK = 0b10000000

    /**
     * Stores the current M flag value, if true, the maintenance mode runs, if false, it stops.
     */
    private var maintenance = false

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
        AccessControlSystem.enterMaintenance()
        println("Maintenance mode. Write help for a list of commands")
        while (maintenance) {
            print("Maintenance> ")
            val command = readln().trim().lowercase()

            if (command.isNotEmpty()) {
                when (command) {
                    "help" -> printHelp()
                    "adduser" -> AccessControlSystem.addUserCommand()
                    "removeuser" -> AccessControlSystem.removeUserCommand()
                    "addmsg" -> AccessControlSystem.addMessageCommand()
                    "close" -> AccessControlSystem.closeCommand()
                    "listusers" -> AccessControlSystem.printUsersCommand()
                    "unlockuser" -> AccessControlSystem.unlockUserCommand()
                    else -> println("Invalid command")
                }
            }
            maintenance = HAL.isBit(MAINTENANCEMASK)
        }
        println("Exiting maintenance mode...")
    }
}


/**
 * Prints the help information for available maintenance commands.
 */
fun printHelp() {
    println("ADDUSER                                                     | Adds a user to the system.")
    println("REMOVEUSER                                                  | Removes a user from the system.")
    println("ADDMSG                                                      | Adds a message to a specified user.")
    println("CLOSE                                                       | Updates the system, allowing it to be shut down.")
    println("LISTUSERS                                                   | Prints the system users.")
    println("UNLOCKUSER                                                  | Unlocks a blocked user.")
}
