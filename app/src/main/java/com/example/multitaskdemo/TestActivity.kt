package com.example.multitaskdemo

import android.app.ActivityManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

open class TestActivity : AppCompatActivity() {

    private val activities = (1..36).map { "com.example.multitaskdemo.TestActivity$it" }

    private lateinit var cbNewTask: CheckBox
    private lateinit var cbMultipleTask: CheckBox
    private lateinit var cbNewDocument: CheckBox
    private lateinit var cbRetainInRecents: CheckBox

    private var documentIdCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 随机化任务卡片外观，降低系统合并可能
        val rnd = java.util.Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val desc = ActivityManager.TaskDescription.Builder()
            .setLabel("Doc #$documentIdCounter")
            .setPrimaryColor(color)
            .setIcon(android.R.drawable.ic_dialog_info)
            .build()
        setTaskDescription(desc)

        // 构建界面：显示当前任务信息 + 启动列表
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
        }

        // 当前任务信息
        val info = """
            Task ID: $taskId
            Activity: ${this@TestActivity::class.java.simpleName}
            Affinity: ${packageManager.getActivityInfo(componentName, 0).taskAffinity}
            Intent Flags: ${intent.flags.toString(16)}
        """.trimIndent()
        val infoView = TextView(this).apply {
            text = info
            textSize = 14f
            setPadding(10, 10, 10, 10)
        }
        root.addView(infoView)

        // 复选框（与主界面一致）
        cbNewTask = CheckBox(this).apply { text = "FLAG_ACTIVITY_NEW_TASK" }
        cbMultipleTask = CheckBox(this).apply { text = "FLAG_ACTIVITY_MULTIPLE_TASK" }
        cbNewDocument = CheckBox(this).apply { text = "FLAG_ACTIVITY_NEW_DOCUMENT" }
        cbRetainInRecents = CheckBox(this).apply { text = "FLAG_ACTIVITY_RETAIN_IN_RECENTS" }
        root.addView(cbNewTask)
        root.addView(cbMultipleTask)
        root.addView(cbNewDocument)
        root.addView(cbRetainInRecents)

        // 启动列表
        val listView = ListView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }
        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activities) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                val label = packageManager.getActivityInfo(
                    android.content.ComponentName(context, activities[position]), 0
                ).loadLabel(packageManager)
                view.text = "Activity ${position + 1}: $label"
                return view
            }
        }
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            launchActivity(activities[position])
        }
        root.addView(listView)

        setContentView(root)
    }

    private fun launchActivity(className: String) {
        val intent = Intent().apply {
            setClassName(this@TestActivity, className)
            // 生成唯一文档 ID
            val docId = documentIdCounter++
            data = android.net.Uri.parse("content://com.example.multitaskdemo.document/$docId")

            var flags = 0
            if (cbNewTask.isChecked) flags = flags or Intent.FLAG_ACTIVITY_NEW_TASK
            if (cbMultipleTask.isChecked) flags = flags or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            if (cbNewDocument.isChecked) flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
            if (cbRetainInRecents.isChecked) flags = flags or Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS
            addFlags(flags)
        }
        startActivity(intent)
    }
}
