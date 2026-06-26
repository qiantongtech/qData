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

package tech.qiantong.qdata.module.da.service.assetchild.geo.impl;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.geo.DaAssetGeoDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.geo.DaAssetGeoMapper;
import tech.qiantong.qdata.module.da.service.assetchild.geo.IDaAssetGeoService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据资产-矢量Service业务层处理
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetGeoServiceImpl  extends ServiceImpl<DaAssetGeoMapper,DaAssetGeoDO> implements IDaAssetGeoService {
    @Resource
    private DaAssetGeoMapper daAssetGeoMapper;

    @Override
    public PageResult<DaAssetGeoDO> getDaAssetGeoPage(DaAssetGeoPageReqVO pageReqVO) {
        return daAssetGeoMapper.selectPage(pageReqVO);
    }

    @Override
    public DaAssetGeoRespVO getDaAssetGeoByAssetId(Long assetId) {
        LambdaQueryWrapperX<DaAssetGeoDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(DaAssetGeoDO::getAssetId,assetId);
        DaAssetGeoDO daAssetApiDO = daAssetGeoMapper.selectOne(queryWrapperX);
        return BeanUtils.toBean(daAssetApiDO, DaAssetGeoRespVO.class);
    }

    @Override
    public Long createDaAssetGeo(DaAssetGeoSaveReqVO createReqVO) {
        DaAssetGeoDO dictType = BeanUtils.toBean(createReqVO, DaAssetGeoDO.class);
        daAssetGeoMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetGeo(DaAssetGeoSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产-矢量
        DaAssetGeoDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetGeoDO.class);
        return daAssetGeoMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetGeo(Collection<Long> idList) {
        // 批量删除数据资产-矢量
        return daAssetGeoMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetGeoDO getDaAssetGeoById(Long id) {
        return daAssetGeoMapper.selectById(id);
    }

    @Override
    public List<DaAssetGeoDO> getDaAssetGeoList() {
        return daAssetGeoMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetGeoDO> getDaAssetGeoMap() {
        List<DaAssetGeoDO> daAssetGeoList = daAssetGeoMapper.selectList();
        return daAssetGeoList.stream()
                .collect(Collectors.toMap(
                        DaAssetGeoDO::getId,
                        daAssetGeoDO -> daAssetGeoDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据资产-矢量数据
         *
         * @param importExcelList 数据资产-矢量数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDaAssetGeo(List<DaAssetGeoRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetGeoRespVO respVO : importExcelList) {
                try {
                    DaAssetGeoDO daAssetGeoDO = BeanUtils.toBean(respVO, DaAssetGeoDO.class);
                    Long daAssetGeoId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetGeoId != null) {
                            DaAssetGeoDO existingDaAssetGeo = daAssetGeoMapper.selectById(daAssetGeoId);
                            if (existingDaAssetGeo != null) {
                                daAssetGeoMapper.updateById(daAssetGeoDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                        "数据更新成功，ID为 " + daAssetGeoId + " 的数据资产-矢量记录。", daAssetGeoId, "数据资产-矢量"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "数据更新失败，ID为 " + daAssetGeoId + " 的数据资产-矢量记录不存在。", daAssetGeoId, "数据资产-矢量"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DaAssetGeoDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetGeoId);
                        DaAssetGeoDO existingDaAssetGeo = daAssetGeoMapper.selectOne(queryWrapper);
                        if (existingDaAssetGeo == null) {
                            daAssetGeoMapper.insert(daAssetGeoDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "数据插入成功，ID为 " + daAssetGeoId + " 的数据资产-矢量记录。", daAssetGeoId, "数据资产-矢量"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "数据插入失败，ID为 " + daAssetGeoId + " 的数据资产-矢量记录已存在。", daAssetGeoId, "数据资产-矢量"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }
}
