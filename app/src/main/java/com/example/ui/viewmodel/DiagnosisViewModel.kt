package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.DiagnosisResult
import com.example.data.DiagnosisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class DiagnosisCategory(val displayName: String) {
    PHYSICAL("からだの充電 (身体面)"),
    MENTAL("きもちの吐き出し (精神面)"),
    TIME("じかんの引き算 (時間面)"),
    SUPPORT("周囲へのヘルプ (サポート面)"),
    COMPASSION("自分をゆるす (自己評価面)")
}

data class DiagnosisQuestion(
    val id: Int,
    val text: String,
    val category: DiagnosisCategory
)

data class AnswerOption(
    val text: String,
    val score: Int
)

class DiagnosisViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "diagnosis_database"
    ).build()

    private val repository = DiagnosisRepository(db.diagnosisDao())

    val historyState: StateFlow<List<DiagnosisResult>> = repository.allResults
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val questions = listOf(
        DiagnosisQuestion(1, "朝、起きた瞬間からすでに体が重い、疲れが取れていないと感じる。", DiagnosisCategory.PHYSICAL),
        DiagnosisQuestion(2, "子どものささいな行動（食べこぼし、泣くなど）に、つい大声で怒ってしまう。", DiagnosisCategory.MENTAL),
        DiagnosisQuestion(3, "自分のための時間（温かいお茶を飲む、趣味に没頭するなど）が1日15分すら取れない。", DiagnosisCategory.TIME),
        DiagnosisQuestion(4, "家事や育児を「すべて自分がやらなければ」と一人で抱え込んでしまう。", DiagnosisCategory.SUPPORT),
        DiagnosisQuestion(5, "疲れて休むことや、家事を手抜きすることに強い罪悪感を感じてしまう。", DiagnosisCategory.COMPASSION),
        DiagnosisQuestion(6, "最近、理由もなくイライラしたり、心から笑える瞬間が減っていると感じる。", DiagnosisCategory.MENTAL),
        DiagnosisQuestion(7, "周囲やパートナーに「助けて」と素直に言えない、頼るのが申し訳ないと感じる。", DiagnosisCategory.SUPPORT),
        DiagnosisQuestion(8, "常にやることリストに追われ、頭がフル回転でリラックスする時間がない。", DiagnosisCategory.TIME),
        DiagnosisQuestion(9, "ちょっとしたことで涙が出そうになったり、強い不安・孤独を感じることがある。", DiagnosisCategory.MENTAL),
        DiagnosisQuestion(10, "夜の睡眠時間が足りていない、または眠りが浅くて途中で目が覚めてしまう。", DiagnosisCategory.PHYSICAL)
    )

    val options = listOf(
        AnswerOption("とても当てはまる", 0),
        AnswerOption("やや当てはまる", 1),
        AnswerOption("あまり当てはまらない", 2),
        AnswerOption("全く当てはまらない", 3)
    )

    // Current diagnostic process state
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _answers = MutableStateFlow<Map<Int, Int>>(emptyMap()) // map of question.id to selected option index (0..3)
    val answers: StateFlow<Map<Int, Int>> = _answers.asStateFlow()

    private val _latestResult = MutableStateFlow<DiagnosisResult?>(null)
    val latestResult: StateFlow<DiagnosisResult?> = _latestResult.asStateFlow()

    fun selectAnswer(questionId: Int, optionIndex: Int) {
        val current = _answers.value.toMutableMap()
        current[questionId] = optionIndex
        _answers.value = current
    }

    fun nextQuestion(): Boolean {
        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value += 1
            return true
        }
        return false
    }

    fun prevQuestion(): Boolean {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
            return true
        }
        return false
    }

    fun resetDiagnostic() {
        _currentQuestionIndex.value = 0
        _answers.value = emptyMap()
        _latestResult.value = null
    }

    fun finishDiagnosis() {
        val answersMap = _answers.value
        var physicalSum = 0
        var mentalSum = 0
        var timeSum = 0
        var supportSum = 0
        var compassionSum = 0

        questions.forEach { q ->
            val optionIdx = answersMap[q.id] ?: 3 // default to fully leeway if not answered
            val score = options[optionIdx].score
            when (q.category) {
                DiagnosisCategory.PHYSICAL -> physicalSum += score
                DiagnosisCategory.MENTAL -> mentalSum += score
                DiagnosisCategory.TIME -> timeSum += score
                DiagnosisCategory.SUPPORT -> supportSum += score
                DiagnosisCategory.COMPASSION -> compassionSum += score
            }
        }

        val totalScore = physicalSum + mentalSum + timeSum + supportSum + compassionSum
        val percentage = ((totalScore / 30.0) * 100).toInt()

        val statusLabel = when {
            percentage >= 80 -> "満タン状態"
            percentage >= 50 -> "ちょっとお疲れ状態"
            percentage >= 30 -> "限界警報状態"
            else -> "緊急レスキュー状態"
        }

        val comment = when {
            percentage >= 80 -> "素晴らしい！心も体もエネルギーに満ちています。今のペースを維持しながら、ご自身へのご褒美も忘れないでくださいね。"
            percentage >= 50 -> "がんばり屋のサインが出ています。日々をなんとかこなせてはいますが、少しずつ自分のための時間を意識して取り入れていきましょう。"
            percentage >= 30 -> "心の余裕が黄色信号です。家事やタスクの優先順位を下げ、まずはご自身の休息を最優先にするタイミングです。周囲に頼ることも検討してください。"
            else -> "限界を超えています。心身ともに緊急SOSを出している状態です。家事はすべて後回しにし、今すぐ横になって体を休めてください。"
        }

        val newResult = DiagnosisResult(
            totalScore = totalScore,
            percentage = percentage,
            physicalScore = physicalSum,
            mentalScore = mentalSum,
            timeScore = timeSum,
            supportScore = supportSum,
            compassionScore = compassionSum,
            statusLabel = statusLabel,
            comment = comment
        )

        _latestResult.value = newResult

        // Save to Database asynchronously
        viewModelScope.launch {
            repository.insertResult(newResult)
        }
    }

    fun deleteResult(id: Int) {
        viewModelScope.launch {
            repository.deleteResultById(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.deleteAllResults()
        }
    }
}

class DiagnosisViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiagnosisViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiagnosisViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
