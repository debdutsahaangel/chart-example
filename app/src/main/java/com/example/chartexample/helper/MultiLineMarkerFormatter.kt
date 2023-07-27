package com.example.chartexample.helper

import com.github.mikephil.charting.data.Entry

interface MultiLineMarkerFormatter {
    fun format(entry: Entry, lineIdentifier: Int): String
}