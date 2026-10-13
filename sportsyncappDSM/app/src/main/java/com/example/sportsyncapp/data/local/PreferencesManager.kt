package com.example.sportsyncapp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.sportsyncapp.data.model.User
import com.google.gson.Gson

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sportsync_preferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ROLE = "user_role" // 1 = FAN, 2 = ADMIN
        private const val KEY_CURRENT_USER = "current_user"
        private const val KEY_REMEMBER_EMAIL = "remember_email"
        private const val KEY_SAVED_EMAIL = "saved_email"
    }

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var userRole: Int
        get() = prefs.getInt(KEY_USER_ROLE, 1) // Por defecto FAN (1)
        set(value) = prefs.edit().putInt(KEY_USER_ROLE, value).apply()

    fun isAdmin(): Boolean = userRole == 2

    var currentUser: User?
        get() {
            val json = prefs.getString(KEY_CURRENT_USER, null) ?: return null
            return try {
                gson.fromJson(json, User::class.java)
            } catch (_: Exception) {
                null
            }
        }
        set(value) {
            if (value == null) {
                prefs.edit().remove(KEY_CURRENT_USER).apply()
            } else {
                prefs.edit().putString(KEY_CURRENT_USER, gson.toJson(value)).apply()
            }
        }

    var rememberEmail: Boolean
        get() = prefs.getBoolean(KEY_REMEMBER_EMAIL, false)
        set(value) = prefs.edit().putBoolean(KEY_REMEMBER_EMAIL, value).apply()

    var savedEmail: String
        get() = prefs.getString(KEY_SAVED_EMAIL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_SAVED_EMAIL, value).apply()

    fun clearSession() {
        prefs.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_USER_ROLE)
            .remove(KEY_CURRENT_USER)
            .apply()
    }
}
