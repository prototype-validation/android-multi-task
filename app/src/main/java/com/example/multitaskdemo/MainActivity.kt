package com.example.multitaskdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val activities = (1..36).map { "com.example.multitaskdemo.TestActivity$it" }
    
    private lateinit var cbNewTask: CheckBox
    private lateinit var cbMultipleTask: CheckBox
    private lateinit var cbNewDocument: CheckBox
    private lateinit var cbRetainInRecents: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
        }

        val header = TextView(this).apply {
            text = "Select Activity and Flags to Launch"
            textSize = 18f
        }
        root.addView(header)

        cbNewTask = CheckBox(this).apply { text = "FLAG_ACTIVITY_NEW_TASK" }
        cbMultipleTask = CheckBox(this).apply { text = "FLAG_ACTIVITY_MULTIPLE_TASK" }
        cbNewDocument = CheckBox(this).apply { text = "FLAG_ACTIVITY_NEW_DOCUMENT" }
        cbRetainInRecents = CheckBox(this).apply { text = "FLAG_ACTIVITY_RETAIN_IN_RECENTS" }

        root.addView(cbNewTask)
        root.addView(cbMultipleTask)
        root.addView(cbNewDocument)
        root.addView(cbRetainInRecents)

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
            launchActivity(activities[position], position + 1)
        }
        
        root.addView(listView)
        setContentView(root)
    }

    private fun launchActivity(className: String, index: Int) {
        val intent = Intent().apply {
            setClassName(this@MainActivity, className)
            putExtra("title", "Instance $index")
            
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
