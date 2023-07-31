package com.example.chartexample.charts

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.chartexample.datamodel.BarChartIndividualDataSet
import com.example.chartexample.datamodel.RoundedRadiusUnit
import com.example.chartexample.helper.YAxisValueFormatter
import com.example.chartexample.renderer.RoundedBarChartRenderer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class BarChartNegetive @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // creating a string array for displaying days.
    private val barChart by lazy { BarChart(context) }

    private var formattedValues = listOf<String>()

    private val barSpace = 0f

    private val groupSpace = 0.70f

    private var leftValueFormatter: ValueFormatter = DefaultValueFormatter(1)

    fun setDataSet(dataSet: List<BarChartIndividualDataSet>, scrollEnabled: Boolean = true, headers: List<String>) {
        val modifiedDataSets = dataSet.map {
            it.dataSet.apply {
                setGradientColor(Color.parseColor(it.gradientColor.startColor), Color.parseColor(it.gradientColor.endColor))
                setDrawValues(false)
            }
        }
        val maxYValue = modifiedDataSets.maxOf { it.yMax }
        val barData = BarData(modifiedDataSets).apply { barWidth = 0.15f }
        barChart.apply {
            data = barData
            groupBars(0f, groupSpace, barSpace)
            if (scrollEnabled) {
                xAxis.apply {
                    granularity = 1f
                    isGranularityEnabled = true
                }
            }
            axisLeft.apply {
                valueFormatter = YAxisValueFormatter().apply {
                    header = headers.getOrNull(0) ?: ""
                    defaultValueFormatter = leftValueFormatter
                    maxValue = maxYValue
                }
            }
            if (scrollEnabled) {
                setVisibleXRangeMaximum(4f)
            }
            isDragEnabled = scrollEnabled
            notifyDataSetChanged()
            invalidate()
        }
    }

    fun setXValueFormatter(formattedValues: List<String>) {
        this.formattedValues = formattedValues
        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(formattedValues)
        }
    }

    fun setYValueFormatter(valueFormatter: ValueFormatter) {
        leftValueFormatter = valueFormatter
    }


    init {
        // initializing variable for bar chart.
        addView(barChart)

        barChart.apply {
            description.isEnabled = false
            xAxis.apply {
                setCenterAxisLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
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
                spaceTop = 50f
            }
            axisRight.apply {
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }

            isDragEnabled = true
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
}