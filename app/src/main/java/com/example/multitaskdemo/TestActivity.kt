package com.example.multitaskdemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.app.ActivityManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

open class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this).apply {
            textSize = 20f
            setPadding(50, 50, 50, 50)
            val info = """
                Task ID: ${taskId}
                Activity: ${this@TestActivity::class.java.simpleName}
                Affinity: ${packageManager.getActivityInfo(componentName, 0).taskAffinity}
                Intent Flags: ${intent.flags.toString(16)}
            """.trimIndent()
            text = info
        }
        setContentView(textView)
        
        // 设置标题，方便在最近任务中区分
        title = intent.getStringExtra("title") ?: "Test Activity"
        
// 随机生成颜色和标签
val r = java.util.Random()
val color = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256))
val label = "Task #${taskId}"
val icon = resources.getDrawable(android.R.drawable.ic_dialog_info, theme) // 随便用一个

val taskDesc = ActivityManager.TaskDescription.Builder()
    .setLabel(label)
    .setPrimaryColor(color)
    .setIcon(icon)
    .build()
setTaskDescription(taskDesc)
    }
}
