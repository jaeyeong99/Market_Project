package com.example.marketproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketproject.data.ChatRoomData
import com.example.marketproject.databinding.ItemHomeBinding
import com.example.marketproject.viewholder.HomeViewHolder
import com.example.marketproject.data.MessageData

open class ChatAdapter(private val chatDataList: List<ChatRoomData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemCount(): Int {
        return chatDataList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = HomeViewHolder(
        ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


    }

}