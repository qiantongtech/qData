# 原型设计

## 通用字段约定

项目内表结构通常包含一组**通用字段**（每表都有，部分为预留）。本技能只设计**业务字段**的归属（列表/搜索/表单）；通用字段由下一技能「数据模型定义」自动带入，**不必在 prototype_spec 的 columns 中逐项列出**。

| 通用字段（中文） | 常见用途 | 是否通常在列表/详情/表单展示 |
|------------------|----------|------------------------------|
| 是否有效 (validFlag) | 逻辑有效标识 | 预留，多数不展示或仅后台用 |
| 删除标志 (delFlag) | 逻辑删除 | 预留，一般不展示 |
| 创建人 (createBy) | 审计 | 列表/详情常展示 |
| 创建人ID (creatorId) | 关联 | 预留，多数不展示 |
| 创建时间 (createTime) | 审计 | 列表/详情常展示 |
| 更新人 (updateBy) | 审计 | 详情可展示 |
| 更新人ID (updaterId) | 关联 | 预留，多数不展示 |
| 更新时间 (updateTime) | 审计 | 详情常展示 |
| 备注 (remark) | 说明 | 表单/详情可展示 |
| 创建类型 (createType) | 业务区分 | 预留或详情展示 |

**结论**：原型设计时 columns 只列**业务字段**；通用字段由数据模型定义阶段自动带上，且不一定全部在页面上展示（由代码生成按约定处理）。

## 与下一技能的分工

| 内容 | 原型设计（本技能） | 数据模型定义（下一技能） |
|------|---------------------|-------------------------------|
| 实体中文名 | 产出 entityLabel | 产出 entityNameEn、API 资源名、表名等 |
| 字段 | 产出 label、type、required（无 key） | 产出 key、与表/API 对齐的命名 |
| 操作 | 产出 actions 类型列表 | 产出权限标识（如 att:project:remove） |
| 接口 | 不产出 | 产出 API（mock）、请求/响应结构 |

本技能输出 **prototype_spec**，下一技能消费后产出 **page_spec**（含 key、entityNameEn、权限等）与 **API 定义（mock）**。

## requirement_spec → prototype_spec 映射规则

| requirement_spec | prototype_spec | 说明 |
|------------------|----------------|------|
| featureList 中「列表展示」+ 涉及字段 | entityLabel、columns（仅 label、type、required） | 不产出 key、entityNameEn |
| interactionIntents.type = query + relatedFields | searchFields（仅 label、type、placeholder） | 不产出 key |
| interactionIntents：create/update/delete/... | actions | 不产出权限标识 |
| businessRules constraintType = required | columns[].required = true | 仅语义 |
| businessRules constraintType = permission | actionPermissions（action + description） | 不产出权限码 |

- **禁止在本阶段出现**：entityNameEn、columns[].key、searchFields[].key、任何权限标识字符串（如 `att:xxx:remove`）。

### 处理上游 AI 增强建议

requirement_spec 中 `source: "ai_suggested"` 的条目：
- **用户已确认采纳的**：按正常条目映射到 prototype_spec（不带 aiSuggested 标记，视为已确认需求）。
- **用户未确认或拒绝的**：不纳入 prototype_spec。
- **本技能自身的增强建议**：以 `aiSuggested: true` 标注，展示给用户确认后才纳入正式 prototype_spec。

## 完整示例：requirement_spec → prototype_spec

**输入 requirement_spec**：

```json
{
  "featureList": [
    { "id": "F01", "name": "员工列表展示", "description": "表格展示员工姓名、工号、部门", "priority": "high" },
    { "id": "F02", "name": "按条件查询", "description": "按姓名、部门筛选", "priority": "high" },
    { "id": "F03", "name": "新增员工", "description": "表单新增员工", "priority": "high" },
    { "id": "F04", "name": "编辑员工", "description": "表单编辑员工信息", "priority": "high" },
    { "id": "F05", "name": "删除员工", "description": "删除员工记录，仅管理员可操作", "priority": "medium" }
  ],
  "businessRules": [
    { "description": "仅管理员可执行删除", "appliesTo": "F05", "constraintType": "permission" }
  ],
  "interactionIntents": [
    { "type": "query", "description": "按姓名、部门查询", "relatedFields": ["姓名", "部门"] },
    { "type": "create", "description": "新增员工" },
    { "type": "update", "description": "编辑员工" },
    { "type": "delete", "description": "删除员工，需管理员权限" }
  ]
}
```

**输出 prototype_spec**（无 key、无 entityNameEn、无权限码）：

```json
{
  "entityLabel": "员工",
  "columns": [
    { "label": "姓名", "type": "string", "required": true },
    { "label": "工号", "type": "string" },
    { "label": "部门", "type": "string" }
  ],
  "searchFields": [
    { "label": "姓名", "type": "string", "placeholder": "请输入姓名" },
    { "label": "部门", "type": "string", "placeholder": "请输入部门" }
  ],
  "actions": ["create", "update", "delete", "view"],
  "actionPermissions": [
    { "action": "delete", "description": "仅管理员可删" }
  ],
  "pagination": {
    "pageSize": 10,
    "showSizeChanger": true,
    "pageSizeOptions": [10, 20, 50, 100]
  },
  "tableConfig": {
    "stripe": true,
    "border": true,
    "showIndex": true,
    "showSelection": false
  }
}
```

说明：实体英文名、字段 key、权限标识（如 `att:employee:remove`）由下一技能「数据模型定义」在产出 page_spec 与 API（mock）时确定。
