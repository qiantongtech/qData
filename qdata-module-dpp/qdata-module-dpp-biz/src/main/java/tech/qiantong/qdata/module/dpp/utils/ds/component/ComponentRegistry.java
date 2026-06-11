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
 */

package tech.qiantong.qdata.module.dpp.utils.ds.component;

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:注册任务组件
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-12 17:19
 **/
public class ComponentRegistry {

    private final Map<String, ComponentItem> componentItemMap = new HashMap<>();

    public ComponentRegistry() {
        this.componentItemMap.put(TaskComponentTypeEnum.DB_READER.getCode(), new DBReaderComponent());

        this.componentItemMap.put(TaskComponentTypeEnum.EXCEL_READER.getCode(), new ExcelReaderComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.CSV_READER.getCode(), new CsvReaderComponent());


        this.componentItemMap.put(TaskComponentTypeEnum.SPARK_CLEAN.getCode(), new SparkCleanComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.SORT_RECORD.getCode(), new SortTransitionComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.FIELD_DERIVATION.getCode(), new FieldDerivationTransitionComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.DATA_DEDUPLICATION.getCode(), new DataDeduplicationTransitionComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.VALUE_MAP.getCode(), new ValueMapTransitionComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.ADD_CONSTANT.getCode(), new AddConstantTransitionComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.SELECT_FIELDS.getCode(), new SelectFieldsTransitionComponent());


        this.componentItemMap.put(TaskComponentTypeEnum.DB_WRITER.getCode(), new DBWriterComponent());



        this.componentItemMap.put(TaskComponentTypeEnum.SQL_DEV.getCode(), new SQLComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.PROCEDURE_DEV.getCode(), new ProcedureComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.SUB_PROCESS.getCode(), new SubProcessComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.SPARK_SQL_DEV.getCode(), new SparkSQLComponent());
        this.componentItemMap.put(TaskComponentTypeEnum.SHELL_DEV.getCode(), new ShellComponent());
    }

    public ComponentItem getComponentItem(String code) {
        return this.componentItemMap.get(code);
    }
}
