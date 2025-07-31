package rj.notes

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import rj.notes.model.TodoItem
import rj.notes.viewmodel.TodoViewModel

class AddTodoActivityTest : AppCompatActivity() {

    private val viewModel: TodoViewModel by viewModels()
    private lateinit var editTextTodo: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonCancel: Button

    companion object {
        private const val TAG = "AddTodoActivityTest"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_add_todo_simple)
            Log.d(TAG, "Simple layout loaded successfully")
            
            setupViews()
            setupClickListeners()
            
            // Set focus to edit text for better UX
            editTextTodo.requestFocus()
            Log.d(TAG, "Test activity setup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            ErrorUtils.showError(this, "测试页面初始化失败", "AddTodoActivityTest初始化时发生错误", e)
        }
    }

    private fun setupViews() {
        try {
            editTextTodo = findViewById(R.id.etNewTodo)
            buttonAdd = findViewById(R.id.btnAddTodo)
            buttonCancel = findViewById(R.id.btnCancel)
            Log.d(TAG, "Simple views found successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error finding views: ${e.message}", e)
            ErrorUtils.showError(this, "视图初始化失败", "无法找到必要的UI组件", e)
        }
    }

    private fun setupClickListeners() {
        buttonAdd.setOnClickListener {
            try {
                addTodo()
            } catch (e: Exception) {
                Log.e(TAG, "Error in addTodo: ${e.message}", e)
                ErrorUtils.showError(this, "添加笔记失败", "添加笔记时发生错误", e)
            }
        }

        buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun addTodo() {
        val todoTitle = editTextTodo.text.toString().trim()
        
        if (todoTitle.isEmpty()) {
            Toast.makeText(this, "请输入笔记内容", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val todoItem = TodoItem(todoTitle)
            viewModel.add(todoItem)
            
            Toast.makeText(this, "笔记添加成功", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Todo added successfully: $todoTitle")
            finish()
        } catch (e: Exception) {
            Log.e(TAG, "Error adding todo: ${e.message}", e)
            ErrorUtils.showError(this, "数据库操作失败", "保存笔记到数据库时发生错误", e)
        }
    }
}