# /impl-fullstack
同一条需求内：DB migration + 后端实现 + 前端实现（按 tech-design / 原型逐条对照）。
目标：一次把三端打通，但仍按阶段 Gate 自检，避免契约与字段不一致。

## 必须引用的上下文（缺一不可）
- @docs/features/FEAT-xxx/04-tech-design.md
- @docs/features/FEAT-xxx/03-prototype-text.md
- DB 目录（@Files & Folders）：migrations / schema / ORM models（以及现有表定义）
- 后端目录（@Files & Folders）：controller/service/repository/dto/error-code/auth/pagination 等相关模块
- 前端目录（@Files & Folders）：pages/components/api-client/types/request 封装等相关模块
- 如已存在：接口类型/契约文件（OpenAPI/Swagger/ts types）用 @Files/@Code 引用

## 输出要求（必须按阶段分节输出）
### Phase A — DB（只做迁移与模型）
1) 将新增/修改的 DB 文件清单（migration/schema/model）
2) migration 方案：顺序、兼容性策略、回滚策略
3) 具体 SQL/迁移内容（按项目规范）
4) 本地验证方式（如何跑 migration、如何检查）
5) 风险点（锁表/耗时/数据量/索引）

**Phase A Gate（末尾给 READY/NOT_READY）**
- migration 可执行（给出命令）
- 兼容策略明确（可空/默认值/双写等）
- 回滚明确（down migration 或替代方案）
- 影响评估有说明

> 然后在 Phase B 必须“引用 Phase A 实际变更的 migration/model 文件”（用 @Files），以它为准，不允许凭记忆复述字段。

### Phase B — Backend（API + 业务逻辑 + 测试，不写前端）
1) 实施计划（按端点/模块拆分）
2) 修改文件清单（含测试）
3) API 逐条实现并对照 tech-design：schema 字段级、鉴权/权限、错误码、分页/筛选/排序、幂等（如涉及）
4) 参数校验与权限校验边界说明
5) 联调说明（示例请求、鉴权方式、典型错误返回）
6) 本地验证命令
7) 测试补充（新增测试点列表 + 代码）

**Phase B Gate**
- API 契约与 tech-design 一致（字段级）
- 鉴权/权限到位
- 错误码与异常处理一致
- 分页/筛选/排序规则一致
- 关键路径至少有自动化测试
- 联调说明可复现

> Phase B 中涉及字段/默认值/可空性时：以 Phase A 引用的 migration/model 为唯一真相；如发现不一致，必须先回到 Phase A 修正方案再继续。

### Phase C — Frontend（页面与交互）
1) 页面/路由/组件清单
2) 按文本原型逐条对照关键交互点（字段/规则/按钮 enable-disable 条件）
3) 状态处理：加载/空/错/无权限/成功
4) 列表规则：筛选/排序/分页/批量操作（限制与部分失败提示）
5) 本地验证步骤
6) 如有测试框架：补测试；否则给手测 checklist

**Phase C Gate**
- 页面结构/字段/校验/可编辑性与原型一致
- 按钮 enable/disable 条件一致
- 列表规则一致（筛选/排序/分页/批量约束）
- 状态齐（加载/空/错/无权限/成功）
- 给出可复现手测路径（或自动化测试）

### Final Cross-check（强制做一致性核对）
- DB 字段清单（来自 Phase A 引用文件） vs 后端 DTO/Entity vs API schema：逐字段对齐（名称/类型/可空/默认值）
- API schema vs 前端 types/表单字段/列表列：逐字段对齐
- 选 2-3 条关键路径用例：按“请求示例 + 页面操作路径”串起来

## 全局约束
- 最小可交付切片，避免无关改动
- 若任一 Phase Gate 为 NOT_READY：必须列阻塞项与回退建议（先修 DB/契约再继续）
- 允许拆成独立 PR（DB / 后端 / 前端 / 测试），但本命令在一个输出里给全链路方案