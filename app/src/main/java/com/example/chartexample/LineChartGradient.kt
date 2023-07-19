package com.example.chartexample

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.math.max
import kotlin.math.roundToInt

class LineChartGradient @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // creating a string array for displaying days.
    private val days = listOf("12PM", "1PM", "2PM", "3PM", "4PM", "5PM")

    init {
        // initializing variable for bar chart.
        val bottomLineChart = LineChart(context)
        val topLineChart = LineChart(context)
        orientation = VERTICAL
        val param = LayoutParams(LayoutParams.MATCH_PARENT,0, 1f)
        addView(topLineChart, param)
        addView(bottomLineChart, param)
        // creating a new bar data set.
        val lineDataSet1 = LineDataSet(getEntriesOne(), "First Set")
        val lineDataSet2 = LineDataSet(getEntriesTwo(), "Second Set")
        val lineDataSet3 = LineDataSet(getEntriesThree(), "Third Set")
        lineDataSet1.apply {
            setDrawValues(false)
            color = Color.parseColor("#F9BA4D")
            lineWidth = 1.5f
            setCircleColor(Color.parseColor("#F9BA4D"))
            circleRadius = 5f
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            setDrawValues(false)
            setDrawFilled(true)
            setDrawCircles(false)
            fillDrawable = ContextCompat.getDrawable(context, R.drawable.line_chart_drawable)
        }

        lineDataSet2.apply {
            setDrawValues(false)
            color = Color.parseColor("#581DBE")
            lineWidth = 1.5f
            setCircleColor(Color.parseColor("#581DBE"))
            circleRadius = 5f
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            setDrawValues(false)
            setDrawCircles(false)
        }

        lineDataSet3.apply {
            setDrawValues(false)
            color = Color.parseColor("#581DBE")
            lineWidth = 1.5f
            setCircleColor(Color.parseColor("#581DBE"))
            circleRadius = 5f
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            setDrawValues(false)
            setDrawCircles(false)
        }

        val bottomLineData = LineData(lineDataSet2, lineDataSet3)
        val topLineData = LineData(lineDataSet1)

        configureLineChart(lineChart = topLineChart, lineData = topLineData) {
            xAxis.setDrawLabels(false)
        }
        configureLineChart(lineChart = bottomLineChart, lineData = bottomLineData)
        mergeTwoLineChart(topLineChart = topLineChart, bottomLineChart = bottomLineChart)
    }

    private fun configureLineChart(
        lineChart: LineChart,
        lineData: LineData,
        scope: LineChart.() -> Unit = {}
    ) {
        lineChart.apply {
            data = lineData
            description.isEnabled = false
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(days)
                setCenterAxisLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                isGranularityEnabled = true
                setDrawGridLines(false)
                textColor = Color.parseColor("#ACB2BD")
                textSize = 12f
                axisLineColor = Color.parseColor("#ACB2BD")
            }
            axisLeft.apply {
                setDrawGridLines(false)
                axisLineColor = Color.parseColor("#ACB2BD")
                textColor = Color.parseColor("#ACB2BD")
                textSize = 12f
            }
            axisRight.apply {
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }

            isDragEnabled = true
            setVisibleXRangeMaximum(4f)
            setScaleEnabled(false)
            legend.isEnabled = false
            scope()
            invalidate()
        }
    }

    private fun mergeTwoLineChart(topLineChart: LineChart, bottomLineChart: LineChart) {
        val topLeft = topLineChart.viewPortHandler.contentLeft()
        val bottomLeft = bottomLineChart.viewPortHandler.contentLeft()
        val targetLeft = max(topLeft, bottomLeft)
        val bottom = topLineChart.viewPortHandler.chartHeight - topLineChart.viewPortHandler.contentBottom()
        topLineChart.apply {
            translationX = targetLeft.minus(topLeft)
            y = 0f
        }
        bottomLineChart.apply {
            translationX = targetLeft.minus(bottomLeft)
            y = -bottom
        }
    }

    private fun getEntriesOne(): List<Entry> {
        return listOf(
            Entry(0f, 18000f),
            Entry(1f, 17600f),
            Entry(2f, 16500f),
            Entry(3f, 17600f),
            Entry(4f, 17650f),
            Entry(4f, 17500f),
            Entry(5f, 17550f)
        )
    }

    private fun getEntriesTwo(): List<Entry> {
        return listOf(
            Entry(0f, 0f),
            Entry(1f, 20f),
            Entry(2f, 25f),
            Entry(3f, 30f),
            Entry(4f, 20f),
            Entry(4f, 16f),
            Entry(5f, 12f)
        )
    }

    private fun getEntriesThree(): List<Entry> {
        return listOf(
            Entry(0f, 0f),
            Entry(1f, 20f),
            Entry(2f, 30f),
            Entry(3f, 35f),
            Entry(4f, 30f),
            Entry(4f, 25f),
            Entry(5f, 20f)
        )
    }

    private fun View.updateMargin(
        left: Int? = null,
        right: Int? = null,
        top: Int? = null,
        bottom: Int? = null
    ) {
        val layoutParams = (this.layoutParams as? MarginLayoutParams)
        layoutParams?.setMargins(
            left ?: this.marginLeft,
            top ?: this.marginTop,
            right ?: this.marginRight,
            bottom ?: this.marginBottom
        )
        setLayoutParams(layoutParams)
    }
}
