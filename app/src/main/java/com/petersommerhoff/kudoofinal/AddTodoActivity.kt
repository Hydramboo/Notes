package com.petersommerhoff.kudoofinal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.petersommerhoff.kudoofinal.model.TodoItem
import com.petersommerhoff.kudoofinal.viewmodel.TodoViewModel

class AddTodoActivity : AppCompatActivity() {

    private val viewModel: TodoViewModel by viewModels()
    private lateinit var editTextTodo: TextInputEditText
    private lateinit var buttonAdd: MaterialButton
    private lateinit var buttonCancel: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        editTextTodo = findViewById(R.id.etNewTodo)
        buttonAdd = findViewById(R.id.btnAddTodo)
        buttonCancel = findViewById(R.id.btnCancel)

        buttonAdd.setOnClickListener {
            addTodo()
        }

        buttonCancel.setOnClickListener {
            finish()
        }

        // Set focus to edit text for better UX
        editTextTodo.requestFocus()
    }

    private fun addTodo() {
        val todoTitle = editTextTodo.text.toString().trim()
        
        if (todoTitle.isEmpty()) {
            Toast.makeText(this, "Please enter a todo title", Toast.LENGTH_SHORT).show()
            return
        }

        val todoItem = TodoItem(todoTitle)
        viewModel.add(todoItem)
        
        Toast.makeText(this, "Todo added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}