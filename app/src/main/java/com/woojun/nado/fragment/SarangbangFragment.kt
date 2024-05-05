package com.woojun.nado.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.woojun.nado.Lecture
import com.woojun.nado.R
import com.woojun.nado.ToolTip
import com.woojun.nado.Utils.dpToPx
import com.woojun.nado.ViewPagerAdapter
import com.woojun.nado.databinding.FragmentSarangbangBinding
import kotlin.math.ceil


class SarangbangFragment : Fragment() {
    private var _binding: FragmentSarangbangBinding? = null
    private val binding get() = _binding!!

    private val viewPagerHandler = Handler()
    private var handlerRunnable = Runnable { binding.viewPager.currentItem = binding.viewPager.currentItem + 1 }
    private val list = mutableListOf(
        Lecture("1", "", "", "https://www.naver.com/"),
        Lecture("2", "", "", "https://www.youtube.com/"),
        Lecture("3", "", "", "https://github.com/"),
        Lecture("4", "", "", "https://www.acmicpc.net/"),
    )

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

        val adapter = ViewPagerAdapter(list)

        binding.viewPager.apply {
            this.adapter = adapter
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