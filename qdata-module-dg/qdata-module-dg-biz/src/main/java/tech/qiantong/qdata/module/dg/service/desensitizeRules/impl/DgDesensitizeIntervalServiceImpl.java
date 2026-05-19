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

package tech.qiantong.qdata.module.dg.service.desensitizeRules.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aliyun.oss.ServiceException;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;
import tech.qiantong.qdata.module.dg.dal.mapper.desensitizeRules.DgDesensitizeIntervalMapper;
import tech.qiantong.qdata.module.dg.service.desensitizeRules.IDgDesensitizeIntervalService;
/**
 * 脱敏区间Service业务层处理
 *
 * @author qdata
 * @date 2026-04-10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeIntervalServiceImpl  extends ServiceImpl<DgDesensitizeIntervalMapper,DgDesensitizeIntervalDO> implements IDgDesensitizeIntervalService {
    @Resource
    private DgDesensitizeIntervalMapper dgDesensitizeIntervalMapper;

    @Override
    public PageResult<DgDesensitizeIntervalDO> getDgDesensitizeIntervalPage(DgDesensitizeIntervalPageReqVO pageReqVO) {
        return dgDesensitizeIntervalMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO createReqVO) {
        DgDesensitizeIntervalDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeIntervalDO.class);
        dgDesensitizeIntervalMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO updateReqVO) {
        // 相关校验

        // 更新脱敏区间
        DgDesensitizeIntervalDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeIntervalDO.class);
        return dgDesensitizeIntervalMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeInterval(Collection<Long> idList) {
        // 批量删除脱敏区间
        return dgDesensitizeIntervalMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDesensitizeIntervalDO getDgDesensitizeIntervalById(Long id) {
        return dgDesensitizeIntervalMapper.selectById(id);
    }

    @Override
    public List<DgDesensitizeIntervalDO> getDgDesensitizeIntervalList() {
        return dgDesensitizeIntervalMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeIntervalDO> getDgDesensitizeIntervalMap() {
        List<DgDesensitizeIntervalDO> dgDesensitizeIntervalList = dgDesensitizeIntervalMapper.selectList();
        return dgDesensitizeIntervalList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeIntervalDO::getId,
                        dgDesensitizeIntervalDO -> dgDesensitizeIntervalDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入脱敏区间数据
         *
         * @param importExcelList 脱敏区间数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDgDesensitizeInterval(List<DgDesensitizeIntervalRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeIntervalRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeIntervalDO dgDesensitizeIntervalDO = BeanUtils.toBean(respVO, DgDesensitizeIntervalDO.class);
                    Long dgDesensitizeIntervalId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeIntervalId != null) {
                            DgDesensitizeIntervalDO existingDgDesensitizeInterval = dgDesensitizeIntervalMapper.selectById(dgDesensitizeIntervalId);
                            if (existingDgDesensitizeInterval != null) {
                                dgDesensitizeIntervalMapper.updateById(dgDesensitizeIntervalDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dgDesensitizeIntervalId + " 的脱敏区间记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dgDesensitizeIntervalId + " 的脱敏区间记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DgDesensitizeIntervalDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeIntervalId);
                        DgDesensitizeIntervalDO existingDgDesensitizeInterval = dgDesensitizeIntervalMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeInterval == null) {
                            dgDesensitizeIntervalMapper.insert(dgDesensitizeIntervalDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dgDesensitizeIntervalId + " 的脱敏区间记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dgDesensitizeIntervalId + " 的脱敏区间记录已存在。");
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
