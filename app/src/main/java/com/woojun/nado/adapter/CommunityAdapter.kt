package com.woojun.nado.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.woojun.nado.R
import com.woojun.nado.data.BoardListItem
import com.woojun.nado.databinding.CommunityItemBinding

class CommunityAdapter(private val boardList: MutableList<BoardListItem>): RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val binding = CommunityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommunityViewHolder(binding).also { handler ->
            binding.root.setOnClickListener {
                val item = boardList[handler.adapterPosition]
                binding.root.findNavController().navigate(
                    R.id.communityInnerFragment,
                    Bundle().apply {
                        this.putString("title", item.title)
                        this.putString("content", item.content)
                        this.putInt("board", item.board)
                        this.putInt("id", item.id)
                    }
                )
            }
        }
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