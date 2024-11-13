package ro.pub.cs.systems.eim.practicaltest11

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest11SecondaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test11_secondary)

        val clickSum = findViewById<TextView>(R.id.clickSum)
        val input1 = intent.getIntExtra(Constants.LEFT_COUNT_STATE, 0)
        val input2 = intent.getIntExtra(Constants.RIGHT_COUNT_STATE, 0)
        val sum = input1 + input2
        clickSum.text = sum.toString()

        val okButton = findViewById<Button>(R.id.buttonOk)
        okButton.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        val cancelButton = findViewById<Button>(R.id.buttonCancel)
        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}