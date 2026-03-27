请基于：
- @docs/features/FEAT-xxx/02-prd.md
- @docs/features/FEAT-xxx/03-prototype-text.md
- @docs/features/FEAT-xxx/04-tech-design.md
  生成测试方案与测试用例，输出到 @docs/features/FEAT-xxx/05-test-plan.md

内容结构：
1) 测试范围（in/out）
2) 环境与数据准备（测试账号、数据构造）
3) 用例清单（按功能点分组，至少：正向/边界/异常/权限）
4) 回归清单（受影响模块）
5) 风险点与重点关注
6) 自动化建议（哪些适合单测/集成/E2E）

约束：
- 用例必须可执行（前置条件、步骤、期望结果）。
