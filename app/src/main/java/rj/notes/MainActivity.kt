package rj.notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import rj.notes.model.TodoItem
import rj.notes.viewmodel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    val viewModel: TodoViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var emptyStateView: View

    val uiScope = CoroutineScope(coroutineContext + SupervisorJob())

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)
            setupViews()
            setupRecyclerView()
            observeTodos()
            setupFab()
            setupTestErrorButton()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            ErrorUtils.showError(this, "主页面初始化失败", "MainActivity初始化时发生错误", e)
        }
    }

    private fun setupViews() {
        try {
            recyclerView = findViewById(R.id.recyclerViewTodos)
            fab = findViewById(R.id.fab)
            emptyStateView = findViewById(R.id.tvEmptyState)
            Log.d(TAG, "Views found successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error finding views: ${e.message}", e)
            ErrorUtils.showError(this, "视图初始化失败", "无法找到必要的UI组件", e)
        }
    }

    private fun setupRecyclerView() {
        try {
            todoAdapter = TodoAdapter { todoItem ->
                try {
                    // Handle todo item click - toggle completion
                    viewModel.delete(todoItem)
                } catch (e: Exception) {
                    Log.e(TAG, "Error deleting todo: ${e.message}", e)
                    ErrorUtils.showError(this, "删除失败", "删除笔记时发生错误", e)
                }
            }
            
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = todoAdapter
            }
            Log.d(TAG, "RecyclerView setup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up RecyclerView: ${e.message}", e)
            ErrorUtils.showError(this, "列表初始化失败", "RecyclerView设置时发生错误", e)
        }
    }

    private fun observeTodos() {
        uiScope.launch {
            try {
                val todos = viewModel.getTodos()
                todos.observe(this@MainActivity) { todoList ->
                    try {
                        todoAdapter.submitList(todoList)
                        updateEmptyState(todoList?.isEmpty() ?: true)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error updating todo list: ${e.message}", e)
                        ErrorUtils.showError(this@MainActivity, "列表更新失败", "更新笔记列表时发生错误", e)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error observing todos: ${e.message}", e)
                ErrorUtils.showError(this@MainActivity, "数据加载失败", "无法加载笔记列表", e)
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        try {
            if (isEmpty) {
                recyclerView.visibility = View.GONE
                emptyStateView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyStateView.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating empty state: ${e.message}", e)
            ErrorUtils.showError(this, "界面更新失败", "更新空状态显示时发生错误", e)
        }
    }

    private fun setupFab() {
        try {
            fab.setOnClickListener {
                try {
                    // 使用正常的AddTodoActivity
                    val intent = Intent(this, AddTodoActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Log.e(TAG, "Error starting AddTodoActivity: ${e.message}", e)
                    ErrorUtils.showError(this, "页面跳转失败", "无法打开添加笔记页面", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up FAB: ${e.message}", e)
            ErrorUtils.showError(this, "按钮设置失败", "设置浮动按钮时发生错误", e)
        }
    }

    private fun setupTestErrorButton() {
        try {
            // 长按FAB可以测试错误显示功能
            fab.setOnLongClickListener {
                try {
                    // 故意抛出一个异常来测试错误显示
                    throw RuntimeException("这是一个测试异常，用于测试错误显示功能")
                } catch (e: Exception) {
                    ErrorUtils.showError(this, "测试错误", "这是一个测试错误，用于验证错误显示功能是否正常工作", e)
                }
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up test error button: ${e.message}", e)
            ErrorUtils.showError(this, "测试按钮设置失败", "设置测试错误按钮时发生错误", e)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            // Refresh the list when returning from AddTodoActivity
            observeTodos()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onResume: ${e.message}", e)
            ErrorUtils.showError(this, "页面恢复失败", "页面恢复时发生错误", e)
        }
    }
}