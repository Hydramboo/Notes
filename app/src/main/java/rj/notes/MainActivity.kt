package rj.notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.hikage.core.Hikage
import com.highcapable.hikage.core.base.Hikageable
import com.highcapable.hikage.core.builder.HikageBuilder
import com.highcapable.hikage.core.runtime.collectAsHikageState
import com.highcapable.hikage.core.runtime.mutableStateOf
import com.highcapable.hikage.extension.lifecycleOwner
import com.highcapable.hikage.extension.setContentView
import com.highcapable.hikage.extension.widget.onClick
import com.highcapable.hikage.extension.widget.onLongClick
import com.highcapable.hikage.extension.widget.vertical
import com.highcapable.hikage.widget.android.widget.LinearLayout
import com.highcapable.hikage.widget.android.widget.TextView
import com.highcapable.hikage.widget.androidx.constraintlayout.widget.ConstraintLayout
import com.highcapable.hikage.widget.androidx.recyclerview.widget.RecyclerView
import com.highcapable.hikage.widget.com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import rj.notes.model.TodoItem
import rj.notes.viewmodel.TodoViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel: TodoViewModel by viewModels()
    private val todosState = mutableStateOf<List<TodoItem>>(emptyList())
    private val isEmptyState = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView {
                val todos = todosState.collectAsHikageState(lifecycleOwner)
                val isEmpty = isEmptyState.collectAsHikageState(lifecycleOwner)
                
                ConstraintLayout(lparams = matchParent()) {
                    // App Name in top left
                    TextView(
                        id = "tvAppName",
                        lparams = wrapContent {
                            marginStart = 8.dp
                            marginTop = 8.dp
                            constraintStartToStartOf = parentId
                            constraintTopToTopOf = parentId
                        }
                    ) {
                        text = getString(R.string.app_name)
                        setTextColor(resources.getColor(R.color.white, null))
                        textSize = 14.sp
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }

                    // Add Button in top right
                    MaterialButton(
                        id = "btnAdd",
                        lparams = LayoutParams(32.dp, 32.dp) {
                            marginEnd = 8.dp
                            marginTop = 8.dp
                            constraintEndToEndOf = parentId
                            constraintTopToTopOf = parentId
                        }
                    ) {
                        text = "+"
                        setTextColor(resources.getColor(R.color.white, null))
                        textSize = 16.sp
                        setTypeface(null, android.graphics.Typeface.BOLD)
                        setBackgroundResource(R.drawable.add_button_background)
                        onClick {
                            try {
                                val intent = Intent(this@MainActivity, AddTodoActivity::class.java)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Log.e(TAG, "Error starting AddTodoActivity: ${e.message}", e)
                                ErrorUtils.showError(this@MainActivity, "页面跳转失败", "无法打开添加笔记页面", e)
                            }
                        }
                        onLongClick {
                            try {
                                throw RuntimeException("测试错误：长按添加按钮")
                            } catch (e: Exception) {
                                Log.e(TAG, "Test error triggered: ${e.message}", e)
                                ErrorUtils.showError(this@MainActivity, "测试错误", "这是一个测试错误", e)
                            }
                            true
                        }
                    }

                    // Content area
                    ConstraintLayout(
                        lparams = LayoutParams(0.dp, 0.dp) {
                            marginTop = 8.dp
                            constraintBottomToBottomOf = parentId
                            constraintEndToEndOf = parentId
                            constraintStartToStartOf = parentId
                            constraintTopToBottomOf = "tvAppName"
                        }
                    ) {
                        RecyclerView(
                            id = "recyclerViewTodos",
                            lparams = matchParent {
                                paddingTop = 4.dp
                                paddingBottom = 4.dp
                                constraintBottomToBottomOf = parentId
                                constraintEndToEndOf = parentId
                                constraintStartToStartOf = parentId
                                constraintTopToTopOf = parentId
                            }
                        ) {
                            // RecyclerView setup will be done in init block
                        }

                        TextView(
                            id = "tvEmptyState",
                            lparams = wrapContent {
                                constraintBottomToBottomOf = parentId
                                constraintEndToEndOf = parentId
                                constraintStartToStartOf = parentId
                                constraintTopToTopOf = parentId
                            }
                        ) {
                            text = getString(R.string.no_todos_yet)
                            setTextColor(resources.getColor(R.color.white, null))
                            textSize = 12.sp
                            gravity = android.view.Gravity.CENTER
                            visibility = if (isEmpty.value) android.view.View.VISIBLE else android.view.View.GONE
                        }
                    }
                }
            }

            setupRecyclerView()
            observeTodos()
            
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            ErrorUtils.showError(this, "初始化失败", "MainActivity初始化时发生错误", e)
        }
    }

    private fun setupRecyclerView() {
        try {
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTodos)
            recyclerView.layoutManager = LinearLayoutManager(this)
            
            val adapter = TodoAdapter { todoItem ->
                try {
                    // Handle item click
                    Log.d(TAG, "Clicked on todo: ${todoItem.title}")
                } catch (e: Exception) {
                    Log.e(TAG, "Error handling item click: ${e.message}", e)
                    ErrorUtils.showError(this, "操作失败", "处理点击事件时发生错误", e)
                }
            }
            
            recyclerView.adapter = adapter
            Log.d(TAG, "RecyclerView setup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up RecyclerView: ${e.message}", e)
            ErrorUtils.showError(this, "列表设置失败", "设置笔记列表时发生错误", e)
        }
    }

    private fun observeTodos() {
        try {
            lifecycleScope.launch {
                try {
                    val todos = viewModel.getTodos()
                    todos.observe(this@MainActivity) { todoList ->
                        try {
                            todosState.value = todoList ?: emptyList()
                            updateEmptyState(todoList?.isEmpty() ?: true)
                            Log.d(TAG, "Todos updated: ${todoList?.size ?: 0} items")
                        } catch (e: Exception) {
                            Log.e(TAG, "Error updating todo list: ${e.message}", e)
                            ErrorUtils.showError(this@MainActivity, "列表更新失败", "更新笔记列表时发生错误", e)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error getting todos: ${e.message}", e)
                    ErrorUtils.showError(this@MainActivity, "数据获取失败", "获取笔记数据时发生错误", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error observing todos: ${e.message}", e)
            ErrorUtils.showError(this, "数据监听失败", "监听笔记数据时发生错误", e)
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        try {
            isEmptyState.value = isEmpty
            val emptyStateView = findViewById<TextView>(R.id.tvEmptyState)
            emptyStateView.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.View.GONE
        } catch (e: Exception) {
            Log.e(TAG, "Error updating empty state: ${e.message}", e)
            ErrorUtils.showError(this, "状态更新失败", "更新空状态时发生错误", e)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            // Refresh data when returning to the activity
            observeTodos()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onResume: ${e.message}", e)
            ErrorUtils.showError(this, "页面恢复失败", "页面恢复时发生错误", e)
        }
    }
}