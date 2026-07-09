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

package tech.qiantong.qdata.module.da.dal.mapper.discovery;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Discovery Task Mapper Interface
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface DaDiscoveryTaskMapper extends BaseMapperX<DaDiscoveryTaskDO> {

    default PageResult<DaDiscoveryTaskDO> selectPage(DaDiscoveryTaskPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DaDiscoveryTaskDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaDiscoveryTaskDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_DISCOVER_TASK_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()), DaDiscoveryTaskDO::getName, reqVO.getName())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DaDiscoveryTaskDO::getCatCode, reqVO.getCatCode())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DaDiscoveryTaskDO::getStatus, reqVO.getStatus())
                .eq(reqVO.getContactId() != null, DaDiscoveryTaskDO::getContactId, reqVO.getContactId())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        return selectJoinPage(reqVO, DaDiscoveryTaskDO.class, lambdaWrapper);

    }

    /**
     * Batch update old CAT_CODE to new CAT_CODE
     *
     * @param oldCatCode Old category code
     * @param newCatCode New category code
     * @return Number of affected rows
     */
    default int updateCatCode(String oldCatCode, String newCatCode) {
        return this.update(
                null,
                Wrappers.<DaDiscoveryTaskDO>lambdaUpdate()
                        .set(DaDiscoveryTaskDO::getCatCode, newCatCode)
                        .eq(DaDiscoveryTaskDO::getDelFlag, "0")
                        .eq(DaDiscoveryTaskDO::getCatCode, oldCatCode)
        );
    }
}
