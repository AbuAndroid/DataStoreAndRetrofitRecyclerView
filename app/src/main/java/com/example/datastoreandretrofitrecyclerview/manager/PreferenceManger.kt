package com.example.datastoreandretrofitrecyclerview.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PreferenceManger(context: Context) {

    companion object{
        const val USER_DETAILS = "key.user.details"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(USER_DETAILS, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor? = sharedPreferences.edit()

    fun getAllUserList(): MutableList<UserModelItem>? {
        var users:MutableList<UserModelItem>? = mutableListOf()
        val user = sharedPreferences.getString(USER_DETAILS, null)
        if(user?.isNotEmpty()==true){
            val type = object : TypeToken<List<UserModelItem>>() {}.type
            users = Gson().fromJson<MutableList<UserModelItem>>(user,type)
        }
        return users?.toMutableList()
    }

    fun saveUserInfo(userData: UserModelItem?) {
        var getList = getAllUserList()
        Log.d("getList","size:"+getList?.size)
        val userModel = getList?.find { it.id == userData?.id }
        if(userModel==null){
            userData?.let { getList?.add(it) }
            val user = Gson().toJson(getList)
            editor?.putString(USER_DETAILS, user)?.commit()
            Log.d("currentpreferencedata",sharedPreferences.getString(USER_DETAILS,null).toString())
            val getList = getAllUserList()
            Log.d("getList","size:"+getList?.size)
        }
    }

    fun removeUserFromList(userData: UserModelItem?) {
        val getList = getAllUserList()
        val useritemToRemove = getList?.find {
             it.id == userData?.id
        }
        if(useritemToRemove!=null){
            userData.let { getList.remove(useritemToRemove) }
            val user = Gson().toJson(getList)
            editor?.putString(USER_DETAILS, user)
            editor?.apply()
        }
    }
}