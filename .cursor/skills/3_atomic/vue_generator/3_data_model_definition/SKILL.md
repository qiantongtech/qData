---
name: data-model-definition
description: 根据原型规格与用户提供的表结构（或推荐字段）维护 page_spec，将数据库字段转为前端驼峰命名，供代码生成技能消费。在用户提供表结构时做字段映射；无表结构或字段缺失时推荐字段命名与注释并请用户确认。不做 UI 与接口实现。
inputs:
  - name: prototype_spec
    type: object
    description: 原型设计技能输出（entityLabel、columns 仅 label/type、searchFields、actions 等）
  - name: table_structure
    type: string
    description: 用户提供的表结构或数据库字段设计（可选）
    required: false
  - name: design_constraints
    type: string
    description: 命名规范、已有接口字段等约束（可选）
    required: false
outputs:
  - name: page_spec
    type: object
    description: 符合 schemas/page_spec.json，作为代码生成技能的输入
  - name: clarificationNeeded
    type: object
    description: 当缺少表结构或字段不满足原型时，输出推荐字段与待确认项（含 clarificationNeeded、reason、recommendedFields、questions），不产出完整 page_spec（可选）
    required: false
---

# 数据模型定义

将「原型设计」产出的 **prototype_spec** 与用户提供的**表结构**结合，维护并产出 **page_spec**。核心工作：**向用户要表结构（支持 CREATE TABLE + COMMENT 两种 DDL 写法）→ 将数据库字段转为前端驼峰 key → 自动带上通用字段（见 reference）→ 若缺失则推荐字段并请用户确认 → 产出符合 [schemas/page_spec.json](../../../schemas/page_spec.json) 的数据**，供下游「代码生成」技能使用。

**流程暂存（crud_flow）**：本步产出暂存于 `.cursor/workflow_artifacts/crud_flow/03_page_spec.json`。用户可在对话中要求修改该 spec（如「给列表加一列」「改某个 key」）；此时应从该路径读取 → 按用户意图增量修改 → 写回同一路径，再继续下一步，见 `skills/2_workflows/EXECUTION.md`。

**通用字段**：每表都有的字段（validFlag、delFlag、createBy、creatorId、createTime、updateBy、updaterId、updateTime、remark、createType）在解析或推荐时**自动带入**；部分为预留，不一定全部在列表/详情/表单展示，详见 [reference.md](reference.md)。

输出必须符合 [schemas/page_spec.json](../../../schemas/page_spec.json)。页面展示与交互细节参见 reference 与项目 CRUD 需求指令模板。

## 输入说明

| 输入 | 用途 | 示例 |
|------|------|------|
| prototype_spec | 必填。上游原型规格 | entityLabel、columns（仅 label/type）、searchFields、actions |
| table_structure | 可选。用户提供的表/库字段 | CREATE TABLE DDL（支持 Oracle 风格 COMMENT ON 或 MySQL 风格列后 COMMENT） |
| design_constraints | 可选。命名或接口约束 | 「接口已定 createTime」「统一驼峰」 |

## 输出说明（page_spec）

- **entityName**：实体中文名，与 prototype_spec.entityLabel 一致。
- **entityNameEn**：实体英文名（帕斯卡/驼峰），用于组件名与路由，需与用户确认或从表名/业务推断。
- **columns**：表格列/表单字段，每项必须含 **key**（英文驼峰）、**label**、**type**；可选 width、sortable、required、enumOptions（value 需与后端一致）。
- **searchFields**：筛选区字段，含 key、label、type、placeholder。
- **actions**：页面操作列表（create/update/delete/view/export/import）。
- **pagination / tableConfig**：按 prototype_spec 或默认填充。

**字段 key 规则**：来自表结构时，将数据库字段名转为**驼峰**（如 `create_time` → `createTime`）；无表结构时使用推荐命名并标注注释供用户确认。

**项目规则参考**：entityNameEn、字段 key、actions 的命名与项目约定一致时，下游代码生成更易对接。可参考：
- [.cursor/rules/00-core/01-style-format.mdc](../../../../../rules/00-core/01-style-format.mdc)：views/components/api 命名（小驼峰、大驼峰等）、路由 path 约定。
- [.cursor/rules/30-project/30-architecture.mdc](../../../../../rules/30-project/30-architecture.mdc)：目录与模块层级，实体/视图命名与之一致。
- [.cursor/rules/30-project/31-api-contract.mdc](../../../../../rules/30-project/31-api-contract.mdc)：接口资源名、RESTful 路径、权限标识格式（模块:功能:操作），便于后续接口与权限对接。
- [.cursor/rules/30-project/32-project-context.mdc](../../../../../rules/30-project/32-project-context.mdc)：项目背景与技术栈约束，便于与业务域、技术边界对齐。

## 核心流程

1. **优先使用用户表结构**  
   若提供 table_structure：解析 DDL（表名、列名、类型、注释；支持 `COMMENT ON COLUMN` 与列后 `COMMENT '...'` 两种写法）→ 与 prototype_spec 按 label/语义对齐 → **自动补全通用字段**（若表中未显式出现则按 reference 中的通用字段列表带入）→ 生成驼峰 key，产出 page_spec。

2. **无表结构或字段不满足原型时触发「推荐与确认」**  
   - 判定：缺少表结构，或表字段无法覆盖 prototype_spec 中列出的 label/能力（如缺主键、缺列表必显字段）。  
   - 行为：不产出完整 page_spec；产出 **clarificationNeeded** 结构，包含：  
     - 推荐表/字段命名（英文、驼峰）与中文注释；  
     - 说明哪些为满足原型所必需、哪些为建议；  
     - 请用户确认或补充表结构后再执行本技能。

3. **维护 page_spec 数据**  
   产出的 page_spec 必须符合 `page_spec.json`，且与 CRUD 模板约定对齐（见 reference.md）：行唯一键建议 `id`、空值展示「-」、日期格式等由代码生成技能按模板实现。

## 边界（必须遵守）

- **只做数据与命名定义**：不设计接口 URL、不写请求体示例、不生成任何代码。
- **不擅自发明字段**：key 与 label 要么来自用户表结构，要么来自推荐并经过「推荐与确认」流程由用户确认。
- **驼峰与一致性**：前端 key 统一驼峰；若 design_constraints 中已有接口字段名，与其保持一致。

## 验收自检

1. **完备性**：prototype_spec 中所有 columns、searchFields 在 page_spec 中有对应项，且均有 key。
2. **结构化**：输出符合 [page_spec.json](../../../schemas/page_spec.json)（required、type、枚举值）。
3. **命名**：key 为驼峰；entityNameEn 帕斯卡；与用户表结构或已确认推荐一致。
4. **缺失时**：未提供表结构或字段不足时，必须走推荐与确认流程，不强行产出完整 page_spec。

## 示例

**输入**：prototype_spec（entityLabel: "数据连接", columns: [{ label: "数据连接名称", type: "string" }, ...]），table_structure: "表 data_connection，字段 id, name, type, description, create_time, create_by"。

**输出片段（page_spec）**：

```json
{
  "entityName": "数据连接",
  "entityNameEn": "DataConnection",
  "columns": [
    { "key": "id", "label": "编号", "type": "string" },
    { "key": "name", "label": "数据连接名称", "type": "string" },
    { "key": "type", "label": "数据连接类型", "type": "enum", "enumOptions": [] },
    { "key": "createTime", "label": "创建时间", "type": "datetime", "sortable": true }
  ],
  "searchFields": [
    { "key": "name", "label": "数据连接名称", "type": "string", "placeholder": "请输入名称" }
  ],
  "actions": ["create", "update", "delete", "view"]
}
```

**无表结构时**：产出 clarificationNeeded 结构，列出推荐字段（业务字段 + **通用字段**，见 reference）及注释，请用户确认后再产出 page_spec。格式见 [reference.md](reference.md)。
