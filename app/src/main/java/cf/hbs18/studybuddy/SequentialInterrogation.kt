package cf.hbs18.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import java.io.File
import kotlin.properties.Delegates
import kotlin.random.Random

var i_seq = 0;
//lateinit var pitanja:String;
lateinit var selected_file_seq:String;
lateinit var pitanja_noTitle_seq:List<String>;
var num_pitanja_seq by Delegates.notNull<Int>();

class SequentialInterrogation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        i_seq=0;
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sequential_interrogation)
        val message = intent.getStringExtra(EXTRA_MESSAGE2)
        if (message != null) {
            selected_file_seq = message
        }
        val f = File(selected_file_seq)
        val pitanja_seq = f.bufferedReader().use { it.readText() }
        val pitanja_array_seq=pitanja_seq.lines()
        pitanja_noTitle_seq=stripTitle(pitanja_array_seq, 0)
        num_pitanja_seq = pitanja_noTitle_seq.size

        setTitle(pitanja_array_seq[0]);



        findViewById<TextView>(R.id.textView_progress).isVisible=false
        findViewById<TextView>(R.id.progressBarQuestions).isVisible=false

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
        findViewById<TextView>(R.id.currentQuestion_seq).setAnimation(animation)    //fade animacija za pitanje text view


        findViewById<TextView>(R.id.textView_progress).isVisible=true
        findViewById<TextView>(R.id.progressBarQuestions).isVisible=true
        findViewById<TextView>(R.id.textView_progress).text = "Question "+(i_seq+1).toString()+" of "+ (num_pitanja_seq-1).toString()

        val questionsProgress = findViewById<ProgressBar>(R.id.progressBarQuestions)
        val questionsPercentage = (i_seq+1)/(num_pitanja_seq-1).toFloat()
        questionsProgress.setProgress((questionsPercentage*100).toInt(), true)

        findViewById<TextView>(R.id.currentQuestion_seq).text = pitanja_noTitle_seq[i_seq].toString()
        i_seq=i_seq+1
        if (i_seq> num_pitanja_seq-1) {
            i_seq=0
            findViewById<TextView>(R.id.currentQuestion_seq).text = "You have reached the end of this question set."
            findViewById<TextView>(R.id.textView_progress).isVisible=false
            findViewById<TextView>(R.id.progressBarQuestions).isVisible=false
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