package com.woojun.nado.fragment.resume

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.woojun.nado.R
import com.woojun.nado.data.Resume
import com.woojun.nado.data.Spelling
import com.woojun.nado.database.AppDatabase
import com.woojun.nado.database.Preferences.loadUserName
import com.woojun.nado.databinding.FragmentSpellingBinding
import com.woojun.nado.network.RetrofitAPI
import com.woojun.nado.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class SpellingFragment : Fragment() {
    private var _binding: FragmentSpellingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpellingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content = arguments?.getString("content") ?: ""
        checkSpelling(content)

        binding.tooltipButton.setOnClickListener {
            val customDialog = Dialog(requireContext())
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

            customDialog.setContentView(R.layout.tooltip_layout)

            customDialog.findViewById<TextView>(R.id.text_view).text = """
                맞춤법이 교정된 부분은 붉은색으로 표시됩니다.

                저장: 교정된 부분으로 저장하고 싶다면, 저장 버튼을 눌러주세요.

                이전: 뒤로 돌아가고 싶다면, 이전 버튼을 눌러주세요.
            """.trimIndent()

            customDialog.show()
        }

        binding.button1.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.button2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val resumeDao = AppDatabase.getDatabase(requireContext())?.resumeDao()
                resumeDao?.insertResume(Resume(name = loadUserName(requireContext()) ?: "", content = binding.contentInput.text.toString()))
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkSpelling(content: String) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<String> = retrofitAPI.checkSpelling(Spelling(content))

        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    binding.contentInput.text = decodeUrl(response.body().toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun decodeUrl(encodedUrl: String): String {
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
    }

}