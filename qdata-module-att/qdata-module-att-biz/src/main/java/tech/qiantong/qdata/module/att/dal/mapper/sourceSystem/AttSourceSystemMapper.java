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
