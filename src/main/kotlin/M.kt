fun main() {
    M.run()
}

object M {
    const val REMOVEUSERARGS = 2
    const val SETMSGARGS = 3
    const val MAINTENANCEMASK = 128

    var maintenance = HAL.isBit(MAINTENANCEMASK)


    fun init() {
        maintenance = HAL.isBit(MAINTENANCEMASK)
        if (maintenance) run()
    }

    fun run() {
        TUI.write("OUT OF SERVICE", TUI.ALIGN.Center, TUI.LINES.First)
        println("Modo de manutenção. Escreva help para lista de comandos")
        while (maintenance) {
            val args = readln().trim().getargs()
            if (args.isNotEmpty()) {
                when (args.first().lowercase()) {
                    "help" -> {
                        println("ADDUSER                                                     | Adiciona um utilizador ao sistema.")
                        println("REMOVEUSER     --UIN                                        | Remove um utilizador do sistema.")
                        println("ADDMSG         --UIN           --message                    | Adiciona a mensagem ao utilizador específicado ao autenticar.")
                        println("CLOSE                                                      | Atualiza os utilizadores do sistema, permitindo que seja desligado.")
                    }

                    "adduser" -> {
                        var username = "                       "
                        while (username.length > 16) {
                            println("Insira o seu nome de utilizador: ")
                            username = readln().trim()
                            if(username.length > 16)
                                println("O seu nome de utilizador não deve exceder a quantidade de dígitos suportada pelo LCD (16).")
                        }
                        var pin = -1
                        while (pin == -1 || pin > 99999) {

                            try {
                                println("Insira o seu PIN: ")
                                pin = readln().toInt()
                            } catch (e: NumberFormatException) {
                                println("Insira apenas números na introdução do PIN.")
                            }

                            if (pin > 99999) println("O Pin não deve exceder 5 dígitos.")
                        }
                        val uin = Users.addUser(username, pin)

                        if (uin >= 0) println("Utilizador $uin $username adicionado com sucesso.")

                        else println("A lista de utilizadores encontra-se cheia.")
                    }

                    "removeuser" ->
                        if (args.size != REMOVEUSERARGS) println("Argumentos inválidos. Verifique os argumentos necessários utilizando help.") else {

                            val (cmd, UIN) = args

                            val user = Users.userlist[UIN.toInt()]

                            if (user == null) println("O utilizador não existe")

                            else {
                                println("${user.username}. Tem a certeza que quer remover este utilizador? (S/N)")

                                if (readln() in "SsYy") {
                                    Users.removeUser(UIN.toInt())
                                    println("Utilizador $UIN apagado com sucesso.")
                                }

                                else println("O utilizador não foi removido.")
                            }
                        }

                    "addmsg" ->
                        if (args.size != SETMSGARGS) println("Argumentos inválidos. Verifique os argumentos necessários utilizando help.") else {
                            val (cmd, UIN, message) = args
                            try {
                                val uin = UIN.toInt()
                                val ret  = Users.setMsg(message, uin)
                                if (ret < 0) println("O utilizador não existe")
                                else println("Mensagem entregue com sucesso")
                            }
                            catch (e: NumberFormatException) {
                                println("O UIN deve ser um número")
                            }
                        }

                    "close" -> {
                        Users.close()
                        println("Sistema atualizado. Pode sair do modo de manutenção.")
                    }
                    else -> println("Comando Inválido")
                 }
            }
            maintenance = HAL.isBit(MAINTENANCEMASK)
        }
        println("A sair do modo de manutenção...")

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