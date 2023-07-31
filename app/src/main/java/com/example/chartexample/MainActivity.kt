package com.example.chartexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chartexample.databinding.ActivityMainBinding
import com.example.chartexample.dataGenerator.barChartData
import com.example.chartexample.dataGenerator.barSignData
import com.example.chartexample.dataGenerator.combinedData
import com.example.chartexample.dataGenerator.multiLineData
import com.example.chartexample.helper.BarChartIndvMarkerFormatter

class MainActivity : AppCompatActivity() {
    private val xAxisLabel = listOf("17600", "17650", "17700", "17750", "17800", "17850")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(binding.root)
        binding.barChartIndv.apply {
            setXValueFormatter(formattedValues = xAxisLabel)
            setMarkerFormatter(markerFormatter = object : BarChartIndvMarkerFormatter {
                override fun format(groupIndex: Int): String {
                    Log.d("GROUP", "INDEX: ${xAxisLabel[groupIndex]}")
                    return "Nifty: ${xAxisLabel[groupIndex]}"
                }
            })
            setDataSet(
                dataSet = barChartData,
                scrollEnabled = false,
                headers = listOf("OI")
            )
        }

        binding.barChartSign.apply {
            setXValueFormatter(formattedValues = xAxisLabel)
            setDataSet(
                dataSet = barSignData,
                scrollEnabled = false,
                headers = listOf("OI")
            )
        }
        binding.combinedChart.apply {
            setXValueFormatter(formattedValues = xAxisLabel)
            setDataSet(
                dataSet = combinedData,
                scrollEnabled = false,
                headers = listOf("0I","PCR")
            )
        }

        binding.multiLineChart.apply {
            setDataSet(
                dataSet = multiLineData,
                headers = listOf("OI","PCR")
            )
        }
    }
}