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
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlNodeRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成节点Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlNodeService extends IService<DppEtlNodeDO> {

    /**
     * 获得数据集成节点分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成节点分页列表
     */
    PageResult<DppEtlNodeDO> getDppEtlNodePage(DppEtlNodePageReqVO pageReqVO);

    List<DppEtlNodeRespVO> getDppEtlNodeRespList(DppEtlNodePageReqVO pageReqVO);

    /**
     * 通过任务id获取节点列表
     *
     * @param taskId
     * @return
     */
    List<DppEtlNodeRespVO> listNodeByTaskId(Long taskId);

    DppEtlNodeRespVO getDppEtlNodeRespVOByReqVO(DppEtlNodePageReqVO reqVOPre);

    /**
     * 创建数据集成节点
     *
     * @param createReqVO 数据集成节点信息
     * @return 数据集成节点编号
     */
    Long createDppEtlNode(DppEtlNodeSaveReqVO createReqVO);

    List<DppEtlNodeDO> createDppEtlNodeBatch(List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList);

    /**
     * 更新数据集成节点
     *
     * @param updateReqVO 数据集成节点信息
     */
    int updateDppEtlNode(DppEtlNodeSaveReqVO updateReqVO);

    /**
     * 删除数据集成节点
     *
     * @param idList 数据集成节点编号
     */
    int removeDppEtlNode(Collection<Long> idList);

    /**
     * 获得数据集成节点详情
     *
     * @param id 数据集成节点编号
     * @return 数据集成节点
     */
    DppEtlNodeDO getDppEtlNodeById(Long id);

    /**
     * 获得全部数据集成节点列表
     *
     * @return 数据集成节点列表
     */
    List<DppEtlNodeDO> getDppEtlNodeList();

    /**
     * 获得全部数据集成节点 Map
     *
     * @return 数据集成节点 Map
     */
    Map<Long, DppEtlNodeDO> getDppEtlNodeMap();


    /**
     * 导入数据集成节点数据
     *
     * @param importExcelList 数据集成节点数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDppEtlNode(List<DppEtlNodeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void removeOldDppEtlNode(List<String> code);

    /**
     * 通过节点编码获取节点id
     *
     * @param nodeCode
     * @return
     */
    Long getNodeIdByNodeCode(String nodeCode);

    /**
     * 通过节点编码获取节点信息
     *
     * @param nodeCode
     * @return
     */
    DppEtlNodeRespDTO getNodeByNodeCode(String nodeCode);
}
