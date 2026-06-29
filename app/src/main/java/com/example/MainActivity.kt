package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.DiagnosisScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.DiagnosisViewModel
import com.example.ui.viewmodel.DiagnosisViewModelFactory

enum class AppScreen {
    Welcome,
    Diagnosis,
    Result,
    History
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent() {
    val application = androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
    val diagnosisViewModel: DiagnosisViewModel = viewModel(
        factory = DiagnosisViewModelFactory(application)
    )

    var currentScreen by remember { mutableStateOf(AppScreen.Welcome) }

    // Safe BackHandler routing
    BackHandler(enabled = currentScreen != AppScreen.Welcome) {
        when (currentScreen) {
            AppScreen.Diagnosis -> currentScreen = AppScreen.Welcome
            AppScreen.Result -> currentScreen = AppScreen.Welcome
            AppScreen.History -> currentScreen = AppScreen.Welcome
            else -> {}
        }
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "ScreenTransition",
        modifier = Modifier.fillMaxSize()
    ) { screen ->
        when (screen) {
            AppScreen.Welcome -> {
                WelcomeScreen(
                    onStartDiagnosis = {
                        diagnosisViewModel.resetDiagnostic()
                        currentScreen = AppScreen.Diagnosis
                    },
                    onViewHistory = {
                        currentScreen = AppScreen.History
                    }
                )
            }
            AppScreen.Diagnosis -> {
                DiagnosisScreen(
                    viewModel = diagnosisViewModel,
                    onDiagnosisFinished = {
                        currentScreen = AppScreen.Result
                    },
                    onBackToWelcome = {
                        currentScreen = AppScreen.Welcome
                    }
                )
            }
            AppScreen.Result -> {
                ResultScreen(
                    viewModel = diagnosisViewModel,
                    onRestart = {
                        diagnosisViewModel.resetDiagnostic()
                        currentScreen = AppScreen.Welcome
                    },
                    onViewHistory = {
                        currentScreen = AppScreen.History
                    }
                )
            }
            AppScreen.History -> {
                HistoryScreen(
                    viewModel = diagnosisViewModel,
                    onBack = {
                        currentScreen = AppScreen.Welcome
                    },
                    onStartNewDiagnosis = {
                        diagnosisViewModel.resetDiagnostic()
                        currentScreen = AppScreen.Diagnosis
                    }
                )
            }
        }
    }
}
