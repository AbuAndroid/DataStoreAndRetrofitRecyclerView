package com.example.datastoreandretrofitrecyclerview.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
            onItemClick = ::onSaveItemOrRemove
        )
    }
    //lazy iitializing preferenceManager
    private val preferenceManger by lazy { PreferenceManger(this) }

    private var users : MutableList<UserModelItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(bind?.root)
        //set up all ui
        setUpui()
        //set all listener funtions
        setUpListeners()
        //getting all data from server
    }
    //settingUi
    private fun setUpui() {
        bind?.uiRvdataList?.adapter = userAdapter
    }
    //setting all clicking events
    @SuppressLint("NotifyDataSetChanged")
    private fun setUpListeners() {
        //pull refersh
        bind?.uiRefresh?.setOnRefreshListener {
            bind?.uiRefresh?.isRefreshing = false
            getUserData()
            bind?.uiRvdataList?.adapter?.notifyDataSetChanged()
        }
        //search text
        bind?.uiEtSearch?.doOnTextChanged { text, start, before, count ->
            filterUserList(text.toString())
        }
    }
    //retriving data from server
    private fun getUserData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val list = RetrofitHelper.newsInstance.getMyQuotes().body()
            bind?.uiPvDataLoading?.visibility = View.GONE
            list?.let {
                checkNewsSavedInDb(it)
                Log.d("servercalldata", "getUser data from server: $it")
                users.clear()
                users.addAll(it)
                setUserListToUi(it)
            }
        }
    }
    //filtering data in userlist
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
    //padding list to recyclerview
    private fun setUserListToUi(users : List<UserModelItem>) {
        userAdapter.onNewsListChanged(users)
    }

    private fun checkNewsSavedInDb(users: List<UserModelItem>) {
        val savedList = preferenceManger.getAllUserList()
        for(user in users){
            savedList?.forEach { userSaved->
                if(user.id == userSaved.id){
                    user.isSaved = true
                }
            }
        }
    }

    private fun onSaveItemOrRemove(userModel: UserModelItem?) {
        if(userModel?.isSaved==false){
           userModel.isSaved=true
            preferenceManger.saveUserInfo(userModel)
        }
        else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete List..")
            builder.setMessage("Do you want to delete this item.")
            builder.setIcon(R.drawable.ic_baseline_bookmark_remove)
            builder.setPositiveButton("OK"){dialog,which ->
                userModel?.isSaved=false
                preferenceManger.removeUserFromList(userModel)
            }
            builder.setNegativeButton("CANCEL"){dialog,which ->
                Toast.makeText(this,"user don't need to delete..",Toast.LENGTH_SHORT).show()
            }
            builder.show()
        }
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

    override fun onResume() {
        super.onResume()
        getUserData()
    }
}