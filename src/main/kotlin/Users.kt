import java.io.*
import java.util.*
import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter


fun main(){

}

fun createReader(fileName: String): BufferedReader {
    return BufferedReader(FileReader(fileName))
}

fun createWriter(fileName: String?): PrintWriter {
    return PrintWriter(fileName)
}


object Users{
    data class User(val UIN : String, var firstname : String, var lastname : String,  var pin: String, var message : String)
    const val SIZE = 1000
    var userlist = arrayOfNulls<User>(SIZE)

    fun init() {
        val fileread = createReader("Users.txt")
        var line : String? = fileread.readLine()
        while (line != null) {
            var lineargs = line.split(";")
            val uid = if (lineargs[1].length == 1) "00${lineargs[1]}" else if(lineargs[1].length == 2) "0${lineargs[1]}" else lineargs[1]
            userlist[uid.toInt()] = User(uid, lineargs[2], lineargs[4], lineargs[6], lineargs[8])
            line = fileread.readLine()
            lineargs = line.split("")

        }
        fileread.close()
    }

    fun addUser(firstname: String, lastname: String, pin: String, message: String) {
        val outputfile = createWriter("Users.txt")

        for (i in 0..userlist.size-1) {
         if(userlist[i] == null) {
             val uidstring = i.toString()
             val uid = if (uidstring.length == 1) "00$uidstring" else if (uidstring.length == 2) "0$uidstring" else uidstring
             val newUser = userlist.set(i, User(uid, firstname = firstname, lastname = lastname, pin = pin, message = "None"))

         }
        }
    }

    //fun User.addMsg =

    fun removeUser() {
        val outputfile = createReader("Users.txt")
    }

}