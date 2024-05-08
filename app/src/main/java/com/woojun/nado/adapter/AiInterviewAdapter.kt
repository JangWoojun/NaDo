package com.woojun.nado.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woojun.nado.data.AiInterview
import com.woojun.nado.databinding.SupportItemBinding

class AiInterviewAdapter(private val aiInterviewList: MutableList<AiInterview>): RecyclerView.Adapter<AiInterviewAdapter.AiInterviewViewHolder>() {
    private var index: Int? = null

    fun getIndex(): Int? {
        return index
    }

    fun removeItem(index: Int) {
        aiInterviewList.removeAt(index)
        notifyItemChanged(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiInterviewViewHolder {
        val binding = SupportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AiInterviewViewHolder(binding)
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
            binding.checkbox.isChecked = aiInterview.isSelected
            binding.dateText.text = aiInterview.date

            binding.checkbox.setOnClickListener {
                aiInterview.isSelected = !aiInterview.isSelected
                notifyItemChanged(adapterPosition)
                updateCheckedItems(adapterPosition)
            }
        }
    }

    private fun updateCheckedItems(adapterPosition: Int) {
        val checkedCount = aiInterviewList.count { it.isSelected }
        index = if (checkedCount == 0) {
            null
        } else {
            adapterPosition
        }
    }

}