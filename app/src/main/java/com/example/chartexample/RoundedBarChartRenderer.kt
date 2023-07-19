package com.example.chartexample

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlin.math.ceil
import kotlin.math.round

class RoundedBarChartRenderer(
    chart: BarDataProvider?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) :
    BarChartRenderer(chart, animator, viewPortHandler) {

    private val mBarShadowRectBuffer by lazy { RectF() }
    private var mRadius = 0
    private var mRadiusUnit: RoundedRadiusUnit = RoundedRadiusUnit.Actual

    private var commonBarWidth = 0

    fun setRadius(mRadius: Int) {
        this.mRadius = mRadius
    }

    fun setRadiusUnit(unit: RoundedRadiusUnit) {
        this.mRadiusUnit = unit
    }

    fun getIndividualBarWidth(): Int {
        return commonBarWidth
    }

    override fun drawDataSet(canvas: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        mBarBorderPaint.apply {
            color = dataSet.barBorderColor
            strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth)
        }
        mShadowPaint.apply {
            color = dataSet.barShadowColor
        }

        // Drawing shadow
        backgroundShadow(canvas = canvas, dataSet = dataSet)

        // initialize the buffer
        mBarBuffers[index].apply {
            bufferConfig(index = index, dataSet = dataSet, transformer = trans)
            drawPath(dataSet = dataSet, canvas = canvas)
        }
    }

    private fun backgroundShadow(canvas: Canvas, dataSet: IBarDataSet) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        if (mChart.isDrawBarShadowEnabled) {
            mShadowPaint.color = dataSet.barShadowColor
            val barData = mChart.barData
            val barWidth = barData.barWidth
            val barWidthHalf = barWidth / 2.0f
            var x: Float
            val count = ceil(
                (dataSet.entryCount.toFloat() * mAnimator.phaseX).toDouble().toInt().toDouble()
            ).coerceAtMost(dataSet.entryCount.toDouble()).toInt()

            for (i in 0 until count) {
                val e = dataSet.getEntryForIndex(i)
                x = e.x
                mBarShadowRectBuffer.left = x - barWidthHalf
                mBarShadowRectBuffer.right = x + barWidthHalf
                trans.rectValueToPixel(mBarShadowRectBuffer)
                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break
                mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()
                canvas.drawRoundRect(mBarRect, mRadius.toFloat(), mRadius.toFloat(), mShadowPaint)
            }
        }
    }

    private fun BarBuffer.bufferConfig(index: Int, dataSet: IBarDataSet, transformer: Transformer) {
        setPhases(mAnimator.phaseX, mAnimator.phaseY)
        setDataSet(index)
        setInverted(mChart.isInverted(dataSet.axisDependency))
        setBarWidth(mChart.barData.barWidth)
        feed(dataSet)
        transformer.pointValuesToPixel(buffer)
        val isSingleColor = dataSet.colors.size == 1
        if (isSingleColor) {
            mRenderPaint.color = dataSet.color
        }
    }

    private fun BarBuffer.drawPath(dataSet: IBarDataSet, canvas: Canvas) {
        val drawBorder = dataSet.barBorderWidth > 0f
        val isSingleColor = dataSet.colors.size == 1
        var j = 0
        while (j < buffer.size) {
            if (!mViewPortHandler.isInBoundsLeft(buffer[j + 2])) {
                j += 4
                continue
            }
            if (!mViewPortHandler.isInBoundsRight(buffer[j])) break
            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.color = dataSet.getColor(j / 4)
            }
            if (dataSet.gradientColor != null) {
                val gradientColor = dataSet.gradientColor
                mRenderPaint.shader = LinearGradient(
                    buffer[j],
                    buffer[j + 3],
                    buffer[j],
                    buffer[j + 1],
                    gradientColor.startColor,
                    gradientColor.endColor,
                    Shader.TileMode.MIRROR
                )
            }
            if (dataSet.gradientColors != null) {
                mRenderPaint.shader = LinearGradient(
                    buffer[j],
                    buffer[j + 3],
                    buffer[j],
                    buffer[j + 1],
                    dataSet.getGradientColor(j / 4).startColor,
                    dataSet.getGradientColor(j / 4).endColor,
                    Shader.TileMode.MIRROR
                )
            }
            val entry = dataSet.getEntryForIndex(j / 4)
            commonBarWidth = (buffer[j+2] - buffer[j]).toInt()
            val path2: Path = roundRect(
                rect = RectF(
                    buffer[j], buffer[j + 1], buffer[j + 2],
                    buffer[j + 3]
                ),
                tl = entry.y >= 0,
                tr = entry.y >= 0,
                br = entry.y < 0,
                bl = entry.y < 0
            )
            canvas.drawPath(path2, mRenderPaint)
            if (drawBorder) {
                val path: Path = roundRect(
                    rect = RectF(
                        buffer[j], buffer[j + 1], buffer[j + 2],
                        buffer[j + 3]
                    ),
                    tl = entry.y >= 0,
                    tr = entry.y >= 0,
                    br = entry.y < 0,
                    bl = entry.y < 0
                )
                canvas.drawPath(path, mBarBorderPaint)
            }
            j += 4
        }
    }

    private fun roundRect(
        rect: RectF,
        tl: Boolean,
        tr: Boolean,
        br: Boolean,
        bl: Boolean
    ): Path {
        val path = Path()
        val width = rect.right - rect.left
        val height = rect.bottom - rect.top
        val r = getRadius(radius = mRadius.toFloat(), width = width)
        val widthMinusCorners = width - 2 * r
        val heightMinusCorners = height - 2 * r
        path.moveTo(rect.right, rect.top + r)
        if (tr) path.rQuadTo(0f, -r, -r, -r) //top-right corner
        else {
            path.rLineTo(0f, -r)
            path.rLineTo(-r, 0f)
        }
        path.rLineTo(-widthMinusCorners, 0f)
        if (tl) path.rQuadTo(-r, 0f, -r, r) //top-left corner
        else {
            path.rLineTo(-r, 0f)
            path.rLineTo(0f, r)
        }
        path.rLineTo(0f, heightMinusCorners)
        if (bl) path.rQuadTo(0f, r, r, r) //bottom-left corner
        else {
            path.rLineTo(0f, r)
            path.rLineTo(r, 0f)
        }
        path.rLineTo(widthMinusCorners, 0f)
        if (br) path.rQuadTo(r, 0f, r, -r) //bottom-right corner
        else {
            path.rLineTo(r, 0f)
            path.rLineTo(0f, -r)
        }
        path.rLineTo(0f, -heightMinusCorners)
        path.close() //Given close, last lineto can be removed.
        return path
    }

    private fun getRadius(radius: Float, width: Float): Float {
        return when (mRadiusUnit) {
            is RoundedRadiusUnit.Percentage -> {
                width.times(mRadius.toFloat().div(100))
            }

            is RoundedRadiusUnit.Actual -> {
                radius
            }
        }
    }

    override fun drawHighlighted(canvas: Canvas?, indices: Array<out Highlight>?) {
        val barData = mChart.barData

        for (high in (indices ?: arrayOf())) {
            val set = barData.getDataSetByIndex(high.dataSetIndex)
            if (set == null || !set.isHighlightEnabled) continue
            val e = set.getEntryForXValue(high.x, high.y)
            if (!isInBoundsX(e, set)) continue
            val trans = mChart.getTransformer(set.axisDependency)
            mHighlightPaint.color = set.highLightColor
            mHighlightPaint.alpha = set.highLightAlpha
            val isStack = high.stackIndex >= 0 && e.isStacked
            val y1: Float
            val y2: Float
            if (isStack) {
                if (mChart.isHighlightFullBarEnabled) {
                    y1 = e.positiveSum
                    y2 = -e.negativeSum
                } else {
                    val range = e.ranges[high.stackIndex]
                    y1 = range.from
                    y2 = range.to
                }
            } else {
                y1 = e.y
                y2 = 0f
            }
            prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, trans)
            setHighlightDrawPos(high, mBarRect)

            val path = roundRect(
                rect = mBarRect,
                tl = e.y >= 0,
                tr = e.y >= 0,
                br = e.y < 0,
                bl = e.y < 0
            )
            canvas?.drawPath(path, mHighlightPaint)
        }
    }
}