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
import com.woojun.nado.adapter.AiInterviewAdapter
import com.woojun.nado.database.AppDatabase
import com.woojun.nado.database.Preferences
import com.woojun.nado.databinding.FragmentMyInterviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyInterviewFragment : Fragment() {

    private var _binding: FragmentMyInterviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyInterviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val aiInterviewDao = AppDatabase.getDatabase(requireContext())?.aiInterviewDao()
            val adapter = AiInterviewAdapter(aiInterviewDao!!.getAiInterviewList())

            withContext(Dispatchers.Main) {

                binding.aiInterviewList.adapter = adapter
                binding.aiInterviewList.layoutManager = LinearLayoutManager(requireContext())

                binding.button1.setOnClickListener {
                    findNavController().popBackStack()
                }
                binding.button2.setOnClickListener {
                    if (adapter.getIndex() != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val aiInterview = aiInterviewDao!!.getAiInterviewList()[adapter.getIndex()!!]
                            aiInterviewDao.deleteAiInterview(aiInterview)

                            withContext(Dispatchers.Main) {
                                adapter.removeItem(adapter.getIndex()!!)
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "인터뷰를 선택해주세요.", Toast.LENGTH_SHORT).show()
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