package com.example.chartexample.markers

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RelativeLayout
import com.example.chartexample.datamodel.Margin
import com.example.chartexample.renderer.RoundedBarChartRenderer
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

open class DottedLineMarkerView constructor(context: Context) :
    RelativeLayout(context), IMarker {

    private val mOffset2 = MPPointF()
    private var mWeakChart: WeakReference<Chart<*>>? = null

    private var inflatedView: View? = null

    private var margin = Margin()

    private val isAlreadyRefreshed by lazy { AtomicBoolean(false) }

    fun setLayoutResource(res: Int) {
        setupLayoutResource(res)
    }

    fun setMargin(margin: Margin) {
        this.margin = margin
    }

    override fun getOffset(): MPPointF {
        val barWidth = (chartView?.renderer as? RoundedBarChartRenderer)?.getIndividualBarWidth() ?: 0
        val chartViewHighlight = chartView?.highlighted?.getOrNull(0)
        val translate = if (chartViewHighlight?.dataSetIndex == 0) 1 else -1
        return MPPointF(barWidth.div(2).times(translate).minus(width.div(2)).toFloat(),  0f)
    }

    /**
     * Sets the layout resource for a custom MarkerView.
     *
     * @param layoutResource
     */
    private fun setupLayoutResource(layoutResource: Int) {
        inflatedView = LayoutInflater.from(context).inflate(layoutResource, this)
        inflatedView?.apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            layout(0, 0, measuredWidth, measuredHeight)
        }
    }

    private fun forceLayoutChange() {
        val exactHeight = chartView?.viewPortHandler?.contentBottom()?.minus(margin.top) ?: 0f
        if (isAlreadyRefreshed.get()) return
        inflatedView?.apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(exactHeight.toInt(), MeasureSpec.EXACTLY))
            layout(0, 0, measuredWidth, measuredHeight)
        }
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

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        val offset = getOffsetForDrawingAtPoint(posX, posY)
        val saveId = canvas.save()
        // translate to the correct position and draw
        canvas.translate(posX + offset.x + margin.left.toFloat(), margin.top.toFloat())
        draw(canvas)
        canvas.restoreToCount(saveId)
    }

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        forceLayoutChange()
    }
}