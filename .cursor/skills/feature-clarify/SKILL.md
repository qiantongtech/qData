---
name: feature-clarify
description: Use when turning a one-sentence feature or backlog item into a clarified question list and lightweight specification before any implementation work.
---

# /feature-clarify Skill

## Overview

This skill guides the agent to take an initial feature request or backlog item and turn it into:

- A **reviewable clarification question list** (with explicit options and selected answers)
- A **lightweight functional specification draft** (no implementation details, no code)

It is designed to work together with the project command definition in `.cursor/commands/01-feature-clarify.md`.

## When to Use

Use this skill when:

- The user invokes the `/feature-clarify` command (or equivalent) with:
  - One-sentence requirement
  - Business background
  - Current state / pain points
  - Constraints (time / tech / compliance / compatibility)
  - Related systems / modules
  - Target users / roles (if known)
  - Success criteria / acceptance criteria (if known)
  - Priority / urgency
- You need to **clarify the requirement before any design/implementation work**.
- You want a **structured, review-ready document** that can later be used to generate PRD, designs, or implementation plans.

If the user is already asking for detailed implementation steps or code, redirect back to clarification first using this skill.

## Input Expectations

From the user (via command or chat):

- One-sentence requirement
- Business background
- Current state / pain points
- Constraints (time / technology / compliance / compatibility)
- Related systems / modules (if any)
- Target users / roles (if known)
- Success criteria / acceptance criteria (if known)
- Priority / urgency (P0 / P1 / P2 or similar)

If any of these are missing, do **not** block; proceed with what you have and highlight missing items in the clarification output.

Additionally, ask (if not provided):

- A **feature identifier** like `FEAT-123` (or agree on a placeholder such as `FEAT-xxx`) to determine the target documentation path:
  - `docs/features/FEAT-xxx/01-clarification.md`

## High-Level Workflow

Follow this workflow whenever this skill is used:

1. **Parse and summarize inputs**
   - Restate the one-sentence requirement.
   - Summarize background, pain points, and constraints in your own words.

2. **Collect structured answers via interactive questions**
   - Use the AskQuestion tool (if available) to present **batched multiple-choice questions**.
   - Prefer 3–7 questions per batch to avoid overwhelming the user.
   - For each critical question:
     - Mark it as **【单选】** or **【多选】**.
     - Provide 2–4 options labeled A/B/C/D.
     - Reserve option D / last option as “自定义：xxxx” when appropriate.

3. **Iterate through 2–3 batches of questions**
   - **Batch 1 – Scope & boundaries**
   - **Batch 2 – Users, roles & permissions**
   - **Batch 3 – Flows, edge cases & non-functionals**
   - After each batch:
     - Merge user selections into an internal “answers state”.
     - Decide whether more batches are needed based on remaining uncertainties.

4. **Synthesize outputs into a structured clarification document**
   - Section 1: Clarification question list
   - Section 2: Functional spec draft
   - Section 3: Key decisions & risks
   - Section 4: Gate judgment (READY_FOR_NEXT / NOT_READY)

5. **Persist or present the result**
   - In **Agent mode** with file access:
     - Write or update `docs/features/FEAT-xxx/01-clarification.md`.
   - In **Ask/Plan modes** or read-only contexts:
     - Present the full document as markdown in the chat and clearly indicate the target file path for the user to copy.

## Question Design (Batches and Examples)

You MUST respect the command’s requirement:

- At most **15** clarification questions.
- Each key question should have **2–4 options** and be marked as **【单选】** or **【多选】**.

### Batch 1 – Scope & Boundaries

Focus on “what is / is not included” and critical constraints.

Typical questions (adapt as needed to the specific feature):

1. **【单选】本期要解决的核心目标是哪一类？**
   - A) 新功能（从无到有）
   - B) 现有功能增强 / 重构
   - C) 运营配置 / 规则变更
   - D) 其它：自定义说明

2. **【多选】本期 in-scope 的范围包含哪些？**
   - A) 后端接口 / 服务能力
   - B) 前端页面 / 交互
   - C) 任务调度 / 异步流程
   - D) 数据结构 / 表结构调整
   - E) 权限 / 审计 / 安全控制
   - F) 其它：自定义说明

3. **【多选】本期明确 out-of-scope 的内容有哪些？**
   - A) 跨系统集成
   - B) 报表 / BI 展示
   - C) 历史数据迁移
   - D) 复杂自动化策略 / 风控
   - E) 其它：自定义说明

4. **【单选】时间与优先级约束？**
   - A) P0：必须在当前迭代上线
   - B) P1：本季度内完成
   - C) P2：有空就做 / 机会型
   - D) 由你补充更具体的时间窗口

### Batch 2 – Users, Roles & Permissions

Clarify **who** can do **what**.

5. **【多选】涉及的主要用户角色有哪些？**
   - A) 业务运营 / 配置人员
   - B) 开发 / 技术人员
   - C) 管理员 / 系统 OWNER
   - D) 终端业务用户（如普通员工 / 客户）
   - E) 其它：自定义说明

6. **【多选】本期是否需要区分不同角色的操作权限？**
   - A) 仅区分“可见 / 不可见”
   - B) 区分“查看 / 新增 / 编辑 / 删除”
   - C) 区分“配置 / 审批 / 执行”
   - D) 不做权限细分，由上游系统控制

7. **【多选】需要审计的操作类型？**
   - A) 新增 / 编辑 / 删除
   - B) 启用 / 禁用 / 状态切换
   - C) 重要测试 /校验操作（如连接测试、风控试跑）
   - D) 敏感数据查看 / 导出
   - E) 其它：自定义说明

### Batch 3 – Flows, Edge Cases & Non-Functional

8. **【多选】本期必须覆盖的主流程有哪些？**
   - A) 新增 / 创建
   - B) 编辑 / 更新
   - C) 启用 / 禁用
   - D) 查询 / 筛选 / 列表展示
   - E) 导出 / 下载
   - F) 其它：自定义说明

9. **【多选】优先考虑的边界 / 异常场景？**
   - A) 删除 / 禁用被引用中的对象
   - B) 外部依赖失败（超时、网络异常、第三方错误）
   - C) 权限不足 / 未登录
   - D) 并发修改 / 冲突
   - E) 输入参数非法 / 校验失败
   - F) 其它：自定义说明

10. **【多选】本期最重要的非功能需求是哪几类？**
    - A) 性能（响应时间、吞吐量）
    - B) 可用性 / 稳定性
    - C) 安全（权限、敏感数据保护）
    - D) 审计 / 可追溯
    - E) 可观测性（日志、指标、告警）
    - F) 其它：自定义说明

You may adjust wording and options to fit the domain, but keep the **structure** (ids, single vs multi-select, 2–4 options with a customizable choice).

## Using AskQuestion (Interactive Form)

When AskQuestion is available, use it to present the questions above in 2–3 batches.

Example (pseudo-structure, not exact JSON):

```markdown
AskQuestion:
  title: "feature-clarify 关键澄清"
  questions:
    - id: "q_scope_type"
      prompt: "1）本期要解决的核心目标是哪一类？"
      options:
        - { id: "A", label: "A) 新功能（从无到有）" }
        - { id: "B", label: "B) 现有功能增强 / 重构" }
        - { id: "C", label: "C) 运营配置 / 规则变更" }
        - { id: "D", label: "D) 其它：我在后续回答里说明" }
    - id: "q_in_scope"
      prompt: "2）本期 in-scope 的范围包含哪些？【可多选】"
      allow_multiple: true
      options:
        - { id: "A", label: "A) 后端接口 / 服务能力" }
        - { id: "B", label: "B) 前端页面 / 交互" }
        - { id: "C", label: "C) 任务调度 / 异步流程" }
        - { id: "D", label: "D) 数据结构 / 表结构调整" }
        - { id: "E", label: "E) 权限 / 审计 / 安全控制" }
        - { id: "F", label: "F) 其它：我在后续回答里说明" }
```

If AskQuestion is not available, ask the same questions conversationally and instruct the user to reply like:

- `1 选 B，2 选 A+C+E，3 选 D：xxxx`

## Generating the Clarification Output

The final output MUST follow the structure defined in `.cursor/commands/01-feature-clarify.md`:

1. **澄清问题清单（按优先级，最多 15 个）**
   - 列出所有关键问题，标明：
     - 【单选】或【多选】
     - 选项 A/B/C/D 的全文
     - 用户的实际选择（如：“已选：B、D（自定义：……）”）
   - 按优先级排序（高优先级痛点 / 影响范围大的问题排前）。

2. **功能规格草案**
   - 目标 / 非目标（in-scope / out-of-scope 明确）
   - 用户角色与权限（用简易权限矩阵表示谁能看 / 改 / 删 / 导出等）
   - 主流程（≥ 1 条，如新增、编辑、启用 / 禁用、测试、筛选等）
   - 边界 / 异常（≥ 10 条，覆盖删除 / 禁用被引用、外部失败、超时、敏感字段展示等）
   - 非功能需求（性能 / 安全 / 审计 / 可观测性）

3. **关键决策与风险**
   - 对关键设计 / 取舍给出备选：
     - A) 保守方案
     - B) 进取方案
     - C) 折中方案
   - 允许用户直接用“选 B”或“都不选，我要 D：xxxx”的方式拍板。

4. **Gate（READY_FOR_NEXT / NOT_READY）**
   - 按命令文件中的 Gate 规则进行判断：
     1. in-scope / out-of-scope 明确
     2. 权限矩阵草案明确（谁能看 / 改 / 删 / 导出等）
     3. 主流程 ≥ 1 条
     4. 边界 / 异常 ≥ 10 条
     5. 待确认项 ≤ 5，且每一项都有清晰的单选 / 多选备选方案
     6. 成功标准 / 验收口径已定义（至少 1 条可验证标准）
   - 若全部满足：标记为 `READY_FOR_NEXT`，并在末尾建议进入 `/prd-generate`。
   - 否则：标记为 `NOT_READY`，列出阻塞项与建议补充的问题。

### Markdown Template Suggestion

When generating the final document, you can follow this markdown layout:

```markdown
1) 澄清问题清单
   1.1 【单选】问题标题……
       - A) 选项 A 文案
       - B) 选项 B 文案
       - C) 选项 C 文案
       - D) 选项 D 文案
       - 已选：B

   1.2 【多选】问题标题……
       - A) ...
       - B) ...
       - C) ...
       - 已选：A、C（D：自定义说明……）

2) 功能规格草案
   - 目标（in-scope）
   - 非目标（out-of-scope）
   - 用户角色与权限
   - 主流程
   - 边界 / 异常
   - 非功能需求

3) 关键决策与风险
   - 决策点 1：A/B/C 方案 + 你的推荐
   - 决策点 2：……

4) Gate 判定
   - Gate 状态：READY_FOR_NEXT / NOT_READY
   - 检查项说明
   - 阻塞项（如有）
   - 下一步建议（如：进入 `/prd-generate`）
```

## Integrating with Documentation

When running in a context that allows file edits:

1. 确认本次澄清对应的 `FEAT` 编号（如 `FEAT-123`）。
2. 计算目标路径：`docs/features/FEAT-123/01-clarification.md`。
3. 若文件不存在：
   - 创建目录 `docs/features/FEAT-123/`。
   - 创建 `01-clarification.md` 并写入完整澄清文档。
4. 若文件已存在：
   - 视情况选择：
     - 覆盖整个文件为最新版本，或
     - 在文件末尾追加“本次澄清版本（日期+时间）”区块。
5. 在聊天回复中始终：
   - 明确给出 Gate 状态。
   - 明确给出目标文件路径。
   - 简要列出阻塞项（若 NOT_READY）。

In read-only modes, skip file writes and instead present the full markdown plus the **recommended file path** so the user can copy-paste.

## Notes and Constraints

- 不要编造关键业务规则；对于不确定的业务细节，优先用“选项 + 让用户拍板”的方式表达。
- 总问题数控制在 15 个以内，且分批提出，避免用户一次性处理过多信息。
- 输出语言默认为中文，分点编号，结构稳定，便于后续 PRD / 设计 / 开发复用。
- 在整个流程中，始终把用户的决策（选项）显式写入澄清文档，避免再次口头同步。

