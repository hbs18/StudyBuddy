package cf.hbs18.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity(), MenuAdapter.OnItemClickListener {
    private val exampleList = generateFileList()
    //private val adapter = MenuAdapter(exampleList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

    }





    private fun generateFileList(): List<MenuItem> {
        val f = File("/storage/emulated/0/cf.hbs18.studybuddy")
        val files: Array<File> = f.listFiles()
        val length=(files.size-1)
        val list = ArrayList<MenuItem>()
        for (i in 0..length) {
            val item = MenuItem(files[i].name)
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
        val intent = Intent(this, InterrogationView::class.java).apply {
            putExtra(EXTRA_MESSAGE, toast_text)
        }
        //startActivity(intent)
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        dialog.setContentView(view)
        dialog.show()
        //adapter.notifyItemChanged(position)
    }
}