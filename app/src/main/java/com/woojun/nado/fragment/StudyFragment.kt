package com.woojun.nado.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.woojun.nado.data.Lecture
import com.woojun.nado.R
import com.woojun.nado.adapter.LectureAdapter
import com.woojun.nado.util.ToolTip
import com.woojun.nado.util.Utils.dpToPx
import com.woojun.nado.adapter.ViewPagerAdapter
import com.woojun.nado.database.ViewModel
import com.woojun.nado.databinding.FragmentStudyBinding
import kotlin.math.ceil


class StudyFragment : Fragment() {
    private var _binding: FragmentStudyBinding? = null
    private val binding get() = _binding!!

    private val viewPagerHandler = Handler()
    private var handlerRunnable = Runnable { binding.viewPager.currentItem = binding.viewPager.currentItem + 1 }

    private lateinit var viewModel: ViewModel

    private var pageIndex = 0
    private var pageEndIndex = 0

    private var categoryButtonIndex = 0
    private var orderButtonIndex = 0

    private lateinit var lectureList: MutableList<List<Lecture>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryBoxList = listOf(binding.button1, binding.button2, binding.button3, binding.button4, binding.button5, binding.button6, binding.button7, binding.button8)
        val categoryTextList = listOf(binding.button1Text, binding.button2Text, binding.button3Text, binding.button4Text, binding.button5Text, binding.button6Text, binding.button7Text, binding.button8Text)

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

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        viewModel.getRecommendationOnlineCourseList().observe(viewLifecycleOwner) { apiData ->
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
                binding.indicator.createDotPanel(list.size, R.drawable.indicator_dot_off, R.drawable.indicator_dot_on, 0)
            }
        }

        viewModel.getOnlineCourseList().observe(viewLifecycleOwner) { apiData ->
            lectureList = mutableListOf()

            if (apiData.size != 0) {
                apiData.chunked(5).forEach {
                    lectureList.add(it)
                }
            }

            pageEndIndex = if (lectureList.isNotEmpty()){
                lectureList.size - 1
            } else {
                0
            }
            pageIndex = 0

            if (lectureList.size != 0) {
                binding.lectureList.adapter = LectureAdapter(lectureList[pageIndex].toMutableList())

                binding.pageText.text = "${pageIndex + 1}/${pageEndIndex + 1} 페이지"
                binding.pageText.apply {
                    val spannableString = SpannableString(this.text)
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor("#FF5656")),
                        0,
                        1,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    this.text = spannableString
                }
            }

            binding.lectureList.layoutManager = LinearLayoutManager(requireContext())

            binding.orderLatestButton.setOnClickListener {
                lectureList = mutableListOf()

                resetOrderButton()
                it as TextView
                it.setTextColor(Color.parseColor("#FF5656"))

                orderButtonIndex = 0

                selectOrder(orderButtonIndex, selectCategory(categoryButtonIndex, apiData))
                    .chunked(5).forEach {
                    lectureList.add(it)
                }

                pageEndIndex = lectureList.size - 1
                pageIndex = 0

                if (lectureList.size != 0) {
                    binding.pageText.text = "${pageIndex + 1}/${pageEndIndex + 1} 페이지"
                    binding.lectureList.adapter = LectureAdapter(lectureList[pageIndex].toMutableList())

                    binding.pageText.apply {
                        val spannableString = SpannableString(this.text)
                        spannableString.setSpan(
                            ForegroundColorSpan(Color.parseColor("#FF5656")),
                            0,
                            (pageIndex + 1).toString().length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        this.text = spannableString
                    }
                }
            }

            binding.orderPopularityButton.setOnClickListener {
                lectureList = mutableListOf()

                resetOrderButton()
                it as TextView
                it.setTextColor(Color.parseColor("#FF5656"))

                orderButtonIndex = 1

                selectOrder(orderButtonIndex, selectCategory(categoryButtonIndex, apiData))
                    .chunked(5).forEach {
                        lectureList.add(it)
                    }

                pageEndIndex = lectureList.size - 1
                pageIndex = 0

                if (lectureList.size != 0) {
                    binding.pageText.text = "${pageIndex + 1}/${pageEndIndex + 1} 페이지"
                    binding.lectureList.adapter = LectureAdapter(lectureList[pageIndex].toMutableList())

                    binding.pageText.apply {
                        val spannableString = SpannableString(this.text)
                        spannableString.setSpan(
                            ForegroundColorSpan(Color.parseColor("#FF5656")),
                            0,
                            (pageIndex + 1).toString().length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        this.text = spannableString
                    }
                }
            }

            binding.orderNameButton.setOnClickListener {
                lectureList = mutableListOf()

                resetOrderButton()
                it as TextView
                it.setTextColor(Color.parseColor("#FF5656"))

                orderButtonIndex = 2

                selectOrder(orderButtonIndex, selectCategory(categoryButtonIndex, apiData))
                    .chunked(5).forEach {
                        lectureList.add(it)
                    }

                pageEndIndex = lectureList.size - 1
                pageIndex = 0

                if (lectureList.size != 0) {
                    binding.pageText.text = "${pageIndex + 1}/${pageEndIndex + 1} 페이지"
                    binding.lectureList.adapter = LectureAdapter(lectureList[pageIndex].toMutableList())

                    binding.pageText.apply {
                        val spannableString = SpannableString(this.text)
                        spannableString.setSpan(
                            ForegroundColorSpan(Color.parseColor("#FF5656")),
                            0,
                            (pageIndex + 1).toString().length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        this.text = spannableString
                    }
                }
            }

            categoryBoxList.forEachIndexed { index, card ->
                card.setOnClickListener {
                    lectureList = mutableListOf()
                    resetCategoryButton()

                    categoryButtonIndex = index

                    selectCategory(categoryButtonIndex, selectOrder(orderButtonIndex, apiData))
                        .chunked(5).forEach {
                            lectureList.add(it)
                        }

                    pageEndIndex = lectureList.size - 1
                    pageIndex = 0

                    if (lectureList.size != 0) {
                        binding.pageText.text = "${pageIndex + 1}/${pageEndIndex + 1} 페이지"
                        binding.lectureList.adapter = LectureAdapter(lectureList[pageIndex].toMutableList())

                        binding.pageText.apply {
                            val spannableString = SpannableString(this.text)
                            spannableString.setSpan(
                                ForegroundColorSpan(Color.parseColor("#FF5656")),
                                0,
                                (pageIndex + 1).toString().length,
                                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            this.text = spannableString
                        }
                    }

                    card.setCardBackgroundColor(Color.parseColor("#FF5656"))
                    categoryTextList[index].setTextColor(Color.parseColor("#FFFFFF"))
                }
            }

        }

        binding.leftButton.setOnClickListener {
            if (pageIndex - 1 == -1) {
                Toast.makeText(requireContext(), "첫 페이지입니다", Toast.LENGTH_SHORT).show()
            } else {
                pageIndex -= 1
                binding.lectureList.adapter = LectureAdapter(lectureList[pageIndex].toMutableList())
                binding.pageText.text = "${pageIndex + 1}/${pageEndIndex + 1} 페이지"

                binding.pageText.apply {
                    val spannableString = SpannableString(this.text)
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor("#FF5656")),
                        0,
                        (pageIndex + 1).toString().length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    this.text = spannableString
                }
            }
        }

        binding.rightButton.setOnClickListener {
            if (pageIndex + 1 > pageEndIndex) {
                Toast.makeText(requireContext(), "마지막 페이지입니다", Toast.LENGTH_SHORT).show()
            } else {
                pageIndex += 1
                binding.lectureList.adapter =
                    LectureAdapter(lectureList[pageIndex].toMutableList())
                binding.pageText.text = "${pageIndex + 1}/${pageEndIndex + 1} 페이지"

                binding.pageText.apply {
                    val spannableString = SpannableString(this.text)
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor("#FF5656")),
                        0,
                        (pageIndex + 1).toString().length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    this.text = spannableString
                }
            }
        }

    }

    private fun resetOrderButton() {
        binding.orderLatestButton.setTextColor(Color.parseColor("#000000"))
        binding.orderPopularityButton.setTextColor(Color.parseColor("#000000"))
        binding.orderNameButton.setTextColor(Color.parseColor("#000000"))
    }

    private fun resetCategoryButton() {
        val categoryBoxList = listOf(binding.button1, binding.button2, binding.button3, binding.button4, binding.button5, binding.button6, binding.button7, binding.button8)
        val categoryTextList = listOf(binding.button1Text, binding.button2Text, binding.button3Text, binding.button4Text, binding.button5Text, binding.button6Text, binding.button7Text, binding.button8Text)

        categoryBoxList.forEach {
            it.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        categoryTextList.forEach {
            it.setTextColor(Color.parseColor("#232323"))
        }
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

    private fun selectCategory(buttonIndex: Int, apiData: MutableList<Lecture>): MutableList<Lecture> {
        val list = when (buttonIndex) {
            0 -> {
                apiData
            }
            1 -> {
                apiData.filter { it.category == "법정의무" }
            }
            2 -> {
                apiData.filter { it.category == "인문/교양" }
            }
            3 -> {
                apiData.filter { it.category == "외국어" }
            }
            4 -> {
                apiData.filter { it.category == "가족/건강" }
            }
            5 -> {
                apiData.filter { it.category == "정보/컴퓨터" }
            }
            6 -> {
                apiData.filter { it.category == "자격증" }
            }
            7 -> {
                apiData.filter { it.category == "취/창업" }
            }
            else -> {
                apiData
            }
        }.toMutableList()

        return list
    }

    private fun selectOrder(buttonIndex: Int, apiData: MutableList<Lecture>): MutableList<Lecture> {
        val list = when (buttonIndex) {
            0 -> {
                apiData
            }
            1 -> {
                apiData.sortedBy { it.isNotPopularity }.toMutableList()
            }
            2 -> {
                apiData.sortedBy { it.titleText }.toMutableList()
            }
            else -> {
                apiData
            }
        }

        return list
    }

}