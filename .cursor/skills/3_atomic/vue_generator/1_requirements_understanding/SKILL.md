---
name: requirements-understanding
description: 从业务背景、用户描述、技术栈约束中提取功能清单、业务规则与交互意图；信息严重缺失时触发澄清。用于需求分析阶段或流程第一步，不做 UI 与代码实现。在用户进行需求理解、功能清单梳理、业务规则提取、交互意图分析、或需根据技术栈约束做可行性判断时使用。
inputs:
  - name: project_context
    type: string
    description: 当前项目的业务背景
  - name: user_description
    type: string
    description: 用户原始描述
  - name: tech_stack_constraints
    type: string
    description: 技术栈约束
outputs:
  - name: requirement_spec
    type: object
    description: 符合 output_schema.json 的结构（featureList、businessRules、interactionIntents）
---

# 需求理解

从业务背景、用户原始描述与技术栈约束中，产出结构化的功能清单、业务逻辑规则与交互意图。输出必须符合 [output_schema.json](output_schema.json)，供下游「原型设计」等技能消费。

**流程暂存（crud_flow）**：本步产出暂存于 `.cursor/workflow_artifacts/crud_flow/01_requirement_spec.json`。用户可在对话中要求修改该 spec（如「在需求里加一条导出」）；此时应从该路径读取 → 按用户意图增量修改 → 写回同一路径，再继续下一步，见 `skills/2_workflows/EXECUTION.md`。

## 输入说明

| 输入 | 用途 | 示例 |
|------|------|------|
| project_context | 项目/模块的业务域、已有能力 | 「人事管理模块，已有组织与员工」 |
| user_description | 用户对本次需求的自然语言描述 | 「做一个项目列表，能查、能增删改，只有管理员能删」 |
| tech_stack_constraints | 技术选型与限制 | 「Vue3 + Element Plus，仅前端」 |

### 输入来源（默认值）

当用户或流程**未显式提供** `project_context`、`tech_stack_constraints` 时，执行本技能前**应从** [.cursor/rules/30-project/32-project-context.mdc](../../../../rules/30-project/32-project-context.mdc) **读取**「项目背景」「技术栈约束」两节内容，分别作为 `project_context`、`tech_stack_constraints` 的默认输入。若用户或流程显式提供了其中某一项，则以用户/流程提供的为准。

## 输出说明

- **功能清单 (featureList)**：将用户描述拆解为可执行子功能，每项含 id、name、description，可选 priority。
- **业务逻辑规则 (businessRules)**：核心约束，如权限（仅管理员可删）、必填项、状态规则等；含 constraintType（permission/required/state/validation/other）与 appliesTo。用户描述中凡涉及权限、必填、状态、校验的，均须在 businessRules 中体现，不得因字段可选而省略。
- **交互意图 (interactionIntents)**：用户在页面的操作意图，如 query、export、create、update、delete、view、associate；可带 relatedFields。

输出必须符合本目录下 `output_schema.json`，不得包含 UI 布局、组件位置等（由原型设计负责）。

## 边界（必须遵守）

- **不做 UI 设计**：只明确「需要什么字段/能力」，不决定「字段放左边还是右边」。
- **不做代码实现**：本阶段禁止输出任何具体代码。
- **不处理非功能性需求**：性能、高可用等 MVP 外内容本阶段不涉及。
- **信息补全策略**：当输入信息严重缺失时，必须触发「澄清机制」，列出待澄清项，不得猜测补全。

## 澄清机制

当出现以下情况之一时，判定为**严重缺失**，必须触发澄清而非产出完整 requirement_spec：

- 用户描述过于笼统，无法拆出至少一条明确功能或交互意图；
- 关键业务对象或操作主体不明（如「谁可以删」未说明）；
- 与技术栈强相关的实现方式缺失且影响功能边界（如是否需服务端接口未说明）。

**澄清时输出形式**：输出结构化澄清问题列表，暂不产出完整 featureList/businessRules/interactionIntents。格式见 [reference.md](reference.md)。

## 验收自检

在交付前自检：

1. **完备性**：用户描述中的每一个动词（动作）和名词（对象）是否都在输出中有对应。
2. **结构化**：输出是否符合 `output_schema.json`（类型、必填项、枚举值）。
3. **无歧义性**：逻辑描述中无「大概」「可能」「类似」等模糊词汇。
4. **技术可行性**：提取出的功能是否在给定的技术栈约束范围内。

## 示例

**输入**：  
user_description：「做一个员工列表页，支持按姓名和部门查询，表格里要有姓名、工号、部门，能新增和编辑，删除仅管理员。」

**输出片段（符合 Schema）**：

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

**信息缺失时**：若 user_description 仅为「做个列表」，则触发澄清，输出澄清问题列表（见 reference.md），不输出上述完整 requirement_spec。
