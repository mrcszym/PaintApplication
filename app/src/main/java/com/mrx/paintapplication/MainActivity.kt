package com.mrx.paintapplication

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mrx.paintapplication.PaintView.Companion.colorList
import com.mrx.paintapplication.PaintView.Companion.currentBrushColor
import com.mrx.paintapplication.PaintView.Companion.pathList


class MainActivity : AppCompatActivity() {

    companion object { // like public static
        var path = Path()
        var paintBrush = Paint()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val violetBtn = findViewById<ImageButton>(R.id.violetColor)
        val pinkBtn = findViewById<ImageButton>(R.id.pinkColor)
        val navyBtn = findViewById<ImageButton>(R.id.navyColor)
        val blackBtn = findViewById<ImageButton>(R.id.blackColor)
        val sponge = findViewById<Button>(R.id.sponge) // eraser

        violetBtn.setOnClickListener {
            paintBrush.color = Color.parseColor("#7c1091")
            currentColor(paintBrush.color)
        }
        pinkBtn.setOnClickListener {
            paintBrush.color = Color.parseColor("#d40f8b")
            currentColor(paintBrush.color)
        }
        navyBtn.setOnClickListener {
            paintBrush.color = Color.parseColor("#1b2e96")
            currentColor(paintBrush.color)
        }
        blackBtn.setOnClickListener {
            paintBrush.color = Color.BLACK
            currentColor(paintBrush.color)
        }
        sponge.setOnClickListener {
            val toast = Toast.makeText(this, "All erased", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()

            pathList.clear()
            colorList.clear()
            path.reset()
        }
    }

    private fun currentColor(color: Int) {
        currentBrushColor = color
        path = Path() // without new()
    }
}