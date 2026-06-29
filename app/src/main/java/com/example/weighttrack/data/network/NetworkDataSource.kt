package com.example.weighttrack.data.network

import com.example.weighttrack.data.network.dto.QuoteData
import com.example.weighttrack.data.network.dto.QuoteResponse
import retrofit2.HttpException
import java.io.IOException

class NetworkDataSource(
    private val api: HealthQuoteApiService
) {
    suspend fun fetchRandomQuote(): QuoteData? {
        return try {
            val response = api.getRandomQuote()
            if (response.isSuccessful) {
                response.body()?.data
            } else null
        } catch (e: HttpException) {
            null
        } catch (e: IOException) {
            null
        }
    }
}