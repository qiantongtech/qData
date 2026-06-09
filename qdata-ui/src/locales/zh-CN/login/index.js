// login 包入口（form 字段扁平展开，greeting/info 保持嵌套）
import form from './form'
import greeting from './greeting'
import info from './info'

export default {
  ...form,
  greeting,
  info
}
