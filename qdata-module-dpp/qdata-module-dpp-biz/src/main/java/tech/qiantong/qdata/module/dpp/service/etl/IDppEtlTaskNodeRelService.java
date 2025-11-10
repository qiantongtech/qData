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

package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据集成任务节点关系Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskNodeRelService extends IService<DppEtlTaskNodeRelDO> {

    /**
     * 获得数据集成任务节点关系分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成任务节点关系分页列表
     */
    PageResult<DppEtlTaskNodeRelDO> getDppEtlTaskNodeRelPage(DppEtlTaskNodeRelPageReqVO pageReqVO);
    List<DppEtlTaskNodeRelRespVO> getDppEtlTaskNodeRelRespVOList(DppEtlTaskNodeRelPageReqVO pageReqVO);

    /**
     * 创建数据集成任务节点关系
     *
     * @param createReqVO 数据集成任务节点关系信息
     * @return 数据集成任务节点关系编号
     */
    Long createDppEtlTaskNodeRel(DppEtlTaskNodeRelSaveReqVO createReqVO);

    void createDppEtlTaskNodeRelBatch(List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS);

    /**
     * 更新数据集成任务节点关系
     *
     * @param updateReqVO 数据集成任务节点关系信息
     */
    int updateDppEtlTaskNodeRel(DppEtlTaskNodeRelSaveReqVO updateReqVO);

    /**
     * 删除数据集成任务节点关系
     *
     * @param idList 数据集成任务节点关系编号
     */
    int removeDppEtlTaskNodeRel(Collection<Long> idList);

    /**
     * 获得数据集成任务节点关系详情
     *
     * @param id 数据集成任务节点关系编号
     * @return 数据集成任务节点关系
     */
    DppEtlTaskNodeRelDO getDppEtlTaskNodeRelById(Long id);

    /**
     * 获得全部数据集成任务节点关系列表
     *
     * @return 数据集成任务节点关系列表
     */
    List<DppEtlTaskNodeRelDO> getDppEtlTaskNodeRelList();

    /**
     * 获得全部数据集成任务节点关系 Map
     *
     * @return 数据集成任务节点关系 Map
     */
    Map<Long, DppEtlTaskNodeRelDO> getDppEtlTaskNodeRelMap();


    /**
     * 导入数据集成任务节点关系数据
     *
     * @param importExcelList 数据集成任务节点关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppEtlTaskNodeRel(List<DppEtlTaskNodeRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<DppEtlTaskNodeRelRespVO> removeOldDppEtlTaskNodeRel(String code);
}
