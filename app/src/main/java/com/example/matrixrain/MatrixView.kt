package com.example.matrixrain

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class MatrixView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$%^&*".map { it.toString() }

    private val fontSize = 40
    private var columns = 0
    private lateinit var drops: IntArray
    private val streamLength = 10  // Number of characters per column

    private val paint = Paint().apply {
        color = Color.GREEN
        textSize = fontSize.toFloat()
        isAntiAlias = true
        typeface = android.graphics.Typeface.MONOSPACE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        columns = w / fontSize
        drops = IntArray(columns) { Random.nextInt(h / fontSize) }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)

        for (i in 0 until columns) {
            for (j in 0 until streamLength) {
                val yPos = (drops[i] - j) * fontSize
                if (yPos < 0) continue

                // Optional fading effect
                val alpha = ((255f / streamLength) * (streamLength - j)).toInt().coerceIn(50, 255)
                paint.alpha = alpha

                val char = characters.random()
                canvas.drawText(char, (i * fontSize).toFloat(), yPos.toFloat(), paint)
            }

            if (drops[i] * fontSize > height && Random.nextInt(100) > 95) {
                drops[i] = 0
            }
            drops[i]++
        }

        postInvalidateDelayed(50)
    }
}
