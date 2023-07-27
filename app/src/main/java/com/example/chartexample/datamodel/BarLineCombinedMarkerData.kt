package com.example.chartexample.datamodel

import com.github.mikephil.charting.components.YAxis.AxisDependency

data class BarLineCombinedMarkerData(
    val chartType: ChartType,
    val yAxisValue: Float,
    val axisDependency: AxisDependency,
    val textColor: Int,
    val currentIndex: Int
)
