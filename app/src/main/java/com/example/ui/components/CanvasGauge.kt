package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DiagnosisResult
import com.example.ui.viewmodel.DiagnosisCategory

/**
 * A beautiful, animated semi-circular gauge displaying the leeway percentage.
 */
@Composable
fun LeewayGauge(
    percentage: Int,
    modifier: Modifier = Modifier
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(percentage) {
        animatedProgress.animateTo(
            targetValue = percentage / 100f,
            animationSpec = tween(durationMillis = 1200)
        )
    }

    // Dynamic colors based on the percentage
    val (primaryColor, secondaryColor) = when {
        percentage >= 80 -> Pair(Color(0xFF81C784), Color(0xFF4CAF50)) // Sage/Mint Green
        percentage >= 50 -> Pair(Color(0xFF64B5F6), Color(0xFF1E88E5)) // Soft Blue
        percentage >= 30 -> Pair(Color(0xFFFFB74D), Color(0xFFFB8C00)) // Amber/Orange
        else -> Pair(Color(0xFFE57373), Color(0xFFE53935)) // Coral Red (Depleted)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val sweepAngle = 240f
            val startAngle = 150f
            val strokeWidth = 18.dp.toPx()
            val diameter = size.width - strokeWidth
            val radius = diameter / 2f
            val centerOffset = Offset(size.width / 2f, size.height / 2f)

            // Background arc (gray track)
            drawArc(
                color = Color(0xFFECEFF1),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(diameter, diameter),
                topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
            )

            // Foreground animated arc with gradient
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(primaryColor, secondaryColor, primaryColor),
                    center = centerOffset
                ),
                startAngle = startAngle,
                sweepAngle = sweepAngle * animatedProgress.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(diameter, diameter),
                topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
            )
        }

        // Inner percentage display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text(
                text = "心のゆとり",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = percentage.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 54.sp),
                    fontWeight = FontWeight.ExtraBold,
                    color = secondaryColor
                )
                Text(
                    text = "%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = secondaryColor,
                    modifier = Modifier.padding(bottom = 8.dp, start = 2.dp)
                )
            }
        }
    }
}

/**
 * A breakdown list showing leeway progress bars for each dimension.
 */
@Composable
fun DimensionBars(
    result: DiagnosisResult,
    modifier: Modifier = Modifier
) {
    val dimensions = listOf(
        Pair("身体 (からだの充電)", result.physicalScore to 6),
        Pair("精神 (きもちの吐き出し)", result.mentalScore to 9),
        Pair("時間 (じかんの引き算)", result.timeScore to 6),
        Pair("サポート (周囲へのヘルプ)", result.supportScore to 6),
        Pair("自己評価 (自分をゆるす)", result.compassionScore to 3)
    )

    Column(modifier = modifier) {
        dimensions.forEach { (name, scorePair) ->
            val (score, max) = scorePair
            val ratio = score.toFloat() / max
            val percent = (ratio * 100).toInt()

            // Custom color depending on the depletion level
            val barColor = when {
                percent >= 80 -> Color(0xFF81C784)
                percent >= 50 -> Color(0xFF64B5F6)
                percent >= 30 -> Color(0xFFFFB74D)
                else -> Color(0xFFE57373)
            }

            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "$percent%",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = barColor
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Beautiful custom canvas progress bar
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val radius = h / 2f

                    // Draw background track
                    drawRoundRect(
                        color = Color(0xFFECEFF1),
                        size = Size(w, h),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius)
                    )

                    // Draw progress bar
                    if (ratio > 0f) {
                        drawRoundRect(
                            color = barColor,
                            size = Size(w * ratio, h),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius)
                        )
                    }
                }
            }
        }
    }
}

/**
 * An elegant trend line chart showing past diagnosis results.
 */
@Composable
fun HistoryTrendChart(
    results: List<DiagnosisResult>,
    modifier: Modifier = Modifier
) {
    if (results.size < 2) {
        // Not enough data to draw a trend line
        return
    }

    // Take up to 7 most recent results and reverse to draw left-to-right (chronological)
    val sortedResults = results.take(7).reversed()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            val width = size.width
            val height = size.height
            val paddingX = 40f
            val paddingY = 30f
            val chartWidth = width - (paddingX * 2)
            val chartHeight = height - (paddingY * 2)

            val maxPoints = sortedResults.size
            val stepX = if (maxPoints > 1) chartWidth / (maxPoints - 1) else chartWidth

            val points = sortedResults.mapIndexed { idx, res ->
                val x = paddingX + (idx * stepX)
                // Percentage is 0..100, maps to height from bottom up
                val y = paddingY + chartHeight - (res.percentage / 100f * chartHeight)
                Offset(x, y)
            }

            // Draw grid lines
            val gridLines = 4
            val gridStepY = chartHeight / gridLines
            for (i in 0..gridLines) {
                val y = paddingY + (i * gridStepY)
                drawLine(
                    color = Color(0xFFECEFF1),
                    start = Offset(paddingX, y),
                    end = Offset(width - paddingX, y),
                    strokeWidth = 1f
                )
            }

            // Draw connecting path
            val path = Path().apply {
                if (points.isNotEmpty()) {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val curr = points[i]
                        // Draw elegant cubic connection curves
                        val controlX = (prev.x + curr.x) / 2f
                        cubicTo(
                            controlX, prev.y,
                            controlX, curr.y,
                            curr.x, curr.y
                        )
                    }
                }
            }

            // Draw line with gradient stroke
            drawPath(
                path = path,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFF8A80), Color(0xFF64B5F6))
                ),
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw nodes and percentage labels
            points.forEachIndexed { index, point ->
                val percentage = sortedResults[index].percentage
                // Draw glow shadow
                drawCircle(
                    color = Color(0x33FF8A80),
                    radius = 8.dp.toPx(),
                    center = point
                )
                // Draw inner circle
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = point
                )
                drawCircle(
                    color = Color(0xFFFF8A80),
                    radius = 3.dp.toPx(),
                    center = point
                )
            }
        }
    }
}
