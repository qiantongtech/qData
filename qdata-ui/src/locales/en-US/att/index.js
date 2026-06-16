export default {
  common: {
    dataAssetCatName: 'Data Asset Category Name',
    dataAssetCatNamePlaceholder: 'Please enter data asset category name',
    dataDocCatName: 'Data Document Name',
    cleanCatName: 'Clean Rule Category Name',
    cleanCatNamePlaceholder: 'Please enter clean rule category name',
    dataElemCatName: 'Data Element Category Name',
    dataElemCatNamePlaceholder: 'Please enter data element category name',
    documentCatName: 'Standard Category Name',
    modelCatName: 'Logical Model Category Name',
    modelCatNamePlaceholder: 'Please enter logical model category name',
    qualityCatName: 'Data Quality Category Name',
    qualityCatNamePlaceholder: 'Please enter data quality category name',
    tagCatName: 'Tag Management Category Name',
    tagCatNamePlaceholder: 'Please enter tag management category name',
    parentCat: 'Parent Category',
    parentCatRequired: 'Parent category is required',
    categoryName: 'Category Name',
    sortOrder: 'Sort Order',
    upperCat: 'Upper Category',
    ruleName: 'Rule Name',
    ruleNameRequired: 'Rule name is required',
    ruleType: 'Rule Type',
    ruleTypeRequired: 'Rule type is required',
    ruleLevel: 'Rule Level',
    ruleLevelRequired: 'Rule level is required',
    useCase: 'Use Case',
    example: 'Example',
    projectName: 'Project Name',
    projectNamePlaceholder: 'Please enter project name',
    projectNameRequired: 'Project name is required',
    manager: 'Manager',
    managerRequired: 'Manager is required',
    contactWay: 'Contact',
    systemName: 'System Name',
    systemType: 'System Type',
    responsiblePerson: 'Responsible Person',
    contactPerson: 'Contact Person',
    tagName: 'Tag Name',
    tagNameRequired: 'Tag name is required',
    nearSynonyms: 'Near Synonyms',
    synonyms: 'Synonyms',
    themeName: 'Theme Name',
    themeNameRequired: 'Theme name is required',
    icon: 'Icon',
    creator: 'Creator',
    createTime: 'Created Time',
    status: 'Status',
    remark: 'Remark',
    description: 'Description',
    code: 'Code',
    codeRequired: 'Code is required',
    operation: 'Operation',
    query: 'Search',
    reset: 'Reset',
    add: 'Add',
    update: 'Edit',
    delete: 'Delete',
    detail: 'Detail',
    addSuccess: 'Added successfully',
    updateSuccess: 'Updated successfully',
    deleteSuccess: 'Deleted successfully',
    noData: 'No data',
    pleaseEnter: 'Please enter',
    pleaseSelect: 'Please select',
    confirmDelete: 'Confirm Delete',
    deleteTip: 'Are you sure to delete the selected data?',
    disable: 'Disable',
    enable: 'Enable',
    dataDoc: 'Data Document',
    importResult: 'Import Result',
    pleaseEnterName: 'Please enter name',
    pleaseSelectParent: 'Please select parent',
    pleaseSelectManager: 'Please select manager',
    catCode: 'Category Code',
    assetCount: 'Asset Count',
    assetInfo: 'Asset Info',
    detailInfo: 'Detail Info',
    extendAlias: 'Extended Info Alias',
    nearSynonymsTip: 'Separate multiple items with commas',
    synonymsTip: 'Separate multiple items with commas',
    codePlaceholder: 'Please enter code',
    ruleTypePlaceholder: 'Please select rule type',
    useCasePlaceholder: 'Please enter use case',
    examplePlaceholder: 'Please enter example',
    qualityDimPlaceholder: 'Please select quality dimension',
    namePlaceholder: 'Please enter name',
    sortOrderPlaceholder: 'Please enter sort order',
    statusPlaceholder: 'Please enter status',
    catCodePlaceholder: 'Please enter category code',
    assetCountPlaceholder: 'Please enter asset count',
    extendAliasPlaceholder: 'Please enter extended info alias',
    nearSynonymsPlaceholder: 'Please enter near synonyms',
    synonymsPlaceholder: 'Please enter synonyms',
    parentCatPlaceholder: 'Please select parent',
    tagCatPlaceholder: 'Please select tag management category',
    cleanRuleCatPlaceholder: 'Please enter audit rule category',
    ruleNamePlaceholder: 'Please enter rule name',
    tagManagementCategory: 'Tag Management Category',
    assetCountTip: 'Total data assets currently associated with this tag',
    statusTip: 'Enabled status means the tag can be used for tagging data assets; once disabled, it cannot be used, but existing tags are retained.',
    createdByPlaceholder: 'Please enter creator',
    confirmStatusChange: 'Are you sure to "{status}" the tag "<name>"?',
    confirmDeleteTag: 'Are you sure to delete the tag with code "<ids>"?',
    tagCatRequired: 'Tag management category is required',
    addTag: 'Add Tag',
    editTag: 'Edit Tag',
    tagDetail: 'Tag Detail',
    ruleCategoryPlaceholder: 'Please enter audit rule category',
    confirmDeleteCat: 'Are you sure to delete the <titleBase> with code "<id>"?',
    deleteConfirmCount: '{canDeleteCount} can be deleted, {cannotDeleteCount} cannot be deleted. Delete the deletable ones?',
    topNode: 'Top Node',
    addTitle: 'Add {title}',
    editTitle: 'Edit {title}',
    detailTitle: '{title} Detail',
    importTitle: '{title} Import',
    confirmStatusChangeGeneric: 'Are you sure to "<status>" "<name>" <type>?',
    statusSuccess: '{status} successful',
    contactWayPlaceholder: 'Please enter contact information'
  },
  assetCat: {
    table: {
      name: 'Data Asset Category Name',
      sortOrder: 'Sort Order'
    },
    validations: {
      nameRequired: 'Data asset category name is required',
      parentIdRequired: 'Parent category is required'
    },
    messages: {
      confirmDelete: 'Are you sure to delete the data asset category with code "<name>"?'
    },
    title: {
      add: 'Add Data Asset Category',
      edit: 'Edit Data Asset Category'
    }
  },
  attDocCat: {
    table: {
      name: 'Document Category Name',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Data Document Category',
      edit: 'Edit Data Document Category'
    },
    messages: {
      confirmDelete: 'Are you sure to delete the data document with code "<name>"?'
    }
  },
  auditRule: {
    table: {
      code: 'Code',
      name: 'Rule Name',
      type: 'Rule Type',
      qualityDim: 'Quality Dimension',
      useCase: 'Use Case',
      example: 'Example'
    },
    texts: {
      name: 'Rule Name',
      qualityDim: 'Quality Dimension',
      scenario: 'Use Case',
      example: 'Example'
    },
    form:{
      codePlaceholder: 'Please enter code',
      descriptionPlaceholder: 'Please enter description'
    },
    qualityDimRequired: 'Quality dimension is required',
    title: {
      add: 'Add Audit Rule',
      edit: 'Edit Audit Rule',
      detail: 'Audit Rule Detail'
    },
    importTitle: 'Audit Rule Import',
    deleteConfirm: 'Are you sure to delete the audit rule with code "<ids>"?',
    treeRootName: 'Quality Dimension'
  },
  cleanCat: {
    table: {
      name: 'Clean Category Name',
      sortOrder: 'Sort Order'
    },
    texts: {
      name: 'Category Name',
      parentId: 'Parent ID',
      sortOrder: 'Category Sort',
      hierarchyCode: 'Hierarchy Code'
    },
    title: {
      add: 'Add Clean Rule Category',
      edit: 'Edit Clean Rule Category',
      detail: 'Clean Rule Category Detail'
    },
    importTitle: 'Clean Rule Category Import',
    messages: {
      confirmDelete: 'Are you sure to delete the clean rule category with code "<ids>"?'
    }
  },
  cleanRule: {
    table: {
      code: 'Code',
      name: 'Rule Name',
      type: 'Rule Type',
      useCase: 'Use Case',
      example: 'Example'
    },
    texts: {
      name: 'Rule Name',
      type: 'Rule Type',
      level: 'Rule Level'
    },
    title: {
      add: 'Add Clean Rule',
      edit: 'Edit Clean Rule',
      detail: 'Clean Rule Detail'
    },
    importTitle: 'Clean Rule Import',
    deleteConfirm: 'Are you sure to delete the item with code "<ids>"?',
    cleanRuleCategory: 'Clean Rule Category'
  },
  dataElemCat: {
    table: {
      name: 'Data Element Category Name',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Data Element Category',
      edit: 'Edit Data Element Category'
    },
    messages: {
      confirmDelete: 'Are you sure to delete the data element category with code "<name>"?'
    }
  },
  documentCat: {
    table: {
      name: 'Standard Info Category Name',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Standard Info Category',
      edit: 'Edit Standard Info Category'
    },
    messages: {
      confirmDelete: 'Are you sure to delete the standard info category with code "<ids>"?'
    }
  },
  modelCat: {
    table: {
      name: 'Model Category Name',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Logical Model Category',
      edit: 'Edit Logical Model Category'
    },
    messages: {
      confirmDelete: 'Are you sure to delete the logical model category with code "<name>"?'
    }
  },
  project: {
    table: {
      name: 'Project Name',
      manager: 'Project Manager'
    },
    texts: {
      name: 'Project Name',
      description: 'Project Description',
      manager: 'Manager',
      contact: 'Contact'
    },
    title: {
      add: 'Add Project',
      edit: 'Edit Project',
      detail: 'Project Detail'
    },
    importTitle: 'Project Import',
    message: {
      confirmDelete: 'Are you sure to delete the project with code "<ids>"?'
    }
  },
  qualityCat: {
    table: {
      name: 'Quality Category Name',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Data Quality Category',
      edit: 'Edit Data Quality Category'
    },
    messages: {
      confirmDelete: 'Are you sure to delete the data quality category with code "<name>"?'
    }
  },
  tag: {
    table: {
      name: 'Tag Name',
      catName: 'Category Name',
      nearSynonyms: 'Near Synonym Tags',
      synonyms: 'Synonym Tags'
    },
    detail: {
      catName: 'Category Name',
      nearSynonyms: 'Near Synonym Tags',
      synonyms: 'Synonym Tags'
    },
    title: {
      add: 'Add Tag Management',
      edit: 'Edit Tag Management',
      detail: 'Tag Management Detail'
    },
    message: {
      deleteConfirm: 'Are you sure to delete the item with "<ids>"?',
      batchDeleteConfirm: 'Are you sure to batch delete selected items?'
    }
  },
  tagCat: {
    table: {
      name: 'Tag Management Category Name'
    }
  },
  theme: {
    table: {
      name: 'Theme Name',
      icon: 'Icon',
      sortOrder: 'Sort Order'
    },
    texts: {
      name: 'Theme Name',
      icon: 'Icon',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Theme',
      edit: 'Edit Theme',
      detail: 'Theme Detail'
    },
    importTitle: 'Theme Import',
    deleteConfirm: 'Are you sure to delete the theme with code "<ids>"?',
    themeWord: 'Theme'
  },
  sourceSystem: {
    table: {
      name: 'System Name',
      type: 'System Type',
      responsiblePerson: 'Responsible Person',
      contactPerson: 'Contact Person',
      sortOrder: 'Sort Order'
    },
    form: {
      name: 'System Name',
      type: 'System Type',
      responsiblePerson: 'Responsible Person',
      contactPerson: 'Contact Person',
      sortOrder: 'Sort Order',
      namePlaceholder: 'Please enter system name',
      typePlaceholder: 'Please select system type',
      responsiblePersonPlaceholder: 'Please select responsible person',
      contactPersonPlaceholder: 'Please select contact person',
      enable: 'Enable',
      disable: 'Disable'
    },
    title: {
      add: 'Add Source System',
      edit: 'Edit Source System',
      detail: 'Source System Detail'
    },
    message: {
      nameRequired: 'System name is required',
      typeRequired: 'System type is required',
      confirmStatus: 'Are you sure to "<status>" "<name>" source system?',
      statusSuccess: '<status>" successful',
      deleteConfirm: '{deletable} can be deleted, {undeletable} cannot be deleted. Delete the deletable ones?'
    },
    textareaLimit: '500 characters'
  }
}
