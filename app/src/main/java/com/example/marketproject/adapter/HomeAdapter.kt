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
import com.google.firebase.storage.FirebaseStorage
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit.*

open class HomeAdapter(private val homeDataList: List<HomeData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener

    }
    override fun getItemCount(): Int {
        return homeDataList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = HomeViewHolder(
        ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //item click listener
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(it, position)
        }


        //뷰에 데이터 출력
        val binding = (holder as HomeViewHolder).binding

        binding.tvTitle.text = homeDataList[position].title
        binding.tvPrice.text = addCommaToPrice(homeDataList[position].price) + "원"

        var storageUrl = "gs://marketproject-29c48.appspot.com"
        val timeStamp = homeDataList[position].timeStamp
        val storage: FirebaseStorage = FirebaseStorage.getInstance(storageUrl)
        val storageRef = storage.reference
        val storagePath = storageRef.child("salesPostImage/$timeStamp")

        storagePath.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.itemView.context)
                .load(uri)
                .into(binding.ivImage)
        }

        binding.tvTime.text = calculationTime(dateTimeToMillSec(timeStamp!!))

    }

    @SuppressLint("SimpleDateFormat")
    private fun dateTimeToMillSec(dateTime: String): Long{
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val mDate = sdf.parse(dateTime)
            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timeInMilliseconds
    }

    private fun calculationTime(createDateTime: Long): String{
        val nowDateTime = Calendar.getInstance().timeInMillis //현재 시간 to millisecond
        var value = ""
        val differenceValue = nowDateTime - createDateTime //현재 시간 - 비교가 될 시간
        when {
            differenceValue < 60000 -> { //59초 보다 적다면
                value = "방금 전"
            }
            differenceValue < 3600000 -> { //59분 보다 적다면
                value =  MILLISECONDS.toMinutes(differenceValue).toString() + "분 전"
            }
            differenceValue < 86400000 -> { //23시간 보다 적다면
                value =  MILLISECONDS.toHours(differenceValue).toString() + "시간 전"
            }
            differenceValue <  604800000 -> { //7일 보다 적다면
                value =  MILLISECONDS.toDays(differenceValue).toString() + "일 전"
            }
            differenceValue < 2419200000 -> { //4주 보다 적다면
                value =  (MILLISECONDS.toDays(differenceValue)/7).toString() + "주 전"
            }
            differenceValue < 31556952000 -> { //12개월 보다 적다면
                value =  (MILLISECONDS.toDays(differenceValue)/30).toString() + "개월 전"
            }
            else -> { //그 외
                value =  (MILLISECONDS.toDays(differenceValue)/365).toString() + "년 전"
            }
        }
        return value
    }

    fun addCommaToPrice(price: String): String {
        val parts = price.split(".")
        val integerPart = parts[0].toIntOrNull() ?: return price  // Return original price if not a valid number

        val formattedIntegerPart = String.format("%,d", integerPart)
        val decimalPart = if (parts.size > 1) ".${parts[1]}" else ""

        return "$formattedIntegerPart$decimalPart"
    }
}