package com.woojun.nado.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woojun.nado.data.GetCommentList
import com.woojun.nado.data.GetCommentListItem
import com.woojun.nado.databinding.CommentItemBinding

class CommentAdapter(private val commentList: GetCommentList): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    fun addItem(item: GetCommentListItem) {
        commentList.add(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentList[position])
    }

    inner class CommentViewHolder(private val binding: CommentItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: GetCommentListItem) {
            binding.contentText.text = comment.content
        }
    }

}