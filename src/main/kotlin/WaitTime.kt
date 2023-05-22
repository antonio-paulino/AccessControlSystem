import isel.leic.utils.Time


/**
 * 22/5/2023
 *
 * Sleep time for functions that
 * do continuous checks. For example, checking if the door is open/closed, or checking
 * if there was a key press.
 *
 * Having a sleep time between checks for these functions allows the system to use less resources.
 *
 *
 */
const val CHECK_INTERVAL = 50

/**
 * Waits a set number of nanoseconds
 * @param time the number of nanoseconds to wait
 */
fun waitTimeNano(time: Int) {
    val endtime = System.nanoTime() + time
    while (System.nanoTime() <= endtime);
}


/**
 * Waits a set number of milliseconds
 * @param time the number of milliseconds to wait
 */
fun waitTimeMilli(time: Int) {
    Time.sleep(time.toLong())
}
