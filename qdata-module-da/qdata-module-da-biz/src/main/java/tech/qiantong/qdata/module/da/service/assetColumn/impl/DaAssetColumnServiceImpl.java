package tech.qiantong.qdata.module.da.service.assetColumn.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetColumn.DaAssetColumnMapper;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;
import tech.qiantong.qdata.module.da.service.assetColumn.IDaAssetColumnService;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemRuleRelService;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据资产字段Service业务层处理
 *
 * @author lhs
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetColumnServiceImpl extends ServiceImpl<DaAssetColumnMapper, DaAssetColumnDO> implements IDaAssetColumnService {
    @Resource
    private DaAssetColumnMapper daAssetColumnMapper;
    @Resource
    private IDpModelApiService iDpModelApiService;
    @Resource
    private IDataElemRuleRelService dataElemRuleRelService;
    @Resource
    private IDaAssetService daAssetService;


    @Override
    public AjaxResult getColumnByAssetId(DaAssetColumnPageReqVO pageReqVO) {
        if (StringUtils.isEmpty(pageReqVO.getAssetId())) {//资产id不能为空
            return AjaxResult.error("资产id不能为空");
        }
        List<DaAssetColumnDO> list = this.lambdaQuery()
                .eq(DaAssetColumnDO::getAssetId, pageReqVO.getAssetId())
                .eq(DaAssetColumnDO::getDelFlag, 0)
                .orderByAsc(DaAssetColumnDO::getId)
                .list();

        for (DaAssetColumnDO daAssetColumnDO : list) {
            Set<Long> dpDataElemListByAssetIdApi = iDpModelApiService.getDpDataElemListByAssetIdAndColumnId(daAssetColumnDO.getAssetId(), daAssetColumnDO.getId());
            daAssetColumnDO.setElementId(dpDataElemListByAssetIdApi);
            if (dpDataElemListByAssetIdApi.size() > 0) {
                daAssetColumnDO.setCleanRuleList(dataElemRuleRelService.listByDataElemIdList(new ArrayList<>(dpDataElemListByAssetIdApi), "2"));
            }
        }
        return AjaxResult.success(list);
    }

    @Override
    public List<DaAssetColumnDO> getDaAssetColumnList(DaAssetColumnPageReqVO pageReqVO) {
        MPJLambdaWrapper<DaAssetColumnDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.eq(StringUtils.isNotEmpty(pageReqVO.getAssetId()), DaAssetColumnDO::getAssetId, pageReqVO.getAssetId())
                .eq(StringUtils.isNotEmpty(pageReqVO.getSensitiveLevelId()), DaAssetColumnDO::getSensitiveLevelId, pageReqVO.getSensitiveLevelId());
        return daAssetColumnMapper.selectList(lambdaWrapper);
    }

    @Override
    public PageResult<DaAssetColumnDO> getDaAssetColumnPage(DaAssetColumnPageReqVO pageReqVO) {
        if (StringUtils.isEmpty(pageReqVO.getAssetId())) {
            return PageResult.empty();
        }
        PageResult<DaAssetColumnDO> daAssetColumnDOPageResult = daAssetColumnMapper.selectPage(pageReqVO);
        Set<Long> ids = new HashSet<>();
        List<?> rows = daAssetColumnDOPageResult.getRows();
        for (Object row : rows) {
            DaAssetColumnDO daAssetColumnDO = (DaAssetColumnDO) row;
            ids.add(daAssetColumnDO.getDataElemCodeId());
        }
        List<DpDataElemRespDTO> dpDataElemListByAssetId = iDpModelApiService.getDpDataElemListByAssetId(Long.valueOf(pageReqVO.getAssetId()), ids);
        for (Object row : rows) {
            DaAssetColumnDO daAssetColumnDO = (DaAssetColumnDO) row;
            String elementName = "";
            for (DpDataElemRespDTO dpDataElemRespDTO : dpDataElemListByAssetId) {
                if (dpDataElemRespDTO.getId().equals(daAssetColumnDO.getDataElemCodeId())) {
                    daAssetColumnDO.setDataElemCodeName(dpDataElemRespDTO.getName());
                }
                if (dpDataElemRespDTO.getColumnId() != null && dpDataElemRespDTO.getColumnId().contains(daAssetColumnDO.getId())) {
                    elementName = dpDataElemRespDTO.getName() + "等";
                }
            }
            daAssetColumnDO.setRelDataElmeName(elementName);
        }
        return daAssetColumnDOPageResult;
    }

    @Override
    public Long createDaAssetColumn(DaAssetColumnSaveReqVO createReqVO) {
        DaAssetColumnDO dictType = BeanUtils.toBean(createReqVO, DaAssetColumnDO.class);
        daAssetColumnMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDaAssetColumn(DaAssetColumnSaveReqVO updateReqVO) {
        DaAssetDO daAssetDO = daAssetService.getById(updateReqVO.getAssetId());
        if (daAssetDO == null) {
            throw new ServiceException("数据资产不存在");
        }
        //维护数据元数据资产关联信息表信息
        DpDataElemAssetRelReqDTO dpDataElemAssetRelReqDTO = new DpDataElemAssetRelReqDTO();
        dpDataElemAssetRelReqDTO.setTableName(daAssetDO.getTableName());
        dpDataElemAssetRelReqDTO.setColumnName(updateReqVO.getColumnName());
        dpDataElemAssetRelReqDTO.setColumnId(updateReqVO.getId());
        dpDataElemAssetRelReqDTO.setAssetId(daAssetDO.getId());
        dpDataElemAssetRelReqDTO.setElementIds(updateReqVO.getElementId());
        boolean b = iDpModelApiService.updateElementAssetRelation(dpDataElemAssetRelReqDTO);
//        if(!b){
//            throw new ServiceException("数据元和资产关系数据更新失败");
//        }
        //不是代码，将代码表关联id制空
        DaAssetColumnDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetColumnDO.class);
        if (StringUtils.isEmpty(updateObj.getDataElemCodeFlag()) || "0".equals(updateObj.getDataElemCodeFlag())) {
            updateObj.setDataElemCodeId(null);
        }
        // 更新数据资产字段
        return daAssetColumnMapper.updateDaAssetColumn(updateObj);
    }

    @Override
    public int removeDaAssetColumn(Collection<Long> idList) {
        // 批量删除数据资产字段
        return daAssetColumnMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetColumnDO getDaAssetColumnById(Long id) {
        DaAssetColumnDO daAssetColumnDO = daAssetColumnMapper.selectById(id);
        //查询数据元id
        Set<Long> dpDataElemListByAssetIdApi = iDpModelApiService.getDpDataElemListByAssetIdApi(daAssetColumnDO.getId());
        daAssetColumnDO.setElementId(dpDataElemListByAssetIdApi);
        return daAssetColumnDO;
    }

    @Override
    public List<DaAssetColumnDO> getDaAssetColumnList() {
        return daAssetColumnMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetColumnDO> getDaAssetColumnMap() {
        List<DaAssetColumnDO> daAssetColumnList = daAssetColumnMapper.selectList();
        return daAssetColumnList.stream()
                .collect(Collectors.toMap(
                        DaAssetColumnDO::getId,
                        daAssetColumnDO -> daAssetColumnDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据资产字段数据
     *
     * @param importExcelList 数据资产字段数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaAssetColumn(List<DaAssetColumnRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetColumnRespVO respVO : importExcelList) {
            try {
                DaAssetColumnDO daAssetColumnDO = BeanUtils.toBean(respVO, DaAssetColumnDO.class);
                Long daAssetColumnId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetColumnId != null) {
                        DaAssetColumnDO existingDaAssetColumn = daAssetColumnMapper.selectById(daAssetColumnId);
                        if (existingDaAssetColumn != null) {
                            daAssetColumnMapper.updateById(daAssetColumnDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daAssetColumnId + " 的数据资产字段记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daAssetColumnId + " 的数据资产字段记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaAssetColumnDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetColumnId);
                    DaAssetColumnDO existingDaAssetColumn = daAssetColumnMapper.selectOne(queryWrapper);
                    if (existingDaAssetColumn == null) {
                        daAssetColumnMapper.insert(daAssetColumnDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daAssetColumnId + " 的数据资产字段记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daAssetColumnId + " 的数据资产字段记录已存在。");
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
}
