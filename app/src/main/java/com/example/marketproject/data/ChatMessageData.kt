package com.example.marketproject.data

import java.sql.Timestamp


data class ChatMessageData (
    val sender: String,
    val message: String,
    val timestamp: Long
)

//user
//user_id
//채팅key - 게시물key
//       - 내id
//       - 상대방id
//       - 메세지
//       - 타임스탬프

//    val key: String,
//    val id: String,
//    val timeStamp : String
//    val message : String,