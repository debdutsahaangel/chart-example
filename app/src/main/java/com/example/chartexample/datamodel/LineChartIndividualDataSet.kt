package com.example.chartexample.datamodel

import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.LineDataSet

data class LineChartIndividualDataSet(
    val dataSet: LineDataSet,
    val lineColor: String? = null,
    val circleColor: String? = null,
    val fillColor: String? = null,
    val axisDependency: AxisDependency = AxisDependency.LEFT
)
