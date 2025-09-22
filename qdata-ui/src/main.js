import { createApp } from 'vue'

import Cookies from 'js-cookie'

import ElementPlus from 'element-plus'
import AniviaComponents from 'anivia-components'
import 'anivia-components/style.css'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn'

import '@/assets/system/styles/index.scss' // global css
import '@/assets/system/styles/anivia.scss' // 自定义样式 css
import '@/assets/iconfont/iconfont.css' // iconfont css

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
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels } from '@/utils/anivia.js'

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
import FcDesigner from '@form-create/designer';
import '@/assets/iconfont/font_new/iconfont.css' // iconfont css
const app = createApp(App)
app.use(AniviaComponents)
app.use(FcDesigner);
app.use(FcDesigner.formCreate);

// 全局方法挂载
app.config.globalProperties.useDict = useDict
app.config.globalProperties.download = download
app.config.globalProperties.download2 = download2
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.resetForm = resetForm
app.config.globalProperties.handleTree = handleTree
app.config.globalProperties.addDateRange = addDateRange
app.config.globalProperties.selectDictLabel = selectDictLabel
app.config.globalProperties.selectDictLabels = selectDictLabels
// 将事件总线挂载到全局属性
app.config.globalProperties.$bus = bus;

// 全局组件挂载
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
app.use(router)
app.use(store)
app.use(plugins)
app.use(elementIcons)
app.component('svg-icon', SvgIcon)

directive(app)

// 使用element-plus 并且设置全局的大小
app.use(ElementPlus, {
  locale: locale,
  // 支持 large、default、small
  size: Cookies.get('size') || 'default'
})

app.mount('#app')
