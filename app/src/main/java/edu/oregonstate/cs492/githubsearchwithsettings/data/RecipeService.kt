package edu.oregonstate.cs492.githubsearchwithsettings.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {
        @GET("api/json/v1/1/search.php")
        suspend fun searchRecipes(
            @Query("s") query: String,
        ): Response<RecipeSearchResults>

        companion object {
            private const val BASE_URL = "https://www.themealdb.com/"
            fun create(): RecipeService {
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                    .create(RecipeService::class.java)
            }
        }

}