package com.example.chartexample.dataGenerator

import com.example.chartexample.datamodel.LineChartIndividualDataSet
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

val multiLineData = listOf(
    LineChartIndividualDataSet(
        dataSet = LineDataSet(getLineEntriesForMultiLineFirst(), "Line Chart 1"),
        lineColor = "#F9BA4D",
        circleColor = "#F9BA4D",
        fillColor = "#F9BA4D",
        axisDependency = YAxis.AxisDependency.RIGHT
    ),
    LineChartIndividualDataSet(
        dataSet = LineDataSet(getLineEntriesForMultiLineSecond(),  "Line Chart 2"),
        lineColor = "#581DBE",
        circleColor = "#581DBE",
        fillColor = "#581DBE"
    ),
    LineChartIndividualDataSet(
        dataSet = LineDataSet(getLineEntriesForMultiLineThird(), "Line Chart 3"),
        lineColor = "#D64D4D",
        circleColor = "#D64D4D",
        fillColor = "#D64D4D"
    ),
    LineChartIndividualDataSet(
        dataSet = LineDataSet(getLineEntriesForMultiLineForth(), "Line Chart 4"),
        lineColor = "#21C7DB",
        circleColor = "#21C7DB",
        fillColor = "#21C7DB"
    )
)

private fun getLineEntriesForMultiLineFirst(): List<Entry> {
    return listOf(
        Entry(0f, 17000f),
        Entry(8f, 17050f),
        Entry(12f, 17120f),
        Entry(16f, 17300f),
        Entry(18f, 17400f),
        Entry(20f, 17200f)
    )
}

private fun getLineEntriesForMultiLineSecond(): List<Entry> {
    return listOf(
        Entry(0f, 7f),
        Entry(7f, 6f),
        Entry(13f, 10f),
        Entry(16f, 12f),
        Entry(19f, 16f),
        Entry(23f, 20f)
    )
}

private fun getLineEntriesForMultiLineThird(): List<Entry> {
    return listOf(
        Entry(0f, 0f),
        Entry(6f, 10f),
        Entry(10f, 15f),
        Entry(12f, 12f),
        Entry(18f, 20f),
        Entry(26f, 8f)
    )
}

private fun getLineEntriesForMultiLineForth(): List<Entry> {
    return listOf(
        Entry(0f, 0f),
        Entry(4f, 18f),
        Entry(8f, 5f),
        Entry(14f, 20f),
        Entry(18f, 5f),
        Entry(30f, 20f)
    )
}
