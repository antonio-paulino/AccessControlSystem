import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter

object FileAccess {
    private fun createReader(fileName: String): BufferedReader = BufferedReader(FileReader(fileName))

    private fun createWriter(fileName: String): PrintWriter = PrintWriter(fileName)

    private fun createWriterAppend (fileName: String) : FileWriter =  FileWriter(fileName, true)


    fun outToFile(lines : List<String>, fileName: String) {
        val outFile = createWriter(fileName)

        for(line in lines) {
            outFile.println(line)
        }

        outFile.close()
    }

    fun inFromFile(fileName: String) : List<String> {
        val inFile = createReader(fileName)
        return inFile.readLines()
    }

    fun appendFile(line : String, fileName : String) {
        val outFile = createWriterAppend(fileName)
        outFile.write(line)
    }

}