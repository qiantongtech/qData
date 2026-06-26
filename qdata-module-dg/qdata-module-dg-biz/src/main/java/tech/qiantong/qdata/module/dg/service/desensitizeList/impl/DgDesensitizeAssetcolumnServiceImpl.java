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

package tech.qiantong.qdata.module.dg.service.desensitizeList.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.desensitizeList.DgDesensitizeAssetcolumnMapper;
import tech.qiantong.qdata.module.dg.service.desensitizeList.IDgDesensitizeAssetcolumnService;
/**
 * 脱敏清单关联关系Service业务层处理
 *
 * @author qdata
 * @date 2026-04-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeAssetcolumnServiceImpl  extends ServiceImpl<DgDesensitizeAssetcolumnMapper,DgDesensitizeAssetcolumnDO> implements IDgDesensitizeAssetcolumnService {
    @Resource
    private DgDesensitizeAssetcolumnMapper dgDesensitizeAssetcolumnMapper;

    @Override
    public PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnPage(DgDesensitizeAssetcolumnPageReqVO pageReqVO) {
        return dgDesensitizeAssetcolumnMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO createReqVO) {
        DgDesensitizeAssetcolumnDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeAssetcolumnDO.class);
        dgDesensitizeAssetcolumnMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO updateReqVO) {
        // 相关校验

        // 更新脱敏清单关联关系
        DgDesensitizeAssetcolumnDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeAssetcolumnDO.class);
        return dgDesensitizeAssetcolumnMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeAssetcolumn(Collection<Long> idList) {
        // 批量删除脱敏清单关联关系
        return dgDesensitizeAssetcolumnMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnById(Long id) {
        return dgDesensitizeAssetcolumnMapper.selectDesensitizeAssetcolumnById(id);
    }

    @Override
    public DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnByAid(Long assetcolumnId) {
        //通过 assetcolumnId 获取
        return dgDesensitizeAssetcolumnMapper.selectOne( new LambdaQueryWrapper<DgDesensitizeAssetcolumnDO>().eq(DgDesensitizeAssetcolumnDO::getAssetcolumnId, assetcolumnId));
    }

    @Override
    public List<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnList() {
        return dgDesensitizeAssetcolumnMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnMap() {
        List<DgDesensitizeAssetcolumnDO> dgDesensitizeAssetcolumnList = dgDesensitizeAssetcolumnMapper.selectList();
        return dgDesensitizeAssetcolumnList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeAssetcolumnDO::getId,
                        dgDesensitizeAssetcolumnDO -> dgDesensitizeAssetcolumnDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入脱敏清单关联关系数据
         *
         * @param importExcelList 脱敏清单关联关系数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDgDesensitizeAssetcolumn(List<DgDesensitizeAssetcolumnRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dg.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeAssetcolumnRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeAssetcolumnDO dgDesensitizeAssetcolumnDO = BeanUtils.toBean(respVO, DgDesensitizeAssetcolumnDO.class);
                    Long dgDesensitizeAssetcolumnId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeAssetcolumnId != null) {
                            DgDesensitizeAssetcolumnDO existingDgDesensitizeAssetcolumn = dgDesensitizeAssetcolumnMapper.selectById(dgDesensitizeAssetcolumnId);
                            if (existingDgDesensitizeAssetcolumn != null) {
                                dgDesensitizeAssetcolumnMapper.updateById(dgDesensitizeAssetcolumnDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                        "数据更新成功，ID为 " + dgDesensitizeAssetcolumnId + " 的脱敏清单关联关系记录。", dgDesensitizeAssetcolumnId, "脱敏清单关联关系"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                        "数据更新失败，ID为 " + dgDesensitizeAssetcolumnId + " 的脱敏清单关联关系记录不存在。", dgDesensitizeAssetcolumnId, "脱敏清单关联关系"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DgDesensitizeAssetcolumnDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeAssetcolumnId);
                        DgDesensitizeAssetcolumnDO existingDgDesensitizeAssetcolumn = dgDesensitizeAssetcolumnMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeAssetcolumn == null) {
                            dgDesensitizeAssetcolumnMapper.insert(dgDesensitizeAssetcolumnDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                    "数据插入成功，ID为 " + dgDesensitizeAssetcolumnId + " 的脱敏清单关联关系记录。", dgDesensitizeAssetcolumnId, "脱敏清单关联关系"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                    "数据插入失败，ID为 " + dgDesensitizeAssetcolumnId + " 的脱敏清单关联关系记录已存在。", dgDesensitizeAssetcolumnId, "脱敏清单关联关系"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dg.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("dg.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }

    @Override
    public PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizePagebyRuleId(DgDesensitizeAssetcolumnPageReqVO dgDesensitizeAssetcolumn) {
            return dgDesensitizeAssetcolumnMapper.selectPagebyRuleId(dgDesensitizeAssetcolumn);
    }

    @Override
    public DgDesensitizeAssetcolumnDO getByassetcolumnId(Long assetcolumnId) {
            return dgDesensitizeAssetcolumnMapper.selectOne( new LambdaQueryWrapper<DgDesensitizeAssetcolumnDO>().eq(DgDesensitizeAssetcolumnDO::getAssetcolumnId, assetcolumnId));
    }

    @Override
    public int deleteByassetcolumnId(Long assetcolumnId) {
            return dgDesensitizeAssetcolumnMapper.delete(new LambdaQueryWrapper<DgDesensitizeAssetcolumnDO>().eq(DgDesensitizeAssetcolumnDO::getAssetcolumnId, assetcolumnId));
    }

}
