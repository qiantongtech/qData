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

package tech.qiantong.qdata.module.da.service.discovery.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;
import tech.qiantong.qdata.module.da.dal.mapper.discovery.DaDiscoveryColumnMapper;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryColumnService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据发现字段Service业务层处理
 *
 * @author qdata
 * @date 2025-02-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDiscoveryColumnServiceImpl  extends ServiceImpl<DaDiscoveryColumnMapper,DaDiscoveryColumnDO> implements IDaDiscoveryColumnService {
    @Resource
    private DaDiscoveryColumnMapper daDiscoveryColumnMapper;

    @Override
    public PageResult<DaDiscoveryColumnDO> getDaDiscoveryColumnPage(DaDiscoveryColumnPageReqVO pageReqVO) {
        return daDiscoveryColumnMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DaDiscoveryColumnDO> getDaDiscoveryColumnList(DaDiscoveryColumnPageReqVO reqVO) {
        MPJLambdaWrapper<DaDiscoveryColumnDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DaDiscoveryColumnDO.class)
                .eq(reqVO.getTaskId() != null, DaDiscoveryColumnDO::getTaskId, reqVO.getTaskId())
                .eq( reqVO.getTableId() != null, DaDiscoveryColumnDO::getTableId, reqVO.getTableId())
                .like(StringUtils.isNotBlank(reqVO.getColumnName()), DaDiscoveryColumnDO::getColumnName, reqVO.getColumnName())
                .eq(StringUtils.isNotBlank(reqVO.getColumnComment()), DaDiscoveryColumnDO::getColumnComment, reqVO.getColumnComment())
                .eq(StringUtils.isNotBlank(reqVO.getColumnType()), DaDiscoveryColumnDO::getColumnType, reqVO.getColumnType())
                .eq(StringUtils.isNotBlank(reqVO.getNullableFlag()), DaDiscoveryColumnDO::getNullableFlag, reqVO.getNullableFlag())
                .eq(StringUtils.isNotBlank(reqVO.getPkFlag()), DaDiscoveryColumnDO::getPkFlag, reqVO.getPkFlag())
                .eq(StringUtils.isNotBlank(reqVO.getDefaultValue()), DaDiscoveryColumnDO::getDefaultValue, reqVO.getDefaultValue());

        return daDiscoveryColumnMapper.selectList(wrapper);
    }

    @Override
    public Long createDaDiscoveryColumn(DaDiscoveryColumnSaveReqVO createReqVO) {
        DaDiscoveryColumnDO dictType = BeanUtils.toBean(createReqVO, DaDiscoveryColumnDO.class);
        daDiscoveryColumnMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public Long createDaDiscoveryColumn(DaDiscoveryColumnDO createReqVO) {
        daDiscoveryColumnMapper.insert(createReqVO);
        return createReqVO.getId();
    }

    @Override
    public int updateDaDiscoveryColumn(DaDiscoveryColumnSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据发现字段
        DaDiscoveryColumnDO updateObj = BeanUtils.toBean(updateReqVO, DaDiscoveryColumnDO.class);
        return daDiscoveryColumnMapper.updateById(updateObj);
    }

    @Override
    public int updateDaDiscoveryColumn(DaDiscoveryColumnDO updateReqVO) {
        // 相关校验

        // 更新数据发现字段
        return daDiscoveryColumnMapper.updateById(updateReqVO);
    }
    @Override
    public int removeDaDiscoveryColumn(Collection<Long> idList) {
        // 批量删除数据发现字段
        return daDiscoveryColumnMapper.deleteBatchIds(idList);
    }

    @Override
    public DaDiscoveryColumnDO getDaDiscoveryColumnById(Long id) {
        return daDiscoveryColumnMapper.selectById(id);
    }

    @Override
    public List<DaDiscoveryColumnDO> getDaDiscoveryColumnList() {
        return daDiscoveryColumnMapper.selectList();
    }

    @Override
    public Map<Long, DaDiscoveryColumnDO> getDaDiscoveryColumnMap() {
        List<DaDiscoveryColumnDO> daDiscoveryColumnList = daDiscoveryColumnMapper.selectList();
        return daDiscoveryColumnList.stream()
                .collect(Collectors.toMap(
                        DaDiscoveryColumnDO::getId,
                        daDiscoveryColumnDO -> daDiscoveryColumnDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据发现字段数据
         *
         * @param importExcelList 数据发现字段数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDaDiscoveryColumn(List<DaDiscoveryColumnRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaDiscoveryColumnRespVO respVO : importExcelList) {
                try {
                    DaDiscoveryColumnDO daDiscoveryColumnDO = BeanUtils.toBean(respVO, DaDiscoveryColumnDO.class);
                    Long daDiscoveryColumnId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daDiscoveryColumnId != null) {
                            DaDiscoveryColumnDO existingDaDiscoveryColumn = daDiscoveryColumnMapper.selectById(daDiscoveryColumnId);
                            if (existingDaDiscoveryColumn != null) {
                                daDiscoveryColumnMapper.updateById(daDiscoveryColumnDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                        "数据更新成功，ID为 " + daDiscoveryColumnId + " 的数据发现字段记录。", daDiscoveryColumnId, "数据发现字段"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "数据更新失败，ID为 " + daDiscoveryColumnId + " 的数据发现字段记录不存在。", daDiscoveryColumnId, "数据发现字段"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DaDiscoveryColumnDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daDiscoveryColumnId);
                        DaDiscoveryColumnDO existingDaDiscoveryColumn = daDiscoveryColumnMapper.selectOne(queryWrapper);
                        if (existingDaDiscoveryColumn == null) {
                            daDiscoveryColumnMapper.insert(daDiscoveryColumnDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "数据插入成功，ID为 " + daDiscoveryColumnId + " 的数据发现字段记录。", daDiscoveryColumnId, "数据发现字段"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "数据插入失败，ID为 " + daDiscoveryColumnId + " 的数据发现字段记录已存在。", daDiscoveryColumnId, "数据发现字段"));
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
