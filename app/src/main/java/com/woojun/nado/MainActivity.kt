package com.woojun.nado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.woojun.nado.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.home.setOnClickListener {
            resetNavigation()
            binding.homeIcon.setImageResource(R.drawable.select_home_icon)
            navController.navigate(R.id.home)
        }
        binding.sarangbang.setOnClickListener {
            resetNavigation()
            binding.sarangbangIcon.setImageResource(R.drawable.select_sarangbang_icon)
            navController.navigate(R.id.sarangbang)
        }
        binding.chat.setOnClickListener {
            resetNavigation()
            binding.chatIcon.setImageResource(R.drawable.select_chat_icon)
            navController.navigate(R.id.chat)
        }
        binding.setting.setOnClickListener {
            resetNavigation()
            binding.settingIcon.setImageResource(R.drawable.select_setting_icon)
            navController.navigate(R.id.setting)
        }
    }

    fun moveNavigation(index: Int) {
        val navController = findNavController(R.id.nav_host_fragment)
        when (index) {
            0 -> {
                resetNavigation()
                binding.homeIcon.setImageResource(R.drawable.select_home_icon)
                navController.navigate(R.id.home)
            }
            1 -> {
                resetNavigation()
                binding.sarangbangIcon.setImageResource(R.drawable.select_sarangbang_icon)
                navController.navigate(R.id.sarangbang)
            }
            2 -> {
                resetNavigation()
                binding.chatIcon.setImageResource(R.drawable.select_chat_icon)
                navController.navigate(R.id.chat)
            }
            3 -> {
                resetNavigation()
                binding.settingIcon.setImageResource(R.drawable.select_setting_icon)
                navController.navigate(R.id.setting)
            }
        }
    }

    private fun resetNavigation() {
        binding.homeIcon.setImageResource(R.drawable.home_icon)
        binding.sarangbangIcon.setImageResource(R.drawable.sarangbang_icon)
        binding.chatIcon.setImageResource(R.drawable.chat_icon)
        binding.settingIcon.setImageResource(R.drawable.setting_icon)
    }

}