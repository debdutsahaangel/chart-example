package com.example.chartexample

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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
            setCircleColor(Color.parseColor("#F9BA4D"))
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            setDrawValues(false)
            setDrawCircles(false)
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        lineDataSet2.apply {
            setDrawValues(false)
            color = Color.parseColor("#581DBE")
            setCircleColor(Color.parseColor("#581DBE"))
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            setDrawValues(false)
            setDrawCircles(false)
        }

        lineDataSet3.apply {
            setDrawValues(false)
            color = Color.parseColor("#581DBE")
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

    @SuppressLint("ClickableViewAccessibility")
    private fun configureLineChart(
        lineChart: LineChart,
        lineData: LineData,
        scope: LineChart.() -> Unit = {}
    ) {
        lineChart.apply {
            data = lineData
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
            setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (lineChart.viewPortHandler.isInBoundsX(motionEvent.rawX)) {
                            val tapPointOnChart = lineChart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(motionEvent.rawX,motionEvent.rawY)
                            val values = buildList {
                                repeat(lineData.dataSetCount) {
                                    add(nearestValue(value = tapPointOnChart.x.toInt(), data = lineData.getDataSetByIndex(it)))
                                }
                            }
                            setOrUpdateMarker(lineChart = lineChart, rawX = motionEvent.rawX, data = values, time = lineChart.xAxis.valueFormatter.getFormattedValue(tapPointOnChart.x.toFloat()))
                            Log.d("CHARTS", "Values: $values - ${values.count()}")
                        }
                    }
                }
                true
            }
            scope()
            invalidate()
        }
    }

    private fun nearestValue(value: Int, data: ILineDataSet): MultiLineData? {
        if (data.entryCount == 0) return null
        if (data.entryCount == 1) return MultiLineData(entry = data.getEntryForIndex(0), color = data.color) // (10) (12) (17) (19) (23) -- 15
        var nearValue = data.getEntryForIndex(0)
        for (index in 1 until data.entryCount) {
            val currentNearXValue = abs(nearValue?.x?.minus(value) ?: 0f)
            val localNearXValue = abs(data.getEntryForIndex(index).x.minus(value))
            if (localNearXValue < currentNearXValue) {
                nearValue = data.getEntryForIndex(index)
            }
        }
        return MultiLineData(entry = nearValue, color = data.color)
    }

    private fun getEntriesOne(): List<Entry> {
        return listOf(
            Entry(0f, 17000f),
            Entry(8f, 17050f),
            Entry(12f, 17120f),
            Entry(16f, 17300f),
            Entry(18f, 17400f),
            Entry(20f, 17200f)
        )
    }

    private fun getEntriesTwo(): List<Entry> {
        return listOf(
            Entry(0f, 7f),
            Entry(7f, 6f),
            Entry(13f, 10f),
            Entry(16f, 12f),
            Entry(19f, 16f),
            Entry(23f, 20f)
        )
    }

    private fun getEntriesThree(): List<Entry> {
        return listOf(
            Entry(0f, 0f),
            Entry(6f, 10f),
            Entry(10f, 15f),
            Entry(12f, 12f),
            Entry(18f, 20f),
            Entry(26f, 8f)
        )
    }

    private fun getEntriesFour(): List<Entry> {
        return listOf(
            Entry(0f, 0f),
            Entry(4f, 18f),
            Entry(8f, 5f),
            Entry(14f, 20f),
            Entry(18f, 5f),
            Entry(30f, 20f)
        )
    }

    private fun setOrUpdateMarker(lineChart: LineChart, rawX: Float, data: List<MultiLineData?>, time: String) {
        val view = if (childCount == 2) {
            getChildAt(1) as? MultiLineChartMarkerView
        } else {
            val requiredHeightForTheView = lineChart.viewPortHandler.contentBottom()
            Log.d("CHARTS", "Required Height: $requiredHeightForTheView")
            val view = MultiLineChartMarkerView(context)
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, requiredHeightForTheView.toInt())
            addView(view, params)
            view
        }
        view?.x = rawX
        view?.setData(data = data, time = time)
    }
}
