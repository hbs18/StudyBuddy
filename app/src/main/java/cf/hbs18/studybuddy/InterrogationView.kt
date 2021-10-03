package cf.hbs18.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import java.io.File
import kotlin.properties.Delegates
import kotlin.random.Random

var i = 0;
//lateinit var pitanja:String;
lateinit var selected_file:String;
lateinit var pitanja_array_shuffled:List<String>;
var num_pitanja by Delegates.notNull<Int>();

class InterrogationView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        i=0;
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interrogation_view)
        val message = intent.getStringExtra(EXTRA_MESSAGE2)
        if (message != null) {
            selected_file = message
        }
        val f = File(selected_file)
        val pitanja = f.bufferedReader().use { it.readText() }
        val pitanja_array=pitanja.lines()
        val pitanja_noTitle=stripTitle(pitanja_array, 0)
        pitanja_array_shuffled=pitanja_noTitle.shuffled()
        num_pitanja = pitanja_noTitle.size
        val textView = findViewById<TextView>(R.id.textView).apply {
            if (message != null) {
                text = "Loaded file: " + message.substringAfterLast("/storage/emulated/0/cf.hbs18.studybuddy/")
            }
        }
        setTitle(pitanja_array[0]);
    }

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        val rand = Random(System.nanoTime())
        return (start..end).random(rand)
    }



    fun nextQuestion(view: View){
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 512
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.startOffset = 512
        fadeOut.duration = 512
        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        findViewById<TextView>(R.id.currentQuestion).setAnimation(animation)    //fade animacija za pitanje text view

        findViewById<TextView>(R.id.currentQuestion).text = pitanja_array_shuffled[i].toString()
        i=i+1
        if (i>num_pitanja-1) {
            i=0
            shuffleQuestionSet()
        }
    }

    fun shuffleQuestionSet(){
        pitanja_array_shuffled = pitanja_array_shuffled.shuffled()
        return
    }

    fun stripTitle(arr: List<String>, index: Int): List<String> {
        if (index < 0 || index >= arr.size) {
            return arr
        }

        val result = arr.toMutableList()
        result.removeAt(index)
        return result
    }
}