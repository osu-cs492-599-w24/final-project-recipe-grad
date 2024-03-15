package edu.oregonstate.cs492.githubsearchwithsettings.ui


import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import edu.oregonstate.cs492.githubsearchwithsettings.R
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeRepo

/**
 * This fragment represents the "current weather" screen.
 */
class RecipePageFragment : Fragment(R.layout.fragment_recipe_page) {
    private val viewModel: RecipePageViewModel by viewModels()

    private lateinit var webView: WebView

    private lateinit var prefs: SharedPreferences

    private lateinit var recipeInfoView: View
    private lateinit var loadingErrorTV: TextView

    private lateinit var cuisineTV: TextView
    private lateinit var instructionTV: TextView
    private lateinit var tagsTV: TextView
    private lateinit var ingredientsTV : TextView
    private lateinit var videoTitleTV : TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        recipeInfoView = view.findViewById(R.id.recipe_detail)
        loadingErrorTV = view.findViewById(R.id.tv_load_error)

        cuisineTV = recipeInfoView.findViewById(R.id.tv_cuisine)
        instructionTV = recipeInfoView.findViewById(R.id.tv_instruction)
        tagsTV = recipeInfoView.findViewById(R.id.tv_tags)
        ingredientsTV = recipeInfoView.findViewById(R.id.tv_ingredients)
        videoTitleTV = recipeInfoView.findViewById(R.id.tv_video_title)

        webView = view.findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()


        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())


        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                bind(recipe)
                recipeInfoView.visibility = View.VISIBLE
            }

            if (recipe != null) {
                val videoUrl = recipe.youtubeUrl
                val videoId = extractVideoIdFromUrl(videoUrl)

                if (videoId != null) {
                    val embedUrl = "https://www.youtube.com/embed/$videoId"
                    webView.loadUrl(embedUrl)
                }
            }
        }


        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                Log.e(tag, "Error fetching forecast: ${error.message}")
                error.printStackTrace()
            }
        }


        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingErrorTV.visibility = View.INVISIBLE
                recipeInfoView.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadRecipeDetail("Arrabiata")
    }

    private fun bind(recipe: RecipeRepo) {
        cuisineTV.text = "Cuisine: " + recipe.name + "\n"

        val instruction = recipe.instruction?.replace("\n", "") ?: ""
        val result = instruction.split(".").mapIndexedNotNull { index, sentence ->
            val trimmedSentence = sentence.trim()
            if (trimmedSentence.isNotBlank()) {
                "${index + 1}.$trimmedSentence."
            } else {
                null
            }
        }.joinToString("\n")

        instructionTV.text = "Instruction:\n$result"

        tagsTV.text = "Tag: ${recipe.tags ?: getString(R.string.no_tags_available)} \n"

        val ingredients = mutableListOf<String?>()
        ingredients.add(recipe.ingredient1)
        ingredients.add(recipe.ingredient2)
        ingredients.add(recipe.ingredient3)
        ingredients.add(recipe.ingredient4)
        ingredients.add(recipe.ingredient5)
        ingredients.add(recipe.ingredient6)
        ingredients.add(recipe.ingredient7)
        ingredients.add(recipe.ingredient8)
        ingredients.add(recipe.ingredient9)
        ingredients.add(recipe.ingredient10)
        ingredients.add(recipe.ingredient11)
        ingredients.add(recipe.ingredient12)
        ingredients.add(recipe.ingredient13)
        ingredients.add(recipe.ingredient14)
        ingredients.add(recipe.ingredient15)
        ingredients.add(recipe.ingredient16)
        ingredients.add(recipe.ingredient17)
        ingredients.add(recipe.ingredient18)
        ingredients.add(recipe.ingredient19)
        ingredients.add(recipe.ingredient20)

        val ingredientsString = ingredients
            .filterNotNull()
            .filter { it.isNotBlank() }
            .joinToString("\n") { it.trim() }

        ingredientsTV.text = "Ingredients:\n$ingredientsString \n"


        videoTitleTV.text = "\nVideo tutorial:"
    }

    fun extractVideoIdFromUrl(youtubeUrl: String): String? {
        val regex = Regex("""(?:youtube\.com.*(?:\?|\&)v=|youtu\.be/)([^"&?/\s]{11})""")
        val matchResult = regex.find(youtubeUrl)
        return matchResult?.groups?.get(1)?.value
    }
}