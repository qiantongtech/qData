# /impl-db
只做 DB 迁移落地（migration/schema/ORM），不做业务逻辑。

## 必须引用的上下文
- @docs/features/FEAT-xxx/04-tech-design.md
- DB 相关目录：@Files & Folders 选择 migrations/schema/ORM models

## 输出要求
1) 先列出将修改/新增的文件清单
2) migration 方案（顺序、兼容性、回滚）
3) 生成对应代码/SQL（符合项目规范）
4) 本地验证方式（如何跑 migration、如何检查）
5) 风险点（锁表、耗时、数据量、索引）

## 约束
- 变更尽量小且可回滚。
- 不要顺手改业务代码。

## Gate（必须在输出末尾给出）
- 产物清单：列出新增/修改的 migration/schema/model 文件
- 验收清单（Checklist）：
    1) migration 可执行（提供命令）
    2) 兼容策略明确（默认值/可空/双写等）
    3) 回滚策略明确（down migration 或替代方案）
    4) 影响评估（锁表/耗时）有说明
- Gate 判定：
    - READY_FOR_NEXT：以上全部满足
    - 否则 NOT_READY：指出缺少的回滚/验证/兼容策略
- 下一步建议：
    - READY_FOR_NEXT -> /impl-backend（需引用：tech-design + DB 变更文件）
