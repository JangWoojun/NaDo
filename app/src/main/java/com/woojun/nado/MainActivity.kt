package com.woojun.nado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.woojun.nado.database.ViewModel
import com.woojun.nado.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.loadOnlineCourse {
            Toast.makeText(this@MainActivity, "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
        viewModel.loadRecommendationOnlineCourseList(4) {
            Toast.makeText(this@MainActivity, "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }

        navController = findNavController(R.id.nav_host_fragment)

        binding.home.setOnClickListener {
            navController.navigate(R.id.home)
        }
        binding.sarangbang.setOnClickListener {
            navController.navigate(R.id.sarangbang)
        }
        binding.chat.setOnClickListener {
            navController.navigate(R.id.chat)
        }
        binding.setting.setOnClickListener {
            navController.navigate(R.id.setting)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            resetNavigation()
            when (destination.id) {
                R.id.home -> {
                    binding.homeIcon.setImageResource(R.drawable.select_home_icon)
                }
                R.id.sarangbang -> {
                    binding.sarangbangIcon.setImageResource(R.drawable.select_sarangbang_icon)
                }
                R.id.chat -> {
                    binding.chatIcon.setImageResource(R.drawable.select_chat_icon)
                }
                R.id.setting -> {
                    binding.settingIcon.setImageResource(R.drawable.select_setting_icon)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun resetNavigation() {
        binding.homeIcon.setImageResource(R.drawable.home_icon)
        binding.sarangbangIcon.setImageResource(R.drawable.sarangbang_icon)
        binding.chatIcon.setImageResource(R.drawable.chat_icon)
        binding.settingIcon.setImageResource(R.drawable.setting_icon)
    }

}