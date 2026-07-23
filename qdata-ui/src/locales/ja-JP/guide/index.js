// GuideTip コンポーネント翻訳
const QDATA_COMMUNITY_URL = 'https://community.qdata.tech';

export default {
  index: {
    title: '重要なお知らせ：本システムはデモサイトです。<u>毎日午前2:30に全ユーザーデータが削除</u>され、システムの健全性と安定運用を確保しています。',
    content: `完全な機能をご利用いただくには、オープンソースコードをダウンロードしてご自身でデプロイしてください。<u>オープンソース版ブランドライセンス</u>または<u>商用版のお問い合わせ</u>は、詳細ページをご覧ください： 👉  <a href="${QDATA_COMMUNITY_URL}/ja/business/policy.html" target="_blank">ライセンス詳細</a>`,
    extensionLabel: 'お知らせ',
    extensionContent: '<span class="guide-tip-announcement-brand">DataX登場</span>、qDataオープンソース版 <span class="guide-tip-announcement-dict-tag">v1.6.0</span> を軽量リリース！DataXによるデータ同期機能を新たに統合し、<span class="guide-tip-announcement-keyword">デプロイ構成と実行時の依存関係をさらに簡素化</span>しました。<span class="guide-tip-announcement-keyword">リソース消費と運用コストを抑え</span>、データ接続・同期・タスク管理をよりシンプルかつ効率的にします。',
    type: 'danger',
    version: '1.0'
  },
  attAuditRule: {
    title: '監査ルール — データ品質を保障する核心的手段であり、<u>標準データ要素</u>の定義（フィールドタイプ、値域など）に基づいて具体的なチェックロジックを策定し、データ品質の評価に用います。',
    content: `設定されたルールは<u>データ品質タスク</u>にバインドでき、スケジュール実行により自動的にデータ異常をスキャン・識別し、品質レポートを生成してデータ問題の発見とガバナンスのクローズドループをサポートします。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/basic-management/rule-management/audit-rules.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  attCleanRule: {
    title: 'クレンジングルール — データ加工プロセスにおける標準化処理ロジックを定義し、データ統合時のフォーマット統一、値域の規範化、論理的な正当性を確保します。',
    content: `<u>標準データ要素</u>の定義に基づいてクレンジングルールを作成し、<u>データ統合タスク</u>で呼び出すことで、ソースデータを自動的にクレンジングし、プラットフォームに取り込まれるデータの品質を保障します。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/basic-management/rule-management/cleaning-rules.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  attProject: {
    title: 'プロジェクト — <u>データ開発</u>のワークスペース',
    content: `ビジネスラインやチームごとにデータ接続、データ資産、タスク、ジョブなどを分離管理します。所属プロジェクトのコンテンツのみにアクセスでき、権限に応じて対応プロジェクトでデータ開発作業を行ってください。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/basic-management/project-management.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  attTheme: {
    title: 'テーマ — <u>データ資産</u>のグルーピング次元の一つで、データ資産のテーマ別タグ付けとグループ管理に使用します。',
    content: `テーマ体系はプラットフォーム初期にデータガバナンスチームが統一的に計画し、適度な粒度と明確なセマンティクスを維持し、頻繁な調整による管理混乱を避けることを推奨します。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/basic-management/topic-management.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  attClient: {
    title: 'アプリケーション — サードパーティシステムとの連携用',
    content: `アプリケーションを作成し専用キーを生成することで、プラットフォームAPIへの安全なアクセスと権限制御を実現します。ここでアプリケーション情報を管理し、呼び出しクレデンシャルを取得できます。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/basic-management/application-management.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  attQualityCat: {
    title: 'データ品質カテゴリとは？',
    content: `データ品質カテゴリは<u>データ品質タスク</u>を分類管理し、タスクの迅速な特定と整理に役立ちます。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/basic-management/category-management/data-quality-category.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  attApiCat: {
    title: 'データAPIサービスカタログとは？',
    content: `データAPIサービスカタログは<u>データサービス</u>モジュールにおけるAPIサービスの論理分類とディレクトリ管理ビューで、インターフェース資産の構造化された整理、便利な検索、統一された表示を実現します。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/basic-management/category-management/api-category.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  dpModel: {
    title: '論理モデル — 可視化設計と標準化されたデータモデリング',
    content: `ビジネス層で標準化されたデータテーブル構造の構築をサポートし、データベースからのインポートまたは手動設計が可能で、<u>標準データ要素</u>を関連付けてフィールドレベルの統一規範を実現します。ワンクリックで物理テーブルにマテリアライズできます。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-standards/logical-model.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  dpDataElem: {
    title: '標準データ要素 — フィールドレベルのデータ定義と規範の統一',
    content: `フィールドの名称、タイプ、長さ、フォーマットなどの標準属性を定義し、コードテーブルとクレンジングルールのバインドをサポートします。モデリング、<u>データクレンジング</u>、<u>品質監査</u>に統一された根拠を提供します。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-standards/standard-data-element.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  daDatasource: {
    title: 'データ接続 — 多ソース異種データアクセスの統合管理',
    content: `プラットフォームに必要なデータソース接続を設定・管理し、リレーショナルデータベース、ビッグデータプラットフォーム、メッセージキュー、ファイルサービス、オブジェクトストレージなど様々なタイプをサポートします。<u>データ収集</u>、<u>メタデータ同期</u>、<u>タスク実行</u>の基盤として機能します。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-assets/data-connections.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  daAsset: {
    title: '資産マップ — 全ドメインデータ資産の統合管理とパノラマインサイト',
    content: `構造化データテーブルや非構造化ファイルなど様々なデータ資産を集中的に管理し、資産概要、リネージ、品質、クレンジング情報のパノラマビューを提供します。自動検出、手動取り込み、フルライフサイクルガバナンスをサポートします。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-assets/asset-map.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  daAssetApply: {
    title: '資産レビュー — クロスプロジェクトデータ利用の承認制御',
    content: `グローバルデータ資産のプロジェクト利用申請を管理し、資産管理者がレビューします。承認後、資産は<u>プロジェクト資産</u>リストに追加され、<u>データ開発タスク</u>で使用できます。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-assets/asset-audit.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  executeSqlQuery: {
    title: 'データクエリ — セルフサービス型多ソースデータ探索と分析',
    content: `可視化SQLクエリインターフェースを提供し、リレーショナルデータベースとビッグデータプラットフォームへの便利なアクセスをサポートします。ディレクトリナビゲーション、構文補完、結果エクスポート機能を統合しています。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-assets/data-query.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  dataQualityTasks: {
    title: 'データ品質タスク — 多次元データ品質モニタリングと評価',
    content: `品質評価タスクを設定し、指定データテーブルに対して完全性、正確性などの多次元ルールチェックを実行し、品質レポートを生成して問題データを追跡します。柔軟なスケジューリングと履歴遡及をサポートします。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-assets/data-quality/quality-tasks.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  daSensitiveLevel: {
    title: '機密レベル — データ分類とセキュリティ管理の基盤',
    content: `データ機密レベルを定義し、フィールドと資産に分類根拠を提供し、マスキングルールと自動的に連携します。データプレビュー、クエリ、エクスポートなどの場面でレベルに基づく動的マスキングを実施します。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-assets/data-security/sensitivity-level.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  dppAsset: {
    title: 'プロジェクト資産 — プロジェクト内のデータ資産の分離と利用',
    content: `現在のプロジェクトで利用可能なデータ資産を集中的に管理し、グローバル資産からの申請または自主作成をサポートします。プロジェクトメンバーは権限範囲内でフィールド表示、リネージ分析、品質・クレンジング設定を行えます。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-development/project-assets.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  dppEtlTask: {
    title: 'データ統合タスク — 多ソースデータ同期と変換の統合チャネル',
    content: `グラフィカルにETLプロセスを設定し、リレーショナルデータベース、ビッグデータプラットフォーム、Kafka、HDFSなどの多ソースデータの入出力をサポートします。豊富な変換コンポーネントを提供し、オフラインバッチ処理とFlinkリアルタイムストリーム処理をサポートします。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-development/task-management/integration-tasks.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  dpptaskerddv: {
    title: 'データ開発タスク — リアルタイムとオフラインデータ処理の統合オーケストレーション',
    content: `Flinkベースのリアルタイムストリーム処理とSpark SQLのオフラインバッチ処理をサポートし、SQL開発、デバッグ、スケジュール実行機能を提供します。データ同期、計算加工、データレイク/ウェアハウスロードなどのシナリオをカバーします。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-development/task-management/development-tasks.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  dppEtlTaskInstance: {
    title: '運用管理 — スケジュールタスクの実行モニタリングと障害対応',
    content: `ジョブとデータ開発タスクの実行インスタンスを集中的に管理し、実行ログ表示、タスク依存関係、実行状態をサポートします。インスタンス再実行とログダウンロード機能を提供します。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-development/operations-management.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  attTaskCat: {
    title: 'タスクカテゴリ — データ開発タスクの分類管理体系',
    content: `ツリー型カテゴリ構造でデータ統合などの開発タスクを分類管理し、マルチレベルディレクトリ設定とタスクバインディングをサポートします。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-development/project-basic/integration-task-category.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  AttDataDevCat: {
    title: 'データ開発カタログ — データ開発タスクのツリー型分類管理',
    content: `マルチレベルディレクトリ構造でリアルタイムとオフラインのデータ開発タスクを分類整理し、柔軟なカテゴリ作成とバインディングをサポートします。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-development/project-basic/development-task-category.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  AttProjectUserRel: {
    title: 'メンバーとロール管理 — プロジェクトコラボレーションの権限基盤',
    content: `メンバー管理とロール管理モジュールを通じて、プロジェクト内の人員追加、ロール割り当て、きめ細かな権限制御を実現します。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-development/project-basic/member-role/" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  dsApi: {
    title: 'API管理 — データサービスの統合公開と共有',
    content: `ウィザード形式の設定またはSQLモードで迅速にデータAPIを作成し、サービス転送、権限制御、地理空間サービスサポートを提供します。 <a href="${QDATA_COMMUNITY_URL}/ja/docs/user/data-services/api-management.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  dpDocument: {
    title: '標準登録 — 多ソースデータ標準の統合管理',
    content: '<u>国家標準</u>、<u>業界標準</u>、<u>地方標準</u>、<u>団体標準</u>など様々なデータ標準ファイルを集中的に管理し、アップロード、検索、バージョン管理、共有利用をサポートします。',
    type: 'remind',
    version: '1.0'
  },
  dataLayer: {
    title: '階層規範 — 自動階層検証によるデータウェアハウス管理の変革',
    content: '命名規則とビジネス次元に基づくデータ階層（ディメンション、詳細、集計）の自動検証をサポートし、定義からスケジューリングまでのフルライフサイクル管理を提供します。',
    type: 'remind',
    version: '1.0'
  },
  structured: {
    title: '収集タスク — 自動検出、統合管理',
    content: `収集タスクを設定し、指定データソースからデータベース、テーブル、フィールドなどのメタデータを取得し、プラットフォーム仕様に従って解析、変換、取り込みを行います。 <a href="${QDATA_COMMUNITY_URL}/ja/user/%E6%95%B0%E6%8D%AE%E7%A0%94%E5%8F%91/%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86/2%E3%80%81%E6%95%B0%E6%8D%AE%E5%BC%80%E5%8F%91.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  },
  unreleasedTable: {
    title: '最新メタデータ — データベースレベル資産の核心ユニット維持キャリア',
    content: `テーブルの構造情報とビジネス記述を管理し、テーブルの所属ビジネスドメイン、公開状態の維持、収集されたテーブルレベルのメタデータ更新が可能です。 <a href="${QDATA_COMMUNITY_URL}/ja/user/%E6%95%B0%E6%8D%AE%E7%A0%94%E5%8F%91/%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86/2%E3%80%81%E6%95%B0%E6%8D%AE%E5%BC%80%E5%8F%91.html" target="_blank">ヘルプドキュメント</a>`,
    type: 'remind',
    version: '1.0'
  }
}
