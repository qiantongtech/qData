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
