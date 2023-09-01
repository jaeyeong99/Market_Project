package com.example.marketproject.fragment

import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.adapter.ChatAdapter
import com.example.marketproject.data.ChatMessageData
import com.example.marketproject.databinding.FragmentChattingBinding
import com.example.marketproject.databinding.FragmentChattingDetailBinding

class ChattingDetailFragment : Fragment() {

    private lateinit var binding: FragmentChattingDetailBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var chatRoomId: String // You should set this ID when navigating to this fragment
    private val messageList: MutableList<ChatMessageData> = mutableListOf() // List to store messages

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingDetailBinding.inflate(layoutInflater)

        init()
        setupChat()

        return inflater.inflate(R.layout.fragment_chatting_detail, container, false)
    }

    private fun init(){
        mainActivity = activity as MainActivity
        mainActivity.binding.floatingActionButton.hide()
    }

    private fun setupChat() {
        // Set up RecyclerView and adapter
        val adapter = ChatAdapter(messageList) // Create your own RecyclerView.Adapter
        binding.recyclerMessages.adapter = adapter

        // Handle message sending
        binding.btnSubmit.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val messageContent = binding.etMessage.text.toString().trim()
        if (messageContent.isNotEmpty()) {
            val sender = "user1UID" // Replace with the actual sender UID
            val timestamp = System.currentTimeMillis()

            val newMessage = ChatMessageData(sender, messageContent, timestamp)
            // Save newMessage to Firebase or update your data source
            messageList.add(newMessage)
            binding.etMessage.text.clear()
        }
    }
}