package com.woojun.nado.fragment.resume

import android.os.Bundle
import android.os.Environment
import android.text.SpannableString
import android.text.style.TypefaceSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.nado.R
import com.woojun.nado.adapter.SupportAdapter
import com.woojun.nado.data.Pdf
import com.woojun.nado.database.AppDatabase
import com.woojun.nado.database.Preferences.loadUserName
import com.woojun.nado.databinding.FragmentResumeListBinding
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
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class ResumeListFragment : Fragment() {
    private var _binding: FragmentResumeListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isWrite = arguments?.getBoolean("isWrite") ?: true
        CoroutineScope(Dispatchers.IO).launch {
                val resumeDao = AppDatabase.getDatabase(requireContext())?.resumeDao()
                val adapter = SupportAdapter(resumeDao!!.getResumeList())

                withContext(Dispatchers.Main) {

                    binding.nameText.apply {
                        val name = loadUserName(requireContext())
                        val spannableString = SpannableString("${name}님의\n자기소개서 목록입니다.")
                        spannableString.setSpan(
                            TypefaceSpan(
                                ResourcesCompat.getFont(
                                    requireContext(),
                                    R.font.spoqahansansneo_bold
                                )!!
                            ),
                            0,
                            name!!.length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        this.text = spannableString
                    }

                    binding.resumeList.adapter = adapter
                    binding.resumeList.layoutManager = LinearLayoutManager(requireContext())

                    binding.button1.setOnClickListener {
                        if (adapter.getIndex() != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val resume = resumeDao.getResumeList()[adapter.getIndex()!!]
                                resumeDao.deleteResume(resume)

                                withContext(Dispatchers.Main) {
                                    adapter.removeItem(adapter.getIndex()!!)
                                }
                            }
                        } else {
                            Toast.makeText(requireContext(), "자기소개서를 선택해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    if (!isWrite) {
                        binding.titleText.text = "맞춤법 검사"

                        binding.button2Text.text = "검사"
                        binding.button2.setOnClickListener {
                            if (adapter.getIndex() != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val resume = resumeDao.getResumeList()[adapter.getIndex()!!]
                                    withContext(Dispatchers.Main) {
                                        findNavController().navigate(
                                            R.id.spellingFragment, Bundle().apply {
                                                this.putString("content", resume.content)
                                            }
                                        )
                                    }
                                }
                            } else {
                                Toast.makeText(requireContext(), "자기소개서를 선택해주세요.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        binding.pdfButton.visibility = View.GONE
                    } else {
                        binding.titleText.text = "작성 후 변환"

                        binding.button2Text.text = "작성"
                        binding.button2.setOnClickListener {
                            findNavController().navigate(R.id.resumeWriteFragment)
                        }

                        binding.pdfButton.visibility = View.VISIBLE
                    }

                    binding.pdfButton.setOnClickListener {
                        if (adapter.getIndex() != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val resume = resumeDao.getResumeList()[adapter.getIndex()!!]
                                postPdf(Pdf(resume.name, resume.content))
                            }
                        } else {
                            Toast.makeText(requireContext(), "자기소개서를 선택해주세요.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun postPdf(pdf: Pdf) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<ResponseBody> = retrofitAPI.postPdf(pdf)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    savePdfToFile(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun savePdfToFile(responseBody: ResponseBody) {
        try {
            val externalDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val fileName = "${loadUserName(requireContext())} 자기소개서.pdf"
            val file = File(externalDir, fileName)
            val outputStream = FileOutputStream(file)

            val buffer = ByteArray(4096)
            var bytesRead: Int

            val inputStream = responseBody.byteStream()

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            Toast.makeText(requireContext(), "파일이 다운로드 폴더에 저장되었습니다.", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "파일 저장 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

}