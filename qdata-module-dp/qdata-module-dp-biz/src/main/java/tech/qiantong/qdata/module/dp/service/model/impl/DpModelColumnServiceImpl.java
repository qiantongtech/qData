package tech.qiantong.qdata.module.dp.service.model.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;
import tech.qiantong.qdata.module.dp.dal.mapper.model.DpModelColumnMapper;
import tech.qiantong.qdata.module.dp.service.model.IDpModelColumnService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 逻辑模型属性信息Service业务层处理
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpModelColumnServiceImpl extends ServiceImpl<DpModelColumnMapper, DpModelColumnDO>
        implements IDpModelColumnService {
    @Resource
    private DpModelColumnMapper dpModelColumnMapper;

    @Override
    public PageResult<DpModelColumnDO> getDpModelColumnPage(DpModelColumnPageReqVO pageReqVO) {
        return dpModelColumnMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpModelColumn(DpModelColumnSaveReqVO createReqVO) {
        DpModelColumnDO dictType = BeanUtils.toBean(createReqVO, DpModelColumnDO.class);
        dpModelColumnMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpModelColumn(DpModelColumnSaveReqVO updateReqVO) {
        // 相关校验

        // 更新逻辑模型属性信息
        DpModelColumnDO updateObj = BeanUtils.toBean(updateReqVO, DpModelColumnDO.class);
        return dpModelColumnMapper.updateById(updateObj);
    }

    @Override
    public int removeDpModelColumn(Collection<Long> idList) {
        // 批量删除逻辑模型属性信息
        return dpModelColumnMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeDpModelColumnByModelId(Collection<Long> modelIdList) {
        return dpModelColumnMapper.delete(new LambdaQueryWrapperX<DpModelColumnDO>()
                .in(DpModelColumnDO::getModelId, modelIdList));
    }

    @Override
    public DpModelColumnDO getDpModelColumnById(Long id) {
        return dpModelColumnMapper.selectById(id);
    }

    @Override
    public List<DpModelColumnDO> getDpModelColumnList() {
        return dpModelColumnMapper.selectList();
    }

    @Override
    public List<DpModelColumnDO> getDpModelColumnList(DpModelColumnSaveReqVO reqVO) {
        DpModelColumnPageReqVO dpModelColumnPageReqVO = BeanUtils.toBean(reqVO, DpModelColumnPageReqVO.class);
        MPJLambdaWrapper<DpModelColumnDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DpModelColumnDO.class)
                .select("t2.NAME AS dataElemName")
                .leftJoin("DP_DATA_ELEM t2 on t.DATA_ELEM_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .eq(dpModelColumnPageReqVO.getModelId() != null, DpModelColumnDO::getModelId,
                        dpModelColumnPageReqVO.getModelId())
                .like(StringUtils.isNotBlank(dpModelColumnPageReqVO.getEngName()), DpModelColumnDO::getEngName,
                        dpModelColumnPageReqVO.getEngName())
                .like(StringUtils.isNotBlank(dpModelColumnPageReqVO.getCnName()), DpModelColumnDO::getCnName,
                        dpModelColumnPageReqVO.getCnName())
                .eq(StringUtils.isNotBlank(dpModelColumnPageReqVO.getColumnType()), DpModelColumnDO::getColumnType,
                        dpModelColumnPageReqVO.getColumnType())
                .eq(dpModelColumnPageReqVO.getColumnLength() != null, DpModelColumnDO::getColumnLength,
                        dpModelColumnPageReqVO.getColumnLength())
                .eq(dpModelColumnPageReqVO.getColumnScale() != null, DpModelColumnDO::getColumnScale,
                        dpModelColumnPageReqVO.getColumnScale())
                .eq(StringUtils.isNotBlank(dpModelColumnPageReqVO.getDefaultValue()), DpModelColumnDO::getDefaultValue,
                        dpModelColumnPageReqVO.getDefaultValue())
                .eq(dpModelColumnPageReqVO.getPkFlag() != null, DpModelColumnDO::getPkFlag,
                        dpModelColumnPageReqVO.getPkFlag())
                .eq(dpModelColumnPageReqVO.getNullableFlag() != null, DpModelColumnDO::getNullableFlag,
                        dpModelColumnPageReqVO.getNullableFlag())
                .eq(dpModelColumnPageReqVO.getSortOrder() != null, DpModelColumnDO::getSortOrder,
                        dpModelColumnPageReqVO.getSortOrder())
                .eq(StringUtils.isNotBlank(dpModelColumnPageReqVO.getAuthorityDept()),
                        DpModelColumnDO::getAuthorityDept, dpModelColumnPageReqVO.getAuthorityDept())
                .eq(dpModelColumnPageReqVO.getDataElemId() != null, DpModelColumnDO::getDataElemId,
                        dpModelColumnPageReqVO.getDataElemId())
                .eq(dpModelColumnPageReqVO.getCreateTime() != null, DpModelColumnDO::getCreateTime,
                        dpModelColumnPageReqVO.getCreateTime());
        return dpModelColumnMapper.selectJoinList(DpModelColumnDO.class, wrapper);
    }

    @Override
    public long countByDpModelColumn(DpModelColumnSaveReqVO reqVO) {
        LambdaQueryWrapperX<DpModelColumnDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(DpModelColumnDO::getModelId, reqVO.getModelId())
                .likeIfPresent(DpModelColumnDO::getEngName, reqVO.getEngName())
                .likeIfPresent(DpModelColumnDO::getCnName, reqVO.getCnName())
                .eqIfPresent(DpModelColumnDO::getColumnType, reqVO.getColumnType())
                .eqIfPresent(DpModelColumnDO::getColumnLength, reqVO.getColumnLength())
                .eqIfPresent(DpModelColumnDO::getColumnScale, reqVO.getColumnScale())
                .eqIfPresent(DpModelColumnDO::getDefaultValue, reqVO.getDefaultValue())
                .eqIfPresent(DpModelColumnDO::getPkFlag, reqVO.getPkFlag())
                .eqIfPresent(DpModelColumnDO::getNullableFlag, reqVO.getNullableFlag())
                .eqIfPresent(DpModelColumnDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(DpModelColumnDO::getAuthorityDept, reqVO.getAuthorityDept())
                .eqIfPresent(DpModelColumnDO::getDataElemId, reqVO.getDataElemId())
                .eqIfPresent(DpModelColumnDO::getCreateTime, reqVO.getCreateTime());
        return dpModelColumnMapper.selectCount(queryWrapper);
    }

    @Override
    public Map<Long, DpModelColumnDO> getDpModelColumnMap() {
        List<DpModelColumnDO> dpModelColumnList = dpModelColumnMapper.selectList();
        return dpModelColumnList.stream()
                .collect(Collectors.toMap(
                        DpModelColumnDO::getId,
                        dpModelColumnDO -> dpModelColumnDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing));
    }

    /**
     * 导入逻辑模型属性信息数据
     *
     * @param importExcelList 逻辑模型属性信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDpModelColumn(List<DpModelColumnRespVO> importExcelList, boolean isUpdateSupport,
            String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpModelColumnRespVO respVO : importExcelList) {
            try {
                DpModelColumnDO dpModelColumnDO = BeanUtils.toBean(respVO, DpModelColumnDO.class);
                Long dpModelColumnId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpModelColumnId != null) {
                        DpModelColumnDO existingDpModelColumn = dpModelColumnMapper.selectById(dpModelColumnId);
                        if (existingDpModelColumn != null) {
                            dpModelColumnMapper.updateById(dpModelColumnDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dpModelColumnId + " 的逻辑模型属性信息记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dpModelColumnId + " 的逻辑模型属性信息记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DpModelColumnDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpModelColumnId);
                    DpModelColumnDO existingDpModelColumn = dpModelColumnMapper.selectOne(queryWrapper);
                    if (existingDpModelColumn == null) {
                        dpModelColumnMapper.insert(dpModelColumnDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dpModelColumnId + " 的逻辑模型属性信息记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dpModelColumnId + " 的逻辑模型属性信息记录已存在。");
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

    /**
     * 批量插入逻辑模型属性信息数据
     *
     * @param dpModelColumnList 逻辑模型属性信息数据列表
     * @return 结果
     */
    @Override
    public Boolean createDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList) {
        List<DpModelColumnDO> dpModelColumnDOList = BeanUtils.toBean(dpModelColumnList, DpModelColumnDO.class);
        for (DpModelColumnDO dpModelColumnDO : dpModelColumnDOList) {
            dpModelColumnMapper.insert(dpModelColumnDO);
        }
        // Boolean aBoolean = dpModelColumnMapper.insertBatch(dpModelColumnDOList);
        return true;
    }

    /**
     * 批量修改和插入逻辑模型属性信息数据
     *
     * @param dpModelColumnList 逻辑模型属性信息数据列表
     * @return 结果
     */
    @Override
    public Boolean updateDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList) {
        List<DpModelColumnDO> dpModelColumnDOList = BeanUtils.toBean(dpModelColumnList, DpModelColumnDO.class);
        Long modelId = dpModelColumnDOList.get(0) == null ? null : dpModelColumnDOList.get(0).getModelId();
        DpModelColumnSaveReqVO dpModelColumnSaveReqVO = new DpModelColumnSaveReqVO();
        dpModelColumnSaveReqVO.setModelId(modelId);
        List<DpModelColumnDO> modelColumnList = this.getDpModelColumnList(dpModelColumnSaveReqVO);
        // 用于存储dpModelColumnDOList中的所有ID
        Set<Long> newIds = new HashSet<>();
        for (DpModelColumnDO dpModelColumnDO : dpModelColumnDOList) {
            if (dpModelColumnDO.getId() != null) {
                dpModelColumnMapper.updateById(dpModelColumnDO);
                newIds.add(dpModelColumnDO.getId());
            } else {
                dpModelColumnMapper.insert(dpModelColumnDO);
            }
        }
        // 删除modelColumnList中存在但dpModelColumnDOList中不存在的记录
        for (DpModelColumnDO existingColumn : modelColumnList) {
            if (!newIds.contains(existingColumn.getId())) {
                dpModelColumnMapper.deleteById(existingColumn.getId());
            }
        }
        return true;
    }
}
