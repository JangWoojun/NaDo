package com.woojun.nado.fragment.resume

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.woojun.nado.R
import com.woojun.nado.data.Ai
import com.woojun.nado.data.Resume
import com.woojun.nado.data.Spelling
import com.woojun.nado.database.AppDatabase
import com.woojun.nado.database.Preferences
import com.woojun.nado.databinding.FragmentAiWriteBinding
import com.woojun.nado.databinding.FragmentSpellingBinding
import com.woojun.nado.network.RetrofitAPI
import com.woojun.nado.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AiWriteFragment : Fragment() {
    private var _binding: FragmentAiWriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {
            if (binding.contentInput.text.isNotEmpty()) {
                checkSpelling(binding.contentInput.text.toString())
                viewChange(true)
            } else {
                Toast.makeText(requireContext(), "본문을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.button1.setOnClickListener {
            viewChange(false)
        }

        binding.button2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val resumeDao = AppDatabase.getDatabase(requireContext())?.resumeDao()
                resumeDao?.insertResume(Resume(name = Preferences.loadUserName(requireContext()) ?: "", content = binding.contentInput.text.toString()))

                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.resumeListFragment)
                }
            }
        }

        binding.tooltipButton.setOnClickListener {
            val customDialog = Dialog(requireContext())
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

            customDialog.setContentView(R.layout.tooltip_layout)

            customDialog.findViewById<TextView>(R.id.text_view).text = """
                자동 완성된 부분은 붉은색으로 표시됩니다.

                저장: 자동완성된 부분으로 저장하고 싶다면, 저장 버튼을 눌러주세요.

                이전: 뒤로 돌아가고 싶다면, 이전 버튼을 눌러주세요.
            """.trimIndent()

            customDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkSpelling(content: String) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<String> = retrofitAPI.generateResumeGpt(Ai(content))

        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    binding.contentInput.apply {
                        this.setText(response.body().toString())
                        this.text.setSpan(
                            ForegroundColorSpan(
                                Color.parseColor("#FF5656")),
                            0,
                            response.body().toString().length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun viewChange(isFinish: Boolean) {
        if (isFinish) {
            binding.buttonBox.visibility = View.VISIBLE
            binding.tooltipButton.visibility = View.VISIBLE
            binding.tooltipText.visibility = View.VISIBLE

            binding.nextButton.visibility = View.GONE
        } else {
            binding.buttonBox.visibility = View.GONE
            binding.tooltipButton.visibility = View.GONE
            binding.tooltipText.visibility = View.GONE

            binding.nextButton.visibility = View.VISIBLE
        }
    }
}