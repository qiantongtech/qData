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

package tech.qiantong.qdata.module.att.dal.mapper.sourceSystem;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 来源系统Mapper接口
 *
 * @author qdata
 * @date 2026-04-03
 */
public interface AttSourceSystemMapper extends BaseMapperX<AttSourceSystemDO> {


    default PageResult<AttSourceSystemDO> selectPage(AttSourceSystemPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapper<AttSourceSystemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttSourceSystemDO.class)
                .select("t2.NICK_NAME AS responsiblePersonName,t3.NICK_NAME AS contactPersonName")
                .leftJoin("SYSTEM_USER t2 on t.RESPONSIBLE_PERSON = t2.USER_ID AND t2.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t3 on t.CONTACT_PERSON = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()), AttSourceSystemDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getType()), AttSourceSystemDO::getType, reqVO.getType())
                .eq(reqVO.getValidFlag()!=null, AttSourceSystemDO::getValidFlag, reqVO.getValidFlag())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, AttSourceSystemDO.class, lambdaWrapper);
    }

    default AttSourceSystemDO selectById(Long id) {
        MPJLambdaWrapper<AttSourceSystemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttSourceSystemDO.class)
                .select("t2.NICK_NAME AS responsiblePersonName,t3.NICK_NAME AS contactPersonName")
                .leftJoin("SYSTEM_USER t2 on t.RESPONSIBLE_PERSON = t2.USER_ID AND t2.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t3 on t.CONTACT_PERSON = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .eq(AttSourceSystemDO::getId, id);
        return selectOne(lambdaWrapper);
    }

  /*  default PageResult<AttSourceSystemDO> selectPage(AttSourceSystemPageReqVO reqVO) {
        MPJLambdaWrapper<AttSourceSystemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttSourceSystemDO.class)
                .like(AttSourceSystemDO::getName, reqVO.getName())
                .eq(AttSourceSystemDO::getType, reqVO.getType())
                .eq(AttSourceSystemDO::getSortOrder, reqVO.getSortOrder())
                .eq(AttSourceSystemDO::getDescription, reqVO.getDescription())
                .eq(AttSourceSystemDO::getValidFlag, reqVO.getValidFlag())
                .eq(AttSourceSystemDO::getResponsiblePerson, reqVO.getResponsiblePerson())
                .eq(AttSourceSystemDO::getContactPerson, reqVO.getContactPerson())
                .eq(AttSourceSystemDO::getCreateTime, reqVO.getCreateTime());
        return selectJoinPage(reqVO, AttSourceSystemDO.class, lambdaWrapper);
    }*/
}
