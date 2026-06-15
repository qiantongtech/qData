export default {
  chat: {
    // === ConversationList 对话列表 ===
    newConversation: '新建对话',
    searchHistory: '搜索历史记录',
    pinTop: '置顶',
    unpinTop: '取消置顶',
    renameTitle: '修改标题',
    titleLabel: '标题',
    titlePlaceholder: '请输入标题',
    titleRequired: '标题不能为空',
    renameSuccess: '重命名成功',
    confirmDeleteConversation: '是否确认删除对话 - {title}?',
    conversationDeleted: '对话已删除',
    pinned: '置顶',
    today: '今天',
    oneDayAgo: '一天前',
    threeDaysAgo: '三天前',
    sevenDaysAgo: '七天前',
    thirtyDaysAgo: '三十天前',

    // === DataScopeConfig 数据范围配置 ===
    newChat: '新对话',
    dimensionTablesCount: '+ {count}张维表',
    dataScope: '数据范围',
    configureDataScope: '请配置当前数据范围',
    dataSource: '数据源',
    selectDataSource: '请选择数据源',
    factTable: '事实表',
    selectFactTable: '请选择事实表',
    dimensionTable: '关联维表',
    selectDimensionTable: '请选择关联维表',
    confirmScopeAndStart: '确认范围并开始问答',
    setAssociation: '设置关联关系',
    setAssociationTitle: '设置关联关系',
    selectDimensionTableName: '请选择维度表',
    selectDimensionColumn: '请选择维表字段',
    fieldName: '字段名称',
    chineseName: '中文名称',
    dataType: '数据类型',
    dimensionTableShort: '维度表',
    dimensionColumn: '维表字段',
    cannotIdentifyAssociation: '关联关系无法自动识别，是否需要手动设置关联关系？',
    cannotConfirmSession: '无法确定当前会话 ID，请先保存配置并开始问答。',
    selectDimensionColumnForField: '字段 [{name}] 已选择维度表，请选择对应的维表字段',
    setAtLeastOneAssociation: '请至少设置一个关联关系',
    setAssociationSuccess: '设置关联关系成功',

    // === Message 消息相关 ===
    insight: '智能洞察',
    analyzing: '问题分析中...',
    suggestedAsk: '试着问问',
    relationshipNotIdentified: '关联关系无法自动识别',
    dialogError: '对话异常',
    analyzingData: '正在分析数据并生成报表，请稍候...',
    thinking: '正在思考中...',
    visualization: '可视化',
    detailData: '明细数据',
    data: '数据',
    dataLabel: '数据{n}',
    downloadChart: '下载图表',
    copySuccess: '复制成功！',
    exportSuccess: '导出成功！',
    exportFailed: '导出失败，请重试！',
    exporting: '正在导出，请稍候...',
    chartFile: '图表_{time}',
    detailFile: '明细数据_{time}',
    confirmDeleteMessage: '是否确认删除？',

    // === MessageListEmpty / 空状态 ===
    welcomeGreeting: 'Hello，我是 qData 智能问数，很高兴见到你!',
    welcomeSubheading: '化繁为简，让数据分析更高效。',
    inputPlaceholder: '问问 qData 智能问数...（Shift+Enter 换行，按下 Enter 发送）',
    selectModel: '选择模型',
    answerType: '回答方式',
    disclaimer: '本功能由 qData 智能问数生成，其回答未必正确无误。',
    configureDataScopeFirst: '请先配置当前数据范围！',

    // === MessageListEmpty2 空状态2 ===
    lingtongAI: '灵桐 AI',

    // === MessageNewConversation 新建对话 ===
    startConversation: '点击下方按钮，开始你的对话吧',

    // === Chat Index 主页 ===
    clearSession: '清空会话',
    navDown: '下',
    navUp: '上',
    send: '发送',
    stop: '停止',
    cannotSwitchDuringConversation: '对话中，不允许切换!',
    cannotDeleteDuringAnswer: '回答中，不能删除!',
    confirmClearMessages: '确认清空对话消息？',
    sendFailedEmpty: '发送失败，原因：内容为空！',
    selectAnswerType: '请先选择回答方式（智能问答或智能图表）！',

    // === AssistantReportCard 报表卡片 ===
    quantity: '数量',

    // === MessageListEmpty2 prompt ===
    promptWeather: '今天气怎么样?',
    promptPoem: '写一首好听的诗歌?',

    // === constants.js ===
    smartChart: '智能图表',
    smartQA: '智能问答'
  },
  model: {
    modelName: '模型名称',
    modelNamePlaceholder: '请输入模型名称',
    platform: '平台',
    selectPlatform: '请选择平台',
    apiUrl: 'API地址',
    apiUrlPlaceholder: '请输入API地址',
    apiKey: 'API秘钥',
    apiKeyPlaceholder: '请输入API秘钥',
    sortOrder: '排序',
    maxLengthChars: '500个字符',
    addModel: '添加模型',
    editModel: '修改模型',
    modelDetail: '模型详情',
    modelNameRequired: '模型名称不能为空',
    platformRequired: '平台不能为空',
    apiAtLeastOne: 'API地址和API秘钥至少填写一个',
    modelImport: '模型导入',
    importResult: '导入结果',
    clickUpload: '点击上传',
    updateExistingModels: '是否更新已经存在的模型数据',
    confirmDeleteModel: '是否确认删除模型编号为"{ids}"的数据项？',
    confirmStatusChange: '确认要"{status}","{name}"主题吗？',
    statusSuccess: '{status}成功'
  }
}