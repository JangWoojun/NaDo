package com.woojun.nado.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.woojun.nado.Lecture
import com.woojun.nado.Utils.dpToPx
import com.woojun.nado.WebViewActivity
import com.woojun.nado.databinding.LectureItemBinding

class LectureAdapter(private val lectureList: MutableList<Lecture>): RecyclerView.Adapter<LectureAdapter.LectureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val binding = LectureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LectureViewHolder(binding).also { handler ->
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

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        holder.bind(lectureList[position])
    }

    class LectureViewHolder(private val binding: LectureItemBinding):
        ViewHolder(binding.root) {
        fun bind(lecture: Lecture) {
            if (binding.root.context != null) {
                Glide.with(binding.root.context)
                    .load(lecture.image)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image)
            }
            binding.mainText.text = lecture.titleText
        }
    }

}