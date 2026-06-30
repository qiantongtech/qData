// Common translations (buttons, messages, forms, display labels)
export default {
  license: {
    title: 'Open Source License',
    text: {
      intro: [
        'qData is made available under the Apache License 2.0, subject to the following additional conditions specific to qData:',
        'For the purpose of this License, "qData" refers to the entire software project, including but not limited to its frontend, backend, API, and related documentation in whole or in part.',
        'For the purpose of this License, "Producer" refers to Jiangsu Qiantong Technology Co., Ltd.'
      ],
      terms: [
        {
          title: 'You may use qData for commercial purposes, provided that you do not remove, hide, or modify the qData logo, copyright notices, license notices, or attribution information displayed in the qData.',
          desc: [
            'If you wish to remove, hide, obscure, replace, or modify such logo, copyright notices, license notices, or attribution information, including but not limited to white-label use, OEM distribution, rebranding, or presenting qData as another product, you must obtain a separate commercial license from the Producer.'
          ]
        },
        {
          title: 'As a contributor, you should agree that:',
          desc: [
            'a. The Producer can adjust the open-source agreement to be more strict or relaxed as deemed necessary.',
            'b. Your contributed code may be used for commercial purposes, including but not limited to its cloud business operations.'
          ]
        }
      ],
      notice: {
        before: 'Apart from the specific conditions mentioned above, all other rights and restrictions follow the Apache License 2.0. Detailed information about the Apache License 2.0 can be found at ',
        link: 'http://www.apache.org/licenses/LICENSE-2.0',
        linkText: 'http://www.apache.org/licenses/LICENSE-2.0',
        after: '.'
      },
      footer: [
        'The interactive design of this product is protected by appearance patent.',
        '© 2025 Jiangsu Qiantong Technology Co., Ltd'
      ]
    },
  },
  button: {
    add: 'Add',
    cancel: 'Cancel',
    clear: 'Clear',
    close: 'Close',
    confirm: 'Confirm',
    copy: 'Copy',
    delete: 'Delete',
    details: 'Details',
    download: 'Download',
    expand: 'Expand',
    export: 'Export',
    fold: 'Collapse',
    import: 'Import',
    linkParentChild: 'Parent-Child Link',
    loading: 'Loading, please wait',
    more: 'More',
    nextStep: 'Next',
    previousStep: 'Previous',
    query: 'Search',
    refresh: 'Refresh',
    reset: 'Reset',
    return: 'Back',
    returnHome: 'Back to Home',
    save: 'Save',
    selectAll: 'Select All',
    unselectAll: 'Deselect All',
    un_fold: 'Expand/Collapse',
    update: 'Edit',
    upload: 'Upload',
    neverShow: 'Don\'t Show Again',
    view: 'View',
    aware:'I am aware',
    commercialAuthorization:'Commercial Authorization'
  },
  error: {
    code401: '401 Error!',
    code403: 'No permission for this operation',
    code404: '404 Error!',
    default: 'System unknown error, please contact administrator',
    findPage: 'Page Not Found!',
    notAccessRights: 'You do not have access rights!',
    Sorry401: 'Sorry, you do not have access rights. Please do not perform unauthorized operations! You can return to the main page.',
    Sorry404: 'Sorry, the page you are looking for does not exist. Try checking the URL for errors, then press the refresh button on your browser or try finding other content in our application.',
    network: 'Backend interface connection error',
    timeout: 'System interface timeout!'
  },
  form: {
    descriptionPlaceholder: 'Please enter description',
    descriptionRequired: 'Description cannot be empty',
    namePlaceholder: 'Please enter name',
    nameRequired: 'Name cannot be empty',
    remarkPlaceholder: 'Please enter remark',
    remarkRequired: 'Remark cannot be empty',
    statusPlaceholder: 'Please select status',
    statusRequired: 'Status cannot be empty',
    startTimePlaceholder: 'Start Time',
    endTimePlaceholder: 'End Time',
    singleSelect: 'Single Select',
    multiSelect: 'Multi Select',
    startDatePlaceholder: 'Start Date',
    endDatePlaceholder: 'End Date'
  },
  texts: {
    action: 'Action',
    createdBy: 'Created By',
    createdTime: 'Created Time',
    description: 'Description',
    name: 'Name',
    number: 'No.',
    operation: 'Operation',
    remark: 'Remark',
    status: 'Status',
    time: 'Time',
    updatedBy: 'Updated By',
    updatedTime: 'Updated Time',
    enable: 'Enable',
    disable: 'Disable',
    topNode: 'Top Node',
    metadata: 'Metadata',
    detail: 'Detail',
    dataItem: 'Data Item',
    data: 'Data',
    success: 'Success',
    sortOrder: 'Sort Order',
    superAdmin: 'Super Admin'
  },
  graph: {
    inputComponent: 'Input Component',
    outputComponent: 'Output Component',
    transformComponent: 'Transform Component',
    zoomOut: 'Zoom Out',
    zoomIn: 'Zoom In',
    resetView: 'Reset View'
  },
  message: {
    addSuccess: 'Added successfully',
    deleteSuccess: 'Deleted successfully',
    downloadingData: 'Downloading data, please wait...',
    editSuccess: 'Updated successfully',
    loading: 'Loading...',
    loadingResource: 'Loading system resources, please wait...',
    msgOpFailed: 'Operation failed',
    msgOpSuccess: 'Operation successful',
    required: 'Cannot be empty',
    confirmDelete: 'Are you sure to delete the item with ID',
    confirm: 'Are you sure',
    question: '?',
    success: 'Success',
    noData: 'No data',
    noMatchingData: 'No matching data',
    noRecord: 'No records',
    operationSuccess: 'Operation successful',
    prompt: 'Prompt',
    systemPrompt: 'System Prompt',
    selectRecord: 'Please select data to delete'
  },
  html: {
    appTitle: 'qData Open Source Data Platform'
  },
  // Rules
  rules: {
    node: 'Node cannot be empty',
    title: 'Title cannot be empty',
    validFlag: 'Valid flag cannot be empty',
    delFlag: 'Delete flag cannot be empty',
    createTime: 'Create time cannot be empty',
    updateTime: 'Update time cannot be empty'
  },
  // Display
  display: {
    index: 'Index',
    status: 'Status',
    createTime: 'Created Time',
    updateTime: 'Updated Time',
    startTime: 'Start Time',
    endTime: 'End Time',
    info: 'Info',
    context: 'Please enter content',
    title: 'Title',
    createBy: 'Created By',
    content: 'Content',
    remark: 'Remark',
    importResult: 'Import Result',
    noselectData: 'No data selected',
    selectData: 'Please select data',
    pleaseSelect: 'Please select',
    componentOne: 'Component One',
    componentTwo: 'Component Two'
  },
  qualityTrends: 'Quality Trends',
  shortcut: {
    title: 'Shortcuts',
    save: 'Save',
    check: 'Validate',
    format: 'Format',
    formatSelection: 'Format Selection',
    notes: 'Comment/Uncomment',
    upperCase: 'To Uppercase',
    lowerCase: 'To Lowercase',
    query: 'Search'
  },
  noData: 'No data',
  noMessage: 'No messages',
  noHistory: 'No history',
  noPermission: 'No permission',
  upload: {
    dragOrClick: 'Drag file here, or click to upload',
    updateExistingData: 'Update existing data',
    fileFormat: 'Only xls, xlsx format files are allowed.',
    downloadTemplate: 'Download Template',
    selectFile: 'Select File',
    uploadFile: 'Upload File',
    uploadAttachment: 'Upload Attachment',
    fileSizeLimit: 'Size does not exceed',
    fileFormatLabel: 'Format is',
    supportedFormat: 'Only supports uploading',
    fileSizeMB: 'MB',
    pleaseUpload: 'Please upload',
    fileTypes: ' files',
    fileName: 'File Name',
    fileSize: 'File Size',
    filePath: 'File Path',
    fileType: 'File Type',
    search: 'Search',
    uploadFailedAdmin: 'Upload failed, please contact administrator',
    uploadResult: 'Upload Result: ',
    selectOneFileOnly: 'Only one file can be selected',
    noFileSelected: 'No file selected, please select a file and retry'
  },
  time: {
      minutesAgo: ' minutes ago',
      hoursAgo: ' hours ago',
      daysAgo: ' days ago',
      monthsAgo: ' months ago',
      yearsAgo: ' years ago',
      second: 'second',
      millisecond: 'millisecond'
    },
  noSearchResult: 'No search results',
  noDescription: 'No description',
  noLog: 'No logs',
  noOpinion: 'No opinions',
  noAssetLineage: 'No asset lineage',
  noAssetQualityTask: 'No asset quality tasks',
  noTaskProcess: 'No task process',
  request: {
    expiredSession: 'Invalid session or session has expired, please log in again.',
    networkError: 'Backend interface connection error',
    timeout: 'System interface request timeout',
    interfaceError: 'System interface {code} error',
    repeatSubmit: 'Data is being processed, please do not submit again',
    routeChangeCancel: 'Route change: Request canceled',
    downloadError: 'Error downloading file, please contact administrator!',
    downloading: 'Downloading data, please wait',
    loginExpired: 'Login status has expired, you can continue on this page or log in again',
    reLogin: 'Re-login'
  },
  crontab: {
    tab: {
      second: 'Second',
      minute: 'Minute',
      hour: 'Hour',
      day: 'Day',
      month: 'Month',
      week: 'Week',
      year: 'Year'
    },
    timeExpression: 'Time Expression',
    cronExpression: 'Cron Expression',
    multiSelect: 'Multi-select',
    specify: 'Specify',
    notSpecify: 'Not specified',
    cycleFrom: 'Cycle from',
    noSecondConfigurable: 'Second-level scheduling cycle is not configurable!',
    second: {
      wildcard: 'Second, allowed wildcards [, - * /]',
      cycleSuffix: 'second(s)',
      averagePrefix: 'Starting at',
      averageMiddle: 'second(s), every',
      averageSuffix: 'second(s)'
    },
    minute: {
      wildcard: 'Minute, allowed wildcards [, - * /]',
      cycleSuffix: 'minute(s)',
      averagePrefix: 'Starting at',
      averageMiddle: 'minute(s), every',
      averageSuffix: 'minute(s)'
    },
    hour: {
      wildcard: 'Hour, allowed wildcards [, - * /]',
      cycleSuffix: 'hour(s)',
      averagePrefix: 'Starting at',
      averageMiddle: 'hour(s), every',
      averageSuffix: 'hour(s)'
    },
    day: {
      wildcard: 'Day, allowed wildcards [, - * ? / L W]',
      cycleSuffix: 'day(s)',
      averagePrefix: 'Starting on day',
      averageMiddle: ', every',
      averageSuffix: 'day(s)',
      nearestWorkday: 'Every month on the',
      nearestWorkdaySuffix: 'th nearest workday',
      lastDay: 'Last day of the month'
    },
    month: {
      wildcard: 'Month, allowed wildcards [, - * /]',
      cycleSuffix: 'month(s)',
      averagePrefix: 'Starting from',
      averageMiddle: 'month(s), every',
      averageSuffix: 'month(s)',
      names: {
        jan: 'January',
        feb: 'February',
        mar: 'March',
        apr: 'April',
        may: 'May',
        jun: 'June',
        jul: 'July',
        aug: 'August',
        sep: 'September',
        oct: 'October',
        nov: 'November',
        dec: 'December'
      }
    },
    week: {
      wildcard: 'Week, allowed wildcards [, - * ? / L #]',
      nthWeekPrefix: 'The',
      nthWeekSuffix: 'week\'s',
      lastPrefix: 'Last',
      names: {
        sun: 'Sunday',
        mon: 'Monday',
        tue: 'Tuesday',
        wed: 'Wednesday',
        thu: 'Thursday',
        fri: 'Friday',
        sat: 'Saturday'
      }
    },
    year: {
      wildcard: 'Leave blank, allowed wildcards [, - * /]',
      every: 'Every year',
      averagePrefix: 'Starting from',
      averageMiddle: 'year(s), every',
      averageSuffix: 'year(s)'
    },
    result: {
      title: 'Last 5 Run Times',
      calculating: 'Calculating...',
      noResult: 'No matching results found!',
      limit: 'Only {count} results within the last 100 years!'
    },
    cronUtils: {
      execute: 'execute',
      invalidExpression: 'Invalid Cron expression'
    }
  }
}
