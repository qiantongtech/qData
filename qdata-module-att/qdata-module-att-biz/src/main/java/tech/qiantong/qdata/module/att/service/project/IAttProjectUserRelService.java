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

package tech.qiantong.qdata.module.att.service.project;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 项目与用户关联关系Service接口
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface IAttProjectUserRelService extends IService<AttProjectUserRelDO> {

    /**
     * 获得项目与用户关联关系分页列表
     *
     * @param pageReqVO 分页请求
     * @return 项目与用户关联关系分页列表
     */
    PageResult<AttProjectUserRelDO> getAttProjectUserRelPage(AttProjectUserRelPageReqVO pageReqVO);

    /**
     * 创建项目与用户关联关系
     *
     * @param createReqVO 项目与用户关联关系信息
     * @return 项目与用户关联关系编号
     */
    Long createAttProjectUserRel(AttProjectUserRelSaveReqVO createReqVO);

    /**
     * 更新项目与用户关联关系
     *
     * @param updateReqVO 项目与用户关联关系信息
     */
    int updateAttProjectUserRel(AttProjectUserRelSaveReqVO updateReqVO);

    /**
     * 更新项目与用户关联关系
     *
     * @param updateReqVO 项目与用户关联关系信息
     */
    int updateUserListAndRoleList(AttProjectUserRelSaveReqVO updateReqVO);

    /**
     * 删除项目与用户关联关系
     *
     * @param idList 项目与用户关联关系编号
     */
    int removeAttProjectUserRel(Collection<Long> idList);

    /**
     * 获得项目与用户关联关系详情
     *
     * @param id 项目与用户关联关系编号
     * @return 项目与用户关联关系
     */
    AttProjectUserRelDO getAttProjectUserRelById(Long id);

    /**
     * 获得全部项目与用户关联关系列表
     *
     * @return 项目与用户关联关系列表
     */
    List<AttProjectUserRelDO> getAttProjectUserRelList();

    /**
     * 获得全部项目与用户关联关系 Map
     *
     * @return 项目与用户关联关系 Map
     */
    Map<Long, AttProjectUserRelDO> getAttProjectUserRelMap();


    /**
     * 导入项目与用户关联关系数据
     *
     * @param importExcelList 项目与用户关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importAttProjectUserRel(List<AttProjectUserRelRespVO> importExcelList, boolean isUpdateSupport, String operName);


    /**
     * 创建项目与用户关联关系前端传用户集合和角色集合
     *
     * @param attProject 项目信息
     * @return 项目编号
     */
    Boolean createUserListAndRoleList(AttProjectUserRelSaveReqVO attProject);

    /**
     * 获取项目与用户关联关系详细信息包括角色信息
     *
     * @param id
     * @return
     */
    AttProjectUserRelRespVO getRoleUser(Long id);
}
