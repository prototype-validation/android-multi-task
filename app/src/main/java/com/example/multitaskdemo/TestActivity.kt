package com.example.multitaskdemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {
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
    }
}
