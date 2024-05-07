package com.woojun.nado.fragment.interview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.nado.adapter.InterviewQuestionAdapter
import com.woojun.nado.data.InterviewQuestion
import com.woojun.nado.databinding.FragmentInterviewListBinding

class InterviewListFragment : Fragment() {

    private var _binding: FragmentInterviewListBinding? = null
    private val binding get() = _binding!!

    private val list = listOf(
        InterviewQuestion("자기소개를 해주세요."),
        InterviewQuestion("우리 회사에 지원하게 된 동기는 무엇인가요?"),
        InterviewQuestion("당신의 장, 단점은 무엇인가요?"),
        InterviewQuestion("우리 회사에 어떤 기여를 할 수 있나요?"),
        InterviewQuestion("직무와 연관된 본인의 경험을 이야기 해주세요."),
        InterviewQuestion("현재의 목표는 무엇인가요?"),
        InterviewQuestion("자기소개를 해주세요.", false),
        InterviewQuestion("자기소개를 해주세요.", false),
        InterviewQuestion("자기소개를 해주세요.", false),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterviewListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.interviewList.adapter = InterviewQuestionAdapter(list)
        binding.interviewList.layoutManager = LinearLayoutManager(requireContext())


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}