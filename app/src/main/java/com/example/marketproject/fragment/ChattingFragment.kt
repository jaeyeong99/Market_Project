package com.example.marketproject.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.adapter.ChatRoomAdapter
import com.example.marketproject.data.ChatRoomData
import com.example.marketproject.databinding.FragmentChattingBinding

class ChattingFragment : Fragment() {

    private lateinit var binding: FragmentChattingBinding
    private lateinit var mainActivity: MainActivity

    private val chatRoomList = mutableListOf<ChatRoomData>()
    lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(layoutInflater)

        init()
        setAdapter()
        clickListener()

        return binding.root
    }

    private fun init(){
        mainActivity = activity as MainActivity
        mainActivity.binding.floatingActionButton.hide()
    }

    private fun setAdapter() {
        chatRoomAdapter = ChatRoomAdapter(chatRoomList)
        binding.chatRoomRecyclerView.adapter = chatRoomAdapter
        binding.chatRoomRecyclerView.layoutManager = createLayoutManager()
    }

    private fun createLayoutManager(): LinearLayoutManager {
        val manager = LinearLayoutManager(context)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        return manager
    }

    private fun clickListener(){
//        binding.tvTitle.setOnClickListener {
//            mainActivity.setFragment(ChattingDetailFragment(), "ChattingDetailFragment")
//            mainActivity.hideNaviBarAndFloatingBtn()
//        }
    }
}