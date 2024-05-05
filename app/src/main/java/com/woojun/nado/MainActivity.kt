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
        binding.study.setOnClickListener {
            navController.navigate(R.id.study)
        }
        binding.support.setOnClickListener {
            navController.navigate(R.id.support)
        }
        binding.community.setOnClickListener {
            navController.navigate(R.id.community)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            resetNavigation()
            when (destination.id) {
                R.id.home -> {
                    binding.homeIcon.setImageResource(R.drawable.select_home_icon)
                }
                R.id.study -> {
                    binding.studyIcon.setImageResource(R.drawable.select_study_icon)
                }
                R.id.support -> {
                    binding.supportIcon.setImageResource(R.drawable.select_support_icon)
                }
                R.id.community -> {
                    binding.communityIcon.setImageResource(R.drawable.select_community_icon)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun resetNavigation() {
        binding.homeIcon.setImageResource(R.drawable.home_icon)
        binding.studyIcon.setImageResource(R.drawable.study_icon)
        binding.supportIcon.setImageResource(R.drawable.support_icon)
        binding.communityIcon.setImageResource(R.drawable.community_icon)
    }

}