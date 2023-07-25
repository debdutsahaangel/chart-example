package com.example.chartexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chartexample.databinding.ActivityMainBinding
import com.example.chartexample.datamodel.BarChartCombinedData
import com.example.chartexample.datamodel.BarChartIndividualDataSet
import com.example.chartexample.datamodel.GradientColors
import com.example.chartexample.datamodel.LineChartIndividualDataSet
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

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
        binding.combinedChart.apply {
            setXValueFormatter(formattedValues = listOf("17600", "17650", "17700", "17750", "17800", "17850"))
            setDataSet(
                dataSet = BarChartCombinedData(
                    barChartDataSet = listOf(
                        BarChartIndividualDataSet(
                            dataSet = BarDataSet(getBarEntriesForIndvFirst(), "Bar Chart 1"),
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
                    ),
                    lineChartDataSet = listOf(
                        LineChartIndividualDataSet(
                            dataSet = LineDataSet(getLineEntriesForCombinedFirst(), "Line Chart 1"),
                            lineColor = "#D7C9EF",
                            circleColor = "#581DBE",
                            fillColor = "#581DBE"
                        ),
                        LineChartIndividualDataSet(
                            dataSet = LineDataSet(getLineEntriesForCombinedSecond(), "Line Chart 2"),
                            lineColor = "#F9BA4D",
                            circleColor = "#581DBE",
                            fillColor = "#581DBE"
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

    private fun getLineEntriesForCombinedFirst(): List<Entry> {
        return listOf(
            Entry(0f, 15f),
            Entry(1f, 55f),
            Entry(2f, 15f),
            Entry(3f, 35f),
            Entry(4f, 15f),
            Entry(5f, 25f)
        )
    }

    private fun getLineEntriesForCombinedSecond(): List<Entry> {
        return listOf(
            Entry(0f, 26f),
            Entry(1f, 66f),
            Entry(2f, 26f),
            Entry(3f, 46f),
            Entry(4f, 26f),
            Entry(5f, 36f)
        )
    }

}