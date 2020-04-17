package practice.kotlin.com.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {
    var toast : Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()

        resultButton.setOnClickListener{

            if(heightEditText.text.isBlank() || weightEditText.text.isBlank()) {
                if(toast == null)
                    toast = Toast.makeText(this,R.string.blank, Toast.LENGTH_SHORT)
                else
                    toast!!.setText(R.string.blank)
                toast?.show()
            }
            else {
                saveData(
                    heightEditText.text.toString(),
                    weightEditText.text.toString()
                )

                startActivity<ResultActivity>(
                    "height" to heightEditText.text.toString(),
                    "weight" to weightEditText.text.toString()
                )
            }
        }

    }

    private fun saveData(height : String, weight: String){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()

        editor.putString("KEY_HEIGHT", height)
            .putString("KEY_WEIGHT", weight)
            .apply()
    }

    private fun loadData(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        // default = 0
        val height = pref.getString("KEY_HEIGHT", "0")
        val weight = pref.getString("KEY_WEIGHT", "0")

        if(height != "0" && weight != "0"){
            heightEditText.setText(height)
            weightEditText.setText(weight)
        }
    }
}
