package com.woojun.nado.fragment.interview

import android.os.Bundle
import android.text.SpannableString
import android.text.style.TypefaceSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.nado.R
import com.woojun.nado.adapter.InterviewAdapter
import com.woojun.nado.database.AppDatabase
import com.woojun.nado.database.Preferences
import com.woojun.nado.databinding.FragmentInterviewListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InterviewListFragment : Fragment() {

    private var _binding: FragmentInterviewListBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterviewListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val interviewDao = AppDatabase.getDatabase(requireContext())?.interviewDao()
            val adapter = InterviewAdapter(interviewDao!!.getInterviewList())

            withContext(Dispatchers.Main) {

                binding.nameText.apply {
                    val name = Preferences.loadUserName(requireContext())
                    val spannableString = SpannableString("${name}님의\n면접 준비 목록입니다.")
                    spannableString.setSpan(
                        TypefaceSpan(
                            ResourcesCompat.getFont(
                                requireContext(),
                                R.font.spoqahansansneo_bold
                            )!!
                        ),
                        0,
                        name!!.length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    this.text = spannableString
                }

                binding.interviewList.adapter = adapter
                binding.interviewList.layoutManager = LinearLayoutManager(requireContext())

                binding.button1.setOnClickListener {
                    findNavController().popBackStack()
                }
                binding.button2.setOnClickListener {
                    if (adapter.getIndex() != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val interview = interviewDao.getInterviewList()[adapter.getIndex()!!]
                            interviewDao.deleteInterview(interview)

                            withContext(Dispatchers.Main) {
                                adapter.removeItem(adapter.getIndex()!!)
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "자기소개서를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}