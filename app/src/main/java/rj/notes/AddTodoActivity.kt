package rj.notes

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.huanli233.hikage.extension.lifecycleOwner
import com.huanli233.hikage.extension.setContentView
import com.huanli233.hikage.extension.widget.onClick
import com.huanli233.hikage.extension.widget.textRes
import com.huanli233.hikage.extension.widget.vertical
import com.huanli233.hikage.widget.android.widget.LinearLayout
import com.huanli233.hikage.widget.android.widget.TextView
import com.huanli233.hikage.widget.com.google.android.material.button.MaterialButton
import com.huanli233.hikage.widget.com.google.android.material.textfield.TextInputEditText
import com.huanli233.hikage.widget.com.google.android.material.textfield.TextInputLayout
import rj.notes.model.TodoItem
import kotlinx.coroutines.launch
import rj.notes.viewmodel.TodoViewModel

class AddTodoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddTodoActivity"
    }

    private val viewModel: TodoViewModel by lazy { TodoViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView {
                var todoText = ""
                
                LinearLayout(
                    lparams = matchParent(),
                    init = {
                        vertical()
                        setPadding(24.dp)
                        setBackgroundResource(R.color.background_color)
                    }
                ) {
                    TextView(
                        lparams = matchParent {
                            marginBottom = 32.dp
                        }
                    ) {
                        text = "添加新笔记"
                        textSize = 24.sp
                        setTypeface(null, android.graphics.Typeface.BOLD)
                        setTextColor(resources.getColor(R.color.text_primary, null))
                        gravity = android.view.Gravity.CENTER
                    }

                    TextInputLayout(
                        lparams = matchParent {
                            marginBottom = 24.dp
                        }
                    ) {
                        TextInputEditText(
                            lparams = matchParent()
                        ) {
                            hint = getString(R.string.enter_new_todo)
                            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_MULTI_LINE
                            maxLines = 3
                            minLines = 2
                            setPadding(16.dp)
                            setBackgroundResource(R.color.surface_color)
                            setTextColor(resources.getColor(R.color.text_primary, null))
                            textSize = 16.sp
                            doOnTextChanged { text, _, _, _ ->
                                todoText = text.toString()
                            }
                        }
                    }

                    MaterialButton(
                        lparams = matchParent {
                            marginBottom = 12.dp
                        }
                    ) {
                        text = getString(R.string.add_to_do)
                        textSize = 16.sp
                        setPadding(16.dp)
                        onClick {
                            addTodo(todoText)
                        }
                    }

                    MaterialButton(
                        lparams = matchParent()
                    ) {
                        text = getString(R.string.cancel)
                        textSize = 16.sp
                        setPadding(16.dp)
                        setBackgroundResource(android.R.color.transparent)
                        onClick {
                            finish()
                        }
                    }
                }
            }
            
            Log.d(TAG, "HikageCompat layout loaded successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            ErrorUtils.showError(this, "初始化失败", "AddTodoActivity初始化时发生错误", e)
        }
    }

    private fun addTodo(todoText: String) {
        try {
            if (todoText.trim().isEmpty()) {
                Toast.makeText(this, "请输入笔记内容", Toast.LENGTH_SHORT).show()
                return
            }

            lifecycleScope.launch {
                try {
                    val todoItem = TodoItem(todoText.trim())
                    viewModel.addTodo(todoItem)
                    Toast.makeText(this@AddTodoActivity, "笔记添加成功", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Log.e(TAG, "Error adding todo: ${e.message}", e)
                    ErrorUtils.showError(this@AddTodoActivity, "添加失败", "添加笔记时发生错误", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in addTodo: ${e.message}", e)
            ErrorUtils.showError(this, "操作失败", "添加笔记操作时发生错误", e)
        }
    }
}