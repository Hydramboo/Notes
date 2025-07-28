package com.petersommerhoff.kudoofinal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Peter Sommerhoff
 */
@Entity(tableName = "todos")
data class TodoItem(val title: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0  // 0 is considered not set by Room
}
