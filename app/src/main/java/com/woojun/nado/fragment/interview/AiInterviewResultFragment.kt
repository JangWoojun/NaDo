package com.woojun.nado.fragment.interview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.woojun.nado.R
import com.woojun.nado.data.AiInterview
import com.woojun.nado.data.Interview
import com.woojun.nado.database.AppDatabase
import com.woojun.nado.databinding.FragmentAiInterviewBinding
import com.woojun.nado.databinding.FragmentAiInterviewResultBinding
import com.woojun.nado.databinding.FragmentInterviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AiInterviewResultFragment : Fragment() {

    private var _binding: FragmentAiInterviewResultBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiInterviewResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content = arguments?.getString("content").toString()

        binding.contentInput.setText(content)
        binding.button1.setOnClickListener {
            findNavController().navigate(R.id.aiInterviewReadyFragment)
        }
        binding.button2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val aiInterviewDao = AppDatabase.getDatabase(requireContext())?.aiInterviewDao()
                aiInterviewDao?.insertAiInterview(AiInterview(content = content, date = getDate()))

                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.myInterviewFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getDate(): String {
        val now = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return now.format(formatter)
    }

}