---
name: frontend-code-gen
description: 根据数据模型定义产出的 page_spec 生成 Vue3 + Element Plus 前端代码、正式 api 模块与 mock 接口；缺信息时不脑补、须向用户询问；代码落盘路径须推荐并获用户确认后再写入。当前仅对接 mock 数据，api.js 正常输出但暂不接后端。Use when generating CRUD frontend from page_spec or when user asks for Vue code generation from page_spec.
inputs:
  - name: page_spec
    type: object
    description: 上一步（数据模型定义）产出，符合 schemas/page_spec.json；须先校验再生成
  - name: module_path_hint
    type: string
    description: 用户指定的模块/一级目录（如 da、dpp），可选，用于推荐路径
    required: false
outputs:
  - name: code_artifacts
    type: object
    description: 生成的页面与组件等代码产物及路径
  - name: api_module_path
    type: string
    description: 最终落盘的 api 模块路径（用户确认后）
  - name: files_written
    type: array
    description: 已写入的文件路径列表
---

# 生成前端代码

根据 **page_spec**（上一节点数据模型定义的产出）生成 CRUD 页面、API 模块与 mock 数据。**不凭空脑补业务规则**：缺信息必须向用户询问；**代码文件位置必须先推荐并征得用户同意后再落盘**。

**流程暂存（crud_flow）**：本步输入 page_spec 来自 `.cursor/workflow_artifacts/crud_flow/03_page_spec.json`。用户要求修改 page_spec 后再生成时，执行层应先从该路径读取 → 按用户意图修改 → 写回同一路径，再执行本步，见 `skills/2_workflows/EXECUTION.md`。

## 必须遵循的项目规则

生成代码与目录、命名、接口形态均须符合项目 `.cursor/rules` 中与开发相关的规则，执行前应读取并遵守：

| 规则文件 | 内容 |
|----------|------|
| [.cursor/rules/00-core/00-language-javascript.mdc](../../../../rules/00-core/00-language-javascript.mdc) | JS 规范、注释、早返回 |
| [.cursor/rules/00-core/01-style-format.mdc](../../../../rules/00-core/01-style-format.mdc) | 命名（views/components/api/路由）、Import 顺序、文件组织 |
| [.cursor/rules/10-frontend/10-vue-basics.mdc](../../../../rules/10-frontend/10-vue-basics.mdc) | Vue3 template→script→style、Composition API、composables |
| [.cursor/rules/10-frontend/11-ui-components.mdc](../../../../rules/10-frontend/11-ui-components.mdc) | 属性顺序、v-for :key、Element Plus |
| [.cursor/rules/10-frontend/12-style.mdc](../../../../rules/10-frontend/12-style.mdc) | CSS 选择器、BEM、变量 |
| [.cursor/rules/30-project/30-architecture.mdc](../../../../rules/30-project/30-architecture.mdc) | 目录职责（api/views/router 等）、引用边界 |
| [.cursor/rules/30-project/31-api-contract.mdc](../../../../rules/30-project/31-api-contract.mdc) | 请求封装、RESTful、响应结构、权限标识、URL 规范 |
| [.cursor/rules/30-project/32-project-context.mdc](../../../../rules/30-project/32-project-context.mdc) | 项目背景与技术栈约束（qData 数据中台；Vue3 + Element Plus、目录与接口约定等），生成时须与之一致 |

## 输入校验（上一步产出）

在执行生成前，**必须校验** page_spec 是否满足本技能输入要求：

- 存在且含 **entityName、entityNameEn、columns**（必填）；columns 每项含 **key、label、type**。
- 若从流程暂存读取，须为完整 page_spec（根级无 clarificationNeeded）；若缺少或结构不符合 [page_spec.json](../../../schemas/page_spec.json)，**不得脑补**，须向用户说明缺失项并等待补全后再执行。

## 核心约束

1. **不脑补业务规则**：page_spec 未给出的字段、操作、校验、权限等，不得擅自发明；缺失时明确列出并**询问用户**，用户确认或补全后再生成。
2. **代码落盘路径须推荐并获用户确认**：根据 page_spec.entityNameEn 与项目架构规则，给出推荐的 **views 路径**、**api 模块路径**（如 `src/views/da/dataAsset/index.vue`、`src/api/da/dataAsset.js`），**先呈现给用户并询问是否可行**；用户同意后才执行写入，否则仅输出代码内容不落盘或按用户指定路径再落盘。

## 路由约定

- **功能主列表页（入口）不生成路由**：列表页由**后台菜单配置**，框架**动态获取**路由与菜单；用户需自行在**菜单管理后台**增加新菜单项，无需在本技能中生成或修改主列表页的路由配置。
- **内部路由由本技能按规范生成**：若采用独立页面形式的新增/编辑/详情（非弹窗），则在对应**模块内**按项目路由规范生成**内部子路由**（path 小写+中划线，如 `add`、`edit`、`detail` 等），仅用于模块内跳转，与 01-style-format、31-api-contract 中的 path 约定一致。不修改框架级或菜单级路由文件。

## 输出要求

1. **代码产物**：列表页（含筛选、表格、分页）、新增/编辑弹窗或页面、详情展示等，符合 page_spec 的 columns、searchFields、actions；组件与页面结构符合 Vue3 + Element Plus 及上述规则。
2. **Mock 数据 API**：按规则输出 **mock 接口**，与 page_spec 的实体与字段一致，供当前阶段前端联调使用；mock 文件命名按规则（同目录下 `xxx.mock.js`），见 [reference.md](reference.md)。
3. **正式 api.js**：按 31-api-contract 与 01-style-format 输出 **正式 API 模块**（小驼峰、不超过 3 个单词），包含 list/get/add/edit/remove 等与 page_spec.actions 对应的请求方法；**当前不对接后端**，仅保证形态正确，后续接真实接口时可直接使用。
4. **api.mock.js**：与 api 模块同目录，命名为 `xxx.mock.js`，实现 mock 数据与延迟，供开发阶段使用。

## 验收

1. **页面完全满足 page_spec**：表格列、搜索项、操作按钮、表单字段、分页等与 page_spec 一致；无 page_spec 未约定的额外业务逻辑。
2. **install && dev 能启动，无编译错误**：生成后执行 `npm install`（或 pnpm/yarn）与项目 dev 命令，无报错、可正常打开页面。

## 流程建议（两阶段落盘）

1. 读取并校验 page_spec；不通过则提示用户补全，不继续。
2. **阶段一**：根据 entityNameEn 与架构规则，输出**推荐路径**（列表页、api、mock），并明确询问用户「是否同意以上路径？同意后我将写入文件。」
3. **阶段二**：仅在用户**明确确认**（如同意、可以、就按这个）后，再写入文件；若用户未确认或修改路径，则按用户指定路径写入或仅输出代码不落盘。
4. 生成完成后可提示用户执行 install && dev 做验收。

详细文件路径约定与 mock 结构见 [reference.md](reference.md).
