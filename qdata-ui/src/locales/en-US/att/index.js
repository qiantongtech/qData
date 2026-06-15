import common from './common';
export default {
  common,
  assetCat: {
    table: {
      name: 'Data Asset Category Name',
      sortOrder: 'Sort Order'
    },
    validations: {
      nameRequired: 'Data asset category name cannot be empty',
      parentIdRequired: 'Parent category cannot be empty'
    },
    messages: {
      confirmDelete: 'Confirm to delete the data item with data asset category management number "<name>"?'
    },
    title: {
      add: 'Add Data Asset Category',
      edit: 'Edit Data Asset Category'
    }
  },
  attDocCat: {
    table: {
      name: 'Data Document Name',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Data Document Category',
      edit: 'Edit Data Document Category'
    },
    messages: {
      confirmDelete: 'Confirm to delete the data document category "<name>"?'
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
    qualityDimRequired: 'Quality dimension cannot be empty',
    title: {
      add: 'Add Audit Rule',
      edit: 'Edit Audit Rule',
      detail: 'Audit Rule Detail'
    },
    importTitle: 'Import Audit Rule',
    deleteConfirm: 'Confirm to delete the data item with audit rule ID "<ids>"?',
    treeRootName: 'Quality Dimension'
  },
  cleanCat: {
    table: {
      name: 'Cleaning Rule Category Name',
      sortOrder: 'Sort Order'
    },
    texts: {
      name: 'Category Name',
      parentId: 'Parent ID',
      sortOrder: 'Category Sort',
      hierarchyCode: 'Hierarchy Code'
    },
    title: {
      add: 'Add Cleaning Rule Category',
      edit: 'Edit Cleaning Rule Category',
      detail: 'Cleaning Rule Category Detail'
    },
    importTitle: 'Import Cleaning Rule Category',
    messages: {
      confirmDelete: 'Confirm to delete the cleaning rule category with ID "<ids>"?'
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
      add: 'Add Cleaning Rule',
      edit: 'Edit Cleaning Rule',
      detail: 'Cleaning Rule Detail'
    },
    importTitle: 'Import Cleaning Rule',
    deleteConfirm: 'Confirm to delete the data item with ID "<ids>"?',
    cleanRuleCategory: 'Cleaning Rule Category'
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
      confirmDelete: 'Confirm to delete the data element category "<name>"?'
    }
  },
  documentCat: {
    table: {
      name: 'Standard Category Name',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Standard Information Category',
      edit: 'Edit Standard Information Category'
    },
    messages: {
      confirmDelete: 'Confirm to delete the standard information category with ID "<ids>"?'
    }
  },
  modelCat: {
    table: {
      name: 'Logical Model Category Name',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Logical Model Category',
      edit: 'Edit Logical Model Category'
    },
    messages: {
      confirmDelete: 'Confirm to delete the logical model category "<name>"?'
    }
  },
  project: {
    table: {
      name: 'Project Name',
      manager: 'Manager'
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
    importTitle: 'Import Project',
    message: {
      confirmDelete: 'Confirm to delete the data item with project ID "<ids>"?'
    }
  },
  qualityCat: {
    table: {
      name: 'Data Quality Category Name',
      sortOrder: 'Sort Order'
    },
    title: {
      add: 'Add Data Quality Category',
      edit: 'Edit Data Quality Category'
    },
    messages: {
      confirmDelete: 'Confirm to delete the data quality category "<name>"?'
    }
  },
  tag: {
    table: {
      name: 'Tag Name',
      catName: 'Tag Management Category',
      nearSynonyms: 'Near Synonyms',
      synonyms: 'Synonyms'
    },
    detail: {
      catName: 'Tag Management Category',
      nearSynonyms: 'Near Synonyms',
      synonyms: 'Synonyms'
    },
    title: {
      add: 'Add Tag',
      edit: 'Edit Tag',
      detail: 'Tag Detail'
    },
    message: {
      deleteConfirm: 'Are you sure to delete the data item with "<ids>"?',
      batchDeleteConfirm: 'Are you sure to batch delete the selected data items?'
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
    importTitle: 'Import Theme',
    deleteConfirm: 'Confirm to delete the data item with theme ID "<ids>"?',
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
      nameRequired: 'System name cannot be empty',
      typeRequired: 'System type cannot be empty',
      confirmStatus: 'Confirm to "<status>< "<name>" source system?',
      statusSuccess: '<status>" Success',
      deleteConfirm: '<deletable> can be deleted, "<undeletable> cannot be deleted. Delete the deletable items?'
    },
    textareaLimit: '500 characters'
  }
}