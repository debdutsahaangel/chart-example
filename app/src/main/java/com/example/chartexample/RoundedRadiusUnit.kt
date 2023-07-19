package com.example.chartexample

sealed interface RoundedRadiusUnit {
    object Percentage: RoundedRadiusUnit
    object Actual: RoundedRadiusUnit
}
