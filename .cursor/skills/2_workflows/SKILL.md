---
name: CRUD 工作流
type: workflow
description: 当意图为“管理端 CRUD 前端代码生成”时执行；编排需求理解→原型设计→数据模型定义→生成前端代码四步，每步产出可暂存并支持人工编辑或对话中由 AI 修改后作为下一步输入。完整流程定义与执行约定见本目录 crud_flow.yaml 与 EXECUTION.md。
inputs:
  - name: user_query
    type: string
    description: 用户的原始需求描述（必填）
  - name: project_context
    type: string
    description: 项目/模块业务背景（可选，未提供时从 .cursor/rules 中 32-project-context 读取）
    required: false
  - name: tech_stack_constraints
    type: string
    description: 技术栈约束（可选）
    required: false
  - name: table_structure
    type: string
    description: 表结构 DDL，供数据模型定义步骤使用（可选）
    required: false
  - name: ui_scope
    type: string
    description: 原型形态约束，如 admin_crud（可选）
    required: false
  - name: design_constraints
    type: string
    description: 设计约束（可选）
    required: false
  - name: module_path_hint
    type: string
    description: 代码落盘的一级模块目录，如 da、dpp（可选）
    required: false
outputs:
  - requirement_spec
  - prototype_spec
  - page_spec
  - code_artifacts
  - api_module_path
  - files_written
---

# CRUD 工作流（crud_flow）

当 **意图路由**（`1_router`）输出 `intent === "ADMIN_CRUD"` 时使用本工作流。执行层将 `user_query` 及上述可选 inputs 注入流程，并按本目录下的 **crud_flow.yaml** 与 **EXECUTION.md** 执行。

## 何时使用

- 用户诉求为「管理端 CRUD 前端页面/代码生成」，且已由 `1_router` 识别为 `ADMIN_CRUD`。
- 执行前若未提供 `project_context` 或 `tech_stack_constraints`，应从 `.cursor/rules/30-project/32-project-context.mdc` 读取并注入。

## 流程步骤（四步）

| 步骤 | 名称 | 技能路径 | 产出暂存文件 |
|------|------|----------|--------------|
| 1 | step_requirements | 3_atomic/vue_generator/1_requirements_understanding | 01_requirement_spec.json |
| 2 | step_prototype | 3_atomic/vue_generator/2_prototype_design | 02_prototype_spec.json |
| 3 | step_data_model | 3_atomic/vue_generator/3_data_model_definition | 03_page_spec.json |
| 4 | step_code | 3_atomic/vue_generator/4_frontend_code_gen | 04_code_manifest.json |

暂存根路径：`.cursor/workflow_artifacts/crud_flow/`。每步执行前从该步的 `staging_input` 文件（即上一步的产出）读取并注入 inputs；执行后将产出写入 `staging_output` 对应文件。若产出为澄清（`clarificationNeeded === true`），则不写暂存，仅展示给用户并停顿。

## 下一步就绪（step_readiness）

执行下一步**前**必须按 **crud_flow.yaml** 的 `step_readiness` 检查上一步产出：

- **step_requirements → step_prototype**：若产出含 `clarificationNeeded === true`，不得进入下一步；向用户展示澄清原因与待补项，待用户补充后重跑 step_requirements 或确认后再执行 step_prototype。
- **step_prototype → step_data_model**：若 02_prototype_spec.json 不存在或缺少 entityLabel/columns，提示用户补全或编辑后继续。
- **step_data_model → step_code**：若产出含 `clarificationNeeded === true` 或 03_page_spec.json 缺少必要字段，提示用户提供表结构或确认推荐字段后再执行 step_code。

不满足条件时**不得自动进入下一步**，须按 yaml 中对应条目的 `action` 向用户说明并等待补充。

## 完整定义与执行约定

- **流程定义**：本目录 **crud_flow.yaml**（inputs、steps、staging、step_readiness、outputs、error_handling）。
- **执行约定**：本目录 **EXECUTION.md**（触发方式、暂存读写、澄清不写暂存、从中间步/重跑时注入用户新输入等）。

执行本工作流时，应始终以 crud_flow.yaml 与 EXECUTION.md 为准；本 SKILL 仅作加载到上下文用的概要说明。
