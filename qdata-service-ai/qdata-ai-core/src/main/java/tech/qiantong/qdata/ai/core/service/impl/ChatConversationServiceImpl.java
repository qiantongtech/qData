/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
 * Purpose: Session implementation class
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
     * Build fact table entities
     *
     * @param tableName table name
     * @param tableComment table comment
     * @param dbColumns database field list
     * @return fact table entity
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
     * Build dimension table entities
     *
     * @param tableName table name
     * @param tableComment table comment
     * @param dbColumns database field list
     * @return dimension table entity
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
