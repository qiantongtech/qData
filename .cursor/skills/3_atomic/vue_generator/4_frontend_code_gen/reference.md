# 生成前端代码

## 1. 推荐文件位置（须经用户确认后再落盘）

根据 [30-architecture](../../../../rules/30-project/30-architecture.mdc) 与 [01-style-format](../../../../rules/00-core/01-style-format.mdc)：

| 类型 | 规则 | 推荐示例（entityNameEn: DataAsset，模块 da） |
|------|------|---------------------------------------------|
| 列表页 | views 层级与菜单一致，列表页 `index.vue` | `src/views/da/dataAsset/index.vue` |
| 新增/编辑/详情 | 小驼峰+功能名，如 add、detail（若独立页面） | `src/views/da/dataAsset/add/index.vue`、`detail/index.vue` |
| API 模块 | api 按业务模块分目录，文件小驼峰、不超过 3 个词 | `src/api/da/dataAsset.js` |
| Mock | 同目录下 `xxx.mock.js` | `src/api/da/dataAsset.mock.js` |
| 路由 | **主列表页不生成路由**：由后台菜单配置、框架动态获取；用户自行在菜单管理后台增加菜单。**内部路由**：若为独立页面的 add/edit/detail，在模块内按规范生成子路由（path 小写+中划线），见 SKILL 路由约定。 | 不生成主列表路由；仅模块内子路由（若有）按 router 规范生成 |

**推荐输出格式（给用户确认）**：

```
推荐落盘路径（请确认是否同意，同意后再执行写入）：

- 列表页：src/views/da/dataAsset/index.vue
- API：   src/api/da/dataAsset.js
- Mock：  src/api/da/dataAsset.mock.js

说明：主列表页入口路由由后台配置，请稍后在「菜单管理后台」中新增对应菜单。若采用独立页面的新增/编辑/详情，将按路由规范生成模块内子路由。

一级模块目录 [da] 可根据您的菜单调整；若需改为其他目录名请直接说明。
```

用户回复同意或给出修改后的路径后，再执行写入。若项目目录与规则假设不一致（如无 `src/views`、`src/api`），推荐路径时先说明并基于项目实际目录给出建议，须经用户确认后再落盘。

## 2. API 模块与 Mock 命名（按 rules）

- **api 文件名**：小驼峰，表达接口职责、不超过 3 个单词（如 `dataAsset.js`、`assetApply.js`）。实体即资源时可用 entityNameEn 转小驼峰（如 DataAsset → dataAsset.js）。
- **mock 文件名**：与 api 同目录，后缀 `.mock.js`，即 `dataAsset.mock.js`。与 30-architecture 中「同目录下可放 mock 文件（xxx.mock.js）」一致。

## 3. Mock 数据与响应结构（按 31-api-contract）

- 统一响应结构：`{ code, msg, data }`；成功 `code === 200`，业务数据在 `data`。
- 列表：`data` 为 `{ total: number, list: Array<...> }`，与 page_spec.columns 的 key 一致。
- 详情：`data` 为单条对象，字段与 page_spec 列/表单一致。
- 新增/修改/删除：成功时 `data` 可为 `{ id }` 或空，`msg` 提示成功。

Mock 实现方式：在 `dataAsset.mock.js` 中导出与 `dataAsset.js` 同名的请求函数，返回 Promise resolve 上述结构；或通过项目已有的 mock 机制（如 mockjs、vite  mock）拦截对应 path。具体以项目现有 mock 约定为准。

## 4. 正式 api.js 形态（暂不接后端）

- 按 RESTful 与 31-api-contract：GET list、GET :id、POST、PUT、DELETE 等，路径与 31 一致。
- 方法名与 01-style-format 中 API 约定一致，小驼峰、语义清晰（如 `getDataAssetList`、`getDataAssetById`、`createDataAsset`、`updateDataAsset`、`deleteDataAsset`）。
- 当前阶段：函数体内可先调用 mock 或返回 Promise.resolve(mockData)，不写真实 baseURL 与后端地址；注释标明「待对接后端时替换为 request 调用」。

## 5. 缺信息时询问（不脑补）

以下情况须向用户列出并询问，不得自行默认：

- page_spec 中无 **searchFields** 或 **actions**：是否不需要搜索/是否仅列表只读，需用户确认。
- **一级模块名/views 路径**未知：推荐 da 或根据 entityNameEn 推断，但须用户确认；主列表不生成路由，由用户到菜单管理后台配置。
- **权限标识**：若项目使用 v-hasPermi，权限码（如 `da:dataAsset:list`）须用户确认或明确「暂不挂权限」。
- 枚举 **enumOptions** 为空：是否后续接字典接口，或先写死若干选项让用户选。

列出「待确认项」→ 用户补充或确认 → 再继续生成。
