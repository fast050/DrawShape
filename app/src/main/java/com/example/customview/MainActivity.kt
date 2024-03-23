package com.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      val shapeChanger =  findViewById<ShapeChanger>(R.id.shapeChanger)
        findViewById<EditText>(R.id.editTextNumbers).apply{
            addTextChangedListener {
                it?.toString()?.toIntOrNull()?.let {sides->
                    shapeChanger.setSides(sides)
                    this.text = null
                }
            }
        }
    }
}