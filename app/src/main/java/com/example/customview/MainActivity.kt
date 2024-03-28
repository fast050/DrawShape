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
      val textCustomView = findViewById<TextCustomView>(R.id.textCustomView)
        findViewById<EditText>(R.id.editTextNumbers).apply{
            addTextChangedListener {
                textCustomView.setText(it.toString()/* , dpToPx(50f)*/ )
                it?.toString()?.toIntOrNull()?.let {sides->
                    shapeChanger.setSides(sides)

                    if(this.text.length >3)
                        this.text = null
                    //textCustomView.setText(sides.toString())
                }
            }
        }
    }
}