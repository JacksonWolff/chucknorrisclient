package com.jacksonwolff.chucknorris.data.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getRandomFact() = apiService.getRandomFact()
    suspend fun getCategories() = apiService.getCategories()
    suspend fun getCategoryFact(category: String) = apiService.getCategoryFact(category)
    suspend fun getSearchFacts(term: String) = apiService.getSearchFacts(term)


}