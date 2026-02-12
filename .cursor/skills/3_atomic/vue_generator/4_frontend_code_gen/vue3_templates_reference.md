# Vue3 模板参考（生成代码必须遵循）

生成前端代码时**必须基于**本项目的 Vue3 官方模板，保证与现有页面结构、样式类名、组件用法一致。模板源码位于：

**`qdata-framework/qdata-generator/src/main/resources/vm/vue/v3/`**

- **列表页**：`index.vue.vm`
- **树表页**：`index-tree.vue.vm`
- **复杂详情页**：`detail/complex-detail.vue.vm`
- **详情内嵌表格组件**：`detail/componentOne.vue.vm`、`detail/componentTwo.vue.vm`（按需）

执行生成前应**优先读取上述模板文件**或本参考文档，按下面结构产出代码，不得自创布局与类名。

---

## 一、列表页（index）结构约定

### 1. 根与分区

- 根节点：`<div class="app-container" ref="app-container">`
- 搜索区：`<div class="pagecont-top" v-show="showSearch">`
- 表格区：`<div class="pagecont-bottom">`

### 2. 搜索表单

- 表单：`<el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px" v-show="showSearch" @submit.prevent>`
- 输入项：`class="el-form-input-width"`，带 `clearable`、`@keyup.enter="handleQuery"`
- 按钮：查询用 `<i class="iconfont-mini icon-a-zu22377 mr5"></i>查询`，重置用 `<i class="iconfont-mini icon-a-zu22378 mr5"></i>重置`
- 按钮需加 `@mousedown="(e) => e.preventDefault()"` 防止表单误提交

### 3. 表格上方工具栏

- 外层：`<div class="justify-between mb15">`
- 按钮行：`<el-row :gutter="15" class="btn-style">`，每个按钮用 `<el-col :span="1.5">` 包裹
- 按钮样式：`type="primary" plain`（新增）、`type="danger" plain`（删除）等；带 `@mousedown="(e) => e.preventDefault()"`
- 图标：新增 `<i class="iconfont-mini icon-xinzeng mr5"></i>`，修改 `<i class="iconfont-mini icon-xiugai--copy mr5"></i>`，删除 `<i class="iconfont-mini icon-shanchu-huise mr5"></i>`，导入 `<i class="iconfont-mini icon-upload-cloud-line mr5"></i>`，导出 `<i class="iconfont-mini icon-download-line mr5"></i>`
- 右侧：`<div class="justify-end top-right-btn">` 内放 `<right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>`

### 4. 表格

- `el-table`：`stripe`、`height="60vh"`、`v-loading="loading"`、`:data="xxxList"`、`@selection-change="handleSelectionChange"`、`:default-sort="defaultSort"`、`@sort-change="handleSortChange"`
- 列：列表列用 `<template #default="scope">{{ scope.row.xxx || '-' }}</template>`；日期用 `parseTime(scope.row.xxx, '{y}-{m}-{d}')`；**字典列**用 `<dict-tag :options="字典变量名" :value="scope.row.xxx"/>`（字典变量名即 `useDict` 解构出来的变量，见下文字典约定）；图片用 `<image-preview :src="scope.row.xxx" :width="50" :height="50"/>`
- 操作列：`label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240"`，内部 `el-button link type="primary" icon="Edit"` 等
- 空数据：`<template #empty><div class="emptyBg"><img src="@/assets/system/images/no_data/noData.png" alt="" /><p>暂无记录</p></div></template>`

### 5. 分页

- `<pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />`

### 6. 弹窗（新增/编辑）

- 对话框：`<el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>`
- 表头：`<template #header="{ close, titleId, titleClass }"><span role="heading" aria-level="2" class="el-dialog__title">{{ title }}</span></template>`
- 表单：`<el-form ref="xxxRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>`，表单项用 `<el-row :gutter="20">` + `<el-col :span="12">` 两列布局
- 底部：`<template #footer><div class="dialog-footer"><el-button size="mini" @click="cancel">取 消</el-button><el-button type="primary" size="mini" @click="submitForm">确 定</el-button></div></template>`

### 6.1 详情弹窗（el-descriptions 描述列表，单列展示）

当 actions 含 `view` 且**无独立详情页**时，详情使用**独立的 el-dialog + el-descriptions**，不复用新增/编辑弹窗。

**结构约定**：
```html
<!-- 详情弹窗 —— 独立于新增/编辑弹窗 -->
<el-dialog title="XXX详情" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
  <el-descriptions :column="2" border>
    <el-descriptions-item v-for="(item, index) in detailDescList" :key="index"
      label-class-name="base-label" :span="item.span" class-name="base-content">
      <template #label>
        <div class="cell-item">{{ item.label }}</div>
      </template>
      <!-- 字典类型字段用 dict-tag 回显 -->
      <div v-if="item.dictCode">
        <dict-tag :options="getDictOptions(item.dictCode)" :value="detailData[item.key]"/>
      </div>
      <!-- 日期类型字段用 parseTime -->
      <div v-else-if="item.type === 'datetime'">
        {{ parseTime(detailData[item.key], '{y}-{m}-{d} {h}:{i}') || '-' }}
      </div>
      <div v-else-if="item.type === 'date'">
        {{ parseTime(detailData[item.key], '{y}-{m}-{d}') || '-' }}
      </div>
      <!-- 普通字段 -->
      <div v-else>{{ detailData[item.key] ?? '-' }}</div>
    </el-descriptions-item>
  </el-descriptions>
  <template #footer>
    <div class="dialog-footer">
      <el-button size="mini" @click="openDetail = false">关 闭</el-button>
    </div>
  </template>
</el-dialog>
```

**Script 中的数据定义**：
```javascript
/** ——— 详情弹窗 ——— */
const openDetail = ref(false);
const detailData = ref({});

// 详情展示字段配置（根据 page_spec columns 生成，排除不需要展示的字段如 id）
const detailDescList = ref([
  { key: 'datasourceName', label: '名称' },
  { key: 'datasourceType', label: '类型', dictCode: 'datasource_type' },
  { key: 'ip', label: 'IP地址' },
  { key: 'port', label: '端口号' },
  { key: 'status', label: '状态', dictCode: 'sys_disable' },
  { key: 'createBy', label: '创建人' },
  { key: 'createTime', label: '创建时间', type: 'datetime' },
  { key: 'updateBy', label: '更新人' },
  { key: 'updateTime', label: '更新时间', type: 'datetime' },
  { key: 'remark', label: '备注', span: 2 },
]);

// 辅助函数：根据 dictCode 获取字典 options
function getDictOptions(dictCode) {
  // 返回对应的字典变量，所有字典变量通过 useDict 已加载
  const dictMap = { datasource_type, sys_disable /* ...其他字典... */ };
  return dictMap[dictCode];
}

/** 查看详情 */
function handleDetail(row) {
  getXxx(row.id).then(response => {
    detailData.value = response.data;
    openDetail.value = true;
  });
}
```

**样式约定**（与项目现有 el-descriptions 一致）：
```css
:deep(.base-label) {
  width: 200px;
  .cell-item {
    font-weight: 500;
  }
}
```

**重要**：
- 详情弹窗**必须是独立的 el-dialog**，不能复用新增/编辑弹窗的 form :disabled 模式
- 字段有 dictCode 时**必须用 dict-tag** 回显
- 日期类型**必须用 parseTime** 格式化
- 备注、描述等长文本字段设置 `span: 2` 占整行
- 底部只有「关 闭」按钮

### 7. Script 约定

- 列表数据：`xxxList = ref([])`，与 API 返回的 `response.data.rows` / `response.data.list` 对应
- 分页：`queryParams.pageNum`、`queryParams.pageSize`、`total`
- 列显隐：`columns` 数组含 `key、label、visible`，`getColumnVisibility(index)` 控制 `v-if`
- 消息：`proxy.$modal.msgSuccess`、`proxy.$modal.confirm`、`proxy.resetForm('queryRef'/'xxxRef')`

### 8. 字典使用约定（dictCode → useDict + dict-tag）

当 page_spec 中 column 的 `displayType` 为 `"dict"` 且有 `dictCode` 时，**必须使用项目字典机制**，不得用硬编码 `v-for el-option` + tag 的方式回显。

**加载字典**（script 区域）：
```javascript
const { dict_code_a, dict_code_b } = proxy.useDict('dict_code_a', 'dict_code_b');
```
- 变量名与 dictCode 相同（下划线命名），多个字典在同一个 `useDict` 调用中加载。

**列表列回显**（table 区域）：
```html
<el-table-column label="类型" align="center" prop="type">
  <template #default="scope">
    <dict-tag :options="dict_code_a" :value="scope.row.type"/>
  </template>
</el-table-column>
```

**搜索表单中使用字典**（搜索区域 el-select）：
```html
<el-form-item label="类型" prop="type">
  <el-select v-model="queryParams.type" class="el-form-input-width" placeholder="请选择类型" clearable>
    <el-option v-for="dict in dict_code_a" :key="dict.value" :label="dict.label" :value="dict.value"/>
  </el-select>
</el-form-item>
```

**新增/编辑表单中使用字典**（dialog 表单 el-select）：
```html
<el-form-item label="类型" prop="type">
  <el-select v-model="form.type" placeholder="请选择类型">
    <el-option v-for="dict in dict_code_a" :key="dict.value" :label="dict.label" :value="dict.value"/>
  </el-select>
</el-form-item>
```

**详情中回显字典**（el-descriptions 中）：
```html
<dict-tag :options="dict_code_a" :value="detail.type"/>
```

**重要**：若 page_spec 中 column 没有 dictCode（仅有 enumOptions），则用硬编码 `el-option` 遍历 enumOptions；一旦有 dictCode，必须用 `useDict` + `dict-tag`。

### 9. 表单校验规则生成约定（validation → rules）

page_spec 中的 `columns[].required` 和 `columns[].validation` 必须转为 Element Plus 的 `rules` 对象。

**生成规则**：

```javascript
const rules = ref({
  // required 字段 → 必填校验
  datasourceName: [
    { required: true, message: '名称不能为空', trigger: 'blur' },
    { max: 100, message: '名称长度不能超过100个字符', trigger: 'blur' },
  ],
  // enum 类型 required 字段 → trigger 用 change
  datasourceType: [
    { required: true, message: '请选择数据连接类型', trigger: 'change' },
  ],
  // number 类型 → 范围校验
  port: [
    { required: true, message: '端口号不能为空', trigger: 'blur' },
    { type: 'number', min: 1, max: 65535, message: '端口号范围 1-65535', trigger: 'blur', transform: Number },
  ],
  // 有 pattern 的字段
  ip: [
    { required: true, message: 'IP地址不能为空', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9.\-:]+$/, message: '请输入有效的主机地址', trigger: 'blur' },
  ],
  // 非必填但有长度限制
  remark: [
    { max: 500, message: '备注长度不能超过500个字符', trigger: 'blur' },
  ],
});
```

**转换映射**：

| page_spec 字段 | Element Plus rules |
|---|---|
| `required: true` + type 为 string/number | `{ required: true, message: 'xxx不能为空', trigger: 'blur' }` |
| `required: true` + type 为 enum/boolean | `{ required: true, message: '请选择xxx', trigger: 'change' }` |
| `validation.maxLength` | `{ max: N, message: '...', trigger: 'blur' }` |
| `validation.pattern` | `{ pattern: /regex/, message: '...', trigger: 'blur' }` |
| `validation.min` / `validation.max` | `{ type: 'number', min: N, max: M, message: '...', trigger: 'blur', transform: Number }` |

**重要**：所有 `required: true` 的字段**必须**有对应的必填校验规则；有 `validation` 的字段**必须**生成对应的校验规则。不得遗漏。

---

## 二、树表页（index-tree）结构约定

- 根与分区同列表页：`app-container`、`pagecont-top`、`pagecont-bottom`
- 表格：`row-key` 为树主键字段，`:tree-props="{ children: 'children', hasChildren: 'hasChildren' }"`，`:default-expand-all="isExpandAll"`
- 工具栏：新增、展开/折叠（`toggleExpandAll`），无批量删除时可不做 selection
- 数据：`listXxx()` 返回后用 `proxy.handleTree(response.data.rows, treeCode, treeParentCode)` 转树形
- 新增/编辑表单中若有父级，使用 `el-tree-select` 选择父节点（`value-key`、`check-strictly`）

---

## 三、复杂详情页（detail）结构约定

- 根：`<div class="app-container" ref="app-container">`
- 顶部信息区：`<div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px"><div class="infotop"><div class="infotop-title mb15">主键或标题</div><el-row :gutter="20">`
- 每块字段：`<el-col :span="8"><div class="infotop-row border-top"><div class="infotop-row-lable">标签</div><div class="infotop-row-value">值或 dict-tag / image-preview</div></div></el-col>`
- 日期：`{{ parseTime(xxxDetail.xxx, '{y}-{m}-{d}') }}`
- 下方 Tab：`<div class="pagecont-bottom"><el-tabs v-model="activeName">`，可挂子组件（如 componentOne、componentTwo）
- 数据：由 `route.query.id` 取 id，`getXxx(id)` 得到详情，赋给 `xxxDetail`

---

## 四、page_spec 与模板的映射

| page_spec | 模板/输出用法 |
|-----------|----------------|
| entityNameEn | 转小驼峰作 businessName（如 DataAsset → dataAsset）；首字母大写作 BusinessName（如 getDataAsset） |
| columns[].key | 对应字段名（javaField），用于 v-model、prop、scope.row.xxx |
| columns[].label | 表单项 label、表头 label、placeholder「请输入/请选择」+ label |
| columns[].type | string→input，enum→select/radio + dict-tag，date/datetime→el-date-picker + parseTime |
| columns[].enumOptions | 无 dictCode 时用作 el-option 遍历；有 dictCode 时仅 mock 用途 |
| columns[].dictCode | **优先级最高**：有 dictCode 时须用 `proxy.useDict(dictCode)` 加载字典 → 列表用 `<dict-tag :options="dictVar" :value="..."/>`，搜索/表单用 `<el-select>` + 字典 options 遍历，详情用 `<dict-tag>` 回显。见第 8 节 |
| columns[].displayType | `"dict"` 表示字典标签回显（配合 dictCode），`"switch"` 表示开关等 |
| columns[].required | 为 true 时生成必填校验规则，string/number 用 `trigger: 'blur'`，enum/boolean 用 `trigger: 'change'` |
| columns[].validation | 转为 Element Plus rules 中的 max/min/pattern 等校验，见第 9 节 |
| searchFields | 仅 key 在 searchFields 的列出现在 pagecont-top 搜索表单 |
| actions | create→新增按钮/弹窗，update→修改，delete→删除，view→**详情弹窗**（el-descriptions，见 6.1）或详情页 |
| permissionPrefix / actionPermissions | 对应 v-hasPermi 的权限码（如 da:dataSource:add） |
| tableConfig.stripe | el-table 的 stripe |
| pagination | queryParams.pageNum/pageSize，total，pagination 组件 |

生成时按 page_spec 的 columns、searchFields、actions 驱动上述结构，**不得省略** app-container、pagecont-top、pagecont-bottom、btn-style、right-toolbar、iconfont-mini、emptyBg、dialog-footer、infotop 等类名与结构，否则与项目现有样式不一致。
