import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter

data class User(val UIN: Int, var pin: Int, var firstname: String, var lastname: String, var message: String?)

fun main() {
    Users.init()
   // Users.addUser("J", "J", 80000)
    Users.removeUser(0)
    Users.close()
}


object Users {


    const val SIZE = 1000
    var userlist = arrayOfNulls<User>(SIZE)
    val OFFSET = 500

    fun init() {
        val fileread = FileAccess.createReader("Users.txt")
        var line: String? = fileread.readLine()
        while (line != null) {
            val lineargs = line.split(";")
            val (uin, pin, firstname, lastname, message) = lineargs
            val uin_Int = uin.toInt()
            userlist[uin_Int] = User(uin_Int, pin.toInt() - OFFSET , firstname, lastname, message)
            line = fileread.readLine()
        }
        fileread.close()
    }

    fun addUser(firstname: String, lastname: String, pin: Int) {
        if (pin > 99999) println("O Pin não deve exceder 5 dígitos")
        else {
            var uin = 0
            while (userlist[uin] != null) {
                uin++
            }
            if (uin < SIZE) {
                userlist[uin] = User(uin, pin, firstname, lastname, null)
            }
        }
    }


    fun removeUser(UIN: Int) { userlist[UIN] = null }

    fun setMsg(message: String, UIN: Int) {
        if (message.length > 16) println("A sua mensagem excede a quantidade de dígitos suportada pelo LCD (16)")
        else userlist[UIN] = userlist[UIN]!!.copy(message = message)
    }

    fun close() {
        val outputfile = FileAccess.createWriter("Users.txt")
        for (uin in 0 until SIZE) {
            val user = userlist[uin]
            if(user != null) {
                outputfile.println("${user.UIN};${user.pin + OFFSET};${user.firstname};${user.lastname};${user.message};")
            }
        }
        outputfile.close()
    }
}