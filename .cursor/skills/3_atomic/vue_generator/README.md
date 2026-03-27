# vue_generator - Vue CRUD 页面生成流水线

本目录收纳「从需求到 Vue 代码」整条流水线用到的原子技能，子目录前的**数字表示流程顺序**：

| 数字 | 目录 | 含义 |
|------|------|------|
| 1 | 1_requirements_understanding | 需求理解：产出功能清单、业务规则、交互意图（requirement_spec） |
| 2 | 2_prototype_design | 原型设计：产出原型规格（prototype_spec） |
| 3 | 3_data_model_definition | 数据模型定义：根据表结构或推荐字段维护 page_spec（驼峰 key），供代码生成消费 |
| 4 | 4_frontend_code_gen | 生成前端代码：根据 page_spec 生成 Vue3 + Element Plus 页面、api.js 与 api.mock.js |

**流水线顺序**：1 → 2 → 3 → 4。编排时 2 产出 prototype_spec，3 产出 page_spec，4 消费 page_spec 并输出代码与 mock。  
编排时按此顺序串联技能，上游输出即下游输入。
