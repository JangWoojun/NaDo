package com.woojun.nado.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.woojun.nado.R
import com.woojun.nado.ToolTip
import com.woojun.nado.UpdateAdapter
import com.woojun.nado.Utils.dpToPx
import com.woojun.nado.ViewPagerAdapter
import com.woojun.nado.database.ViewModel
import com.woojun.nado.databinding.FragmentSarangbangBinding
import kotlin.math.ceil


class SarangbangFragment : Fragment() {
    private var _binding: FragmentSarangbangBinding? = null
    private val binding get() = _binding!!

    private val viewPagerHandler = Handler()
    private var handlerRunnable = Runnable { binding.viewPager.currentItem = binding.viewPager.currentItem + 1 }

    private lateinit var viewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSarangbangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        viewModel.getOnlineCourseList().observe(viewLifecycleOwner) { apiData ->
            binding.lectureList.adapter = UpdateAdapter(apiData)
            binding.lectureList.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.infoButton.setOnClickListener {
            val balloon = ToolTip.createBalloon(
                requireContext(),
                view,
                viewLifecycleOwner,
                "평생교육강의를 제공하고 있습니다.\n" +
                        "카테고리별 강의를 둘러보세요."
            )
            balloon.showAlignBottom(it)
        }
        viewModel.getOnlineCourseList().observe(viewLifecycleOwner) { apiData ->
            val list = apiData.subList(0, 4)
            binding.viewPager.apply {
                this.adapter = ViewPagerAdapter(list)
                this.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                offscreenPageLimit = 3

                val pageMargin = requireContext().dpToPx(20f)
                val offset = requireContext().dpToPx(20f)

                setPageTransformer { page, position ->
                    val pageOffset = position * -(2 * offset + pageMargin)
                    if (ViewCompat.getLayoutDirection(binding.viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -pageOffset
                    } else {
                        page.translationX = pageOffset
                    }
                }

                registerOnPageChangeCallback(object : OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        viewPagerHandler.removeCallbacks(handlerRunnable)
                        viewPagerHandler.postDelayed(handlerRunnable, 2000)

                        binding.indicator.selectDot(position % list.size)
                    }
                })

                setCurrentItem(Int.MAX_VALUE / 2 - ceil(list.size.toDouble() / 2).toInt() - 1, false)

            }
            binding.indicator.createDotPanel(list.size, R.drawable.indicator_dot_off, R.drawable.indicator_dot_on, 0)

            binding.lectureList.adapter = LectureAdapter(list)
            binding.lectureList.layoutManager = LinearLayoutManager(requireContext())
        }


        binding.orderLatestButton.setOnClickListener {
            resetText()
            it as TextView
            it.setTextColor(Color.parseColor("#FF5656"))
        }
        binding.orderPopularityButton.setOnClickListener {
            resetText()
            it as TextView
            it.setTextColor(Color.parseColor("#FF5656"))
        }
        binding.orderNameButton.setOnClickListener {
            resetText()
            it as TextView
            it.setTextColor(Color.parseColor("#FF5656"))
        }
    }

    private fun resetText() {
        binding.orderLatestButton.setTextColor(Color.parseColor("#000000"))
        binding.orderPopularityButton.setTextColor(Color.parseColor("#000000"))
        binding.orderNameButton.setTextColor(Color.parseColor("#000000"))
    }

    override fun onPause() {
        super.onPause()
        viewPagerHandler.removeCallbacks(handlerRunnable)
    }

    override fun onResume() {
        super.onResume()
        viewPagerHandler.postDelayed(handlerRunnable, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}