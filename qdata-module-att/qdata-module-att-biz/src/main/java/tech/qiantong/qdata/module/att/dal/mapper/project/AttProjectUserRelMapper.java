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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.att.dal.mapper.project;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * 项目与用户关联关系Mapper接口
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface AttProjectUserRelMapper extends BaseMapperX<AttProjectUserRelDO> {

    default PageResult<AttProjectUserRelDO> selectPage(AttProjectUserRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        MPJLambdaWrapper<AttProjectUserRelDO> lambdaWrapper = new MPJLambdaWrapper();

        lambdaWrapper.selectAll(AttProjectUserRelDO.class)
                .select("u.nick_name AS nickName, u.user_name AS userName , u.phonenumber AS phoneNumber, u.status AS status ,d.dept_name AS deptName")
                .leftJoin("SYSTEM_USER u on t.user_id = u.user_id")
                .leftJoin("SYSTEM_DEPT d on u.dept_id = d.dept_id")
                .eq("u.del_flag","0")
                .eq("d.del_flag","0")
                .eq("t.del_flag","0")
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
