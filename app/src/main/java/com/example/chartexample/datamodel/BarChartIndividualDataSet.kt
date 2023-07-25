package com.example.chartexample.datamodel

import com.github.mikephil.charting.data.BarDataSet

data class BarChartIndividualDataSet(
    val dataSet: BarDataSet,
    val gradientColor: GradientColors,
)
