export default {
  catEditDialog: {
    parentIdLabel: '上级类目',
    parentIdPlaceholder: '请选择上级',
    enable: '启用',
    disable: '禁用',
    sortOrder: '排序',
    charLimit: '500个字符',
    nameLabel: '类目名称',
    namePlaceholder: '请输入{nameLabel}',
    parentIdRequired: '上级类目不能为空',
    codeRequired: '编码不能为空',
    nameRequired: '{nameLabel}不能为空',
    editTitle: '编辑',
    addTitle: '新增{nameLabel}',
    modifyTitle: '修改{nameLabel}'
  },
  catPage: {
    nameLabel: '类目名称',
    titleBase: '类目',
    sortOrder: '排序',
    parentCat: '上级类目',
    enable: '启用',
    disable: '禁用',
    confirmEnableDisable: '确认要"{text}","{name}"{titleBase}吗？',
    operationSuccess: '{text}成功',
    addTitle: '添加{titleBase}',
    modifyTitle: '修改{titleBase}',
    deleteConfirm: '是否确认删除{titleBase}编号为"{id}"的数据项？',
    batchDeleteConfirm: '可删除{canDeleteCount}个，不可删除{cannotDeleteCount}个，是否删除可删部分',
    executeSuccess: '执行成功',
    batchDeleteAllConfirm: '可删除{count}个，不可删除0个，是否删除可删部分'
  },
  crontab: {
    second: '秒',
    minute: '分钟',
    hour: '小时',
    day: '日',
    month: '月',
    week: '周',
    year: '年',
    timeExpression: '时间表达式',
    cronExpression: 'Cron 表达式',
    secondLevelError: '不可配置秒级别调度周期!'
  },
  crontabSecond: {
    wildcard: '秒，允许的通配符[, - * /]',
    cycleFrom: '周期从',
    cycleTo: '-',
    seconds: '秒',
    startFrom: '从',
    executeEvery: '秒开始，每',
    executeOnce: '秒执行一次',
    specify: '指定'
  },
  deleteConfirmDialog: {
    delete: '删除',
    warning: '警告：',
    cannotUndo: '删除无法撤消！请慎重操作！',
    deletePrompt: '该操作将永久删除编号',
    deletePromptSuffix: '的数据项，同时可能取消与之关联的关系。',
    confirmInputPrompt: '为防止意外，确认继续操作请输入以下内容:',
    inputPlaceholder: '请输入{verificationText}以确认继续操作',
    confirmDelete: '确认删除',
    inputError: '请输入正确的验证文本: {verificationText}',
    defaultVerifyText: '立即删除'
  },
  deptTree: {
    searchPlaceholder: '请输入部门名称',
    defaultTitle: '类目',
    addChild: '新增子级',
    addSibling: '新增同级',
    edit: '编辑',
    deleteConfirm: '是否确认删除"{name}"？',
    loadSuccess: '加载成功',
    loadError: '加载失败，点击重试'
  },
  logDialog: {
    title: '日志详情',
    copySuccess: '复制成功！'
  },
  qtWrap: {
    hideSearch: '隐藏搜索',
    showHideColumns: '显隐列'
  },
  qtSearchBar: {
    inputPlaceholder: '请输入{label}',
    selectPlaceholder: '请选择{label}'
  },
  rightToolbar: {
    hideSearch: '隐藏搜索',
    showSearch: '显示搜索',
    showHideColumns: '显隐列',
    show: '显示',
    hide: '隐藏',
    dialogTitle: '显示/隐藏',
    collapse: '收起',
    expand: '展开',
    showHide: '显示/隐藏',
  },
  fileUploadbtn: {
    fileFormatError: '文件格式不正确，请上传 {fileTypes} 格式文件',
    fileSizeError: '文件大小不能超过 {fileSize}MB',
    uploadError: '上传文件失败',
    exceedLimit: '最多只能上传 {limit} 个文件'
  },
  fileUpload: {
    fileFormatError: '文件格式不正确, 请上传{fileTypes}格式文件!',
    fileSizeError: '上传文件大小不能超过 {fileSize} MB!',
    exceedLimit: '上传文件数量不能超过 {limit} 个!',
    uploadError: '上传文件失败',
    uploading: '正在上传文件，请稍候...'
  },
  fileUpload2: {
    fileFormatError: '文件格式不正确, 请上传{fileTypes}格式文件!',
    exceedLimit: '上传文件数量不能超过 {limit} 个!',
    uploadError: '上传文件失败',
    uploading: '正在上传文件，请稍候...'
  },
  imageUpload: {
    fileFormatError: '文件格式不正确, 请上传{fileTypes}图片格式文件!',
    fileSizeError: '上传头像图片大小不能超过 {fileSize} MB!',
    exceedLimit: '上传文件数量不能超过 {limit} 个!',
    uploadError: '上传图片失败',
    uploading: '正在上传图片，请稍候...',
    preview: '预览'
  },
  iconSelect: {
    searchPlaceholder: '请输入图标名称'
  },
  sizeSelect: {
    large: '较大',
    default: '默认',
    small: '稍小',
    settingSize: '正在设置布局大小，请稍候...'
  },
  sqlEditor: {
    logConsole: '日志控制台',
    queryResult: '查询结果',
    executionHistory: '执行历史记录',
    attrConfig: '属性配置'
  },
  sqlEditorConfigView: {
    basicConfig: '基础配置',
    taskPriority: '任务优先级:',
    taskPriorityPlaceholder: '请选择任务优先级',
    workerGroup: 'Worker分组:',
    workerGroupPlaceholder: '请输入Worker分组',
    failRetryTimes: '失败重试次数:',
    failRetryTimesPlaceholder: '请输入失败重试次数',
    times: '次',
    failRetryInterval: '失败重试间隔:',
    failRetryIntervalPlaceholder: '请输入失败重试间隔',
    minutes: '分',
    delayTime: '延迟执行时间:',
    delayTimePlaceholder: '请输入延迟执行时间',
    jobManagerMemory: 'JobManager内存数',
    jobManagerMemoryPlaceholder: '请输入JobManager内存数',
    taskManagerMemory: 'TaskManager内存数',
    taskManagerMemoryPlaceholder: '请输入TaskManager内存数',
    slot: 'Slot数量',
    slotPlaceholder: '请输入Slot数量',
    taskManager: 'TaskManager数量',
    taskManagerPlaceholder: '请输入TaskManager数量',
    parallelism: '并行度',
    parallelismPlaceholder: '请输入并行度',
    yarnQueue: 'Yarn队列',
    yarnQueuePlaceholder: '请输入Yarn队列(选填)',
    driverCores: 'Driver核心数',
    driverCoresPlaceholder: '请输入Driver核心数',
    driverMemory: 'Driver内存数',
    driverMemoryPlaceholder: '请输入Driver内存数',
    numExecutors: 'Executor数量',
    numExecutorsPlaceholder: '请输入Executor数量',
    executorMemory: 'Executor内存数',
    executorMemoryPlaceholder: '请输入Executor内存数',
    executorCores: 'Executor核心数',
    executorCoresPlaceholder: '请输入Executor核心数',
    otherConfig: '其他配置',
    dataConnectionType: '数据连接类型:',
    datasourceConnection: '数据源连接:',
    datasourceConnectionPlaceholder: '请选择数据源连接',
    sqlType: 'SQL类型:',
    segmentSymbol: '分段执行符号:',
    segmentSymbolPlaceholder: '请输入分段执行符号',
    paramsConfig: '参数配置',
    customParams: '自定义参数:',
    paramNamePlaceholder: '参数名称',
    paramTypePlaceholder: '请选择参数类型',
    paramValuePlaceholder: '值',
    addConfigItem: '添加配置项',
    nonQuery: '非查询',
    storedProcedure: '储存过程',
    sparkSql: 'SparkSql开发',
    flinkSql: 'FlinkSql开发',
    taskPriorityRequired: '任务优先级不能为空',
    workerGroupRequired: 'Worker分组不能为空',
    datasourceIdRequired: '数据源连接不能为空',
    sqlTypeRequired: 'SQL类型不能为空'
  },
  datasource: {
    ossAliyun: 'OSS(阿里云)',
    api: 'API接口',
    file: 'excel、csv文件',
    flinkBatch: 'Flink批',
    flinkStream: 'Flink流'
  },
  crontabResult: {
    lastFiveRunTimes: '最近5次运行时间',
    calculating: '计算结果中...',
    noResults: '没有达到条件的结果！',
    limitedResults: '最近100年内只有上面{count}条结果！'
  },
  markdownView: {
    reference: '引用',
    copy: '复制',
    thinking: '思考中……',
    copySuccess: '复制成功!'
  },
  sqlEditorConsole: {
    fetchLatestData: '获取最新数据',
    checkJob: '检查作业',
    executeJob: '执行作业',
    buildConfig: '构建配置信息'
  },
  topNav: {
    moreMenus: '更多菜单'
  },
  editor: {
    placeholder: '请输入内容',
    save: '保存',
    text: '文本',
    heading1: '标题1',
    heading2: '标题2',
    heading3: '标题3',
    heading4: '标题4',
    heading5: '标题5',
    heading6: '标题6',
    standardFont: '标准字体',
    serifFont: '衬线字体',
    monospaceFont: '等宽字体',
    linkPlaceholder: '请输入链接地址:',
    videoPlaceholder: '请输入视频地址:',
    imageFormatError: '图片格式错误!',
    fileSizeError: '上传文件大小不能超过 {size} MB!',
    imageUploadFailed: '图片插入失败'
  },
  settings: {
    themeSettings: '主题风格设置',
    themeColor: '主题颜色',
    layoutSettings: '系统布局配置',
    tagsView: '开启 Tags-Views',
    fixedHeader: '固定 Header',
    showLogo: '显示 Logo',
    dynamicTitle: '动态标题',
    saveSettings: '保存配置',
    resetSettings: '重置配置',
    savingLoading: '正在保存到本地，请稍候...',
    resetLoading: '正在清除设置缓存并刷新，请稍候...'
  },
  tagsView: {
    refreshPage: '刷新页面',
    closeCurrent: '关闭当前',
    closeOthers: '关闭其他',
    closeLeft: '关闭左侧',
    closeRight: '关闭右侧',
    closeAll: '全部关闭'
  },
  fullScreen: {
    exit: '退出全屏',
    enter: '全屏'
  },
  cleanRule: {
    numberBoundary: '数值边界值调整',
    decimalFormatter: '小数位数',
    affixEditor: '字段前缀/后缀统一',
    enumMap: '枚举值映射标准化',
    combinerFieldUnique: '按组合字段去重',
    regexReplace: '正则表达式替换',
    cleanExpiredData: '清理过期记录',
    longFieldTruncator: '超长字段截断',
    emptyRule: '组合字段为空删除',
    fieldToLower: '字段转小写',
    dateFormatter: '日期格式统一',
    trimSpace: '去除字段空格',
    placeholder: '占位规则'
  }
}