package com.example.chartexample.charts

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.example.chartexample.R
import com.example.chartexample.datamodel.BarChartIndividualDataSet
import com.example.chartexample.datamodel.Margin
import com.example.chartexample.datamodel.RoundedRadiusUnit
import com.example.chartexample.markers.DottedLineMarkerView
import com.example.chartexample.renderer.RoundedBarChartRenderer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight

class BarChartIndividuals @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // creating a string array for displaying days.
    private val barSpace = 0f

    private val groupSpace = 0.70f

    private val barChart by lazy { BarChart(context) }

    private var formattedValues = listOf<String>()

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
            val indvMarker = BarChartIndvMarker(context = context)
            marker = indvMarker
            indvMarker.init()
            notifyDataSetChanged()
            invalidate()
        }
    }

    fun setXValueFormatter(formattedValues: List<String>) {
        if (formattedValues.isEmpty()) return
        this.formattedValues = formattedValues
        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(formattedValues)
        }
    }

    init {
        addView(barChart)
        barChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
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

    inner class BarChartIndvMarker(
        context: Context
    ) : DottedLineMarkerView(context) {

        fun init() {
            setChartView(chart = barChart)
            setMargin(margin = Margin(top = 20))
            setLayoutResource(R.layout.bar_indv_marker)
        }

        override fun refreshContent(entry: Entry?, highlight: Highlight?) {
            super.refreshContent(entry, highlight)
            val title = findViewById<TextView>(R.id.title)
            val subTitle = findViewById<TextView>(R.id.sub_title)
            entry?.let {
                title.text = formattedValues[entry.x.toInt()]
                subTitle.text = entry.y.toString()
            }
        }
    }
}