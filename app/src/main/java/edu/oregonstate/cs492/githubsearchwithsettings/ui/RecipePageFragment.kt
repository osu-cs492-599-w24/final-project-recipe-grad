package edu.oregonstate.cs492.githubsearchwithsettings.ui


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import edu.oregonstate.cs492.githubsearchwithsettings.R
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeEntity
import edu.oregonstate.cs492.githubsearchwithsettings.data.RecipeRepo
import java.io.ByteArrayOutputStream

/**
 * This fragment represents the "current weather" screen.
 */
class RecipePageFragment : Fragment(R.layout.fragment_recipe_page) {
    private val viewModel: RecipePageViewModel by viewModels()
    private val recipeViewModel : BookmarkRepoViewModel by viewModels()

    private val REQUEST_IMAGE_CAPTURE = 1

    private var bitmap : Bitmap? = null
    private lateinit var webView: WebView

    private lateinit var prefs: SharedPreferences

    private lateinit var recipeInfoView: View
    private lateinit var loadingErrorTV: TextView

    private lateinit var cuisineTV: TextView
    private lateinit var instructionTV: TextView
    private lateinit var tagsTV: TextView
    private lateinit var ingredientsTV : TextView
    private lateinit var videoTitleTV : TextView
    private lateinit var photoTV : TextView

    private lateinit var starImageView: ImageView
    private var isStarFilled: Boolean = false

    private lateinit var openCameraBtn: ImageView

    private lateinit var photoImageView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeInfoView = view.findViewById(R.id.recipe_detail)
        loadingErrorTV = view.findViewById(R.id.tv_load_error)

        cuisineTV = recipeInfoView.findViewById(R.id.tv_cuisine)
        instructionTV = recipeInfoView.findViewById(R.id.tv_instruction)
        tagsTV = recipeInfoView.findViewById(R.id.tv_tags)
        ingredientsTV = recipeInfoView.findViewById(R.id.tv_ingredients)
        videoTitleTV = recipeInfoView.findViewById(R.id.tv_video_title)
        photoTV = recipeInfoView.findViewById(R.id.tv_photo_title)

        openCameraBtn = view.findViewById(R.id.openCameraButton)
        photoImageView = view.findViewById(R.id.photoImageView)

        starImageView = view.findViewById(R.id.star_favorite)

        webView = view.findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()



        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())


        val recipeName = arguments?.getString("recipeNameFavorite") ?: arguments?.getString("recipeNameSearch")

        recipeName?.let { name ->
            recipeViewModel.getIsClickedStatus(name).observe(viewLifecycleOwner) { isClicked ->
                if (isClicked != null) {
                    if (isClicked) {
                        starImageView.setImageResource(R.drawable.star_full)
                        isStarFilled = true
                        recipeViewModel.getImageData(name)?.observe(viewLifecycleOwner) { imageData ->
                            imageData?.let {
                                bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                                photoImageView.setImageBitmap(bitmap)
                                photoImageView.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        starImageView.setImageResource(R.drawable.star)
                        isStarFilled = false
                        photoImageView.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                bind(recipe)
                recipeInfoView.visibility = View.VISIBLE
                photoTV.visibility = if (photoImageView.visibility == View.VISIBLE) View.VISIBLE else View.GONE

                val videoUrl = recipe.youtubeUrl
                val videoId = extractVideoIdFromUrl(videoUrl)
                webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        url?.let {
                            // Check URL is a YouTube video link or not
                            if (it.startsWith("https://www.youtube.com") || it.startsWith("https://m.youtube.com") || it.startsWith("https://youtube.com") || it.contains("youtu.be")) {
                                // Open the YouTube link in the YouTube app
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                startActivity(intent)
                                return true // Indicates you've handled the URL
                            }
                        }
                        return false // Indicates the WebView should handle the URL
                    }
                }

                if (videoId != null) {
                    val embedUrl = "https://www.youtube.com/embed/${videoId}"
//                    webView.loadUrl(embedUrl)
                    val htmlContent = """
                    <html>
                    <body>
                    <iframe width="100%" height="100%" src="$embedUrl" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
                    </body>
                    </html>
                     """.trimIndent()

                    webView.loadData(htmlContent, "text/html", "UTF-8")

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

        openCameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
            photoImageView.visibility = View.VISIBLE
        }

        openCameraBtn.setOnClickListener {
            dispatchTakePictureIntent()
        }

        starImageView.setOnClickListener {
            isStarFilled = !isStarFilled
            val recipeNameFavorite = arguments?.getString("recipeNameFavorite")
            val recipeNameSearch = arguments?.getString("recipeNameSearch")
            var getName : String = ""
            if (recipeNameFavorite != null) {
                getName = recipeNameFavorite
                viewModel.loadRecipeDetail(getName)

            }else if (recipeNameSearch != null) {
                getName = recipeNameSearch
                viewModel.loadRecipeDetail(getName)
            }
            else {
                Log.d("RecipePageFragment", "Don't have value!")
            }
            if (isStarFilled) {
                starImageView.setImageResource(R.drawable.star_full)
                var saveRecipeEntity = RecipeEntity(getName, true, null)
                val tempBitmap = bitmap
                if(bitmap != null){
                    val stream = ByteArrayOutputStream()
                    tempBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    photoImageView.setImageBitmap(tempBitmap)
                    photoTV.visibility = if (photoImageView.visibility == View.VISIBLE) View.VISIBLE else View.GONE
                    saveRecipeEntity = RecipeEntity(getName, true, byteArray)
                    photoImageView.visibility = View.VISIBLE
                }
                recipeViewModel.addBookmarkRepo(saveRecipeEntity)
                openCameraBtn.visibility = View.VISIBLE


            } else {
                val saveRecipeEntity = RecipeEntity(getName, false,null)
                starImageView.setImageResource(R.drawable.star)
                recipeViewModel.removeBookmarkRepo(saveRecipeEntity)
                openCameraBtn.visibility = View.GONE
                photoImageView.visibility = View.GONE
                if(bitmap == null){
                    bitmap = null
                    photoImageView.setImageBitmap(bitmap)
                    photoTV.visibility = if (photoImageView.visibility == View.VISIBLE) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val recipeNameFavorite = arguments?.getString("recipeNameFavorite")
        val recipeNameSearch = arguments?.getString("recipeNameSearch")
        if (recipeNameFavorite != null) {
            viewModel.loadRecipeDetail(recipeNameFavorite)
        }else if (recipeNameSearch != null) {
            viewModel.loadRecipeDetail(recipeNameSearch)
            openCameraBtn.visibility = View.VISIBLE
        }
        else {
            Log.d("RecipePageFragment", "Don't have value!")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageData = convertBitmapToByteArray(imageBitmap)
            photoImageView.setImageBitmap(imageBitmap)

            val recipeNameFavorite = arguments?.getString("recipeNameFavorite")
            val recipeNameSearch = arguments?.getString("recipeNameSearch")



            if (recipeNameFavorite != null) {
                viewModel.loadRecipeDetail(recipeNameFavorite)
                val saveRecipeEntity = RecipeEntity(recipeNameFavorite, true, imageData)
                recipeViewModel.addBookmarkRepo(saveRecipeEntity)
            }else if (recipeNameSearch != null) {
                viewModel.loadRecipeDetail(recipeNameSearch)
                val saveRecipeEntity = RecipeEntity(recipeNameSearch, true, imageData)
                recipeViewModel.addBookmarkRepo(saveRecipeEntity)
            }
            else {
                Log.d("RecipePageFragment", "Don't have value!")
            }
        }
    }

    private fun bind(recipe: RecipeRepo) {
        cuisineTV.text = "Cuisine: " + recipe.name + "\n"

        val removeNum = recipe.instruction?.replace(Regex("\\d+\\."), "")
        val replaceNewLine = removeNum?.replace("\n", "") ?: ""
        val result = replaceNewLine.split(".").mapIndexedNotNull { index, sentence ->
            val trimmedSentence = sentence.trim()
            if (trimmedSentence.isNotBlank()) {
                "${index + 1}.$trimmedSentence."
            } else {
                null
            }
        }.joinToString("\n")

        instructionTV.text = "Instruction:\n$result"

        if (recipe.tags == null) {
            tagsTV.visibility = View.GONE
        } else {
            tagsTV.text = "Tag: ${recipe.tags} \n"
        }

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

        val measures = mutableListOf<String?>()
        measures.add(recipe.measure1)
        measures.add(recipe.measure2)
        measures.add(recipe.measure3)
        measures.add(recipe.measure4)
        measures.add(recipe.measure5)
        measures.add(recipe.measure6)
        measures.add(recipe.measure7)
        measures.add(recipe.measure8)
        measures.add(recipe.measure9)
        measures.add(recipe.measure10)
        measures.add(recipe.measure11)
        measures.add(recipe.measure12)
        measures.add(recipe.measure13)
        measures.add(recipe.measure14)
        measures.add(recipe.measure15)
        measures.add(recipe.measure16)
        measures.add(recipe.measure17)
        measures.add(recipe.measure18)
        measures.add(recipe.measure19)
        measures.add(recipe.measure20)

        val ingredientsString = ingredients
            .mapIndexedNotNull { index, ingredient ->
                val measure = measures.getOrNull(index)
                if (ingredient != null && ingredient.isNotBlank()) {
                    val ingredientWithMeasure = if (!measure.isNullOrBlank()) {
                        "$ingredient : $measure"
                    } else {
                        ingredient
                    }
                    "${index + 1}. $ingredientWithMeasure"
                } else {
                    null
                }
            }
            .joinToString("\n")

        ingredientsTV.text = "Ingredients:\n$ingredientsString \n"


        videoTitleTV.text = "\nVideo tutorial:\n"
    }

    fun extractVideoIdFromUrl(youtubeUrl: String): String? {
        val regex = Regex("""(?:youtube\.com.*(?:\?|\&)v=|youtu\.be/)([^"&?/\s]{11})""")
        val matchResult = regex.find(youtubeUrl)
        return matchResult?.groups?.get(1)?.value
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}