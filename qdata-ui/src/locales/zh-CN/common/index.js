// 通用翻译（按钮、消息、表单、展示标签）
export default {
  license: {
    title: '开源协议',
    text: 'qData 依据 Apache License 2.0 提供，并受以下针对 qData 的特定附加条件约束：\n' +
        '\n' +
        '在本协议中，"qData" 是指整个软件项目，包括但不限于其前端、后端、API 及相关文档的全部或部分内容。\n' +
        '\n' +
        '在本协议中，"制作方" 是指江苏千桐科技有限公司。\n' +
        '\n' +
        '1. 您可以将 qData 用于商业目的，但不得移除、隐藏或修改 qData 中显示的 qData 标识、版权声明、许可声明或归属信息。\n' +
        '\n' +
        '如果您希望移除、隐藏、遮蔽、替换或修改上述标识、版权声明、许可声明或归属信息，包括但不限于白标使用、OEM 分发、品牌重塑或将 qData 作为其他产品展示，您必须向制作方另行申请商业许可。\n' +
        '\n' +
        '2. 作为贡献者，您应当同意：\n' +
        '\n' +
        'a. 制作方可根据需要调整开源协议，使其更为严格或宽松。\n' +
        'b. 您贡献的代码可能被用于商业目的，包括但不限于其云服务业务运营。\n' +
        '\n' +
        '除上述特定条件外，其他所有权利和限制均遵循 Apache License 2.0 协议。有关 Apache License 2.0 的详细信息，请访问 http://www.apache.org/licenses/LICENSE-2.0。\n' +
        '\n' +
        '本产品的交互设计受外观专利保护。\n' +
        '\n' +
        '© 2025 江苏千桐科技有限公司'
  },
  button: {
    add: '新增',
    cancel: '取消',
    clear: '清除',
    close: '关闭',
    confirm: '确定',
    copy: '复制',
    delete: '删除',
    details: '详情',
    download: '下载',
    expand: '展开',
    export: '导出',
    fold: '折叠',
    import: '导入',
    linkParentChild: '父子联动',
    loading: '加载中，请稍候',
    more: '更多',
    nextStep: '下一步',
    previousStep: '上一步',
    query: '查询',
    refresh: '刷新',
    reset: '重置',
    return: '返回',
    returnHome: '返回首页',
    save: '保存',
    selectAll: '全选',
    unselectAll: '全不选',
    un_fold: '展开/折叠',
    update: '修改',
    upload: '上传',
    neverShow: '不再展示',
    view: '查看',
    aware:'我已知晓'
  },
  error: {
    code401: '认证失败，无法访问系统资源',
    code403: '当前操作没有权限',
    code404: '访问资源不存在',
    default: '系统未知错误，请反馈给管理员',
    findPage: '找不到网页！',
    notAccessRights: '您没有访问权限！',
    Sorry401: '对不起，您没有访问权限，请不要进行非法操作！您可以返回主页面',
    Sorry404: '对不起，您正在寻找的页面不存在。尝试检查URL的错误，然后按浏览器上的刷新按钮或尝试在我们的应用程序中找到其他内容。',
    network: '后端接口连接异常',
    timeout: '系统接口超时！',
    setPermissionValue: '请设置操作权限标签值',
    setRoleValue: '请设置角色权限标签值'
  },
  form: {
    descriptionPlaceholder: '请输入描述',
    descriptionRequired: '描述不能为空',
    namePlaceholder: '请输入名称',
    nameRequired: '名称不能为空',
    remarkPlaceholder: '请输入备注',
    remarkRequired: '备注不能为空',
    statusPlaceholder: '请选择状态',
    statusRequired: '状态不能为空',
    startTimePlaceholder: '开始时间',
    endTimePlaceholder: '结束时间',
    singleSelect: '单选',
    multiSelect: '多选',
    startDatePlaceholder: '开始日期',
    endDatePlaceholder: '结束日期'
  },
  texts: {
    action: '操作',
    createdBy: '创建人',
    createdTime: '创建时间',
    description: '描述',
    name: '名称',
    number: '编号',
    operation: '操作',
    remark: '备注',
    status: '状态',
    time: '时间',
    updatedBy: '更新人',
    updatedTime: '更新时间',
    enable: '启用',
    disable: '禁用',
    topNode: '顶级节点',
    metadata: '元数据',
    detail: '详情',
    dataItem: '数据项',
    data: '数据',
    success: '成功',
    sortOrder: '排序',
    superAdmin: '超级管理员'
  },
  message: {
    addSuccess: '新增成功',
    deleteSuccess: '删除成功',
    downloadingData: '正在下载数据，请稍后...',
    editSuccess: '修改成功',
    loading: '加载中...',
    loadingResource: '正在加载系统资源，请耐心等待...',
    msgOpFailed: '操作失败',
    msgOpSuccess: '操作成功',
    required: '不能为空',
    confirmDelete: '是否确认删除编号为',
    confirm: '是否确认',
    question: '吗？',
    success: '成功',
    noData: '暂无数据',
    noMatchingData: '无匹配数据',
    noRecord: '暂无记录',
    operationSuccess: '操作成功',
    prompt: '提示',
    systemPrompt: '系统提示',
    selectRecord: '请选择要删除的数据'
  },
  html: {
    appTitle: 'qData开源数据中台'
  },
  // 展示
  display: {
    index: '序号',
    status: '状态',
    createTime: '创建时间',
    updateTime: '更新时间',
    startTime: '开始时间',
    endTime: '结束时间'
  },
  qualityTrends: '质量趋势',
  shortcut: {
    title: '快捷键',
    save: '保存',
    check: '校验',
    format: '格式化',
    formatSelection: '格式化选中内容',
    notes: '注释/取消注释该行(区域)',
    upperCase: '转为大写',
    lowerCase: '转为小写',
    query: '查询'
  },
  graph: {
    inputComponent: '输入组件',
    outputComponent: '输出组件',
    transformComponent: '转换组件',
    zoomOut: '缩小',
    zoomIn: '放大',
    resetView: '重置视角'
  },
  time: {
    justNow: '刚刚',
    minutesAgo: '分钟前',
    hoursAgo: '小时前',
    oneDayAgo: '1天前',
    month: '月',
    day: '日',
    hour: '时',
    minute: '分',
    daysAgo: '天前',
    monthsAgo: '月前',
    yearsAgo: '年前',
    second: '秒',
    millisecond: '毫秒'
  },
  noData: '暂无数据',
  noMessage: '暂无消息',
  noHistory: '暂无历史记录',
  noPermission: '暂无权限',
  upload: {
    dragOrClick: '将文件拖到此处，或点击上传',
    updateExistingData: '是否更新已经存在的数据',
    fileFormat: '仅允许导入xls、xlsx格式文件。',
    downloadTemplate: '下载模板',
    selectFile: '选择文件',
    uploadFile: '上传文件',
    uploadAttachment: '上传附件',
    fileSizeLimit: '大小不超过',
    fileFormatLabel: '格式为',
    supportedFormat: '仅支持上传',
    fileSizeMB: 'MB',
    pleaseUpload: '请上传',
    fileTypes: '的文件',
    fileName: '文件名',
    fileSize: '文件大小',
    filePath: '文件路径',
    fileType: '文件类型',
    search: '搜索',
    uploadFailedAdmin: '上传文件失败，请联系管理员',
    uploadResult: '上传结果：',
    selectOneFileOnly: '数量限制，只能选择一个文件',
    noFileSelected: '未选择文件，请选择文件后重试'
  },
  noSearchResult: '暂无搜索结果',
  noDescription: '暂无描述',
  noLog: '暂无日志',
  noOpinion: '暂无意见',
  noAssetLineage: '暂无资产血缘',
  noAssetQualityTask: '暂无资产质量任务',
  noTaskProcess: '暂无任务流程',
  request: {
    expiredSession: '无效的会话，或者会话已过期，请重新登录。',
    networkError: '后端接口连接异常',
    timeout: '系统接口请求超时',
    interfaceError: '系统接口{code}异常',
    repeatSubmit: '数据正在处理，请勿重复提交',
    routeChangeCancel: 'Route change: Request canceled',
    downloadError: '下载文件出现错误，请联系管理员！',
    downloading: '正在下载数据，请稍候',
    loginExpired: '登录状态已过期，您可以继续留在该页面，或者重新登录',
    reLogin: '重新登录'
  },
  crontab: {
    tab: {
      second: '秒',
      minute: '分钟',
      hour: '小时',
      day: '日',
      month: '月',
      week: '周',
      year: '年'
    },
    timeExpression: '时间表达式',
    cronExpression: 'Cron 表达式',
    multiSelect: '可多选',
    specify: '指定',
    notSpecify: '不指定',
    cycleFrom: '周期从',
    noSecondConfigurable: '不可配置秒级别调度周期!',
    second: {
      wildcard: '秒，允许的通配符[, - * /]',
      cycleSuffix: '秒',
      averagePrefix: '从',
      averageMiddle: '秒开始，每',
      averageSuffix: '秒执行一次'
    },
    minute: {
      wildcard: '分钟，允许的通配符[, - * /]',
      cycleSuffix: '分钟',
      averagePrefix: '从',
      averageMiddle: '分钟开始，每',
      averageSuffix: '分钟执行一次'
    },
    hour: {
      wildcard: '小时，允许的通配符[, - * /]',
      cycleSuffix: '时',
      averagePrefix: '从',
      averageMiddle: '时开始，每',
      averageSuffix: '小时执行一次'
    },
    day: {
      wildcard: '日，允许的通配符[, - * ? / L W]',
      cycleSuffix: '日',
      averagePrefix: '从',
      averageMiddle: '号开始，每',
      averageSuffix: '日执行一次',
      nearestWorkday: '每月',
      nearestWorkdaySuffix: '号最近的那个工作日',
      lastDay: '本月最后一天'
    },
    month: {
      wildcard: '月，允许的通配符[, - * /]',
      cycleSuffix: '月',
      averagePrefix: '从',
      averageMiddle: '月开始，每',
      averageSuffix: '月执行一次',
      names: {
        jan: '一月',
        feb: '二月',
        mar: '三月',
        apr: '四月',
        may: '五月',
        jun: '六月',
        jul: '七月',
        aug: '八月',
        sep: '九月',
        oct: '十月',
        nov: '十一月',
        dec: '十二月'
      }
    },
    week: {
      wildcard: '周，允许的通配符[, - * ? / L #]',
      nthWeekPrefix: '第',
      nthWeekSuffix: '周的',
      lastPrefix: '本月最后一个',
      names: {
        sun: '星期日',
        mon: '星期一',
        tue: '星期二',
        wed: '星期三',
        thu: '星期四',
        fri: '星期五',
        sat: '星期六'
      }
    },
    year: {
      wildcard: '不填，允许的通配符[, - * /]',
      every: '每年',
      averagePrefix: '从',
      averageMiddle: '年开始，每',
      averageSuffix: '年执行一次'
    },
      time: {
    minutesAgo: '分钟前',
    hoursAgo: '小时前',
    daysAgo: '天前',
    monthsAgo: '月前',
    yearsAgo: '年前',
    second: '秒',
    millisecond: '毫秒'
    },
    result: {
      title: '最近5次运行时间',
      calculating: '计算结果中...',
      noResult: '没有达到条件的结果！',
      limit: '最近100年内只有上面{count}条结果！'
    },
    cronUtils: {
      execute: '执行',
      invalidExpression: '无效的 Cron 表达式'
    }
  }
}
