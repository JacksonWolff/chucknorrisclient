package com.jacksonwolff.chucknorris.data.model

import com.squareup.moshi.Json

data class SearchFacts(
    @Json(name = "total") val total: Int,
    @Json(name = "result") val result: MutableList<ChuckNorrisFact>
)
