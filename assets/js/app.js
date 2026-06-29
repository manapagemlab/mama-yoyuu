const DIMENSIONS = {
  body: { label: "からだ", icon: "🩺", color: "#e57373" },
  emotion: { label: "きもち", icon: "🧠", color: "#8d7bbd" },
  time: { label: "じかん", icon: "⏳", color: "#5f89b8" },
  help: { label: "ヘルプ", icon: "🤝", color: "#6aa88f" },
  self: { label: "ゆるす", icon: "🌸", color: "#d8a13a" },
};

const QUESTIONS = [
  ["body", "朝起きた時点で、すでに体が重いと感じることがありますか？"],
  ["emotion", "小さなことで涙が出そうになったり、心がざわざわしたりしますか？"],
  ["time", "自分のために座って飲み物を飲む時間すら取りにくいですか？"],
  ["help", "本当は誰かに頼りたいのに、言い出せずに抱え込んでいますか？"],
  ["self", "家事や育児が予定通りに進まないと、自分を責めてしまいますか？"],
  ["body", "肩・腰・目・頭など、疲れのサインを後回しにしていますか？"],
  ["emotion", "家族にやさしくしたいのに、言葉が強くなってしまうことがありますか？"],
  ["time", "やることが多すぎて、頭の中が常に散らかっている感覚がありますか？"],
  ["help", "頼むくらいなら自分でやった方が早い、と感じることが多いですか？"],
  ["self", "休むことに罪悪感があり、何かしていないと落ち着かないですか？"],
].map(([dimension, text]) => ({ dimension, text }));

const ANSWERS = [
  { label: "ほとんどない", value: 5 },
  { label: "たまにある", value: 4 },
  { label: "半分くらいある", value: 3 },
  { label: "よくある", value: 2 },
  { label: "かなり当てはまる", value: 1 },
];

const PHASES = {
  new: {
    label: "新月",
    icon: "🌑",
    range: [0, 1.85],
    short: "今日はリセットと余白づくりの日。予定を増やすより、減らすケアが合います。",
    long: "新月期は心身が静かに再起動したがるタイミングです。完璧なスタートを切ろうとしなくて大丈夫。冷蔵庫の中、バッグ、頭の中のタスクをひとつだけ軽くして、眠る前に「今月やらないこと」を1つ決めてみてください。",
  },
  waxing: {
    label: "満ちていく月",
    icon: "🌓",
    range: [1.85, 12.9],
    short: "今日は少しずつ満たす日。小さな回復習慣を足すのに向いています。",
    long: "満ちていく月は、エネルギーを育てる時期です。がんばりを増やすより、回復の材料を足してあげるのが先。温かい飲み物、短い散歩、早めの入浴など、未来の自分が助かる小さな仕込みをひとつ選びましょう。",
  },
  full: {
    label: "満月",
    icon: "🌕",
    range: [12.9, 16.65],
    short: "今日は感情と緊張が高まりやすい日。ゆるめるケアを最優先に。",
    long: "満月期は、気持ちも体も張りつめやすいピークです。結論を急がず、深呼吸、首肩を温める、スマホから離れるなど、刺激を減らすケアが味方になります。言葉が強くなりそうな時は、まず水を一口飲むだけでも立派なセルフケアです。",
  },
  waning: {
    label: "欠けていく月",
    icon: "🌗",
    range: [16.65, 29.53],
    short: "今日は手放しと整理の日。背負いすぎたものを下ろすのに向いています。",
    long: "欠けていく月は、不要なものを手放しやすい時期です。家事を1つ省く、返信を明日にする、気持ちを紙に殴り書きして破るなど、内側の圧を外へ逃がす行動が合います。減らすことは、家族への愛情を減らすことではありません。",
  },
};

const CARES = {
  body: {
    new: ["塩をひとつまみ入れた湯船に10分だけ浸かる", "温アイマスクで目の奥を休ませる", "夕食はお惣菜にして、子どもと一緒に5分横になる"],
    waxing: ["朝の白湯かハーブティーを一杯だけ足す", "肩甲骨をゆっくり5回まわす", "寝る前に足首を温めて眠りの準備をする"],
    full: ["首の後ろを温めて緊張をほどく", "カフェインを控えめにして水分を増やす", "音と光を少し落として、体への刺激を減らす"],
    waning: ["冷蔵庫にあるもので済ませる日を作る", "不要な予定をひとつ延期する", "疲れの強い場所に手を当てて30秒呼吸する"],
  },
  emotion: {
    new: ["モヤモヤを紙に書き出して、最後に破り捨てる", "今月の心の合言葉を一言だけ決める", "誰にも見せない本音メモを3行書く"],
    waxing: ["うれしかったことを1つだけ保存する", "好きな香りを手首やハンカチに少しつける", "自分に言ってほしい言葉をスマホメモに残す"],
    full: ["大事な話し合いは一晩寝かせる", "怒りの前に水を一口飲む", "泣ける時は我慢せず、短くても感情を流す"],
    waning: ["気にしている一言を紙に書いて外へ出す", "SNSを見る時間を少し削る", "今日は反省会を閉店すると決める"],
  },
  time: {
    new: ["今月やらない家事を1つ決める", "朝の支度を1工程だけ減らす", "献立を考えない日を先に予定へ入れる"],
    waxing: ["15分の自分時間をカレンダーに置く", "買い物リストを定番化する", "寝る前の片づけを3分で終了にする"],
    full: ["予定を詰め込まず、移動と休憩の余白を作る", "即返信しない時間帯を決める", "今日の最低ラインを3つに絞る"],
    waning: ["不要タスクを断捨離する", "期限のない用事を翌週へ送る", "家族に任せる家事をひとつ固定する"],
  },
  help: {
    new: ["頼る相手を1人だけ思い浮かべる", "お願い文を短く下書きする", "困った時リストを冷蔵庫やスマホに置く"],
    waxing: ["小さなお願いを1つ練習する", "買い物や送迎など具体的に頼む", "ありがとうを先に伝える形で協力を依頼する"],
    full: ["限界になる前に「今日は余裕がない」と共有する", "説明しすぎず短く頼む", "家族会議は10分で切り上げる"],
    waning: ["自分が抱えすぎている役割を見直す", "やらなくていい係を手放す", "外部サービスや宅配を選択肢に戻す"],
  },
  self: {
    new: ["今日の合格ラインを低く設定する", "できなかったことより残したい気持ちを書く", "休む許可を自分に一文で出す"],
    waxing: ["自分を褒める言葉を1つ声に出す", "好きなおやつを座って味わう", "できたことを寝る前に3つ数える"],
    full: ["完璧な母でいようとする力を少し抜く", "子どもへの愛情と家事の量を切り離す", "今日は70点で終えていいと決める"],
    waning: ["罪悪感を紙に書き、事実と気持ちに分ける", "比べてしまう情報から距離を置く", "明日の自分に渡す荷物を減らす"],
  },
};

const STORAGE_KEY = "mamaYoyuuHistory";
const SYNODIC_MONTH = 29.530588853;
const KNOWN_NEW_MOON_UTC = Date.UTC(2000, 0, 6, 18, 14);

let state = {
  index: 0,
  answers: [],
  selectedAnswer: null,
  moon: null,
  selectedCare: "body",
  selectedPhase: "new",
};

const $ = (id) => document.getElementById(id);

document.addEventListener("DOMContentLoaded", () => {
  state.moon = calculateMoon(new Date());
  state.selectedPhase = state.moon.phaseKey;
  document.body.dataset.view = "welcomeView";
  renderMoonCard();
  renderHistory();
  bindEvents();
});

function bindEvents() {
  $("startButton").addEventListener("click", startQuiz);
  $("restartButton").addEventListener("click", startQuiz);
  $("nextQuestionButton").addEventListener("click", goToNextQuestion);
  $("toggleMoonDetail").addEventListener("click", () => {
    const detail = $("moonDetail");
    detail.hidden = !detail.hidden;
  });
  $("clearHistoryButton").addEventListener("click", () => {
    if (getHistory().length === 0) return;
    openConfirmDialog();
  });
  $("cancelClearButton").addEventListener("click", closeConfirmDialog);
  $("confirmClearButton").addEventListener("click", () => {
    localStorage.removeItem(STORAGE_KEY);
    closeConfirmDialog();
    renderHistory();
  });
  $("confirmDialog").addEventListener("click", (event) => {
    if (event.target === $("confirmDialog")) closeConfirmDialog();
  });
  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape") closeConfirmDialog();
  });
}

function openConfirmDialog() {
  $("confirmDialog").hidden = false;
  $("cancelClearButton").focus();
}

function closeConfirmDialog() {
  $("confirmDialog").hidden = true;
  $("clearHistoryButton").focus();
}

function calculateMoon(date) {
  const nowUtc = Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), 12);
  const daysSince = (nowUtc - KNOWN_NEW_MOON_UTC) / 86400000;
  const age = ((daysSince % SYNODIC_MONTH) + SYNODIC_MONTH) % SYNODIC_MONTH;
  const illumination = Math.round((1 - Math.cos((2 * Math.PI * age) / SYNODIC_MONTH)) * 50);
  const phaseKey = Object.entries(PHASES).find(([, phase]) => age >= phase.range[0] && age < phase.range[1])[0];
  const daysToNextNew = Math.ceil(SYNODIC_MONTH - age);
  const daysToFull = age <= SYNODIC_MONTH / 2
    ? Math.ceil(SYNODIC_MONTH / 2 - age)
    : Math.ceil(SYNODIC_MONTH + SYNODIC_MONTH / 2 - age);

  return {
    age,
    illumination,
    phaseKey,
    phase: PHASES[phaseKey],
    nextNewDate: addDays(date, daysToNextNew),
    nextFullDate: addDays(date, daysToFull),
    daysToNextNew,
    daysToFull,
  };
}

function addDays(date, days) {
  const next = new Date(date);
  next.setDate(next.getDate() + days);
  return next;
}

function formatDate(date) {
  return new Intl.DateTimeFormat("ja-JP", { month: "numeric", day: "numeric", weekday: "short" }).format(date);
}

function renderMoonCard() {
  const moon = state.moon;
  $("moonIcon").textContent = moon.phase.icon;
  $("moonPhaseName").textContent = `${moon.phase.label}に近い日（輝き ${moon.illumination}%）`;
  $("moonShortAdvice").textContent = moon.phase.short;
  $("moonLongAdvice").textContent = moon.phase.long;
  $("nextNewMoon").textContent = `次の新月: ${formatDate(moon.nextNewDate)} / あと${moon.daysToNextNew}日`;
  $("nextFullMoon").textContent = `次の満月: ${formatDate(moon.nextFullDate)} / あと${moon.daysToFull}日`;
}

function startQuiz() {
  state.index = 0;
  state.answers = [];
  state.selectedAnswer = null;
  showView("quizView");
  renderQuestion();
}

function showView(id) {
  document.body.dataset.view = id;
  document.querySelectorAll(".view").forEach((view) => view.classList.toggle("active", view.id === id));
  window.scrollTo({ top: 0, behavior: "smooth" });
}

function renderQuestion() {
  const question = QUESTIONS[state.index];
  const dimension = DIMENSIONS[question.dimension];
  state.selectedAnswer = state.answers[state.index] || null;
  $("questionCounter").textContent = `質問 ${state.index + 1} / ${QUESTIONS.length}`;
  $("questionDimension").textContent = `${dimension.icon} ${dimension.label}の余裕`;
  $("questionText").textContent = question.text;
  $("progressBar").style.width = `${((state.index + 1) / QUESTIONS.length) * 100}%`;
  $("nextQuestionButton").textContent = state.index === QUESTIONS.length - 1 ? "結果を見る" : "次へ";
  $("nextQuestionButton").disabled = !state.selectedAnswer;
  $("answerGrid").innerHTML = "";
  ANSWERS.forEach((answer) => {
    const button = document.createElement("button");
    const isSelected = state.selectedAnswer?.value === answer.value;
    button.className = `answer-button${isSelected ? " selected" : ""}`;
    button.type = "button";
    button.setAttribute("aria-pressed", String(isSelected));
    button.textContent = answer.label;
    button.addEventListener("click", () => selectAnswer(question.dimension, answer.value));
    $("answerGrid").appendChild(button);
  });
}

function selectAnswer(dimension, value) {
  state.selectedAnswer = { dimension, value };
  state.answers[state.index] = state.selectedAnswer;
  $("nextQuestionButton").disabled = false;
  document.querySelectorAll(".answer-button").forEach((button) => {
    const selected = button.textContent === ANSWERS.find((answer) => answer.value === value).label;
    button.classList.toggle("selected", selected);
    button.setAttribute("aria-pressed", String(selected));
  });
}

function goToNextQuestion() {
  if (!state.selectedAnswer) return;
  state.index += 1;
  if (state.index < QUESTIONS.length) {
    renderQuestion();
    return;
  }
  const result = buildResult();
  saveResult(result);
  renderResult(result);
  renderHistory();
  showView("resultView");
}

function buildResult() {
  const dimensionScores = {};
  Object.keys(DIMENSIONS).forEach((key) => {
    const values = state.answers.filter((answer) => answer.dimension === key).map((answer) => answer.value);
    dimensionScores[key] = Math.round((values.reduce((sum, value) => sum + value, 0) / (values.length * 5)) * 100);
  });
  const total = Math.round(
    Object.values(dimensionScores).reduce((sum, score) => sum + score, 0) / Object.keys(dimensionScores).length
  );
  return {
    id: crypto.randomUUID ? crypto.randomUUID() : String(Date.now()),
    createdAt: new Date().toISOString(),
    total,
    dimensionScores,
    moonPhase: state.moon.phaseKey,
  };
}

function renderResult(result) {
  drawGauge(result.total);
  $("scoreCopy").textContent = getScoreMessage(result.total);
  renderDimensionBars(result.dimensionScores);
  renderPriorityList(result.dimensionScores);
  state.selectedCare = getPriority(result.dimensionScores)[0][0];
  state.selectedPhase = state.moon.phaseKey;
  renderCareBoard();
}

function getScoreMessage(score) {
  if (score >= 76) return "今は比較的ゆとりがあります。余裕があるうちに、未来の自分を助ける小さな仕組みを作るのがおすすめです。";
  if (score >= 51) return "がんばれている一方で、ところどころ余裕が削れています。今日はひとつだけ、回復を先に置きましょう。";
  if (score >= 31) return "心と体がかなり踏ん張っています。家事や予定を減らし、頼る・休む・ゆるすケアを優先してください。";
  return "かなり消耗が強い状態です。今日の目標は立て直すことではなく、これ以上削らないこと。安全な相手や専門窓口へ頼る選択も大切です。";
}

function drawGauge(score) {
  const canvas = $("gaugeCanvas");
  const ctx = canvas.getContext("2d");
  const width = canvas.width;
  const height = canvas.height;
  ctx.clearRect(0, 0, width, height);
  ctx.lineCap = "round";
  ctx.lineWidth = 24;
  ctx.strokeStyle = "#efdcd1";
  ctx.beginPath();
  ctx.arc(width / 2, 168, 112, Math.PI, 2 * Math.PI);
  ctx.stroke();

  const gradient = ctx.createLinearGradient(48, 0, width - 48, 0);
  gradient.addColorStop(0, "#e57373");
  gradient.addColorStop(0.6, "#f0a45f");
  gradient.addColorStop(1, "#d8a13a");
  ctx.strokeStyle = gradient;
  ctx.beginPath();
  ctx.arc(width / 2, 168, 112, Math.PI, Math.PI + Math.PI * (score / 100));
  ctx.stroke();

  ctx.fillStyle = "#4d342d";
  ctx.font = "900 52px Noto Sans JP, sans-serif";
  ctx.textAlign = "center";
  ctx.fillText(`${score}%`, width / 2, 145);
  ctx.font = "700 16px Noto Sans JP, sans-serif";
  ctx.fillStyle = "#795548";
  ctx.fillText("ゆとり残量", width / 2, 174);
}

function renderDimensionBars(scores) {
  $("dimensionBars").innerHTML = "";
  Object.entries(DIMENSIONS).forEach(([key, dimension]) => {
    const row = document.createElement("div");
    row.className = "dimension-row";
    row.innerHTML = `
      <div class="dimension-label"><span>${dimension.icon} ${dimension.label}</span><span>${scores[key]}%</span></div>
      <div class="bar-track"><div class="bar-fill" style="width:${scores[key]}%; background:${dimension.color}"></div></div>
    `;
    $("dimensionBars").appendChild(row);
  });
}

function getPriority(scores) {
  return Object.entries(scores).sort((a, b) => a[1] - b[1]);
}

function renderPriorityList(scores) {
  $("priorityList").innerHTML = "";
  getPriority(scores).forEach(([key, score], index) => {
    const dimension = DIMENSIONS[key];
    const phaseCare = CARES[key][state.moon.phaseKey][0];
    const card = document.createElement("article");
    card.className = "priority-card";
    card.innerHTML = `
      <h3>${index + 1}位 ${dimension.icon} ${dimension.label}の余裕を戻す</h3>
      <p>現在の残量は<strong>${score}%</strong>。今日の月相に合わせるなら、まずは「${phaseCare}」から。</p>
    `;
    $("priorityList").appendChild(card);
  });
}

function renderCareBoard() {
  $("activeMoonBadge").textContent = `今日: ${state.moon.phase.icon} ${state.moon.phase.label}`;
  $("careTabs").innerHTML = "";
  Object.entries(DIMENSIONS).forEach(([key, dimension]) => {
    const button = document.createElement("button");
    button.className = `tab-button ${state.selectedCare === key ? "active" : ""}`;
    button.type = "button";
    button.textContent = `${dimension.icon} ${dimension.label}`;
    button.addEventListener("click", () => {
      state.selectedCare = key;
      renderCareBoard();
    });
    $("careTabs").appendChild(button);
  });

  $("phaseTabs").innerHTML = "";
  Object.entries(PHASES).forEach(([key, phase]) => {
    const button = document.createElement("button");
    button.className = `phase-button ${state.selectedPhase === key ? "active" : ""}`;
    button.type = "button";
    button.textContent = `${phase.icon} ${phase.label}`;
    button.addEventListener("click", () => {
      state.selectedPhase = key;
      renderCareBoard();
    });
    $("phaseTabs").appendChild(button);
  });

  const dimension = DIMENSIONS[state.selectedCare];
  const phase = PHASES[state.selectedPhase];
  const items = CARES[state.selectedCare][state.selectedPhase];
  $("careDetail").innerHTML = `
    <h3>${dimension.icon} ${dimension.label} × ${phase.icon} ${phase.label}</h3>
    <p>${phase.short}</p>
    <ul>${items.map((item) => `<li>${item}</li>`).join("")}</ul>
  `;
}

function getHistory() {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEY) || "[]");
  } catch {
    return [];
  }
}

function saveResult(result) {
  const history = [result, ...getHistory()].slice(0, 30);
  localStorage.setItem(STORAGE_KEY, JSON.stringify(history));
}

function deleteHistoryItem(id) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(getHistory().filter((item) => item.id !== id)));
  renderHistory();
}

function renderHistory() {
  const history = getHistory();
  drawHistory(history.slice(0, 7).reverse());
  $("historyList").innerHTML = "";
  if (history.length === 0) {
    $("historyList").innerHTML = '<p class="lead">まだ履歴はありません。最初の診断結果がここに保存されます。</p>';
    return;
  }
  history.slice(0, 7).forEach((item) => {
    const row = document.createElement("div");
    row.className = "history-item";
    row.innerHTML = `
      <span>${new Intl.DateTimeFormat("ja-JP", { dateStyle: "medium", timeStyle: "short" }).format(new Date(item.createdAt))}</span>
      <strong>${item.total}%</strong>
    `;
    const button = document.createElement("button");
    button.className = "ghost-button";
    button.type = "button";
    button.textContent = "削除";
    button.addEventListener("click", () => deleteHistoryItem(item.id));
    row.appendChild(button);
    $("historyList").appendChild(row);
  });
}

function drawHistory(items) {
  const canvas = $("historyCanvas");
  const ctx = canvas.getContext("2d");
  const width = canvas.width;
  const height = canvas.height;
  ctx.clearRect(0, 0, width, height);
  ctx.fillStyle = "#fdf6f0";
  ctx.fillRect(0, 0, width, height);
  ctx.strokeStyle = "#eadbd1";
  ctx.lineWidth = 1;
  for (let i = 1; i <= 4; i += 1) {
    const y = (height / 5) * i;
    ctx.beginPath();
    ctx.moveTo(28, y);
    ctx.lineTo(width - 28, y);
    ctx.stroke();
  }
  if (items.length === 0) {
    ctx.fillStyle = "#795548";
    ctx.font = "700 18px Noto Sans JP, sans-serif";
    ctx.textAlign = "center";
    ctx.fillText("診断すると、過去7回の変化がここに表示されます", width / 2, height / 2);
    return;
  }

  const points = items.map((item, index) => {
    const x = items.length === 1 ? width / 2 : 42 + (index * (width - 84)) / (items.length - 1);
    const y = height - 34 - (item.total / 100) * (height - 68);
    return { x, y, score: item.total };
  });
  const gradient = ctx.createLinearGradient(0, 0, width, 0);
  gradient.addColorStop(0, "#e57373");
  gradient.addColorStop(0.6, "#f0a45f");
  gradient.addColorStop(1, "#6aa88f");
  ctx.strokeStyle = gradient;
  ctx.lineWidth = 5;
  ctx.lineCap = "round";
  ctx.lineJoin = "round";
  ctx.beginPath();
  points.forEach((point, index) => {
    if (index === 0) ctx.moveTo(point.x, point.y);
    else ctx.lineTo(point.x, point.y);
  });
  ctx.stroke();
  points.forEach((point) => {
    ctx.fillStyle = "#fff";
    ctx.beginPath();
    ctx.arc(point.x, point.y, 9, 0, Math.PI * 2);
    ctx.fill();
    ctx.strokeStyle = "#e57373";
    ctx.lineWidth = 3;
    ctx.stroke();
    ctx.fillStyle = "#4d342d";
    ctx.font = "800 13px Noto Sans JP, sans-serif";
    ctx.textAlign = "center";
    ctx.fillText(`${point.score}%`, point.x, point.y - 16);
  });
}
