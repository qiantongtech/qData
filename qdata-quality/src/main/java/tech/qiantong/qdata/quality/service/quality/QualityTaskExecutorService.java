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

package tech.qiantong.qdata.quality.service.quality;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import tech.qiantong.qdata.quality.controller.quality.vo.CheckErrorDataReqDTO;
import tech.qiantong.qdata.quality.controller.quality.vo.QualityRuleQueryReqDTO;
import tech.qiantong.qdata.quality.controller.quality.vo.ValidationSqlResult;
import tech.qiantong.qdata.quality.dal.dataobject.quality.CheckErrorData;

public interface QualityTaskExecutorService {
    public void executeTask(String taskId);


    public ValidationSqlResult generateValidationValidDataSql(QualityRuleQueryReqDTO queryReqDTO);

    public ValidationSqlResult generateValidationErrorDataSql(QualityRuleQueryReqDTO queryReqDTO);

    /**
     * 错误数据分页查询
     *
     * @param of
     * @return
     */
    Page<CheckErrorData> pageErrorData(PageRequest of, CheckErrorDataReqDTO checkErrorDataReqDTO);

    boolean updateErrorData( CheckErrorDataReqDTO checkErrorDataReqDTO);

    String generateDataCheck(QualityRuleQueryReqDTO queryReqDTO);
}
