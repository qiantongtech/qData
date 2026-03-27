# 需求理解

## 澄清机制

### 何时触发

满足任一时触发澄清，不产出完整 requirement_spec：

- 用户描述过于笼统，无法拆出至少一条明确功能或交互意图。
- 关键业务对象或操作主体不明（如谁可删、谁可审）。
- 与技术栈强相关的实现方式缺失且影响功能边界（如是否需后端接口、是否仅前端 Mock）。

### 澄清输出结构

触发澄清时，输出以下结构，不输出 featureList/businessRules/interactionIntents 的完整内容：

```json
{
  "clarificationNeeded": true,
  "reason": "简要说明为何无法继续（如：描述过于笼统）",
  "questions": [
    {
      "question": "要管理的对象是什么？例如：员工、订单、项目",
      "missingType": "business_object",
      "suggestion": "请补充实体名称与主要字段"
    },
    {
      "question": "列表页需要哪些操作？例如：仅查询、还是增删改查、导出",
      "missingType": "interaction_scope",
      "suggestion": "请列举期望的页面操作"
    }
  ]
}
```

| 字段 | 说明 |
|------|------|
| clarificationNeeded | 固定为 true |
| reason | 为何需要澄清 |
| questions[].question | 向用户提出的问题 |
| questions[].missingType | 缺失信息类型：business_object / interaction_scope / permission / tech_boundary / other |
| questions[].suggestion | 建议用户如何补全 |

---

## 完整示例 1：正常输出

**输入**

- project_context：人事管理模块，已有组织、部门。
- user_description：做一个项目列表，能按项目名称和状态查，表格显示项目名、负责人、开始结束日期，能新增和编辑，只有管理员能删除项目。
- tech_stack_constraints：Vue3、Element Plus、REST 接口。

**输出（符合 output_schema.json）**

```json
{
  "featureList": [
    { "id": "F01", "name": "项目列表展示", "description": "表格展示项目名称、负责人、开始日期、结束日期", "priority": "high" },
    { "id": "F02", "name": "按条件查询", "description": "按项目名称、状态筛选", "priority": "high" },
    { "id": "F03", "name": "新增项目", "description": "表单新增项目", "priority": "high" },
    { "id": "F04", "name": "编辑项目", "description": "表单编辑项目信息", "priority": "high" },
    { "id": "F05", "name": "删除项目", "description": "删除项目，仅管理员可操作", "priority": "medium" }
  ],
  "businessRules": [
    { "description": "仅管理员可删除项目", "appliesTo": "F05", "constraintType": "permission" }
  ],
  "interactionIntents": [
    { "type": "query", "description": "按项目名称、状态查询", "relatedFields": ["项目名称", "状态"] },
    { "type": "create", "description": "新增项目" },
    { "type": "update", "description": "编辑项目" },
    { "type": "delete", "description": "删除项目，需管理员权限" }
  ]
}
```

---

## 完整示例 2：触发澄清

**输入**

- project_context：（空）
- user_description：做个列表，要能搜。
- tech_stack_constraints：Vue3。

**输出（澄清，非 requirement_spec）**

```json
{
  "clarificationNeeded": true,
  "reason": "描述过于笼统，无法确定管理对象与具体功能范围",
  "questions": [
    {
      "question": "「列表」管理的业务对象是什么？例如：员工、订单、项目",
      "missingType": "business_object",
      "suggestion": "请补充实体名称"
    },
    {
      "question": "「能搜」具体指按哪些字段筛选？",
      "missingType": "interaction_scope",
      "suggestion": "请列出需要支持的搜索/筛选字段"
    },
    {
      "question": "除搜索外是否需要新增、编辑、删除等操作？",
      "missingType": "interaction_scope",
      "suggestion": "请说明列表页需要的操作类型"
    }
  ]
}
```

下游技能可根据 `clarificationNeeded === true` 识别为澄清结果，先引导用户补全再重新调用本技能。
