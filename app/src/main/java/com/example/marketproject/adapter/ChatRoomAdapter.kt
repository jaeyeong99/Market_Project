package com.example.marketproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketproject.data.ChatRoomData
import com.example.marketproject.databinding.ItemChatRoomBinding
import com.example.marketproject.viewholder.ChatRoomViewHolder
import com.example.marketproject.viewholder.MessageViewHolder

open class ChatRoomAdapter(private val chatRoomDataList: List<ChatRoomData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return chatRoomDataList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ChatRoomViewHolder(
        ItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = (holder as ChatRoomViewHolder).binding


    }

}