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

package tech.qiantong.qdata.module.att.service.sourceSystem;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemRespVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 来源系统Service接口
 *
 * @author qdata
 * @date 2026-04-03
 */
public interface IAttSourceSystemService extends IService<AttSourceSystemDO> {

    /**
     * 获得来源系统分页列表
     *
     * @param pageReqVO 分页请求
     * @return 来源系统分页列表
     */
    PageResult<AttSourceSystemDO> getAttSourceSystemPage(AttSourceSystemPageReqVO pageReqVO);

    /**
     * 创建来源系统
     *
     * @param createReqVO 来源系统信息
     * @return 来源系统编号
     */
    Long createAttSourceSystem(AttSourceSystemSaveReqVO createReqVO);

    /**
     * 更新来源系统
     *
     * @param updateReqVO 来源系统信息
     */
    int updateAttSourceSystem(AttSourceSystemSaveReqVO updateReqVO);

    /**
     * 删除来源系统
     *
     * @param idList 来源系统编号
     */
    int removeAttSourceSystem(Collection<Long> idList);

    /**
     * 获得来源系统详情
     *
     * @param id 来源系统编号
     * @return 来源系统
     */
    AttSourceSystemDO getAttSourceSystemById(Long id);

    /**
     * 获得全部来源系统列表
     *
     * @return 来源系统列表
     */
    List<AttSourceSystemDO> getAttSourceSystemList();

    /**
     * 获得全部来源系统列表(带状态)
     *
     * @return 来源系统列表
     */
    public List<AttSourceSystemDO> getAttSourceSystemListByValidFlag(Boolean validFlag);

    /**
     * 获得全部来源系统 Map
     *
     * @return 来源系统 Map
     */
    Map<Long, AttSourceSystemDO> getAttSourceSystemMap();


    /**
     * 导入来源系统数据
     *
     * @param importExcelList 来源系统数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttSourceSystem(List<AttSourceSystemRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
