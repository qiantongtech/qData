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

package tech.qiantong.qdata.module.dg.service.whitelist.impl;

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
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.whitelist.DgDesensitizeUserRelMapper;
import tech.qiantong.qdata.module.dg.service.whitelist.IDgDesensitizeUserRelService;
/**
 * 脱敏白名单与用户关联关系Service业务层处理
 *
 * @author qdata
 * @date 2026-04-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeUserRelServiceImpl  extends ServiceImpl<DgDesensitizeUserRelMapper,DgDesensitizeUserRelDO> implements IDgDesensitizeUserRelService {
    @Resource
    private DgDesensitizeUserRelMapper dgDesensitizeUserRelMapper;

    @Override
    public PageResult<DgDesensitizeUserRelDO> getDgDesensitizeUserRelPage(DgDesensitizeUserRelPageReqVO pageReqVO) {
        return dgDesensitizeUserRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDesensitizeUserRel(DgDesensitizeUserRelSaveReqVO createReqVO) {
        DgDesensitizeUserRelDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeUserRelDO.class);
        dgDesensitizeUserRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDesensitizeUserRel(DgDesensitizeUserRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新脱敏白名单与用户关联关系
        DgDesensitizeUserRelDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeUserRelDO.class);
        return dgDesensitizeUserRelMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeUserRel(Collection<Long> idList) {
        // 批量删除脱敏白名单与用户关联关系
        return dgDesensitizeUserRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDesensitizeUserRelDO getDgDesensitizeUserRelById(Long id) {
        return dgDesensitizeUserRelMapper.selectById(id);
    }

    @Override
    public List<DgDesensitizeUserRelDO> getDgDesensitizeUserRelList() {
        return dgDesensitizeUserRelMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeUserRelDO> getDgDesensitizeUserRelMap() {
        List<DgDesensitizeUserRelDO> dgDesensitizeUserRelList = dgDesensitizeUserRelMapper.selectList();
        return dgDesensitizeUserRelList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeUserRelDO::getId,
                        dgDesensitizeUserRelDO -> dgDesensitizeUserRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入脱敏白名单与用户关联关系数据
         *
         * @param importExcelList 脱敏白名单与用户关联关系数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDgDesensitizeUserRel(List<DgDesensitizeUserRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeUserRelRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeUserRelDO dgDesensitizeUserRelDO = BeanUtils.toBean(respVO, DgDesensitizeUserRelDO.class);
                    Long dgDesensitizeUserRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeUserRelId != null) {
                            DgDesensitizeUserRelDO existingDgDesensitizeUserRel = dgDesensitizeUserRelMapper.selectById(dgDesensitizeUserRelId);
                            if (existingDgDesensitizeUserRel != null) {
                                dgDesensitizeUserRelMapper.updateById(dgDesensitizeUserRelDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dgDesensitizeUserRelId + " 的脱敏白名单与用户关联关系记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dgDesensitizeUserRelId + " 的脱敏白名单与用户关联关系记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DgDesensitizeUserRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeUserRelId);
                        DgDesensitizeUserRelDO existingDgDesensitizeUserRel = dgDesensitizeUserRelMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeUserRel == null) {
                            dgDesensitizeUserRelMapper.insert(dgDesensitizeUserRelDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dgDesensitizeUserRelId + " 的脱敏白名单与用户关联关系记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dgDesensitizeUserRelId + " 的脱敏白名单与用户关联关系记录已存在。");
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
