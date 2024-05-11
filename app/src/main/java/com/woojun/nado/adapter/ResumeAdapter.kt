package com.woojun.nado.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.woojun.nado.R
import com.woojun.nado.data.Resume
import com.woojun.nado.databinding.ResumeItemBinding

class ResumeAdapter(private val resumeList: MutableList<Resume>): RecyclerView.Adapter<ResumeAdapter.SupportViewHolder>() {
    private var index: Int? = null

    fun getIndex(): Int? {
        return index
    }

    fun removeItem(index: Int) {
        resumeList.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportViewHolder {
        val binding = ResumeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupportViewHolder(binding).also { handler ->
            binding.root.setOnClickListener {
                binding.root.findNavController().navigate(
                    R.id.adapterInnerFragment,
                    Bundle().apply {
                        this.putString(
                            "title",
                            resumeList[handler.adapterPosition].name
                        )
                        this.putString("content", resumeList[handler.adapterPosition].content)
                    }
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return resumeList.size
    }

    override fun onBindViewHolder(holder: SupportViewHolder, position: Int) {
        holder.bind(resumeList[position])
    }

    inner class SupportViewHolder(private val binding: ResumeItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(support: Resume) {
            binding.textView.text = support.content
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