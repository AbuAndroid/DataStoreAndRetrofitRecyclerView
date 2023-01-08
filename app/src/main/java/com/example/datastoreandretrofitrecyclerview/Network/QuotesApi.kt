package com.example.datastoreandretrofitrecyclerview.Network

import com.example.datastoreandretrofitrecyclerview.Model.UserModelItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi{

    @GET("/posts")
    suspend fun getMyQuotes(): Response<List<UserModelItem>>
}