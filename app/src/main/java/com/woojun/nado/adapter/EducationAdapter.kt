package com.woojun.nado.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.woojun.nado.WebViewActivity
import com.woojun.nado.data.Education
import com.woojun.nado.data.Lecture
import com.woojun.nado.databinding.EducationItemBinding
import com.woojun.nado.databinding.UpdateItemBinding

class EducationAdapter(private val educationList: List<Education>): RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val binding = EducationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EducationViewHolder(binding).also {handler ->
            binding.box.setOnClickListener {
                parent.context.startActivity(
                    Intent(parent.context, WebViewActivity::class.java)
                        .putExtra("url", educationList[handler.position].url)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return educationList.size
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        holder.bind(educationList[position])
    }

    class EducationViewHolder(private val binding: EducationItemBinding):
        ViewHolder(binding.root) {
        fun bind(education: Education) {
            binding.dateText.text = education.date
            binding.titleText.text = education.title
            binding.isEndText.text = education.isEnd
        }
    }

}