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

package tech.qiantong.qdata.module.da.service.assetchild.projectRel.impl;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.projectRel.DaAssetProjectRelDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.projectRel.DaAssetProjectRelMapper;
import tech.qiantong.qdata.module.da.service.assetchild.projectRel.IDaAssetProjectRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 数据资产与项目关联关系Service业务层处理
 *
 * @author qdata
 * @date 2025-04-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetProjectRelServiceImpl  extends ServiceImpl<DaAssetProjectRelMapper,DaAssetProjectRelDO> implements IDaAssetProjectRelService {
    @Resource
    private DaAssetProjectRelMapper daAssetProjectRelMapper;

    @Override
    public PageResult<DaAssetProjectRelDO> getDaAssetProjectRelPage(DaAssetProjectRelPageReqVO pageReqVO) {
        return daAssetProjectRelMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DaAssetProjectRelDO> getDaAssetProjectRelList(DaAssetProjectRelPageReqVO pageReqVO) {
        return null;
    }

    @Override
    public Long createDaAssetProjectRel(DaAssetProjectRelSaveReqVO createReqVO) {
        this.removeProjectRelByAssetId(createReqVO.getAssetId());
        DaAssetProjectRelDO dictType = BeanUtils.toBean(createReqVO, DaAssetProjectRelDO.class);
        daAssetProjectRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int removeProjectRelByAssetId(Long assetId) {
        daAssetProjectRelMapper.removeProjectRelByAssetId(assetId);
        return 1;
    }

    @Override
    public int updateDaAssetProjectRel(DaAssetProjectRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产与项目关联关系
        DaAssetProjectRelDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetProjectRelDO.class);
        return daAssetProjectRelMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetProjectRel(Collection<Long> idList) {
        // 批量删除数据资产与项目关联关系
        return daAssetProjectRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetProjectRelDO getDaAssetProjectRelById(Long id) {
        return daAssetProjectRelMapper.selectById(id);
    }

    @Override
    public List<DaAssetProjectRelDO> getDaAssetProjectRelList() {
        return daAssetProjectRelMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetProjectRelDO> getDaAssetProjectRelMap() {
        List<DaAssetProjectRelDO> daAssetProjectRelList = daAssetProjectRelMapper.selectList();
        return daAssetProjectRelList.stream()
                .collect(Collectors.toMap(
                        DaAssetProjectRelDO::getId,
                        daAssetProjectRelDO -> daAssetProjectRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据资产与项目关联关系数据
     *
     * @param importExcelList 数据资产与项目关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDaAssetProjectRel(List<DaAssetProjectRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetProjectRelRespVO respVO : importExcelList) {
            try {
                DaAssetProjectRelDO daAssetProjectRelDO = BeanUtils.toBean(respVO, DaAssetProjectRelDO.class);
                Long daAssetProjectRelId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetProjectRelId != null) {
                        DaAssetProjectRelDO existingDaAssetProjectRel = daAssetProjectRelMapper.selectById(daAssetProjectRelId);
                        if (existingDaAssetProjectRel != null) {
                            daAssetProjectRelMapper.updateById(daAssetProjectRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "数据更新成功，ID为 " + daAssetProjectRelId + " 的数据资产与项目关联关系记录。", daAssetProjectRelId, "数据资产与项目关联关系"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "数据更新失败，ID为 " + daAssetProjectRelId + " 的数据资产与项目关联关系记录不存在。", daAssetProjectRelId, "数据资产与项目关联关系"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DaAssetProjectRelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetProjectRelId);
                    DaAssetProjectRelDO existingDaAssetProjectRel = daAssetProjectRelMapper.selectOne(queryWrapper);
                    if (existingDaAssetProjectRel == null) {
                        daAssetProjectRelMapper.insert(daAssetProjectRelDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "数据插入成功，ID为 " + daAssetProjectRelId + " 的数据资产与项目关联关系记录。", daAssetProjectRelId, "数据资产与项目关联关系"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "数据插入失败，ID为 " + daAssetProjectRelId + " 的数据资产与项目关联关系记录已存在。", daAssetProjectRelId, "数据资产与项目关联关系"));
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
