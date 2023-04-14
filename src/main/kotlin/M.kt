
fun main() {
    M.run()
}

object M {
    const val MASKM = 32
    const val ADDUSERARGS = 4
    const val REMOVEUSERARGS = 2
    const val SETMSGARGS = 3

    var maintenance = true
    fun run() {
        LCD.init()
        TUI.queryOrWrite("OUT OF SERVICE", TUI.ALIGN.Center, TUI.LINES.First)
        LCD.cursor(1,17)
        Users.init()
        println("Modo de manutenção. Escreva help para lista de comandos")
        while(maintenance) {
            val args = readln().getargs()
            if (args.isEmpty()) println("Não foram passados argumentos") else {
                when (args.first().lowercase()) {
                    "help" -> {
                        println("adduser        --firstname --lastname --PIN | Adiciona um utilizador ao sistema.")
                        println("removeuser     --UIN                        | Remove um utilizador do sistema .")
                        println("addmsg         --UIN --message              | Adiciona a mensagem ao utilizador específicado ao autenticar.")
                        println("close                                       | Atualiza os utilizadores do sistema, permitindo que seja desligado.")
                    }

                    "adduser" -> if (args.size != ADDUSERARGS) println("Argumentos inválidos") else {
                        val (cmd, firstname, lastname, PIN) = args
                        Users.addUser(firstname, lastname, PIN.toInt())
                    }

                    "removeuser" -> if (args.size != REMOVEUSERARGS) println("Argumentos inválidos") else {
                        val (cmd, UIN) = args
                        val user = Users.userlist[UIN.toInt()]
                        if (user == null) println("O utilizador não existe")
                        else {
                            println("${user.firstname} ${user.lastname}. Tem a certeza que quer remover este utilizador? (S/N)")
                            if (readln() in "Ss") {
                                Users.removeUser(UIN.toInt())
                            }
                        }
                    }

                    "addmsg" -> if (args.size != SETMSGARGS) println("Argumentos inválidos") else {
                        val (cmd, UIN, message) = args
                        val user = Users.userlist[UIN.toInt()]
                        if (user == null) println("O utilizador não existe")
                        else {
                            Users.setMsg(message, user.UIN)
                        }
                    }

                    "close" -> Users.close()
                    else -> println("Comando Inválido")
                }
            }
            maintenance = true
        }
    }
    private fun String.getargs() : List<String> {
        var word = ""
        val args = mutableListOf<String>()
        for(char in this) {
            if (char != ' ') word += char
            else {
                if (word != "") args += word
                word = ""
            }
        }
        if (word != "") args += word
        return args
    }
}