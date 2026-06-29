package com.example.weighttrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weighttrack.data.repository.WeightRepository

class EditRecordViewModelFactory(
    private val repo: WeightRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditRecordViewModel::class.java)) {
            return EditRecordViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}