package com.example.datastoreandretrofitrecyclerview.network

import com.example.datastoreandretrofitrecyclerview.warehouse.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    val newsInstance : QuotesApi
    init {
        val userInstance = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsInstance =userInstance.create(QuotesApi::class.java)
    }


}