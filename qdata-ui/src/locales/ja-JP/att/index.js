export default {
  common: {
    dataAssetCatName: 'データ資産カテゴリ名',
    dataDocCatName: 'データ文書名',
    cleanCatName: 'クリーニングルールカテゴリ名',
    dataElemCatName: 'データ要素カテゴリ名',
    documentCatName: '標準カテゴリ名',
    modelCatName: '論理モデルカテゴリ名',
    qualityCatName: 'データ品質カテゴリ名',
    tagCatName: 'タグ管理カテゴリ名',
    parentCat: '親カテゴリ',
    parentCatRequired: '親カテゴリは必須です',
    categoryName: 'カテゴリ名',
    sortOrder: 'ソート順',
    upperCat: '親カテゴリ',
    ruleName: 'ルール名',
    ruleNameRequired: 'ルール名は必須です',
    ruleType: 'ルールタイプ',
    ruleLevel: 'ルールレベル',
    useCase: '使用シナリオ',
    example: '例',
    projectName: 'プロジェクト名',
    projectNameRequired: 'プロジェクト名は必須です',
    manager: '管理者',
    managerRequired: '管理者は必須です',
    contactWay: '連絡先',
    systemName: 'システム名',
    systemType: 'システムタイプ',
    responsiblePerson: '責任者',
    contactPerson: '連絡担当者',
    tagName: 'タグ名',
    tagNameRequired: 'タグ名は必須です',
    nearSynonyms: '類義語',
    synonyms: '同義語',
    themeName: 'テーマ名',
    themeNameRequired: 'テーマ名は必須です',
    icon: 'アイコン',
    creator: '作成者',
    createTime: '作成時間',
    status: 'ステータス',
    remark: '備考',
    description: '説明',
    code: 'コード',
    codeRequired: 'コードは必須です',
    operation: '操作',
    query: '検索',
    reset: 'リセット',
    add: '追加',
    update: '更新',
    delete: '削除',
    detail: '詳細',
    addSuccess: '追加成功',
    updateSuccess: '更新成功',
    deleteSuccess: '削除成功',
    noData: 'データなし',
    pleaseEnter: '入力してください',
    pleaseSelect: '選択してください',
    confirmDelete: '削除確認',
    deleteTip: '選択したデータを削除してもよろしいですか？',
    disable: '無効',
    enable: '有効',
    // プレースホルダー
    pleaseEnterName: '名前を入力してください',
    pleaseSelectParent: '親を選択してください',
    pleaseSelectManager: '管理者を選択してください',
    // アセット関連
    catCode: 'カテゴリコード',
    assetCount: 'アセット数',
    assetInfo: 'アセット情報',
    detailInfo: '詳細情報',
    extendAlias: '拡張情報エイリアス',
    nearSynonymsTip: '複数ある場合は英語のカンマで区切ってください',
    synonymsTip: '複数ある場合は英語のカンマで区切ってください',
    // プレースホルダー
    codePlaceholder: 'コードを入力してください',
    ruleTypePlaceholder: 'ルールタイプを選択してください',
    useCasePlaceholder: '使用シナリオを入力してください',
    examplePlaceholder: '例を入力してください',
    qualityDimPlaceholder: '品質次元を選択してください',
    namePlaceholder: '名前を入力してください',
    sortOrderPlaceholder: 'ソート順を入力してください',
    statusPlaceholder: 'ステータスを入力してください',
    catCodePlaceholder: 'カテゴリコードを入力してください',
    assetCountPlaceholder: 'アセット数を入力してください',
    extendAliasPlaceholder: '拡張情報エイリアスを入力してください',
    nearSynonymsPlaceholder: '類義語を入力してください',
    synonymsPlaceholder: '同義語を入力してください',
    parentCatPlaceholder: '親を選択してください',
    tagCatPlaceholder: 'タグ管理カテゴリを選択してください',
    cleanRuleCatPlaceholder: '検査ルールカテゴリを入力してください',
    // タグ関連
    tagManagementCategory: 'タグ管理カテゴリ',
    assetCountTip: '現在このタグに関連付けられたデータ資産の総数',
    statusTip: '有効状態はそのタグがデータ資産のタグ付けに使用できることを示します。無効にすると使用できなくなりますが、既存のタグは保持されます。',
    createdByPlaceholder: '作成者を入力してください',
    confirmStatusChange: '「{status}」"{name}"タグを実行しますか？',
    confirmDelete: 'タグ番号"{ids}"のデータ項目を削除しますか？',
    tagCatRequired: 'タグ管理カテゴリは必須です',
    addTag: 'タグ追加',
    editTag: 'タグ編集',
    tagDetail: 'タグ詳細',
    ruleCategoryPlaceholder: '検査ルールカテゴリを入力してください'
  },
  assetCat: {
    table: {
      name: 'データ資産カテゴリ名',
      sortOrder: 'ソート順'
    }
  },
  attDocCat: {
    table: {
      name: 'データ文書名',
      sortOrder: 'ソート順'
    }
  },
  auditRule: {
    table: {
      code: 'コード',
      name: 'ルール名',
      type: 'ルールタイプ',
      qualityDim: '品質次元',
      useCase: '使用シナリオ',
      example: '例'
    },
    form: {

    }
  },
  cleanCat: {
    table: {
      name: 'クリーニングルールカテゴリ名',
      sortOrder: 'ソート順'
    }
  },
  cleanRule: {
    table: {
      code: 'コード',
      name: 'ルール名',
      type: 'ルールタイプ',
      useCase: '使用シナリオ',
      example: '例'
    }
  },
  dataElemCat: {
    table: {
      name: 'データ要素カテゴリ名',
      sortOrder: 'ソート順'
    }
  },
  documentCat: {
    table: {
      name: '標準カテゴリ名',
      sortOrder: 'ソート順'
    }
  },
  modelCat: {
    table: {
      name: '論理モデルカテゴリ名',
      sortOrder: 'ソート順'
    }
  },
  project: {
    table: {
      name: 'プロジェクト名',
      manager: '管理者'
    }
  },
  qualityCat: {
    table: {
      name: 'データ品質カテゴリ名',
      sortOrder: 'ソート順'
    }
  },
  sourceSystem: {
    table: {
      name: 'システム名',
      type: 'システムタイプ',
      responsiblePerson: '責任者',
      contactPerson: '連絡担当者',
      sortOrder: 'ソート順'
    },
    form: {
      name: 'システム名',
      type: 'システムタイプ',
      responsiblePerson: '責任者',
      contactPerson: '連絡担当者',
      sortOrder: 'ソート順',
      namePlaceholder: 'システム名を入力してください',
      typePlaceholder: 'システムタイプを選択してください',
      responsiblePersonPlaceholder: '責任者を選択してください',
      contactPersonPlaceholder: '連絡担当者を選択してください',
      enable: '有効',
      disable: '無効'
    },
    title: {
      add: 'ソースシステム追加',
      edit: 'ソースシステム編集',
      detail: 'ソースシステム詳細'
    },
    message: {
      nameRequired: 'システム名は必須です',
      typeRequired: 'システムタイプは必須です',
      confirmStatus: '「{status}」"{name}"ソースシステムを実行しますか？',
      statusSuccess: '{status}成功',
      deleteConfirm: '削除可能{deletable}件、削除不可{undeletable}件です。削除可能なものを削除しますか'
    },
    textareaLimit: '500文字'
  },
  tag: {
    table: {
      name: 'タグ名',
      catName: 'タグ管理カテゴリ',
      nearSynonyms: '類義語',
      synonyms: '同義語'
    },
    detail: {
      catName: 'タグ管理カテゴリ',
      nearSynonyms: '類義語',
      synonyms: '同義語'
    }
  },
  tagCat: {
    table: {
      name: 'タグ管理カテゴリ名'
    }
  },
  theme: {
    table: {
      name: 'テーマ名',
      icon: 'アイコン',
      sortOrder: 'ソート順'
    }
  }
}
