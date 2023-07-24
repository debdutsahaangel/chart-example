package com.example.chartexample

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.forEachIndexed

class MultiLineChartMarkerView constructor(context: Context) : FrameLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.multi_line_marker_view, this, true)
    }

    fun setData(data: List<MultiLineData?>, time: String) {
        val multiTextView = findViewById<MultiTextView>(R.id.multi_text_view)
        val timeTextView = findViewById<TextView>(R.id.time_textview)
        multiTextView.setText(texts = data.filterNotNull())
        timeTextView.text = time
    }
}

class MultiTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    fun setText(texts: List<MultiLineData>) {
        if (childCount == 0) {
            texts.forEach {
                addView(
                    TextView(context).apply {
                        text = "Y: ${it.entry?.y}"
                        setTextColor(it.color)
                    }
                )
            }
        } else {
            forEachIndexed { index, view ->
                (view as? TextView)?.apply {
                    text = "Y: ${texts[index].entry?.y}"
                    setTextColor(texts[index].color)
                }
            }
        }
    }
}
