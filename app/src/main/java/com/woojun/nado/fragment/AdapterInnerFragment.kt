package com.woojun.nado.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.woojun.nado.databinding.FragmentAdapterInnerBinding

class AdapterInnerFragment : Fragment() {
    private var _binding: FragmentAdapterInnerBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdapterInnerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val content = arguments?.getString("content")

        binding.contentText.text = content
        binding.titleText.text = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}