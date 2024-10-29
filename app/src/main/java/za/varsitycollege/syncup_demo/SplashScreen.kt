package za.varsitycollege.syncup_demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Delay for 3 seconds before navigating to the LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreen, Login::class.java)
            startActivity(intent)
            finish() // Finish splash activity so it's not returned to
        }, 4000) // 3000 milliseconds = 3 seconds
    }
}
