package com.example.datastoreandretrofitrecyclerview

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.datastoreandretrofitrecyclerview.adapter.UserAdapter
import com.example.datastoreandretrofitrecyclerview.databinding.ActivityMainBinding
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import com.example.datastoreandretrofitrecyclerview.network.RetrofitHelper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var bind: ActivityMainBinding? = null
    private val customAdapter: UserAdapter by lazy {
        UserAdapter(
            userList = arrayListOf(),
            onItemClick = this::makeToast
        )
    }
    val sharedPreference : SharedPreferences = getSharedPreferences("savedList", MODE_PRIVATE)
    val editor : SharedPreferences.Editor = sharedPreference.edit()
    val gson = Gson()

    var savedDataArrayList : ArrayList<UserModelItem> = arrayListOf()
    var uiPvDataLoading:ProgressBar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind?.root)

        uiPvDataLoading = findViewById(R.id.uiPvDataLoading)
        bind?.uiRvdataList?.adapter = customAdapter
        getResultFromServer()

    }

    private fun getResultFromServer() {
        lifecycleScope.launch(Dispatchers.Main) {
            val list = RetrofitHelper.newsInstance.getMyQuotes().body()
            uiPvDataLoading?.visibility = View.GONE
            list?.let {
                customAdapter.onNewsListChanged(list)
            }
        }
    }

    private fun makeToast(posts:UserModelItem?) {
        /*Log.d("res",posts.toString())
        savedDataArrayList.add(posts)

        val json = gson.toJson(posts)
        Log.d("postresponce",json)
        editor.putString("savedpostList",json).apply()*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuitems, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.uiBmitem) {
            startActivity(Intent(this, BookmarkItems::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}

