package com.woojun.nado.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.woojun.nado.util.ToolTip
import com.woojun.nado.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoButton.setOnClickListener {
            val balloon = ToolTip.createBalloon(
                requireContext(),
                view,
                viewLifecycleOwner,
                "소통하는 커뮤니티입니다.\n" +
                        "주제별 게시판을 둘러보세요."
            )
            balloon.showAlignBottom(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}