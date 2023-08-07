package com.example.marketproject.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.adapter.HomeAdapter
import com.example.marketproject.data.HomeData
import com.example.marketproject.databinding.FragmentHomeBinding
import com.example.marketproject.viewmodel.HomeViewModel
import com.google.firebase.database.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainActivity: MainActivity

    private val homeDataList = mutableListOf<HomeData>()
    private val viewModel: HomeViewModel by activityViewModels()
    private var homeData: MutableList<HomeData> = mutableListOf()

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference

    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        init()
        clickListener()

        homeAdapter = HomeAdapter(homeDataList)
        binding.homeRecyclerView.adapter = homeAdapter
        binding.homeRecyclerView.layoutManager = createLayoutManager()

        mainActivity.setBottomNavigationVisibility(View.VISIBLE)


        addHomeData()


        return binding.root
    }


    private fun init(){
        mainActivity = activity as MainActivity
    }

    private fun clickListener(){


    }

    private fun createLayoutManager(): LinearLayoutManager {
        val manager = LinearLayoutManager(context)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        return manager
    }


    private fun addHomeData() {

//        homeData = viewModel.getData()
//
//        for (data in homeData) {
//            homeDataList.add(HomeData(data.id, data.title, data.price, data.description, data.imageUri))
//        }



        val userId = mainActivity.auth?.currentUser?.uid.orEmpty()


        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("salesPost")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (post in snapshot.children){
                    if (snapshot.exists()) {

                        val id = post.child("id").getValue(String::class.java)
                        val title = post.child("title").getValue(String::class.java)
                        val price = post.child("price").getValue(String::class.java)
                        val description = post.child("description").getValue(String::class.java)
                        val timeStamp = post.child("timeStamp").getValue(String::class.java)

                        val imageUri = snapshot.child("imageUri").getValue(String::class.java)?.toUri()
                        Log.d(TAG, id + title + price + description + timeStamp+ " 이거")


                        homeDataList.add(HomeData(id, title, price, description, timeStamp))

                        //homeDataList.add(HomeData(id, title, price, description, imageUri))

                    }
                }

                homeAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG,"Error fetching data: ${error.message}")
            }
        })
    }

    private fun imageLoad() {

    }

}