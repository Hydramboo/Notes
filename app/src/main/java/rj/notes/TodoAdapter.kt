package rj.notes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.hikage.annotation.HikageView
import com.highcapable.hikage.core.Hikage
import com.highcapable.hikage.core.base.Hikageable
import com.highcapable.hikage.core.builder.HikageBuilder
import com.highcapable.hikage.extension.widget.onClick
import com.highcapable.hikage.widget.android.widget.LinearLayout
import com.highcapable.hikage.widget.android.widget.TextView
import com.highcapable.hikage.widget.com.google.android.material.checkbox.MaterialCheckBox
import rj.notes.model.TodoItem

class TodoAdapter(
    private val onItemClick: (TodoItem) -> Unit
) : ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

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
            ErrorUtils.showError(parent.context, "列表项创建失败", "无法创建列表项视图", e)
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
            val todoItem = getItem(position)
            holder.bind(todoItem)
        } catch (e: Exception) {
            Log.e(TAG, "Error binding ViewHolder: ${e.message}", e)
            ErrorUtils.showError(holder.itemView.context, "列表项绑定失败", "无法绑定列表项数据", e)
        }
    }

    class TodoViewHolder(
        itemView: View,
        private val onItemClick: (TodoItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val checkBox: CheckBox = try {
            itemView.findViewById(R.id.checkBoxTodo)
        } catch (e: Exception) {
            Log.e(TAG, "Error finding checkBox: ${e.message}", e)
            ErrorUtils.showError(itemView.context, "视图查找失败", "无法找到复选框组件", e)
            CheckBox(itemView.context)
        }

        private val textView: TextView = try {
            itemView.findViewById(R.id.textViewTodo)
        } catch (e: Exception) {
            Log.e(TAG, "Error finding textView: ${e.message}", e)
            ErrorUtils.showError(itemView.context, "视图查找失败", "无法找到文本组件", e)
            TextView(itemView.context)
        }

        fun bind(todoItem: TodoItem) {
            try {
                textView.text = todoItem.title
                checkBox.isChecked = todoItem.isCompleted
                
                itemView.setOnClickListener {
                    try {
                        onItemClick(todoItem)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error handling item click: ${e.message}", e)
                        ErrorUtils.showError(itemView.context, "点击处理失败", "处理列表项点击时发生错误", e)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error binding todo item: ${e.message}", e)
                ErrorUtils.showError(itemView.context, "数据绑定失败", "绑定笔记数据时发生错误", e)
            }
        }
    }

    private class TodoDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem == newItem
        }
    }
}

// HikageCompat TodoItem UI Builder
object TodoItemUi : HikageBuilder {
    override fun build(): Hikage.Delegate<*> = Hikageable {
        LinearLayout(
            lparams = matchParent(),
            init = {
                vertical()
                setPadding(8.dp)
                setBackgroundResource(R.color.surface_color)
                setMargins(8.dp, 2.dp, 8.dp, 2.dp)
            }
        ) {
            MaterialCheckBox(
                id = "checkBoxTodo",
                lparams = wrapContent {
                    marginEnd = 8.dp
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }
            ) {
                scaleX = 0.8f
                scaleY = 0.8f
            }

            TextView(
                id = "textViewTodo",
                lparams = LayoutParams(0.dp, wrapContent) {
                    weight = 1f
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }
            ) {
                text = "示例笔记"
                setTextColor(resources.getColor(R.color.text_primary, null))
                textSize = 12.sp
                maxLines = 3
                ellipsize = android.text.TextUtils.TruncateAt.END
            }
        }
    }
}