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

package tech.qiantong.qdata.module.att.dal.mapper.project;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;
import tech.qiantong.qdata.mybatis.config.MasterDataSourceConfig;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * Project-User Relationship Mapper Interface
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface AttProjectUserRelMapper extends BaseMapperX<AttProjectUserRelDO> {

    default PageResult<AttProjectUserRelDO> selectPage(AttProjectUserRelPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        MPJLambdaWrapper<AttProjectUserRelDO> lambdaWrapper = new MPJLambdaWrapper();
        String sql = "";
        if(StringUtils.equals("mysql", MasterDataSourceConfig.getDatabaseType())){
            sql = "(SELECT GROUP_CONCAT(r.ROLE_NAME) AS roleStr FROM system_role r JOIN system_user_role ur on r.role_id = ur.role_id WHERE u.user_id = ur.user_id" +
                    " and r.PROJECT_ID = '"+reqVO.getProjectId() +"'"+
                    ") AS roleStr";
        }else if(StringUtils.equals("dm8",MasterDataSourceConfig.getDatabaseType())){
            sql = "(SELECT WM_CONCAT(r.ROLE_NAME) AS roleStr FROM system_role r JOIN system_user_role ur on r.role_id = ur.role_id WHERE u.user_id = ur.user_id" +
                    " and r.PROJECT_ID = '"+reqVO.getProjectId() +"'"+
                    ") AS roleStr";
        }
        lambdaWrapper.selectAll(AttProjectUserRelDO.class)
                .select(sql)
                .select("u.nick_name AS nickName, u.user_name AS userName , u.phonenumber AS phoneNumber, u.status AS status ,d.dept_name AS deptName")
                .innerJoin("SYSTEM_USER u on t.user_id = u.user_id AND u.del_flag = 0")
                .leftJoin("SYSTEM_DEPT d on u.dept_id = d.dept_id AND d.del_flag = 0")
                .eq(reqVO.getProjectId() != null, AttProjectUserRelDO::getProjectId, reqVO.getProjectId())
                .eq(reqVO.getUserId() != null, AttProjectUserRelDO::getUserId, reqVO.getUserId())
                .like(StringUtils.isNotBlank(reqVO.getUserName()), "u.user_name", reqVO.getUserName())
                .like(StringUtils.isNotBlank(reqVO.getNickName()), "u.nick_name", reqVO.getNickName())
                .like(StringUtils.isNotBlank(reqVO.getPhoneNumber()), "u.phonenumber", reqVO.getPhoneNumber())
                .between(reqVO.getStartTime() != null && reqVO.getEndTime() != null,
                        AttProjectUserRelDO::getCreateTime, reqVO.getStartTime(), reqVO.getEndTime())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()),
                        StringUtils.isNotBlank(reqVO.getOrderByColumn())
                                ? Arrays.asList(reqVO.getOrderByColumn().split(","))
                                : null);

        return selectJoinPage(reqVO, AttProjectUserRelDO.class, lambdaWrapper);
    }
}
