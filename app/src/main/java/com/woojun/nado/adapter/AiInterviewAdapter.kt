package com.woojun.nado.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.woojun.nado.R
import com.woojun.nado.data.AiInterview
import com.woojun.nado.databinding.SupportItemBinding

class AiInterviewAdapter(private val aiInterviewList: MutableList<AiInterview>): RecyclerView.Adapter<AiInterviewAdapter.AiInterviewViewHolder>() {
    private var index: Int? = null

    fun getIndex(): Int? {
        return index
    }

    fun removeItem(index: Int) {
        aiInterviewList.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiInterviewViewHolder {
        val binding = SupportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AiInterviewViewHolder(binding).also { handler ->
            binding.root.setOnClickListener {
                binding.root.findNavController().navigate(
                    R.id.adapterInnerFragment,
                    Bundle().apply {
                        this.putString("title", "AI 면접 결과")
                        this.putString("content", aiInterviewList[handler.adapterPosition].content)
                    }
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return aiInterviewList.size
    }

    override fun onBindViewHolder(holder: AiInterviewViewHolder, position: Int) {
        holder.bind(aiInterviewList[position])
    }

    inner class AiInterviewViewHolder(private val binding: SupportItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(aiInterview: AiInterview) {
            binding.textView.text = aiInterview.content
            binding.dateText.text = aiInterview.date
            binding.checkbox.isChecked = adapterPosition == index

            binding.checkbox.setOnClickListener {
                val previousIndex = index
                index = if (index == adapterPosition) {
                    null
                } else {
                    adapterPosition
                }

                previousIndex?.let { notifyItemChanged(it) }
                notifyItemChanged(adapterPosition)
            }
        }
    }



}