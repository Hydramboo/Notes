package rj.notes.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import rj.notes.db.AppDatabase
import rj.notes.db.DB
import rj.notes.db.dbScope
import rj.notes.model.TodoItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Peter Sommerhoff
 */
class TodoViewModel(app: Application) : AndroidViewModel(app) {
  
  companion object {
    private const val TAG = "TodoViewModel"
  }
  
  private val dao by lazy { 
    try {
      AppDatabase.getDatabase(getApplication()).todoItemDao()
    } catch (e: Exception) {
      Log.e(TAG, "Error getting database: ${e.message}", e)
      throw e
    }
  }

  suspend fun getTodos(): LiveData<List<TodoItem>> = withContext(DB) {
    try {
      Log.d(TAG, "Getting todos from database")
      dao.loadAllTodos()
    } catch (e: Exception) {
      Log.e(TAG, "Error getting todos: ${e.message}", e)
      throw e
    }
  }

  fun add(todo: TodoItem) = dbScope.launch { 
    try {
      Log.d(TAG, "Adding todo: ${todo.title}")
      dao.insertTodo(todo)
      Log.d(TAG, "Todo added successfully")
    } catch (e: Exception) {
      Log.e(TAG, "Error adding todo: ${e.message}", e)
      throw e
    }
  }

  fun delete(todo: TodoItem) = dbScope.launch { 
    try {
      Log.d(TAG, "Deleting todo: ${todo.title}")
      dao.deleteTodo(todo)
      Log.d(TAG, "Todo deleted successfully")
    } catch (e: Exception) {
      Log.e(TAG, "Error deleting todo: ${e.message}", e)
      throw e
    }
  }
}