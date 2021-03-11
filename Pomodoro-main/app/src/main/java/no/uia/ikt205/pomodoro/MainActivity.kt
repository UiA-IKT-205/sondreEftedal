package no.uia.ikt205.pomodoro

import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import no.uia.ikt205.pomodoro.util.minutesToMilliSeconds

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var coutdownDisplay:TextView
    lateinit var setTimeInMinSeekBar: SeekBar
    lateinit var viewSeekBarProgress:TextView
    lateinit var pauseButton: Button
    lateinit var resumeButton:Button
    lateinit var setPauseTimeInMinSeekBar: SeekBar
    lateinit var setNumberOfBreaks:EditText
    lateinit var viewPauseSeekbarProgress:TextView
    lateinit var pauseTimer:CountDownTimer
    var isTimerRunning = false



    var pauseTimeCountdownInMs = 0L
    val timeTicks = 1000L
    var timeToCountDownInMs = 5000L
    var isPaused = false
    var resumeFromMillis = 0L
    var pauseTimeInMs = 0L
    var numberOfBreaks:Int = 0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coutdownDisplay = findViewById(R.id.countDownView)
        setTimeInMinSeekBar = findViewById(R.id.setTimerSeekbar)
        viewSeekBarProgress = findViewById(R.id.viewSeekBarProgress)
        pauseButton = findViewById(R.id.pauseButton)
        startButton = findViewById<Button>(R.id.startCountdownButton)
        setPauseTimeInMinSeekBar = findViewById(R.id.pauseTimeSeekbar)
        viewPauseSeekbarProgress = findViewById(R.id.viewPauseTimeProgress)


        startButton.setOnClickListener(){

            pauseButton.isEnabled = true
            isPaused = false
            startButton.isEnabled = false
            resumeButton.isEnabled = false
            getNumberofBreaks()
            startCountDown(it)
        }

        pauseButton.setOnClickListener() {
            pauseCountDown(it)
            resumeFromMillis = coutdownDisplay.drawingTime
            resumeButton.isEnabled = true
        }

        resumeButton = findViewById(R.id.resumeButton)
        resumeButton.setOnClickListener(){
            resumeCountdown(it)
            startCountDown(it)
        }

        setTimeInMinSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                timeToCountDownInMs = minutesToMilliSeconds(progress.toLong())
                viewSeekBarProgress.text = progress.toString() + " Minutter"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        setPauseTimeInMinSeekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pauseTimeInMs = minutesToMilliSeconds(progress.toLong())
                viewPauseSeekbarProgress.text = progress.toString() + "Minutter"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

    }


    fun startCountDown(v: View){
            timer = object : CountDownTimer((timeToCountDownInMs/(numberOfBreaks+1).toLong()),timeTicks) {


                override fun onFinish() {
                    Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig, ta pause nå", Toast.LENGTH_SHORT).show()
                    if(numberOfBreaks > 0)
                        startPauseTimer(v)
                }

                override fun onTick(millisUntilFinished: Long) {

                    updateCountDownDisplay(millisUntilFinished)
                    timeToCountDownInMs = millisUntilFinished
                    isTimerRunning = false


                }
            }

            timer.start()}


    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)

    }
    fun updatePauseTimer(pauseTimeInMs: Long){
        pauseTimeCountdownInMs = pauseTimeInMs
        updateCountDownDisplay(pauseTimeInMs)
    }
    fun pauseCountDown(v: View){
        isPaused = true
        timer.cancel()
        pauseButton.isEnabled = false

    }
    fun resumeCountdown(v: View){
        isPaused = false
        pauseButton.isEnabled = true
    }
    private fun getNumberofBreaks(): Int {
        val setNumberOfBreaks = findViewById<EditText>(R.id.setNumberOfBreaks)
        if(setNumberOfBreaks != null){
        numberOfBreaks = setNumberOfBreaks.text.toString().toInt()

    }
        return numberOfBreaks}


    fun startPauseTimer(v: View){
        isTimerRunning = true
        pauseButton.isEnabled =false

        pauseTimer = object : CountDownTimer(pauseTimeInMs, timeTicks) {
            override fun onTick(millisUntilFinished: Long) {
                updatePauseTimer(millisUntilFinished)
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Pausen er ferdig, ny arbeidsøkt starter", Toast.LENGTH_SHORT).show()
                isTimerRunning = false
                if(numberOfBreaks > 0){
                    numberOfBreaks --
                   startCountDown(v)
                }
                isTimerRunning = false
                pauseButton.isEnabled = true
            }
    }
        pauseTimer.start()
    }
}





