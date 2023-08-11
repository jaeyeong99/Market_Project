package com.example.marketproject.fragment

import android.content.ContentValues
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

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference

    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        homeAdapter = HomeAdapter(homeDataList)
        binding.homeRecyclerView.adapter = homeAdapter
        binding.homeRecyclerView.layoutManager = createLayoutManager()



        init()
        clickListener()
        addHomeData()


        return binding.root
    }


    private fun init(){
        mainActivity = activity as MainActivity
        mainActivity.setBottomNavigationVisibility(View.VISIBLE)
    }

    private fun clickListener(){
        homeAdapter.setItemClickListener(object: HomeAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                Log.d(TAG, "onItemClick: $position")
                mainActivity.setFragment("HomeDetailFragment")


            }
        })

    }

    private fun createLayoutManager(): LinearLayoutManager {
        val manager = LinearLayoutManager(context)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        return manager
    }


    private fun addHomeData() {
        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("salesPost")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (post in snapshot.children){
                    if (snapshot.exists()) {
                        val id = post.child("id").getValue(String::class.java)!!
                        val title = post.child("title").getValue(String::class.java)!!
                        val price = post.child("price").getValue(String::class.java)!!
                        val description = post.child("description").getValue(String::class.java)!!
                        val timeStamp = post.child("timeStamp").getValue(String::class.java)!!

                        homeDataList.add(HomeData(id, title, price, description, timeStamp))
                    }
                }

                homeAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG,"Error fetching data: ${error.message}")
            }
        })
    }


}