package com.example.weighttrack.data.network.dto

data class HealthQuoteDto(
    val hitokoto: String,
    val from: String,
    val from_who: String?,
    val type: String
)