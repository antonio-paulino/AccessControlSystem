import java.text.SimpleDateFormat
import java.util.*

/**
 * 22/5/2023
 *
 * Registers the authenticated user and their time of entry to a file.
 * @author Bernardo Pereira
 * @author António Paulino
 * @see Users
 * @see FileAccess
 */
object LogFile {


    /**
     * Adds a log entry to the log file
     * @param uin the UIN (Universal Identification Number) of the authenticated user
     */
    fun add(uin: Int) {
        val time = getDate(false)
        val user = Users.userlist[uin]!!
        FileAccess.appendFile("$time -> ${user.UIN}, ${user.username} \n", "LOG.txt")
    }


    /**
     * Gets the date of the log entry or the date to display on the LCD when the [AccessControlSystem] is on.
     *
     * @param display Whether to return the date to display on the LCD (true) or the date to write to the log file (false).
     * @return The formatted date to display on the LCD or write to the log file.
     */
    fun getDate(display: Boolean): String {
        val date = Calendar.getInstance().time
        val dateformat = if (display) SimpleDateFormat("MMM dd HH:mm")
        else SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return dateformat.format(date)
    }
}