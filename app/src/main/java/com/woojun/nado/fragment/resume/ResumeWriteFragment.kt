package com.woojun.nado.fragment.resume

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.woojun.nado.R
import com.woojun.nado.databinding.FragmentResumeWriteBinding

class ResumeWriteFragment : Fragment() {
    private var _binding: FragmentResumeWriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumeWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name")



        binding.button1.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.resumeListFragment)
        }

        binding.nameText.text = name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}