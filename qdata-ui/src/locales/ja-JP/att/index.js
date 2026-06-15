import common from './common';
export default {
  common,
  assetCat: {
    table: {
      name: 'データ資産カテゴリ名',
      sortOrder: 'ソート順'
    },
    validations: {
      nameRequired: 'データ資産カテゴリ名は必須です',
      parentIdRequired: '親カテゴリは必須です'
    },
    messages: {
      confirmDelete: 'データ資産カテゴリ番号"{name}"のデータ項目を削除しますか？'
    },
    title: {
      add: 'データ資産カテゴリ追加',
      edit: 'データ資産カテゴリ編集'
    }
  },
  attDocCat: {
    table: {
      name: 'データ文書名',
      sortOrder: 'ソート順'
    },
    title: {
      add: 'データ文書カテゴリ追加',
      edit: 'データ文書カテゴリ編集'
    },
    messages: {
      confirmDelete: 'データ文書管理番号"{name}"のデータ項目を削除しますか？'
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
    texts: {
      name: 'ルール名',
      qualityDim: '品質次元',
      scenario: '使用シナリオ',
      example: '例'
    },
    form: {
      codePlaceholder: 'コードを入力してください',
      descriptionPlaceholder: '説明を入力してください'
    },
    qualityDimRequired: '品質次元は必須です',
    title: {
      add: '検査ルール追加',
      edit: '検査ルール編集',
      detail: '検査ルール詳細'
    },
    importTitle: '検査ルールインポート',
    deleteConfirm: '検査ルール番号"{ids}"のデータ項目を削除しますか？',
    treeRootName: '品質次元'
  },
  cleanCat: {
    table: {
      name: 'クリーニングルールカテゴリ名',
      sortOrder: 'ソート順'
    },
    texts: {
      name: 'カテゴリ名',
      parentId: '親ID',
      sortOrder: 'ソート順',
      hierarchyCode: '階層コード'
    },
    title: {
      add: 'クリーニングルールカテゴリ追加',
      edit: 'クリーニングルールカテゴリ編集',
      detail: 'クリーニングルールカテゴリ詳細'
    },
    importTitle: 'クリーニングルールカテゴリインポート',
    messages: {
      confirmDelete: 'クリーニングルールカテゴリ番号"{ids}"のデータ項目を削除しますか？'
    }
  },
  cleanRule: {
    table: {
      code: 'コード',
      name: 'ルール名',
      type: 'ルールタイプ',
      useCase: '使用シナリオ',
      example: '例'
    },
    texts: {
      name: 'ルール名',
      type: 'ルールタイプ',
      level: 'ルールレベル'
    },
    title: {
      add: 'クリーニングルール追加',
      edit: 'クリーニングルール編集',
      detail: 'クリーニングルール詳細'
    },
    importTitle: 'クリーニングルールインポート',
    deleteConfirm: '番号"{ids}"のデータ項目を削除しますか？',
    cleanRuleCategory: 'クリーニングルールカテゴリ'
  },
  dataElemCat: {
    table: {
      name: 'データ要素カテゴリ名',
      sortOrder: 'ソート順'
    },
    title: {
      add: 'データ要素カテゴリ追加',
      edit: 'データ要素カテゴリ編集'
    },
    messages: {
      confirmDelete: 'データ要素カテゴリ管理番号"{name}"のデータ項目を削除しますか？'
    }
  },
  documentCat: {
    table: {
      name: '標準カテゴリ名',
      sortOrder: 'ソート順'
    },
    title: {
      add: '標準情報分類管理追加',
      edit: '標準情報分類管理編集'
    },
    messages: {
      confirmDelete: '標準情報分類管理番号"{ids}"のデータ項目を削除しますか？'
    }
  },
  modelCat: {
    table: {
      name: '論理モデルカテゴリ名',
      sortOrder: 'ソート順'
    },
    title: {
      add: '論理モデルカテゴリ追加',
      edit: '論理モデルカテゴリ編集'
    },
    messages: {
      confirmDelete: '論理モデルカテゴリ管理番号"{name}"のデータ項目を削除しますか？'
    }
  },
  project: {
    table: {
      name: 'プロジェクト名',
      manager: '管理者'
    },
    texts: {
      name: 'プロジェクト名',
      description: 'プロジェクト説明',
      manager: '管理者',
      contact: '連絡先'
    },
    title: {
      add: 'プロジェクト追加',
      edit: 'プロジェクト編集',
      detail: 'プロジェクト詳細'
    },
    importTitle: 'プロジェクトインポート',
    message: {
      confirmDelete: 'プロジェクト番号"{ids}"のデータ項目を削除しますか？'
    }
  },
  qualityCat: {
    table: {
      name: 'データ品質カテゴリ名',
      sortOrder: 'ソート順'
    },
    title: {
      add: 'データ品質カテゴリ追加',
      edit: 'データ品質カテゴリ編集'
    },
    messages: {
      confirmDelete: 'データ品質カテゴリ管理番号"{name}"のデータ項目を削除しますか？'
    }
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
    },
    title: {
      add: 'タグ管理追加',
      edit: 'タグ管理編集',
      detail: 'タグ管理詳細'
    },
    message: {
      deleteConfirm: '「"<ids>"」のデータ項目を削除しますか？',
      batchDeleteConfirm: '選択したデータ項目を一括削除しますか？'
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
    },
    texts: {
      name: 'テーマ名',
      icon: 'アイコン',
      sortOrder: 'ソート順'
    },
    title: {
      add: 'テーマ追加',
      edit: 'テーマ編集',
      detail: 'テーマ詳細'
    },
    importTitle: 'テーマインポート',
    deleteConfirm: 'テーマ番号"{ids}"のデータ項目を削除しますか？',
    themeWord: 'テーマ'
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
      confirmStatus: '「{status}」「{name}」ソースシステムを実行しますか？',
      statusSuccess: '{status}成功',
      deleteConfirm: '削除可能{deletable}件、削除不可{undeletable}件です。削除可能なものを削除しますか'
    },
    textareaLimit: '500文字'
  }
}