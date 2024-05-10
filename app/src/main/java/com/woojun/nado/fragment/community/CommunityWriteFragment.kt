package com.woojun.nado.fragment.community

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.woojun.nado.R
import com.woojun.nado.data.BoardList
import com.woojun.nado.data.Post
import com.woojun.nado.databinding.FragmentCommunityListBinding
import com.woojun.nado.databinding.FragmentCommunityWriteBinding
import com.woojun.nado.network.RetrofitAPI
import com.woojun.nado.network.RetrofitClient
import com.woojun.nado.util.ToolTip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class CommunityWriteFragment : Fragment() {
    private var _binding: FragmentCommunityWriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    private var isStart = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryList = listOf("취업/진로", "요리", "건강", "문화 생활", "운동", "자유/기타")
        val category = arguments?.getInt("category")!!

        binding.categoryText.text = categoryList[category]

        binding.constraintLayout.setOnClickListener {
            val balloon = ToolTip.createBalloon(
                requireContext(),
                view,
                viewLifecycleOwner,
                "글 작성을 원하시면 글 작성 버튼을 누르세요.\n게시판 주제와 관련된 글을 작성해주세요."
            )
            balloon.showAlignBottom(it)
        }

        binding.writeButton.setOnClickListener {
            if (binding.titleInput.text.isNotEmpty() && binding.contentInput.text.isNotEmpty() && binding.passwordInput.text.isNotEmpty()) {
                writePost(
                    Post(binding.titleInput.text.toString(), binding.contentInput.text.toString(), category, binding.passwordInput.text.toString())
                ) {
                    findNavController().apply {
                        popBackStack(R.id.communityListFragment, true)
                        navigate(
                            R.id.communityListFragment,
                            Bundle().apply {
                                putInt("category", category)
                            }
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), "모든 칸을 채워주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizer.setRecognitionListener(recognitionListener)

        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "작성하실 단어를 말하세요.")


        binding.micButton.setOnClickListener {
            requestPermission()
            if (isStart) {
                speechRecognizer.stopListening()
                speechRecognizer.destroy()
                Toast.makeText(requireContext(), "중지하셨습니다.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "작성하실 단어를 말하세요.", Toast.LENGTH_LONG).show()
                speechRecognizer.startListening(recognizerIntent)
            }
            isStart = !isStart
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }
    }

    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}

        override fun onBeginningOfSpeech() {}

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            Toast.makeText(requireContext(), "듣기 종료", Toast.LENGTH_SHORT).show()
        }

        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
            Toast.makeText(requireContext(), "에러 발생: $message", Toast.LENGTH_SHORT).show()
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                val result = matches[0]
                binding.contentInput.setText("${binding.contentInput.text} $result")
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {}

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        speechRecognizer.destroy()
    }

    private fun writePost(post: Post, callback: () -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<Boolean> = retrofitAPI.createPost(post)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() == true) {
                    callback()
                } else {
                    Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_LONG).show()
            }
        })
    }
}