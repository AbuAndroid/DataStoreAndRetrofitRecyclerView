package com.example.datastoreandretrofitrecyclerview

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.datastoreandretrofitrecyclerview.adapter.RecyclerBookmarkListAdapter
import com.example.datastoreandretrofitrecyclerview.databinding.ActivityBookmarkItemsBinding
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class BookmarkItems : AppCompatActivity() {
    var bind : ActivityBookmarkItemsBinding?=null
    private val customAdapter:RecyclerBookmarkListAdapter by lazy {
        RecyclerBookmarkListAdapter(
            savedList = arrayListOf()
        )
    }

    var courseModalArrayList : ArrayList<UserModelItem> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityBookmarkItemsBinding.inflate(layoutInflater)
        setContentView(bind?.root)

        loadData()

        bind?.uiRvSaveItems?.adapter = customAdapter
        //customAdapter.onNewsListChanged(courseModalArrayList)
    }
    private fun loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        val sharedPreferences :SharedPreferences = getSharedPreferences("savedList", MODE_PRIVATE)

        // creating a variable for gson.
        val gson = Gson()

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        val json = sharedPreferences.getString("savedpostList", null)
        Log.d("resp",json.toString())
        // below line is to get the type of our array list.
        val type: Type = object : TypeToken<ArrayList<UserModelItem?>?>() {}.type

        // in below line we are getting data from gson
        // and saving it to our array list
        courseModalArrayList = gson.fromJson(json, type)
        customAdapter.notifyDataSetChanged()

        // checking below if the array list is empty or not
    }
}