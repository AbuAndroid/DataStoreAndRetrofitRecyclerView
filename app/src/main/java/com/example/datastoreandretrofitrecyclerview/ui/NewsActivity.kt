package com.example.datastoreandretrofitrecyclerview.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.datastoreandretrofitrecyclerview.R
import com.example.datastoreandretrofitrecyclerview.adapter.UserAdapter
import com.example.datastoreandretrofitrecyclerview.databinding.ActivityNewsBinding
import com.example.datastoreandretrofitrecyclerview.manager.PreferenceManger
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import com.example.datastoreandretrofitrecyclerview.network.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsActivity : AppCompatActivity() {

    var bind: ActivityNewsBinding? = null
    private val userAdapter by lazy {
        UserAdapter(
            userList = arrayListOf(),
            onItemClick = ::onSaveItem
        )
    }

    private val preferenceManger by lazy { PreferenceManger(this) }

    private val users : ArrayList<UserModelItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(bind?.root)
        setUpui()
        setUpListeners()
        getUserData()
    }

    private fun setUpui() {
        bind?.uiRvdataList?.adapter = userAdapter
    }

    private fun setUpListeners() {
        bind?.uiEtSearch?.doOnTextChanged { text, start, before, count ->
            filterUserList(text.toString())
        }
    }

    private fun getUserData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val list = RetrofitHelper.newsInstance.getMyQuotes().body()
            bind?.uiPvDataLoading?.visibility = View.GONE
            list?.let {
                Log.d("SAVED", "getSavedData: ${preferenceManger.getUserList()}")
                checkNewsSavedInDb(it)
                Log.d("TAG", "getUserData: $it")
                users.addAll(it)
                setUserListToUi(it)
            }
        }
    }

    private fun filterUserList(searchText: String) {
        if (searchText.isEmpty()) {
            setUserListToUi(users)
        }else {
            val filteredList = users.filter {
                it.title.contains(searchText,true)
            }
            setUserListToUi(filteredList)
        }
    }


    private fun checkNewsSavedInDb(users: List<UserModelItem>) {
        val savedList = preferenceManger.getUserList()
        users.forEach { user ->
            val savedUser = savedList?.find { savedUser ->
                savedUser?.id == user.id
            }
            if (savedUser != null){
                savedUser.isSaved = true
            }
        }
    }

    private fun setUserListToUi(users : List<UserModelItem>) {
        userAdapter.onNewsListChanged(users)
    }


    private fun onSaveItem(userModelItem: UserModelItem?) {
        userModelItem?.isSaved = true
        preferenceManger.saveUserInfo(userModelItem)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuitems, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.uiBmitem) {
            startActivity(Intent(this, SavedNewsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}