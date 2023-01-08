package com.example.datastoreandretrofitrecyclerview

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey


class DataStoreManager(context:Context) {

    val datastore = context.createDataStore(name = "savedPosts")

    companion object{
//        val SAVED_POSTS = preferencesKey<ArrayList>("posts")
    }
}