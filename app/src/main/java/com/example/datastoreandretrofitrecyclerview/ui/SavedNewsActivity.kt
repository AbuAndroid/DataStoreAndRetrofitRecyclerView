package com.example.datastoreandretrofitrecyclerview.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.datastoreandretrofitrecyclerview.adapter.UserAdapter
import com.example.datastoreandretrofitrecyclerview.databinding.ActivitySavedNewsBinding
import com.example.datastoreandretrofitrecyclerview.manager.PreferenceManger
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem

class SavedNewsActivity : AppCompatActivity() {

    private var bind : ActivitySavedNewsBinding? = null

    private val userAdapter by lazy {
        UserAdapter(
            userList = arrayListOf(),
            onItemClick = ::onUnSavedNews
        )
    }

    private val preferenceManger by lazy { PreferenceManger(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySavedNewsBinding.inflate(layoutInflater)
        setContentView(bind?.root)
        setUpui()
        getUserData()
    }

    private fun getUserData() {
        preferenceManger.getUserList()?.let {
            userAdapter.onNewsListChanged(it)
        }
    }

    private fun setUpui() {
        bind?.uiRvSaveItems?.adapter = userAdapter
    }

    private fun onUnSavedNews(userModelItem: UserModelItem?) {
        userModelItem?.isSaved = false
        preferenceManger.removeUserFromList(userModelItem)
        getUserData()
    }
}