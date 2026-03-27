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
- **信息补全策略**：当输入信息**严重缺失**时，必须触发「澄清机制」；当信息**基本充分**时，按「AI 审查与按需建议」机制处理（见下文）。

## AI 审查与按需建议（保底 + 审查 + 按需建议 + 确认）

当用户描述的信息**基本充分**（能拆出至少一条明确功能且业务对象明确）时，AI **必须**完整提取用户需求，并**审查**需求的完备性与合理性。只有在发现实际问题时才给出建议，**不得为了建议而建议**。

### 核心原则

1. **保底**：用户明确描述的功能、字段、规则 **100% 保留**，不得删减、替换或降级——这是铁律。
2. **审查**：AI **必须**从以下三个维度审视用户需求，判断是否存在需要建议的问题：
   - **完备性**：需求描述是否存在明显遗漏（如缺少关键环节、上下游未闭环）；
   - **合理性**：业务规则是否存在逻辑矛盾或明显不合理之处；
   - **行业对标**：同类管理端标准产品普遍具备、但用户未提及的重要功能。
3. **按需建议**：仅当审查发现上述问题时，才给出建议项，标记 `"source": "ai_suggested"` 并附 `"reason"` 说明具体问题或价值。若审查后认为需求已完善，**可以没有任何建议项**。
4. **确认**：无论是否有建议项，都必须向用户展示提取结果并等待确认后才写入暂存。

### 审查底线（不得违反）

- 建议项须是**确有实际价值**的补充或纠正，不得建议无意义、过度复杂或降低体验的内容。
- 建议数量**不做硬性要求**——可以是 0 条（需求已完善），也可以是多条（问题较多），取决于审查结果。
- AI 建议项**绝不能替代**用户明确描述的内容；若 AI 认为用户的某个描述可优化，应以「额外建议」形式提出，同时保留用户原始描述。

### 输出中的标注方式

- 用户明确描述的功能/规则/意图：`"source": "user"`（或省略 source，默认为 user）。
- AI 建议的补充/纠正项：`"source": "ai_suggested"`，附 `"reason": "说明发现的问题或补充价值"`。

### 展示给用户的格式

输出 requirement_spec 后，**必须**向用户展示并等待确认：

**有建议项时**，分两部分展示：
1. **用户需求**（source = user）：「以下是根据您的描述提取的需求：」
2. **AI 审查建议**（source = ai_suggested）：「审查后发现以下可改进之处，供您参考，采纳后我会纳入正式需求：」

**无建议项时**，直接展示：
> 「以下是根据您的描述提取的需求，经审查未发现明显遗漏或问题，请确认。」

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
5. **建议项标注**：若存在 AI 建议项，均已标注 `source: "ai_suggested"` 与 `reason`，且未替换或削弱用户原始需求中的任何内容。
6. **建议项质量**：建议项仅在审查发现实际问题（不完善、逻辑错误、行业标配缺失）时给出，数量不做硬性要求；无问题时可以没有建议项。

## 示例

**输入**：  
user_description：「做一个员工列表页，支持按姓名和部门查询，表格里要有姓名、工号、部门，能新增和编辑，删除仅管理员。」

**输出片段（符合 Schema，审查后有建议）**：

```json
{
  "featureList": [
    { "id": "F01", "name": "员工列表展示", "description": "表格展示员工姓名、工号、部门", "priority": "high" },
    { "id": "F02", "name": "按条件查询", "description": "按姓名、部门筛选", "priority": "high" },
    { "id": "F03", "name": "新增员工", "description": "表单新增员工", "priority": "high" },
    { "id": "F04", "name": "编辑员工", "description": "表单编辑员工信息", "priority": "high" },
    { "id": "F05", "name": "删除员工", "description": "删除员工记录，仅管理员可操作", "priority": "medium" },
    { "id": "F06", "name": "查看员工详情", "description": "查看员工完整信息（只读）", "priority": "medium", "source": "ai_suggested", "reason": "管理端常见操作，便于快速查看完整信息而不触发编辑" },
    { "id": "F07", "name": "导出员工列表", "description": "将当前筛选结果导出为 Excel", "priority": "low", "source": "ai_suggested", "reason": "员工数据常有导出需求，便于线下汇总或交接" }
  ],
  "businessRules": [
    { "description": "仅管理员可执行删除", "appliesTo": "F05", "constraintType": "permission" }
  ],
  "interactionIntents": [
    { "type": "query", "description": "按姓名、部门查询", "relatedFields": ["姓名", "部门"] },
    { "type": "create", "description": "新增员工" },
    { "type": "update", "description": "编辑员工" },
    { "type": "delete", "description": "删除员工，需管理员权限" },
    { "type": "view", "description": "查看员工详情", "source": "ai_suggested", "reason": "管理端标配，与编辑分离更安全" },
    { "type": "export", "description": "导出员工列表", "source": "ai_suggested", "reason": "员工数据常有导出需求" }
  ]
}
```

**展示给用户时（有建议）**：

> **用户需求（已提取）**：F01~F05，含按姓名/部门查询、增删改，删除仅管理员。
>
> **AI 审查建议（供参考，采纳后纳入正式需求）**：
> - F06 查看员工详情：管理端标配操作，当前仅有编辑入口，缺少只读查看，易误触修改
> - F07 导出员工列表：员工数据常有导出需求，便于线下汇总或交接
>
> 请确认以上需求，以及是否采纳 AI 建议项。

---

**示例 2（审查后无建议）**

**输入**：  
user_description：「做一个员工列表页，支持按姓名和部门查询，表格里要有姓名、工号、部门、入职日期，能新增、编辑、查看详情、导出，删除仅管理员。」

**展示给用户时（无建议）**：

> **用户需求（已提取）**：F01~F07，含按姓名/部门查询、列表展示、增删改查、查看详情、导出，删除仅管理员。
>
> 经审查未发现明显遗漏或问题，请确认。

---

**信息缺失时**：若 user_description 仅为「做个列表」，则触发澄清，输出澄清问题列表（见 reference.md），不输出上述完整 requirement_spec。
