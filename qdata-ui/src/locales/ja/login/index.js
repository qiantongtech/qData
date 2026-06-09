// login パッケージ入口（form フラット展開、greeting/info はネスト）（骨架，內容待補全）
import form from './form'
import greeting from './greeting'
import info from './info'

export default {
  ...form,
  greeting,
  info
}