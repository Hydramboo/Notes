package rj.notes

import android.content.Context
import android.content.Intent
import android.os.Process
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

class GlobalExceptionHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    companion object {
        private const val TAG = "GlobalExceptionHandler"
    }

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            Log.e(TAG, "Uncaught exception: ${throwable.message}", throwable)
            
            // 获取错误信息
            val errorMessage = throwable.message ?: "未知错误"
            val stackTrace = getStackTrace(throwable)
            val errorTitle = "应用崩溃"
            
            // 创建错误显示Activity的Intent
            val intent = Intent(context, ErrorDisplayActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(ErrorDisplayActivity.EXTRA_ERROR_TITLE, errorTitle)
                putExtra(ErrorDisplayActivity.EXTRA_ERROR_MESSAGE, errorMessage)
                putExtra(ErrorDisplayActivity.EXTRA_ERROR_STACK_TRACE, stackTrace)
            }
            
            // 启动错误显示Activity
            context.startActivity(intent)
            
            // 等待一段时间确保Activity启动
            Thread.sleep(1000)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error in GlobalExceptionHandler: ${e.message}", e)
        } finally {
            // 调用默认处理器
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun getStackTrace(throwable: Throwable): String {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        throwable.printStackTrace(printWriter)
        return stringWriter.toString()
    }
}