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

package tech.qiantong.qdata.module.dm.service.dm.impl;

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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;
import tech.qiantong.qdata.module.dm.dal.mapper.dm.DmDataDomainMapper;
import tech.qiantong.qdata.module.dm.service.dm.IDmDataDomainService;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * 数据域管理Service业务层处理
 *
 * @author FXB
 * @date 2026-03-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmDataDomainServiceImpl  extends ServiceImpl<DmDataDomainMapper,DmDataDomainDO> implements IDmDataDomainService {
    @Resource
    private DmDataDomainMapper dmDataDomainMapper;

    @Override
    public PageResult<DmDataDomainDO> getDmDataDomainPage(DmDataDomainPageReqVO pageReqVO) {
        return dmDataDomainMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmDataDomain(DmDataDomainSaveReqVO createReqVO) {
        DmDataDomainDO dictType = BeanUtils.toBean(createReqVO, DmDataDomainDO.class);
        dmDataDomainMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmDataDomain(DmDataDomainSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据域管理
        DmDataDomainDO updateObj = BeanUtils.toBean(updateReqVO, DmDataDomainDO.class);
        return dmDataDomainMapper.updateById(updateObj);
    }
    @Override
    public int removeDmDataDomain(Collection<Long> idList) {
        // 批量删除数据域管理
        return dmDataDomainMapper.deleteBatchIds(idList);
    }

    @Override
    public DmDataDomainDO getDmDataDomainById(Long id) {
        MPJLambdaWrapperX<DmDataDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmDataDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName","u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .eq(DmDataDomainDO::getId, id);
        return dmDataDomainMapper.selectOne(lambdaWrapper);
    }

    @Override
    public List<DmDataDomainDO> getDmDataDomainList() {
        return dmDataDomainMapper.selectList();
    }

    @Override
    public Map<Long, DmDataDomainDO> getDmDataDomainMap() {
        List<DmDataDomainDO> dmDataDomainList = dmDataDomainMapper.selectList();
        return dmDataDomainList.stream()
                .collect(Collectors.toMap(
                        DmDataDomainDO::getId,
                        dmDataDomainDO -> dmDataDomainDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据域管理数据
         *
         * @param importExcelList 数据域管理数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDmDataDomain(List<DmDataDomainRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dm.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DmDataDomainRespVO respVO : importExcelList) {
                try {
                    DmDataDomainDO dmDataDomainDO = BeanUtils.toBean(respVO, DmDataDomainDO.class);
                    Long dmDataDomainId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dmDataDomainId != null) {
                            DmDataDomainDO existingDmDataDomain = dmDataDomainMapper.selectById(dmDataDomainId);
                            if (existingDmDataDomain != null) {
                                dmDataDomainMapper.updateById(dmDataDomainDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dm.import.update.success",
                                        "数据更新成功，ID为 " + dmDataDomainId + " 的数据域管理记录。", dmDataDomainId, "数据域管理"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.fail",
                                        "数据更新失败，ID为 " + dmDataDomainId + " 的数据域管理记录不存在。", dmDataDomainId, "数据域管理"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DmDataDomainDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dmDataDomainId);
                        DmDataDomainDO existingDmDataDomain = dmDataDomainMapper.selectOne(queryWrapper);
                        if (existingDmDataDomain == null) {
                            dmDataDomainMapper.insert(dmDataDomainDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dm.import.insert.success",
                                    "数据插入成功，ID为 " + dmDataDomainId + " 的数据域管理记录。", dmDataDomainId, "数据域管理"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.insert.fail",
                                    "数据插入失败，ID为 " + dmDataDomainId + " 的数据域管理记录已存在。", dmDataDomainId, "数据域管理"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dm.import.error.detail",
                            "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dm.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("dm.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dm.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }

    @Override
    public PageResult<DmDataDomainDO> getDmDataDomainByCategoryId(DmDataDomainPageReqVO dmDataDomain) {
        return dmDataDomainMapper.selectlistBybusinessDomainId(dmDataDomain);
    }
}
