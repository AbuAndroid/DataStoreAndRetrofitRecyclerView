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
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.datastoreandretrofitrecyclerview.R
import com.example.datastoreandretrofitrecyclerview.adapter.UserAdapter
import com.example.datastoreandretrofitrecyclerview.databinding.ActivityNewsBinding
import com.example.datastoreandretrofitrecyclerview.manager.DatStoreManager
import com.example.datastoreandretrofitrecyclerview.manager.dataStore
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import com.example.datastoreandretrofitrecyclerview.network.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

class NewsActivity : AppCompatActivity() {

    var bind: ActivityNewsBinding? = null
    private val userAdapter by lazy {
        UserAdapter(
            userList = arrayListOf(),
            onItemClick = ::onSaveItemOrRemoveItem
        )
    }

    //lazy iitializing preferenceManager
    private val datStoreManager by lazy {
        DatStoreManager(this)
    }

    // private var dataStoreManager: DatStoreManager? = null
    private val users: MutableList<UserModelItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(bind?.root)

        
        //set up all ui
        setUpui()
        //set all listener funtions
        setUpListeners()
        //getting all data from server
        getUserData()
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

    private fun checkNewsSavedInDb(users: List<UserModelItem>) {
        datStoreManager.userDetails.asLiveData().observe(this) {
            val savedList = datStoreManager.getAllUserList(it)
            for (user in users) {
                savedList?.forEach { userSaved ->
                    if (user.id == userSaved.id) {
                        user.isSaved = true
                    }
                }
            }
        }

    }

    //filtering data in userlist
    private fun filterUserList(searchText: String) {
        if (searchText.isEmpty()) {
            setUserListToUi(users)
        } else {
            val filteredList = users.filter {
                it.title.contains(searchText, true)
            }
            setUserListToUi(filteredList)
        }
    }

    //padding list to recyclerview
    private fun setUserListToUi(users: List<UserModelItem>) {
        userAdapter.onNewsListChanged(users)
    }


    @SuppressLint("SuspiciousIndentation")
    private fun onSaveItemOrRemoveItem(currentItem: UserModelItem?) {
        Log.e("onSaveItemOrRemove", ":" + currentItem.toString())
        if (currentItem?.isSaved == false) {
            currentItem.isSaved = true
            //preferenceManger.saveUserInfo(userModel)
            lifecycleScope.launch {
                datStoreManager.userDetails.collectLatest {
                    val getList = datStoreManager.getAllUserList(it)
                    Log.e("datatostore", getList.toString())
                    val userModel = getList?.find { currentItem.id ==it.id }
                    Log.e("matcheditem", userModel.toString())
                    if (userModel == null) {
                        getList?.add(currentItem)
                        Log.e("addeditem", getList.toString())
                        if (getList != null) {
                            datStoreManager.storeUserDetails(getList)
                        }
                    }
                }
            }
            //  datStoreManager.saveUserInfo(userModel,this)
        } else {
            currentItem?.isSaved = false
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete List..")
            builder.setMessage("Do you want to delete this item.")
            builder.setIcon(R.drawable.ic_baseline_bookmark_remove)
            builder.setPositiveButton("OK") { dialog, which ->
                //currentItem?.isSaved=false
                lifecycleScope.launch {
                    datStoreManager.userDetails.collectLatest {
                        val getList = datStoreManager.getAllUserList(it)
                        val useritemToRemove = getList?.find {  currentItem?.id==it.id }
                        if (useritemToRemove != null) {
                            Log.d("removeitem",useritemToRemove.toString())
                            getList.remove(useritemToRemove)
                            if (getList != null) {
                                datStoreManager.storeUserDetails(getList)
                            }
                        }
                    }
                }


//                    datStoreManager.userDetails.asLiveData().observeForever{
//                        val getList = datStoreManager.getAllUserList(it)
//                        val useritemToRemove = getList?.find { it.id == currentItem?.id }
//                        if(useritemToRemove!=null){
//                            currentItem.let { getList.remove(useritemToRemove) }
//                            lifecycleScope.launch {
//                                datStoreManager.storeUserDetails(getList)
//                            }
//
//                        }
            // datStoreManager.removeUserFromList(currentItem,this)
        }

        builder.setNegativeButton("CANCEL") { dialog, which ->
            Toast.makeText(this, "user don't need to delete..", Toast.LENGTH_SHORT).show()
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

}