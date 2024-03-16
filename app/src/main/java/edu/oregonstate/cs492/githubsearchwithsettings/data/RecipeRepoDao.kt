package edu.oregonstate.cs492.githubsearchwithsettings.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.lifecycle.LiveData
@Dao
interface RecipeRepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM RecipeEntity ORDER BY recipeName")
    fun getAllRecipe(): LiveData<List<RecipeEntity>>

    @Query("SELECT isClicked FROM RecipeEntity WHERE recipeName = :recipeName")
    fun getIsClickedStatus(recipeName: String): LiveData<Boolean>

    @Query("SELECT imageData FROM RecipeEntity WHERE recipeName = :recipeName")
    fun getImageData(recipeName: String): LiveData<ByteArray?>
}