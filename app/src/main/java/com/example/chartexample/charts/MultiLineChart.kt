package com.example.chartexample.charts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import com.example.chartexample.datamodel.BarChartIndividualDataSet
import com.example.chartexample.datamodel.LineChartIndividualDataSet
import com.example.chartexample.datamodel.MultiLineData
import com.example.chartexample.helper.MultiLineMarkerFormatter
import com.example.chartexample.markers.MultiLineChartMarkerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.abs

class MultiLineChart @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val lineChart by lazy { LineChart(context) }

    private var leftValueFormatter: ValueFormatter? = null

    private var rightValueFormatter: ValueFormatter? = null

    private var lineMarkerFormatter: MultiLineMarkerFormatter? = null

    fun setDataSet(dataSet: List<LineChartIndividualDataSet>) {
        val modifiedLineDataSet = dataSet.map {
            it.dataSet.apply {
                setDrawValues(false)
                it.lineColor?.let { lineColor ->
                    color = Color.parseColor(lineColor)
                }
                it.circleColor?.let { circleColor ->
                    setCircleColor(Color.parseColor(circleColor))
                }
                axisDependency = it.axisDependency
                circleRadius = 5f
                mode = LineDataSet.Mode.LINEAR
                setDrawValues(true)
                setDrawValues(false)
                setDrawCircles(false)
            }
        }
        val lineData = LineData(modifiedLineDataSet)
        lineChart.apply {
            data = lineData
            setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (lineChart.viewPortHandler.isInBoundsX(motionEvent.rawX)) {
                            val tapPointOnChart = lineChart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(motionEvent.rawX,motionEvent.rawY)
                            val values = buildList {
                                repeat(lineData.dataSetCount) {
                                    add(nearestValue(value = tapPointOnChart.x.toInt(), data = lineData.getDataSetByIndex(it), lineIndex = it))
                                }
                            }
                            setOrUpdateMarker(lineChart = lineChart, rawX = motionEvent.rawX, data = values, time = lineChart.xAxis.valueFormatter.getFormattedValue(tapPointOnChart.x.toFloat()))
                            Log.d("CHARTS", "Values: $values - ${values.count()}")
                        }
                    }
                }
                true
            }
            notifyDataSetChanged()
            invalidate()
        }
    }

    fun setYValueFormatter(valueFormatter: ValueFormatter, axisDependency: YAxis.AxisDependency) {
        when (axisDependency) {
            YAxis.AxisDependency.LEFT -> {
                lineChart.axisLeft.apply {
                    setValueFormatter(valueFormatter)
                }
                leftValueFormatter = valueFormatter
            }
            YAxis.AxisDependency.RIGHT -> {
                lineChart.axisRight.apply {
                    setValueFormatter(valueFormatter)
                }
                rightValueFormatter = valueFormatter
            }
        }
    }

    fun setMarkerValueFormatter(markerFormatter: MultiLineMarkerFormatter) {
        this.lineMarkerFormatter = lineMarkerFormatter
    }

    init {
        // initializing variable for bar chart.
        addView(lineChart)
        configureLineChart(lineChart = lineChart)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configureLineChart(
        lineChart: LineChart,
        scope: LineChart.() -> Unit = {}
    ) {
        lineChart.apply {
            description.isEnabled = false
            //isHighlightPerDragEnabled = false
            //isHighlightPerTapEnabled = false
            xAxis.apply {
                valueFormatter = object : IndexAxisValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val simpleDateFormat = SimpleDateFormat("HH:mm aa")
                        val calender = Calendar.getInstance()
                        calender.time = Date(System.currentTimeMillis())
                        calender.add(Calendar.MINUTE, value.toInt())
                        return simpleDateFormat.format(calender.time)
                    }
                }
                setCenterAxisLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.parseColor("#ACB2BD")
                textSize = 12f
                axisLineColor = Color.parseColor("#ACB2BD")
                axisMinimum = 0f
                axisMaximum = 30f
                setLabelCount(6, true)
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
            setScaleEnabled(false)
            legend.isEnabled = false
            extraBottomOffset = 20f
            scope()
            invalidate()
        }
    }

    private fun nearestValue(value: Int, data: ILineDataSet, lineIndex: Int): MultiLineData? {
        if (data.entryCount == 0) return null
        if (data.entryCount == 1) return MultiLineData(entry = data.getEntryForIndex(0), color = data.color, lineIndex = lineIndex)
        var nearValue = data.getEntryForIndex(0)
        for (index in 1 until data.entryCount) {
            val currentNearXValue = abs(nearValue?.x?.minus(value) ?: 0f)
            val localNearXValue = abs(data.getEntryForIndex(index).x.minus(value))
            if (localNearXValue < currentNearXValue) {
                nearValue = data.getEntryForIndex(index)
            }
        }
        return MultiLineData(entry = nearValue, color = data.color, lineIndex = lineIndex)
    }

    private fun setOrUpdateMarker(lineChart: LineChart, rawX: Float, data: List<MultiLineData?>, time: String) {
        val view = if (childCount == 2) {
            getChildAt(1) as? MultiLineChartMarkerView
        } else {
            val requiredHeightForTheView = lineChart.viewPortHandler.contentBottom()
            val view = MultiLineChartMarkerView(context)
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, requiredHeightForTheView.toInt())
            addView(view, params)
            view
        }
        view?.x = rawX
        view?.setData(data = data, time = time, valueFormatter = lineMarkerFormatter)
    }
}
