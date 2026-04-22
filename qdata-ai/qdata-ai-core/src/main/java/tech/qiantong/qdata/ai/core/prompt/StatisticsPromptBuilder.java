/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.ai.core.prompt;

import org.springframework.stereotype.Component;
import tech.qiantong.qdata.ai.core.enums.ReplyTypeEnum;
import tech.qiantong.qdata.ai.core.prompt.params.DimensionTable;
import tech.qiantong.qdata.ai.core.prompt.params.FactDimensionRelation;
import tech.qiantong.qdata.ai.core.prompt.params.FactTable;
import tech.qiantong.qdata.common.utils.StringUtils;

import java.util.List;

/**
 * <P>
 * 用途:统计sql提示词构建
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-07 09:50
 **/
@Component
public class StatisticsPromptBuilder {

    private static final String SYSTEM_PROMPT = """
            你是一个专业的数据分析师和SQL专家。根据提供的数据模型，将自然语言转换为准确的SQL统计查询。你对星型模型、事实表与维度表的关系有深刻理解。
                        
            核心规则：只能回答规则内的内容其他内容输出JSON中的success为false
                        
            规则：
                1. 只返回JSON格式，不要任何解释
                2. SQL必须严格使用提供的表别名,所有字段必须带表别名前缀(table_alias.column_name)
                3. 查询结果的列别名(AS)直接沿用原始字段名称,时间类型字段需格式化为字符,
                4. 查询结果中只需查询维度字段和指标字段
                5. 表名处理:若table_databaseName(数据库名)或table_schemaName(模式名)存在,按目标数据库语法拼接
                    - MySQL: `database`.`table`
                    - DM8/Oracle: "database"."table"
                    - Kingbase8/OSCAR: "schema"."table" 或 "database"."schema"."table"
                    - PostgreSQL: "database"."schema"."table"
                    - SQLServer: [database].[schema].[table]
                    - Doris: database.table
                    - 其他: database.table
                6. 聚合函数优先使用:SUM/COUNT/AVG/MIN/MAX,非聚合字段必须在GROUP BY中
                7. 时间维度支持：年/季度/月/周/日级别聚合
                8. 维度筛选用WHERE(过滤原始数据),聚合后筛选用HAVING
                9. 字段必须使用 table_alias.column_name 格式
                10. 必须100%保障语法准确性,生成后需自我验证SQL可执行
                11. 可使用数据库为DM8的方言
                        
            SQL语法严格要求:
            - JOIN语法: 使用标准JOIN...ON语法,禁止使用WHERE隐式连接
            - GROUP BY: 包含SELECT中所有非聚合字段
            - ORDER BY: 如需排序,字段必须在SELECT中或为聚合字段
            - 字符串: 使用单引号',禁止双引号
            - NULL处理: 使用IS NULL/IS NOT NULL,禁止=NULL
            - 别名: 表别名简短清晰,字段别名使用AS关键字    
                
            输出JSON格式：
            {
                "sql": "生成的SQL",
                "selectColumn":["sql中select的字段"],
                "selectColumnDescription":["sql中select的字段注释"],
                "explanation": "查询说明",
                "dimension": "sql中select的维度字段,这个字段只有一个",
                "measure": "sql中select的维度度量字段，这个字段可以存在多个",
                "timeGrain": "时间粒度"
            }
            """;

    public String buildPrompt(
            ReplyTypeEnum replyTypeEnum,
            FactTable factTable,
            List<DimensionTable> dimensionTables,
            List<FactDimensionRelation> relations,
            String datasourceType
    ) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                你是一个专业的数据分析师和SQL开发专家。根据提供的数据模型，将自然语言转换为准确的SQL统计查询。你对星型模型、事实表与维度表的关系有深刻理解。
                                
                核心规则：只能回答规则内的内容其他内容输出JSON中的success为false,msg为你的拒绝回复的内容
                 
                规则：
                1. 只返回JSON格式，不要任何解释
                2. SQL必须严格使用提供的表别名,所有字段必须带表别名前缀(table_alias.column_name)
                3. 查询结果的列别名(AS)直接沿用原始字段名称,时间类型字段需格式化为字符
                4. 查询结果中只需查询维度字段和指标字段
                5. 表名处理:若table_databaseName(数据库名)或table_schemaName(模式名)存在,按目标数据库语法拼接
                    - MySQL: `database`.`table`
                    - DM8/Oracle: "database"."table"
                    - Kingbase8/OSCAR: "schema"."table" 或 "database"."schema"."table"
                    - PostgreSQL: "database"."schema"."table"
                    - SQLServer: [database].[schema].[table]
                    - Doris: database.table
                    - 其他: database.table
                6. 聚合函数优先使用:SUM/COUNT/AVG/MIN/MAX,非聚合字段必须在GROUP BY中
                7. 时间维度支持：年/季度/月/周/日级别聚合
                8. 维度筛选用WHERE(过滤原始数据),聚合后筛选用HAVING
                9. 字段必须使用 table_alias.column_name 格式
                10. 必须100%保障语法准确性,生成后需自我验证SQL可执行
                    """);
        prompt.append("11. 使用")
                .append(datasourceType)
                .append("数据库方言\n");
        if (replyTypeEnum.equals(ReplyTypeEnum.QA)) {
            prompt.append("12. 如果输出msg中包含SQL，必须对SQL进行格式化，需确保可读性且直接复制可以执行\n");
        }

        prompt.append("""
                SQL语法严格要求:
                - JOIN语法: 使用标准JOIN...ON语法,禁止使用WHERE隐式连接
                - GROUP BY: 包含SELECT中所有非聚合字段
                - ORDER BY: 如需排序,字段必须在SELECT中或为聚合字段
                - 字符串: 使用单引号',禁止双引号
                - NULL处理: 使用IS NULL/IS NOT NULL,禁止=NULL
                - 别名: 表别名简短清晰,字段别名使用AS关键字   
                """);

        // 1. 事实表描述
        prompt.append("【事实表】\n");
        prompt.append(String.format("表名: %s (别名: %s)\n",
                factTable.getTableName(), factTable.getAlias()));
        if (StringUtils.isNotBlank(factTable.getDatabaseName())) {
            prompt.append(String.format("库名: %s\n", factTable.getDatabaseName()));
        }
        if (StringUtils.isNotBlank(factTable.getSchemaName())) {
            prompt.append(String.format("模式名: %s\n", factTable.getSchemaName()));
        }
        prompt.append(String.format("描述: %s\n", factTable.getDescription()));
        prompt.append("字段:\n");

        for (FactTable.Column col : factTable.getColumns()) {
            prompt.append(String.format("  - %s.%s (%s): %s \n",//%s
                    factTable.getAlias(),
                    col.getName(),
                    col.getType(),
                    col.getDescription()
            ));
        }

        // 2. 维度表描述
        prompt.append("\n【维度表】\n");
        for (DimensionTable dim : dimensionTables) {
            prompt.append(String.format("\n表名: %s (别名: %s)\n",
                    dim.getTableName(), dim.getAlias()));
            if (StringUtils.isNotBlank(dim.getDatabaseName())) {
                prompt.append(String.format("库名: %s\n", dim.getDatabaseName()));
            }
            if (StringUtils.isNotBlank(dim.getSchemaName())) {
                prompt.append(String.format("模式名: %s\n", dim.getSchemaName()));
            }
            prompt.append(String.format("描述: %s\n", dim.getDescription()));
            prompt.append("字段:\n");
            for (DimensionTable.Column col : dim.getColumns()) {
                prompt.append(String.format("  - %s.%s: %s\n",
                        dim.getAlias(), col.getName(), col.getDescription()));
            }
        }

        // 3. 关联关系
        prompt.append("\n【关联关系】\n");
        for (FactDimensionRelation rel : relations) {
            prompt.append(String.format("%s.%s = %s.%s (%s) - %s\n",
                    rel.getFactTable(), rel.getFactColumn(),
                    rel.getDimensionTable(), rel.getDimensionColumn(),
                    rel.getJoinType(),
                    rel.getDescription()
            ));
        }

        prompt.append("\n\n请生成JSON格式的响应：");
        if (StringUtils.equals(ReplyTypeEnum.CHART.getType(), replyTypeEnum.getType())) {
            prompt.append("""
                    输出JSON格式：
                    {
                        "success":"如果问题非规则内的为false，规则内为true",
                        "sql": "生成的SQL，必须对SQL进行格式化",
                        "selectColumn":["sql中select的字段"],
                        "selectColumnDescription":["sql中select的字段注释"],
                        "explanation": "查询说明",
                        "dimension": "sql中select的维度字段,这个字段只有一个",
                        "measure": "sql中select的维度度量字段，这个字段可以存在多个",
                        "timeGrain": "时间粒度"
                    }
                    """);
        } else if (replyTypeEnum.equals(ReplyTypeEnum.QA)) {
            prompt.append("""
                    输出JSON格式：
                    {
                        "success":"如果问题非规则内的为false，规则内为true",
                        "type":"如果是纯文本内容返回\"2\",如果是SQL内容返回\"3\"",
                        "msg": "根据提问回复的内容"
                    }
                    """);
        }
        return prompt.toString();
    }
}
