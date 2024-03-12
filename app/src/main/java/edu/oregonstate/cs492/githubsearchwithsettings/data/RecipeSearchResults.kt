package edu.oregonstate.cs492.githubsearchwithsettings.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeSearchResults (
    val meals: List<RecipeRepo>
)