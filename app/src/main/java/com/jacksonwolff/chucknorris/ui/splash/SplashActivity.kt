package com.jacksonwolff.chucknorris.ui.splash

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.jacksonwolff.chucknorris.R
import com.jacksonwolff.chucknorris.ui.main.view.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        makeProgress(findViewById<ProgressBar>(R.id.progressBar));
    }

    private fun makeProgress(progressBar: ProgressBar) {
        val barAnim = ObjectAnimator.ofInt(progressBar, "progress", 100)
                .setDuration(3000);

        barAnim.doOnEnd {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        barAnim.start()

    }
}