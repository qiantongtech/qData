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

package tech.qiantong.qdata.quality.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tech.qiantong.qdata.quality.dal.dataobject.quality.CheckErrorData;

/**
 * <P>
 * Purpose:
 * </p>
 *
 * @author: FXB
 * @create: 2024-08-06 10:56
 **/
public interface CheckErrorDataRepository extends MongoRepository<CheckErrorData, String> {
    @Query("{ 'id': ?0}")
    CheckErrorData getById(String id);
}
