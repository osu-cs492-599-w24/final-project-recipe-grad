package edu.oregonstate.cs492.githubsearchwithsettings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.githubsearchwithsettings.data.BookmarkRepoRepository
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeDatabase
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeEntity
import kotlinx.coroutines.launch

class BookmarkRepoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookmarkRepoRepository(
        RecipeDatabase.getInstance(application).recipeEntityDao()
    )

    fun addBookmarkRepo(repo: RecipeEntity) {
        viewModelScope.launch {
            repository.insertBookmarkRepo(repo)
        }
    }
    fun removeBookmarkRepo(repo: RecipeEntity) {
        viewModelScope.launch {
            repository.deleteBookmarkRepo(repo)
        }
    }

    fun getAllBookmarkRepo(){
        viewModelScope.launch {
            repository.getAllRecipe()
        }
    }

    fun getBookmarkRepoByName(name: String) = repository.getBookmarkRecipeByName(name)
}