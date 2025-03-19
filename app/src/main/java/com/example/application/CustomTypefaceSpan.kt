package com.example.application

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class CustomTypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeface(ds, typeface)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeface(paint, typeface)
    }

    private fun applyCustomTypeface(paint: TextPaint, tf: Typeface) {
        paint.typeface = tf
    }
}