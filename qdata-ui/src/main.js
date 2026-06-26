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

// 初始化多语言
import { setupI18n, i18n } from '@/plugins/vueI18n'
import { useLocaleStoreWithOut } from '@/store/system/locale'

import '@/assets/styles/system/index.scss' // global css
import '@/assets/styles/system/anivia.scss' // 自定义样式 css
import '@/assets/icons/iconfont/iconfont.css' // iconfont css

import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive

// 注册指令
import plugins from './plugins' // plugins
import { download, download2 } from '@/utils/request'
// 引入自定义事件总线
import bus from '@/utils/bus';

// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
import elementIcons from '@/components/SvgIcon/svgicon'

import './permission' // permission control

import { useDict } from '@/utils/dict'
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels, getFormatValue, formatNewlines, formatVersion, downloadContent } from '@/utils/anivia.js'

// 分页组件
import Pagination from '@/components/Pagination'
// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar'
// 自定义表格工具组件 样式二
import RightToolbar2 from '@/components/RightToolbar/index2.vue'
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload2"
// 文件上传按钮组件
import FileUploadbtn from "@/components/FileUploadbtn"
// 提示组件
import GuideTip from "@/components/GuideTip"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 自定义树选择组件
import TreeSelect from '@/components/TreeSelect'
// 字典标签组件
import DictTag from '@/components/DictTag'
// 可视化表单设计器工具
// import FcDesigner from '@form-create/designer';
import '@/assets/icons/iconfont/fontNew/iconfont.css' // iconfont css
// 通用详情页头部组件
import DetailInfo from "@/components/DetailInfo"
// 通用描述信息组件（el-descriptions 封装）
import DescriptionsInfo from "@/components/DescriptionsInfo"

import QtSearchBar from '@/components/QtSearchBar/index.vue';
import QtWrap from '@/components/QtWrap/index.vue';
import QtTable from '@/components/QtTable/index.vue';
import QtTabPane from '@/components/QtTabPane/index.vue';
import QtFormItem from '@/components/QtFormItem/index.vue';
import QtTagGroup from '@/components/QtTagGroup/index.vue';

const app = createApp(App)

// 初始化多语言（必须在使用 store / element-plus 之前完成）
const setupAll = async () => {
  app.use(store)
  await setupI18n(app)

  app.use(AniviaComponents)
//   app.use(FcDesigner)
//   app.use(FcDesigner.formCreate)

  // 全局方法挂载
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
  // 将事件总线挂载到全局属性
  app.config.globalProperties.$bus = bus

  // 全局组件挂载
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

  // 使用 element-plus 并且设置全局的大小、跟随当前语言
  const localeStore = useLocaleStoreWithOut()
  app.use(ElementPlus, {
    locale: localeStore.getCurrentLocale.elLocale,
    // 支持 large、default、small
    size: Cookies.get('size') || 'default'
  })

  app.mount('#app')
}

setupAll()
