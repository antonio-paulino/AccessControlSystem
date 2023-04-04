import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter

fun main() {
    Users.init()
   // Users.addUser("J", "J", 80000)
    Users.removeUser(0)
    Users.close()
}

fun createReader(fileName: String): BufferedReader { return BufferedReader(FileReader(fileName)) }

fun createWriter(fileName: String): PrintWriter { return PrintWriter(fileName) }

object Users {
    data class User(val UIN: Int, var pin: Int, var firstname: String, var lastname: String, var message: String)

    const val SIZE = 1000
    var userlist = arrayOfNulls<User>(SIZE)

    fun init() {
        val fileread = createReader("Users.txt")
        var line: String? = fileread.readLine()
        while (line != null) {
            val lineargs = line.split(";")
            val (uin, pin, firstname, lastname, message) = lineargs
            val uin_Int = uin.toInt()
            userlist[uin_Int] = User(uin_Int, pin.toInt(), firstname, lastname, message)
            line = fileread.readLine()
        }
        fileread.close()
    }

    fun addUser(firstname: String, lastname: String, pin: Int) {
        var uin = 0
        while (userlist[uin] != null) { uin++ }
        if (uin < SIZE) {
            userlist[uin] = User(uin, pin, firstname, lastname, "NONE")
        }
    }


    fun removeUser(UIN: Int) { userlist[UIN] = null }

    fun setMsg(message: String, UIN: String) {
        val idx = UIN.toInt()
        if (userlist[idx] == null)
            println("Utilizador não existente")
        else userlist[idx] = userlist[idx]!!.copy(message = message)
    }

    fun close() {
        val outputfile = createWriter("Users.txt")
        for (uin in 0 until SIZE) {
            val user = userlist[uin]
            if(user != null) {
                outputfile.println("${user.UIN};${user.pin};${user.firstname};${user.lastname};${user.message};")
            }
        }
        outputfile.close()
    }
}