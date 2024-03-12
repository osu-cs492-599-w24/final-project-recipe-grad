package edu.oregonstate.cs492.githubsearchwithsettings.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.githubsearchwithsettings.R
import edu.oregonstate.cs492.githubsearchwithsettings.data.GitHubRepo
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeRepo

class RecipeRepoListAdapter ()
    : RecyclerView.Adapter<RecipeRepoListAdapter.RecipeRepoViewHolder>() {
    private var RecipeRepoList = listOf<RecipeRepo>()

    fun updateRepoList(newRepoList: List<RecipeRepo>?) {
        notifyItemRangeRemoved(0, RecipeRepoList.size)
        RecipeRepoList = newRepoList ?: listOf()
        RecipeRepoList.forEach{
            Log.d("test","name: ${it.name}")
            Log.d("test","url: ${it.url}")
        }


        notifyItemRangeInserted(0, RecipeRepoList.size)
    }

    override fun getItemCount() = RecipeRepoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeRepoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_repo_list_item, parent, false)
        return RecipeRepoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeRepoViewHolder, position: Int) {
        holder.bind(RecipeRepoList[position])
    }

    class RecipeRepoViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.dishDescription)
        private val img : ImageView =itemView.findViewById(R.id.dishImage)
        private var currentRecipeRepo: RecipeRepo? = null

        fun bind(reciperepo: RecipeRepo) {
            currentRecipeRepo = reciperepo
            nameTV.text = reciperepo.name
            Glide.with(itemView.context)
                .load(reciperepo.url)
                .into(img)


        }
    }
}