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
    private val days = listOf("17600", "17650", "17700", "17750", "17800", "17850")

    private val barSpace = 0f

    private val groupSpace = 0.70f

    init {
        // initializing variable for bar chart.
        val combinedChart = CombinedChart(context)

        addView(combinedChart)
        val combinedData = CombinedData()
        combinedData.setData(generateBarChartData())
        combinedData.setData(generateLineData())
        combinedChart.apply {
            description.isEnabled = false
            drawOrder = arrayOf(
                DrawOrder.BAR,
                DrawOrder.LINE
            )
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
                axisMaximum = combinedData.xMax + 0.25f
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
            setVisibleXRangeMaximum(4f)
            setScaleEnabled(false)
            val roundBarChartRenderer = RoundedBarChartRenderer(
                chart = combinedChart,
                animator = combinedChart.animator,
                viewPortHandler = combinedChart.viewPortHandler
            )
            roundBarChartRenderer.setRadius(50)
            roundBarChartRenderer.setRadiusUnit(RoundedRadiusUnit.Percentage)
            data = combinedData
            (renderer as? CombinedChartRenderer)?.subRenderers = listOf(
                roundBarChartRenderer,
                LineChartCircleCenterRenderer(
                    combinedChart,
                    combinedChart.animator,
                    combinedChart.viewPortHandler
                )
            )
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

    private fun generateLineData(): LineData {
        val d = LineData()
        val entries1 = listOf(
            Entry(0f, 15f),
            Entry(1f, 55f),
            Entry(2f, 15f),
            Entry(3f, 35f),
            Entry(4f, 15f),
            Entry(5f, 25f)
        )
        val entries2 = listOf(
            Entry(0f, 26f),
            Entry(1f, 66f),
            Entry(2f, 26f),
            Entry(3f, 46f),
            Entry(4f, 26f),
            Entry(5f, 36f)
        )
        val set1 = LineDataSet(entries1, "Line DataSet 1")
        val set2 = LineDataSet(entries2, "Line DataSet 2")
        set1.apply {
            color = Color.parseColor("#D7C9EF")
            lineWidth = 1.5f
            setCircleColor(Color.parseColor("#581DBE"))
            circleRadius = 5f
            fillColor = Color.parseColor("#581DBE")
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawValues(false)
            isHighlightEnabled = false
        }
        set2.apply {
            color = Color.parseColor("#FEEED4")
            lineWidth = 1.5f
            setCircleColor(Color.parseColor("#F9BA4D"))
            circleRadius = 5f
            fillColor = Color.parseColor("#581DBE")
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawValues(false)
            isHighlightEnabled = false
        }
        d.addDataSet(set1)
        d.addDataSet(set2)
        return d
    }

    private fun generateBarChartData(): BarData {
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
        barData.groupBars(0f, groupSpace, barSpace)

        return barData
    }

    private fun getBarEntriesOne(): List<BarEntry> {
        return listOf(
            BarEntry(1f, 4f),
            BarEntry(2f, 6f),
            BarEntry(3f, 8f),
            BarEntry(4f, 2f),
            BarEntry(5f, 4f),
            BarEntry(6f, 1f)
        )
    }

    // array list for second set.
    private fun getBarEntriesTwo(): List<BarEntry> {
        return listOf(
            BarEntry(1f, 8f),
            BarEntry(2f, 12f),
            BarEntry(3f, 4f),
            BarEntry(4f, 1f),
            BarEntry(5f, 7f),
            BarEntry(6f, 3f)
        )
    }
}