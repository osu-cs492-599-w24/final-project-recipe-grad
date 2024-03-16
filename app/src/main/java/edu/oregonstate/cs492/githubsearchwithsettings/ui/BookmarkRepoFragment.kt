package edu.oregonstate.cs492.githubsearchwithsettings.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.githubsearchwithsettings.R
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeEntity

class BookmarkRepoFragment : Fragment() {
    private val viewModel: BookmarkRepoViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    //private lateinit var emptyView: TextView // TextView to show when there are no bookmarked repos
    private val adapter = RecipeEntityAdapter(::onRecipeEntityClick)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bookmark_repo, container, false)
//        recyclerView = view.findViewById(R.id.rv_bookmarked_repos)
//        emptyView = view.findViewById(R.id.tv_empty_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_the_bookmarked_repos)
        //emptyView = view.findViewById(R.id.tv_empty_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.allBookmarkRepo.observe(viewLifecycleOwner, Observer { bookmarkedRepos ->
            if (bookmarkedRepos.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                //emptyView.visibility = View.GONE
                adapter.updateRepoList(bookmarkedRepos)
            } else {
                recyclerView.visibility = View.GONE
                //emptyView.visibility = View.VISIBLE
            }
        })
        //recyclerView.visibility = View.VISIBLE
        val fakeRecipeEntity = RecipeEntity(
            recipeName = "Real Recipe",
            imageData = byteArrayOf(0x12, 0x34, 0x56)
        )
        viewModel.addBookmarkRepo(fakeRecipeEntity)

    }

    private fun onRecipeEntityClick(recipeEntity: RecipeEntity) {
        // Assuming you're using Navigation Component
        Log.d("Entityclicktest", "onRecipeEntityClick: ${recipeEntity.recipeName}")
        val action = BookmarkRepoFragmentDirections.navigateToRecipePage(recipeEntity.recipeName)
        findNavController().navigate(action)
    }

}
