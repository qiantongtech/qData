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
