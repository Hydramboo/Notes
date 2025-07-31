package rj.notes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import rj.notes.model.TodoItem

class TodoAdapter(private val onItemClick: (TodoItem) -> Unit) : 
    ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    companion object {
        private const val TAG = "TodoAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return try {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_item_simple, parent, false)
            TodoViewHolder(view, onItemClick)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating ViewHolder: ${e.message}", e)
            // 显示错误页面
            val context = parent.context
            ErrorUtils.showError(context, "列表项创建失败", "无法创建列表项视图", e)
            // 创建一个简单的备用视图
            val fallbackView = TextView(parent.context).apply {
                text = "加载失败"
                setPadding(16, 16, 16, 16)
            }
            TodoViewHolder(fallbackView, onItemClick)
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        try {
            holder.bind(getItem(position))
        } catch (e: Exception) {
            Log.e(TAG, "Error binding ViewHolder at position $position: ${e.message}", e)
            // 显示错误页面
            val context = holder.itemView.context
            ErrorUtils.showError(context, "列表项绑定失败", "无法绑定列表项数据", e)
        }
    }

    class TodoViewHolder(
        itemView: View,
        private val onItemClick: (TodoItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val checkBox: CheckBox? = try {
            itemView.findViewById(R.id.cbTodoDone)
        } catch (e: Exception) {
            Log.e(TAG, "Error finding CheckBox: ${e.message}", e)
            // 显示错误页面
            val context = itemView.context
            ErrorUtils.showError(context, "组件查找失败", "无法找到CheckBox组件", e)
            null
        }
        
        private val titleText: TextView? = try {
            itemView.findViewById(R.id.tvTodoTitle)
        } catch (e: Exception) {
            Log.e(TAG, "Error finding TextView: ${e.message}", e)
            // 显示错误页面
            val context = itemView.context
            ErrorUtils.showError(context, "组件查找失败", "无法找到TextView组件", e)
            null
        }
        
        private var currentTodo: TodoItem? = null

        init {
            itemView.setOnClickListener {
                currentTodo?.let { onItemClick(it) }
            }
        }

        fun bind(todo: TodoItem) {
            try {
                currentTodo = todo
                titleText?.text = todo.title
                checkBox?.isChecked = false // For now, we don't have completion status
            } catch (e: Exception) {
                Log.e(TAG, "Error binding todo: ${e.message}", e)
                // 显示错误页面
                val context = itemView.context
                ErrorUtils.showError(context, "数据绑定失败", "无法绑定笔记数据", e)
            }
        }
    }

    private class TodoDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.title == newItem.title
        }
    }
}