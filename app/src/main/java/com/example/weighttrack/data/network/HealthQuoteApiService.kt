package com.example.weighttrack.data.network

import com.example.weighttrack.data.network.dto.QuoteResponse
import retrofit2.Response
import retrofit2.http.GET

interface HealthQuoteApiService {
    // 励志名言接口，国内直连无拦截
    @GET("api/other/yiyan")
    suspend fun getRandomQuote(): Response<QuoteResponse>
}