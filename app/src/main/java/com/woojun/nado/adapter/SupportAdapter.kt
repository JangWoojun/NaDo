package com.woojun.nado.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.woojun.nado.R
import com.woojun.nado.data.Resume
import com.woojun.nado.databinding.SupportItemBinding

class SupportAdapter(private val supportList: MutableList<Resume>): RecyclerView.Adapter<SupportAdapter.SupportViewHolder>() {
    private var index: Int? = null

    fun getIndex(): Int? {
        return index
    }

    fun removeItem(index: Int) {
        supportList.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportViewHolder {
        val binding = SupportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupportViewHolder(binding).also { handler ->
            binding.root.setOnClickListener {
                binding.root.findNavController().navigate(
                    R.id.adapterInnerFragment,
                    Bundle().apply {
                        this.putString(
                            "title",
                            supportList[handler.adapterPosition].name
                        )
                        this.putString("content", supportList[handler.adapterPosition].content)
                    }
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return supportList.size
    }

    override fun onBindViewHolder(holder: SupportViewHolder, position: Int) {
        holder.bind(supportList[position])
    }

    inner class SupportViewHolder(private val binding: SupportItemBinding):
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