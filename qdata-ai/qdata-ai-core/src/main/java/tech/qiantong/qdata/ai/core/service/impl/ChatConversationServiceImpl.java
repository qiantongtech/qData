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

package tech.qiantong.qdata.ai.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.ai.core.prompt.params.DimensionTable;
import tech.qiantong.qdata.ai.core.prompt.params.FactTable;
import tech.qiantong.qdata.ai.core.prompt.MatchPromptBuilder;
import tech.qiantong.qdata.ai.core.service.IChatConversationService;
import tech.qiantong.qdata.ai.core.service.IChatModelService;
import tech.qiantong.qdata.ai.core.vo.AiChatConversationSaveRespVO;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationSaveReqVO;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatConversationService;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;

import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 * 用途:会话实现类
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-09 10:50
 **/
@Slf4j
@Service
public class ChatConversationServiceImpl implements IChatConversationService {

    @Resource
    private IAiChatConversationService aiChatConversationService;

    @Resource
    private IDaDatasourceApiService daDatasourceApiService;

    @Resource
    private IChatModelService chatModelService;

    @Resource
    private MatchPromptBuilder matchPromptBuilder;

    @Override
    public AiChatConversationSaveRespVO createAiChatConversation(AiChatConversationSaveReqVO reqVO) {
        ChatModel chatModel = chatModelService.getChatModel(reqVO.getModelId());

        Long datasourceId = reqVO.getDatasourceId();
        String factTableName = reqVO.getFactTableName();
        String factTableCommen = reqVO.getFactTableComment();

        List<DbColumn> dbTableColumns = daDatasourceApiService.getDbTableColumns(datasourceId, factTableName);

        FactTable factTable = buildFactTable(factTableName, factTableCommen, dbTableColumns);

        List<DimensionTable> dimensions = new ArrayList<>();
        List<JSONObject> dimensionTablesData = new ArrayList<>();

        if (StringUtils.isNotBlank(reqVO.getDimensionTable())) {
            JSONArray dimensionTables = JSONArray.parse(reqVO.getDimensionTable());
            if (!dimensionTables.isEmpty()) {
                for (Object obj : dimensionTables) {
                    JSONObject dimensionTable = (JSONObject) obj;
                    dimensionTablesData.add(dimensionTable);

                    String dimensionTableName = dimensionTable.getString("tableName");
                    String dimensionTableCommen = dimensionTable.getString("tableComment");

                    List<DbColumn> dbDimensionColumns = daDatasourceApiService.getDbTableColumns(datasourceId, dimensionTableName);

                    DimensionTable dimensionTableEntity = buildDimensionTable(dimensionTableName, dimensionTableCommen, dbDimensionColumns);
                    dimensions.add(dimensionTableEntity);
                }
            }
        }

        Boolean success = true;
        if (!dimensionTablesData.isEmpty()) {
            String prompt = matchPromptBuilder.buildPrompt(factTable, dimensions);
            String result = chatModel.call(prompt);
            JSONObject resultObject = JSONObject.parseObject(result);

            if (resultObject.getBoolean("success")) {
                JSONArray associations = resultObject.getJSONArray("associations");
                if (associations.size() != dimensionTablesData.size()) {
                    success = false;
                }
                reqVO.setAssociations(associations.toJSONString());
            } else {
                success = false;
            }
        }

        Long resCode;
        if (success) {
            reqVO.setJoinConditionMatchFlag(true);
            reqVO.setJoinConditionMatchType("1");
            resCode = 200L;
        } else {
            reqVO.setJoinConditionMatchFlag(false);
            reqVO.setJoinConditionMatchType("2");
            resCode = 10001L;
        }

        return AiChatConversationSaveRespVO.builder()
                .id(aiChatConversationService.createAiChatConversation(reqVO))
                .code(resCode)
                .build();
    }

    /**
     * 构建事实表实体
     *
     * @param tableName    表名
     * @param tableComment 表注释
     * @param dbColumns    数据库字段列表
     * @return 事实表实体
     */
    private FactTable buildFactTable(String tableName, String tableComment, List<DbColumn> dbColumns) {
        List<FactTable.Column> columns = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();

        for (DbColumn dbColumn : dbColumns) {
            if (dbColumn.getColKey()) {
                primaryKeys.add(dbColumn.getColName());
            }
            columns.add(FactTable.Column.builder()
                    .name(dbColumn.getColName())
                    .type(dbColumn.getDataType())
                    .description(dbColumn.getColComment())
                    .build());
        }

        return FactTable.builder()
                .tableName(tableName)
                .alias(StrUtil.toCamelCase(tableName))
                .description(tableComment)
                .columns(columns)
                .primaryKeys(primaryKeys)
                .build();
    }

    /**
     * 构建维度表实体
     *
     * @param tableName    表名
     * @param tableComment 表注释
     * @param dbColumns    数据库字段列表
     * @return 维度表实体
     */
    private DimensionTable buildDimensionTable(String tableName, String tableComment, List<DbColumn> dbColumns) {
        List<DimensionTable.Column> columns = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();

        for (DbColumn dbColumn : dbColumns) {
            if (dbColumn.getColKey()) {
                primaryKeys.add(dbColumn.getColName());
            }
            columns.add(DimensionTable.Column.builder()
                    .name(dbColumn.getColName())
                    .type(dbColumn.getDataType())
                    .description(dbColumn.getColComment())
                    .build());
        }

        return DimensionTable.builder()
                .tableName(tableName)
                .alias(StrUtil.toCamelCase(tableName))
                .description(tableComment)
                .columns(columns)
                .primaryKeys(primaryKeys)
                .build();
    }
}
