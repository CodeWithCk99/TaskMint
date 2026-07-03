package com.codewithck.taskmint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codewithck.taskmint.bottomsheet.AddTaskBottomSheet
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        fabAdd.setOnClickListener {
            AddTaskBottomSheet().show(
                supportFragmentManager,
                "AddTaskBottomSheet"
            )
        }
    }
}