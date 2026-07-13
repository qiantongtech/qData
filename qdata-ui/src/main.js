/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import { createApp } from 'vue'

import Cookies from 'js-cookie'

import ElementPlus from 'element-plus'
import AniviaComponents from 'anivia-components'
import 'anivia-components/style.css'
import 'element-plus/dist/index.css'

// Initialize multiple languages
import { setupI18n, i18n } from '@/plugins/vueI18n'
import { useLocaleStoreWithOut } from '@/store/system/locale'

import '@/assets/styles/system/index.scss' // global css
import '@/assets/styles/system/anivia.scss' // Custom style css
import '@/assets/icons/iconfont/iconfont.css' // iconfont css

import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive

// Registration instructions
import plugins from './plugins' // plugins
import { download, download2 } from '@/utils/request'
// Introducing a custom event bus
import bus from '@/utils/bus';

// svg icon
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
import elementIcons from '@/components/SvgIcon/svgicon'

import './permission' // permission control

import { useDict } from '@/utils/dict'
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels, getFormatValue, formatNewlines, formatVersion, downloadContent } from '@/utils/anivia.js'

// Pagination component
import Pagination from '@/components/Pagination'
// Custom form tool component
import RightToolbar from '@/components/RightToolbar'
// Custom table tool component style 2
import RightToolbar2 from '@/components/RightToolbar/index2.vue'
// Rich text component
import Editor from "@/components/Editor"
// File upload component
import FileUpload from "@/components/FileUpload2"
// File upload button component
import FileUploadbtn from "@/components/FileUploadbtn"
// Prompt component
import GuideTip from "@/components/GuideTip"
// Image upload component
import ImageUpload from "@/components/ImageUpload"
// Image preview component
import ImagePreview from "@/components/ImagePreview"
// Custom tree selection component
import TreeSelect from '@/components/TreeSelect'
// dictionary tag component
import DictTag from '@/components/DictTag'
// Visual form designer tool
// import FcDesigner from '@form-create/designer';
import '@/assets/icons/iconfont/fontNew/iconfont.css' // iconfont css
// Universal detail page header component
import DetailInfo from "@/components/DetailInfo"
// Universal description information component (el-descriptions package)
import DescriptionsInfo from "@/components/DescriptionsInfo"

import QtSearchBar from '@/components/QtSearchBar/index.vue';
import QtWrap from '@/components/QtWrap/index.vue';
import QtTable from '@/components/QtTable/index.vue';
import QtTabPane from '@/components/QtTabPane/index.vue';
import QtFormItem from '@/components/QtFormItem/index.vue';
import QtTagGroup from '@/components/QtTagGroup/index.vue';

const app = createApp(App)

// Initialize multilingual (must be done before using store / element-plus)
const setupAll = async () => {
  app.use(store)
  await setupI18n(app)

  app.use(AniviaComponents)
//   app.use(FcDesigner)
//   app.use(FcDesigner.formCreate)

  // Global method mounting
  app.config.globalProperties.labelPosition = i18n.global.locale.value === 'zh-CN' ? 'right' : 'top';
  app.config.globalProperties.useDict = useDict
  app.config.globalProperties.download = download
  app.config.globalProperties.download2 = download2
  app.config.globalProperties.parseTime = parseTime
  app.config.globalProperties.resetForm = resetForm
  app.config.globalProperties.handleTree = handleTree
  app.config.globalProperties.addDateRange = addDateRange
  app.config.globalProperties.selectDictLabel = selectDictLabel
  app.config.globalProperties.selectDictLabels = selectDictLabels
  app.config.globalProperties.getFormatValue = getFormatValue
  app.config.globalProperties.downloadContent = downloadContent
  app.config.globalProperties.formatVersion = formatVersion
  // Mount event bus to global properties
  app.config.globalProperties.$bus = bus

  // Global component mounting
  app.component('QtTagGroup', QtTagGroup)
  app.component('DictTag', DictTag)
  app.component('Pagination', Pagination)
  app.component('TreeSelect', TreeSelect)
  app.component('FileUpload', FileUpload)
  app.component('FileUploadbtn', FileUploadbtn)
  app.component('GuideTip', GuideTip)
  app.component('ImageUpload', ImageUpload)
  app.component('ImagePreview', ImagePreview)
  app.component('RightToolbar', RightToolbar)
  app.component('RightToolbar2', RightToolbar2)
  app.component('Editor', Editor)
  app.component('QtSearchBar', QtSearchBar)
  app.component('QtWrap', QtWrap)
  app.component('QtTable', QtTable)
  app.component('QtTabPane', QtTabPane)
  app.component('QtFormItem', QtFormItem)
  app.component('DetailInfo', DetailInfo)
  app.component('DescriptionsInfo', DescriptionsInfo)

  app.use(router)
  app.use(plugins)
  app.use(elementIcons)
  app.component('svg-icon', SvgIcon)

  directive(app)

  // Use element-plus and set the global size to follow the current language
  const localeStore = useLocaleStoreWithOut()
  app.use(ElementPlus, {
    locale: localeStore.getCurrentLocale.elLocale,
    // Support large, default, small
    size: Cookies.get('size') || 'default'
  })

  app.mount('#app')
}

setupAll()
