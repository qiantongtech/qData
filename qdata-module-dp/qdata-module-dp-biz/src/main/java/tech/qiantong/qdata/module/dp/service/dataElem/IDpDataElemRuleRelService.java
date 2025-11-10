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

package tech.qiantong.qdata.module.dp.service.dataElem;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemRuleRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据元数据规则关联信息Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpDataElemRuleRelService extends IService<DpDataElemRuleRelDO> {

    /**
     * 获得数据元数据规则关联信息分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据元数据规则关联信息分页列表
     */
    PageResult<DpDataElemRuleRelDO> getDpDataElemRuleRelPage(DpDataElemRuleRelPageReqVO pageReqVO);

    /**
     * 创建数据元数据规则关联信息
     *
     * @param createReqVO 数据元数据规则关联信息信息
     * @return 数据元数据规则关联信息编号
     */
    Long createDpDataElemRuleRel(DpDataElemRuleRelSaveReqVO createReqVO);

    /**
     * 更新数据元数据规则关联信息
     *
     * @param updateReqVO 数据元数据规则关联信息信息
     */
    int updateDpDataElemRuleRel(DpDataElemRuleRelSaveReqVO updateReqVO);

    /**
     * 删除数据元数据规则关联信息
     *
     * @param idList 数据元数据规则关联信息编号
     */
    int removeDpDataElemRuleRel(Collection<Long> idList);

    /**
     * 获得数据元数据规则关联信息详情
     *
     * @param id 数据元数据规则关联信息编号
     * @return 数据元数据规则关联信息
     */
    DpDataElemRuleRelDO getDpDataElemRuleRelById(Long id);

    /**
     * 获得全部数据元数据规则关联信息列表
     *
     * @return 数据元数据规则关联信息列表
     */
    List<DpDataElemRuleRelDO> getDpDataElemRuleRelList();

    /**
     * 获得全部数据元数据规则关联信息 Map
     *
     * @return 数据元数据规则关联信息 Map
     */
    Map<Long, DpDataElemRuleRelDO> getDpDataElemRuleRelMap();


    /**
     * 导入数据元数据规则关联信息数据
     *
     * @param importExcelList 数据元数据规则关联信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDpDataElemRuleRel(List<DpDataElemRuleRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
