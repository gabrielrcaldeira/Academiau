package com.academiau.app.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.PathParser
import com.academiau.app.ui.theme.ColorLegs
import com.academiau.app.ui.theme.ColorPull
import com.academiau.app.ui.theme.ColorPush

@Composable
fun MuscleMap(
    activeSplit: String,
    modifier: Modifier = Modifier
) {
    val isPush = activeSplit == "Push"
    val isPull = activeSplit == "Pull"
    val isLegs = activeSplit == "Legs"

    // Colors
    val baseFillColor = Color(0xFF1E293B)
    val baseStrokeColor = Color(0xFF334155)
    
    val highlightColor = when {
        isPush -> ColorPush
        isPull -> ColorPull
        isLegs -> ColorLegs
        else -> baseFillColor
    }

    // SVG paths translated to Android Path data
    val shouldersLeftPathData = "M38 36 C34 36 30 40 30 46 C30 52 35 56 38 56 Z"
    val shouldersRightPathData = "M82 36 C86 36 90 40 90 46 C90 52 85 56 82 56 Z"
    val chestPathData = "M42 36 L78 36 L74 58 L46 58 Z"
    val bicepsLeftPathData = "M28 56 C26 56 24 64 26 74 C28 84 32 84 32 74 Z"
    val bicepsRightPathData = "M92 56 C94 56 96 64 94 74 C92 84 88 84 88 74 Z"
    val tricepsLeftPathData = "M32 56 L34 76 L38 76 L38 56 Z"
    val tricepsRightPathData = "M88 56 L86 76 L82 76 L82 56 Z"
    val backPathData = "M46 58 L74 58 L70 86 L50 86 Z"
    val hipsPathData = "M44 86 L76 86 L72 102 L48 102 Z"
    val quadsLeftPathData = "M44 102 L59 102 L56 142 L46 142 Z"
    val quadsRightPathData = "M61 102 L76 102 L74 142 L64 142 Z"
    val calvesLeftPathData = "M46 142 L55 142 L53 182 L48 182 Z"
    val calvesRightPathData = "M65 142 L74 142 L72 182 L67 182 Z"
    val forearmsLeftPathData = "M26 74 L22 105 L27 105 L32 74 Z"
    val forearmsRightPathData = "M94 74 L98 105 L93 105 L88 74 Z"

    Canvas(
        modifier = modifier
            .aspectRatio(120f / 220f)
            .fillMaxSize()
    ) {
        val scaleX = size.width / 120f
        val scaleY = size.height / 220f

        drawIntoCanvas { canvas ->
            val nativeCanvas = canvas.nativeCanvas
            
            // Save state for scaling
            nativeCanvas.save()
            nativeCanvas.scale(scaleX, scaleY)

            // Paint configurations
            val fillPaint = Paint().apply {
                style = Paint.Style.FILL
                isAntiAlias = true
            }

            val strokePaint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 1.5f
                color = baseStrokeColor.toArgb()
                isAntiAlias = true
            }

            // Function to draw path
            fun drawSvgPath(pathData: String, fillColor: Color) {
                try {
                    val path = PathParser.createPathFromPathData(pathData)
                    fillPaint.color = fillColor.toArgb()
                    nativeCanvas.drawPath(path, fillPaint)
                    nativeCanvas.drawPath(path, strokePaint)
                } catch (e: Exception) {
                    // Fallback
                }
            }

            // 1. Head (Circle cx=60, cy=20, r=10)
            fillPaint.color = baseFillColor.toArgb()
            nativeCanvas.drawCircle(60f, 20f, 10f, fillPaint)
            nativeCanvas.drawCircle(60f, 20f, 10f, strokePaint)

            // 2. Neck (Rect x=57, y=30, w=6, h=6)
            nativeCanvas.drawRect(57f, 30f, 63f, 36f, fillPaint)
            nativeCanvas.drawRect(57f, 30f, 63f, 36f, strokePaint)

            // 3. Shoulders (Highlight on Push)
            val shoulderColor = if (isPush) highlightColor else baseFillColor
            drawSvgPath(shouldersLeftPathData, shoulderColor)
            drawSvgPath(shouldersRightPathData, shoulderColor)

            // 4. Chest (Highlight on Push)
            val chestColor = if (isPush) highlightColor else baseFillColor
            drawSvgPath(chestPathData, chestColor)

            // 5. Biceps (Highlight on Pull)
            val bicepsColor = if (isPull) highlightColor else baseFillColor
            drawSvgPath(bicepsLeftPathData, bicepsColor)
            drawSvgPath(bicepsRightPathData, bicepsColor)

            // 6. Triceps (Highlight on Push)
            val tricepsColor = if (isPush) highlightColor else baseFillColor
            drawSvgPath(tricepsLeftPathData, tricepsColor)
            drawSvgPath(tricepsRightPathData, tricepsColor)

            // 7. Back (Highlight on Pull)
            val backColor = if (isPull) highlightColor else baseFillColor
            drawSvgPath(backPathData, backColor)

            // 8. Hips/Glutes (Highlight on Legs)
            val hipsColor = if (isLegs) highlightColor else baseFillColor
            drawSvgPath(hipsPathData, hipsColor)

            // 9. Quadriceps (Highlight on Legs)
            val quadsColor = if (isLegs) highlightColor else baseFillColor
            drawSvgPath(quadsLeftPathData, quadsColor)
            drawSvgPath(quadsRightPathData, quadsColor)

            // 10. Calves (Highlight on Legs)
            val calvesColor = if (isLegs) highlightColor else baseFillColor
            drawSvgPath(calvesLeftPathData, calvesColor)
            drawSvgPath(calvesRightPathData, calvesColor)

            // 11. Forearms (Always base color)
            drawSvgPath(forearmsLeftPathData, baseFillColor)
            drawSvgPath(forearmsRightPathData, baseFillColor)

            nativeCanvas.restore()
        }
    }
}
