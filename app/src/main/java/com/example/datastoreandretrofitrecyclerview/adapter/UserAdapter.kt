package com.example.datastoreandretrofitrecyclerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.datastoreandretrofitrecyclerview.R
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import java.util.*


class UserAdapter(
    private val userList: ArrayList<UserModelItem>?,
    private val onItemClick: (UserModelItem?) -> Unit,
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclercustomlayout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsList = userList?.get(position)
        if ((itemsList?.id?.rem(2) ?: 0) == 0) {
            holder.uiCardItems.setCardBackgroundColor(R.color.blue)
        } else {
            holder.uiCardItems.setCardBackgroundColor(R.color.red)
        }
        holder.uiTvUserId.text = itemsList?.id.toString()
        holder.uiTvUserTitle.text = itemsList?.title
        holder.uiTvUserDescription.text = itemsList?.body
        if (itemsList?.isSaved == true) {
            holder.uiBtToSaveItems.setImageResource(R.drawable.ic_baseline_bookmark)
        } else {
            holder.uiBtToSaveItems.setImageResource(R.drawable.ic_baseline_bookmark_border)
        }
    }

    override fun getItemCount(): Int {
        return userList?.size?:0
    }


    //updating recyclerview when data list changed
    @SuppressLint("NotifyDataSetChanged")
    fun onNewsListChanged(postList:List<UserModelItem>?) {
        this.userList?.clear()
        postList?.let { this.userList?.addAll(it) }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val uiTvUserId = view.findViewById<TextView>(R.id.uiTvId)!!
        val uiTvUserTitle = view.findViewById<TextView>(R.id.uiTvTitle)!!
        val uiTvUserDescription = view.findViewById<TextView>(R.id.uiTvBody)!!
        val uiBtToSaveItems = view.findViewById<ImageView>(R.id.uiBtSaveitems)!!
        val uiCardItems = view.findViewById<CardView>(R.id.uiCardItems)!!

        init {
            uiBtToSaveItems.setOnClickListener {
                onItemClick(userList?.get(adapterPosition))
                notifyDataSetChanged()
            }
        }
    }
}