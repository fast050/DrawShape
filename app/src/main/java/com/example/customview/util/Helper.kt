package com.example.customview.util

import android.content.Context
import android.util.TypedValue
import android.view.View


fun View.dpToPx(dp:Float): Float {
  return  TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp ,
        this.resources.displayMetrics
    )
}