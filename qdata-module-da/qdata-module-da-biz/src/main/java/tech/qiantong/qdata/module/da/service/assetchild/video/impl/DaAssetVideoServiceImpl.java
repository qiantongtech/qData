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

package tech.qiantong.qdata.module.da.service.assetchild.video.impl;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.video.DaAssetVideoMapper;
import tech.qiantong.qdata.module.da.service.assetchild.video.IDaAssetVideoService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data asset-video data Service business layer processing
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetVideoServiceImpl  extends ServiceImpl<DaAssetVideoMapper,DaAssetVideoDO> implements IDaAssetVideoService {
    @Resource
    private DaAssetVideoMapper daAssetVideoMapper;

    @Override
    public PageResult<DaAssetVideoDO> getDaAssetVideoPage(DaAssetVideoPageReqVO pageReqVO) {
        return daAssetVideoMapper.selectPage(pageReqVO);
    }

    @Override
    public DaAssetVideoRespVO getDaAssetVideoByAssetId(Long assetId) {
        LambdaQueryWrapperX<DaAssetVideoDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(DaAssetVideoDO::getAssetId,assetId);
        DaAssetVideoDO daAssetApiDO = daAssetVideoMapper.selectOne(queryWrapperX);
        return BeanUtils.toBean(daAssetApiDO, DaAssetVideoRespVO.class);
    }

    @Override
    public Long createDaAssetVideo(DaAssetVideoSaveReqVO createReqVO) {
        DaAssetVideoDO dictType = BeanUtils.toBean(createReqVO, DaAssetVideoDO.class);
        daAssetVideoMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetVideo(DaAssetVideoSaveReqVO updateReqVO) {
        // Validation checks

        // Update data asset-video data
        DaAssetVideoDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetVideoDO.class);
        return daAssetVideoMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetVideo(Collection<Long> idList) {
        // Batch delete data asset-video data
        return daAssetVideoMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetVideoDO getDaAssetVideoById(Long id) {
        return daAssetVideoMapper.selectById(id);
    }

    @Override
    public List<DaAssetVideoDO> getDaAssetVideoList() {
        return daAssetVideoMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetVideoDO> getDaAssetVideoMap() {
        List<DaAssetVideoDO> daAssetVideoList = daAssetVideoMapper.selectList();
        return daAssetVideoList.stream()
                .collect(Collectors.toMap(
                        DaAssetVideoDO::getId,
                        daAssetVideoDO -> daAssetVideoDO,
                        // Retain existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data asset-video data
         *
         * @param importExcelList Data asset-video data list
         * @param isUpdateSupport Whether to support update, if already exists, update the data
         * @param operName Operator user
         * @return Result
         */
        @Override
        public String importDaAssetVideo(List<DaAssetVideoRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetVideoRespVO respVO : importExcelList) {
                try {
                    DaAssetVideoDO daAssetVideoDO = BeanUtils.toBean(respVO, DaAssetVideoDO.class);
                    Long daAssetVideoId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetVideoId != null) {
                            DaAssetVideoDO existingDaAssetVideo = daAssetVideoMapper.selectById(daAssetVideoId);
                            if (existingDaAssetVideo != null) {
                                daAssetVideoMapper.updateById(daAssetVideoDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                        "Data update successful, ID {0} {1} record.", daAssetVideoId, MessageUtils.messageWithFallback("da.entity.asset.video", "Data asset video")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", daAssetVideoId, MessageUtils.messageWithFallback("da.entity.asset.video", "Data asset video")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DaAssetVideoDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetVideoId);
                        DaAssetVideoDO existingDaAssetVideo = daAssetVideoMapper.selectOne(queryWrapper);
                        if (existingDaAssetVideo == null) {
                            daAssetVideoMapper.insert(daAssetVideoDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", daAssetVideoId, MessageUtils.messageWithFallback("da.entity.asset.video", "Data asset video")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", daAssetVideoId, MessageUtils.messageWithFallback("da.entity.asset.video", "Data asset video")));
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

    @Override
    public void queryServiceForwarding(HttpServletResponse response, DaAssetVideoReqVO daAssetVideoReqVO) {

    }
}
