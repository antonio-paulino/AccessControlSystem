fun main() {
    M.run()
}

object M {

    const val MAINTENANCEMASK = 128

    var maintenance = HAL.isBit(MAINTENANCEMASK)

    fun init() {
        maintenance = HAL.isBit(MAINTENANCEMASK)
        if (maintenance) run()
    }

    fun run() {
        TUI.write("OUT OF SERVICE", TUI.ALIGN.Center, TUI.LINES.First)
        println("Maintenance mode. Write help for list of commands")
        while (maintenance) {
            val args = readln().trim().getargs()
            if (args.isNotEmpty()) {
                when (args.first().lowercase()) {
                    "help" -> {
                        println("ADDUSER                                                     | Adds a user to the system.")
                        println("REMOVEUSER                                                  | Removes a user from the system.")
                        println("ADDMSG                                                      | Adds message to specified user.")
                        println("CLOSE                                                       | Updates the system, allowing it to be shut down.")
                    }

                    "adduser" -> {
                        var username = "                       "
                        while (username.length > 16) {

                            print("Username : ")
                            username = readln().trim()

                            if(username.length > 16)
                                println("Your username must not exceed the char count supported by the LCD (16).")
                        }

                        var pin = - 1

                        while (pin < 0) {
                            try  {
                                print("PIN: ")
                                pin = readln().toInt()
                            }
                            catch (e: NumberFormatException) {
                                println("Value must be a number.")
                            }
                        }


                        if (pin > 99999) println("Your pin must not exceeed 5 digits.")

                        val uin = Users.addUser(username, pin)

                        if (uin >= 0) println("User $uin $username added successfully.")

                        else println("The user list is full.")
                    }

                    "removeuser" -> {
                            var uin = - 1

                            while (uin < 0) {
                                try  {
                                    print("UIN: ")
                                    uin = readln().toInt()
                                }
                                catch (e: NumberFormatException) {
                                    println("Value must be a number.")
                                }
                            }

                            val user = Users.userlist[uin]

                            if (user == null) println("User does not exist.")

                            else {
                                println("${user.username}. Are you sure you want to remove this user? (Y/N)")

                                if (readln() in "SsYy") {
                                    Users.removeUser(uin)
                                    println("User $uin deleted successfully.")
                                }

                                else println("User was not removed.")
                            }
                        }

                    "addmsg" -> {

                            var uin = - 1

                            while (uin < 0) {
                                try  {
                                    print("UIN: ")
                                    uin = readln().toInt()
                                }
                                catch (e: NumberFormatException) {
                                    println("Value must be a number.")
                                }
                            }

                            if (Users.userlist[uin] == null) {
                                println("User does not exist.")
                                return
                            }

                            var msg = "                       "
                            while (msg.length > 16) {

                                print("Message : ")
                                msg = readln().trim()

                                if(msg.length > 16)
                                    println("Your username must not exceed the char count supported by the LCD (16).")
                            }

                            Users.setMsg(msg, uin)

                            println("Message delivered successfully")

                        }

                    "close" -> {
                        Users.close()
                        println("System updated. You can now exit the maintenance mode.")
                    }
                    else -> println("Invalid command")
                 }
            }
            maintenance = HAL.isBit(MAINTENANCEMASK)
        }
        println("Exiting maintenance mode...")

        LCD.clear()
    }

    private fun String.getargs(): List<String> {
        var word = ""
        val args = mutableListOf<String>()
        for (char in this) {
            if (char == ' ') {
                if (word != "") args += word
                word = ""
            } else {
                word += char
            }
        }
        if (word != "") args += word
        return args
    }


}