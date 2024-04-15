package com.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.customview.stateProgress.State
import com.example.customview.stateProgress.StateProgress
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val states = listOf(
        State("state 1"),
        State("state 2"),
        State("state 3 - longest name"),
        State("state 4"),
    )

    private var stateChangeJob: Job? = null
    private var currentStateIndex = -1
    private lateinit var stateProgress : StateProgress
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      val shapeChanger =  findViewById<ShapeChanger>(R.id.shapeChanger)
      val textCustomView = findViewById<TextCustomView>(R.id.textCustomView)
      stateProgress = findViewById(R.id.stateProgress)
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

    override fun onStart() {
        super.onStart()
        stateChangeJob = lifecycleScope.launch {
            delay(10)
            stateProgress.bindStates(states)
            while (isActive) {
                stateProgress.bindCurrentState(states.getOrNull(currentStateIndex))
                currentStateIndex = (currentStateIndex + 1) % (states.size + 1)
                delay(1000)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stateChangeJob?.cancel()
    }
}