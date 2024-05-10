package com.woojun.nado.fragment.interview

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
import com.woojun.nado.data.Interview
import com.woojun.nado.database.AppDatabase
import com.woojun.nado.databinding.FragmentInterviewWriteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class InterviewWriteFragment : Fragment() {


    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    private var isStart = false

    private var _binding: FragmentInterviewWriteBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterviewWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val question = arguments?.getString("question")

        binding.titleText.text = "Q. $question"
        binding.button1.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.button2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val interviewDao = AppDatabase.getDatabase(requireContext())?.interviewDao()
                interviewDao?.insertInterview(Interview(question = question ?: "", content = binding.contentInput.text.toString()))

                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.interviewListFragment)
                }
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

}