package com.example.weighttrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weighttrack.datastore.UserPreferencesRepository

class SettingsViewModelFactory(
    private val prefsRepo: UserPreferencesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(prefsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}