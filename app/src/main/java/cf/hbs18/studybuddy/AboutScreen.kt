package cf.hbs18.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AboutScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_screen)
        setTitle("About StudyBuddy");
    }
}