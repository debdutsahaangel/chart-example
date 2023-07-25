package com.example.chartexample.dataGenerator

import com.example.chartexample.datamodel.BarChartCombinedData
import com.example.chartexample.datamodel.BarChartIndividualDataSet
import com.example.chartexample.datamodel.GradientColors
import com.example.chartexample.datamodel.LineChartIndividualDataSet
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

val combinedData = BarChartCombinedData(
    barChartDataSet = listOf(
        BarChartIndividualDataSet(
            dataSet = BarDataSet(getBarEntriesForIndvFirst(), "Bar Chart 1"),
            gradientColor = GradientColors(
                startColor = "#40D64D4D",
                endColor = "#D64D4D"
            )
        ),
        BarChartIndividualDataSet(
            dataSet = BarDataSet(
                getBarEntriesForIndvSecond(),
                "Second Set"
            ),
            gradientColor = GradientColors(
                startColor = "#26008F75",
                endColor = "#008F3C"
            )
        )
    ),
    lineChartDataSet = listOf(
        LineChartIndividualDataSet(
            dataSet = LineDataSet(getLineEntriesForCombinedFirst(), "Line Chart 1"),
            lineColor = "#D7C9EF",
            circleColor = "#581DBE",
            fillColor = "#581DBE",
            axisDependency = YAxis.AxisDependency.RIGHT
        ),
        LineChartIndividualDataSet(
            dataSet = LineDataSet(getLineEntriesForCombinedSecond(), "Line Chart 2"),
            lineColor = "#F9BA4D",
            circleColor = "#581DBE",
            fillColor = "#581DBE",
            axisDependency = YAxis.AxisDependency.RIGHT
        )
    )
)

private fun getLineEntriesForCombinedFirst(): List<Entry> {
    return listOf(
        Entry(0f, 15f),
        Entry(1f, 55f),
        Entry(2f, 15f),
        Entry(3f, 35f),
        Entry(4f, 15f),
        Entry(5f, 25f)
    )
}

private fun getLineEntriesForCombinedSecond(): List<Entry> {
    return listOf(
        Entry(0f, 26f),
        Entry(1f, 66f),
        Entry(2f, 26f),
        Entry(3f, 46f),
        Entry(4f, 26f),
        Entry(5f, 36f)
    )
}
