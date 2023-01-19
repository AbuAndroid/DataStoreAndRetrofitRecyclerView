package com.example.datastoreandretrofitrecyclerview.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.datastoreandretrofitrecyclerview.R
import com.example.datastoreandretrofitrecyclerview.adapter.UserAdapter
import com.example.datastoreandretrofitrecyclerview.databinding.ActivitySavedNewsBinding
import com.example.datastoreandretrofitrecyclerview.manager.PreferenceManger
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem

class SavedNewsActivity : AppCompatActivity() {

    private val preferenceManger by lazy { PreferenceManger(this) }
    private var bind: ActivitySavedNewsBinding? = null
    private val userAdapter by lazy {
        UserAdapter(
            userList = arrayListOf(),
            onItemClick = {
                onUnSavedNews(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySavedNewsBinding.inflate(layoutInflater)
        setContentView(bind?.root)
        setUpui()
        getUserData()
    }

    private fun getUserData() {
        preferenceManger.getAllUserList().let {
            userAdapter.onNewsListChanged(it)
        }
    }

    private fun setUpui() {
        bind?.uiRvSaveItems?.adapter = userAdapter
    }

    private fun onUnSavedNews(userModelItem: UserModelItem?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete List..")
        builder.setMessage("Do you want to delete this item.")
        builder.setIcon(R.drawable.ic_baseline_bookmark_remove)
        builder.setPositiveButton("OK"){dialog,which ->
            preferenceManger.removeUserFromList(userModelItem)
            getUserData()
        }
        builder.setNegativeButton("CANCEL"){dialog,which ->
            Toast.makeText(this,"user don't need to delete..", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }
}