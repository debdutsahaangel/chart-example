package com.example.chartexample.helper

import com.example.chartexample.datamodel.ChartType
import com.github.mikephil.charting.components.YAxis.AxisDependency

interface BarLineCombinedFormatter {
    fun format(chartType: ChartType, dataIndex: Int, yValue: Float, axisDependency: AxisDependency): String
}