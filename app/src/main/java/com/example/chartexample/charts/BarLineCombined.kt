package com.example.chartexample.charts

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.chartexample.datamodel.BarChartCombinedData
import com.example.chartexample.datamodel.RoundedRadiusUnit
import com.example.chartexample.helper.YAxisValueFormatter
import com.example.chartexample.markers.BarChartCombinedMarker
import com.example.chartexample.renderer.LineChartCircleCenterRenderer
import com.example.chartexample.renderer.RoundedBarChartRenderer
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.CombinedChartRenderer


class BarLineCombined @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // creating a string array for displaying days.
    private var formattedValues = listOf<String>()

    private val barWidth = 0.15f

    private val barSpace = 0f

    private val groupSpace = 0.70f

    private val combinedChart by lazy { CombinedChart(context) }

    private var leftValueFormatter: ValueFormatter = DefaultValueFormatter(1)

    private var rightValueFormatter: ValueFormatter? = DefaultValueFormatter(1)

    fun setDataSet(dataSet: BarChartCombinedData, scrollEnabled: Boolean = true, headers: List<String>) {
        val modifiedDataSets = dataSet.barChartDataSet.map {
            it.dataSet.apply {
                setGradientColor(Color.parseColor(it.gradientColor.startColor), Color.parseColor(it.gradientColor.endColor))
                setDrawValues(false)
            }
        }
        val modifiedLineSets = dataSet.lineChartDataSet.map {
            it.dataSet.apply {
                setDrawValues(false)
                it.lineColor?.let { lineColor ->
                    color = Color.parseColor(lineColor)
                }
                it.circleColor?.let { circleColor ->
                    setCircleColor(Color.parseColor(circleColor))
                }
                it.fillColor?.let { color ->
                    fillColor = Color.parseColor(color)
                }
                lineWidth = 1.5f
                circleRadius = 5f
                mode = LineDataSet.Mode.LINEAR
                setDrawValues(true)
                axisDependency = it.axisDependency
                setDrawValues(false)
                isHighlightEnabled = false
            }
        }
        val maxLeftYValue = modifiedDataSets.maxOf { it.yMax }
        val maxRightYValue = modifiedDataSets.maxOf { it.yMax }
        val combinedData = CombinedData().apply {
            // Setting the bar chart data set for displaying bar chart
            setData(
                BarData(modifiedDataSets).apply {
                    barWidth = this@BarLineCombined.barWidth
                }
            )
            // Setting the line chart data set for displaying line charts on top of bar chart
            setData(LineData(modifiedLineSets))
        }
        combinedChart.apply {
            data = combinedData
            combinedData.barData?.apply {
                groupBars(0f, groupSpace, barSpace)
            }
            val roundBarChartRenderer = RoundedBarChartRenderer(
                chart = combinedChart,
                animator = combinedChart.animator,
                viewPortHandler = combinedChart.viewPortHandler
            )
            roundBarChartRenderer.setRadius(50)
            roundBarChartRenderer.setRadiusUnit(RoundedRadiusUnit.Percentage)
            (renderer as? CombinedChartRenderer)?.subRenderers = listOf(
                roundBarChartRenderer,
                LineChartCircleCenterRenderer(
                    combinedChart,
                    combinedChart.animator,
                    combinedChart.viewPortHandler
                )
            )
            if (scrollEnabled) {
                xAxis.apply {
                    granularity = 1f
                    isGranularityEnabled = true
                    axisMaximum = combinedData.xMax + 0.25f
                }
                setVisibleXRangeMaximum(4f)
            }
            axisLeft.apply {
                valueFormatter = YAxisValueFormatter().apply {
                    maxValue = maxLeftYValue
                    defaultValueFormatter = leftValueFormatter
                    header = headers.getOrNull(0) ?: ""
                }
            }
            axisRight.apply {
                valueFormatter = YAxisValueFormatter().apply {
                    maxValue = maxRightYValue
                    defaultValueFormatter = rightValueFormatter
                    header = headers.getOrNull(1) ?: ""
                }
            }
            isDragEnabled = scrollEnabled
            marker = BarChartCombinedMarker(context = context, chartView = combinedChart, data = combinedData)
            notifyDataSetChanged()
            invalidate()
        }
    }

    fun setXValueFormatter(formattedValues: List<String>) {
        if (formattedValues.isEmpty()) return
        this.formattedValues = formattedValues
        combinedChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(formattedValues)
        }
    }

    fun setYValueFormatter(valueFormatter: ValueFormatter, axisDependency: AxisDependency) {
        when (axisDependency) {
            AxisDependency.LEFT -> {
                leftValueFormatter = valueFormatter
            }
            AxisDependency.RIGHT -> {
                rightValueFormatter = valueFormatter
            }
        }
    }

    init {
        addView(combinedChart)
        combinedChart.apply {
            description.isEnabled = false
            drawOrder = arrayOf(
                DrawOrder.BAR,
                DrawOrder.LINE
            )
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
                spaceTop = 70f
            }
            axisRight.apply {
                setDrawGridLines(false)
                axisLineColor = Color.parseColor("#ACB2BD")
                textColor = Color.parseColor("#ACB2BD")
                textSize = 12f
                spaceTop = 70f
            }

            isDragEnabled = true
            setScaleEnabled(false)
            setTouchEnabled(true)
            legend.isEnabled = false
            invalidate()
        }
    }
}
