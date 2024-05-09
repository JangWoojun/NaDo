package com.woojun.nado.fragment.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.woojun.nado.R
import com.woojun.nado.data.BoardList
import com.woojun.nado.data.Post
import com.woojun.nado.databinding.FragmentCommunityListBinding
import com.woojun.nado.databinding.FragmentCommunityWriteBinding
import com.woojun.nado.network.RetrofitAPI
import com.woojun.nado.network.RetrofitClient
import com.woojun.nado.util.ToolTip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityWriteFragment : Fragment() {
    private var _binding: FragmentCommunityWriteBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityWriteBinding.inflate(inflater, container, false)
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

        binding.writeButton.setOnClickListener {
            if (binding.titleInput.text.isNotEmpty() && binding.contentInput.text.isNotEmpty() && binding.passwordInput.text.isNotEmpty()) {
                writePost(
                    Post(binding.titleInput.text.toString(), binding.contentInput.text.toString(), category, binding.passwordInput.text.toString())
                ) {
                    findNavController().navigate(
                        R.id.communityListFragment,
                        Bundle().apply {
                            putInt("category", category)
                        }
                    )
                }
            } else {
                Toast.makeText(requireContext(), "모든 칸을 채워주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun writePost(post: Post, callback: () -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<Boolean> = retrofitAPI.createPost(post)

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