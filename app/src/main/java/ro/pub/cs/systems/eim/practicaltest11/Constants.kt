package ro.pub.cs.systems.eim.practicaltest11

object Constants {
    const val tag = "PracticalTest11"
    const val LEFT_COUNT_STATE = "LEFT_COUNT_STATE"
    const val RIGHT_COUNT_STATE = "RIGHT_COUNT_STATE"
    const val SLEEP_TIME = 1000L
    const val BROADCAST_RECEIVER_EXTRA = "broadcast_receiver_extra"

    const val BROADCAST_RECEIVER_TAG = "[Message]"
    const val PROCESSING_THREAD_TAG = "[Processing Thread]";

    const val SERVICE_STOPPED = 0;
    const val SERVICE_STARTED = 1;

    val actionTypes = arrayOf(
        "ro.pub.cs.systems.eim.practicaltest01.arithmeticmean",
        "ro.pub.cs.systems.eim.practicaltest01.geometricmean"
    )

    const val NUMBER_OF_CLICKS_THRESHOLD = 5
}