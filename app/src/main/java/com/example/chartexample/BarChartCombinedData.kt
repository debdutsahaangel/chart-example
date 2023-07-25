package com.example.chartexample

data class BarChartCombinedData(
    val barChartDataSet: List<BarChartIndividualDataSet>,
    val lineChartDataSet: List<LineChartIndividualDataSet>
)
