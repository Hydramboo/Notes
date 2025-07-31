package rj.notes

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupRecyclerView()
        observeTodos()
        setupFab()
        setupTestErrorButton()
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerViewTodos)
        fab = findViewById(R.id.fab)
        emptyStateView = findViewById(R.id.tvEmptyState)
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter { todoItem ->
            // Handle todo item click - toggle completion
            viewModel.delete(todoItem)
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoAdapter
        }
    }

    private fun observeTodos() {
        uiScope.launch {
            val todos = viewModel.getTodos()
            todos.observe(this@MainActivity) { todoList ->
                todoAdapter.submitList(todoList)
                updateEmptyState(todoList?.isEmpty() ?: true)
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            recyclerView.visibility = View.GONE
            emptyStateView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyStateView.visibility = View.GONE
        }
    }

    private fun setupFab() {
        fab.setOnClickListener {
            // 使用测试版本的Activity来避免闪退
            val intent = Intent(this, AddTodoActivityTest::class.java)
            startActivity(intent)
        }
    }

    private fun setupTestErrorButton() {
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
    }

    override fun onResume() {
        super.onResume()
        // Refresh the list when returning from AddTodoActivity
        observeTodos()
    }
}