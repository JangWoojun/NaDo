package com.woojun.nado

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.woojun.nado.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainText.apply {
            val spannableString = SpannableString(this.text)
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF5656")),
                1,
                3,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.text = spannableString
        }

        binding.subText.apply {
            val spannableString = SpannableString(this.text)
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF5656")),
                0,
                1,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.text = spannableString
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF5656")),
                3,
                4,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.text = spannableString
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finishAffinity()
        }

    }
}