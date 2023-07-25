package com.example.chartexample.datamodel

sealed interface RoundedRadiusUnit {
    object Percentage: RoundedRadiusUnit
    object Actual: RoundedRadiusUnit
}
