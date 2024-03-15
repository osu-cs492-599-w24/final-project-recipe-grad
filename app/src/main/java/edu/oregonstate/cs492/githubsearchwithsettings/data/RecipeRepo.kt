package edu.oregonstate.cs492.githubsearchwithsettings.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class RecipeRepo (
    @Json(name = "strMeal") val name: String,
    @Json(name = "strMealThumb") val url: String,
    @Json(name = "strInstructions") val instruction : String,
    @Json(name = "strYoutube") val youtubeUrl : String,
    @Json(name = "strTags") val tags : String?,
    @Json(name = "strIngredient1") val ingredient1: String?,
    @Json(name = "strIngredient2") val ingredient2: String?,
    @Json(name = "strIngredient3") val ingredient3: String?,
    @Json(name = "strIngredient4") val ingredient4: String?,
    @Json(name = "strIngredient5") val ingredient5: String?,
    @Json(name = "strIngredient6") val ingredient6: String?,
    @Json(name = "strIngredient7") val ingredient7: String?,
    @Json(name = "strIngredient8") val ingredient8: String?,
    @Json(name = "strIngredient9") val ingredient9: String?,
    @Json(name = "strIngredient10") val ingredient10: String?,
    @Json(name = "strIngredient11") val ingredient11: String?,
    @Json(name = "strIngredient12") val ingredient12: String?,
    @Json(name = "strIngredient13") val ingredient13: String?,
    @Json(name = "strIngredient14") val ingredient14: String?,
    @Json(name = "strIngredient15") val ingredient15: String?,
    @Json(name = "strIngredient16") val ingredient16: String?,
    @Json(name = "strIngredient17") val ingredient17: String?,
    @Json(name = "strIngredient18") val ingredient18: String?,
    @Json(name = "strIngredient19") val ingredient19: String?,
    @Json(name = "strIngredient20") val ingredient20: String?
)