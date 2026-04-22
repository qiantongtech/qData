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

package tech.qiantong.qdata.ai.core.service.impl;

import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlExecutorService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlNodeService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlSchedulerService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlTaskService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.api.project.IAttProjectApi;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelRespDTO;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemRuleRelService;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;
import tech.qiantong.qdata.module.dpp.api.etl.dto.*;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <P>
 * 用途:伪实现类
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-09 15:38
 **/
@Service
public class DualServiceImpl implements IDsEtlExecutorService, IDsEtlNodeService, IDsEtlSchedulerService, IDsEtlTaskService,
        IAttProjectApi, IDataElemRuleRelService, IDpModelApiService, DppEtlTaskService {
    @Override
    public DsStatusRespDTO execute(DSExecuteDTO dsExecuteDTO, String s) {
        return null;
    }

    @Override
    public DsNodeGenCodeRespDTO genCode(Long aLong) {
        return null;
    }

    @Override
    public DsSchedulerRespDTO saveScheduler(DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO, String s) {
        return null;
    }

    @Override
    public DsSchedulerRespDTO updateScheduler(DsSchedulerUpdateReqDTO dsSchedulerUpdateReqDTO, String s) {
        return null;
    }

    @Override
    public DsStatusRespDTO onlineScheduler(String s, Long aLong) {
        return null;
    }

    @Override
    public DsStatusRespDTO offlineScheduler(String s, Long aLong) {
        return null;
    }

    @Override
    public DsSchedulerRespDTO getByTaskCode(String s, String s1) {
        return null;
    }

    @Override
    public DsTaskSaveRespDTO createTask(DsTaskSaveReqDTO dsTaskSaveReqDTO, Long aLong) {
        return null;
    }

    @Override
    public DsTaskSaveRespDTO updateTask(DsTaskSaveReqDTO dsTaskSaveReqDTO, String s, String s1) {
        return null;
    }

    @Override
    public DsStatusRespDTO releaseTask(String s, String s1, String s2) {
        return null;
    }

    @Override
    public DsStatusRespDTO deleteTask(String s, String s1) {
        return null;
    }

    @Override
    public DsStatusRespDTO startTask(DsStartTaskReqDTO dsStartTaskReqDTO, String s) {
        return null;
    }

    @Override
    public DsTaskSaveRespDTO batchCopy(String s, String s1) {
        return null;
    }

    @Override
    public Long getProjectIdByProjectCode(String s) {
        return 0L;
    }

    @Override
    public PageResult<AttProjectRespDTO> getAttProjectPage(AttProjectReqDTO attProjectReqDTO) {
        return null;
    }

    @Override
    public List<DpDataElemRuleRelRespDTO> listByDataElemIdList(Collection<Long> collection, String s) {
        return List.of();
    }

    @Override
    public DpModelRespDTO getDpModelByIdApi(Long aLong) {
        return null;
    }

    @Override
    public List<DpModelColumnRespDTO> getDpModelColumnListByModelIdApi(Long aLong) {
        return List.of();
    }

    @Override
    public List<DpDataElemRespDTO> getDpDataElemListByIdsApi(Set<Long> set) {
        return List.of();
    }

    @Override
    public Set<Long> getDpDataElemListByAssetIdApi(Long aLong) {
        return Set.of();
    }

    @Override
    public List<DpDataElemAssetRelRespDTO> getDpDataElemListByColumnIdInApi(Collection<Long> collection) {
        return List.of();
    }

    @Override
    public Set<Long> getDpDataElemListByAssetIdAndColumnId(Long aLong, Long aLong1) {
        return Set.of();
    }

    @Override
    public boolean insertElementAssetRelation(List<DpDataElemAssetRelReqDTO> list) {
        return false;
    }

    @Override
    public Long getCountByCatCode(String s) {
        return 0L;
    }

    @Override
    public boolean updateElementAssetRelation(DpDataElemAssetRelReqDTO dpDataElemAssetRelReqDTO) {
        return false;
    }

    @Override
    public List<DpDataElemRespDTO> getDpDataElemListByAssetId(Long aLong, Set<Long> set) {
        return List.of();
    }

    @Override
    public List<DpModelColumnRespDTO> getModelIdColumnList(Long aLong) {
        return List.of();
    }

    @Override
    public int checkTaskIdInDatasource(List<Long> list, List<Long> list1) {
        return 0;
    }

    @Override
    public int checkTaskIdInAsset(List<Long> list) {
        return 0;
    }
}
