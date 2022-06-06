package com.mrx.paintapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.mrx.paintapplication.MainActivity.Companion.paintBrush
import com.mrx.paintapplication.MainActivity.Companion.path


class PaintView : View {

    var params : ViewGroup.LayoutParams? = null

    companion object {
        var pathList = ArrayList<Path>()
        var colorList = ArrayList<Int>()
        var currentBrushColor = Color.BLACK
    }

    // constructors from https://antonioleiva.com/custom-views-android-kotlin :
    constructor(context: Context) : this(context, null){
        initCurrentColorBrush()
    }
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0){
        initCurrentColorBrush()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initCurrentColorBrush()
    }

    private fun initCurrentColorBrush(){
        paintBrush.isAntiAlias = true
        paintBrush.color = currentBrushColor
        paintBrush.style = Paint.Style.STROKE
        paintBrush.strokeJoin = Paint.Join.ROUND
        paintBrush.strokeWidth = 10f;

        params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    // register fingers on the screen:
    override fun onTouchEvent(event: MotionEvent): Boolean { // changed MotionEvent to not-nullable
        var x = event.x
        var y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                pathList.add(path)
                colorList.add(currentBrushColor)
            }
            else -> return false
        }
        postInvalidate() // informs that changes was made in ui
        return false
    }

    override fun onDraw(canvas: Canvas) { // changed Canvas to not-nullable
        for(i in pathList.indices) {
            paintBrush.setColor(colorList[i])
            canvas.drawPath(pathList[i], paintBrush)
            invalidate() // informs that changes in ui was made
        }
    }

}
