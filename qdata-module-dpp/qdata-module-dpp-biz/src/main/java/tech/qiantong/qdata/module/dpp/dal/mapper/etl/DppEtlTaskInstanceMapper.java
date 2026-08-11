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

package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceTreeListReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceTreeListRespVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data Integration Task Instance Mapper
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlTaskInstanceMapper extends BaseMapperX<DppEtlTaskInstanceDO> {

    default PageResult<DppEtlTaskInstanceDO> selectPage(DppEtlTaskInstancePageReqVO reqVO) {

        MPJLambdaWrapper<DppEtlTaskInstanceDO> lambdaWrapper = new MPJLambdaWrapper();


        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        lambdaWrapper.selectAll(DppEtlTaskInstanceDO.class)
                .select("t3.NICK_NAME AS personChargeName")
                .leftJoin("SYSTEM_USER t3 ON t.PERSON_CHARGE = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()),DppEtlTaskInstanceDO::getName, reqVO.getName())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DppEtlTaskInstanceDO::getCatCode, reqVO.getCatCode())
                .eq(StringUtils.isNotBlank(reqVO.getTaskType()),DppEtlTaskInstanceDO::getTaskType, reqVO.getTaskType())
                .eq(reqVO.getTaskId() !=null,DppEtlTaskInstanceDO::getTaskId, reqVO.getTaskId())
                .eq(StringUtils.isNotBlank(reqVO.getTaskCode()),DppEtlTaskInstanceDO::getTaskCode, reqVO.getTaskCode())
                .eq(reqVO.getTaskVersion() !=null,DppEtlTaskInstanceDO::getTaskVersion, reqVO.getTaskVersion())
                .eq(StringUtils.isNotBlank(reqVO.getPersonCharge()),DppEtlTaskInstanceDO::getPersonCharge, reqVO.getPersonCharge())
                .eq(reqVO.getProjectId() !=null,DppEtlTaskInstanceDO::getProjectId, reqVO.getProjectId())
                .eq(StringUtils.isNotBlank(reqVO.getProjectCode()),DppEtlTaskInstanceDO::getProjectCode, reqVO.getProjectCode())
                .ge(reqVO.getStartTime() != null,DppEtlTaskInstanceDO::getStartTime, reqVO.getStartTime())
                .le(reqVO.getEndTime() != null,DppEtlTaskInstanceDO::getStartTime, reqVO.getEndTime())
                .eq(StringUtils.isNotBlank(reqVO.getCommandType()),DppEtlTaskInstanceDO::getCommandType, reqVO.getCommandType())
                .eq(reqVO.getMaxTryTimes() !=null,DppEtlTaskInstanceDO::getMaxTryTimes, reqVO.getMaxTryTimes())
                .eq(StringUtils.isNotBlank(reqVO.getFailureStrategy()),DppEtlTaskInstanceDO::getFailureStrategy, reqVO.getFailureStrategy())
                .eq(StringUtils.isNotBlank(reqVO.getSubTaskFlag()),DppEtlTaskInstanceDO::getSubTaskFlag, reqVO.getSubTaskFlag())
                .in(StringUtils.equals(reqVO.getStatus(), "running"), DppEtlTaskInstanceDO::getStatus, "0", "1", "12")
                .eq(StringUtils.equals(reqVO.getStatus(), "success"), DppEtlTaskInstanceDO::getStatus, "7")
                .eq(StringUtils.equals(reqVO.getStatus(), "failed"), DppEtlTaskInstanceDO::getStatus, "6")
                .eq(StringUtils.isNotBlank(reqVO.getStatus())
                                && !StringUtils.equals(reqVO.getStatus(), "running")
                                && !StringUtils.equals(reqVO.getStatus(), "success")
                                && !StringUtils.equals(reqVO.getStatus(), "failed"),
                        DppEtlTaskInstanceDO::getStatus, reqVO.getStatus())
                .eq(reqVO.getDsId() !=null,DppEtlTaskInstanceDO::getDsId, reqVO.getDsId())
                .eq(reqVO.getCreateTime() !=null,DppEtlTaskInstanceDO::getCreateTime, reqVO.getCreateTime())

                .in(DppEtlTaskInstanceDO::getStatus, "0", "1", "5", "6", "7", "12")
                .orderByDesc(DppEtlNodeInstanceDO::getStartTime);


        // Build dynamic query conditions
        return selectJoinPage(reqVO, DppEtlTaskInstanceDO.class, lambdaWrapper);
    }

    DppEtlTaskInstanceDO selectOneNew(@Param("reqVO") DppEtlTaskInstanceDO reqVO);

    /**
     * Query node instance list by task instance ID
     *
     * @param taskInstanceId
     * @return
     */
    List<DppEtlTaskInstanceTreeListRespVO> nodeListByTaskInstanceId(@Param("taskInstanceId") Long taskInstanceId);

    IPage<DppEtlTaskInstanceTreeListRespVO> treeList(Page page, @Param("params") DppEtlTaskInstanceTreeListReqVO params);

    /**
     * Get all node instances under sub-task
     *
     * @param taskInstanceId
     * @param nodeInstanceId
     * @return
     */
    List<DppEtlTaskInstanceTreeListRespVO> listSubNodeInstance(@Param("taskInstanceId") Long taskInstanceId, @Param("nodeInstanceId") Long nodeInstanceId);

    /**
     * Get the latest task instance
     *
     * @param taskIdList
     * @return
     */
    List<DppEtlTaskInstanceDO> getLastTaskInstance(@Param("taskIdList") List<Long> taskIdList);
}
