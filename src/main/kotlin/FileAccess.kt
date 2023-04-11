import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter

object FileAccess {
    fun createReader(fileName: String): BufferedReader = BufferedReader(FileReader(fileName))

    fun createWriter(fileName: String): PrintWriter = PrintWriter(fileName)

    fun createWriterAppend (fileName: String) : FileWriter =  FileWriter(fileName, true)
}