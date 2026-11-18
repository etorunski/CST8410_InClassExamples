package com.example.cst8410_inclassexamples

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    var name: String,
    var sel: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
