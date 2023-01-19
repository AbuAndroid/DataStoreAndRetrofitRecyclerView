package com.example.datastoreandretrofitrecyclerview.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.datastoreandretrofitrecyclerview.R
import com.example.datastoreandretrofitrecyclerview.adapter.UserAdapter
import com.example.datastoreandretrofitrecyclerview.databinding.ActivitySavedNewsBinding
import com.example.datastoreandretrofitrecyclerview.manager.DatStoreManager
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SavedNewsActivity : AppCompatActivity() {

    private val dataStoreManager by lazy {
        DatStoreManager(this)
    }

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
        lifecycleScope.launch {
            dataStoreManager.userDetails.asLiveData().observeForever {
                val type = object : TypeToken<List<UserModelItem>>() {}.type
                val users = Gson().fromJson<MutableList<UserModelItem>>(it, type)
                userAdapter.onNewsListChanged(users)
            }
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
        builder.setPositiveButton("OK") { dialog, which ->
            lifecycleScope.launch{
                dataStoreManager.userDetails.collectLatest {
                    val getList = dataStoreManager.getAllUserList(it)
                    val useritemToRemove = getList?.find { it.id == userModelItem?.id }
                    if (useritemToRemove != null) {
                        userModelItem.let { getList.remove(useritemToRemove) }
                        if (getList != null) {
                            lifecycleScope.launch {
                                dataStoreManager.storeUserDetails(getList)
                            }
                        }

                    }
                }
            }

            builder.setNegativeButton("CANCEL") { dialog, which ->
                Toast.makeText(this, "user don't need to delete..", Toast.LENGTH_SHORT).show()
            }
            builder.show()
        }


    }
}