package com.example.chartexample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.renderer.CombinedChartRenderer
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.MPPointF


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

    fun setDataSet(dataSet: BarChartCombinedData) {
        val modifiedDataSets = dataSet.barChartDataSet.map {
            it.dataSet.apply {
                setGradientColor(Color.parseColor(it.gradientColor.startColor), Color.parseColor(it.gradientColor.endColor))
                setDrawValues(false)
            }
        }
        val modifiedLineSets = dataSet.lineChartDataSet.map {
            it.dataSet.apply {
                setDrawValues(false)
                color = Color.parseColor(it.lineColor)
                lineWidth = 1.5f
                setCircleColor(Color.parseColor(it.circleColor))
                circleRadius = 5f
                fillColor = Color.parseColor(it.fillColor)
                mode = LineDataSet.Mode.LINEAR
                setDrawValues(true)
                axisDependency = YAxis.AxisDependency.RIGHT
                setDrawValues(false)
                isHighlightEnabled = false
            }
        }
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
            xAxis.apply {
                granularity = 1f
                isGranularityEnabled = true
                axisMaximum = combinedData.xMax + 0.25f
            }
            setVisibleXRangeMaximum(4f)
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
            }
            axisRight.apply {
                setDrawGridLines(false)
                axisLineColor = Color.parseColor("#ACB2BD")
                textColor = Color.parseColor("#ACB2BD")
                textSize = 12f
            }

            isDragEnabled = true
            setScaleEnabled(false)
            marker = BarChartCombinedMarker(context = context).apply {
                setChartView(chart = combinedChart)
                setMargin(margin = Margin(top = 20))
                setLayoutResource(res = R.layout.combined_marker)
            }
            setTouchEnabled(true)
            legend.isEnabled = false
            invalidate()
        }
    }
}
