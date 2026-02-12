# 数据模型定义

## 1. 通用字段（每表自动带入）

以下字段为项目内**通用字段**，在解析用户表结构或推荐表结构时**自动带上**（若 DDL 中已存在则用表内命名与注释，缺失则按下表补全）。前端 key 使用驼峰。

| 数据库字段名（下划线） | 前端 key（驼峰） | 中文注释 | 建议展示方式 |
|------------------------|------------------|----------|--------------|
| VALID_FLAG | validFlag | 是否有效；0：无效，1：有效 | 预留，多数不展示 |
| DEL_FLAG | delFlag | 删除标志；1：已删除，0：未删除 | 预留，一般不展示 |
| CREATE_BY | createBy | 创建人 | 列表/详情常展示 |
| CREATOR_ID | creatorId | 创建人id | 预留，多数不展示 |
| CREATE_TIME | createTime | 创建时间 | 列表/详情常展示 |
| UPDATE_BY | updateBy | 更新人 | 详情可展示 |
| UPDATER_ID | updaterId | 更新人id | 预留，多数不展示 |
| UPDATE_TIME | updateTime | 更新时间 | 详情常展示 |
| REMARK | remark | 备注 | 表单/详情可展示 |
| CREATE_TYPE | createType | 创建类型；1：虚拟资产创建，2：完整资产创建 | 预留或详情展示 |

产出 page_spec 时：columns 应包含**业务字段 + 通用字段**；是否在列表/详情/表单中展示由代码生成技能按上表「建议展示方式」或项目约定处理（可不全部展示）。

## 2. 表结构 DDL 解析（两种写法）

- **Oracle / 达梦等**：`CREATE TABLE ... ( 列定义 );` + `COMMENT ON TABLE ...`、`COMMENT ON COLUMN 表.列 IS '注释';`，从 COLUMN 注释解析字段说明。
- **MySQL 等**：列定义内联 `COMMENT '注释'`，如 `` `CREATE_TIME` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ``。解析列名与 COMMENT 得到字段注释。

列名统一转为驼峰作为 page_spec 的 key（如 `CREATE_TIME` → `createTime`）。

## 3. 何时向用户要表结构 / 何时推荐字段

| 情况 | 行为 |
|------|------|
| 用户已提供完整表结构 | 解析表名、列名、类型、注释 → 与 prototype_spec 对齐：**优先 label 精确匹配**表结构 COMMENT，其次语义/简称匹配；未匹配到的标注推荐并请用户确认 → 生成驼峰 key，直接产出 page_spec |
| 用户未提供表结构 | 不产出完整 page_spec；产出**推荐字段列表**（命名 + 注释），请用户确认或补充表结构 |
| 表结构存在但缺少必要字段 | 列出缺失项（如缺 id、缺原型中必显列对应字段）→ 推荐补充字段命名与注释 → 请用户确认后合并再产出 page_spec |

**必要字段**：至少包含原型中 columns 与 searchFields 所需业务字段 + **通用字段**（见上文「通用字段」）；主键（如 id）必含。

## 4. 数据库字段 → 前端 key（驼峰）

- 规则：下划线命名 → 驼峰。如 `create_time` → `createTime`，`data_source_type` → `dataSourceType`。
- 表名 → entityNameEn：取表名或业务实体英文，帕斯卡。如 `data_connection` → `DataConnection`。
- 若用户表已用驼峰或接口已定名，以 design_constraints 或用户确认为准。

## 5. 推荐字段时的输出格式（clarificationNeeded）

当需要用户确认表结构或补充字段时，输出以下结构，**不**产出完整 page_spec：

```json
{
  "clarificationNeeded": true,
  "reason": "未提供表结构，或表结构无法满足原型所需字段",
  "recommendedEntity": {
    "tableName": "data_connection",
    "entityNameEn": "DataConnection",
    "comment": "数据连接"
  },
  "recommendedFields": [
    { "fieldKey": "id", "comment": "主键", "required": true, "category": "business" },
    { "fieldKey": "name", "comment": "数据连接名称", "required": true, "category": "business" },
    { "fieldKey": "type", "comment": "数据连接类型", "required": true, "category": "business" },
    { "fieldKey": "description", "comment": "描述", "required": false, "category": "business" },
    { "fieldKey": "createBy", "comment": "创建人", "required": false, "category": "common" },
    { "fieldKey": "createTime", "comment": "创建时间", "required": false, "category": "common" },
    { "fieldKey": "updateBy", "comment": "更新人", "required": false, "category": "common" },
    { "fieldKey": "updateTime", "comment": "更新时间", "required": false, "category": "common" },
    { "fieldKey": "remark", "comment": "备注", "required": false, "category": "common" }
  ],
  "questions": [
    { "question": "请确认上述表名与字段命名，或提供您的表结构", "missingType": "table_structure" }
  ]
}
```

用户确认或补充后，再次调用本技能时可产出完整 page_spec。推荐时业务字段与**通用字段**一并列出，可选 `category: "business" | "common"` 区分。

## 6. 与 CRUD 需求指令模板的对应（page_spec 与代码生成的衔接）

本技能只维护 **page_spec** 的数据；以下约定由**代码生成技能**按项目 CRUD 模板实现，本技能在产出 page_spec 时保持字段一致即可：

| 模板约定 | page_spec 对应 | 说明 |
|----------|----------------|------|
| 行唯一键 row-key | columns 中保留 `id` | 表格行唯一键，通常为 id |
| 筛选区字段 | searchFields（key/label/type/placeholder） | 与表格列可复用 key |
| 表格列顺序与类型 | columns（key/label/type/width/sortable） | 日期列可设 sortable: true 支持服务端排序 |
| 空值展示「-」 | 不写在 page_spec | 代码生成时统一空值显示 - |
| 日期格式 YYYY-MM-DD HH:mm | columns 中 type: date/datetime | 具体格式由代码生成实现 |
| 枚举/字典 | columns 中 type: enum，enumOptions | 字典码由后端/字典表定，value 与接口一致 |
| 操作列（详情/修改/删除等） | actions 数组 | 具体按钮与权限码由代码生成按模板与权限配置实现 |
| 分页 pageNum/pageSize/total | pagination | page_spec 中配置 pageSize、showSizeChanger、pageSizeOptions |
| 权限标识（如 da:qualityTask:add） | 不写在 page_spec | 由代码生成或配置层按模板写入 |

以上确保 page_spec 只承载「实体名、列定义、搜索项、操作类型、分页与表格配置」，细节行为由下游代码生成技能对齐 CRUD 需求指令模板。通用字段是否在列表/详情/表单展示按「通用字段」表建议处理，不必全部展示。

## 7. 完整示例：有表结构 → page_spec

**输入**  
- prototype_spec：entityLabel "数据连接"，columns 含「数据连接名称」「数据连接类型」「描述」「创建时间」等；searchFields 含名称、类型；actions 含 create、update、delete、view。  
- table_structure：表 `data_connection`，字段 `id`, `name`, `type`, `description`, `create_time`, `create_by`。

**产出 page_spec（片段）**

```json
{
  "entityName": "数据连接",
  "entityNameEn": "DataConnection",
  "columns": [
    { "key": "id", "label": "编号", "type": "string" },
    { "key": "name", "label": "数据连接名称", "type": "string" },
    { "key": "type", "label": "数据连接类型", "type": "enum", "enumOptions": [] },
    { "key": "description", "label": "描述", "type": "string" },
    { "key": "createBy", "label": "创建人", "type": "string" },
    { "key": "createTime", "label": "创建时间", "type": "datetime", "sortable": true }
  ],
  "searchFields": [
    { "key": "name", "label": "数据连接名称", "type": "string", "placeholder": "请输入名称/编码" },
    { "key": "type", "label": "数据连接类型", "type": "string", "placeholder": "请选择数据连接类型" }
  ],
  "actions": ["create", "update", "delete", "view"],
  "pagination": { "pageSize": 10, "showSizeChanger": true, "pageSizeOptions": [10, 20, 50, 100] },
  "tableConfig": { "stripe": true, "border": true, "showIndex": true, "showSelection": false }
}
```

枚举选项若需与后端一致，在 enumOptions 中填 value/label；若由字典接口提供，可留空由代码生成阶段接字典。
