package tech.qiantong.qdata.module.dp.service.model.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelRespDTO;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.*;
import tech.qiantong.qdata.module.dp.convert.model.DpModelColumnConvert;
import tech.qiantong.qdata.module.dp.convert.model.DpModelConvert;
import tech.qiantong.qdata.module.dp.convert.model.DpModelMaterializedConvert;
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
            dpDataElemDOS = iDpDataElemService.lambdaQuery()
                    .in(DpDataElemDO::getId, ids)
                    .list();
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
        iDpDataElemAssetRelService.lambdaUpdate()
                .eq(DpDataElemAssetRelDO::getAssetId, assetId)
                .remove();
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
        return baseMapper.selectCount(Wrappers.lambdaQuery(DpModelDO.class)
                .likeRight(DpModelDO::getCatCode, catCode));
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
        List<DpModelDO> rows = (List<DpModelDO>) dpModelDOPageResult.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return dpModelDOPageResult;
        }
        for (DpModelDO row : rows) {
            //字段
            DpModelColumnSaveReqVO dpModelColumnSaveReqVO = new DpModelColumnSaveReqVO();
            dpModelColumnSaveReqVO.setModelId(row.getId());
            long count = iDpModelColumnService.countByDpModelColumn(dpModelColumnSaveReqVO);
            row.setColumnCount(count);

            //资产

        }
        dpModelDOPageResult.setRows(rows);
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
                .select("t2.name AS catName")
                .leftJoin("ATT_MODEL_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .eq(DpModelDO::getId, id);
        DpModelDO dpModelDO = dpModelMapper.selectJoinOne(DpModelDO.class, mpjLambdaWrapper);
        if (dpModelDO == null) {
            return null;
        }
        if ("2".equals(dpModelDO.getCreateType())) {
            DaDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(dpModelDO.getDatasourceId());
            dpModelDO.setPort(datasource.getPort());
            dpModelDO.setIp(datasource.getIp());
            dpModelDO.setDatasourceConfig(datasource.getDatasourceConfig());
            dpModelDO.setDatasourceType(datasource.getDatasourceType());
            dpModelDO.setDatasourceName(datasource.getDatasourceName());
        }
        if(dpModelDO.getDocumentId() != null){
            DpDocumentDO dpDocumentById = dpDocumentService.getDpDocumentById(dpModelDO.getDocumentId());
            dpDocumentById = dpDocumentById == null ? new DpDocumentDO():dpDocumentById;
            dpModelDO.setDocumentCode(dpDocumentById.getCode());
            dpModelDO.setDocumentName(dpDocumentById.getName());
            dpModelDO.setDocumentType(dpDocumentById.getType());
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
        DpModelDO dpModelDO = dpModelMapper.selectById(id);
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
        return dpModelList.stream()
                .collect(Collectors.toMap(
                        DpModelDO::getId,
                        dpModelDO -> dpModelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
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
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + dpModelId + " 的逻辑模型记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dpModelId + " 的逻辑模型记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DpModelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpModelId);
                    DpModelDO existingDpModel = dpModelMapper.selectOne(queryWrapper);
                    if (existingDpModel == null) {
                        dpModelMapper.insert(dpModelDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dpModelId + " 的逻辑模型记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dpModelId + " 的逻辑模型记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
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
}
