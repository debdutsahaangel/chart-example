package com.example.chartexample.datamodel

import com.github.mikephil.charting.data.LineDataSet

data class LineChartIndividualDataSet(
    val dataSet: LineDataSet,
    val lineColor: String,
    val circleColor: String,
    val fillColor: String
)
