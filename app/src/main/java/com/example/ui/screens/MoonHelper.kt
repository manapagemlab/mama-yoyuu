package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.DiagnosisCategory

enum class MoonPhase(
    val displayName: String,
    val iconChar: String,
    val description: String,
    val recommendedGeneralAction: String
) {
    NEW_MOON(
        "新月 (ニュームーン)",
        "🌑",
        "リセット・浄化・新しいスタートのタイミング。心身ともにエネルギーが最も内側に向くため、静かに休み、不要なものを手放すのに最適です。",
        "スマホを切り、温かいハーブティーを飲みながら、自分だけの静寂を楽しみましょう。"
    ),
    WAXING_MOON(
        "満ちていく月 (アクティブ期)",
        "🌓",
        "吸収・成長・エネルギーを蓄えるタイミング。少しずつ活力が湧いてくるため、身体に良い栄養を取り入れ、軽いストレッチでエネルギーの循環を助けましょう。",
        "栄養満点の食事や、好きなアロマを焚いて自分に元気をチャージ。"
    ),
    FULL_MOON(
        "満月 (フルムーン)",
        "🌕",
        "吸収力が最大、感情が高ぶる、放出のタイミング。心も体もパンパンに満たされているため、緊張しやすくイライラもしがち。リラックスを最優先に。",
        "湯船にゆっくり浸かる、深呼吸をするなど、高ぶった神経を緩めてあげてください。"
    ),
    WANING_MOON(
        "欠けていく月 (デトックス期)",
        "🌗",
        "排出・整理・引き算のタイミング。余分なものを身体や心から削ぎ落とし、身軽になるのに最適な時期です。家事やタスクの断捨離を。",
        "不要なタスクを削除し、部屋や頭の中を整理してすっきりさせましょう。"
    )
}

object MoonHelper {
    /**
     * Calculates the approximate moon age based on a reference New Moon on January 11, 2024.
     * The average lunar cycle length is 29.53059 days.
     */
    fun getMoonAge(timestamp: Long): Double {
        val baseTime = 1704974400000L // Jan 11, 2024 12:00:00 UTC (New Moon)
        val diffDays = (timestamp - baseTime) / (1000.0 * 60 * 60 * 24)
        val cycleLength = 29.53059
        var age = diffDays % cycleLength
        if (age < 0) {
            age += cycleLength
        }
        return age
    }

    /**
     * Calculates the next New Moon date based on the current date.
     */
    fun getNextNewMoonTimestamp(currentMillis: Long): Long {
        val currentAge = getMoonAge(currentMillis)
        val remainingAge = 29.53059 - currentAge
        return currentMillis + (remainingAge * 24 * 60 * 60 * 1000).toLong()
    }

    /**
     * Calculates the next Full Moon date based on the current date.
     */
    fun getNextFullMoonTimestamp(currentMillis: Long): Long {
        val currentAge = getMoonAge(currentMillis)
        val targetAge = 14.765 // Half of 29.53059
        var diffAge = targetAge - currentAge
        if (diffAge <= 0) {
            diffAge += 29.53059
        }
        return currentMillis + (diffAge * 24 * 60 * 60 * 1000).toLong()
    }

    /**
     * Formats timestamp into Japanese date format.
     */
    fun formatDate(millis: Long): String {
        val sdf = java.text.SimpleDateFormat("M月d日(E)", java.util.Locale.JAPAN)
        return sdf.format(java.util.Date(millis))
    }

    /**
     * Calculates days until target timestamp.
     */
    fun daysUntil(targetMillis: Long, currentMillis: Long): Int {
        val diff = targetMillis - currentMillis
        val days = (diff / (1000.0 * 60 * 60 * 24)).toInt()
        return if (days < 0) 0 else days
    }

    /**
     * Returns the appropriate MoonPhase based on the calculated moon age.
     */
    fun getMoonPhase(timestamp: Long): MoonPhase {
        val age = getMoonAge(timestamp)
        return when {
            age < 2.0 || age >= 28.0 -> MoonPhase.NEW_MOON
            age in 2.0..13.0 -> MoonPhase.WAXING_MOON
            age in 13.0..17.0 -> MoonPhase.FULL_MOON
            else -> MoonPhase.WANING_MOON
        }
    }

    /**
     * Returns detailed custom advice matching the Lowest Leeway Category and Selected Moon Phase.
     * Provides extremely supportive, empathetic, and highly specific steps for moms.
     */
    fun getCombinedMoonAdvice(category: DiagnosisCategory, phase: MoonPhase): Pair<String, String> {
        return when (category) {
            DiagnosisCategory.PHYSICAL -> {
                when (phase) {
                    MoonPhase.NEW_MOON -> Pair(
                        "【新月 × からだ】完全休養とディープリセット",
                        "最もエネルギーが枯渇しやすいリセット期。今夜はすべての家事を完全にストップ（お惣菜やデリバリーを活用）し、スマホを19時以降シャットアウトしましょう。温かい白湯を一杯ゆっくり飲んだら、子どもと一緒に21時前に布団に入ること。目元をホットアイマスクや温タオルで温めると、良質な睡眠が取れます。"
                    )
                    MoonPhase.WAXING_MOON -> Pair(
                        "【満ちていく月 × からだ】良質な栄養補給と緩やかなストレッチ",
                        "身体が様々なものを吸収・蓄積しやすい時期です。良質なタンパク質や鉄分を豊富に含んだスープ（具だくさん豚汁やポトフなど）をいただきましょう。お風呂上がりに、足首やふくらはぎをボディクリームで下から上へ優しくさすり、明日へのエネルギーを蓄えてください。"
                    )
                    MoonPhase.FULL_MOON -> Pair(
                        "【満月 × からだ】神経の緊張緩和とディープリリース",
                        "身体の水分や緊張が最も満ちる時期で、むくみや頭痛が出やすく、自律神経が過敏になりがち。40度程度の少しぬるめの湯船に、お気に入りの入浴剤や塩（エプソムソルト等）を入れて10分間ゆっくり浸かりましょう。浮遊感を感じながら肩の力を抜き、お風呂の中で首の後ろを優しくほぐすのがお勧めです。"
                    )
                    MoonPhase.WANING_MOON -> Pair(
                        "【欠けていく月 × からだ】老廃物デトックスと軽やかなリフレッシュ",
                        "身体から不要な水分や毒素を排出するのが得意な時期。少し多めの常温の水を意識して飲み、巡りを高めてください。また、両方の肩甲骨を後ろに向けて「引き算するイメージ」で大きくグルグルと回し、背中に溜まったコリや重さを手放しましょう。"
                    )
                }
            }
            DiagnosisCategory.MENTAL -> {
                when (phase) {
                    MoonPhase.NEW_MOON -> Pair(
                        "【新月 × きもち】心のモヤモヤ大掃除と静かな内観",
                        "自分自身の本音や不安と繋がりやすい日。誰にも見せない裏紙やノートに、今あなたが抱える不安や不満、イライラ、孤独感を包み隠さず書き殴ってみましょう（ブレインダンプ）。最後にその紙をビリビリに細かく破り捨てることで、新月の暗闇にすべてをリセットし、すっきり再出発できます。"
                    )
                    MoonPhase.WAXING_MOON -> Pair(
                        "【満ちていく月 × きもち】ポジティブなワクワクの種まき",
                        "新しい情報やポジティブなエネルギーを吸収しやすい時期。育児の不安を煽るSNS情報などは一時シャットアウトして、あなたが純粋に『これをすると癒される』と思う本や、行ってみたいカフェを調べましょう。未来の『小さなご褒美プラン』をスケジュール帳に1つ書き込むだけで元気が湧きます。"
                    )
                    MoonPhase.FULL_MOON -> Pair(
                        "【満月 × きもち】高ぶる感情の全面受容とセルフハグ",
                        "感情のコップが溢れやすく、些細なことで子どもに感情的に怒って自己嫌悪になりやすい時期。『今日は満月の引力のせい。私が怒りん坊なわけじゃない、お月様のせい！』と自分を許して。深呼吸（4秒吸って8秒吐く）を5回行い、がんばっている自分の胸に手を当てて『よしよし、私本当によくやってるよ』と労いましょう。"
                    )
                    MoonPhase.WANING_MOON -> Pair(
                        "【欠けていく月 × きもち】余計な悩み・他人との比較の手放し",
                        "他人と自分を比べて落ち込む気持ちを、すーっと手放していくのに最適な時期。今日はスマホのSNS閲覧を意識的にオフにする時間を増やしましょう。夜は部屋のメインの明かりを消し、関節照明やキャンドル（LEDでも可）の優しい明かりだけで15分静かに過ごすと、波立つ心が整います。"
                    )
                }
            }
            DiagnosisCategory.TIME -> {
                when (phase) {
                    MoonPhase.NEW_MOON -> Pair(
                        "【新月 × じかん】予定の完全リセット・『何もしない』の許可",
                        "『やらなきゃリスト』を今日は一度ゴミ箱へ。部屋が散らかっていても、洗濯物が溜まっていても『今日はサボる！』と潔く許可しましょう。1日の中で完全にスケジュールが空いている『15分間の空白時間』を意図的に作り、ただソファに寝転がって好きな音楽を聴いたり、温かいココアを飲むためだけに使いましょう。"
                    )
                    MoonPhase.WAXING_MOON -> Pair(
                        "【満ちていく月 × じかん】家事の仕組み化とゆとり創出の仕掛け",
                        "物事を計画し、取り入れていくのに良い時期です。今日のうちに、毎日の家事で『自動化』や『お任せ』できる仕組みを検討しましょう。例えばお掃除ロボットの設定、食洗機のフル活用、ミールキットや食材宅配の注文など、時間を増やす仕掛け作りに最適なタイミングです。"
                    )
                    MoonPhase.FULL_MOON -> Pair(
                        "【満月 × じかん】脳内マルチタスクの一時停止と『今ここ』への集中",
                        "あれもこれもとタスクに追われ、脳がパンク寸前の興奮状態。今日やることを『3つだけ』紙に書いて決め、それ以外の脳内タスクは一時的に忘れましょう。子どもを抱きしめる時は、マルチタスクをストップし、ただ『ぎゅー』とする感覚だけに10秒間、全力で浸ってみてください。脳が安らぎます。"
                    )
                    MoonPhase.WANING_MOON -> Pair(
                        "【欠けていく月 × じかん】家事の『やらないこと』引き算リスト",
                        "『やること』を減らす引き算の時期。今日絶対にやらなくても死なない家事（床のワイパーがけ、シーツ交換、凝った手料理、アイロンなど）を1つ見つけ、それを『今週はやらない！』と宣言して完全にリストから引き算しましょう。生まれた10分で、ただボーッと空を眺めてください。"
                    )
                }
            }
            DiagnosisCategory.SUPPORT -> {
                when (phase) {
                    MoonPhase.NEW_MOON -> Pair(
                        "【新月 × ヘルプ】SOSを発信する勇気と具体的要請",
                        "『全部自分でやらなきゃ』をリセットする日。新月のタイミングで、パートナーや家族に『今本当に疲れてるから、〇〇をやってほしい』と具体的かつストレートに助けを求めましょう。不機嫌な態度で伝えるのではなく、シンプルに『助けて』と伝えることで、周囲も動きやすくなります。"
                    )
                    MoonPhase.WAXING_MOON -> Pair(
                        "【満ちていく月 × ヘルプ】外部支援サービスの登録やリサーチ",
                        "新しいサポート体制を受け入れる準備に適した時期。自治体の一時預かり、ファミリーサポート、民間のシッターサービスや家事代行などを検索・登録してみましょう。今すぐ使わなくても、『いざとなったらここに電話すればいい』というお守りリストをスマホに作っておくだけで、心の支えになります。"
                    )
                    MoonPhase.FULL_MOON -> Pair(
                        "【満月 × ヘルプ】パートナーや周囲への愛と共感の分かち合い",
                        "感情が満ちる時期だからこそ、周囲のサポートに感謝を伝えつつ、本音を打ち明けましょう。『いつも助けてくれてありがとう。でも実は今ちょっとキャパオーバーで…』と、不完全な自分の状態をオープンに分かち合うことで、より深い絆と協力関係が生まれます。"
                    )
                    MoonPhase.WANING_MOON -> Pair(
                        "【欠けていく月 × ヘルプ】『私がやった方が早い』からの卒業",
                        "自分で家事を抱え込んでしまう呪いから卒業する時期。夫や子どもに対して『完璧じゃなくても、任せてみる』練習をしましょう。畳んだ服がズレていても、お皿の置き方が違っても、口を出さずに『やってくれてありがとう』と手放す引き算が、あなたに大きな余白をもたらします。"
                    )
                }
            }
            DiagnosisCategory.COMPASSION -> {
                when (phase) {
                    MoonPhase.NEW_MOON -> Pair(
                        "【新月 × ゆるす】ありのままの自分に合格点を出すスタート",
                        "自分に対する厳しさを一度リセットする日。今日、子どもを笑わせられなくても、ご飯をサボっても、生きて夜を迎えられたならそれだけで100点満点の素晴らしい母親です。新月の始まりに『未熟な私でも、これでいい。毎日がんばってる』と深く自分を許して、受け入れましょう。"
                    )
                    MoonPhase.WAXING_MOON -> Pair(
                        "【満ちていく月 × ゆるす】自分への小さなご投資と五感の喜び",
                        "自分にご褒美をあげることを許可する時期。少し贅沢なスイーツを買う、お気に入りの香りのリップバームを塗る、上質なハーブティーを飲むなど、五感が『心地よい』と喜ぶ小さなプレゼントを自分に贈りましょう。お母さんだからと我慢せず、喜びをチャージしてください。"
                    )
                    MoonPhase.FULL_MOON -> Pair(
                        "【満月 × ゆるす】がんばる自分をハグするセルフいたわり",
                        "常に限界までがんばり続けている自分を全肯定する時期。両腕を胸の前で交差させて自分自身の肩を優しくきゅっと抱きしめ、10秒間『いつも本当にありがとう。お疲れ様、大好きだよ』と声に出すか心の中で唱えて。自分の体の温もりを感じることで、自慈心が満たされます。"
                    )
                    MoonPhase.WANING_MOON -> Pair(
                        "【欠けていく月 × ゆるす】『〇〇すべき』というマイルールの手放し",
                        "『良い母親なら手作りすべき』『いつも笑顔でいるべき』などの自分を縛る『べき論』を、下弦の月とともに手放しましょう。『怒ってもいい』『惣惣でいい』『サボってもいい』。自分宛てにたくさんの『許可証』を書いて、心の中の厳しいルールを引き算してください。"
                    )
                }
            }
        }
    }
}

@Composable
fun MoonCanvas(phase: MoonPhase, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        val darkMoonColor = Color(0xFF37474F)
        val brightMoonColor = Color(0xFFFFD54F)
        val glowColor = Color(0xFFFFF9C4)

        when (phase) {
            MoonPhase.NEW_MOON -> {
                drawCircle(color = darkMoonColor, radius = radius, center = center)
                drawCircle(
                    color = Color(0xFFCFD8DC),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
            MoonPhase.WAXING_MOON -> {
                drawCircle(color = darkMoonColor, radius = radius, center = center)
                val path = Path().apply {
                    addArc(
                        oval = androidx.compose.ui.geometry.Rect(
                            center.x - radius,
                            center.y - radius,
                            center.x + radius,
                            center.y + radius
                        ),
                        startAngleDegrees = -90f,
                        sweepAngleDegrees = 180f
                    )
                }
                drawPath(path = path, color = brightMoonColor)
            }
            MoonPhase.FULL_MOON -> {
                drawCircle(
                    color = glowColor.copy(alpha = 0.25f),
                    radius = radius + 6.dp.toPx(),
                    center = center
                )
                drawCircle(color = brightMoonColor, radius = radius, center = center)
                drawCircle(
                    color = Color(0xFFFBC02D).copy(alpha = 0.25f),
                    radius = radius * 0.2f,
                    center = Offset(center.x - radius * 0.3f, center.y - radius * 0.2f)
                )
                drawCircle(
                    color = Color(0xFFFBC02D).copy(alpha = 0.25f),
                    radius = radius * 0.15f,
                    center = Offset(center.x + radius * 0.2f, center.y + radius * 0.3f)
                )
            }
            MoonPhase.WANING_MOON -> {
                drawCircle(color = darkMoonColor, radius = radius, center = center)
                val path = Path().apply {
                    addArc(
                        oval = androidx.compose.ui.geometry.Rect(
                            center.x - radius,
                            center.y - radius,
                            center.x + radius,
                            center.y + radius
                        ),
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = 180f
                    )
                }
                drawPath(path = path, color = brightMoonColor)
            }
        }
    }
}
