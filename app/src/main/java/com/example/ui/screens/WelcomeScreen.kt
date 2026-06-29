package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.geometry.Offset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onStartDiagnosis: () -> Unit,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFFFBF7) // Warm organic cream
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Subtitle
            Text(
                text = "忙しい毎日に立ち止まる、5分間の寄り添い時間",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF9E7C66), // Soothing soft brown
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main Title
            Text(
                text = "ママの余裕\n見える化診断",
                style = MaterialTheme.typography.displayMedium.copy(
                    lineHeight = 46.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color(0xFFE57373), // Warm coral red
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Calm Interactive Graphics (Decorative Flower)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {
                CalmingFlowerAnimation()
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Intro Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "気づかないうちに削れている\n「ママの心の余裕」を測定。",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF5D4037), // Warm deep brown
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "10個の簡単な質問に答えるだけで、あなたの心の「余白」を数値化し、本当に必要なセルフケアの優先順位をアドバイスします。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF795548),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Beautiful Today's Moon Phase Card
            val now = System.currentTimeMillis()
            val currentPhase = MoonHelper.getMoonPhase(now)
            val nextNewMoon = MoonHelper.getNextNewMoonTimestamp(now)
            val nextFullMoon = MoonHelper.getNextFullMoonTimestamp(now)
            
            val daysToNew = MoonHelper.daysUntil(nextNewMoon, now)
            val daysToFull = MoonHelper.daysUntil(nextFullMoon, now)

            var isMoonExpanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFDF6F0) // Extra soft warm skin-sand
                ),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1E3D3))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MoonCanvas(
                            phase = currentPhase,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🌕 今日の月のリズム 🌕",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE57373) // Corresponds with coral highlight
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentPhase.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF5D4037)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentPhase.recommendedGeneralAction,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF795548),
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Simple Box-based custom divider (completely version-safe)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFEFE5DB))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Next New Moon and Full Moon dynamic dates
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🌑 次の新月 (デトックス開始)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8D6E63)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${MoonHelper.formatDate(nextNewMoon)} (あと${daysToNew}日)",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF5D4037)
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🌕 次の満月 (感情と緊張のピーク)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8D6E63)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${MoonHelper.formatDate(nextFullMoon)} (あと${daysToFull}日)",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF5D4037)
                            )
                        }
                    }

                    // Interactive details toggle
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isMoonExpanded = !isMoonExpanded }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isMoonExpanded) "お月様のメッセージを閉じる" else "お月様からの今日のアドバイスを詳しく見る",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFE57373)
                            )
                            Icon(
                                imageVector = if (isMoonExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color(0xFFE57373),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    if (isMoonExpanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1E3D3))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "💡 今の月のフェーズの特徴：",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFE57373)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentPhase.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF5D4037),
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "お母さんへのプチご自愛アドバイス：",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF8D6E63)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "お月様のリズムに合わせて、無理に動こうとせず自分の心と身体を一番に労ってあげてくださいね。各フェーズに応じた詳細なパーソナル診断アドバイスは「診断スタート」から受け取ることができます。",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF795548),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Action Buttons
            Button(
                onClick = onStartDiagnosis,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("start_diagnosis_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE57373),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 1.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "診断スタート"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "診断をスタートする",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onViewHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("history_button"),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF8D6E63)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.5.dp
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "履歴を見る"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "過去の診断履歴を見る",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

/**
 * Beautiful rotating/pulsing decorative graphic that represents peace of mind.
 */
@Composable
fun CalmingFlowerAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "CalmFlower")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(180.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .rotate(rotation)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val petalCount = 8
            val petalRadius = size.width / 4f

            for (i in 0 until petalCount) {
                val angle = (i * (360f / petalCount)) * (Math.PI / 180f)
                val petalCenter = Offset(
                    (center.x + Math.cos(angle) * petalRadius).toFloat(),
                    (center.y + Math.sin(angle) * petalRadius).toFloat()
                )

                // Alternate between soft coral and soft pastel orange/yellow
                val brush = Brush.radialGradient(
                    colors = if (i % 2 == 0) {
                        listOf(Color(0xFFFFCDD2), Color(0xFFFF8A80))
                    } else {
                        listOf(Color(0xFFFFECB3), Color(0xFFFFB74D))
                    },
                    center = petalCenter,
                    radius = petalRadius
                )

                drawCircle(
                    brush = brush,
                    radius = petalRadius,
                    center = petalCenter,
                    alpha = 0.45f
                )
            }

            // Central core glowing light
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, Color(0xFFFFF9C4), Color(0x00FFFFFF)),
                    center = center,
                    radius = size.width / 3f
                ),
                radius = size.width / 3f
            )
        }
    }
}
