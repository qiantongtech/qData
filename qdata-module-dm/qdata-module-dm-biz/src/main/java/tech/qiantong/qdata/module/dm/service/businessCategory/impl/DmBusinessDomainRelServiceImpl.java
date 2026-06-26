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

package tech.qiantong.qdata.module.dm.service.businessCategory.impl;

import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;
import tech.qiantong.qdata.module.dm.dal.mapper.businessCategory.DmBusinessDomainRelMapper;
import tech.qiantong.qdata.module.dm.service.businessCategory.IDmBusinessDomainRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务分类数据域关联关系Service业务层处理
 *
 * @author qdata
 * @date 2026-04-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmBusinessDomainRelServiceImpl extends ServiceImpl<DmBusinessDomainRelMapper,DmBusinessDomainRelDO> implements IDmBusinessDomainRelService {
    @Resource
    private DmBusinessDomainRelMapper dmBusinessDomainRelMapper;

    @Override
    public PageResult<DmBusinessDomainRelDO> getDmBusinessDomainRelPage(DmBusinessDomainRelPageReqVO pageReqVO) {
        return dmBusinessDomainRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmBusinessDomainRel(DmBusinessDomainRelSaveReqVO createReqVO) {
        DmBusinessDomainRelDO dictType = BeanUtils.toBean(createReqVO, DmBusinessDomainRelDO.class);
        dmBusinessDomainRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmBusinessDomainRel(DmBusinessDomainRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新业务分类数据域关联关系
        DmBusinessDomainRelDO updateObj = BeanUtils.toBean(updateReqVO, DmBusinessDomainRelDO.class);
        return dmBusinessDomainRelMapper.updateById(updateObj);
    }
    @Override
    public int removeDmBusinessDomainRel(Collection<Long> idList) {
        // 批量删除业务分类数据域关联关系
        return dmBusinessDomainRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DmBusinessDomainRelDO getDmBusinessDomainRelById(Long id) {
        return dmBusinessDomainRelMapper.selectById(id);
    }

    @Override
    public List<DmBusinessDomainRelDO> getDmBusinessDomainRelList() {
        return dmBusinessDomainRelMapper.selectList();
    }

    @Override
    public Map<Long, DmBusinessDomainRelDO> getDmBusinessDomainRelMap() {
        List<DmBusinessDomainRelDO> dmBusinessDomainRelList = dmBusinessDomainRelMapper.selectList();
        return dmBusinessDomainRelList.stream()
                .collect(Collectors.toMap(
                        DmBusinessDomainRelDO::getId,
                        dmBusinessDomainRelDO -> dmBusinessDomainRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入业务分类数据域关联关系数据
         *
         * @param importExcelList 业务分类数据域关联关系数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDmBusinessDomainRel(List<DmBusinessDomainRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dm.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DmBusinessDomainRelRespVO respVO : importExcelList) {
                try {
                    DmBusinessDomainRelDO dmBusinessDomainRelDO = BeanUtils.toBean(respVO, DmBusinessDomainRelDO.class);
                    Long dmBusinessDomainRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dmBusinessDomainRelId != null) {
                            DmBusinessDomainRelDO existingDmBusinessDomainRel = dmBusinessDomainRelMapper.selectById(dmBusinessDomainRelId);
                            if (existingDmBusinessDomainRel != null) {
                                dmBusinessDomainRelMapper.updateById(dmBusinessDomainRelDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dm.import.update.success",
                                        "数据更新成功，ID为 " + dmBusinessDomainRelId + " 的业务分类数据域关联关系记录。", dmBusinessDomainRelId, "业务分类数据域关联关系"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.fail",
                                        "数据更新失败，ID为 " + dmBusinessDomainRelId + " 的业务分类数据域关联关系记录不存在。", dmBusinessDomainRelId, "业务分类数据域关联关系"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DmBusinessDomainRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dmBusinessDomainRelId);
                        DmBusinessDomainRelDO existingDmBusinessDomainRel = dmBusinessDomainRelMapper.selectOne(queryWrapper);
                        if (existingDmBusinessDomainRel == null) {
                            dmBusinessDomainRelMapper.insert(dmBusinessDomainRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dm.import.insert.success",
                                    "数据插入成功，ID为 " + dmBusinessDomainRelId + " 的业务分类数据域关联关系记录。", dmBusinessDomainRelId, "业务分类数据域关联关系"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.insert.fail",
                                    "数据插入失败，ID为 " + dmBusinessDomainRelId + " 的业务分类数据域关联关系记录已存在。", dmBusinessDomainRelId, "业务分类数据域关联关系"));
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
    public Integer removeDmBusinessDomainRelByDomainId(Long domainId, Long businessCategoryId) {
         if (domainId == null || businessCategoryId == null || businessCategoryId == 0) {
              throw new ServiceException("dm.error.id.empty", "数据域ID或业务分类ID不能为空！");
         }
        //根据数据域ID和业务分类ID删除关联关系
        return dmBusinessDomainRelMapper.delete(new LambdaQueryWrapper<DmBusinessDomainRelDO>()
               .eq(DmBusinessDomainRelDO::getDataDomainId, domainId)
               .eq(DmBusinessDomainRelDO::getBusinessCategoryId, businessCategoryId));
    }
}
