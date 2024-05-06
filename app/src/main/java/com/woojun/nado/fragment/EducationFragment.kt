package com.woojun.nado.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.nado.R
import com.woojun.nado.adapter.EducationAdapter
import com.woojun.nado.database.ViewModel
import com.woojun.nado.databinding.FragmentEducationBinding

class EducationFragment : Fragment() {
    private var _binding: FragmentEducationBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEducationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tooltipButton.setOnClickListener {
            val customDialog = Dialog(requireContext())
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

            customDialog.setContentView(R.layout.tooltip_layout)

            customDialog.findViewById<TextView>(R.id.text_view).text = """
                이 화면은 서울시 어르신 취업지원 센터 교육정보에 대한 안내를 드리는 곳입니다.

                관심있는 교육을 선택하면, 자동으로 <서울시어르신취업센터>의 해당 안내문으로 연결됩니다. 

                이전으로 돌아가기: 다시 <학습하기>의 메인 화면으로 이동합니다.
            """.trimIndent()

            customDialog.show()
        }

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        viewModel.getEducationList().observe(viewLifecycleOwner) { list ->
            Log.d("확인", list.toString())
            binding.educationList.adapter = EducationAdapter(list)
            binding.educationList.layoutManager = LinearLayoutManager(requireContext())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}