package com.example.marketproject.data


data class ChatData (
    val key: String,
    val id: String,
    val message : String,
    val timeStamp : String
)

//user
//user_id
//채팅key - 게시물key
//       - 내id
//       - 상대방id
//       - 메세지
//       - 타임스탬프