package edu.oregonstate.cs492.githubsearchwithsettings.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecipeEntity::class], version = 1)
abstract class RecipeDatabase : RoomDatabase(){
    abstract fun recipeEntityDao(): RecipeRepoDao

    companion object {
        @Volatile private var instance: RecipeDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                RecipeDatabase::class.java,
                "recipe-db"
            ).build()

        fun getInstance(context: Context) : RecipeDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}