package com.example.marketproject.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.data.HomeData
import com.example.marketproject.databinding.FragmentHomeDetailBinding
import com.google.firebase.storage.FirebaseStorage
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

class HomeDetailFragment : Fragment() {

    private lateinit var binding: FragmentHomeDetailBinding
    private lateinit var mainActivity : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeDetailBinding.inflate(layoutInflater)

        init()

        val title = arguments?.getString("title")
        val price = arguments?.getString("price")
        val description = arguments?.getString("description")
        val timeStamp = arguments?.getString("timeStamp")
        val nickName = arguments?.getString("nickName")


        binding.tvTitle.text = title
        binding.tvPrice.text = price?.let { addCommaToPrice(it) } + "원"
        binding.tvDescription.text = description
        binding.tvTime.text = calculationTime(dateTimeToMillSec(timeStamp!!))
        binding.tvNickName.text = nickName

        var storageUrl = "gs://marketproject-29c48.appspot.com"
        val storage: FirebaseStorage = FirebaseStorage.getInstance(storageUrl)
        val storageRef = storage.reference
        val storagePath = storageRef.child("salesPostImage/$timeStamp")

        storagePath.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(binding.root.context)
                .load(uri)
                .into(binding.ivImage)
        }



        return binding.root
    }

    override fun onStop() {
        super.onStop()
        mainActivity.showNaviBarAndFloatingBtn()
    }

    private fun init(){
        mainActivity = activity as MainActivity
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
                value =  TimeUnit.MILLISECONDS.toMinutes(differenceValue).toString() + "분 전"
            }
            differenceValue < 86400000 -> { //23시간 보다 적다면
                value =  TimeUnit.MILLISECONDS.toHours(differenceValue).toString() + "시간 전"
            }
            differenceValue <  604800000 -> { //7일 보다 적다면
                value =  TimeUnit.MILLISECONDS.toDays(differenceValue).toString() + "일 전"
            }
            differenceValue < 2419200000 -> { //4주 보다 적다면
                value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/7).toString() + "주 전"
            }
            differenceValue < 31556952000 -> { //12개월 보다 적다면
                value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/30).toString() + "개월 전"
            }
            else -> { //그 외
                value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/365).toString() + "년 전"
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