package com.example.chartexample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class BarChartNegetive @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // creating a string array for displaying days.
    private val days = listOf("17600", "17650", "17700", "17750", "17800", "17850")

    private val barSpace = 0f

    private val groupSpace = 0.70f

    init {
        // initializing variable for bar chart.
        val barChart = BarChart(context)
        addView(barChart)
        // creating a new bar data set.
        val barDataSet1 = BarDataSet(getBarEntriesOne(), "First Set");
        barDataSet1.apply {
            setGradientColor(Color.parseColor("#40D64D4D"), Color.parseColor("#D64D4D"))
            setDrawValues(false)
        }
        val barDataSet2 = BarDataSet(getBarEntriesTwo(), "Second Set");
        barDataSet2.apply {
            setGradientColor(Color.parseColor("#26008F75"), Color.parseColor("#008F3C"))
            setDrawValues(false)
        }

        val barData = BarData(barDataSet1, barDataSet2);
        barData.barWidth = 0.15f

        barChart.apply {
            data = barData
            description.isEnabled = false
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(days)
                setCenterAxisLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                isGranularityEnabled = true
                axisMinimum = 0f
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
            groupBars(0f, groupSpace, barSpace)
            setScaleEnabled(false)
            val roundRenderer = RoundedBarChartRenderer(
                chart = barChart,
                animator = barChart.animator,
                viewPortHandler = barChart.viewPortHandler
            )
            roundRenderer.setRadius(50)
            roundRenderer.setRadiusUnit(RoundedRadiusUnit.Percentage)
            renderer = roundRenderer
            legend.isEnabled = false
            invalidate()
        }
    }

    private fun getBarEntriesOne(): List<BarEntry> {
        return listOf(
            BarEntry(1f, 4f),
            BarEntry(2f, -6f),
            BarEntry(3f, 8f),
            BarEntry(4f, -2f),
            BarEntry(5f, 4f),
            BarEntry(6f, -1f)
        )
    }

    // array list for second set.
    private fun getBarEntriesTwo(): List<BarEntry> {
        return listOf(
            BarEntry(1f, 8f),
            BarEntry(2f, 12f),
            BarEntry(3f, -4f),
            BarEntry(4f, -1f),
            BarEntry(5f, 7f),
            BarEntry(6f, -3f)
        )
    }
}