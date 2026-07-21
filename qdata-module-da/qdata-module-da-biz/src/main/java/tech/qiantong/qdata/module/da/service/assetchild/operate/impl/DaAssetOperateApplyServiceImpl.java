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

package tech.qiantong.qdata.module.da.service.assetchild.operate.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateApplyDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.operate.DaAssetOperateApplyMapper;
import tech.qiantong.qdata.module.da.service.assetchild.operate.IDaAssetOperateApplyService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data asset operation application Service business layer processing
 *
 * @author qdata
 * @date 2025-05-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetOperateApplyServiceImpl  extends ServiceImpl<DaAssetOperateApplyMapper,DaAssetOperateApplyDO> implements IDaAssetOperateApplyService {
    @Resource
    private DaAssetOperateApplyMapper daAssetOperateApplyMapper;

    @Override
    public PageResult<DaAssetOperateApplyDO> getDaAssetOperateApplyPage(DaAssetOperateApplyPageReqVO pageReqVO) {
        return daAssetOperateApplyMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetOperateApply(DaAssetOperateApplySaveReqVO createReqVO) {
        DaAssetOperateApplyDO dictType = BeanUtils.toBean(createReqVO, DaAssetOperateApplyDO.class);
        daAssetOperateApplyMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetOperateApply(DaAssetOperateApplySaveReqVO updateReqVO) {
        // Related validation

        // Update data asset operation application
        DaAssetOperateApplyDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetOperateApplyDO.class);
        return daAssetOperateApplyMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetOperateApply(Collection<Long> idList) {
        // Batch delete data asset operation applications
        return daAssetOperateApplyMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetOperateApplyDO getDaAssetOperateApplyById(Long id) {
        return daAssetOperateApplyMapper.selectById(id);
    }

    @Override
    public List<DaAssetOperateApplyDO> getDaAssetOperateApplyList() {
        return daAssetOperateApplyMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetOperateApplyDO> getDaAssetOperateApplyMap() {
        List<DaAssetOperateApplyDO> daAssetOperateApplyList = daAssetOperateApplyMapper.selectList();
        return daAssetOperateApplyList.stream()
                .collect(Collectors.toMap(
                        DaAssetOperateApplyDO::getId,
                        daAssetOperateApplyDO -> daAssetOperateApplyDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data asset operation application data
         *
         * @param importExcelList Data asset operation application data list
         * @param isUpdateSupport Whether to support update, if already exists, update the data
         * @param operName Operating user
         * @return Result
         */
        @Override
        public String importDaAssetOperateApply(List<DaAssetOperateApplyRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetOperateApplyRespVO respVO : importExcelList) {
                try {
                    DaAssetOperateApplyDO daAssetOperateApplyDO = BeanUtils.toBean(respVO, DaAssetOperateApplyDO.class);
                    Long daAssetOperateApplyId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetOperateApplyId != null) {
                            DaAssetOperateApplyDO existingDaAssetOperateApply = daAssetOperateApplyMapper.selectById(daAssetOperateApplyId);
                            if (existingDaAssetOperateApply != null) {
                                daAssetOperateApplyMapper.updateById(daAssetOperateApplyDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                        "Data update successful, ID {0} {1} record.", daAssetOperateApplyId, MessageUtils.messageWithFallback("da.entity.asset.operation.request", "Data asset operation request")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", daAssetOperateApplyId, MessageUtils.messageWithFallback("da.entity.asset.operation.request", "Data asset operation request")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DaAssetOperateApplyDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetOperateApplyId);
                        DaAssetOperateApplyDO existingDaAssetOperateApply = daAssetOperateApplyMapper.selectOne(queryWrapper);
                        if (existingDaAssetOperateApply == null) {
                            daAssetOperateApplyMapper.insert(daAssetOperateApplyDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", daAssetOperateApplyId, MessageUtils.messageWithFallback("da.entity.asset.operation.request", "Data asset operation request")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", daAssetOperateApplyId, MessageUtils.messageWithFallback("da.entity.asset.operation.request", "Data asset operation request")));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                        "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                        failureNum, failureDetails));
                throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                        "Congratulations! All data imported! Total: {0} records.", successNum));
            }
            return resultMsg.toString();
        }
}
