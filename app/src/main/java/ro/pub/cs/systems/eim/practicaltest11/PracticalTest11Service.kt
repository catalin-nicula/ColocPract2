package ro.pub.cs.systems.eim.practicaltest11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import ro.pub.cs.systems.eim.practicaltest11.ProcessingThread
import ro.pub.cs.systems.eim.practicaltest11.Constants.LEFT_COUNT_STATE
import ro.pub.cs.systems.eim.practicaltest11.Constants.RIGHT_COUNT_STATE
import java.util.Objects


class PracticalTest11Service : Service() {
    lateinit var processingThread: ProcessingThread
    override fun onCreate() {
        super.onCreate()

        val CHANNEL_ID = "my_channel_01"
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        (Objects.requireNonNull(getSystemService(NOTIFICATION_SERVICE)) as NotificationManager).createNotificationChannel(
            channel
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("")
            .setContentText("").build()

        startForeground(1, notification)

    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val firstNumber = intent.getIntExtra(LEFT_COUNT_STATE, -1)
        val secondNumber = intent.getIntExtra(RIGHT_COUNT_STATE, -1)
        processingThread = ProcessingThread(this, firstNumber, secondNumber)
        processingThread.start()
        Log.d("PracticalTest01Service", "onStartCommand() method was invoked with the following parameters: " + firstNumber + ", " + secondNumber)
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        processingThread.stopThread()
    }
}