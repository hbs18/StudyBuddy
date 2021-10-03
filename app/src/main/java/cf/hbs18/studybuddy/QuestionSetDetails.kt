package cf.hbs18.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import java.io.File

const val EXTRA_MESSAGE2 = "cf.hbs18.studybuddy.MESSAGE2"

lateinit var path:String

class QuestionSetDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_set_details)
        setTitle("Question set details")
        path = intent.getStringExtra(EXTRA_MESSAGE).toString()
        findViewById<TextView>(R.id.text_view_locationentry).text = path

        if (path != null) {
            selected_file = path
        }
        val f = File(selected_file)
        val pitanja = f.bufferedReader().use { it.readText() }
        val pitanja_array = pitanja.lines()
        findViewById<TextView>(R.id.text_view_2).text = pitanja_array[0]

        findViewById<TextView>(R.id.text_view_numbertitle).text =
            (pitanja_array.size - 2).toString() + " questions"

        val fab: View = findViewById(R.id.fab)
        /*fab.setOnClickListener { view ->
            Snackbar.make(view, path, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }
    }*/
        fab.setOnClickListener { view ->
            val intent = Intent(this, EditQuestionSet::class.java).apply {
                putExtra(EXTRA_MESSAGE3, path)
            }
            startActivity(intent)
        }
    }

    fun randomQuestions(view: View){
        val f = File("/storage/emulated/0/cf.hbs18.studybuddy")
        val files: Array<File> = f.listFiles()
        val length=(files.size-1)
        val list = ArrayList<MenuItem>()
        val toast_text=path
        val intent = Intent(this, InterrogationView::class.java).apply {
            putExtra(EXTRA_MESSAGE2, toast_text)
        }
        startActivity(intent)
        return
    }

    fun sequentialQuestions(view: View){
        val f = File("/storage/emulated/0/cf.hbs18.studybuddy")
        val files: Array<File> = f.listFiles()
        val length=(files.size-1)
        val list = ArrayList<MenuItem>()
        val toast_text=path
        val intent = Intent(this, SequentialInterrogation::class.java).apply {
            putExtra(EXTRA_MESSAGE2, toast_text)
        }
        startActivity(intent)
        return
    }
}