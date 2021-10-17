package cf.hbs18.studybuddy

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val EXTRA_MESSAGE = "cf.hbs18.studybuddy.MESSAGE"
const val EXTRA_MESSAGE3 = "cf.hbs18.studybuddy.MESSAGE3"

lateinit var currentPath : String

class QuestionSetPicker : AppCompatActivity(), MenuAdapter.OnItemClickListener {
    private var exampleList = generateFileList()
    private var adapter = MenuAdapter(exampleList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_set_picker)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)     //true

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            createNewQuestionSet()
        }
    }

    override fun onResume() {
        super.onResume()
        exampleList = generateFileList()
        adapter = MenuAdapter(exampleList, this)
        recyclerView.adapter = adapter
        recyclerView.invalidate()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_question_set_picker, menu)
        return true
    }

    private fun generateFileList(): List<MenuItem> {
        val f = File("/storage/emulated/0/cf.hbs18.studybuddy")
        val files: Array<File> = f.listFiles()
        val length=(files.size-1)
        val list = ArrayList<MenuItem>()
        for (i in 0..length) {
            //open file, get first line, set it as menu item
            val file=File("/storage/emulated/0/cf.hbs18.studybuddy/" + files[i].name)
            val entry_title = file.bufferedReader().use { it.readLine() }

            //val item = MenuItem(files[i].name)
            val item = MenuItem(entry_title)
            list += item
        }
        return list
    }

    override fun onItemClick(position: Int) {
        //get list of files and build path of selected file
        val f = File("/storage/emulated/0/cf.hbs18.studybuddy")
        val files: Array<File> = f.listFiles()
        val toast_text="/storage/emulated/0/cf.hbs18.studybuddy/"+files[position].name  //this is the picked file's path
        currentPath = toast_text

        //read selected file so bottom info sheet text views can be set
        if (toast_text != null) {
            selected_file = toast_text
        }
        val file2 = File(selected_file)
        val pitanja = file2.bufferedReader().use { it.readText() }
        val pitanja_array = pitanja.lines()



        //inflate and show the bottom sheet
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        dialog.setContentView(view)
        dialog.show()

        //now set text views
        //you gotta use view. prefix here! source: https://stackoverflow.com/questions/31959101/set-text-in-textview-in-custom-dialog#comment51827229_31959501
        view.findViewById<TextView>(R.id.bottomSheet_setLocation).text = toast_text     //set set location text
        view.findViewById<TextView>(R.id.bottomSheet_questionSetTitle).text = pitanja_array[0]      //set title text
        view.findViewById<TextView>(R.id.bottomSheet_numOfQuestions).text = (pitanja_array.size - 1).toString() + " questions"      //set no. of questions

        //set on click listeners for other menu options
        var buttonRandom = view.findViewById<LinearLayout>(R.id.bottomSheetItem5)
        var buttonSequential = view.findViewById<LinearLayout>(R.id.bottomSheetItem4)
        var buttonEdit = view.findViewById<LinearLayout>(R.id.bottomSheetItem6)
        var buttonDelete = view.findViewById<LinearLayout>(R.id.bottomSheetItem7)
        var emptyNotificationText = view.findViewById<LinearLayout>(R.id.bottomSheetEmptySetNotification)
        var questionsCount = view.findViewById<LinearLayout>(R.id.bottomSheetItem2)

        buttonEdit.setOnClickListener { view ->
            val intent = Intent(this, EditQuestionSet::class.java).apply {
                putExtra(EXTRA_MESSAGE3, currentPath)
            }
            startActivity(intent)
            dialog.dismiss()
        }

        buttonRandom.setOnClickListener { view ->
            val intent = Intent(this, InterrogationView::class.java).apply {
                putExtra(EXTRA_MESSAGE2, currentPath)
            }
            startActivity(intent)
            dialog.dismiss()
        }

        buttonSequential.setOnClickListener { view ->
            val intent = Intent(this, SequentialInterrogation::class.java).apply {
                putExtra(EXTRA_MESSAGE2, currentPath)
            }
            startActivity(intent)
            dialog.dismiss()
        }

        buttonDelete.setOnClickListener { view ->
            showDefaultDialog(position)
            dialog.dismiss()
        }

        //disable seq/rand question buttons if there are no questions in set
        if (pitanja_array.size - 1 <= 0){
            buttonSequential.isVisible = false
            buttonRandom.isVisible = false
            questionsCount.isVisible = false
            emptyNotificationText.isVisible = true

        } else {
            buttonSequential.isVisible = true
            buttonRandom.isVisible = true
            questionsCount.isVisible = true
            emptyNotificationText.isVisible = false
        }

        adapter.notifyItemChanged(position)
    }

    private fun showDefaultDialog(position: Int) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setIcon(R.drawable.ic_baseline_delete_24)
            setTitle("Delete question set")
            setMessage("Are you sure you want to delete this question set file?")
            setPositiveButton("Yes") { _, _ ->
                val file3 = File(currentPath)
                file3.delete()
                adapter.notifyItemRemoved(position);
            }
            setNegativeButton("No") { _, _ ->
            }
        }.create().show()
    }

        fun openAbout(item: android.view.MenuItem) {
        val intent = Intent(this, AboutScreen::class.java)
        startActivity(intent)
    }

    fun openHelp(item: android.view.MenuItem) {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    fun createNewQuestionSet(){
        //create a new file
        /* val file = File("/storage/emulated/0/cf.hbs18.studybuddy/new.txt")
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } */

        //write to file
        //app crashes with empty file - expects a title but crashes when there is none
        /* val stream = FileOutputStream(file)
        try {
            stream.write("New question set\nThis question set is empty".toByteArray())
        } finally {
            stream.close()
        } */

        //open edit question set activity
        val toast_text="nothing"
        val intent = Intent(this, EditQuestionSet::class.java).apply {
            putExtra(EXTRA_MESSAGE3, toast_text)
        }
        startActivity(intent)
    }

    /* fun randomQuestions(view: View){
        val f = File("/storage/emulated/0/cf.hbs18.studybuddy")
        val files: Array<File> = f.listFiles()
        val length=(files.size-1)
        val list = ArrayList<MenuItem>()
        val toast_text = currentPath
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
        val toast_text = currentPath
        val intent = Intent(this, SequentialInterrogation::class.java).apply {
            putExtra(EXTRA_MESSAGE2, toast_text)
        }
        startActivity(intent)
        return
    }

    fun editQuestionSet(view: View){
        val intent = Intent(this, EditQuestionSet::class.java).apply {
            putExtra(EXTRA_MESSAGE3, currentPath)
        }
        startActivity(intent)
        return
    } */
}