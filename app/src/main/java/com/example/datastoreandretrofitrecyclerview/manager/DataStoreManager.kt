package com.example.datastoreandretrofitrecyclerview.manager

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import com.google.gson.Gson
import kotlinx.coroutines.flow.*



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_model")

class DataStoreManager(val context: Context) {

    companion object {
        val USER_DETAIL = stringPreferencesKey("USER_DETAIL")
    }

    var users: MutableList<UserModelItem> = mutableListOf()
    val mDataStore: DataStore<Preferences> = context.dataStore
    val userFlow = flow<List<UserModelItem>> {
        Log.d("emit",users.toString())
        emit(users)
    }
//    val userDetails: Flow<String> = context.dataStore.data.map {
//        it[USER_DETAIL] ?: " "
//    }

    suspend fun saveuserItem(addUserData: UserModelItem) {
        val userModel = users.find { data ->
            addUserData.id == data.id
        }
        if (userModel == null) {
            addUserData.let { users.add(it) }
        }
        val user = Gson().toJson(users)
        mDataStore.edit { prefernces ->
            prefernces[USER_DETAIL] = user
        }
        Log.d("sa",user)
    }

    suspend fun removeUserItem(removeUserData: UserModelItem) {
        val userModel = users.find { data ->
            removeUserData.id == data.id
        }
        if (userModel != null) {
            removeUserData.let { users.remove(it) }
        }
        val user = Gson().toJson(users)
        mDataStore.edit { prefernces ->
            prefernces[USER_DETAIL] = user
        }
        Log.d("rm",user)
    }
}