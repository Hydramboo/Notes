package rj.notes

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TodoViewHolder(
        itemView: View,
        private val onItemClick: (TodoItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val checkBox: CheckBox = itemView.findViewById(R.id.cbTodoDone)
        private val titleText: TextView = itemView.findViewById(R.id.tvTodoTitle)
        private var currentTodo: TodoItem? = null

        init {
            itemView.setOnClickListener {
                currentTodo?.let { onItemClick(it) }
            }
        }

        fun bind(todo: TodoItem) {
            currentTodo = todo
            titleText.text = todo.title
            checkBox.isChecked = false // For now, we don't have completion status
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