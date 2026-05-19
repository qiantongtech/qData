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

package tech.qiantong.qdata.module.da.service.discovery.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.*;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTableDO;
import tech.qiantong.qdata.module.da.dal.mapper.discovery.DaDiscoveryTableMapper;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryColumnService;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTableService;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTaskService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据发现库信息Service业务层处理
 *
 * @author qdata
 * @date 2025-02-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDiscoveryTableServiceImpl  extends ServiceImpl<DaDiscoveryTableMapper,DaDiscoveryTableDO> implements IDaDiscoveryTableService {
    @Resource
    private DaDiscoveryTableMapper daDiscoveryTableMapper;
    @Resource
    @Lazy
    private IDaDiscoveryColumnService iDaDiscoveryColumnService;
    @Resource
    @Lazy
    private IDaAssetService iDaAssetService;
    @Resource
    @Lazy
    private IDaDiscoveryTaskService iDaDiscoveryTaskService;

    @Override
    public PageResult<DaDiscoveryTableDO> getDaDiscoveryTablePage(DaDiscoveryTablePageReqVO pageReqVO) {
        return daDiscoveryTableMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DaDiscoveryTableDO> getDaDiscoveryTableList(DaDiscoveryTablePageReqVO reqVO) {

        MPJLambdaWrapper<DaDiscoveryTableDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DaDiscoveryTableDO.class)
                .eq(reqVO.getTaskId() != null, DaDiscoveryTableDO::getTaskId, reqVO.getTaskId())
                .like(StringUtils.isNotBlank(reqVO.getTableName()), DaDiscoveryTableDO::getTableName, reqVO.getTableName())
                .eq(StringUtils.isNotBlank(reqVO.getTableComment()), DaDiscoveryTableDO::getTableComment, reqVO.getTableComment())
                .eq(StringUtils.isNotBlank(reqVO.getChangeFlag()), DaDiscoveryTableDO::getChangeFlag, reqVO.getChangeFlag())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DaDiscoveryTableDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotBlank(reqVO.getIgnoreFlag()), DaDiscoveryTableDO::getIgnoreFlag, reqVO.getIgnoreFlag());
        if(StringUtils.isNotBlank(reqVO.getKeyword())){
            // 新增的 keyword 模糊匹配
            wrapper.and(q -> q.like(DaDiscoveryTableDO::getTableName, reqVO.getKeyword())
                    .or()
                    .like(DaDiscoveryTableDO::getTableComment, reqVO.getKeyword()));
        }

        return daDiscoveryTableMapper.selectList(wrapper);
    }

    @Override
    public Long createDaDiscoveryTable(DaDiscoveryTableSaveReqVO createReqVO) {
        DaDiscoveryTableDO dictType = BeanUtils.toBean(createReqVO, DaDiscoveryTableDO.class);
        daDiscoveryTableMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public Long createDaDiscoveryTable(DaDiscoveryTableDO createReqVO) {
        daDiscoveryTableMapper.insert(createReqVO);
        return createReqVO.getId();
    }

    @Override
    public int updateDaDiscoveryTable(DaDiscoveryTableSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据发现库信息
        DaDiscoveryTableDO updateObj = BeanUtils.toBean(updateReqVO, DaDiscoveryTableDO.class);
        return daDiscoveryTableMapper.updateById(updateObj);
    }
    @Override
    public int updateDaDiscoveryTable(DaDiscoveryTableDO updateReqVO) {
        // 更新数据发现库信息
        return daDiscoveryTableMapper.updateById(updateReqVO);
    }
    @Override
    public int removeDaDiscoveryTable(Collection<Long> idList) {
        // 批量删除数据发现库信息
        return daDiscoveryTableMapper.deleteBatchIds(idList);
    }

    @Override
    public DaDiscoveryTableDO getDaDiscoveryTableById(Long id) {
        return daDiscoveryTableMapper.selectById(id);
    }

    @Override
    public List<DaDiscoveryTableDO> getDaDiscoveryTableList() {
        return daDiscoveryTableMapper.selectList();
    }

    @Override
    public Map<Long, DaDiscoveryTableDO> getDaDiscoveryTableMap() {
        List<DaDiscoveryTableDO> daDiscoveryTableList = daDiscoveryTableMapper.selectList();
        return daDiscoveryTableList.stream()
                .collect(Collectors.toMap(
                        DaDiscoveryTableDO::getId,
                        daDiscoveryTableDO -> daDiscoveryTableDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据发现库信息数据
     *
     * @param importExcelList 数据发现库信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDaDiscoveryTable(List<DaDiscoveryTableRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaDiscoveryTableRespVO respVO : importExcelList) {
            try {
                DaDiscoveryTableDO daDiscoveryTableDO = BeanUtils.toBean(respVO, DaDiscoveryTableDO.class);
                Long daDiscoveryTableId = respVO.getId();
                if (isUpdateSupport) {
                    if (daDiscoveryTableId != null) {
                        DaDiscoveryTableDO existingDaDiscoveryTable = daDiscoveryTableMapper.selectById(daDiscoveryTableId);
                        if (existingDaDiscoveryTable != null) {
                            daDiscoveryTableMapper.updateById(daDiscoveryTableDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daDiscoveryTableId + " 的数据发现库信息记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daDiscoveryTableId + " 的数据发现库信息记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaDiscoveryTableDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDiscoveryTableId);
                    DaDiscoveryTableDO existingDaDiscoveryTable = daDiscoveryTableMapper.selectOne(queryWrapper);
                    if (existingDaDiscoveryTable == null) {
                        daDiscoveryTableMapper.insert(daDiscoveryTableDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daDiscoveryTableId + " 的数据发现库信息记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daDiscoveryTableId + " 的数据发现库信息记录已存在。");
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
    public Integer commitOrRevokeDiscoveryInfo(DaDiscoveryTableSaveReqVO daDiscoveryTable) {
        //状态;1:待提交，2:已提交
        String status = daDiscoveryTable.getStatus();
        //是否忽略;0:否，1：是
        String ignoreFlag = daDiscoveryTable.getIgnoreFlag();
        String themeId = daDiscoveryTable.getThemeId();

        //获取信息
        DaDiscoveryTableDO daDiscoveryTableById = this.getDaDiscoveryTableById(daDiscoveryTable.getId());
        if(daDiscoveryTableById == null){
            throw new ServiceException("未获取到该表信息，请刷新后重试！");
        }
        DaDiscoveryTaskRespVO daDiscoveryTaskById = iDaDiscoveryTaskService.getDaDiscoveryTaskById(daDiscoveryTableById.getTaskId());
        if(daDiscoveryTaskById == null){
            throw new ServiceException("未获取到该发现任务信息，请刷新后重试！");
        }

        if(StringUtils.equals(daDiscoveryTableById.getStatus(),status) && StringUtils.equals(daDiscoveryTableById.getIgnoreFlag(),ignoreFlag)){
            return 1;
        }
        DaDiscoveryColumnPageReqVO reqVO = new DaDiscoveryColumnPageReqVO();
        reqVO.setTableId(daDiscoveryTableById.getId());
        List<DaDiscoveryColumnDO> daDiscoveryColumnList = iDaDiscoveryColumnService.getDaDiscoveryColumnList(reqVO);

        DaAssetPageReqVO daAssetPageReqVO = new DaAssetPageReqVO(daDiscoveryTableById);
        //兼容表的备注为空时候，导致的资产地图为空
        daAssetPageReqVO.setName(daDiscoveryTable.getAssetName());
        daAssetPageReqVO.setDatasourceId(String.valueOf(daDiscoveryTaskById.getDatasourceId()));
        daAssetPageReqVO.setCatCode(daDiscoveryTable.getCatCode());
        List<String> themeIdList = new ArrayList<>();
        themeIdList.add(StringUtils.isEmpty(themeId) ? "1":themeId);
        daAssetPageReqVO.setThemeIdList(themeIdList);
        daAssetPageReqVO.setSource("1");
        if(StringUtils.equals("2",status)){
            List<DaAssetColumnSaveReqVO> columnSaveReqVOList = daDiscoveryColumnList.stream().map(itam -> new DaAssetColumnSaveReqVO(itam)).collect(Collectors.toList());
            iDaAssetService.insertAssetByDiscoveryInfo(daAssetPageReqVO,columnSaveReqVOList);
        }else {
            iDaAssetService.updateAssetByDiscoveryInfo(daAssetPageReqVO);
        }

        return this.updateDaDiscoveryTable(daDiscoveryTable);
    }

    @Override
    public Integer updateByTaskIdListAndTableNameStatus(DaDiscoveryTableSaveReqVO daDiscoveryTable) {
        LambdaQueryWrapperX<DaDiscoveryTableDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.inIfPresent(DaDiscoveryTableDO::getTaskId,daDiscoveryTable.getTaskIdList())
                .eqIfPresent(DaDiscoveryTableDO::getTableName,daDiscoveryTable.getTableName());
        DaDiscoveryTableDO daDiscoveryTableDO = BeanUtils.toBean(daDiscoveryTable, DaDiscoveryTableDO.class);
        return daDiscoveryTableMapper.update(daDiscoveryTableDO,queryWrapperX);
    }
}
