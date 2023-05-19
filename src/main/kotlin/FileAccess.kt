import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter


/**
 * Provides functionality for reading and writing to a file.
 * @author Bernardo Pereira
 * @author António Paulino
 */
object FileAccess {

    /**
     * Creates a BufferedReader for reading from the specified file
     * @param fileName The name of the file to read from
     * @return A BufferedReader associated with the file.
     */
    private fun createReader(fileName: String): BufferedReader = BufferedReader(FileReader(fileName))

    /**
     * Creates a PrintWriter for writing to the specified file
     * @param fileName The name of the file to write to
     * @return A PrintWriter associated with the file.
     */
    private fun createWriter(fileName: String): PrintWriter = PrintWriter(fileName)


    /**
     * Creates a FileWriter for appending to the specified file
     * @param fileName The name of the file to append to
     * @return A FileWriter associated with the file.
     */

    private fun createWriterAppend (fileName: String) : FileWriter =  FileWriter(fileName, true)


    /**
     * Writes the provided lines to the specified file
     * @param lines The lines to write
     * @param fileName The name of the file to write to
     */
    fun outToFile(lines : List<String>, fileName: String) {
        val outFile = createWriter(fileName)

        for(line in lines) {
            outFile.println(line)
        }

        outFile.close()
    }

    /**
     * Reads all the lines from the specified file
     * @param fileName The name of the file to read from
     * @return A list of strings with all the lines from the file.
     */
    fun inFromFile(fileName: String) : List<String> {
        val inFile = createReader(fileName)
        val lines = inFile.readLines()
        inFile.close()
        return lines
    }

    /**
     * Appends the provided line to the specified file
     * @param fileName The name of the file to append to
     * @param line The line to append
     *
     */
    fun appendFile(line : String, fileName : String) {
        val outFile = createWriterAppend(fileName)
        outFile.write(line)
        outFile.close()
    }

}