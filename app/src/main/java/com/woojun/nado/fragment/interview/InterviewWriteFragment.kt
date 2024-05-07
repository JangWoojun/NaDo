package com.woojun.nado.fragment.interview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.woojun.nado.R
import com.woojun.nado.data.Interview
import com.woojun.nado.database.AppDatabase
import com.woojun.nado.databinding.FragmentInterviewWriteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InterviewWriteFragment : Fragment() {

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}