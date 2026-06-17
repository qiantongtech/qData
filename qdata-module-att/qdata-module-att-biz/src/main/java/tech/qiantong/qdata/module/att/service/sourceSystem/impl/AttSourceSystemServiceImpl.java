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

package tech.qiantong.qdata.module.att.service.sourceSystem.impl;

import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemRespVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;
import tech.qiantong.qdata.module.att.dal.mapper.sourceSystem.AttSourceSystemMapper;
import tech.qiantong.qdata.module.att.service.sourceSystem.IAttSourceSystemService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 来源系统Service业务层处理
 *
 * @author qdata
 * @date 2026-04-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttSourceSystemServiceImpl  extends ServiceImpl<AttSourceSystemMapper,AttSourceSystemDO> implements IAttSourceSystemService {
    @Resource
    private AttSourceSystemMapper attSourceSystemMapper;

    @Override
    public PageResult<AttSourceSystemDO> getAttSourceSystemPage(AttSourceSystemPageReqVO pageReqVO) {
        return attSourceSystemMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttSourceSystem(AttSourceSystemSaveReqVO createReqVO) {
        AttSourceSystemDO dictType = BeanUtils.toBean(createReqVO, AttSourceSystemDO.class);
        attSourceSystemMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttSourceSystem(AttSourceSystemSaveReqVO updateReqVO) {
        // 相关校验

        // 更新来源系统
        AttSourceSystemDO updateObj = BeanUtils.toBean(updateReqVO, AttSourceSystemDO.class);
        return attSourceSystemMapper.updateById(updateObj);
    }
    @Override
    public int removeAttSourceSystem(Collection<Long> idList) {
        //判断validFlag是否为true
        if (idList.stream().anyMatch(id -> attSourceSystemMapper.selectById(id).getValidFlag() == true)) {
            throw new ServiceException("att.error.source.system.enabled", "已启用的来源系统，不能删除！");
        }
        // 批量删除来源系统
        return attSourceSystemMapper.deleteBatchIds(idList);
    }

    @Override
    public AttSourceSystemDO getAttSourceSystemById(Long id) {
        return attSourceSystemMapper.selectById(id);
    }

    @Override
    public List<AttSourceSystemDO> getAttSourceSystemList() {
        return attSourceSystemMapper.selectList();
    }

    @Override
    public List<AttSourceSystemDO> getAttSourceSystemListByValidFlag(Boolean validFlag) {
        return attSourceSystemMapper.selectList(new LambdaQueryWrapperX<AttSourceSystemDO>().eqIfPresent(AttSourceSystemDO::getValidFlag, validFlag));
    }

    @Override
    public Map<Long, AttSourceSystemDO> getAttSourceSystemMap() {
        List<AttSourceSystemDO> attSourceSystemList = attSourceSystemMapper.selectList();
        return attSourceSystemList.stream()
                .collect(Collectors.toMap(
                        AttSourceSystemDO::getId,
                        attSourceSystemDO -> attSourceSystemDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入来源系统数据
         *
         * @param importExcelList 来源系统数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importAttSourceSystem(List<AttSourceSystemRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("att.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttSourceSystemRespVO respVO : importExcelList) {
                try {
                    AttSourceSystemDO attSourceSystemDO = BeanUtils.toBean(respVO, AttSourceSystemDO.class);
                    Long attSourceSystemId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attSourceSystemId != null) {
                            AttSourceSystemDO existingAttSourceSystem = attSourceSystemMapper.selectById(attSourceSystemId);
                            if (existingAttSourceSystem != null) {
                                attSourceSystemMapper.updateById(attSourceSystemDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                        "数据更新成功，ID为 " + attSourceSystemId + " 的来源系统记录。", attSourceSystemId, "来源系统"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                        "数据更新失败，ID为 " + attSourceSystemId + " 的来源系统记录不存在。", attSourceSystemId, "来源系统"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<AttSourceSystemDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attSourceSystemId);
                        AttSourceSystemDO existingAttSourceSystem = attSourceSystemMapper.selectOne(queryWrapper);
                        if (existingAttSourceSystem == null) {
                            attSourceSystemMapper.insert(attSourceSystemDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                    "数据插入成功，ID为 " + attSourceSystemId + " 的来源系统记录。", attSourceSystemId, "来源系统"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                    "数据插入失败，ID为 " + attSourceSystemId + " 的来源系统记录已存在。", attSourceSystemId, "来源系统"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("att.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("att.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("att.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("att.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }
}
