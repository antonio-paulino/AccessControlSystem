import java.text.SimpleDateFormat
import java.util.*


object LogFile {

    fun add(uin: Int) {
        val logWrite = FileAccess.createWriterAppend("LogFile.txt")
        val time = getDate(false)
        val user = Users.userlist[uin]!!
        logWrite.write("$time ->\n")
        logWrite.close()
    }

    fun getDate(display : Boolean): String {
        val date = Calendar.getInstance().time
        val dateformat = if(display) SimpleDateFormat("MMM dd HH:mm")
                         else        SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return dateformat.format(date)
    }
}