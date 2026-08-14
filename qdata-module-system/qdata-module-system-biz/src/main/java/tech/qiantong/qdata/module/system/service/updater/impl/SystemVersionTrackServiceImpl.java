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

package tech.qiantong.qdata.module.system.service.updater.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackPageReqVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackRespVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackSaveReqVO;
import tech.qiantong.qdata.module.system.dal.dataobject.updater.SystemVersionTrackDO;
import tech.qiantong.qdata.module.system.dal.mapper.updater.SystemVersionTrackMapper;
import tech.qiantong.qdata.module.system.service.updater.ISystemVersionTrackService;
/**
 * 版本跟踪Service business layer processing
 *
 * @author qdata
 * @date 2026-08-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemVersionTrackServiceImpl  extends ServiceImpl<SystemVersionTrackMapper,SystemVersionTrackDO> implements ISystemVersionTrackService {
    @Resource
    private SystemVersionTrackMapper systemVersionTrackMapper;

    @Override
    public PageResult<SystemVersionTrackDO> getSystemVersionTrackPage(SystemVersionTrackPageReqVO pageReqVO) {
        return systemVersionTrackMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createSystemVersionTrack(SystemVersionTrackSaveReqVO createReqVO) {
        SystemVersionTrackDO dictType = BeanUtils.toBean(createReqVO, SystemVersionTrackDO.class);
        systemVersionTrackMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateSystemVersionTrack(SystemVersionTrackSaveReqVO updateReqVO) {
        // Related verification

        // Update 版本跟踪
        SystemVersionTrackDO updateObj = BeanUtils.toBean(updateReqVO, SystemVersionTrackDO.class);
        return systemVersionTrackMapper.updateById(updateObj);
    }
    @Override
    public int removeSystemVersionTrack(Collection<Long> idList) {
        // Delete 版本跟踪 in batches
        return systemVersionTrackMapper.deleteBatchIds(idList);
    }

    @Override
    public SystemVersionTrackDO getSystemVersionTrackById(Long id) {
        return systemVersionTrackMapper.selectById(id);
    }

    @Override
    public List<SystemVersionTrackDO> getSystemVersionTrackList() {
        return systemVersionTrackMapper.selectList();
    }

    @Override
    public Map<Long, SystemVersionTrackDO> getSystemVersionTrackMap() {
        List<SystemVersionTrackDO> systemVersionTrackList = systemVersionTrackMapper.selectList();
        return systemVersionTrackList.stream()
                .collect(Collectors.toMap(
                        SystemVersionTrackDO::getId,
                        systemVersionTrackDO -> systemVersionTrackDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import 版本跟踪 data
         *
         * @param importExcelList 版本跟踪data list
         * @param isUpdateSupport Whether to update support, if it already exists, update the data
         * @param operName operating user
         * @return result
         */
        @Override
        public String importSystemVersionTrack(List<SystemVersionTrackRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("common.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (SystemVersionTrackRespVO respVO : importExcelList) {
                try {
                    SystemVersionTrackDO systemVersionTrackDO = BeanUtils.toBean(respVO, SystemVersionTrackDO.class);
                    Long systemVersionTrackId = respVO.getId();
                    if (isUpdateSupport) {
                        if (systemVersionTrackId != null) {
                            SystemVersionTrackDO existingSystemVersionTrack = systemVersionTrackMapper.selectById(systemVersionTrackId);
                            if (existingSystemVersionTrack != null) {
                                systemVersionTrackMapper.updateById(systemVersionTrackDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("common.import.update.success",
                                        "数据更新成功，ID为 " + systemVersionTrackId + " 的版本跟踪记录。", systemVersionTrackId, "版本跟踪"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("common.import.update.fail",
                                        "数据更新失败，ID为 " + systemVersionTrackId + " 的版本跟踪记录不存在。", systemVersionTrackId, "版本跟踪"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("common.import.update.fail",
                                    "数据更新失败，某条记录的ID不存在。", "版本跟踪"));
                        }
                    } else {
                        QueryWrapper<SystemVersionTrackDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", systemVersionTrackId);
                        SystemVersionTrackDO existingSystemVersionTrack = systemVersionTrackMapper.selectOne(queryWrapper);
                        if (existingSystemVersionTrack == null) {
                            systemVersionTrackMapper.insert(systemVersionTrackDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("common.import.insert.success",
                                    "数据插入成功，ID为 " + systemVersionTrackId + " 的版本跟踪记录。", systemVersionTrackId, "版本跟踪"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("common.import.insert.fail",
                                    "数据插入失败，ID为 " + systemVersionTrackId + " 的版本跟踪记录已存在。", systemVersionTrackId, "版本跟踪"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("common.import.error.detail",
                            "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("common.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("common.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("common.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }
}
