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

package tech.qiantong.qdata.module.dpp.utils.ds.component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.config.AniviaConfig;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.utils.MD5Util;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.module.dpp.utils.datax.FlinkxJson;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import java.io.File;
import java.util.*;

/**
 * <P>
 * Purpose: Excel input component
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-14 11:31
 **/
public class ExcelReaderComponent implements ComponentItem {
    @Override
    public Map<String, Object> parse(Map<String, Object> params) {
        Map<String, Object> taskParams = new LinkedHashMap<>();
        taskParams.put("localParams", params.getOrDefault("localParams", new ArrayList<>())); // Default empty list
        taskParams.put("resourceList", params.getOrDefault("resourceList", new ArrayList<>())); // Default empty list
        taskParams.put("customConfig", params.getOrDefault("customConfig", 1)); // Default fixed to 1
        taskParams.put("xms", params.getOrDefault("xms", 1)); // Default 1
        taskParams.put("xmx", params.getOrDefault("xmx", 1)); // Default 1
        taskParams.put("json", buildJson(params)); // Default empty JSON string
        return taskParams;
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.EXCEL_READER.getCode();
    }


    public static String buildJson(Map<String, Object> taskParams) {

        // Create outermost jobJson Map
        Map<String, Object> jobJson = new HashMap<>();

        // Set job-related setting config
        Map<String, Object> setting = new HashMap<>();

        // Speed config, default values assigned directly
        Map<String, Object> speed = new HashMap<>();
        speed.put("channel", 1);  // Default value
        speed.put("bytes", 0);    // Default value
        setting.put("speed", speed);

        // ErrorLimit config, default values assigned directly
        Map<String, Object> errorLimit = new HashMap<>();
        errorLimit.put("record", 999999999);  // Default value
        setting.put("errorLimit", errorLimit);

        // Restore config, default values assigned directly
        Map<String, Object> restore = new HashMap<>();
        restore.put("maxRowNumForCheckpoint", 0);   // Default value
        restore.put("isRestore", false);            // Default value
        restore.put("restoreColumnName", "");       // Default value
        restore.put("restoreColumnIndex", 0);       // Default value
        setting.put("restore", restore);

        // Log config, default values assigned directly
        Map<String, Object> log = new HashMap<>();
        log.put("isLogger", false);  // Default value
        log.put("level", "debug");   // Default value
        log.put("path", "");         // Default value
        log.put("pattern", "");      // Default value
        setting.put("log", log);

        jobJson.put("setting", setting);


        //Input writerDatasource
        Map<String, Object> writerDatasource = (Map<String, Object>) MapUtils.getObject(taskParams, "writerDatasource");

        DbQueryProperty writerProperty = MD5Util.buildJobDatasource(writerDatasource);


        String target_table_name = MapUtils.getString(taskParams, "target_table_name", "");
        Object columns = MapUtils.getObject(taskParams, "columns");
        Object target_columns = MapUtils.getObject(taskParams, "target_columns");
        String writeMode = "insert";

        // Create job-related content config
        List<Map<String, Object>> content = new ArrayList<>();
        Map<String, Object> contentItem = new HashMap<>();

        // Reader config
        Map<String, Object> reader = new HashMap<>();
        reader.put("name", "txtfilereader");
        Map<String, Object> readerParameter = new HashMap<>();
        readerParameter.put("path", Arrays.asList(MapUtils.getString(taskParams, "csvFile")));
        readerParameter.put("encoding", "UTF-8");
        readerParameter.put("column", columns);
        readerParameter.put("fieldDelimiter", ",");
        reader.put("parameter", readerParameter);

        // Writer config
        Map<String, Object> writer = new HashMap<>();
        writer.put("name", writerProperty.trainToJdbcWriterName());
        Map<String, Object> writerParameter = new HashMap<>();
        writerParameter.put("username", writerProperty.getUsername());
        writerParameter.put("password", writerProperty.getPassword());
        writerParameter.put("batchSize", taskParams.getOrDefault("batchSize", 1024)); // Default 1024
        //
        writerParameter.put("writeMode", writeMode);
        writerParameter.put("column", target_columns);
        List<Map<String, Object>> writerConnection = new ArrayList<>();
        Map<String, Object> writerConnectionItem = new HashMap<>();
        writerConnectionItem.put("table", Arrays.asList(writerProperty.getDbNameTableName(target_table_name)));
        writerConnectionItem.put("jdbcUrl", writerProperty.trainToJdbcUrl());
        writerConnection.add(writerConnectionItem);
        writerParameter.put("connection", writerConnection);
        writer.put("parameter", writerParameter);

        // Add reader and writer to content
        contentItem.put("reader", reader);
        contentItem.put("writer", writer);
        content.add(contentItem);

        jobJson.put("content", content);
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("job", jobJson);
        // Convert to JSON string and return
        return JSON.toJSONString(objectObjectHashMap);
    }

    @Override
    public Map<String, Object> parse2(String nodeCode, Integer nodeVersion, TaskComponentTypeEnum componentType, Map<String, Object> taskParams, String resourceUrl, List<DsResource> resourceList) {
        // Reader config
        Map<String, Object> reader = new HashMap<>();
        reader.put("nodeCode", nodeCode);
        reader.put("nodeVersion", nodeVersion);
        reader.put("componentType", componentType.getCode());

        //Parameters
        Map<String, Object> parameter = new HashMap<>();
        reader.put("parameter", parameter);

        //Copy file to static resources
//        String path = resourceUrl + DateUtil.format(new Date(), "yyyyMMdd") + File.separator + nodeCode + ".csv";
//        String csvFile = (String) taskParams.get("csvFile");
//        FileUtil.copy(csvFile, path, true);

        parameter.put("path", taskParams.get("csvFile"));
        parameter.put("startColumn", taskParams.getOrDefault("startColumn", 1));
        parameter.put("startData", taskParams.getOrDefault("startData", 2));
        parameter.put("column", taskParams.get("columns"));
        return reader;
    }
}
