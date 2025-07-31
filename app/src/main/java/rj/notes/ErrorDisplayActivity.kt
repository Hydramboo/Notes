package rj.notes

import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.content.Intent

class ErrorDisplayActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ERROR_MESSAGE = "error_message"
        const val EXTRA_ERROR_STACK_TRACE = "error_stack_trace"
        const val EXTRA_ERROR_TITLE = "error_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_display)

        val errorTitle = intent.getStringExtra(EXTRA_ERROR_TITLE) ?: "应用错误"
        val errorMessage = intent.getStringExtra(EXTRA_ERROR_MESSAGE) ?: "未知错误"
        val stackTrace = intent.getStringExtra(EXTRA_ERROR_STACK_TRACE) ?: "无堆栈信息"

        setupViews(errorTitle, errorMessage, stackTrace)
    }

    private fun setupViews(title: String, message: String, stackTrace: String) {
        val titleTextView = findViewById<TextView>(R.id.tvErrorTitle)
        val messageTextView = findViewById<TextView>(R.id.tvErrorMessage)
        val stackTraceTextView = findViewById<TextView>(R.id.tvStackTrace)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val btnRestart = findViewById<Button>(R.id.btnRestart)
        val btnClose = findViewById<Button>(R.id.btnClose)
        val btnCopy = findViewById<Button>(R.id.btnCopy)

        titleTextView.text = title
        messageTextView.text = message
        stackTraceTextView.text = stackTrace

        // 设置按钮点击事件
        btnRestart.setOnClickListener {
            // 重启应用
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        btnClose.setOnClickListener {
            finish()
        }

        btnCopy.setOnClickListener {
            // 复制错误信息到剪贴板
            val errorInfo = """
                错误标题: $title
                错误信息: $message
                堆栈跟踪:
                $stackTrace
            """.trimIndent()
            
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("错误信息", errorInfo)
            clipboard.setPrimaryClip(clip)
            
            android.widget.Toast.makeText(this, "错误信息已复制到剪贴板", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}