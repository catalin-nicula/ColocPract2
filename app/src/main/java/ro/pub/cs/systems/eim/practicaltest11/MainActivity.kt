package ro.pub.cs.systems.eim.practicaltest11

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ro.pub.cs.systems.eim.practicaltest11.Constants.BROADCAST_RECEIVER_EXTRA
import ro.pub.cs.systems.eim.practicaltest11.Constants.BROADCAST_RECEIVER_TAG
import ro.pub.cs.systems.eim.practicaltest11.Constants.LEFT_COUNT_STATE
import ro.pub.cs.systems.eim.practicaltest11.Constants.NUMBER_OF_CLICKS_THRESHOLD
import ro.pub.cs.systems.eim.practicaltest11.Constants.RIGHT_COUNT_STATE
import ro.pub.cs.systems.eim.practicaltest11.Constants.SERVICE_STARTED
import ro.pub.cs.systems.eim.practicaltest11.Constants.SERVICE_STOPPED
import ro.pub.cs.systems.eim.practicaltest11.Constants.actionTypes
import java.util.Objects

class MainActivity : AppCompatActivity() {
    lateinit var leftEditText: EditText
    lateinit var rightEditText: EditText
    lateinit var pressMeButton: Button
    lateinit var pressMeTooButton: Button
    private val intentFilter = IntentFilter()
    private var serviceStatus: Int = SERVICE_STOPPED

    private val buttonClickListener = ButtonClickListener()

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            var leftNumberOfClicks = leftEditText.text.toString().toInt()
            var rightNumberOfClicks = rightEditText.text.toString().toInt()

            when (view.id) {
                R.id.buttonPressMe -> {
                    leftNumberOfClicks++
                    leftEditText.setText(leftNumberOfClicks.toString())
                }

                R.id.buttonPressMeToo -> {
                    rightNumberOfClicks++
                    rightEditText.setText(rightNumberOfClicks.toString())
                }
            }
            if (leftNumberOfClicks + rightNumberOfClicks > NUMBER_OF_CLICKS_THRESHOLD) {
                if(serviceStatus == SERVICE_STOPPED){
                    val intent = Intent(
                        applicationContext,
                        PracticalTest11Service::class.java
                    )
                    intent.putExtra(LEFT_COUNT_STATE, leftNumberOfClicks)
                    intent.putExtra(RIGHT_COUNT_STATE, rightNumberOfClicks)
                    applicationContext.startService(intent)
                    serviceStatus = SERVICE_STARTED
                }
                else{
                    val stopIntent = Intent(
                        applicationContext,
                        PracticalTest11Service::class.java
                    )
                    stopService(stopIntent)
                    val intent = Intent(
                        applicationContext,
                        PracticalTest11Service::class.java
                    )
                    intent.putExtra(LEFT_COUNT_STATE, leftNumberOfClicks)
                    intent.putExtra(RIGHT_COUNT_STATE, rightNumberOfClicks)
                    applicationContext.startService(intent)
                    serviceStatus = SERVICE_STARTED
                }
            }
        }
    }

    private val messageBroadcastReceiver = MessageBroadcastReceiver()
    private class MessageBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(
                BROADCAST_RECEIVER_TAG, Objects.requireNonNull<String?>(
                    intent.getStringExtra(
                        BROADCAST_RECEIVER_EXTRA
                    )
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        leftEditText = findViewById<EditText>(R.id.editText1)
        rightEditText = findViewById<EditText>(R.id.editText2)

        pressMeButton = findViewById<Button>(R.id.buttonPressMe)
        pressMeTooButton = findViewById<Button>(R.id.buttonPressMeToo)

        pressMeButton.setOnClickListener(buttonClickListener)
        pressMeTooButton.setOnClickListener(buttonClickListener)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LEFT_COUNT_STATE)) {
                leftEditText.setText(savedInstanceState.getString(LEFT_COUNT_STATE));
            } else {
                leftEditText.setText("0");
            }
            if (savedInstanceState.containsKey(RIGHT_COUNT_STATE)) {
                rightEditText.setText(savedInstanceState.getString(RIGHT_COUNT_STATE));
            } else {
                rightEditText.setText("0");
            }
        } else {
            leftEditText.setText("0");
            rightEditText.setText("0");
        }

        val activityResultsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "The activity returned with OK", Toast.LENGTH_LONG).show()
            }
            else if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "The activity returned with CANCELED", Toast.LENGTH_LONG).show()
            }
        }

        val navigateToSecondaryButton = findViewById<Button>(R.id.navigateToSecondary);
        navigateToSecondaryButton.setOnClickListener {
            val intent = Intent(this, PracticalTest11SecondaryActivity::class.java)
            intent.putExtra(LEFT_COUNT_STATE, Integer.valueOf(leftEditText.text.toString()))
            intent.putExtra(RIGHT_COUNT_STATE, Integer.valueOf(rightEditText.text.toString()))
            activityResultsLauncher.launch(intent)
        }

        for (index in actionTypes.indices) {
            intentFilter.addAction(actionTypes[index])
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString(LEFT_COUNT_STATE, leftEditText.text.toString())
        savedInstanceState.putString(RIGHT_COUNT_STATE, rightEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(LEFT_COUNT_STATE)) {
            leftEditText.setText(savedInstanceState.getString(LEFT_COUNT_STATE))
        } else {
            leftEditText.setText("0")
        }
        if (savedInstanceState.containsKey(RIGHT_COUNT_STATE)) {
            rightEditText.setText(savedInstanceState.getString(RIGHT_COUNT_STATE))
        } else {
            rightEditText.setText("0")
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }
    override fun onPause() {
        unregisterReceiver(messageBroadcastReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        val intent = Intent(
            this,
            PracticalTest11Service::class.java
        )
        stopService(intent)
        super.onDestroy()
    }
}