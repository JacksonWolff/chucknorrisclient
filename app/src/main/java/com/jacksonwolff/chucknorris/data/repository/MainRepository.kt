package com.jacksonwolff.chucknorris.data.repository

import com.jacksonwolff.chucknorris.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getRandomFact() = apiHelper.getRandomFact()
    suspend fun getCategoryFact(category: String) = apiHelper.getCategoryFact(category)
    suspend fun getCategories() = apiHelper.getCategories()
    suspend fun getSearchFacts(term: String) = apiHelper.getSearchFacts(term)

}