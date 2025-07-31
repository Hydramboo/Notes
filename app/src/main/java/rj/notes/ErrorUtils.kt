package rj.notes

import android.content.Context
import android.content.Intent
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

object ErrorUtils {

    private const val TAG = "ErrorUtils"

    /**
     * 显示错误页面
     */
    fun showError(context: Context, title: String, message: String, throwable: Throwable? = null) {
        try {
            val stackTrace = throwable?.let { getStackTrace(it) } ?: "无堆栈信息"
            
            val intent = Intent(context, ErrorDisplayActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(ErrorDisplayActivity.EXTRA_ERROR_TITLE, title)
                putExtra(ErrorDisplayActivity.EXTRA_ERROR_MESSAGE, message)
                putExtra(ErrorDisplayActivity.EXTRA_ERROR_STACK_TRACE, stackTrace)
            }
            
            context.startActivity(intent)
            Log.d(TAG, "Error activity started: $title - $message")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error showing error page: ${e.message}", e)
        }
    }

    /**
     * 显示错误页面（简化版本）
     */
    fun showError(context: Context, message: String, throwable: Throwable? = null) {
        showError(context, "应用错误", message, throwable)
    }

    /**
     * 获取异常的堆栈跟踪字符串
     */
    private fun getStackTrace(throwable: Throwable): String {
        return try {
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            throwable.printStackTrace(printWriter)
            stringWriter.toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting stack trace: ${e.message}", e)
            "无法获取堆栈信息"
        }
    }

    /**
     * 记录错误日志
     */
    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }
}