package com.woojun.nado.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.woojun.nado.R
import com.woojun.nado.ToolTip.createBalloon
import com.woojun.nado.UpdateAdapter
import com.woojun.nado.WebViewActivity
import com.woojun.nado.database.ViewModel
import com.woojun.nado.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ViewModel

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
            findNavController().navigate(R.id.study)
        }

        binding.supportButton.setOnClickListener {
            findNavController().navigate(R.id.support)
        }

        binding.communityButton.setOnClickListener {
            findNavController().navigate(R.id.community)
        }



        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        viewModel.getOnlineCourseList().observe(viewLifecycleOwner) { apiData ->
            binding.updateList.adapter = UpdateAdapter(apiData)
            binding.updateList.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.getRecommendationOnlineCourseList().observe(viewLifecycleOwner) { apiData ->
            Glide.with(binding.root.context)
                .load(apiData[0].image)
                .centerCrop()
                .into(binding.lectureBox1Image)
            binding.lectureBox1MainText.text = apiData[0].titleText
            binding.lectureBox1SubText.text = apiData[0].subText

            Glide.with(binding.root.context)
                .load(apiData[1].image)
                .centerCrop()
                .into(binding.lectureBox2Image)
            binding.lectureBox2MainText.text = apiData[1].titleText
            binding.lectureBox2SubText.text = apiData[1].subText

            binding.lectureBox1.setOnClickListener {
                requireActivity().startActivity(Intent(
                    requireActivity(), WebViewActivity::class.java).apply {
                        this.putExtra("url", apiData[0].url)
                    }
                )
            }

            binding.lectureBox2.setOnClickListener {
                requireActivity().startActivity(Intent(
                    requireActivity(), WebViewActivity::class.java).apply {
                        this.putExtra("url", apiData[1].url)
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