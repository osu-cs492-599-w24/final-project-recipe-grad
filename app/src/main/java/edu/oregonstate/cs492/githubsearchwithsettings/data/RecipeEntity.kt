package edu.oregonstate.cs492.githubsearchwithsettings.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecipeEntity")
data class RecipeEntity(
    @PrimaryKey val recipeName: String,
    val isClicked : Boolean,
    val imageData: ByteArray? // Image data stored as a ByteArray
)
