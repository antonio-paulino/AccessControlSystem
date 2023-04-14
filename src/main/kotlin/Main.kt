
fun waitTimeNano(time : Int) {
    val endtime = System.nanoTime() + time
    while(System.nanoTime() <= endtime);
}
fun waitTimeMilli(time : Int) {
    val endtime = System.currentTimeMillis() + time
    while(System.currentTimeMillis() <= endtime);
}
