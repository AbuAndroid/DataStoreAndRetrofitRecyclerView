package com.example.datastoreandretrofitrecyclerview.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceManger(context: Context) {

    companion object{
        const val USER_DETAILS = "key.user.details"
    }
    private val users = mutableSetOf<UserModelItem>()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(USER_DETAILS, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor? = sharedPreferences.edit()

    fun getUserList(): List<UserModelItem?>? {
        val user = sharedPreferences.getString(USER_DETAILS, null)
        val type = object : TypeToken<List<UserModelItem?>?>() {}.type
        return Gson().fromJson<List<UserModelItem?>?>(user,type)
    }

    fun saveUserInfo(userData: UserModelItem?) {
        userData?.let { users.add(it) }
        val user = Gson().toJson(users)
        editor?.putString(USER_DETAILS, user)?.apply()
    }

    fun removeUserFromList(userData: UserModelItem?) {
        val userModel = users.find { it.id == userData?.id }
        userData?.let { users.remove(userModel) }
        val user = Gson().toJson(users)
        editor?.putString(USER_DETAILS, user)?.apply()
    }
}