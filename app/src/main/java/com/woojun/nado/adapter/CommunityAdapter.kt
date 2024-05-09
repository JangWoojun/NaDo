package com.woojun.nado.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woojun.nado.data.AiInterview
import com.woojun.nado.data.BoardListItem
import com.woojun.nado.databinding.CommunityItemBinding
import com.woojun.nado.databinding.SupportItemBinding

class CommunityAdapter(private val boardList: MutableList<BoardListItem>): RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val binding = CommunityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommunityViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.bind(boardList[position])
    }

    inner class CommunityViewHolder(private val binding: CommunityItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(board: BoardListItem) {
            binding.titleText.text = board.title
            binding.contentText.text = board.content
        }
    }

}