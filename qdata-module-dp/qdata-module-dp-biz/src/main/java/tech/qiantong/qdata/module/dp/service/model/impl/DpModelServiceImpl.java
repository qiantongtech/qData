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

package tech.qiantong.qdata.module.dp.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.domain.TreeData;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;
import tech.qiantong.qdata.module.dm.api.service.businessCategory.IDmBusinessCategoryApiService;
import tech.qiantong.qdata.module.dm.api.service.themeDomain.IDmThemeDomainApiService;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelRespDTO;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemAssetRelDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;
import tech.qiantong.qdata.module.dp.dal.mapper.model.DpModelMapper;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemAssetRelService;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemService;
import tech.qiantong.qdata.module.dp.service.document.IDpDocumentService;
import tech.qiantong.qdata.module.dp.service.model.IDpModelColumnService;
import tech.qiantong.qdata.module.dp.service.model.IDpModelService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 逻辑模型Service业务层处理
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpModelServiceImpl extends ServiceImpl<DpModelMapper, DpModelDO> implements IDpModelService, IDpModelApiService {
    @Resource
    private DpModelMapper dpModelMapper;
    @Resource
    private IDpModelColumnService iDpModelColumnService;
    @Resource
    private IDpDataElemService iDpDataElemService;

    @Resource
    private IDpDataElemAssetRelService iDpDataElemAssetRelService;
    @Resource
    private IDaDatasourceApiService daDatasourceApiService;

    @Resource
    private IDpDocumentService dpDocumentService;

    @Resource
    private IDmThemeDomainApiService dmThemeDomainApiService;

    @Resource
    private IDmBusinessCategoryApiService dmBusinessCategoryApiService;


    /**
     * 根据资产id和代码表id查询数据元信息
     *
     * @param assetId 资产id
     * @param codeId  代码表id
     * @return
     */
    @Override
    public List<DpDataElemRespDTO> getDpDataElemListByAssetId(Long assetId, Set<Long> codeId) {
        //查询和资产关联的数据元信息id
        Set<Long> ids = new HashSet<>();
        List<DpDataElemAssetRelDO> list = iDpDataElemAssetRelService.lambdaQuery()
                .eq(DpDataElemAssetRelDO::getAssetId, assetId)
                .list();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DpDataElemAssetRelDO dpDataElemAssetRelDO : list) {
                ids.add(Long.valueOf(dpDataElemAssetRelDO.getDataElemId()));
            }
        }
        ids.addAll(codeId);
        List<DpDataElemDO> dpDataElemDOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            dpDataElemDOS = iDpDataElemService.lambdaQuery().in(DpDataElemDO::getId, ids).list();
            for (DpDataElemDO dpDataElemDO : dpDataElemDOS) {
                Set<Long> columnId = new HashSet<>();
                for (DpDataElemAssetRelDO dpDataElemAssetRelDO : list) {
                    if (dpDataElemAssetRelDO.getDataElemId().equals(dpDataElemDO.getId().toString())) {
                        columnId.add(Long.valueOf(dpDataElemAssetRelDO.getColumnId()));
                    }
                }
                dpDataElemDO.setColumnId(columnId);
            }
        }

        return BeanUtils.toBean(dpDataElemDOS, DpDataElemRespDTO.class);
    }

    /**
     * 更具模型id查询模型下的字段集合
     *
     * @param modelId 模型id
     */
    @Override
    public List<DpModelColumnRespDTO> getModelIdColumnList(Long modelId) {
        DpModelColumnSaveReqVO dpModelColumnSaveReqVO = new DpModelColumnSaveReqVO();
        dpModelColumnSaveReqVO.setModelId(modelId);
        List<DpModelColumnDO> dpModelColumnList = iDpModelColumnService.getDpModelColumnList(dpModelColumnSaveReqVO);
        List<DpModelColumnRespDTO> dpModelColumnRespDTOList = BeanUtils.toBean(dpModelColumnList, DpModelColumnRespDTO.class);
        return dpModelColumnRespDTOList;
    }

    /**
     * 根据字段id获取数据元id集合
     *
     * @param columnId
     * @return
     */
    @Override
    public Set<Long> getDpDataElemListByAssetIdApi(Long columnId) {
        Set<Long> result = new HashSet<>();
        List<DpDataElemAssetRelDO> list = iDpDataElemAssetRelService.lambdaQuery()
                .select(DpDataElemAssetRelDO::getDataElemId)
                .eq(DpDataElemAssetRelDO::getColumnId, columnId)
                .eq(DpDataElemAssetRelDO::getDelFlag, "0")
                .list();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DpDataElemAssetRelDO dpDataElemAssetRelDO : list) {
                result.add(Long.valueOf(dpDataElemAssetRelDO.getDataElemId()));
            }
        }

        return result;
    }

    @Override
    public List<DpDataElemAssetRelRespDTO> getDpDataElemListByColumnIdInApi(Collection<Long> columnIds) {
        List<DpDataElemAssetRelDO> list = iDpDataElemAssetRelService.lambdaQuery()
                .in(DpDataElemAssetRelDO::getColumnId, columnIds)
                .eq(DpDataElemAssetRelDO::getDelFlag, "0")
                .list();
        return BeanUtils.toBean(list, DpDataElemAssetRelRespDTO.class);
    }

    @Override
    public Set<Long> getDpDataElemListByAssetIdAndColumnId(Long assetId, Long columnId) {
        Set<Long> result = new HashSet<>();
        List<DpDataElemAssetRelDO> list = iDpDataElemAssetRelService.lambdaQuery()
                .select(DpDataElemAssetRelDO::getDataElemId)
                .eq(DpDataElemAssetRelDO::getAssetId, assetId)
                .eq(DpDataElemAssetRelDO::getColumnId, columnId)
                .list();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DpDataElemAssetRelDO dpDataElemAssetRelDO : list) {
                result.add(Long.valueOf(dpDataElemAssetRelDO.getDataElemId()));
            }
        }
        return result;
    }


    /**
     * 更新数据元和资产关系数据
     *
     * @param dpDataElemAssetRel
     * @return
     */
    @Override
    public boolean updateElementAssetRelation(DpDataElemAssetRelReqDTO dpDataElemAssetRel) {
        boolean save = false;
        Long assetId = dpDataElemAssetRel.getAssetId();
        iDpDataElemAssetRelService.lambdaUpdate().eq(DpDataElemAssetRelDO::getAssetId, assetId).remove();
        Set<Long> elementIds = dpDataElemAssetRel.getElementIds();
        List<DpDataElemAssetRelDO> dpDataElemAssetRelDOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(elementIds)) {
            dpDataElemAssetRelDOList = elementIds.stream().map(item -> {
                DpDataElemAssetRelDO dpDataElemAssetRelDO = new DpDataElemAssetRelDO();
                dpDataElemAssetRelDO.setAssetId(String.valueOf(assetId));//资产id
                dpDataElemAssetRelDO.setDataElemId(String.valueOf(item));//数据元id
                dpDataElemAssetRelDO.setDataElemType("1");//是数据元
                dpDataElemAssetRelDO.setTableName(dpDataElemAssetRel.getTableName());
                dpDataElemAssetRelDO.setColumnId(String.valueOf(dpDataElemAssetRel.getColumnId()));
                dpDataElemAssetRelDO.setColumnName(dpDataElemAssetRel.getColumnName());
                return dpDataElemAssetRelDO;
            }).collect(Collectors.toList());
        }
        for (DpDataElemAssetRelDO dpDataElemAssetRelDO : dpDataElemAssetRelDOList) {
            save = iDpDataElemAssetRelService.save(dpDataElemAssetRelDO);
        }
        return save;
    }

    /**
     * 插入数据元和资产关系数据
     *
     * @param dpDataElemAssetRel
     * @return
     */
    @Override
    public boolean insertElementAssetRelation(List<DpDataElemAssetRelReqDTO> dpDataElemAssetRel) {
        boolean result = false;
        if (CollectionUtils.isNotEmpty(dpDataElemAssetRel)) {
            //DpDataElemAssetRelReqDTO 转换为 DpDataElemAssetRelDO
            List<DpDataElemAssetRelDO> dpDataElemAssetRelDOList = dpDataElemAssetRel.stream().map(item -> {
                DpDataElemAssetRelDO dpDataElemAssetRelDO = new DpDataElemAssetRelDO();
                BeanUtil.copyProperties(item, dpDataElemAssetRelDO);
                return dpDataElemAssetRelDO;
            }).collect(Collectors.toList());
//            result = iDpDataElemAssetRelService.saveBatch(dpDataElemAssetRelDOList);
            for (DpDataElemAssetRelDO dpDataElemAssetRelDO : dpDataElemAssetRelDOList) {
                result = iDpDataElemAssetRelService.save(dpDataElemAssetRelDO);
            }
        }
        return result;
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DpModelDO.class).likeRight(DpModelDO::getCatCode, catCode));
    }

    /**
     * 根据数据元id查询数据元信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<DpDataElemRespDTO> getDpDataElemListByIdsApi(Set<Long> ids) {

        List<DpDataElemDO> list = iDpDataElemService.lambdaQuery()
                .in(DpDataElemDO::getId, ids)
                .eq(DpDataElemDO::getDelFlag, 0)
                .list();
        //将list的类型转换为DpDataElemRespDTO
        return list.stream().map(item -> {
            DpDataElemRespDTO dpModelColumnRespDTO = new DpDataElemRespDTO();
            BeanUtil.copyProperties(item, dpModelColumnRespDTO);
            return dpModelColumnRespDTO;
        }).collect(Collectors.toList());
    }

    /**
     * 根据逻辑模型ID获取逻辑模型列信息
     *
     * @param modelId 逻辑模型ID
     * @return 逻辑模型列信息
     */
    @Override
    public List<DpModelColumnRespDTO> getDpModelColumnListByModelIdApi(Long modelId) {
        List<DpModelColumnDO> list = iDpModelColumnService.lambdaQuery()
                .eq(DpModelColumnDO::getModelId, modelId)
                .list();
        //将list的类型转换为DpModelColumnRespDTO
        return list.stream().map(item -> {
            DpModelColumnRespDTO dpModelColumnRespDTO = new DpModelColumnRespDTO();
            BeanUtil.copyProperties(item, dpModelColumnRespDTO);
            return dpModelColumnRespDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public PageResult<DpModelDO> getDpModelPage(DpModelPageReqVO pageReqVO) {
        PageResult<DpModelDO> dpModelDOPageResult = dpModelMapper.selectPage(pageReqVO);
//        List<DpModelDO> rows = (List<DpModelDO>) dpModelDOPageResult.getRows();
//        if (CollectionUtils.isEmpty(rows)) {
//            return dpModelDOPageResult;
//        }
//        for (DpModelDO row : rows) {
//            //字段
//            DpModelColumnSaveReqVO dpModelColumnSaveReqVO = new DpModelColumnSaveReqVO();
//            dpModelColumnSaveReqVO.setModelId(row.getId());
//            long count = iDpModelColumnService.countByDpModelColumn(dpModelColumnSaveReqVO);
//            row.setColumnCount(count);
//
//            //资产
//
//        }
//        dpModelDOPageResult.setRows(rows);
        return dpModelDOPageResult;
    }

    @Override
    public Long createDpModel(DpModelSaveReqVO createReqVO) {
        DpModelDO dictType = BeanUtils.toBean(createReqVO, DpModelDO.class);
        dpModelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpModel(DpModelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新逻辑模型
        DpModelDO updateObj = BeanUtils.toBean(updateReqVO, DpModelDO.class);
        return dpModelMapper.updateById(updateObj);
    }

    @Override
    public int removeDpModel(Collection<Long> idList) {
        // 批量删除逻辑模型
        return dpModelMapper.deleteBatchIds(idList);
    }

    @Override
    public DpModelDO getDpModelById(Long id) {
        MPJLambdaWrapper<DpModelDO> mpjLambdaWrapper = new MPJLambdaWrapper();
        mpjLambdaWrapper.selectAll(DpModelDO.class)
                .select("t2.name AS catName",
                        "t3.NAME AS dataLayerName",
                        "t3.ENG_NAME AS dataLayerEngName",
                        "t4.NAME AS businessCategoryName",
                        "t4.ENG_NAME AS businessCategoryEngName",
                        "t5.NAME AS dataDomainName",
                        "t5.ENG_NAME AS dataDomainEngName",
                        "t6.NAME AS themeDomainName",
                        "t6.ENG_NAME AS themeDomainEngName",
                        "u.PHONENUMBER AS createUserPhoneNumber",
                        "u2.PHONENUMBER AS updateUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.CREATOR_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER u2 on t.UPDATER_ID = u2.USER_ID AND u2.DEL_FLAG = '0'")
                .leftJoin("ATT_MODEL_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DM_DATA_LAYER t3 ON t.DATA_LAYER_ID = t3.id AND t3.DEL_FLAG = '0'")
                .leftJoin("DM_BUSINESS_CATEGORY t4 ON t.BUSINESS_CATEGORY_ID = t4.id AND t4.DEL_FLAG = '0'")
                .leftJoin("DM_DATA_DOMAIN t5 ON t.DATA_DOMAIN_ID = t5.id AND t5.DEL_FLAG = '0'")
                .leftJoin("DM_THEME_DOMAIN t6 ON t.THEME_DOMAIN_ID = t6.id AND t6.DEL_FLAG = '0'")
                .eq(DpModelDO::getId, id);
        DpModelDO dpModelDO = dpModelMapper.selectJoinOne(DpModelDO.class, mpjLambdaWrapper);
        if (dpModelDO == null) {
            return null;
        }
        if ("2".equals(dpModelDO.getCreateType())) {
            DaDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(dpModelDO.getDatasourceId());
            if (datasource != null) {
                dpModelDO.setPort(datasource.getPort());
                dpModelDO.setIp(datasource.getIp());
                dpModelDO.setDatasourceConfig(datasource.getDatasourceConfig());
                dpModelDO.setDatasourceType(datasource.getDatasourceType());
                dpModelDO.setDatasourceName(datasource.getDatasourceName());
            }
        }
        if (dpModelDO.getDocumentId() != null) {
            DpDocumentDO dpDocument = dpDocumentService.getDpDocumentById(dpModelDO.getDocumentId());
            if (dpDocument != null) {
                dpModelDO.setDocumentCode(dpDocument.getCode());
                dpModelDO.setDocumentName(dpDocument.getName());
                dpModelDO.setDocumentType(dpDocument.getType());
            }
        }
        return dpModelDO;
    }

    /**
     * 根据逻辑模型ID获取逻辑模型信息
     *
     * @param id
     * @return
     */
    @Override
    public DpModelRespDTO getDpModelByIdApi(Long id) {
        DpModelRespDTO dto = new DpModelRespDTO();
        DpModelDO dpModelDO = this.getDpModelById(id);
        BeanUtil.copyProperties(dpModelDO, dto);
        return dto;
    }


    @Override
    public List<DpModelDO> getDpModelList() {
        return dpModelMapper.selectList();
    }

    @Override
    public Map<Long, DpModelDO> getDpModelMap() {
        List<DpModelDO> dpModelList = dpModelMapper.selectList();
        return dpModelList.stream().collect(Collectors.toMap(DpModelDO::getId, dpModelDO -> dpModelDO,
                // 保留已存在的值
                (existing, replacement) -> existing));
    }


    /**
     * 导入逻辑模型数据
     *
     * @param importExcelList 逻辑模型数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDpModel(List<DpModelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dp.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpModelRespVO respVO : importExcelList) {
            try {
                DpModelDO dpModelDO = BeanUtils.toBean(respVO, DpModelDO.class);
                Long dpModelId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpModelId != null) {
                        DpModelDO existingDpModel = dpModelMapper.selectById(dpModelId);
                        if (existingDpModel != null) {
                            dpModelMapper.updateById(dpModelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dp.import.update.success",
                                    "数据更新成功，ID为 " + dpModelId + " 的逻辑模型记录。", dpModelId, "逻辑模型"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.fail",
                                    "数据更新失败，ID为 " + dpModelId + " 的逻辑模型记录不存在。", dpModelId, "逻辑模型"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DpModelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpModelId);
                    DpModelDO existingDpModel = dpModelMapper.selectOne(queryWrapper);
                    if (existingDpModel == null) {
                        dpModelMapper.insert(dpModelDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dp.import.insert.success",
                                "数据插入成功，ID为 " + dpModelId + " 的逻辑模型记录。", dpModelId, "逻辑模型"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.insert.fail",
                                "数据插入失败，ID为 " + dpModelId + " 的逻辑模型记录已存在。", dpModelId, "逻辑模型"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dp.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("dp.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public int removeDpModelAndColumnAll(List<Long> asList) {
        int i = dpModelMapper.deleteBatchIds(asList);
        iDpModelColumnService.removeDpModelColumnByModelId(asList);
        return i > 0 ? 1 : 0;
    }

    @Override
    public Boolean updateStatus(Long id, Long status) {
        return this.update(Wrappers.lambdaUpdate(DpModelDO.class)
                .eq(DpModelDO::getId, id)
                .set(DpModelDO::getStatus, status));
    }

    @Override
    public List<TreeData> getTreeData() {
        List<TreeData> treeData = new ArrayList<>();

        treeData.add(TreeData.builder()
                .name("公共层")
                .type("0")
                .otherData(JSON.parseObject("{\"tooltipStr\":\"主要面向数据开发人员，作为应用层的数据地基，把杂乱的数据建成通用的明细模型，方便大家复用。\"}"))
                .children(dmBusinessCategoryApiService.getTreeData(null))
                .build());
        treeData.add(TreeData.builder()
                .name("应用层")
                .type("0")
                .otherData(JSON.parseObject("{\"tooltipStr\":\"主要面向业务及分析人员，通过加工公共层的基础数据计算而来，直接用来做可视化大屏或业务报表。\"}"))
                .children(dmThemeDomainApiService.getTreeData(null))
                .build());
        return treeData;
    }

    @Override
    public int updateCatCode(String oldCatCode, String newCatCode) {
        return dpModelMapper.updateCatCode(oldCatCode, newCatCode);
    }

    @Override
    public PageResult<DpModelDO> getReleaseListPage(DpModelPageReqVO pageReqVO) {
        return dpModelMapper.getReleaseListPage(pageReqVO);
    }

    @Override
    public BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids) {
        List<DpModelDO> list = baseMapper.selectList(Wrappers.lambdaQuery(DpModelDO.class)
                .select(DpModelDO::getId, DpModelDO::getStatus)
                .in(DpModelDO::getId, ids));
        int cannotDeleteCount = 0;
        List<Long> canDeleteIds = new ArrayList<>();
        for (DpModelDO one : list) {
            if ("1".equals(one.getStatus())) {
                cannotDeleteCount++;
                continue;
            }
            canDeleteIds.add(one.getId());
        }
        return new BatchDeleteCheck<>(cannotDeleteCount, canDeleteIds);
    }
}
