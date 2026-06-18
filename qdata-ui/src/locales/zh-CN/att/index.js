export default {
  common: {
    edit: '修改',
    nameRequired: '{name}不能为空',
    confirmDeleteItem: '是否确认删除编号为"<ids>"的数据项？',
    dataAssetCatName: '数据资产类目名称',
    dataAssetCatNamePlaceholder: '请输入数据资产类目名称',
    dataDocCatName: '数据文档名称',
    cleanCatName: '清洗规则类目名称',
    cleanCatNamePlaceholder: '请输入清洗规则类目名称',
    dataElemCatName: '数据元类目名称',
    dataElemCatNamePlaceholder: '请输入数据元类目名称',
    documentCatName: '标准类目名称',
    modelCatName: '逻辑模型类目名称',
    modelCatNamePlaceholder: '请输入逻辑模型类目名称',
    qualityCatName: '数据质量类目名称',
    qualityCatNamePlaceholder: '请输入数据质量类目名称',
    tagCatName: '标签管理类目名称',
    tagCatNamePlaceholder: '请输入标签管理类目名称',
    parentCat: '上级类目',
    parentCatRequired: '上级类目不能为空',
    categoryName: '类目名称',
    sortOrder: '排序',
    upperCat: '上级类目',
    ruleName: '规则名称',
    ruleNameRequired: '规则名称不能为空',
    ruleType: '规则类型',
    ruleTypeRequired: '规则类型不能为空',
    ruleLevel: '规则级别',
    ruleLevelRequired: '规则级别不能为空',
    useCase: '使用场景',
    example: '示例',
    projectName: '项目名称',
    projectNamePlaceholder: '请输入项目名称',
    projectNameRequired: '项目名称不能为空',
    manager: '负责人',
    managerRequired: '负责人不能为空',
    contactWay: '联系方式',
    systemName: '系统名称',
    systemType: '系统类型',
    responsiblePerson: '负责人',
    contactPerson: '对接人',
    tagName: '标签名称',
    tagNameRequired: '标签名称不能为空',
    nearSynonyms: '近义词',
    synonyms: '同义词',
    themeName: '主题名称',
    themeNameRequired: '主题名称不能为空',
    icon: '图标',
    creator: '创建人',
    createTime: '创建时间',
    status: '状态',
    remark: '备注',
    description: '描述',
    code: '编号',
    codeRequired: '编号不能为空',
    operation: '操作',
    query: '查询',
    reset: '重置',
    add: '新增',
    update: '修改',
    delete: '删除',
    detail: '详情',
    addSuccess: '新增成功',
    updateSuccess: '修改成功',
    deleteSuccess: '删除成功',
    noData: '暂无数据',
    pleaseEnter: '请输入',
    pleaseSelect: '请选择',
    confirmDelete: '确认删除',
    deleteTip: '确定要删除选中的数据吗？',
    disable: '禁用',
    enable: '启用',
    dataDoc: '数据文档',
    importResult: '导入结果',
    pleaseEnterName: '请输入名称',
    pleaseSelectParent: '请选择上级',
    pleaseSelectManager: '请选择负责人',
    catCode: '类目编码',
    assetCount: '资产数量',
    assetInfo: '资产信息',
    detailInfo: '详细信息',
    extendAlias: '扩展信息别名',
    nearSynonymsTip: '如果有多个请用英文逗号分隔',
    synonymsTip: '如果有多个请用英文逗号分隔',
    codePlaceholder: '请输入编号',
    ruleTypePlaceholder: '请选择规则类型',
    useCasePlaceholder: '请输入使用场景',
    examplePlaceholder: '请输入示例',
    qualityDimPlaceholder: '请选择质量维度',
    namePlaceholder: '请输入名称',
    sortOrderPlaceholder: '请输入排序',
    statusPlaceholder: '请输入状态',
    catCodePlaceholder: '请输入类目编码',
    assetCountPlaceholder: '请输入资产数量',
    extendAliasPlaceholder: '请输入扩展信息别名',
    nearSynonymsPlaceholder: '请输入近义词',
    synonymsPlaceholder: '请输入同义词',
    parentCatPlaceholder: '请选择上级',
    tagCatPlaceholder: '请选择标签管理类目',
    cleanRuleCatPlaceholder: '请输入稽查规则类目',
    ruleNamePlaceholder: '请输入规则名称',
    tagManagementCategory: '标签管理类目',
    assetCountTip: '当前已关联此标签的数据资产总数',
    statusTip: '启用状态表示该标签可用于打标数据资产；禁用后无法再被使用，但已有标签仍保留。',
    createdByPlaceholder: '请输入创建人',
    confirmStatusChange: '确认要"{status}""<name>"标签吗？',
    confirmDeleteTag: '是否确认删除标签编号为"<ids>"的数据项？',
    tagCatRequired: '标签管理类目不能为空',
    addTag: '新增标签',
    editTag: '修改标签',
    tagDetail: '标签详情',
    ruleCategoryPlaceholder: '请输入稽查规则类目',
    confirmDeleteCat: '是否确认删除<titleBase>编号为"<id>"的数据项？',
    deleteConfirmCount: '可删除<canDeleteCount>个，不可删除<cannotDeleteCount>个，是否删除可删部分',
    addTitle: '新增{title}',
    editTitle: '修改{title}',
    detailTitle: '{title}详情',
    importTitle: '{title}导入',
    confirmStatusChangeGeneric: '确认要"<status>","<name>"<type>吗？',
    statusSuccess: '{status}成功',
    contactWayPlaceholder: '请输入联系方式'
  },
  assetCat: {
    table: {
      name: '数据资产类目名称',
      sortOrder: '排序'
    },
    validations: {
      nameRequired: '数据资产类目名称不能为空',
      parentIdRequired: '上级类目不能为空'
    },
    messages: {
      confirmDelete: '是否确认删除数据资产类目管理编号为"<name>"的数据项？'
    },
    title: {
      add: '新增数据资产类目',
      edit: '修改数据资产类目'
    }
  },
  attDocCat: {
    table: {
      name: '文档类目名称',
      sortOrder: '排序'
    },
    validations: {
      nameRequired: '文档类目名称不能为空'
    },
    title: {
      add: '新增数据文档类目',
      edit: '修改数据文档类目'
    },
    messages: {
      confirmDelete: '是否确认删除数据文档管理编号为"<name>"的数据项？'
    }
  },
  auditRule: {
    table: {
      code: '编号',
      name: '规则名称',
      type: '规则类型',
      qualityDim: '质量维度',
      useCase: '使用场景',
      example: '示例'
    },
    texts: {
      name: '规则名称',
      qualityDim: '质量维度',
      scenario: '使用场景',
      example: '示例'
    },
    form:{
      codePlaceholder: '请输入编号',
      descriptionPlaceholder: '请输入描述'
    },
    qualityDimRequired: '质量维度不能为空',
    title: {
      add: '新增稽查规则',
      edit: '修改稽查规则',
      detail: '稽查规则详情'
    },
    importTitle: '稽查规则导入',
    deleteConfirm: '是否确认删除稽查规则编号为"<ids>"的数据项？',
    treeRootName: '质量维度'
  },
  cleanCat: {
    table: {
      name: '清洗类目名称',
      sortOrder: '排序'
    },
    texts: {
      name: '类别名称',
      parentId: '关联上级ID',
      sortOrder: '类别排序',
      hierarchyCode: '层级编码'
    },
    validations: {
      nameRequired: '清洗规则类目名称不能为空',
      parentIdRequired: '上级类目不能为空'
    },
    title: {
      add: '添加清洗规则类目',
      edit: '修改清洗规则类目',
      detail: '清洗规则类目详情'
    },
    importTitle: '清洗规则类目导入',
    messages: {
      confirmDelete: '是否确认删除清洗规则类目编号为"<ids>"的数据项？'
    }
  },
  cleanRule: {
    table: {
      code: '编号',
      name: '规则名称',
      type: '规则类型',
      useCase: '使用场景',
      example: '示例'
    },
    texts: {
      name: '规则名称',
      type: '规则类型',
      level: '规则级别'
    },
    title: {
      add: '新增清洗规则',
      edit: '修改清洗规则',
      detail: '清洗规则详情'
    },
    importTitle: '清洗规则导入',
    deleteConfirm: '是否确认删除编号为"<ids>"的数据项？',
    cleanRuleCategory: '清洗规则类目'
  },
  dataElemCat: {
    table: {
      name: '数据元类目名称',
      sortOrder: '排序'
    },
    validations: {
      nameRequired: '数据元类目名称不能为空',
      parentIdRequired: '上级类目不能为空'
    },
    title: {
      add: '新增数据元类目',
      edit: '修改数据元类目'
    },
    messages: {
      confirmDelete: '是否确认删除数据元类目管理编号为"<name>"的数据项？'
    }
  },
  documentCat: {
    table: {
      name: '标准信息分类名称',
      sortOrder: '排序'
    },
    validations: {
      nameRequired: '标准信息分类名称不能为空',
      parentIdRequired: '上级类目不能为空'
    },
    title: {
      add: '新增标准信息分类管理',
      edit: '修改标准信息分类管理'
    },
    messages: {
      confirmDelete: '是否确认删除标准信息分类管理编号为"<ids>"的数据项？'
    }
  },
  modelCat: {
    table: {
      name: '模型类目名称',
      sortOrder: '排序'
    },
    validations: {
      nameRequired: '逻辑模型类目名称不能为空',
      parentIdRequired: '上级类目不能为空'
    },
    title: {
      add: '新增逻辑模型类目',
      edit: '修改逻辑模型类目'
    },
    messages: {
      confirmDelete: '是否确认删除逻辑模型类目管理编号为"<name>"的数据项？'
    }
  },
  project: {
    table: {
      name: '项目名称',
      manager: '项目负责人'
    },
    texts: {
      name: '项目名称',
      description: '项目描述',
      manager: '负责人',
      contact: '联系方式'
    },
    validations: {
      nameRequired: '项目名称不能为空',
      managerRequired: '负责人不能为空'
    },
    title: {
      add: '新增项目',
      edit: '修改项目',
      detail: '项目详情'
    },
    importTitle: '项目导入',
    message: {
      confirmDelete: '是否确认删除项目编号为"<ids>"的数据项？'
    },
    importResult: '导入结果'
  },
  qualityCat: {
    table: {
      name: '质量类目名称',
      sortOrder: '排序'
    },
    validations: {
      nameRequired: '数据质量类目名称不能为空'
    },
    title: {
      add: '新增数据质量类目',
      edit: '修改数据质量类目'
    },
    messages: {
      confirmDelete: '是否确认删除数据质量类目管理编号为"<name>"的数据项？'
    }
  },
  tag: {
    table: {
      name: '标签名称',
      catName: '类目名称',
      nearSynonyms: '近义标签',
      synonyms: '同义标签'
    },
    detail: {
      catName: '类目名称',
      nearSynonyms: '近义标签',
      synonyms: '同义标签'
    },
    title: {
      add: '添加标签管理',
      edit: '修改标签管理',
      detail: '标签管理详情'
    },
    message: {
      deleteConfirm: '是否确认删除为"<ids>"的数据项？',
      batchDeleteConfirm: '是否确认批量删除选中的数据项？'
    }
  },
  tagCat: {
    table: {
      name: '标签管理类目名称'
    }
  },
  theme: {
    table: {
      name: '主题名称',
      icon: '图标',
      sortOrder: '排序'
    },
    texts: {
      name: '主题名称',
      icon: '图标',
      sortOrder: '排序'
    },
    title: {
      add: '新增主题',
      edit: '修改主题',
      detail: '主题详情'
    },
    importTitle: '主题导入',
    deleteConfirm: '是否确认删除主题编号为"<ids>"的数据项？',
    themeWord: '主题'
  },
  sourceSystem: {
    table: {
      name: '系统名称',
      type: '系统类型',
      responsiblePerson: '负责人',
      contactPerson: '对接人',
      sortOrder: '排序'
    },
    form: {
      name: '系统名称',
      type: '系统类型',
      responsiblePerson: '负责人',
      contactPerson: '对接人',
      sortOrder: '排序',
      namePlaceholder: '请输入系统名称',
      typePlaceholder: '请选择系统类型',
      responsiblePersonPlaceholder: '请选择负责人',
      contactPersonPlaceholder: '请选择对接人',
      enable: '启用',
      disable: '禁用'
    },
    title: {
      add: '新增来源系统',
      edit: '修改来源系统',
      detail: '来源系统详情'
    },
    message: {
      nameRequired: '系统名称不能为空',
      typeRequired: '系统类型不能为空',
      confirmStatus: '确认要"<status><<name>"来源系统吗？',
      statusSuccess: '<status>"成功',
      deleteConfirm: '可删除"<deletable>"个，不可删除"<undeletable>"个，是否删除可删部分'
    },
    textareaLimit: '500个字符'
  }
}