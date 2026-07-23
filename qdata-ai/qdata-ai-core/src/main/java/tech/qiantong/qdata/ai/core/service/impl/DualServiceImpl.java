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

package tech.qiantong.qdata.ai.core.service.impl;

import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlExecutorService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlNodeService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlSchedulerService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlTaskService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.api.Rel.dto.AttTagAssetRelReqDTO;
import tech.qiantong.qdata.module.att.api.Rel.dto.AttTagAssetRelRespDTO;
import tech.qiantong.qdata.module.att.api.Tag.dto.AttTagRespDTO;
import tech.qiantong.qdata.module.att.api.project.IAttProjectApi;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.tag.IAttTagApiService;
import tech.qiantong.qdata.module.att.api.service.cat.tagRel.IAttTagAssetRelApiService;
import tech.qiantong.qdata.module.att.api.sourceSystem.dto.AttSourceSystemRespDTO;
import tech.qiantong.qdata.module.att.api.sourceSystem.service.IAttSourceSystemApiService;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelRespDTO;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemRuleRelService;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceRespDTO;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskInstanceService;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <P>
 * Purpose: Pseudo implementation class
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-09 15:38
 **/
@Service
public class DualServiceImpl implements IDsEtlExecutorService, IDsEtlNodeService, IDsEtlSchedulerService, IDsEtlTaskService, IAttProjectApi, IDataElemRuleRelService, IDpModelApiService, DppEtlTaskService, IAttTagAssetRelApiService, DppEtlTaskInstanceService, IAttTagApiService, IAttSourceSystemApiService {
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
        return null;
    }

    @Override
    public PageResult<AttProjectRespDTO> getAttProjectPage(AttProjectReqDTO attProjectReqDTO) {
        return null;
    }

    @Override
    public List<DpDataElemRuleRelRespDTO> listByDataElemIdList(Collection<Long> collection, String s) {
        return null;
    }

    @Override
    public DpModelRespDTO getDpModelByIdApi(Long aLong) {
        return null;
    }

    @Override
    public List<DpModelColumnRespDTO> getDpModelColumnListByModelIdApi(Long aLong) {
        return null;
    }

    @Override
    public List<DpDataElemRespDTO> getDpDataElemListByIdsApi(Set<Long> set) {
        return null;
    }

    @Override
    public Set<Long> getDpDataElemListByAssetIdApi(Long aLong) {
        return null;
    }

    @Override
    public List<DpDataElemAssetRelRespDTO> getDpDataElemListByColumnIdInApi(Collection<Long> collection) {
        return null;
    }

    @Override
    public Set<Long> getDpDataElemListByAssetIdAndColumnId(Long aLong, Long aLong1) {
        return null;
    }

    @Override
    public boolean insertElementAssetRelation(List<DpDataElemAssetRelReqDTO> list) {
        return false;
    }

    @Override
    public Long getCountByCatCode(String s) {
        return null;
    }

    @Override
    public boolean updateElementAssetRelation(DpDataElemAssetRelReqDTO dpDataElemAssetRelReqDTO) {
        return false;
    }

    @Override
    public List<DpDataElemRespDTO> getDpDataElemListByAssetId(Long aLong, Set<Long> set) {
        return null;
    }

    @Override
    public List<DpModelColumnRespDTO> getModelIdColumnList(Long aLong) {
        return null;
    }

    @Override
    public int updateCatCode(String s, String s1) {
        return 0;
    }

    @Override
    public int checkTaskIdInDatasource(List<Long> list, List<Long> list1) {
        return 0;
    }

    @Override
    public int checkTaskIdInAsset(List<Long> list) {
        return 0;
    }

    @Override
    public long getCountByCatCode(String s, List<String> list) {
        return 0;
    }

    @Override
    public long getCountByCatId(Long aLong, List<String> list) {
        return 0;
    }

    @Override
    public List<AttTagAssetRelRespDTO> getApiList(AttTagAssetRelReqDTO attTagAssetRelReqDTO) {
        return null;
    }

    @Override
    public void deleteRelByUpdateTag(Long aLong) {

    }

    @Override
    public List<DppEtlTaskInstanceRespDTO> getLastTaskInstance(List<Long> list) {
        return null;
    }

    @Override
    public List<AttTagRespDTO> getApiList() {
        return null;
    }

    @Override
    public List<AttSourceSystemRespDTO> getValidSourceSystems() {
        return List.of();
    }
}
