package com.example.multitaskdemo

import android.app.ActivityManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this).apply {
            setPadding(50, 50, 50, 50)
            textSize = 16f
        }
        setContentView(textView)

        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.appTasks  // 只返回本应用的任务
        val sb = StringBuilder("Current App Tasks (${tasks.size}):\n\n")
        for ((index, task) in tasks.withIndex()) {
            val info = task.taskInfo
            sb.append("Task ${index + 1}:\n")
            sb.append("  id: ${info.id}\n")
            sb.append("  baseActivity: ${info.baseActivity?.className ?: "N/A"}\n")
            sb.append("  topActivity: ${info.topActivity?.className ?: "N/A"}\n")
            sb.append("  numActivities: ${info.numActivities}\n\n")
        }
        textView.text = sb.toString()
    }
}
