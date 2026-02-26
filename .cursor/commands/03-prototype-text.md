# /prototype-text
把PRD转换成“文字版原型”，把交互细节写到可编码粒度（不画图、不写代码）。

## 必须引用的上下文
- @docs/features/FEAT-xxx/02-prd.md

## 输出（写入 docs/features/FEAT-xxx/03-prototype-text.md）
逐页面输出：
- 页面名称 & 入口
- 适用角色/权限
- 页面结构（区块划分）
- 字段清单（字段名/类型/默认值/校验/是否展示/是否可编辑）
- 交互（点击/输入/提交/取消/二次确认）
- 列表规则（筛选/排序/分页/批量操作：限制、部分失败提示）
- 状态：加载态/空态/错误态/无权限态/成功态
- 错误提示文案（示例即可）
- 可测试验收点（每页>=8条）

## 约束
- 必须写清按钮 enable/disable 条件。
- 列表页必须写清分页默认值/最大值、排序默认规则。

## Gate（必须在输出末尾给出）
- 产物清单：写入 docs/features/FEAT-xxx/03-prototype-text.md
- 验收清单（Checklist）：
    1) 每页都有结构区块
    2) 字段清单完整（类型/默认值/校验）
    3) 状态齐（加载/空/错/无权限/成功）
    4) 按钮 enable/disable 规则明确
    5) 列表规则明确（筛选/排序/分页/批量约束）
    6) 每页>=8条可测试点
- Gate 判定：
    - READY_FOR_NEXT：以上全部满足
    - 否则 NOT_READY：指出缺页/缺状态/缺规则
- 下一步建议：
    - READY_FOR_NEXT -> /tech-design（需引用 @docs/features/FEAT-xxx/02-prd.md 和 @docs/features/FEAT-xxx/03-prototype-text.md，并补充 @Files & Folders 相关代码目录）
