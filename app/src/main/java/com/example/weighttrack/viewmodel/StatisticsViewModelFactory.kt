package com.example.weighttrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weighttrack.data.repository.WeightRepository
import com.example.weighttrack.datastore.UserPreferencesRepository

class StatisticsViewModelFactory(
    private val weightRepo: WeightRepository,
    private val prefsRepo: UserPreferencesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(weightRepo, prefsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}