package com.example.datastoreandretrofitrecyclerview.network

import com.example.datastoreandretrofitrecyclerview.model.UserModelItem
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi{

    @GET("/posts")
    suspend fun getMyQuotes(): Response<List<UserModelItem>>
}