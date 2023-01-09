package com.example.datastoreandretrofitrecyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.datastoreandretrofitrecyclerview.R
import com.example.datastoreandretrofitrecyclerview.model.UserModelItem

class RecyclerBookmarkListAdapter(
    val savedList: ArrayList<UserModelItem>
):RecyclerView.Adapter<RecyclerBookmarkListAdapter.ViewHolder>() {

    inner class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        val uiTvUserId = itemview.findViewById<TextView>(R.id.uiTvId)!!
        val uiTvUserTitle = itemview.findViewById<TextView>(R.id.uiTvTitle)!!
        val uiTvUserDescription = itemview.findViewById<TextView>(R.id.uiTvBody)!!
        private val uiBtToSaveitems = itemview.findViewById<ImageView>(R.id.uiBtSaveitems)!!
        val uiCardItems = itemview.findViewById<CardView>(R.id.uiCardItems)!!

//        init {
//            uiBtToSaveitems.setOnClickListener {
//                onItemClick(userFilterList[adapterPosition])
//                uiBtToSaveitems.setImageResource(R.drawable.ic_baseline_bookmark)
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclercustomlayout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = savedList[position]
        holder.uiTvUserId.text = list.id.toString()
        holder.uiTvUserTitle.text = list.title
        holder.uiTvUserDescription.text = list.body
    }

    override fun getItemCount(): Int {
       return savedList.size
    }


}