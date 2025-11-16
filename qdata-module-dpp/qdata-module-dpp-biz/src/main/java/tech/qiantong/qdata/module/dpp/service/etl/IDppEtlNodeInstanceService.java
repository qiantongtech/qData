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

package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成节点实例Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlNodeInstanceService extends IService<DppEtlNodeInstanceDO> {

    /**
     * 获得数据集成节点实例分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成节点实例分页列表
     */
    PageResult<DppEtlNodeInstanceDO> getDppEtlNodeInstancePage(DppEtlNodeInstancePageReqVO pageReqVO);

    /**
     * 创建数据集成节点实例
     *
     * @param createReqVO 数据集成节点实例信息
     * @return 数据集成节点实例编号
     */
    Long createDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO createReqVO);

    /**
     * 更新数据集成节点实例
     *
     * @param updateReqVO 数据集成节点实例信息
     */
    int updateDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO updateReqVO);

    /**
     * 删除数据集成节点实例
     *
     * @param idList 数据集成节点实例编号
     */
    int removeDppEtlNodeInstance(Collection<Long> idList);

    /**
     * 获得数据集成节点实例详情
     *
     * @param id 数据集成节点实例编号
     * @return 数据集成节点实例
     */
    DppEtlNodeInstanceDO getDppEtlNodeInstanceById(Long id);

    /**
     * 获得全部数据集成节点实例列表
     *
     * @return 数据集成节点实例列表
     */
    List<DppEtlNodeInstanceDO> getDppEtlNodeInstanceList();

    /**
     * 获得全部数据集成节点实例 Map
     *
     * @return 数据集成节点实例 Map
     */
    Map<Long, DppEtlNodeInstanceDO> getDppEtlNodeInstanceMap();


    /**
     * 导入数据集成节点实例数据
     *
     * @param importExcelList 数据集成节点实例数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDppEtlNodeInstance(List<DppEtlNodeInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 创建任务节点实例
     *
     * @param taskInstance
     * @return
     */
    Boolean createNodeInstance(TaskInstance taskInstance);

    /**
     * 更细任务节点实例
     *
     * @param taskInstance
     * @return
     */
    Boolean updateNodeInstance(TaskInstance taskInstance);

    /**
     * 根据dsId获取数据
     *
     * @param dsId
     * @return
     */
    DppEtlNodeInstanceDO getByDsId(Long dsId);

    /**
     * 节点实例日志的处理
     *
     * @param taskInstanceId
     * @param processInstanceId
     * @param logStr
     */
    void taskInstanceLogInsert(String taskInstanceId, String processInstanceId, String logStr);

    String getLogByNodeInstanceId(Long nodeInstanceId);
}
