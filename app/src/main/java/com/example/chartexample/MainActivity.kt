package com.example.chartexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chartexample.databinding.ActivityMainBinding
import com.example.chartexample.dataGenerator.barChartData
import com.example.chartexample.dataGenerator.barSignData
import com.example.chartexample.dataGenerator.combinedData
import com.example.chartexample.dataGenerator.multiLineData

class MainActivity : AppCompatActivity() {
    private val xAxisLabel = listOf("17600", "17650", "17700", "17750", "17800", "17850")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(binding.root)
        binding.barChartIndv.apply {
            setXValueFormatter(formattedValues = xAxisLabel)
            setDataSet(
                dataSet = barChartData,
                scrollEnabled = false
            )
        }

        binding.barChartSign.apply {
            setXValueFormatter(formattedValues = xAxisLabel)
            setDataSet(
                dataSet = barSignData,
                scrollEnabled = false
            )
        }
        binding.combinedChart.apply {
            setXValueFormatter(formattedValues = xAxisLabel)
            setDataSet(
                dataSet = combinedData,
                scrollEnabled = false
            )
        }

        binding.multiLineChart.apply {
            setDataSet(
                dataSet = multiLineData
            )
        }
    }
}