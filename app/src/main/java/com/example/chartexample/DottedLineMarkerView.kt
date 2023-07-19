package com.example.chartexample

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.lang.ref.WeakReference

abstract class DottedLineMarkerView constructor(context: Context) :
    FrameLayout(context), IMarker {

    private val mOffset2 = MPPointF()
    private var mWeakChart: WeakReference<Chart<*>>? = null

    private var inflatedView: View? = null


    fun setLayoutResource(res: Int) {
        setupLayoutResource(res)
    }

    /**
     * Sets the layout resource for a custom MarkerView.
     *
     * @param layoutResource
     */
    private fun setupLayoutResource(layoutResource: Int) {
        inflatedView = LayoutInflater.from(context).inflate(layoutResource, this)
        inflatedView?.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        inflatedView?.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        // measure(getWidth(), getHeight());
        inflatedView?.layout(0, 0, inflatedView?.measuredWidth ?: 0, inflatedView?.measuredHeight ?: 0)
    }

    fun setChartView(chart: Chart<*>) {
        mWeakChart = WeakReference(chart)
    }

    private val chartView: Chart<*>?
        get() = if (mWeakChart == null) null else mWeakChart?.get()

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val offset = offset
        mOffset2.x = offset.x
        mOffset2.y = offset.y
        val chart = chartView
        val width = width.toFloat()
        val height = height.toFloat()
        if (posX + mOffset2.x < 0) {
            mOffset2.x = -posX
        } else if (chart != null && posX + width + mOffset2.x > chart.width) {
            mOffset2.x = chart.width - posX - width
        }
        if (posY + mOffset2.y < 0) {
            mOffset2.y = -posY
        } else if (chart != null && posY + height + mOffset2.y > chart.height) {
            mOffset2.y = chart.height - posY - height
        }
        return mOffset2
    }

    fun forceLayoutMeasurement(width: Int, height: Int) {
        measure(
            MeasureSpec.makeMeasureSpec(width, width),
            MeasureSpec.makeMeasureSpec(height, height)
        )
        layout(0, 0, measuredWidth, measuredHeight)
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        val offset = getOffsetForDrawingAtPoint(posX, posY)
        val saveId = canvas.save()
        // translate to the correct position and draw
        canvas.translate(posX + offset.x, posY + offset.y)
        draw(canvas)
        canvas.restoreToCount(saveId)
    }
}