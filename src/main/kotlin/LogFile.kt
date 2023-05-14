import java.text.SimpleDateFormat
import java.util.*


object LogFile {

    fun add(uin: Int) {
        val logWrite = FileAccess.createWriterAppend("LogFile.txt")
        val time = getDate()
        val user = Users.userlist[uin]!!
        logWrite.write("$uin, ${user.username} $time\n")
        logWrite.close()
    }

    fun getDate(): String {
        val date = Calendar.getInstance().time
        val dateformat = SimpleDateFormat("MMM dd HH:mm")
        return dateformat.format(date)
    }
}