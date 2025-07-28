package com.petersommerhoff.kudoofinal.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.petersommerhoff.kudoofinal.model.TodoItem

/**
 * @author Peter Sommerhoff
 */
@Dao
interface TodoItemDao {

  @Query("SELECT * FROM todos")
  fun loadAllTodos(): LiveData<List<TodoItem>>  // Wraps return type in LiveData now

  @Insert(onConflict = IGNORE)
  fun insertTodo(todo: TodoItem)

  @Delete
  fun deleteTodo(todo: TodoItem)
}
