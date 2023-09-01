package com.example.marketproject.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketproject.data.HomeData
import com.example.marketproject.databinding.ItemHomeBinding
import com.example.marketproject.viewholder.HomeViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.marketproject.data.ChatMessageData
import com.google.firebase.storage.FirebaseStorage
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit.*

open class ChatAdapter(private val chatDataList: List<ChatMessageData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemCount(): Int {
        return chatDataList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = HomeViewHolder(
        ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


    }

}