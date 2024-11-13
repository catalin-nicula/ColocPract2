package ro.pub.cs.systems.eim.practicaltest11

import android.content.Context
import android.content.Intent
import android.os.Process
import android.util.Log
import ro.pub.cs.systems.eim.practicaltest11.Constants.BROADCAST_RECEIVER_EXTRA
import ro.pub.cs.systems.eim.practicaltest11.Constants.PROCESSING_THREAD_TAG
import ro.pub.cs.systems.eim.practicaltest11.Constants.actionTypes
import java.util.Date
import java.util.Random
import kotlin.math.sqrt

class ProcessingThread(private val context: Context, firstNumber: Int, secondNumber: Int) : Thread() {
    private var isRunning = true

    private val random = Random()

    private val arithmeticMean = (firstNumber + secondNumber).toDouble() / 2
    private val geometricMean = sqrt((firstNumber * secondNumber).toDouble())

    override fun run() {
        Log.d(
            PROCESSING_THREAD_TAG,
            "Thread has started! PID: " + Process.myPid() + " TID: " + Process.myTid()
        )
        while (isRunning) {
            sendMessage()
            sleep()
        }
        Log.d(PROCESSING_THREAD_TAG, "Thread has stopped!")
    }

    private fun sendMessage() {
        val intent = Intent()
        intent.setAction(actionTypes[random.nextInt(actionTypes.count())])
        intent.putExtra(
            BROADCAST_RECEIVER_EXTRA,
            Date(System.currentTimeMillis()).toString() + " " + arithmeticMean + " " + geometricMean
        )
        context.sendBroadcast(intent)
    }

    private fun sleep() {
        try {
            sleep(1000)
        } catch (interruptedException: InterruptedException) {
            interruptedException.printStackTrace()
        }
    }

    fun stopThread() {
        isRunning = false
    }
}