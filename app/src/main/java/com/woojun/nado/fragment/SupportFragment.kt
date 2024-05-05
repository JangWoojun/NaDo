package com.woojun.nado.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.woojun.nado.R
import com.woojun.nado.ToolTip
import com.woojun.nado.databinding.FragmentSupportBinding

class SupportFragment : Fragment() {
    private var _binding: FragmentSupportBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainText.apply {
            val spannableString = SpannableString(this.text)
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF5656")),
                1,
                4,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.text = spannableString
        }

        binding.infoButton.setOnClickListener {
            val balloon = ToolTip.createBalloon(
                requireContext(),
                view,
                viewLifecycleOwner,
                "취업을 위한 도움을 제공하고 있습니다.\n" +
                        "카테고리별 기능을 둘러보세요."
            )
            balloon.showAlignBottom(it)
        }

        binding.tooltipButton.setOnClickListener {
            val customDialog = Dialog(requireContext())
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

            customDialog.setContentView(R.layout.tooltip_layout)

            customDialog.findViewById<TextView>(R.id.text_view).text = """
                1. 자기소개서 바로가기 
                자기소개서 작성 및 검사를 원하면 이력서 바로가기 클릭!
                
                2. 면접 바로가기 
                면접 질문을 받고, AI면접을 원하면 면접 바로가기 클릭! 
                
                3. 교육정보 바로가기 
                서울시 어르신 취업지원센터 교육정보를 활용한 센터의 다양한 교육정보 제공!
            """.trimIndent()

            customDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}