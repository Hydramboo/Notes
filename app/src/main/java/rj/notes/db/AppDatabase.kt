package rj.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import rj.notes.model.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

/**
 * @author Peter Sommerhoff
 */
val DB = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

val dbScope = CoroutineScope(DB)

@Database(entities = [TodoItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

  companion object {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(ctx: Context): AppDatabase {
      if (INSTANCE == null) {
        INSTANCE = Room.databaseBuilder(ctx, AppDatabase::class.java, "AppDatabase")
            .addCallback(prepopulateCallback(ctx))
            .fallbackToDestructiveMigration() // 允许版本升级时重建数据库
            .build()
      }

      return INSTANCE!!
    }

    private fun prepopulateCallback(ctx: Context): Callback {
      return object : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
          super.onCreate(db)
          populateWithSampleData(ctx)
        }
      }
    }

    private fun populateWithSampleData(ctx: Context) {
      dbScope.launch {  // DB operations must be done on a background thread
        with(getDatabase(ctx).todoItemDao()) {
          insertTodo(TodoItem("创建笔记"))
          insertTodo(TodoItem("添加数据访问层"))
          insertTodo(TodoItem("继承RoomDatabase"))
        }
      }
    }
  }

  abstract fun todoItemDao(): TodoItemDao  // Triggers Room to provide an impl.
}