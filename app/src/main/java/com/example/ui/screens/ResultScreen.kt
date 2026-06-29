package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DiagnosisResult
import com.example.ui.components.DimensionBars
import com.example.ui.components.LeewayGauge
import com.example.ui.viewmodel.DiagnosisCategory
import com.example.ui.viewmodel.DiagnosisViewModel

data class PriorityItem(
    val category: DiagnosisCategory,
    val score: Int,
    val maxScore: Int,
    val percentage: Int,
    val rank: Int,
    val title: String,
    val description: String,
    val action: String
)

@Composable
fun ResultScreen(
    viewModel: DiagnosisViewModel,
    onRestart: () -> Unit,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val latestResult by viewModel.latestResult.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFFFBF7)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val result = latestResult
            if (result == null) {
                // Safe Fallback if null
                Text(
                    text = "診断結果が見つかりません。",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = onRestart) {
                    Text("ホームに戻る")
                }
            } else {
                // App Logo or Header
                Text(
                    text = "あなたの診断結果",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8D6E63),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Beautiful Circular Gauge
                LeewayGauge(
                    percentage = result.percentage,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Status Badge
                val badgeColor = when {
                    result.percentage >= 80 -> Color(0xFF81C784)
                    result.percentage >= 50 -> Color(0xFF64B5F6)
                    result.percentage >= 30 -> Color(0xFFFFB74D)
                    else -> Color(0xFFE57373)
                }

                Surface(
                    color = badgeColor,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.padding(vertical = 12.dp),
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text = "診断：${result.statusLabel}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Commentary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ママへのメッセージ",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE57373)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = result.comment,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = Color(0xFF5D4037),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Section Title: Detailed breakdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color(0xFFE57373),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "5つの余裕の「見える化」",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    DimensionBars(
                        result = result,
                        modifier = Modifier.padding(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Section Title: Self Care Priorities
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFE57373),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "あなたに必要なセルフケア優先順位",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Calculate priorities: sort dimensions by percentage ascending
                val priorityList = calculatePriorities(result)

                priorityList.forEach { item ->
                    PriorityCard(item = item)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Moon Rhythm & Deep Self-Care Card
                val topCategory = priorityList.firstOrNull()?.category ?: DiagnosisCategory.PHYSICAL
                MoonSelfCareCard(category = topCategory)

                Spacer(modifier = Modifier.height(32.dp))

                // Action Row
                Button(
                    onClick = onViewHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("history_button_from_results"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8D6E63),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "履歴")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "診断履歴・変化をみる",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("restart_diagnosis_button"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE57373)
                    ),
                    border = BorderStroke(1.5.dp, Color(0xFFE57373)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "もう一度")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "もう一度診断する",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun PriorityCard(item: PriorityItem) {
    val rankBadgeColor = when (item.rank) {
        1 -> Color(0xFFE57373) // High priority Red
        2 -> Color(0xFFFFB74D) // Medium priority Amber
        else -> Color(0xFF90A4AE) // Low priority Grey
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        border = if (item.rank == 1) BorderStroke(1.5.dp, Color(0xFFFFCDD2)) else null
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: Rank and Category Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = rankBadgeColor,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "${item.rank}位",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "残量 ${item.percentage}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = rankBadgeColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Explanation
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = Color(0xFF795548)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Custom Action Suggestion Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFDE7), shape = RoundedCornerShape(12.dp)) // Gentle yellow highlight
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "セルフケア",
                        tint = Color(0xFFFBC02D),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "今日おすすめの小さなセルフケア",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8D6E63)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.action,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Calculates and ranks the priorities of dimensions from lowest percentage to highest percentage.
 */
fun calculatePriorities(result: DiagnosisResult): List<PriorityItem> {
    val items = listOf(
        Pair(DiagnosisCategory.PHYSICAL, result.physicalScore to 6),
        Pair(DiagnosisCategory.MENTAL, result.mentalScore to 9),
        Pair(DiagnosisCategory.TIME, result.timeScore to 6),
        Pair(DiagnosisCategory.SUPPORT, result.supportScore to 6),
        Pair(DiagnosisCategory.COMPASSION, result.compassionScore to 3)
    )

    // Calculate percentage for each and sort ascending
    val sorted = items.map { (cat, scorePair) ->
        val (score, max) = scorePair
        val percent = ((score.toFloat() / max) * 100).toInt()
        Triple(cat, scorePair, percent)
    }.sortedBy { it.third } // lowest first

    val metadata = mapOf(
        DiagnosisCategory.PHYSICAL to Triple(
            "身体のケア (からだの充電)",
            "睡眠不足や肉体的疲労が最も深刻なレベルです。今は小難しい心の整え方よりも、物理的に睡眠を取ったり横になって体を休めることが、他の何よりも最優先です。",
            "すべての予定をキャンセルし、お惣菜を買って家事をサボり、子どもと一緒に今すぐ20分横になりましょう。"
        ),
        DiagnosisCategory.MENTAL to Triple(
            "感情のケア (きもちの吐き出し)",
            "イライラやモヤモヤ、孤独感が限界に達し、感情のコップが溢れかけています。自分のつらい気持ちを否定せず、外に吐き出す「感情のアウトプット」が必要です。",
            "ノートや真っ白な紙に、誰にも見せないつもりで今の愚痴や怒りを殴り書きし、最後にビリビリに破り捨てましょう。"
        ),
        DiagnosisCategory.TIME to Triple(
            "時間の余白づくり (じかんの引き算)",
            "常にタスクに追われ、脳が興奮状態でリラックスできていません。スケジュールや家事の「やること」を強制的に減らし、時間のゆとりを確保してください。",
            "今日やる予定だった家事（掃除やアイロンなど）を1つ、明日に完全に回して「やらない」と決めましょう。"
        ),
        DiagnosisCategory.SUPPORT to Triple(
            "頼る力のケア (周囲へのヘルプ)",
            "一人で何もかも抱え込むワンオペ状態、または「頼るのは甘え」という固定観念が首をしめています。周囲の協力や便利なサービスを借りる練習の時です。",
            "パートナーや身内に「具体的にこれ（例：15分だけ子どもをみてて）をやって」とお願いするか、地域の預かりサービスを調べてみましょう。"
        ),
        DiagnosisCategory.COMPASSION to Triple(
            "自愛のケア (自分をゆるす)",
            "自分に対する採点基準が厳しく、家事や育児を完璧にできない自分を責めがちです。まずは「今日子どもを生かしているだけで100点満点」と自分を許してください。",
            "寝る前に「今日も生きてるだけで最高！えらい！」と声に出して自分をハグして褒めてあげましょう。"
        )
    )

    return sorted.mapIndexed { index, (cat, scorePair, percent) ->
        val (score, max) = scorePair
        val meta = metadata[cat] ?: Triple("セルフケア", "セルフケアを優先しましょう。", "今日一歩踏み出してみましょう。")
        PriorityItem(
            category = cat,
            score = score,
            maxScore = max,
            percentage = percent,
            rank = index + 1,
            title = meta.first,
            description = meta.second,
            action = meta.third
        )
    }
}

@Composable
fun MoonSelfCareCard(category: DiagnosisCategory, modifier: Modifier = Modifier) {
    val todayPhase = remember { MoonHelper.getMoonPhase(System.currentTimeMillis()) }
    var selectedPhase by remember { mutableStateOf(todayPhase) }
    var activeCategory by remember(category) { mutableStateOf(category) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF6F0)), // Soft sand-skin
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.5.dp, Color(0xFFF1E3D3))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌙 月のリズムとご自愛アドバイス 🌙",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE57373),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "お母さんの体調や感情は、月の満ち欠け（引力）と深く結びついています。現在の課題や気になるカテゴリーを選び、今宵の月のリズムと掛け合わせた、具体的で丁寧なセルフケアプランをチェックしてみましょう。",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF795548),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Tab Row (Interactive Category Selection)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFE5DB).copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DiagnosisCategory.values().forEach { cat ->
                    val isSelectedCat = activeCategory == cat
                    val catLabel = when (cat) {
                        DiagnosisCategory.PHYSICAL -> "からだ"
                        DiagnosisCategory.MENTAL -> "きもち"
                        DiagnosisCategory.TIME -> "じかん"
                        DiagnosisCategory.SUPPORT -> "ヘルプ"
                        DiagnosisCategory.COMPASSION -> "ゆるす"
                    }
                    val catIcon = when (cat) {
                        DiagnosisCategory.PHYSICAL -> "🩺"
                        DiagnosisCategory.MENTAL -> "🧠"
                        DiagnosisCategory.TIME -> "⏳"
                        DiagnosisCategory.SUPPORT -> "🤝"
                        DiagnosisCategory.COMPASSION -> "🌸"
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeCategory = cat }
                            .background(
                                color = if (isSelectedCat) Color(0xFFE57373) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(vertical = 8.dp, horizontal = 2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = catIcon,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = catLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSelectedCat) Color.White else Color(0xFF795548),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MoonPhase.values().forEach { phase ->
                    val isSelected = selectedPhase == phase
                    val isToday = todayPhase == phase

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedPhase = phase }
                            .background(
                                color = if (isSelected) Color(0xFFE57373).copy(alpha = 0.12f) else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = phase.iconChar,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = phase.displayName.substringBefore(" ("),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                            color = if (isSelected) Color(0xFFE57373) else Color(0xFF8D6E63)
                        )
                        if (isToday) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                color = Color(0xFFE57373),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "今日の月",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Content Card
            val advice = MoonHelper.getCombinedMoonAdvice(activeCategory, selectedPhase)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFF1E3D3))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MoonCanvas(
                            phase = selectedPhase,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = advice.first,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFE57373)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "お月様のテーマ: ${selectedPhase.displayName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF8D6E63),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = advice.second,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF5D4037),
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tip box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFDE7), shape = RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "💡 この時期の月のエネルギー",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF8D6E63)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedPhase.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF795548),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
