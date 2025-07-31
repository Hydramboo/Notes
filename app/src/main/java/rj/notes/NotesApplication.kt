package rj.notes

import android.app.Application
import android.util.Log

class NotesApplication : Application() {

    companion object {
        private const val TAG = "NotesApplication"
    }

    override fun onCreate() {
        super.onCreate()
        
        try {
            // 设置全局异常处理器
            val globalExceptionHandler = GlobalExceptionHandler(this)
            Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler)
            
            Log.d(TAG, "Global exception handler set successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting global exception handler: ${e.message}", e)
        }
    }
}