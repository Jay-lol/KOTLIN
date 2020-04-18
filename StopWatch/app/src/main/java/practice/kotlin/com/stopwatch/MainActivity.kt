package practice.kotlin.com.stopwatch

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0
    private var timerTask: Timer? = null
    private var isRunning = false
    private var lap = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ab: ActionBar? = supportActionBar
        ab?.setTitle("StopWatch")


        fab.setOnClickListener {
            isRunning = !isRunning      // 누르면 반대로
            if(isRunning){
                start()     // start 함수 호출
            } else{
                pause()     // pause 함수 호출
            }
        }

        lapButton.setOnClickListener{
            lapRecord()
        }

        resetFab.setOnClickListener {
            reset()
        }
    }

    private fun reset(){
        timerTask?.cancel()
        time = 0
        isRunning = false

        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp)

        secTextView.text = "0"
        milliTextView.text = "00"


        lapLayout.removeAllViews()
        lap = 1
    }

    private fun start() {
        fab.setImageResource(R.drawable.ic_pause_black_24dp)

        timerTask = timer(period = 10) {
            time++
            val sec = time / 100
            val milli = time % 100

            runOnUiThread {
                if (isRunning) {    // 조건문이 없으면 reset버튼시 텍스트 0으로 바꾸는 부분이랑 충돌 가능성 생김.
                    secTextView.text = "$sec"
                    milliTextView.text = "$milli"
                }
            }
        }
    }

    private fun pause(){
        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        timerTask?.cancel()
    }

    private fun lapRecord(){
        val lapTime = this.time
        val textView = TextView(this)
        textView.text = "$lap LAP : ${lapTime / 100}.${lapTime % 100}"
        textView.setTextColor(getColor(R.color.textColor))      // R.color만 사용하면 안됨. getColor를 사용
        // 맨 위로 쌓이게
        lapLayout.addView(textView,0)
        lap++
    }
}
