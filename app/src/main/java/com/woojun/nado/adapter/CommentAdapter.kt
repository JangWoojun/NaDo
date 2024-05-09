package com.woojun.nado.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.woojun.nado.R
import com.woojun.nado.data.AiInterview
import com.woojun.nado.data.BoardListItem
import com.woojun.nado.data.GetCommentList
import com.woojun.nado.data.GetCommentListItem
import com.woojun.nado.data.Post
import com.woojun.nado.databinding.CommentItemBinding
import com.woojun.nado.databinding.CommunityItemBinding
import com.woojun.nado.databinding.SupportItemBinding

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