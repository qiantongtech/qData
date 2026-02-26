# /impl-backend
实现后端：API + 业务逻辑 + 测试（不写前端）。

## 必须引用的上下文
- @docs/features/FEAT-xxx/04-tech-design.md
- DB 变更文件（migration/model）
- 后端相关模块目录（@Files & Folders）

## 输出要求
1) 实施计划（按端点/模块拆分）
2) 修改文件清单
3) 代码实现（遵循现有风格）
4) 测试补充（新增测试点列表 + 代码）
5) 本地验证命令（如何跑）
6) 自检清单：权限、参数校验、错误码、分页/筛选/排序、幂等（如涉及）

## 约束
- 不要改前端。
- 不要破坏既有公共接口行为（除非 PRD 明确）。

## Gate（必须在输出末尾给出）
- 产物清单：列出新增/修改的后端文件与测试文件
- 验收清单（Checklist）：
  1) API 契约与 tech-design 一致（字段级）
  2) 鉴权/权限到位
  3) 错误码与异常处理一致
  4) 分页/筛选/排序规则一致
  5) 关键路径至少有自动化测试
  6) 给出联调说明（示例请求/鉴权方式）
- Gate 判定：
  - READY_FOR_NEXT：以上全部满足
  - 否则 NOT_READY：指出未实现的端点/缺失测试/契约不一致处
- 下一步建议：
  - READY_FOR_NEXT -> /impl-frontend（需引用：prototype-text + tech-design + 接口类型/示例）
