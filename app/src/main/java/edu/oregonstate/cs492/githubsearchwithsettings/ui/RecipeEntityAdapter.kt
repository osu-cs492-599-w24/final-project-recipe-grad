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
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeEntity
//import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeRepo

class RecipeEntityAdapter (
    private val onRecipeEntityClick: (RecipeEntity) -> Unit
) : RecyclerView.Adapter<RecipeEntityAdapter.RecipeEntityViewHolder>() {
    private var recipeList = listOf<RecipeEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeEntityViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmark_list, parent, false)
        return RecipeEntityViewHolder(itemView, onRecipeEntityClick)
    }

    override fun onBindViewHolder(holder: RecipeEntityViewHolder, position: Int) {
        holder.bind(recipeList[position], position + 1) // Add 1 to position to start index from 1
    }

    override fun getItemCount() = recipeList.size

    fun updateRepoList(newRecipeList: List<RecipeEntity>?) {
        recipeList = newRecipeList ?: listOf()
        notifyDataSetChanged()
    }

    class RecipeEntityViewHolder(
        itemView: View,
        private val onClick: (RecipeEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val indexTextView: TextView = itemView.findViewById(R.id.tv_index)
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_bookmark_list)

        fun bind(recipeEntity: RecipeEntity, index: Int) {
            indexTextView.text = index.toString()
            nameTextView.text = recipeEntity.recipeName
            itemView.setOnClickListener {
                onClick(recipeEntity)
            }
        }
    }
}
