package com.example.marketproject.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketproject.databinding.ItemHomeBinding
import com.example.marketproject.viewholder.HomeViewHolder
import com.example.marketproject.data.MessageData
import com.example.marketproject.databinding.ItemMessageBinding
import com.example.marketproject.viewholder.MessageViewHolder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

open class MessageAdapter(private val messageDataList: List<MessageData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemCount(): Int {
        return messageDataList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MessageViewHolder(
        ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MessageViewHolder).binding
        binding.tvMessage.text = messageDataList[position].message
        binding.tvTime.text = convertTimestampToHHmm(messageDataList[position].timestamp)

        if (!messageDataList[position].read) {
            binding.tvRead.text = "1"
        } else {
            binding.tvRead.text = " "
        }


    }

    private fun convertTimestampToHHmm(timestamp: Long): String {
        val sdf = SimpleDateFormat("a h:mm", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

}