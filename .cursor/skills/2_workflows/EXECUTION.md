# crud_flow 执行约定（含 Agent 使用方式）

执行 crud_flow 时，须遵守以下约定；若由 Cursor Agent 执行，应在规则或技能中体现。

## 触发方式（意图路由 → 流程）

- **先调用意图路由**：使用 `skills/1_router`，输入 `user_query`，得到 `intent`（如 `ADMIN_CRUD`）、`confidence`、`reasoning`。
- **再启动流程**：当 `intent === "ADMIN_CRUD"` 时，启动 crud_flow，并将 `user_query` 及可选参数（`project_context`、`tech_stack_constraints`、`table_structure`、`ui_scope`、`design_constraints`、`module_path_hint`）注入流程 inputs。其余意图（如 `UNKNOWN`）不启动本流程，由执行层另行处理。
- **默认项目背景与技术栈**：若启动 crud_flow 时**未提供** `project_context` 或 `tech_stack_constraints`，执行层**应从** [.cursor/rules/30-project/32-project-context.mdc](../../rules/30-project/32-project-context.mdc) **读取**对应小节并注入 flow 的 `project_context`、`tech_stack_constraints`，以便 step_requirements 获得默认项目背景与技术栈。

## 暂存（staging）

- **每步结束后**：将该步 outputs 对应变量序列化为 JSON，写入 `staging.base_path` 下该步 `staging_output` 指定文件名（如 `01_requirement_spec.json`）。
- **澄清产出不写正式暂存**：若某步产出根级为 `clarificationNeeded === true`，则不写入该步的 `staging_output` 文件，避免下一步误读；仅将澄清内容展示给用户，待用户补充或确认后重跑该步再写入正式产出。
- **下一步开始前**：若该步配置了 `staging_input`，且对应文件存在，则从该文件读取并反序列化，注入本步 inputs，覆盖同名字段（支持人工微调或对话中由 AI 修改后的内容）。
- **04_code_manifest.json 的 key 映射**：step_code 产出变量名为 `code_artifacts`、`api_module_path`、`files_written`（snake_case）；写入 `04_code_manifest.json` 时须映射为 schema 要求的 `codeArtifacts`、`apiModulePath`、`filesWritten`（camelCase）。

## 暂存文件的两种修改方式（均支持）

每步产出的 JSON 均可通过以下两种方式修改，下一步一律从暂存路径读取，不区分修改来源：

1. **手动编辑**：用户直接在编辑器中打开并修改 `.cursor/workflow_artifacts/crud_flow/` 下对应文件（01～03），保存后继续下一步即可。
2. **对话式由 AI 修改**：当用户在对话中要求修改已产出的 spec（如「在需求里加一条导出」「把 02 的搜索项改成只有 2 个」「给 page_spec 加一列」）时，执行层/Agent 应：
   - **从暂存路径读取**当前内容：`staging.base_path` + 该步对应的 `staging_output` 文件名（见 crud_flow.yaml 的 `staging.artifact_files`），即 `01_requirement_spec.json` / `02_prototype_spec.json` / `03_page_spec.json`；
   - **按用户意图做增量修改**（在现有结构上增删改，保持符合对应 schema）；
   - **写回同一路径**，并告知用户已更新，可继续下一步。
   - **优先采用「读→改→写回」**，而非「重跑该步」：这样可保留用户此前的手动修改；仅当用户明确要求「重新跑需求理解」「从这一步重新生成」时，才重新执行该步并覆盖暂存。

## 下一步就绪（step_readiness）

- 执行下一步**前**，按 `crud_flow.yaml` 中 `step_readiness` 检查上一步产出是否满足条件。
- **若不满足**（如产出含 `clarificationNeeded === true`，或暂存缺失/不符合 schema）：**不得自动进入下一步**；须停顿并向用户展示该条目的 `action` 中的提示，待用户补充或编辑暂存后，再由用户触发继续或重新执行上一步。

## 澄清产出的校验

- 若上一步产出根级存在 `clarificationNeeded === true`，视为澄清/推荐结果，**不要**用完整 requirement_spec / page_spec 的 schema 校验；可选用对应技能的 clarification schema 校验：需求理解见 `1_requirements_understanding/output_clarification_schema.json`，数据模型定义见 `3_data_model_definition/output_clarification_schema.json`。校验通过后仍按 step_readiness 做停顿与提示。

## 从中间步开始或重跑某步

- 除从 staging 读取上一步产出外，**须把用户新提供的输入显式注入当前步**：例如用户补充 `table_structure` 后重跑 step_data_model，执行层须将新的 `table_structure` 注入 flow 的 inputs 并传入该步；同理 `module_path_hint` 等。否则会沿用旧值或空，导致结果不符合用户预期。
