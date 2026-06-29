package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.DiagnosisCategory
import com.example.ui.viewmodel.DiagnosisViewModel

@Composable
fun DiagnosisScreen(
    viewModel: DiagnosisViewModel,
    onDiagnosisFinished: () -> Unit,
    onBackToWelcome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentIdx by viewModel.currentQuestionIndex.collectAsState()
    val answers by viewModel.answers.collectAsState()

    val currentQuestion = viewModel.questions[currentIdx]
    val selectedOptionIdx = answers[currentQuestion.id]

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFFFBF7),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (currentIdx > 0) {
                            viewModel.prevQuestion()
                        } else {
                            onBackToWelcome()
                        }
                    },
                    modifier = Modifier.testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "戻る",
                        tint = Color(0xFF8D6E63)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "心の余裕診断中",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress Bar
            val progress = (currentIdx + 1).toFloat() / viewModel.questions.size
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "質問 ${currentIdx + 1} / ${viewModel.questions.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8D6E63)
                )
                Text(
                    text = "${((currentIdx + 1).toFloat() / viewModel.questions.size * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE57373)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFFE57373),
                trackColor = Color(0xFFFFEBEE),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Animated content container for smooth question transitions
            AnimatedContent(
                targetState = currentQuestion,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "QuestionTransition"
            ) { targetQuestion ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Category Badge
                    val categoryColor = when (targetQuestion.category) {
                        DiagnosisCategory.PHYSICAL -> Color(0xFF81C784) // Green
                        DiagnosisCategory.MENTAL -> Color(0xFF64B5F6)   // Blue
                        DiagnosisCategory.TIME -> Color(0xFFFFB74D)     // Amber
                        DiagnosisCategory.SUPPORT -> Color(0xFFBA68C8)  // Purple
                        DiagnosisCategory.COMPASSION -> Color(0xFFF06292) // Pink
                    }

                    Surface(
                        color = categoryColor.copy(alpha = 0.15f),
                        contentColor = categoryColor,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = categoryColor
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = targetQuestion.category.displayName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Question Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = targetQuestion.text,
                            style = MaterialTheme.typography.titleLarge.copy(
                                lineHeight = 28.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF5D4037),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp, horizontal = 24.dp)
                        )
                    }

                    // Options list
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.options.forEachIndexed { optIdx, option ->
                            val isSelected = selectedOptionIdx == optIdx
                            val optionBgColor = if (isSelected) Color(0xFFFFEBEE) else Color.White
                            val optionBorderColor = if (isSelected) Color(0xFFE57373) else Color(0xFFE0E0E0)
                            val optionTextColor = if (isSelected) Color(0xFFC62828) else Color(0xFF5D4037)

                            Surface(
                                onClick = {
                                    viewModel.selectAnswer(targetQuestion.id, optIdx)
                                    // Slight delay or direct advance
                                    if (currentIdx < viewModel.questions.size - 1) {
                                        viewModel.nextQuestion()
                                    }
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = optionBgColor,
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = optionBorderColor
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("option_button_$optIdx"),
                                shadowElevation = if (isSelected) 1.dp else 0.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 18.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Circle Check indicator
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) Color(0xFFE57373) else Color(0xFFF5F5F5),
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Text(
                                        text = option.text,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = optionTextColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Navigation at Bottom
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                if (currentIdx > 0) {
                    OutlinedButton(
                        onClick = { viewModel.prevQuestion() },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF8D6E63)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFBCAAA4))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "前へ"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("前の質問へ")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp)) // Placeholder
                }

                // Final Finish Button (Only shown when all 10 answered or at the 10th index)
                if (currentIdx == viewModel.questions.size - 1) {
                    val allAnswered = answers.size == viewModel.questions.size
                    Button(
                        onClick = {
                            viewModel.finishDiagnosis()
                            onDiagnosisFinished()
                        },
                        enabled = allAnswered,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE57373),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("view_results_button")
                    ) {
                        Text(
                            text = "診断結果をみる",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "次へ"
                        )
                    }
                } else {
                    // Next Button (if already answered, can advance)
                    val currentAnswered = selectedOptionIdx != null
                    if (currentAnswered) {
                        OutlinedButton(
                            onClick = { viewModel.nextQuestion() },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFE57373)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFE57373))
                        ) {
                            Text("次へ")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "次へ"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
