package ITI.covid19_tracker

import ITI.covid19_tracker.Tab.ActivityTab
import ITI.covid19_tracker.view.MainActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity()  {

    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        linearId.startAnimation(animation)
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this, ActivityTab::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}