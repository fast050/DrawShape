package com.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.example.customview.matrix.ArrowShapeView
import com.example.customview.matrix.ProgressSlider
import com.example.customview.matrix.SliderChangeListener
import com.example.customview.stateProgress.State
import com.example.customview.stateProgress.StateProgress
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arrowShapeView :ArrowShapeView = findViewById(R.id.arrowShapeView)
        val sliderX :ProgressSlider = findViewById(R.id.progressSliderX)
        val sliderY :ProgressSlider = findViewById(R.id.progressSliderY)
        val sliderRotate :ProgressSlider = findViewById(R.id.progressSliderRotate)


        sliderX.sliderChangeListener = object :SliderChangeListener{
            override fun onValueChanged(value: Float) {
                arrowShapeView.startArrowX = value * arrowShapeView.width
            }
        }

        sliderY.sliderChangeListener = object :SliderChangeListener{
            override fun onValueChanged(value: Float) {
                arrowShapeView.startArrowY = value * arrowShapeView.height
            }
        }

        sliderRotate.sliderChangeListener = object :SliderChangeListener{
            override fun onValueChanged(value: Float) {
                arrowShapeView.rotationOfShape = value * 360
            }
        }
    }
}