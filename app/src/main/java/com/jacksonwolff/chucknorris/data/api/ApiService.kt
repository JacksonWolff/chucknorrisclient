package com.jacksonwolff.chucknorris.data.api

import com.jacksonwolff.chucknorris.data.model.ChuckNorrisFact
import com.jacksonwolff.chucknorris.data.model.SearchFacts
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("jokes/random")
    suspend fun getRandomFact(): ChuckNorrisFact

    @GET("jokes/categories")
    suspend fun getCategories(): MutableList<String>

    @GET("jokes/random")
    suspend fun getCategoryFact(@Query("category") category: String): ChuckNorrisFact

    @GET("jokes/search")
    suspend fun getSearchFacts(@Query("query") query: String): SearchFacts

}