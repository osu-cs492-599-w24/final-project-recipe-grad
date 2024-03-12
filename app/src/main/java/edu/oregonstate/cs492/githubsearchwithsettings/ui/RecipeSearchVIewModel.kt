package edu.oregonstate.cs492.githubsearchwithsettings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import edu.oregonstate.cs492.githubsearchwithsettings.data.GitHubRepo
import edu.oregonstate.cs492.githubsearchwithsettings.data.GitHubReposRepository
import edu.oregonstate.cs492.githubsearchwithsettings.data.GitHubService
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeRepo
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeRepoRepository
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeService
import edu.oregonstate.cs492.githubsearchwithsettings.util.LoadingStatus
import kotlinx.coroutines.launch

class RecipeSearchVIewModel: ViewModel() {
    private val repository = RecipeRepoRepository(RecipeService.create())
    private val _searchResults = MutableLiveData<List<RecipeRepo>?>(null)
    val searchResults: LiveData<List<RecipeRepo>?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadSearchResults(query: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadRecipesSearch(query)
            _searchResults.value = result.getOrNull()
            _error.value = result.exceptionOrNull()?.message
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}