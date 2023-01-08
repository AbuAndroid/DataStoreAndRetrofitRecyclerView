package com.example.datastoreandretrofitrecyclerview.Adapter

import android.annotation.SuppressLint
import android.graphics.Color.BLUE
import android.graphics.Color.RED
import android.hardware.camera2.params.RggbChannelVector.BLUE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.datastoreandretrofitrecyclerview.Model.UserModelItem
import com.example.datastoreandretrofitrecyclerview.R
import java.util.*
import kotlin.collections.ArrayList


class RecyclerUserListAdapter(
    val userList: ArrayList<UserModelItem>,
    private val onItemClick: (UserModelItem)->Unit
    ): RecyclerView.Adapter<RecyclerUserListAdapter.ViewHolder>(),Filterable {

    var userFilterList = ArrayList<UserModelItem>()
    init {
        userFilterList = userList
    }
//gjhghjgkhgjkhgj
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclercustomlayout,parent,false)
        return  ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = userFilterList[position]
        if(position%2 == 0){
            holder.uiitems.setBackgroundColor(R.color.red)
        }else{
//            holder.uiCardItems.setBackgroundColor(R.color.blue)
        }
        holder.uiTvUserId.text = ItemsViewModel.id.toString()
        holder.uiTvUserTitle.text = ItemsViewModel.title
        holder.uiTvUserDescription.text = ItemsViewModel.body
    }

    override fun getItemCount(): Int {
        return userFilterList.size
    }

   @SuppressLint("NotifyDataSetChanged")
   fun onNewsListChanged(postList: List<UserModelItem>) {
        this.userFilterList.clear()
        this.userFilterList.addAll(postList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(Itemview : View):RecyclerView.ViewHolder(Itemview){
        val uiTvUserId = Itemview.findViewById<TextView>(R.id.uiTvId)
        val uiTvUserTitle = Itemview.findViewById<TextView>(R.id.uiTvTitle)
        val uiTvUserDescription = Itemview.findViewById<TextView>(R.id.uiTvBody)
        val uiIbtnSaveitems = Itemview.findViewById<ImageButton>(R.id.uiIbtnSaveitems)
        val uiCardItems = Itemview.findViewById<CardView>(R.id.uiCardItems)
        val uiitems = Itemview.findViewById<ConstraintLayout>(R.id.uiitems)
        init {
            uiIbtnSaveitems.setOnClickListener{
//                onItemClick(newsList[adapterPosition])
                onItemClick(userFilterList[adapterPosition])
            }
        }
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    userFilterList = userList
                } else {
                    val resultList = arrayListOf<UserModelItem>()
                    for (i in userList) {
                        if (i.title.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(i)
                        }
                    }
                    userFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = userFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, result: FilterResults?) {
                userFilterList = result?.values as ArrayList<UserModelItem>
                notifyDataSetChanged()
            }

        }
    }
}