package com.woojun.nado.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.woojun.nado.R
import com.woojun.nado.data.InterviewQuestion
import com.woojun.nado.databinding.InterviewQuestionItemBinding
import com.woojun.nado.databinding.PremiumInterviewQuestionItemBinding

class InterviewQuestionAdapter(private val interviewList: List<InterviewQuestion>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            1 -> {
                val binding = InterviewQuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                InterviewQuestionViewHolder(binding).also { handler ->
                    binding.root.setOnClickListener {
                        binding.root.findNavController().navigate(
                            R.id.interviewWriteFragment,
                            Bundle().apply {
                                this.putString("question", interviewList[handler.adapterPosition].question)
                            }
                        )
                    }
                }
            }
            0 -> {
                val binding = PremiumInterviewQuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PremiumInterviewQuestionViewHolder(binding).also { handler ->
                    binding.root.setOnClickListener {
                        Toast.makeText(binding.root.context, "프리미엄 서비스를 구독해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val interviewQuestion = interviewList[position]
        return if (interviewQuestion.isFree) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is InterviewQuestionViewHolder -> {
                holder.bind(interviewList[position].question)
            }
            is PremiumInterviewQuestionViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return interviewList.size
    }

    class InterviewQuestionViewHolder(private val binding: InterviewQuestionItemBinding):
        ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.titleText.text = title
        }
    }

    class PremiumInterviewQuestionViewHolder(private val binding: PremiumInterviewQuestionItemBinding):
        ViewHolder(binding.root) {
        fun bind() {
        }
    }

}