---
name: prototype-design
description: 将需求理解产出的 requirement_spec 转为原型规格 prototype_spec，仅决定字段归属（列表列/搜索/表单）、操作类型与分页等页面结构。不产出与后端对齐的对象名、字段 key、权限标识，这些由下一技能「数据模型定义」确定。不写接口与代码，不做高保真视觉稿。
inputs:
  - name: requirement_spec
    type: object
    description: 需求理解技能输出（featureList、businessRules、interactionIntents）
  - name: ui_scope
    type: string
    description: 约束原型形态，如 admin_crud（可选）
    required: false
  - name: design_constraints
    type: string
    description: 简要设计约束（可选）
    required: false
outputs:
  - name: prototype_spec
    type: object
    description: 符合 schemas/prototype_spec.json，供「数据模型定义」消费并产出 page_spec 与 API（mock）
---

# 原型设计

将「需求理解」产出的 **requirement_spec** 转为 **prototype_spec**（原型规格）。只做页面级结构设计：哪些字段在列表/搜索/表单、有哪些操作类型、是否分页等；**不敲定与后端对齐的命名**。

**流程暂存（crud_flow）**：本步产出暂存于 `.cursor/workflow_artifacts/crud_flow/02_prototype_spec.json`。用户可在对话中要求修改该 spec（如「把搜索项改成只有 2 个」）；此时应从该路径读取 → 按用户意图增量修改 → 写回同一路径，再继续下一步，见 `skills/2_workflows/EXECUTION.md`。

**与下一技能的分工**：对象名称（英文/资源名）、字段 key、权限标识等，均由下一技能「数据模型定义（每个表的字段、产出一份 API/mock）」在产出接口与数据定义时确定。本技能仅产出「语义与结构」，便于数据模型定义技能据此对齐后端。

输出必须符合 [schemas/prototype_spec.json](../../../schemas/prototype_spec.json)。

## 输入说明

| 输入 | 用途 | 示例 |
|------|------|------|
| requirement_spec | 必填。需求理解输出 | 见需求理解技能输出 |
| ui_scope | 可选。收敛原型形态 | `admin_crud`、`form_only` |
| design_constraints | 可选。设计约束 | 「搜索不超过 4 个字段」 |

## 输出说明（prototype_spec）

产出 **prototype_spec**，仅含与后端无关的页面语义与结构：

- **entityLabel**：实体中文名称（如「员工」）。不产出 entityNameEn、API 资源名——由数据模型定义确定。
- **columns**：列/表单字段，仅 **label**（中文）、**type**、**required** 等；**不产出 key**。只列出**业务字段**（需求中要在列表/表单/详情展示的）；**通用字段**（创建人、创建时间、更新时间、备注等）由下游数据模型定义技能自动带入，本阶段不必逐项列出，详见 [reference.md](reference.md) 的「通用字段约定」。
- **searchFields**：参与搜索的字段，仅 **label**、**type**、placeholder；**不产出 key**。
- **actions**：操作类型（create/update/delete/view/export/import）。**不产出权限标识**（如 `att:project:remove` 由数据模型定义确定）。
- **actionPermissions**：仅描述「哪些操作有权限约束」及说明（如「仅管理员可删」），不产出具体权限码。
- **pagination / tableConfig**：按需设置。

映射细节与示例见 [reference.md](reference.md)。

**项目规则参考**：本技能不产出英文名与权限码，但若需与项目菜单/页面层级或操作类型约定对齐（如 actions 与数据模型定义中的 list/add/edit/remove 等一致），可参考 [.cursor/rules/30-project/31-api-contract.mdc](../../../../../rules/30-project/31-api-contract.mdc) 中的操作类型与权限标识格式，便于下游数据模型定义与代码生成对接。如需与项目业务域、技术边界对齐（如模块划分、操作类型与权限风格），可参考 [.cursor/rules/30-project/32-project-context.mdc](../../../../../rules/30-project/32-project-context.mdc) 中的项目背景与技术栈约束。

## 边界（必须遵守）

- **只做页面级结构设计**：决定字段归属、操作类型、分页等；不做像素级布局、不指定组件 API。
- **不产出与后端对齐的命名**：不产出对象英文名、字段 key、权限标识、接口路径、表名；上述均由「数据模型定义」技能产出。
- **基于 requirement_spec 并可建议 UX 增强**（见下文「AI 增强建议」节）：requirement_spec 中已有的功能/字段**必须完整体现**，不得遗漏或削弱；AI 可额外建议 UX 增强项，但须标注并由用户确认。
- **不写接口与代码**：不定义接口 URL、请求体、后端表结构，不生成任何代码。
- **通用字段**（见 reference）视为表级约定，不必在 requirement_spec 中显式写出，下游会自动带上。

## AI 增强建议（保底 + 增强 + 确认）

在将 requirement_spec 转为 prototype_spec 时，AI 可基于 UX 最佳实践，**在完整保留用户需求的基础上**，建议页面结构层面的增强。

### 核心原则

1. **保底**：requirement_spec 中所有 featureList、interactionIntents、businessRules **必须完整映射**到 prototype_spec，不得遗漏或降级。
2. **增强**：AI 可建议 UX 增强项，如：
   - 合理的列排序（sortable）建议
   - showSelection（勾选列）以支持批量操作
   - 搜索区的展开/折叠建议
   - 合理的分页默认值调整
   - 表格配置优化（如 showOverflowTooltip）
   - 其他同类管理端页面常见的 UX 改进
3. **标注**：增强项须以 `"aiSuggested": true` 标注，与用户需求项区分。
4. **确认**：增强建议在展示给用户时**单独说明**，用户确认后才纳入正式 prototype_spec 传给下游；用户拒绝的建议项须移除。

### 增强底线

- 建议项须是**管理端 UX 最佳实践**，不得建议无意义或过度复杂的设计。
- 增强数量**适度**，不要为了建议而建议。
- **绝不能替代**用户原始需求中的任何内容。

### 处理上游 AI 建议

requirement_spec 中 `source: "ai_suggested"` 的条目，若用户已确认采纳，则按正常条目处理；若用户未确认，则不纳入 prototype_spec。

## 验收自检

1. **与 requirement_spec 一致**：featureList、interactionIntents 在 prototype_spec 中有对应体现（列/搜索/操作类型）。
2. **businessRules 仅落语义**：必填 → columns 对应项 `required: true`；权限类 → 仅用 actionPermissions 描述，不产出权限码。
3. **结构化**：输出符合 [prototype_spec.json](../../../schemas/prototype_spec.json)。
4. **无 key/entityNameEn/权限码**：输出中不出现字段 key、实体英文名、权限标识字符串。
5. **增强项标注**：AI 建议的增强项均已标注 `aiSuggested: true`，且未替换或削弱用户原始需求中的任何内容。

## 示例

**输入**：requirement_spec（员工列表，含查询/增删改、仅管理员可删）。

**输出片段（prototype_spec，含 AI 增强建议，无 key/英文名/权限码）**：

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
  "tableConfig": {
    "stripe": true,
    "border": true,
    "showIndex": true,
    "showSelection": true,
    "showSelection_aiSuggested": true,
    "showSelection_reason": "支持批量勾选，便于后续批量删除等操作"
  }
}
```

**展示给用户时**：

> **基于您的需求生成的原型规格**：列表展示姓名/工号/部门，搜索按姓名/部门，操作含增删改查，删除仅管理员。
>
> **AI 增强建议（供参考）**：
> - 表格开启勾选列（showSelection），便于后续批量操作
>
> 请确认原型规格，以及是否采纳 AI 建议项。

完整示例与 requirement_spec → prototype_spec 映射见 [reference.md](reference.md)。
