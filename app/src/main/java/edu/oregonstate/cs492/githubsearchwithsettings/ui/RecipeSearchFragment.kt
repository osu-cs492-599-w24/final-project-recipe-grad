package edu.oregonstate.cs492.githubsearchwithsettings.ui

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.githubsearchwithsettings.R
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeRepo
import edu.oregonstate.cs492.githubsearchwithsettings.util.LoadingStatus


class RecipeSearchFragment: Fragment(R.layout.fragment_recipe_search) {
    private val viewModel: RecipeSearchVIewModel by viewModels()

    private val recipeViewModel: BookmarkRepoViewModel by viewModels()

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private val adapter = RecipeRepoListAdapter(::onRecipeRepoClick)
    private lateinit var viewPopWindow: View



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPopWindow = view
        val searchBoxET: EditText = view.findViewById(R.id.et_search_box)
        val searchBtn: Button = view.findViewById(R.id.btn_search)

        searchErrorTV = view.findViewById(R.id.tv_search_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        searchResultsListRV = view.findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = GridLayoutManager(context, 2)

        searchResultsListRV.setHasFixedSize(true)


        searchResultsListRV.adapter = adapter

        viewModel.searchResults.observe(viewLifecycleOwner) {
                searchResults -> adapter.updateRepoList(searchResults)
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) {
                loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    searchResultsListRV.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    searchResultsListRV.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    searchResultsListRV.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
                error -> searchErrorTV.text = getString(
            R.string.search_error,
            error
        )
        }

//        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        searchBtn.setOnClickListener {
            val query = searchBoxET.text.toString()
            viewModel.loadSearchResults(query)
            searchResultsListRV.scrollToPosition(0)

        }
    }

    private fun onRecipeRepoClick(repo: RecipeRepo) {
//        val directions = GitHubSearchFragmentDirections.navigateToRepoDetail(repo)
//        findNavController().navigate(directions)
        Log.d("clicktest", "onRecipeRepoClick: ${repo.name}")
        showPopup(repo)
    }
    private fun showPopup(repo: RecipeRepo) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.pop_window, null)


        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // calculate the width
        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidthPx = displayMetrics.widthPixels
        val width = (screenWidthPx * 0.8).toInt() // Calculate 80% of screen width
        popupWindow.width = width
        //


        val popFoodName : TextView = popupView.findViewById(R.id.foodName)
        val popingredient : TextView = popupView.findViewById(R.id.ingredient)
        val img : ImageView = popupView.findViewById(R.id.img)
        popFoodName.text = repo.name
        Glide.with(viewPopWindow.context)
            .load(repo.url)
            .into(img)


        // Set an elevation value for popup window (optional)
        popupWindow.elevation = 10.0F

        // If you need the popup to dismiss when touched outside
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // Use the view to show the popup window
        popupWindow.showAtLocation(viewPopWindow, Gravity.CENTER, 0, 0)

        // Dismiss the popup window when touched
        val closeButton: Button = popupView.findViewById(R.id.close_button)
        closeButton.setOnClickListener { popupWindow.dismiss() }
    }
}