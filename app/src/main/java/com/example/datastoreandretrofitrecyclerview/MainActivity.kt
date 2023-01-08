package com.example.datastoreandretrofitrecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.datastoreandretrofitrecyclerview.Adapter.RecyclerUserListAdapter
import com.example.datastoreandretrofitrecyclerview.Model.UserModelItem
import com.example.datastoreandretrofitrecyclerview.Network.RetrofitHelper
import com.example.datastoreandretrofitrecyclerview.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var bind: ActivityMainBinding? = null
    var recyclerView: RecyclerView? = null

    private val customAdapter: RecyclerUserListAdapter by lazy {
        RecyclerUserListAdapter(
            userList = arrayListOf(),
            onItemClick = this::makeToast
        )
    }


    var uiPvDataLoading:ProgressBar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind?.root)

        uiPvDataLoading = findViewById(R.id.uiPvDataLoading)

        recyclerView = findViewById(R.id.uiRvdataList)
        recyclerView?.adapter = customAdapter
        getResultFromServer()

        bind?.uiSvSearchItems?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                customAdapter.filter.filter(newText)
                return false
            }
        })
        //recyclerView?.adapter=customAdapter
    }

//    private fun setUpUi() {
//
//    }

    private fun getResultFromServer() {
        GlobalScope.launch(Dispatchers.Main) {
            val list = RetrofitHelper.newsInstance.getMyQuotes().body()
            uiPvDataLoading?.visibility = View.GONE
            list?.let {
                customAdapter.onNewsListChanged(list)
            }
        }
    }

    private fun makeToast(posts:UserModelItem) {
        Log.d("res",posts.toString())

        Toast.makeText(this,posts.id.toString(),Toast.LENGTH_LONG).show()
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

