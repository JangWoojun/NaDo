package com.woojun.nado.fragment.community

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.nado.R
import com.woojun.nado.adapter.CommentAdapter
import com.woojun.nado.data.BoardList
import com.woojun.nado.data.BoardListItem
import com.woojun.nado.data.Comment
import com.woojun.nado.data.GetCommentList
import com.woojun.nado.data.GetCommentListItem
import com.woojun.nado.data.Post
import com.woojun.nado.databinding.FragmentCommunityInnerBinding
import com.woojun.nado.databinding.FragmentCommunityListBinding
import com.woojun.nado.network.RetrofitAPI
import com.woojun.nado.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityInnerFragment : Fragment() {
    private var _binding: FragmentCommunityInnerBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityInnerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val board = BoardListItem(
            arguments?.getInt("board")!!,
            arguments?.getString("content")!!,
            arguments?.getInt("id")!!,
            arguments?.getString("title")!!
        )

        binding.titleText.text = board.title
        binding.contentText.text = board.content
        binding.removeButton.apply {
            val spannableString = SpannableString(this.text)
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF5656")),
                0,
                2,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.text = spannableString
        }

        binding.removeButton.setOnClickListener {

        }

        getComment(board.id) { list ->
            val adapter = CommentAdapter(list)
            binding.commentList.adapter = adapter
            binding.commentList.layoutManager = LinearLayoutManager(requireContext())

            binding.sendButton.setOnClickListener {
                val comment = Comment(
                    0,
                    binding.commentInput.text.toString(),
                    board.id
                )
                createComment(comment) {
                    adapter.addItem(GetCommentListItem(0, comment.content, 0 , 0))
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getComment(id: Int, callback: (GetCommentList) -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<GetCommentList> = retrofitAPI.getComment(id)

        call.enqueue(object : Callback<GetCommentList> {
            override fun onResponse(call: Call<GetCommentList>, response: Response<GetCommentList>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                } else {
                    Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<GetCommentList>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createComment(comment: Comment, callback: () -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<Boolean> = retrofitAPI.createComment(comment)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() == true) {
                    callback()
                } else {
                    Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_LONG).show()
            }
        })
    }

}