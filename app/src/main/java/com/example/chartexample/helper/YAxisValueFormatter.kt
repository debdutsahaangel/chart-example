package com.example.chartexample.helper

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class YAxisValueFormatter: ValueFormatter() {
    var header: String = ""
    var defaultValueFormatter: ValueFormatter? = null
    var maxValue: Float = 0f
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return if (value == axis?.mEntries?.lastOrNull() && value > maxValue) header else defaultValueFormatter?.getFormattedValue(value) ?: ""
    }
}
