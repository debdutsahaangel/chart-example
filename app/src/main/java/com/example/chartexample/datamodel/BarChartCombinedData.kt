package com.example.chartexample.datamodel

data class BarChartCombinedData(
    val barChartDataSet: List<BarChartIndividualDataSet>,
    val lineChartDataSet: List<LineChartIndividualDataSet>
)
