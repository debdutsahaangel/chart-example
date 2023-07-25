package com.example.chartexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chartexample.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(binding.root)
        binding.barChartIndv.apply {
            setXValueFormatter(formattedValues = listOf("17600", "17650", "17700", "17750", "17800", "17850"))
            setDataSet(
                dataSet = listOf(
                    BarChartIndividualDataSet(
                        dataSet = BarDataSet(getBarEntriesForIndvFirst(), "First Set"),
                        gradientColor = GradientColors(
                            startColor = "#40D64D4D",
                            endColor = "#D64D4D"
                        )
                    ),
                    BarChartIndividualDataSet(
                        dataSet = BarDataSet(
                            getBarEntriesForIndvSecond(),
                            "Second Set"
                        ),
                        gradientColor = GradientColors(
                            startColor = "#26008F75",
                            endColor = "#008F3C"
                        )
                    )
                )
            )
        }

        binding.barChartSign.apply {
            setXValueFormatter(formattedValues = listOf("17600", "17650", "17700", "17750", "17800", "17850"))
            setDataSet(
                dataSet = listOf(
                    BarChartIndividualDataSet(
                        dataSet = BarDataSet(getBarEntriesForNegativeFirst(), "First Set"),
                        gradientColor = GradientColors(
                            startColor = "#40D64D4D",
                            endColor = "#D64D4D"
                        )
                    ),
                    BarChartIndividualDataSet(
                        dataSet = BarDataSet(
                            getBarEntriesForNegativeSecond(),
                            "Second Set"
                        ),
                        gradientColor = GradientColors(
                            startColor = "#26008F75",
                            endColor = "#008F3C"
                        )
                    )
                )
            )
        }
    }

    private fun getBarEntriesForIndvFirst(): List<BarEntry> {
        return listOf(
            BarEntry(1f, 4f),
            BarEntry(2f, 6f),
            BarEntry(3f, 8f),
            BarEntry(4f, 2f),
            BarEntry(5f, 4f),
            BarEntry(6f, 1f)
        )
    }

    // array list for second set.
    private fun getBarEntriesForIndvSecond(): List<BarEntry> {
        return listOf(
            BarEntry(1f, 8f),
            BarEntry(2f, 12f),
            BarEntry(3f, 4f),
            BarEntry(4f, 1f),
            BarEntry(5f, 7f),
            BarEntry(6f, 3f)
        )
    }

    private fun getBarEntriesForNegativeFirst(): List<BarEntry> {
        return listOf(
            BarEntry(1f, 4f),
            BarEntry(2f, -6f),
            BarEntry(3f, 8f),
            BarEntry(4f, -2f),
            BarEntry(5f, 4f),
            BarEntry(6f, -1f)
        )
    }

    private fun getBarEntriesForNegativeSecond(): List<BarEntry> {
        return listOf(
            BarEntry(1f, 8f),
            BarEntry(2f, 12f),
            BarEntry(3f, -4f),
            BarEntry(4f, -1f),
            BarEntry(5f, 7f),
            BarEntry(6f, -3f)
        )
    }

}