package edu.oregonstate.cs492.githubsearchwithsettings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeRepo
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeRepoRepository
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeService
import kotlinx.coroutines.launch

/**
 * This is a ViewModel class that holds current weather data for the UI.
 */
class RecipePageViewModel: ViewModel() {
    private val repository = RecipeRepoRepository(RecipeService.create())


    private val _recipe = MutableLiveData<RecipeRepo>(null)

    val recipe: LiveData<RecipeRepo> = _recipe

    private val _error = MutableLiveData<Throwable?>(null)


    val error: LiveData<Throwable?> = _error


    private val _loading = MutableLiveData<Boolean>(false)

    val loading: LiveData<Boolean> = _loading


    fun loadRecipeDetail(query : String) {

        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadRecipesSearch(query)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _recipe.value = result.getOrNull()?.firstOrNull()
        }
    }
}