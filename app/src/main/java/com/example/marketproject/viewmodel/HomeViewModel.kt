package com.example.marketproject.viewmodel
import androidx.lifecycle.ViewModel
import com.example.marketproject.data.HomeData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class HomeViewModel : ViewModel() {

    private var homeData: MutableList<HomeData> = mutableListOf()

    fun getData(): MutableList<HomeData> = homeData

    fun updateData(data: HomeData) {
        homeData.add(HomeData(data.id, data.title, data.price, data.description, data.timeStamp))
    }


}