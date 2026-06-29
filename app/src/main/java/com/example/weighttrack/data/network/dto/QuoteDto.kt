package com.example.weighttrack.data.network.dto

// 外层整体响应
data class QuoteResponse(
    val success: Boolean,
    val data: QuoteData
)

// 名言本体
data class QuoteData(
    val content: String,
    val author: String
)