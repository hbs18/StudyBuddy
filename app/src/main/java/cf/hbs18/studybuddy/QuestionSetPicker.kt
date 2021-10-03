package cf.hbs18.studybuddy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val EXTRA_MESSAGE = "cf.hbs18.studybuddy.MESSAGE"
const val EXTRA_MESSAGE3 = "cf.hbs18.studybuddy.MESSAGE3"

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
        val f = File("/storage/emulated/0/cf.hbs18.studybuddy")
        val files: Array<File> = f.listFiles()
        val length=(files.size-1)
        val list = ArrayList<MenuItem>()
        val toast_text="/storage/emulated/0/cf.hbs18.studybuddy/"+files[position].name
        val intent = Intent(this, QuestionSetDetails::class.java).apply {
            putExtra(EXTRA_MESSAGE, toast_text)
        }
        startActivity(intent)
        adapter.notifyItemChanged(position)
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
}