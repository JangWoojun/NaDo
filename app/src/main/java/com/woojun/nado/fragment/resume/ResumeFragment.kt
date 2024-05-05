package com.woojun.nado.fragment.resume

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
import com.woojun.nado.R
import com.woojun.nado.ToolTip
import com.woojun.nado.databinding.FragmentResumeBinding
import com.woojun.nado.databinding.FragmentSettingBinding

class ResumeFragment : Fragment() {
    private var _binding: FragmentResumeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoButton.setOnClickListener {
            val balloon = ToolTip.createBalloon(
                requireContext(),
                view,
                viewLifecycleOwner,
                "자기소개서를 작성할 수 있습니다.\n" +
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
                1. 작성 후 변환 
                이력서 내용을 작성한 뒤, pdf로 변환!            
                
                2. 맞춤법 검사
                내 작성 글을 선택하고 맞춤법검사! 
                
                3. AI 자동 완성
                키워드를 작성하면 AI가 자동으로 글을 완성!
            """.trimIndent()

            customDialog.show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}