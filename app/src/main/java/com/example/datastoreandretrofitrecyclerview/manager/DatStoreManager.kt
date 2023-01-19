package com.example.datastoreandretrofitrecyclerview.manager


import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_model")

class DatStoreManager(private val context: Context) {

    companion object {
        val USER_DETAIL = stringPreferencesKey("USER_DETAIL")
    }

    suspend fun storeUserDetails(userdata: List<UserModelItem>) {
        Log.e("storeUserDetails", "users: " + userdata)
        val user = Gson().toJson(userdata)
        context.dataStore.edit {
            it[USER_DETAIL] = user
        }
    }

    val userDetails: Flow<String> = context.dataStore.data.map {
        it[USER_DETAIL] ?: ""
    }

    fun getAllUserList(user: String): MutableList<UserModelItem>? {
        var users: MutableList<UserModelItem>? = mutableListOf()
        if (user.isNotEmpty() == true) {
            val type = object : TypeToken<List<UserModelItem>>() {}.type
            users = Gson().fromJson<MutableList<UserModelItem>>(user, type)
        }
        Log.d("currentdataprefrence", users.toString())
        return users?.toMutableList()
    }

    /* suspend fun saveUserInfo(userData: UserModelItem?, scope: CoroutineScope ) {
         userDetails.asLiveData().observeForever{
             val getList = getAllUserList(it)
             Log.e("datatostore",getList.toString())
             val userModel = getList?.find { it.id == userData?.id }
             Log.e("matcheditem",userModel.toString())
             if(userModel==null){
                 userData?.let { getList?.add(it) }
                 Log.e("addeditem",getList.toString())
                 if (getList != null) {
                     scope.launch {
                         storeUserDetails(getList)
                     }
                 }
             }
         }

     }
     */
//    suspend fun removeUserFromList(userData: UserModelItem?, coroutineScope:  CoroutineScope) {
//        userDetails.asLiveData().observeForever{
//            val getList = getAllUserList(it)
//            val useritemToRemove = getList?.find { it.id == userData?.id }
//            if(useritemToRemove!=null){
//                userData.let { getList.remove(useritemToRemove) }
//                coroutineScope.launch {
//                    storeUserDetails(getList)
//                }
//            }
//        }
//
//    }
}

