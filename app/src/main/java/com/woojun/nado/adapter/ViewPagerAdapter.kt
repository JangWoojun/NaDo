package com.woojun.nado.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.woojun.nado.WebViewActivity
import com.woojun.nado.util.Utils.dpToPx
import com.woojun.nado.data.Lecture
import com.woojun.nado.databinding.ViewpagerItemBinding


class ViewPagerAdapter(private val lectureList: MutableList<Lecture>) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val binding = ViewpagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerHolder(binding).also { handler ->
            binding.box.setOnClickListener {
                parent.context.startActivity(
                    Intent(parent.context, WebViewActivity::class.java)
                        .putExtra("url", lectureList[handler.position % lectureList.size].url)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.bind(lectureList[position % lectureList.size])
    }


    override fun getItemCount(): Int = Int.MAX_VALUE


    class ViewPagerHolder(private val binding: ViewpagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lecture: Lecture) {
            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = binding.root.context.dpToPx(40f).toInt()
                marginEnd = binding.root.context.dpToPx(40f).toInt()
            }

            if (binding.root.context != null) {
                Glide.with(binding.root.context)
                    .load(lecture.image)
                    .centerCrop()
                    .into(binding.image)
            }

            binding.mainText.text = lecture.titleText
        }
    }

}
