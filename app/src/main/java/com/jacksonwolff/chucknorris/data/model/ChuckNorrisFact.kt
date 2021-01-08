package com.jacksonwolff.chucknorris.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class ChuckNorrisFact(
    @Json(name = "id") val id: String,
    @Json(name = "url") val url: String,
    @Json(name = "value") val value: String,
    @Json(name = "icon_url") val iconUrl: String,
    @Json(name = "categories") val categories: Array<String>
) :Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChuckNorrisFact

        if (!categories.contentEquals(other.categories)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + categories.contentHashCode()
        return result
    }
}