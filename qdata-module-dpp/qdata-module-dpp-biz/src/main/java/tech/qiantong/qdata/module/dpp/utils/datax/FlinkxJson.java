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

package tech.qiantong.qdata.module.dpp.utils.datax;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.utils.MD5Util;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;

import java.util.*;

@Component
public class FlinkxJson {

    public static String buildJobJsonMasterdata(Map<String, Object> taskParams) {

        // Create the required record.
        Map<String, Object> jobJson = new HashMap<>();

        // Implementation details.
        Map<String, Object> setting = new HashMap<>();

        // Implementation details.
        Map<String, Object> speed = new HashMap<>();
        speed.put("channel", 1);  // Implementation details.
        speed.put("bytes", 0);    // Implementation details.
        setting.put("speed", speed);

        // Implementation details.
        Map<String, Object> errorLimit = new HashMap<>();
        errorLimit.put("record", 999999999);  // Implementation details.
        setting.put("errorLimit", errorLimit);

        // Implementation details.
        Map<String, Object> restore = new HashMap<>();
        restore.put("maxRowNumForCheckpoint", 0);   // Implementation details.
        restore.put("isRestore", false);            // Implementation details.
        restore.put("restoreColumnName", "");       // Implementation details.
        restore.put("restoreColumnIndex", 0);       // Implementation details.
        setting.put("restore", restore);

        // Implementation details.
        Map<String, Object> log = new HashMap<>();
        log.put("isLogger", false);  // Implementation details.
        log.put("level", "debug");   // Implementation details.
        log.put("path", "");         // Implementation details.
        log.put("pattern", "");      // Implementation details.
        setting.put("log", log);

        jobJson.put("setting", setting);


        // Handle database and data source configuration.
        // Implementation details.
        Map<String, Object> readerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "readerDatasource");
        // Implementation details.
        Map<String, Object> writerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "writerDatasource");

        DbQueryProperty readerProperty = MD5Util.buildJobDatasource(readerDatasource);
        DbQueryProperty writerProperty = MD5Util.buildJobDatasource(writerDatasource);


        String writeMode = "";
        String where = MapUtils.getString(taskParams, "where", "");
        String preSql = MapUtils.getString(taskParams, "preSql", "");
        String postSql = MapUtils.getString(taskParams, "postSql", "");
        String table_name = MapUtils.getString(taskParams, "table_name", "");
        String target_table_name = MapUtils.getString(taskParams, "target_table_name", "");
        Object columns = MapUtils.getObject(taskParams, "columns");
        Object target_columns = MapUtils.getObject(taskParams, "target_columns");
        Object writeKeySet = MapUtils.getObject(taskParams, "selectedColumns");// Implementation details.
        // Handle node-related data and operations.
        String type = MapUtils.getString(taskParams, "type", "");
        String writeModeType = MapUtils.getString(taskParams, "writeModeType", "");
        if (StringUtils.equals("1", type)) {
            writeMode = readerProperty.trainToJdbcWriteMode(null, writeModeType,writerProperty.getDbType());
        }
        if (StringUtils.equals("2", type)) {
            writeMode = readerProperty.trainToJdbcWriteMode(writeKeySet, writeModeType,writerProperty.getDbType());
            // Delete the related record.
            if (StringUtils.equals("1", writeModeType)) {
                preSql = readerProperty.trainToJdbcTruncateTable(writerProperty.getDbNameTableName(target_table_name));
            }
        }

        // Create the required record.
        List<Map<String, Object>> content = new ArrayList<>();
        Map<String, Object> contentItem = new HashMap<>();

        // Implementation details.
        Map<String, Object> reader = new HashMap<>();
        reader.put("name", readerProperty.trainToJdbcReaderName());
        Map<String, Object> readerParameter = new HashMap<>();
        readerParameter.put("username", readerProperty.getUsername());
        readerParameter.put("password", readerProperty.getPassword());
        readerParameter.put("where", where);
        readerParameter.put("column", columns);
        readerParameter.put("splitPk", MapUtils.getString(taskParams, "readerSplitPk", ""));
        List<Map<String, Object>> readerConnection = new ArrayList<>();
        Map<String, Object> readerConnectionItem = new HashMap<>();
        readerConnectionItem.put("table", Arrays.asList(readerProperty.getDbNameTableName(table_name)));
        readerConnectionItem.put("jdbcUrl", Arrays.asList(readerProperty.trainToJdbcUrl()));
        readerConnection.add(readerConnectionItem);
        readerParameter.put("connection", readerConnection);
        reader.put("parameter", readerParameter);

        // Implementation details.
        Map<String, Object> writer = new HashMap<>();
        writer.put("name", writerProperty.trainToJdbcWriterName());
        Map<String, Object> writerParameter = new HashMap<>();
        writerParameter.put("username", writerProperty.getUsername());
        writerParameter.put("password", writerProperty.getPassword());
        writerParameter.put("batchSize", taskParams.getOrDefault("batchSize", 1024)); // Implementation details.
        //
        writerParameter.put("writeMode", writeMode);
        writerParameter.put("column", target_columns);
        if (StringUtils.isNotBlank(preSql)) {
            writerParameter.put("preSql", preSql.split(","));
        }
        if (StringUtils.isNotBlank(postSql)) {
            writerParameter.put("postSql", postSql.split(","));
        }
        List<Map<String, Object>> writerConnection = new ArrayList<>();
        Map<String, Object> writerConnectionItem = new HashMap<>();
        writerConnectionItem.put("table", Arrays.asList(writerProperty.getDbNameTableName(target_table_name)));
        writerConnectionItem.put("jdbcUrl", writerProperty.trainToJdbcUrl());
        writerConnection.add(writerConnectionItem);
        writerParameter.put("connection", writerConnection);
        writer.put("parameter", writerParameter);

        // Implementation details.
        contentItem.put("reader", reader);
        contentItem.put("writer", writer);
        content.add(contentItem);

        jobJson.put("content", content);
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("job", jobJson);
        // Handle JSON data for this operation.
        return JSON.toJSONString(objectObjectHashMap);
    }

    /**
     * Handle node-related data and operations.
     * Handle DataX task configuration and execution.
     */
    public static DppEtlNodeRespVO findLocalDataXNode(List<DppEtlNodeRespVO> nodeList, String componentType) {
        for (DppEtlNodeRespVO node : nodeList) {
            // Handle node-related data and operations.
            if (node == null) {
                continue;
            }
            // Handle node-related data and operations.
            if (StringUtils.equals(componentType, node.getComponentType())) {
                return node;
            }
        }
        return null;
    }
}
