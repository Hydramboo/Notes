package rj.notes.viewmodel

import android.app.Application
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
  private val dao by lazy { AppDatabase.getDatabase(getApplication()).todoItemDao() }

  suspend fun getTodos(): LiveData<List<TodoItem>> = withContext(DB) {
    dao.loadAllTodos()
  }

  fun add(todo: TodoItem) = dbScope.launch { dao.insertTodo(todo) }

  fun delete(todo: TodoItem) = dbScope.launch { dao.deleteTodo(todo) }
}