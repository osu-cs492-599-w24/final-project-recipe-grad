package edu.oregonstate.cs492.githubsearchwithsettings.data

class BookmarkRepoRepository(
    private val dao: RecipeRepoDao
) {
    suspend fun insertBookmarkRepo(recipe: RecipeEntity) = dao.insertRecipe(recipe)
    suspend fun deleteBookmarkRepo(recipe: RecipeEntity) = dao.deleteRecipe(recipe)

    fun getAllRecipe() = dao.getAllRecipe()

    fun getBookmarkRecipeByName(name: String) = dao.getRecipeByName(name)
}