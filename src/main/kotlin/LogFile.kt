
import java.text.SimpleDateFormat
import java.util.*


object LogFile {
    fun add(uin : Int) {
        val logWrite = FileAccess.createWriterAppend("LogFile.txt")
        val time = getDate()
        logWrite.write("$uin, $time\n")
        logWrite.close()
    }

    fun clear(){
        val logWrite = FileAccess.createWriter("LogFile.txt")
        logWrite.close()
    }

    fun getDate() : String {
        val date = Calendar.getInstance().time
        val dateformat = SimpleDateFormat("MMM 14, HH:mm")
        return dateformat.format(date)
    }
}