package com.woojun.nado.fragment.interview

import android.app.Dialog
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.woojun.nado.databinding.FragmentAiInterviewReadyBinding

class AiInterviewReadyFragment : Fragment() {

    private var _binding: FragmentAiInterviewReadyBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiInterviewReadyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var index = 0
        binding.backButton.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.tooltipButton.setOnClickListener {
            val customDialog = Dialog(requireContext())
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

            customDialog.setContentView(com.woojun.nado.R.layout.tooltip_layout)

            customDialog.findViewById<TextView>(com.woojun.nado.R.id.text_view).text = """
                지금부터 AI 면접관이 가상 면접을 진행하려고 합니다. 

                시작하기: 면접 준비가 되셨다면, 시작하기 버튼을 눌러주세요.

                돌아가기: 이전 페이지로 돌아가기를 원하시나요? 면접 생각이 사라지셨다면, 돌아가기 버튼을 눌러주세요.
            """.trimIndent()

            customDialog.show()
        }

        binding.startButton.setOnClickListener {
            if (index == 0) {
                binding.titleText.text = "면접 응시자님 께서는\n질문에 답해주세요."
                binding.buttonText.text = "네"
                index+=1
            } else {
                it.visibility = View.INVISIBLE
                binding.doText.visibility = View.INVISIBLE
                binding.tooltipButton.visibility = View.INVISIBLE
                binding.tooltipText.visibility = View.INVISIBLE
                binding.backButton.visibility = View.INVISIBLE

                binding.titleText.apply {
                    this.text = "이제,\n면접을 시작합니다."
                    val fadeIn: Animation = AnimationUtils.loadAnimation(requireContext(), com.woojun.nado.R.anim.fade_in)
                    binding.titleText.startAnimation(fadeIn)
                }
                Handler().postDelayed({
                    findNavController().navigate(com.woojun.nado.R.id.aiInterviewFragment)
                }, 1500)
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}