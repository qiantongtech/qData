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
 * 数据发现任务Mapper接口
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface DaDiscoveryTaskMapper extends BaseMapperX<DaDiscoveryTaskDO> {

    default PageResult<DaDiscoveryTaskDO> selectPage(DaDiscoveryTaskPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
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
     * 将老的 CAT_CODE 批量更新成新的 CAT_CODE
     *
     * @param oldCatCode 旧分类编码
     * @param newCatCode 新分类编码
     * @return 受影响行数
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
