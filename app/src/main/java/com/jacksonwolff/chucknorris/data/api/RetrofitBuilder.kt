package com.jacksonwolff.chucknorris.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitBuilder {

    private const val BASE_URL = "https://api.chucknorris.io/"

    private fun getRetrofit(): Retrofit {

        val moshBuild = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshBuild))
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}