package com.woojun.nado.fragment.interview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.woojun.nado.R
import com.woojun.nado.databinding.FragmentAiInterviewBinding
import com.woojun.nado.databinding.FragmentAiInterviewResultBinding
import com.woojun.nado.databinding.FragmentInterviewBinding


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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}