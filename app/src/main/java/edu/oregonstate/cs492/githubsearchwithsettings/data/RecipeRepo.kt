package edu.oregonstate.cs492.githubsearchwithsettings.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class RecipeRepo (
    @Json(name = "strMeal") val name: String,
    @Json(name = "strMealThumb") val url: String
)