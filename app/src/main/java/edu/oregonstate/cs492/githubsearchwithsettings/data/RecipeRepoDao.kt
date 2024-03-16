package edu.oregonstate.cs492.githubsearchwithsettings.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.LiveData
@Dao
interface RecipeRepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM RecipeEntity ORDER BY recipeName")
    fun getAllRecipe(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM RecipeEntity WHERE recipeName = :name LIMIT 1")
    fun getRecipeByName(name: String): Flow<RecipeEntity?>
}