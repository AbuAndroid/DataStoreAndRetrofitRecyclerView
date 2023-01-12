package com.example.datastoreandretrofitrecyclerview.manager

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import com.example.datastoreandretrofitrecyclerview.ui.NewsActivity

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_model")

class DataStoreManager(val context: Context) {

    companion object {
        val USER_DETAIL = stringPreferencesKey("USER_DETAIL")
    }

    private val userDetails: Flow<String> = context.dataStore.data.map {
        it[USER_DETAIL] ?: " "
    }


    private var users: MutableList<UserModelItem>? = mutableListOf()

    suspend fun currentFlowValue(): String? {
        var  list = userDetails.asLiveData().value
//        userDetails.collect{name->
//            list = name
//            Log.d("itvalue",list)
//        }
        Log.d("currentlist", " h  "+list)
        return list
    }

    suspend fun getAllUserList(user: String): MutableList<UserModelItem>? {

        users?.let { storeUserDetails(it) }
        Log.e("uservalue", user + "empty")

        val type = object : TypeToken<List<UserModelItem>>() {}.type
        users = Gson().fromJson<MutableList<UserModelItem>>(user, type)

        Log.d("user", users.toString())
        //Log.d("currentusermodel", usermodel.toString())
        //Log.d("currentdataprefrence", users.toString())
        return users?.toMutableList()
    }

    suspend fun storeUserDetails(userdata: List<UserModelItem>) {
        Log.e("storeUserDetails", "users: " + userdata)
        val user = Gson().toJson(userdata)
        context.dataStore.edit {
            it[USER_DETAIL] = user
        }
    }

    suspend fun saveuserItem(addUserData: UserModelItem) {
        Log.e("savefun", "save function calling")

        val value = currentFlowValue()
        Log.d("redlist", "hjhk"+value)
        val getlist = value?.let { getAllUserList(it) }

        Log.e("datatostore", getlist.toString())
        val userModel = getlist?.find { data ->
            addUserData.id == data.id
        }
        Log.e("matcheditem", userModel.toString())
        if (userModel == null) {
            addUserData.let { getlist?.add(it) }
            Log.e("addeditem", getlist.toString())
            if (getlist != null) {
                storeUserDetails(getlist)
            }
        }
    }

    suspend fun removeUserItem(userData: UserModelItem?) {
        Log.e("teddy", "remove function called")
    }


}