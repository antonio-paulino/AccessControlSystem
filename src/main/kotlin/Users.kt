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

    fun addUser() {

    }

    fun removeUser() {

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