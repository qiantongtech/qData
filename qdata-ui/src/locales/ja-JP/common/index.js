// 共通翻訳（ボタン、メッセージ、フォーム、表示ラベル）
export default {
  license: {
    title: 'オープンソースライセンス',
    text: 'qData は Apache License 2.0 に基づき提供されており、以下の qData 固有の追加条件が適用されます：\n' +
        '\n' +
        '本ライセンスにおいて「qData」とは、フロントエンド、バックエンド、API、および関連文書を含む、ソフトウェアプロジェクト全体またはその一部を指します。\n' +
        '\n' +
        '本ライセンスにおいて「製作者」とは、江蘇千桐科技有限公司を指します。\n' +
        '\n' +
        '1. qData に表示されるロゴ、著作権表示、ライセンス表示、または帰属情報を削除、非表示、または改変しない限り、qData を商用目的で使用することができます。\n' +
        '\n' +
        '上記のロゴ、著作権表示、ライセンス表示、または帰属情報を削除、非表示、隠蔽、置換、または改変したい場合（ホワイトラベル使用、OEM 配布、リブランディング、または qData を別の製品として提示することを含むがこれに限定されない）、製作者から別の商用ライセンスを取得する必要があります。\n' +
        '\n' +
        '2. 貢献者として、以下のことに同意するものとします：\n' +
        '\n' +
        'a. 製作者は、必要に応じてオープンソース契約をより厳しく、または緩和することができます。\n' +
        'b. 貢献したコードは、クラウドビジネス運営を含むがこれに限定されない商用目的で使用される場合があります。\n' +
        '\n' +
        '上記の特定の条件を除き、その他のすべての権利と制限は Apache License 2.0 に従います。Apache License 2.0 の詳細については、http://www.apache.org/licenses/LICENSE-2.0 をご覧ください。\n' +
        '\n' +
        '本製品のインタラクティブデザインは外観特許により保護されています。\n' +
        '\n' +
        '© 2025 江蘇千桐科技有限公司'
  },
  button: {
    add: '新規追加',
    cancel: 'キャンセル',
    clear: 'クリア',
    close: '閉じる',
    confirm: '確認',
    copy: 'コピー',
    delete: '削除',
    details: '詳細',
    download: 'ダウンロード',
    expand: '展開',
    export: 'エクスポート',
    fold: '折りたたむ',
    import: 'インポート',
    linkParentChild: '親子連携',
    loading: '読み込み中、お待ちください',
    more: 'もっと見る',
    nextStep: '次へ',
    previousStep: '前へ',
    query: '検索',
    refresh: '更新',
    reset: 'リセット',
    return: '戻る',
    returnHome: 'ホームへ戻る',
    save: '保存',
    selectAll: '全選択',
    unselectAll: '全解除',
    un_fold: '展開/折りたたむ',
    update: '編集',
    upload: 'アップロード',
    neverShow: '今後表示しない',
    view: '表示'
  },
  error: {
    code401: '401エラー！',
    code403: 'この操作の権限がありません',
    code404: '404エラー！',
    default: 'システム不明のエラーが発生しました。管理者に連絡してください',
    findPage: 'ページが見つかりません！',
    notAccessRights: 'アクセス権限がありません！',
    Sorry401: '申し訳ございません、アクセス権限がありません。不正な操作は行わないでください！メインページに戻ることができます。',
    Sorry404: '申し訳ございません、お探しのページは存在しません。URLの誤りを確認し、ブラウザの更新ボタンを押すか、アプリケーション内で他のコンテンツを探してください。',
    network: 'バックエンドインターフェース接続エラー',
    timeout: 'システムインターフェースタイムアウト！'
  },
  form: {
    descriptionPlaceholder: '説明を入力してください',
    descriptionRequired: '説明は必須です',
    namePlaceholder: '名前を入力してください',
    nameRequired: '名前は必須です',
    remarkPlaceholder: '備考を入力してください',
    remarkRequired: '備考は必須です',
    statusPlaceholder: 'ステータスを選択してください',
    statusRequired: 'ステータスは必須です',
    startTimePlaceholder: '開始時間',
    endTimePlaceholder: '終了時間',
    singleSelect: '単一選択',
    multiSelect: '複数選択',
    startDatePlaceholder: '開始日',
    endDatePlaceholder: '終了日'
  },
  texts: {
    action: '操作',
    createdBy: '作成者',
    createdTime: '作成日時',
    description: '説明',
    name: '名前',
    number: '番号',
    operation: '操作',
    remark: '備考',
    status: 'ステータス',
    time: '時間',
    updatedBy: '更新者',
    updatedTime: '更新日時',
    enable: '有効',
    disable: '無効',
    topNode: 'トップノード',
    metadata: 'メタデータ',
    detail: '詳細',
    dataItem: 'データ項目',
    data: 'データ',
    success: '成功',
    sortOrder: '並び順',
    superAdmin: 'スーパー管理者'
  },
  graph: {
    inputComponent: '入力コンポーネント',
    outputComponent: '出力コンポーネント',
    transformComponent: '変換コンポーネント',
    zoomOut: '縮小',
    zoomIn: '拡大',
    resetView: 'ビューをリセット'
  },
  message: {
    addSuccess: '追加成功',
    deleteSuccess: '削除成功',
    downloadingData: 'データダウンロード中、お待ちください...',
    editSuccess: '更新成功',
    loading: '読み込み中...',
    loadingResource: 'システムリソースを読み込み中、お待ちください...',
    msgOpFailed: '操作失敗',
    msgOpSuccess: '操作成功',
    required: '必須項目です',
    confirmDelete: '番号のデータを削除してもよろしいですか：',
    confirm: 'よろしいですか',
    question: 'か？',
    success: '成功',
    noData: 'データなし',
    noMatchingData: '一致するデータなし',
    noRecord: '記録なし',
    operationSuccess: '操作成功',
    prompt: '確認',
    systemPrompt: 'システム確認',
    selectRecord: '削除するデータを選択してください'
  },
  html: {
    appTitle: 'qDataオープンソースデータプラットフォーム'
  },
  // ルール
  rules: {
    node: 'ノードは必須です',
    title: 'タイトルは必須です',
    validFlag: '有効フラグを指定してください',
    delFlag: '削除フラグを指定してください',
    createTime: '作成日時は必須です',
    updateTime: '更新日時は必須です'
  },
  // 表示
  display: {
    index: '番号',
    status: 'ステータス',
    createTime: '作成日時',
    updateTime: '更新日時',
    startTime: '開始時間',
    endTime: '終了時間',
    info: '情報',
    context: '内容を入力してください',
    title: 'タイトル',
    createBy: '作成者',
    content: '内容',
    remark: '備考',
    importResult: 'インポート結果',
    noselectData: 'データが選択されていません',
    selectData: 'データを選択してください',
    pleaseSelect: '選択してください',
    componentOne: 'コンポーネント1',
    componentTwo: 'コンポーネント2'
  },
  qualityTrends: '品質トレンド',
  shortcut: {
    title: 'ショートカット',
    save: '保存',
    check: '検証',
    format: 'フォーマット',
    formatSelection: '選択範囲をフォーマット',
    notes: 'コメント/コメント解除',
    upperCase: '大文字に変換',
    lowerCase: '小文字に変換',
    query: '検索'
  },
  noData: 'データなし',
  noMessage: 'メッセージなし',
  noHistory: '履歴なし',
  noPermission: '権限なし',
      time: {
      minutesAgo: '分前',
      hoursAgo: '時間前',
      daysAgo: '日前',
      monthsAgo: 'ヶ月前',
      yearsAgo: '年前',
      second: '秒',
      millisecond: 'ミリ秒'
    },
  upload: {
    dragOrClick: 'ファイルをここにドラッグ、またはクリックしてアップロード',
    updateExistingData: '既存データを更新する',
    fileFormat: 'xls、xlsx形式のファイルのみインポート可能です。',
    downloadTemplate: 'テンプレートダウンロード',
    selectFile: 'ファイル選択',
    uploadFile: 'ファイルアップロード',
    uploadAttachment: '添付ファイルアップロード',
    fileSizeLimit: 'サイズ制限',
    fileFormatLabel: '形式は',
    supportedFormat: 'アップロード対応形式のみ',
    fileSizeMB: 'MB',
    pleaseUpload: 'アップロードしてください',
    fileTypes: 'のファイル',
    fileName: 'ファイル名',
    fileSize: 'ファイルサイズ',
    filePath: 'ファイルパス',
    fileType: 'ファイル形式',
    search: '検索',
    uploadFailedAdmin: 'アップロード失敗、管理者にお問い合わせください',
    uploadResult: 'アップロード結果：',
    selectOneFileOnly: '1つのファイルのみ選択可能です',
    noFileSelected: 'ファイルが選択されていません。ファイルを選択してから再試行してください'
  },
  noSearchResult: '検索結果なし',
  noDescription: '説明なし',
  noLog: 'ログなし',
  noOpinion: '意見なし',
  noAssetLineage: 'アセット系譜なし',
  noAssetQualityTask: 'アセット品質タスクなし',
  noTaskProcess: 'タスクフローなし',
  request: {
    expiredSession: '無効なセッション、またはセッションの有効期限が切れています。再ログインしてください。',
    networkError: 'バックエンドインターフェース接続エラー',
    timeout: 'システムインターフェースリクエストタイムアウト',
    interfaceError: 'システムインターフェース {code} エラー',
    repeatSubmit: 'データ処理中です。重複して送信しないでください',
    routeChangeCancel: 'Route change: Request canceled',
    downloadError: 'ファイルダウンロードエラー、管理者にお問い合わせください！',
    downloading: 'データダウンロード中、お待ちください',
    loginExpired: 'ログイン状態の有効期限が切れました。このページに留まるか、再ログインしてください',
    reLogin: '再ログイン'
  },
  crontab: {
    tab: {
      second: '秒',
      minute: '分',
      hour: '時',
      day: '日',
      month: '月',
      week: '週',
      year: '年'
    },
    timeExpression: '時間式',
    cronExpression: 'Cron式',
    multiSelect: '複数選択可',
    specify: '指定',
    notSpecify: '指定なし',
    cycleFrom: '周期開始',
    noSecondConfigurable: '秒レベルのスケジューリング周期は設定できません！',
    second: {
      wildcard: '秒、使用可能なワイルドカード [, - * /]',
      cycleSuffix: '秒',
      averagePrefix: '',
      averageMiddle: '秒から、',
      averageSuffix: '秒ごとに実行'
    },
    minute: {
      wildcard: '分、使用可能なワイルドカード [, - * /]',
      cycleSuffix: '分',
      averagePrefix: '',
      averageMiddle: '分から、',
      averageSuffix: '分ごとに実行'
    },
    hour: {
      wildcard: '時、使用可能なワイルドカード [, - * /]',
      cycleSuffix: '時',
      averagePrefix: '',
      averageMiddle: '時から、',
      averageSuffix: '時間ごとに実行'
    },
    day: {
      wildcard: '日、使用可能なワイルドカード [, - * ? / L W]',
      cycleSuffix: '日',
      averagePrefix: '',
      averageMiddle: '日から、',
      averageSuffix: '日ごとに実行',
      nearestWorkday: '毎月',
      nearestWorkdaySuffix: '日に最も近い営業日',
      lastDay: '月の最終日'
    },
    month: {
      wildcard: '月、使用可能なワイルドカード [, - * /]',
      cycleSuffix: '月',
      averagePrefix: '',
      averageMiddle: '月から、',
      averageSuffix: 'ヶ月ごとに実行',
      names: {
        jan: '1月',
        feb: '2月',
        mar: '3月',
        apr: '4月',
        may: '5月',
        jun: '6月',
        jul: '7月',
        aug: '8月',
        sep: '9月',
        oct: '10月',
        nov: '11月',
        dec: '12月'
      }
    },
    week: {
      wildcard: '週、使用可能なワイルドカード [, - * ? / L #]',
      nthWeekPrefix: '第',
      nthWeekSuffix: '週の',
      lastPrefix: '当月最後の',
      names: {
        sun: '日曜日',
        mon: '月曜日',
        tue: '火曜日',
        wed: '水曜日',
        thu: '木曜日',
        fri: '金曜日',
        sat: '土曜日'
      }
    },
    year: {
      wildcard: '未入力、使用可能なワイルドカード [, - * /]',
      every: '毎年',
      averagePrefix: '',
      averageMiddle: '年から、',
      averageSuffix: '年ごとに実行'
    },
    result: {
      title: '直近5回の実行時間',
      calculating: '計算中...',
      noResult: '条件に一致する結果がありません！',
      limit: '直近100年以内に{count}件の結果のみ表示されています！'
    },
    cronUtils: {
      execute: '実行',
      invalidExpression: '無効なCron式'
    }
  }
}
