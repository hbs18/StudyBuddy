package cf.hbs18.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream

class EditQuestionSet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_question_set)

        setTitle("Question set editor")

        if (intent.getStringExtra(EXTRA_MESSAGE3) == "nothing") {

            findViewById<TextView>(R.id.pathOfFileBeingEdited).text = "Editing a new question set"

        }
        else{
            findViewById<EditText>(R.id.editTextFileName).visibility = View.GONE
            path = intent.getStringExtra(EXTRA_MESSAGE3).toString()

            findViewById<TextView>(R.id.pathOfFileBeingEdited).text = "Path: "+ path

            if (path != null) {
                selected_file = path
            }
            val f = File(selected_file)
            val file_text = f.bufferedReader().use { it.readText() }

            findViewById<EditText>(R.id.editTextTextMultiLine).setText(file_text, TextView.BufferType.EDITABLE)
        }

    }

    fun saveFile(view: View){
        if(intent.getStringExtra(EXTRA_MESSAGE3) == "nothing") {
            var filename = findViewById<EditText>(R.id.editTextFileName).text
            if (findViewById<EditText>(R.id.editTextFileName).text.toString() == "" || findViewById<EditText>(R.id.editTextFileName).text.toString() == null){
                Snackbar.make(view, "You need to set a file name!", Snackbar.LENGTH_LONG)
                    .setAction("You need to set a file name!", null)
                    .show()
            }
            else {
                var newPath = "/storage/emulated/0/cf.hbs18.studybuddy/" + filename + ".txt"

                val file = File(newPath)
                if(file.exists()){
                    Snackbar.make(view, "Error, this file name already exists.", Snackbar.LENGTH_LONG)
                        .setAction("Error, this file name already exists.", null)
                        .show()
                }
                else {
                    val contents = findViewById<EditText>(R.id.editTextTextMultiLine).text.toString()
                    val stream = FileOutputStream(file)
                    try {
                        stream.write(contents.toByteArray())
                        Snackbar.make(view, "File saved!", Snackbar.LENGTH_LONG)
                            .setAction("File saved!", null)
                            .show()
                    } finally {
                        stream.close()
                    }
                }
            }
        }

        else {
            val file = File(intent.getStringExtra(EXTRA_MESSAGE3).toString())
            val contents = findViewById<EditText>(R.id.editTextTextMultiLine).text.toString()
            val stream = FileOutputStream(file)
            try {
                stream.write(contents.toByteArray())
                Snackbar.make(view, path, Snackbar.LENGTH_LONG)
                    .setAction("File saved!", null)
                    .show()
            } finally {
                stream.close()
            }
        }

    }
}