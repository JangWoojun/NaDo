package com.woojun.nado.fragment.interview

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.woojun.nado.R
import com.woojun.nado.database.AppDatabase
import com.woojun.nado.database.Preferences
import com.woojun.nado.databinding.FragmentInterviewBinding
import com.woojun.nado.util.ToolTip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InterviewFragment : Fragment() {

    private var _binding: FragmentInterviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoButton.setOnClickListener {
            val balloon = ToolTip.createBalloon(
                requireContext(),
                view,
                viewLifecycleOwner,
                "면접 준비를 할 수 있습니다.\n" +
                        "다양한 기능들을 둘러보세요."
            )
            balloon.showAlignBottom(it)
        }

        binding.tooltipButton.setOnClickListener {
            val customDialog = Dialog(requireContext())
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

            customDialog.setContentView(R.layout.tooltip_layout)

            customDialog.findViewById<TextView>(R.id.text_view).text = """
                1. 면접 준비 바로가기
                면접 질문 리스트 제공!
                
                2. AI 면접 바로가기
                모의 가상 면접 실시! 
                
                3. 나의 면접 바로가기
                나의 AI 면접 결과지 확인!
            """.trimIndent()

            customDialog.show()
        }

        binding.interviewReadyButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val interviewDao = AppDatabase.getDatabase(requireContext())?.interviewDao()
                val interviewList = interviewDao?.getInterviewList()

                withContext(Dispatchers.Main) {
                    if (Preferences.loadUserName(requireContext()) != null) {
                        if (interviewList != null && interviewList.size > 0) {
                            findNavController().navigate(R.id.interviewListFragment)
                        } else {
                            findNavController().navigate(R.id.interviewReadyFragment)
                        }
                    } else {
                        findNavController().navigate(
                            R.id.nameFragment,
                            Bundle().apply {
                                this.putInt("nav", R.id.interviewReadyFragment)
                            }
                        )
                    }
                }
            }

        }

        binding.aiInterviewButton.setOnClickListener {
            if (Preferences.loadUserName(requireContext()) != null){
                findNavController().navigate(R.id.aiInterviewReadyFragment)
            } else {
                findNavController().navigate(
                    R.id.nameFragment,
                    Bundle().apply {
                        this.putInt("nav", R.id.aiInterviewReadyFragment)
                    }
                )
            }
        }

        binding.myInterviewButton.setOnClickListener {
            if (Preferences.loadUserName(requireContext()) != null){
                findNavController().navigate(R.id.myInterviewFragment)
            } else {
                findNavController().navigate(
                    R.id.nameFragment,
                    Bundle().apply {
                        this.putInt("nav", R.id.myInterviewFragment)
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}