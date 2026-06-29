package com.example.weighttrack.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {
    // 原有存储键
    private val KEY_HEIGHT = stringPreferencesKey("height")
    private val KEY_TARGET_WEIGHT = stringPreferencesKey("target_weight")
    private val KEY_GENDER = stringPreferencesKey("gender")
    private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")

    // ========== 新增：出生日期存储键 ==========
    private val KEY_BIRTH_DATE = stringPreferencesKey("birth_date")

    // 原有数据流
    val heightFlow: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[KEY_HEIGHT] ?: "" }

    val targetWeightFlow: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[KEY_TARGET_WEIGHT] ?: "" }

    val genderFlow: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[KEY_GENDER] ?: "" }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[KEY_DARK_MODE] ?: false }

    // ========== 新增：出生日期数据流 ==========
    val birthDateFlow: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[KEY_BIRTH_DATE] ?: "" }

    // ========== 修改：保存方法新增出生日期参数 ==========
    suspend fun saveUserProfile(height: String, targetWeight: String, gender: String, birthDate: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HEIGHT] = height
            prefs[KEY_TARGET_WEIGHT] = targetWeight
            prefs[KEY_GENDER] = gender
            prefs[KEY_BIRTH_DATE] = birthDate // 新增保存出生日期
        }
    }

    // 修改深色模式开关（原有不变）
    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = isDark
        }
    }
}