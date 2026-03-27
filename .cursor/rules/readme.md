.cursor/rules/
├── 00-core/                          # 全局通用底座
│   ├── 00-language-javascript.mdc    # JS 规范、注释规范（JSDoc、TODO/FIXME）、早返回
│   └── 01-style-format.mdc          # 命名（views/components/api/静态/路由/脚本）、Import 顺序
│
├── 10-frontend/                      # 前端（Vue3 + Element Plus）
│   ├── 10-vue-basics.mdc            # 文件结构 template→script→style、Composition API、composables
│   ├── 11-ui-components.mdc         # 属性顺序、v-for :key、Element Plus、组件目录/文件命名
│   └── 12-style.mdc                 # CSS：选择器、BEM、变量、禁止内联、书写顺序
│
├── 20-quality/                       # 质量保障
│   ├── 20-testing.mdc                # Vitest、Vue Test Utils、mock、覆盖要求
│   ├── 21-performance.mdc            # 避免重复渲染、路由懒加载、大列表
│   └── 22-security.mdc               # XSS、Token 存储、日志脱敏、依赖
│
├── 30-project/                       # 项目约定
│   ├── 30-architecture.mdc           # 目录结构（api/assets/components/layout/router/store/views 等）
│   ├── 31-api-contract.mdc           # 请求封装、RESTful、URL 规范、权限 v-hasPermi
│   └── 32-project-context.mdc        # 项目背景与技术栈约束（qData 数据中台）
