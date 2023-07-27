package com.example.chartexample.datamodel

sealed interface ChartType {
    object BarChart: ChartType
    object LineChart: ChartType
}
