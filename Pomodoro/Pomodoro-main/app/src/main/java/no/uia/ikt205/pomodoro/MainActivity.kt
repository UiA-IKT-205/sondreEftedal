package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var coutdownDisplay:TextView
    lateinit var ninetymin:Button
    lateinit var thirtymin:Button
    lateinit var sixtymin:Button
    lateinit var hundredandtwentymin:Button

    //val timeToCountDownInMs = 5000L
    val timeTicks = 1000L
    var timeToCountDownInMs = 5000L



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       startButton = findViewById<Button>(R.id.startCountdownButton)
       startButton.setOnClickListener(){
           startCountDown(it)
       }
       coutdownDisplay = findViewById<TextView>(R.id.countDownView)
        val ninety = findViewById<Button>(R.id.nintymin)
        ninety.setOnClickListener(){
            timeToCountDownInMs = 90*60*1000
        }

        val thirty = findViewById<Button>(R.id.thirtymin)
        thirty.setOnClickListener(){
            timeToCountDownInMs = 30*60*1000
        }

        val sixty = findViewById<Button>(R.id.sixtymin)
        sixty.setOnClickListener(){
            timeToCountDownInMs = 60*60*1000
        }

        val hundredandtwenty = findViewById<Button>(R.id.hundredandtwentymin)
        hundredandtwenty.setOnClickListener(){
            timeToCountDownInMs = 120*60*1000
        }

    }


    fun startCountDown(v: View){
        if(coutdownDisplay.text == "00:00:00"){
        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
            }

            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()}
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}