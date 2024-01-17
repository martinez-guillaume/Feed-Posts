package com.devid_academy.tutocomposeoct23.network

import android.content.SharedPreferences
import javax.inject.Inject

class User  @Inject constructor(private val sharedPreferences: SharedPreferences){

    companion object {
        private const val KEY_USER_ID = "userId"
        private const val KEY_TOKEN = "token"
    }

    fun saveUserId(userId: Long) {
        sharedPreferences.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun getUserIdFromPreferences(): Long {
        return sharedPreferences.getLong(KEY_USER_ID, 0L)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getTokenFromPreferences(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }
    fun clearToken() {
        sharedPreferences.edit().remove(KEY_TOKEN).apply()
    }
}
