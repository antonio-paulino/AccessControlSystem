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


    data class User(val UID : Int, var firstname : String?, var lastname : String?,  var pin: Int?, var message : String?)
    const val SIZE = 1000
    var userlist = Array(SIZE) {User(it, null, null , null, null)}


    fun init() {
        val fileread = createReader("Users.txt")
        var lineargs = fileread.readLine().split(" ")
        while (validLine(lineargs))
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

    fun removeUser() {
        val outputfile = createReader("Users.txt")
    }

    fun setMsg(){

    }



    fun setPin(){

    }

    private fun validLine(lineargs : List<String>) =
            lineargs?.get(1) != null ||
            lineargs?.get(3) != null ||
            lineargs?.get(4) != null ||
            lineargs?.get(6) != null ||
            lineargs?.get(8) != null
}