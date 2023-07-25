package com.example.chartexample.dataGenerator

import com.example.chartexample.datamodel.BarChartIndividualDataSet
import com.example.chartexample.datamodel.GradientColors
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

val barSignData = listOf(
    BarChartIndividualDataSet(
        dataSet = BarDataSet(getBarEntriesForNegativeFirst(), "First Set"),
        gradientColor = GradientColors(
            startColor = "#40D64D4D",
            endColor = "#D64D4D"
        )
    ),
    BarChartIndividualDataSet(
        dataSet = BarDataSet(
            getBarEntriesForNegativeSecond(),
            "Second Set"
        ),
        gradientColor = GradientColors(
            startColor = "#26008F75",
            endColor = "#008F3C"
        )
    )
)

private fun getBarEntriesForNegativeFirst(): List<BarEntry> {
    return listOf(
        BarEntry(1f, 4f),
        BarEntry(2f, -6f),
        BarEntry(3f, 8f),
        BarEntry(4f, -2f),
        BarEntry(5f, 4f),
        BarEntry(6f, -1f)
    )
}

private fun getBarEntriesForNegativeSecond(): List<BarEntry> {
    return listOf(
        BarEntry(1f, 8f),
        BarEntry(2f, 12f),
        BarEntry(3f, -4f),
        BarEntry(4f, -1f),
        BarEntry(5f, 7f),
        BarEntry(6f, -3f)
    )
}
