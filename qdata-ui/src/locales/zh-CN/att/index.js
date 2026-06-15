import common from './common';
export default {
  common,
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
    title: {
      add: '新增项目',
      edit: '修改项目',
      detail: '项目详情'
    },
    importTitle: '项目导入',
    message: {
      confirmDelete: '是否确认删除项目编号为"<ids>"的数据项？'
    }
  },
  qualityCat: {
    table: {
      name: '质量类目名称',
      sortOrder: '排序'
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