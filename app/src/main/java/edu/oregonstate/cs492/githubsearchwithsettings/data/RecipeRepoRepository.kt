package edu.oregonstate.cs492.githubsearchwithsettings.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepoRepository (
    private val service: RecipeService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadRecipesSearch(query: String): Result<List<RecipeRepo>> =
        withContext(ioDispatcher) {
            try {
                val response = service.searchRecipes(query)
                if (response.isSuccessful) {
                    Result.success(response.body()?.meals ?: listOf())
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}