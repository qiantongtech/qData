// GuideTip 翻訳
export default {
  index: {
    title: '重要な注意：本システムはデモサイトです。<u>毎日午前2時30分にすべてのユーザーデータが削除されます</u>、システムの整理と安定した運用を維持するためです。',
    content: '完全な機能を体験するには、オープンソースコードをダウンロードしてご自身でデプロイしてください。<u>オープンソースブランド認証</u>または<u>商用版のご相談</u>については、こちらをご覧ください： 👉 <a href="https://qdata.qiantong.tech/business.html" target="_blank">認証詳細を確認</a>',
    type: 'danger',
    version: '1.0'
  },
  attAuditRule: {
    title: '監査ルール — データ品質を確保する核心的手段。<u>標準データエレメント</u>の定義（フィールド型、値域など）に基づき、具体的なチェックロジックを策定し、データ品質の評価を行います。',
    content: 'これらのルールを設定後、<u>データ品質タスク</u>にバインドして定期的に実行し、データ異常を自動的にスキャン・識別、品質レポートを生成し、データ問題の発見とガバナンスの閉ループを支えます。 <a href="https://qdata.qiantong.tech/docs/user/basic-management/rule-management/audit-rules.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  attCleanRule: {
    title: 'クリーニングルール — データ処理における標準化処理ロジックを定義し、統合過程中でのフォーマット統一、値域規範、論理合理性を確保します。',
    content: '<u>標準データエレメント</u>の定義に基づいてクリーニングルールを作成し、<u>データ統合タスク</u>で呼び出して、ソースデータを自動的にクリーニングし、プラットフォームに入るデータの品質を保証します。 <a href="https://qdata.qiantong.tech/docs/user/basic-management/rule-management/cleaning-rules.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  attProject: {
    title: 'プロジェクト — <u>データ開発</u>のワークスペース',
    content: 'ビジネスラインやチームごとにデータ接続、データアセット、タスク、ジョブを分離して管理するために使用します。所属プロジェクトの内容のみアクセス可能で、権限に応じて対応プロジェクトでデータ開発作業を行ってください。 <a href="https://qdata.qiantong.tech/docs/user/basic-management/project-management.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  attTheme: {
    title: 'テーマ — <u>データアセット</u>のグループ化次元の1つ。データアセットのテーマ別注釈とグループ管理に使用されます。',
    content: 'プラットフォーム初期にはデータガバナンスチームによるテーマ体系の統一計画を推奨し、適切なテーマ粒度と明確なセマンティクスを維持し、頻繁な調整による管理混乱を回避することをお勧めします。 <a href="https://qdata.qiantong.tech/docs/user/basic-management/topic-management.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  attClient: {
    title: 'アプリケーション — サードパーティシステムとの連携に使用',
    content: 'アプリケーションを作成し専用キーを生成することで、プラットフォームAPIへの安全なアクセスと権限制御を実現します。アプリケーション情報の管理と呼び出し資格の取得が可能で、サービス間の安全で制御可能な統合を保証します。 <a href="https://qdata.qiantong.tech/docs/user/basic-management/application-management.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  attQualityCat: {
    title: 'データ品質カテゴリとは？',
    content: 'データ品質カテゴリは<u>データ品質タスク</u>の分類管理に使用され、タスクの迅速な検索と組織化を支援します。 <a href="https://qdata.qiantong.tech/docs/user/basic-management/category-management/data-quality-category.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  attApiCat: {
    title: 'データAPIサービスディレクトリとは？',
    content: 'データAPIサービスディレクトリは<u>データサービス</u>モジュールにおけるAPIサービスの論理分類とディレクトリ管理ビューで、インターフェースアセットの構造化組織、便利な検索、統一プレゼンテーションを実現します。 <a href="https://qdata.qiantong.tech/docs/user/basic-management/category-management/api-category.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  dpModel: {
    title: '論理モデル — ビジュアルデザインと標準化データモデリング',
    content: 'ビジネスレベルで標準化されたデータテーブル構造の構築をサポートし、データベースからのインポートまたは手動デザインが可能で、<u>標準データエレメント</u>と関連付けてフィールドレベルの統一仕様を実現します。ワンクリックで物理テーブルへの物化をサポートし、モデルデザインからデータ実装までの全体プロセスを接続します。 <a href="https://qdata.qiantong.tech/docs/user/data-standards/logical-model.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  dpDataElem: {
    title: '標準データエレメント — フィールドレベルの統一データ定義と仕様',
    content: 'フィールドの名前、型、長さ、フォーマットなどの標準属性を定義し、コードテーブルとクリーニングルールのバインドをサポート。モデリング、<u>データクリーニング</u>、<u>品質監査</u>の統一根拠を提供し、データの整合性とガバナンス自動化レベルを向上させます。 <a href="https://qdata.qiantong.tech/docs/user/data-standards/standard-data-element.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  daDatasource: {
    title: 'データ接続 — 多源異種データアクセスの統一管理',
    content: 'プラットフォームに必要なデータソース接続の設定と管理に使用し、リレーショナルデータベース、ビッグデータプラットフォーム、メッセージキュー、ファイルサービス、オブジェクトストレージなど various タイプをサポート。<u>データ収集</u>、<u>メタデータ同期</u>、<u>タスク実行</u>の基盤として、<u>アセットマップ</u>などのモジュールのデータ発見可能性とアクセス可能性を保証します。 <a href="https://qdata.qiantong.tech/docs/user/data-assets/data-connections.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  daAsset: {
    title: 'アセットマップ — ドメイン全体データアセットの統一管理とパノラマインサイト',
    content: '構造化データテーブルや非構造化ファイルなど various データアセットを一元管理し、アセット概要、リネーション関係、品質とクリーニング情報のパノラマビューを提供。自動発見、手動アクセス、ライフサイクルガバナンスをサポートし、可視、管理可能、制御可能なデータアセットを実現し、データの効率的な発見とコラボレーション使用を支えます。 <a href="https://qdata.qiantong.tech/docs/user/data-assets/asset-map.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  daAssetApply: {
    title: 'アセット審査 — プロジェクト間データ使用の承認制御',
    content: 'プロジェクトによるグローバルデータアセットの使用申請を管理し、アセット管理者が承認します。承認後、アセットは<u>プロジェクトアセット</u>リストに含まれ、<u>データ開発タスク</u>で使用可能となり、データ使用のコンプライアンスと権限の制御を確保し、アセット共有とセキュリティのバランスを実現します。 <a href="https://qdata.qiantong.tech/docs/user/data-assets/asset-audit.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  executeSqlQuery: {
    title: 'データクエリ — セルフサービス多源データ探索と分析',
    content: 'ビジュアルSQLクエリインターフェースを提供し、リレーショナルデータベースとビッグデータプラットフォームへの便利なアクセスをサポート。ディレクトリナビゲーション、構文補完、結果エクスポート機能を統合し、ユーザーが迅速にデータを取得し、分析検証、問題トラブルシューティングなどのシナリオニーズを満たし、データ使用効率を向上させます。 <a href="https://qdata.qiantong.tech/docs/user/data-assets/data-query.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  dataQualityTasks: {
    title: 'データ品質タスク — 多次元データ品質監視と評価',
    content: '品質評価タスクを設定し、指定されたデータテーブルに対して完全性、正確性など多次元ルールチェックを実行し、品質レポートを生成して問題データを追跡。柔軟なスケジューリングと履歴レビューをサポートし、ユーザーがデータ異常を及时に発見し、データガバナンスの閉ループ管理を支えます。 <a href="https://qdata.qiantong.tech/docs/user/data-assets/data-quality/quality-tasks.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  daSensitiveLevel: {
    title: '感度レベル — データ分類とセキュリティ制御の基盤',
    content: 'データ感度レベルを定義し、フィールドとアセットに分類根拠を提供し、マスキングルールと自動関連付け。データプレビュー、クエリ、出力などのシナリオで、レベルに基づく動的マスキングを実装し、機密情報の安全でコンプライアンスにかなった使用を保証します。 <a href="https://qdata.qiantong.tech/docs/user/data-assets/data-security/sensitivity-level.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  dppAsset: {
    title: 'プロジェクトアセット — プロジェクト内データアセットの分離と使用',
    content: '現在のプロジェクトで使用可能なデータアセットを一元管理し、グローバルアセットからの申請または自主作成をサポート。プロジェクトメンバーは権限範囲内でフィールド表示、リネーション分析、品質とクリーニング設定が可能で、データ開発タスクで使用し、安全な分離とコンプライアンス使用を実現します。 <a href="https://qdata.qiantong.tech/docs/user/data-development/project-assets.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  dppEtlTask: {
    title: 'データ統合タスク — 多源データ同期と変換の統一チャネル',
    content: 'グラフィカルな方法でETLプロセスを設定し、リレーショナルデータベース、ビッグデータプラットフォーム、Kafka、HDFSなど various データソースへのアクセスと出力をサポート。豊富な変換コンポーネントを提供し、オフラインバッチ処理とFlinkリアルタイムストリーム処理をサポートし、データクリーニング、変換、レイク/ウェアハウスローディングの統合スケジューリングを実現します。 <a href="https://qdata.qiantong.tech/docs/user/data-development/task-management/integration-tasks.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  dpptaskerddv: {
    title: 'データ開発タスク — リアルタイムとオフラインデータ処理の統一オーケストレーション',
    content: 'Flinkベースのリアルタイムストリーム処理とSpark SQLベースのオフラインバッチ処理をサポートし、SQL開発、デバッグ、定期実行機能を提供。データ同期、計算処理、レイク/ウェアハウスローディングシナリオをカバーし、多源データの柔軟な処理と効率的なオーケストレーションを実現します。 <a href="https://qdata.qiantong.tech/docs/user/data-development/task-management/development-tasks.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  dppEtlTaskInstance: {
    title: '運用管理 — スケジュールタスクの実行監視と障害処理',
    content: 'ジョブとデータ開発タスクの実行インスタンスを一元管理し、実行ログ、タスク依存関係、実行状態の表示をサポート。インスタンス再実行、ログダウンロードなどの機能を提供し、運用担当者がスケジューリングリンクを効率的に監視し、実行例外を迅速に特定・処理するのを支援します。 <a href="https://qdata.qiantong.tech/docs/user/data-development/operations-management.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  attTaskCat: {
    title: 'タスクカテゴリ — データ開発タスクの分類管理システム',
    content: 'ツリー型カテゴリ構造を通じてデータ統合などの開発タスクを分類管理し、多レベルディレクトリ設定とタスクバインドをサポート。ビジネスや部門ごとにタスクを分類し、迅速なフィルタリングと統一メンテナンスを容易にし、タスク管理の条理性とコラボレーション効率を向上させます。 <a href="https://qdata.qiantong.tech/docs/user/data-development/project-basic/integration-task-category.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  AttDataDevCat: {
    title: 'データ開発ディレクトリ — データ開発タスクのツリー型分類管理',
    content: '多層ディレクトリ構造を通じてリアルタイムとオフラインデータ開発タスクを分類組織し、柔軟な作成とカテゴリバインドをサポート。ビジネスシナリオやプロジェクト次元でタスクを管理し、迅速な検索と効率的なメンテナンスを実現します。 <a href="https://qdata.qiantong.tech/docs/user/data-development/project-basic/development-task-category.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  AttProjectUserRel: {
    title: 'メンバーとロール管理 — プロジェクトコラボレーションの権限基盤',
    content: 'メンバー管理とロール管理モジュールを通じて、プロジェクト内人員の追加、ロール割り当て、細粒度の権限制御を実現。職責ごとにロールを定義し権限をバインドし、安全に分離された環境でのチームの効率的なコラボレーションを保証します。 <a href="https://qdata.qiantong.tech/docs/user/data-development/project-basic/member-role/" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  dsApi: {
    title: 'API管理 — データサービスの統一公開と共有',
    content: 'ウィザード設定またはSQLモードを通じてデータAPIを迅速に作成し、サービスフォワーディング、権限制御、ジオスペースサービスサポートを提供。プラットフォームデータの安全で効率的な公開と共有を容易にし、内外部システム統合とアプリケーション呼び出しニーズを満たします。 <a href="https://qdata.qiantong.tech/docs/user/data-services/api-management.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  dpDocument: {
    title: '標準登録 — 多源データ標準の統一管理',
    content: '<u>国家标准</u>、<u>行业标准</u>、<u>地方标准</u>、<u>団体标准</u>など various データ標準文書を一元管理し、アップロード、検索、バージョン管理、共有アプリケーションをサポート。統一収集とライフサイクル管理を通じて、データ定義に根拠を保証し、モデリング、品質などの側面での標準の効果的な実装を支えます。',
    type: 'remind',
    version: '1.0'
  },
  dataLayer: {
    title: '階層仕様 — 自動階層検証によるデータウェアハウス管理の再構築',
    content: '命名規則とビジネス次元に基づいてデータ階層（ディメンション、明細、集約など）を自動検証し、定義からスケジューリングまでのライフサイクル管理を提供。統一データ標準の実現、モデルメンテナンスコストの削減、データアセットの効率的ガバナンスとコンプライアンス要件の充足を容易にします。',
    type: 'remind',
    version: '1.0'
  },
  structured: {
    title: '収集タスク — 自動発見、統一管理',
    content: '収集タスクを設定し、指定されたデータソースからデータベース、テーブル、フィールドなどのメタデータを取得し、プラットフォーム仕様に従って解析、変換、格納します。メタデータの完全性、時効性、一貫性を保証し、ドメイン全体のデータアセットの動的感知と一元管理を実現します。 <a href="https://qdata.qiantong.tech/user/%E6%95%B0%E6%8D%AE%E7%A0%94%E5%8F%91/%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86/2%E3%80%81%E6%95%B0%E6%8D%AE%E5%BC%80%E5%8F%91.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  },
  unreleasedTable: {
    title: '最新メタデータ — データベースレベルアセットのコアユニットメンテナンスキャリア',
    content: 'テーブルの構造情報、ビジネス記述を管理し、テーブルの归属ビジネスドメイン、公開状態を維持し、収集から得られたテーブルレベルメタデータを更新することも可能です。 <a href="https://qdata.qiantong.tech/user/%E6%95%B0%E6%8D%AE%E7%A0%94%E5%8F%91/%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86/2%E3%80%81%E6%95%B0%E6%8D%AE%E5%BC%80%E5%8F%91.html" target="_blank">ヘルプドキュメントを確認</a>',
    type: 'remind',
    version: '1.0'
  }
}
