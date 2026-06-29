package com.example.weighttrack.viewmodel

import com.example.weighttrack.data.entity.WeightRecordEntity

// 网络每日语录状态
sealed class QuoteUiState {
    object Loading : QuoteUiState()
    data class Success(val quote: String, val source: String) : QuoteUiState()
    data class Error(val message: String) : QuoteUiState()
}

// 体重记录列表状态
sealed class RecordListUiState {
    object Loading : RecordListUiState()
    object Empty : RecordListUiState()
    data class Success(val records: List<WeightRecordEntity>) : RecordListUiState()
    data class Error(val message: String) : RecordListUiState()
}