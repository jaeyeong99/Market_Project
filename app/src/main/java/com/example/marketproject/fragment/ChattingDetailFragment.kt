package com.example.marketproject.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.adapter.ChatAdapter
import com.example.marketproject.adapter.MessageAdapter
import com.example.marketproject.data.MessageData
import com.example.marketproject.databinding.FragmentChattingDetailBinding
import com.google.firebase.storage.FirebaseStorage

class ChattingDetailFragment : Fragment() {

    private lateinit var binding: FragmentChattingDetailBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var chatRoomId: String // You should set this ID when navigating to this fragment
    private val messageList = mutableListOf<MessageData>() // List to store messages
    private lateinit var messageAdapter: MessageAdapter // Create your own RecyclerView.Adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingDetailBinding.inflate(layoutInflater)

        messageAdapter = MessageAdapter(messageList)
        binding.messageRecyclerView.adapter = messageAdapter
        binding.messageRecyclerView.layoutManager = createLayoutManager()

        init()
        setupChat()
        addData()

        return binding.root
    }

    private fun init(){
        mainActivity = activity as MainActivity
        mainActivity.binding.floatingActionButton.hide()
    }

    private fun createLayoutManager(): LinearLayoutManager {
        val manager = LinearLayoutManager(context)
        //manager.reverseLayout = true
        //manager.stackFromEnd = true
        return manager
    }

    private fun addData() {
        val title = arguments?.getString("title")
        val price = arguments?.getString("price")
        val nickName = arguments?.getString("nickName")
        val key = arguments?.getString("key")
        //val sellerId = arguments?.getString("id")

        binding.tvTitle.text = title
        binding.tvPrice.text = price?.let { addCommaToPrice(it) } + "원"
        binding.tvNickName.text = nickName

        var storageUrl = "gs://marketproject-29c48.appspot.com"
        val storage: FirebaseStorage = FirebaseStorage.getInstance(storageUrl)
        val storageRef = storage.reference
        val storagePath = storageRef.child("salesPostImage/$key")

        storagePath.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(binding.root.context)
                .load(uri)
                .into(binding.ivImage)
        }
    }

    private fun setupChat() {
        // Handle message sending
        binding.btnSubmit.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val messageContent = binding.etMessage.text.toString().trim()
        if (messageContent.isNotEmpty()) {
            val sender = mainActivity.auth?.currentUser?.uid.orEmpty()
            val read = false
            val timestamp = System.currentTimeMillis()
            val newMessage = MessageData(sender, messageContent, read, timestamp)
            // Save newMessage to Firebase or update your data source
            messageList.add(newMessage)
            Log.d(ContentValues.TAG, "onnnnnnn11111")
            binding.etMessage.text.clear()
            messageAdapter.notifyDataSetChanged()
        }
    }

    private fun addCommaToPrice(price: String): String {
        val parts = price.split(".")
        val integerPart = parts[0].toIntOrNull() ?: return price  // Return original price if not a valid number

        val formattedIntegerPart = String.format("%,d", integerPart)
        val decimalPart = if (parts.size > 1) ".${parts[1]}" else ""

        return "$formattedIntegerPart$decimalPart"
    }
}