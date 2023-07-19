package com.example.chartexample

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class MultiLineChart @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // creating a string array for displaying days.
    private val days = listOf("12PM", "1PM", "2PM", "3PM", "4PM", "5PM")

    init {
        // initializing variable for bar chart.
        val lineChart = LineChart(context)
        addView(lineChart)
        // creating a new bar data set.
        val lineDataSet1 = LineDataSet(getEntriesOne(), "First Set")
        val lineDataSet2 = LineDataSet(getEntriesTwo(), "Second Set")
        val lineDataSet3 = LineDataSet(getEntriesThree(), "Third Set")
        val lineDataSet4 = LineDataSet(getEntriesFour(), "Fourth Part")
        lineDataSet1.apply {
            setDrawValues(false)
            color = Color.parseColor("#F9BA4D")
            lineWidth = 1.5f
            setCircleColor(Color.parseColor("#F9BA4D"))
            circleRadius = 5f
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            setDrawValues(false)
            setDrawCircles(false)
            axisDependency = YAxis.AxisDependency.RIGHT
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
            color = Color.parseColor("#D64D4D")
        }

        lineDataSet4.apply {
            setDrawValues(false)
            color = Color.parseColor("#581DBE")
            lineWidth = 1.5f
            setCircleColor(Color.parseColor("#581DBE"))
            circleRadius = 5f
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            setDrawValues(false)
            setDrawCircles(false)
            color = Color.parseColor("#21C7DB")
        }

        val lineData = LineData(lineDataSet1, lineDataSet2, lineDataSet3, lineDataSet4)

        configureLineChart(lineChart = lineChart, lineData = lineData)
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
                setDrawGridLines(false)
                textColor = Color.parseColor("#ACB2BD")
                textSize = 12f
                axisLineColor = Color.parseColor("#ACB2BD")
            }

            isDragEnabled = true
            setVisibleXRangeMaximum(4f)
            setScaleEnabled(false)
            legend.isEnabled = false
            scope()
            invalidate()
        }
    }

    private fun getEntriesOne(): List<Entry> {
        return listOf(
            Entry(0f, 18000f),
            Entry(1f, 17600f),
            Entry(2f, 16500f),
            Entry(3f, 17600f),
            Entry(4f, 17650f),
            Entry(5f, 17500f)
        )
    }

    private fun getEntriesTwo(): List<Entry> {
        return listOf(
            Entry(0f, 7f),
            Entry(1f, 6f),
            Entry(2f, 8f),
            Entry(3f, 0f),
            Entry(4f, 8f),
            Entry(5f, 7f)
        )
    }

    private fun getEntriesThree(): List<Entry> {
        return listOf(
            Entry(0f, 0f),
            Entry(1f, 10f),
            Entry(2f, 15f),
            Entry(3f, 12f),
            Entry(4f, 20f),
            Entry(5f, 8f),
        )
    }

    private fun getEntriesFour(): List<Entry> {
        return listOf(
            Entry(0f, 0f),
            Entry(1f, 18f),
            Entry(2f, 5f),
            Entry(3f, 20f),
            Entry(4f, 5f),
            Entry(5f, 20f)
        )
    }
}
