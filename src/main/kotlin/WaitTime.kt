

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
    val endtime = System.currentTimeMillis() + time
    while (System.currentTimeMillis() <= endtime);
}
