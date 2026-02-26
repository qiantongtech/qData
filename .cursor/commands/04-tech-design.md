# /tech-design
收敛技术方案：DB/API契约/前后端边界/迁移与回滚/测试策略（不直接改代码）。

## 必须引用的上下文
- @docs/features/FEAT-xxx/02-prd.md
- @docs/features/FEAT-xxx/03-prototype-text.md
- 以及相关代码：通过 @Files & Folders / @Code 引用涉及模块

## 输出（写入 docs/features/FEAT-xxx/04-tech-design.md）
结构：
1) 现状与影响范围（模块/表/页面）
2) 数据设计（新增/改表、字段、索引、约束、兼容性、迁移顺序、回滚）
3) API 设计（端点列表、鉴权、请求/响应 schema、错误码、幂等、分页/筛选/排序规则）
4) 前端设计（页面/组件/状态/接口调用/错误态）
5) 可观测性（日志、埋点、告警、审计）
6) 测试策略（单测/集成/E2E 覆盖点 + 关键用例）
7) 任务拆解（DB、后端、前端、测试：可独立PR）
8) 风险与 trade-off（推荐决策）

## 约束
- 不确定处写【待确认项】+ 推荐选项A/B。

## Gate（必须在输出末尾给出）
- 产物清单：写入 docs/features/FEAT-xxx/04-tech-design.md
- 验收清单（Checklist）：
    1) DB 变更明确：字段/索引/约束/兼容/迁移顺序/回滚
    2) API 契约明确：schema 到字段级、错误码、鉴权、分页筛选排序
    3) 前后端职责边界明确（校验/权限/异常处理）
    4) 测试策略明确（覆盖点 + 用例）
    5) 任务拆解可独立PR
- Gate 判定：
    - READY_FOR_NEXT：以上全部满足
    - 否则 NOT_READY：列缺失项（例如 API schema 未细化/回滚缺失）
- 下一步建议（按依赖顺序给出）：
    - 一般：/impl-db -> /impl-backend -> /impl-frontend
    - 并说明每一步需要引用哪些文件（migration/接口类型/页面目录）
