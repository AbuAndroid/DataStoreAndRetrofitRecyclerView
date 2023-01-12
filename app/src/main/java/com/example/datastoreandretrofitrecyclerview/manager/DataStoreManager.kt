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

class DataStoreManager(private val context: Context) {

    companion object {
        val USER_DETAIL = stringPreferencesKey("USER_DETAIL")
    }


    //    val userDetails : Flow<String> = context.dataStore.data.map {
//        it[USER_DETAIL] ?:" "
//    }
   // var users: mut = mutableListOf()
    var users = mutableListOf<UserModelItem>()
    suspend fun storeUserDetails(userdata: List<UserModelItem>) {
        Log.e("storeUserDetails", "users: " + userdata)
        val user = Gson().toJson(userdata)

        context.dataStore.edit {
            it[USER_DETAIL] = user
        }
    }

    val userDetails: Flow<String> = context.dataStore.data.map {
        it[USER_DETAIL] ?: " "
    }

    suspend fun getAllUserList(user:String): MutableList<UserModelItem> {

        val usermodel = userDetails.asLiveData().observeForever {
            if (user.isNotEmpty() == true) {
                val type = object : TypeToken<List<UserModelItem>>() {}.type
                users = Gson().fromJson<MutableList<UserModelItem>>(user, type)
            }
        }
        Log.d("currentusermodel", usermodel.toString())
        Log.d("currentdataprefrence", users.toString())
        return users.toMutableList()
    }

    suspend fun saveUserItem(addUserData: UserModelItem) {
        val getList = getAllUserList()
        Log.e("datatostore", getList.toString())
        val userModel = getList.find { addUserData.id == it.id }
        Log.e("matcheditem", userModel.toString())
        if (userModel == null) {
            addUserData.let { getList.add(it) }
            Log.e("addeditem", getList.toString())
            if (getList != null) {
                storeUserDetails(getList)
            }
        }
    }

    suspend fun removeUserItem(userData: UserModelItem?) {

    }
}