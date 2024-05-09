package com.woojun.nado.fragment.community

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
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
            val customDialog = Dialog(requireContext())

            customDialog.setContentView(R.layout.password_dialog)

            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            customDialog.findViewById<ConstraintLayout>(R.id.send_button).setOnClickListener {
                if (customDialog.findViewById<EditText>(R.id.password_input).text.isNotEmpty()) {
                    deleteBoard(board.id, customDialog.findViewById<EditText>(R.id.password_input).text.toString()) {
                        if (it) {
                            findNavController().navigate(
                                R.id.communityListFragment,
                                Bundle().apply {
                                    putInt("category", board.board)
                                }
                            )
                            Toast.makeText(requireContext(), "삭제 완료", Toast.LENGTH_SHORT).show()
                            customDialog.dismiss()
                        } else {
                            Toast.makeText(requireContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            customDialog.show()
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

    private fun deleteBoard(id: Int, password: String, callback: (Boolean) -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<Boolean> = retrofitAPI.deleteBoard(id, password)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                } else {
                    Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류, 다시 시도해주세요", Toast.LENGTH_LONG).show()
            }
        })
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