# ママの余裕見える化診断

ママの心のゆとりを5つの次元で診断し、月のリズムに合わせたセルフケアを提案するGitHub Pages向けの静的Webアプリです。

## 機能

- 全10問の5分診断
- からだ・きもち・じかん・ヘルプ・ゆるすの5次元スコア
- 余裕残量ゲージと5大要素のディメンションバー
- 枯渇している順のセルフケア優先順位
- 現在日付から月相、次回新月、次回満月を動的算出
- 月相別のご自愛インタラクティブボード
- ブラウザ内保存による履歴管理、個別削除、全削除

## ファイル構成

```text
.
├── index.html
├── assets/
│   ├── css/
│   │   └── styles.css
│   ├── icons/
│   │   └── icon.svg
│   └── js/
│       └── app.js
├── tools/
│   └── serve.mjs
├── .gitignore
├── .nojekyll
├── package.json
└── README.md
```

## ローカルで確認する

Node.jsが入っている環境なら、以下でローカル確認できます。

```bash
npm start
```

表示されたURLをブラウザで開いてください。ビルド不要の静的アプリなので、`index.html` を直接開いても動作します。

## GitHub Pagesで公開する

1. このフォルダの中身をGitHubリポジトリのルートに置きます。
2. GitHubの `Settings` → `Pages` を開きます。
3. `Build and deployment` の `Source` を `Deploy from a branch` にします。
4. `Branch` で `main`、フォルダで `/root` を選択して保存します。
5. 数分後に表示されるURLからアプリを開けます。

## データ保存について

診断履歴はサーバーへ送信せず、利用者のブラウザ内 `localStorage` に保存されます。ブラウザのデータ削除や別端末利用では履歴は引き継がれません。
