package com.example.chartexample.markers

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.forEachIndexed
import com.example.chartexample.datamodel.MultiLineData
import com.example.chartexample.R
import com.example.chartexample.dataGenerator.multiLineData
import com.example.chartexample.helper.MultiLineMarkerFormatter

class MultiLineChartMarkerView constructor(context: Context) : FrameLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.multi_line_marker_view, this, true)
    }

    fun setData(data: List<MultiLineData?>, time: String, valueFormatter: MultiLineMarkerFormatter?) {
        visibility = View.VISIBLE
        val tooltip = findViewById<CardView>(R.id.tooltip)
        val multiTextView = findViewById<MultiTextView>(R.id.multi_text_view)
        val timeTextView = findViewById<TextView>(R.id.time_textview)
        multiTextView.setText(texts = data.filterNotNull(), valueFormatter = valueFormatter)
        timeTextView.text = time
        multiTextView.setOnClickListener {
            toggle()
        }
        tooltip.setOnClickListener {
            toggle()
        }
    }

    private fun View.toggle() {
        visibility = if (visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
    }
}

class MultiTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    fun setText(texts: List<MultiLineData>, valueFormatter: MultiLineMarkerFormatter?) {
        if (childCount == 0) {
            texts.forEach {
                addView(
                    TextView(context).apply {
                        text = format(multiLineData = it, formatter = valueFormatter)
                        setTextColor(it.color)
                        setTextSize(TypedValue.COMPLEX_UNIT_SP,9f)
                    }
                )
            }
        } else {
            forEachIndexed { index, view ->
                (view as? TextView)?.apply {
                    text = format(multiLineData = texts[index], formatter = valueFormatter)
                    setTextColor(texts[index].color)
                }
            }
        }
    }

    private fun format(multiLineData: MultiLineData, formatter: MultiLineMarkerFormatter?): String {
        return formatter?.let {
            multiLineData.entry?.let { entry ->
                formatter.format(entry = entry, lineIdentifier = multiLineData.lineIndex)
            }
        } ?: multiLineData.entry?.y?.toString() ?: ""
    }
}
