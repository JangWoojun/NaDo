package com.woojun.nado.database

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    fun saveUserName(context: Context, name: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("UserName", name)
        editor.apply()
    }

    fun loadUserName(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("UserName", null)
    }

}