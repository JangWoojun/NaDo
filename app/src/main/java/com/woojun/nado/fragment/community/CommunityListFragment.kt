package com.woojun.nado.fragment.community

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.nado.R
import com.woojun.nado.adapter.CommunityAdapter
import com.woojun.nado.adapter.LectureAdapter
import com.woojun.nado.data.BoardList
import com.woojun.nado.data.BoardListItem
import com.woojun.nado.data.Lecture
import com.woojun.nado.data.Weather
import com.woojun.nado.databinding.FragmentCommunityListBinding
import com.woojun.nado.network.RetrofitAPI
import com.woojun.nado.network.RetrofitClient
import com.woojun.nado.util.ToolTip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityListFragment : Fragment() {
    private var _binding: FragmentCommunityListBinding? = null
    private val binding get() = _binding!!

    private var pageIndex = 0
    private var pageEndIndex = 0

    private lateinit var boardList: MutableList<List<BoardListItem>>
    private lateinit var originalList: MutableList<BoardListItem>

    private lateinit var adapter: CommunityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryList = listOf("취업/진로", "요리", "건강", "문화 생활", "운동", "자유/기타")
        val category = arguments?.getInt("category")!!

        binding.categoryText.text = categoryList[category]

        binding.constraintLayout.setOnClickListener {
            val balloon = ToolTip.createBalloon(
                requireContext(),
                view,
                viewLifecycleOwner,
                "글 작성을 원하시면 글 작성 버튼을 누르세요.\n게시판 주제와 관련된 글을 작성해주세요."
            )
            balloon.showAlignBottom(it)
        }

        getBoardList(category) { apiData ->
            boardList = mutableListOf()
            originalList = apiData

            if (apiData.size != 0) {
                apiData.chunked(4).forEach {
                    boardList.add(it)
                }
            }

            pageEndIndex = if (boardList.isNotEmpty()){
                boardList.size - 1
            } else {
                0
            }

            pageIndex = 0

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

            if (boardList.size != 0) {
                adapter = CommunityAdapter(boardList[pageIndex].toMutableList())
                binding.communityList.adapter = adapter
                binding.communityList.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        binding.writeButton.setOnClickListener {
            findNavController().navigate(
                R.id.communityWriteFragment,
                Bundle().apply {
                    putInt("category", category)
                }
            )
        }

        binding.leftButton.setOnClickListener {
            if (pageIndex - 1 == -1) {
                Toast.makeText(requireContext(), "첫 페이지입니다", Toast.LENGTH_SHORT).show()
            } else {
                pageIndex -= 1

                adapter = CommunityAdapter(boardList[pageIndex].toMutableList())
                binding.communityList.adapter = adapter

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

                adapter = CommunityAdapter(boardList[pageIndex].toMutableList())
                binding.communityList.adapter = adapter

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

        binding.titleInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterItem(s.toString())
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filterItem(title: String) {
        boardList = mutableListOf()

        originalList.filter { it.title.contains(title, ignoreCase = true) }.chunked(4).forEach {
            boardList.add(it)
        }

        pageIndex = 0
        pageEndIndex = if (boardList.isNotEmpty()){
            boardList.size - 1
        } else {
            0
        }

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

        if (boardList.size != 0) {
            adapter = CommunityAdapter(boardList[pageIndex].toMutableList())
            binding.communityList.adapter = adapter
            binding.communityList.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding.communityList.adapter = CommunityAdapter(mutableListOf())
            binding.communityList.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getBoardList(category: Int, callback: (BoardList) -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<BoardList> = retrofitAPI.getBoardList(category)

        call.enqueue(object : Callback<BoardList> {
            override fun onResponse(call: Call<BoardList>, response: Response<BoardList>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }

            override fun onFailure(call: Call<BoardList>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_LONG).show()
            }
        })
    }

}