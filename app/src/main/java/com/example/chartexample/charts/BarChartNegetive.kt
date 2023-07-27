package com.example.chartexample.charts

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.chartexample.datamodel.BarChartIndividualDataSet
import com.example.chartexample.datamodel.RoundedRadiusUnit
import com.example.chartexample.renderer.RoundedBarChartRenderer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
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

    private var leftValueFormatter: ValueFormatter? = null

    private var rightValueFormatter: ValueFormatter? = null

    fun setDataSet(dataSet: List<BarChartIndividualDataSet>) {
        val modifiedDataSets = dataSet.map {
            it.dataSet.apply {
                setGradientColor(Color.parseColor(it.gradientColor.startColor), Color.parseColor(it.gradientColor.endColor))
                setDrawValues(false)
            }
        }
        val barData = BarData(modifiedDataSets).apply { barWidth = 0.15f }
        barChart.apply {
            data = barData
            groupBars(0f, groupSpace, barSpace)
            xAxis.apply {
                granularity = 1f
                isGranularityEnabled = true
            }
            setVisibleXRangeMaximum(4f)
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

    fun setYValueFormatter(valueFormatter: ValueFormatter, axisDependency: YAxis.AxisDependency) {
        when (axisDependency) {
            YAxis.AxisDependency.LEFT -> {
                barChart.axisLeft.apply {
                    setValueFormatter(valueFormatter)
                }
                leftValueFormatter = valueFormatter
            }
            YAxis.AxisDependency.RIGHT -> {
                barChart.axisRight.apply {
                    setValueFormatter(valueFormatter)
                }
                rightValueFormatter = valueFormatter
            }
        }
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