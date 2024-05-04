package com.woojun.nado.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.nado.Lecture
import com.woojun.nado.MainActivity
import com.woojun.nado.ToolTip.createBalloon
import com.woojun.nado.UpdateAdapter
import com.woojun.nado.WebViewActivity
import com.woojun.nado.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoButton.setOnClickListener {
            val balloon = createBalloon(
                requireContext(),
                view,
                viewLifecycleOwner,
                "평생교육강의를 제공하고 있습니다.\n" +
                    "카테고리별 강의를 둘러보세요."
            )
            balloon.showAlignBottom(it)
        }

        binding.studyButton.setOnClickListener {
            (requireActivity() as MainActivity).moveNavigation(1)
        }

        binding.supportButton.setOnClickListener {
            (requireActivity() as MainActivity).moveNavigation(2)
        }

        binding.communityButton.setOnClickListener {
            (requireActivity() as MainActivity).moveNavigation(3)
        }

        binding.lectureBox1.setOnClickListener {
            val lecture = Lecture("", "", "", "")
            requireActivity().startActivity(Intent(
                requireActivity(), WebViewActivity::class.java).apply {
                    this.putExtra("url", lecture.url)
                }
            )
        }

        binding.lectureBox2.setOnClickListener {
            val lecture = Lecture("", "", "", "")
            requireActivity().startActivity(Intent(
                requireActivity(), WebViewActivity::class.java).apply {
                this.putExtra("url", lecture.url)
                }
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}