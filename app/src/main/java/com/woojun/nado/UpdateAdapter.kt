package com.woojun.nado

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.woojun.nado.databinding.UpdateItemBinding

class UpdateAdapter(private val lectureList: MutableList<Lecture>): RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val binding = UpdateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpdateViewHolder(binding).also { handler ->
            binding.box.setOnClickListener {
                parent.context.startActivity(
                    Intent(parent.context, WebViewActivity::class.java)
                        .putExtra("url", lectureList[handler.position].url)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return lectureList.size
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        holder.bind(lectureList[position])
    }

    class UpdateViewHolder(private val binding: UpdateItemBinding):
        ViewHolder(binding.root) {
        fun bind(lecture: Lecture) {
            if (binding.root.context != null) {
                Glide.with(binding.root.context)
                    .load(lecture.image)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageView)
            }
            binding.titleText.text = lecture.titleText
            binding.subText.text = lecture.subText
        }
    }

}