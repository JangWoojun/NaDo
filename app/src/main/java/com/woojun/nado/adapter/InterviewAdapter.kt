package com.woojun.nado.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woojun.nado.data.Interview
import com.woojun.nado.databinding.SupportItemBinding

class InterviewAdapter(private val interviewList: MutableList<Interview>): RecyclerView.Adapter<InterviewAdapter.InterviewViewHolder>() {
    private var index: Int? = null

    fun getIndex(): Int? {
        return index
    }

    fun removeItem(index: Int) {
        interviewList.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterviewViewHolder {
        val binding = SupportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InterviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return interviewList.size
    }

    override fun onBindViewHolder(holder: InterviewViewHolder, position: Int) {
        holder.bind(interviewList[position])
    }

    inner class InterviewViewHolder(private val binding: SupportItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(interview: Interview) {
            binding.textView.text = "${interview.question} ${interview.content}"
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