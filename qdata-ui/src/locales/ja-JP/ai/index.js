export default {
  chat: {
    // === ConversationList ===
    newConversation: '新規会話',
    searchHistory: '履歴検索',
    pinTop: 'トップに固定',
    unpinTop: '固定解除',
    renameTitle: 'タイトル変更',
    titleLabel: 'タイトル',
    titlePlaceholder: 'タイトルを入力してください',
    titleRequired: 'タイトルは必須です',
    renameSuccess: '名前変更成功',
    confirmDeleteConversation: '会話「{title}」を削除してもよろしいですか？',
    conversationDeleted: '会話が削除されました',
    pinned: '固定済み',
    today: '今日',
    oneDayAgo: '1日前',
    threeDaysAgo: '3日前',
    sevenDaysAgo: '7日前',
    thirtyDaysAgo: '30日前',

    // === DataScopeConfig ===
    newChat: '新規チャット',
    dimensionTablesCount: '+ {count}件のディメンションテーブル',
    dataScope: 'データスコープ',
    configureDataScope: '現在のデータスコープを設定してください',
    dataSource: 'データソース',
    selectDataSource: 'データソースを選択してください',
    factTable: 'ファクトテーブル',
    selectFactTable: 'ファクトテーブルを選択してください',
    dimensionTable: 'ディメンションテーブル',
    selectDimensionTable: 'ディメンションテーブルを選択してください',
    confirmScopeAndStart: 'スコープを確認してQ&Aを開始',
    setAssociation: '関連付け設定',
    setAssociationTitle: '関連付け設定',
    selectDimensionTableName: 'ディメンションテーブルを選択してください',
    selectDimensionColumn: 'ディメンションテーブルのフィールドを選択してください',
    fieldName: 'フィールド名',
    chineseName: '中国語名',
    dataType: 'データタイプ',
    dimensionTableShort: 'ディムテーブル',
    dimensionColumn: 'ディムフィールド',
    cannotIdentifyAssociation: '関連付けを自動識別できません。手動で設定しますか？',
    cannotConfirmSession: '現在のセッションIDを特定できません。設定を保存してQ&Aを開始してください。',
    selectDimensionColumnForField: 'フィールド [{name}] はディメンションテーブルが選択済みです。対応するディムフィールドを選択してください',
    setAtLeastOneAssociation: '少なくとも1つの関連付けを設定してください',
    setAssociationSuccess: '関連付け設定成功',

    // === Message ===
    insight: 'スマートインサイト',
    analyzing: '分析中...',
    suggestedAsk: '試してみてください',
    relationshipNotIdentified: '関連付けを自動識別できません',
    dialogError: '対話エラー',
    analyzingData: 'データを分析してレポートを生成中、お待ちください...',
    thinking: '思考中...',
    visualization: '可視化',
    detailData: '詳細データ',
    data: 'データ',
    dataLabel: 'データ{n}',
    downloadChart: 'チャートダウンロード',
    copySuccess: 'コピー成功！',
    exportSuccess: 'エクスポート成功！',
    exportFailed: 'エクスポート失敗、再試行してください！',
    exporting: 'エクスポート中、お待ちください...',
    chartFile: 'チャート_{time}',
    detailFile: '詳細データ_{time}',
    confirmDeleteMessage: '削除してもよろしいですか？',

    // === MessageListEmpty ===
    welcomeGreeting: 'こんにちは、qDataスマートQ&Aです。よろしくお願いいたします！',
    welcomeSubheading: '複雑さをシンプルに、データ分析をより効率的に。',
    inputPlaceholder: 'qDataスマートQ&Aに質問...（Shift+Enterで改行、Enterで送信）',
    selectModel: 'モデル選択',
    answerType: '回答方式',
    disclaimer: '本機能はqDataスマートQ&Aにより生成されています。回答が必ずしも正確とは限りません。',
    configureDataScopeFirst: 'まず現在のデータスコープを設定してください！',

    // === MessageListEmpty2 ===
    lingtongAI: '霊桐AI',

    // === MessageNewConversation ===
    startConversation: '下のボタンをクリックして会話を始めましょう',

    // === Chat Index ===
    clearSession: 'セッションクリア',
    navDown: '下',
    navUp: '上',
    send: '送信',
    stop: '停止',
    cannotSwitchDuringConversation: '会話中は切り替えできません！',
    cannotDeleteDuringAnswer: '回答中は削除できません！',
    confirmClearMessages: 'すべての対話メッセージをクリアしてもよろしいですか？',
    sendFailedEmpty: '送信失敗：内容が空です！',
    selectAnswerType: 'まず回答方式（スマートQ&Aまたはスマートチャート）を選択してください！',

    // === AssistantReportCard ===
    quantity: '数量',

    // === MessageListEmpty2 prompt ===
    promptWeather: '今日の天気はどうですか？',
    promptPoem: '素敵な詩を書いてください？',

    // === constants.js ===
    smartChart: 'スマートチャート',
    smartQA: 'スマートQ&A'
  },
  model: {
    modelName: 'モデル名',
    modelNamePlaceholder: 'モデル名を入力してください',
    platform: 'プラットフォーム',
    selectPlatform: 'プラットフォームを選択してください',
    apiUrl: 'APIアドレス',
    apiUrlPlaceholder: 'APIアドレスを入力してください',
    apiKey: 'APIシークレットキー',
    apiKeyPlaceholder: 'APIシークレットキーを入力してください',
    sortOrder: '並び順',
    maxLengthChars: '500文字',
    addModel: 'モデル追加',
    editModel: 'モデル編集',
    modelDetail: 'モデル詳細',
    modelNameRequired: 'モデル名は必須です',
    platformRequired: 'プラットフォームは必須です',
    apiAtLeastOne: 'APIアドレスとAPIシークレットキーの少なくとも1つを入力してください',
    modelImport: 'モデルインポート',
    importResult: 'インポート結果',
    clickUpload: 'クリックしてアップロード',
    updateExistingModels: '既存のモデルデータを更新しますか',
    confirmDeleteModel: 'モデル番号「{ids}」のデータ項目を削除してもよろしいですか？',
    confirmStatusChange: 'トピック「{name}」を「{status}」にしてもよろしいですか？',
    statusSuccess: '{status}成功'
  }
}

