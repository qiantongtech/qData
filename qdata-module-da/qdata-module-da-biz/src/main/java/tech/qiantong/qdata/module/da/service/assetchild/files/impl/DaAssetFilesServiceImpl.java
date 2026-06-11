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
 */

package tech.qiantong.qdata.module.da.service.assetchild.files.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.files.DaAssetFilesDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.files.DaAssetFilesMapper;
import tech.qiantong.qdata.module.da.service.assetchild.files.IDaAssetFilesService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据资产-文件服务Service业务层处理
 *
 * @author qdata
 * @date 2025-06-26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetFilesServiceImpl  extends ServiceImpl<DaAssetFilesMapper,DaAssetFilesDO> implements IDaAssetFilesService {
    @Resource
    private DaAssetFilesMapper daAssetFilesMapper;

    @Override
    public PageResult<DaAssetFilesDO> getDaAssetFilesPage(DaAssetFilesPageReqVO pageReqVO) {
        return daAssetFilesMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetFiles(DaAssetFilesSaveReqVO createReqVO) {
        DaAssetFilesDO dictType = BeanUtils.toBean(createReqVO, DaAssetFilesDO.class);
        daAssetFilesMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetFiles(DaAssetFilesSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产-文件服务
        DaAssetFilesDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetFilesDO.class);
        return daAssetFilesMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetFiles(Collection<Long> idList) {
        // 批量删除数据资产-文件服务
        return daAssetFilesMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetFilesDO getDaAssetFilesById(Long id) {
        return daAssetFilesMapper.selectById(id);
    }

    @Override
    public List<DaAssetFilesDO> getDaAssetFilesList() {
        return daAssetFilesMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetFilesDO> getDaAssetFilesMap() {
        List<DaAssetFilesDO> daAssetFilesList = daAssetFilesMapper.selectList();
        return daAssetFilesList.stream()
                .collect(Collectors.toMap(
                        DaAssetFilesDO::getId,
                        daAssetFilesDO -> daAssetFilesDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据资产-文件服务数据
         *
         * @param importExcelList 数据资产-文件服务数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDaAssetFiles(List<DaAssetFilesRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetFilesRespVO respVO : importExcelList) {
                try {
                    DaAssetFilesDO daAssetFilesDO = BeanUtils.toBean(respVO, DaAssetFilesDO.class);
                    Long daAssetFilesId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetFilesId != null) {
                            DaAssetFilesDO existingDaAssetFiles = daAssetFilesMapper.selectById(daAssetFilesId);
                            if (existingDaAssetFiles != null) {
                                daAssetFilesMapper.updateById(daAssetFilesDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + daAssetFilesId + " 的数据资产-文件服务记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + daAssetFilesId + " 的数据资产-文件服务记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DaAssetFilesDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetFilesId);
                        DaAssetFilesDO existingDaAssetFiles = daAssetFilesMapper.selectOne(queryWrapper);
                        if (existingDaAssetFiles == null) {
                            daAssetFilesMapper.insert(daAssetFilesDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + daAssetFilesId + " 的数据资产-文件服务记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + daAssetFilesId + " 的数据资产-文件服务记录已存在。");
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
