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
 */

package tech.qiantong.qdata.module.dg.service.dataLevel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aliyun.oss.ServiceException;
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
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataLevel.DgDataLevelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.dataLevel.DgDataLevelMapper;
import tech.qiantong.qdata.module.dg.service.dataLevel.IDgDataLevelService;
/**
 * 数据分级Service业务层处理
 *
 * @author qdata
 * @date 2026-04-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDataLevelServiceImpl  extends ServiceImpl<DgDataLevelMapper,DgDataLevelDO> implements IDgDataLevelService {
    @Resource
    private DgDataLevelMapper dgDataLevelMapper;

    @Override
    public PageResult<DgDataLevelDO> getDgDataLevelPage(DgDataLevelPageReqVO pageReqVO) {
        return dgDataLevelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDataLevel(DgDataLevelSaveReqVO createReqVO) {
        DgDataLevelDO dictType = BeanUtils.toBean(createReqVO, DgDataLevelDO.class);
        // 敏感等级不能重复
        if (dgDataLevelMapper.selectCount(new LambdaQueryWrapper<DgDataLevelDO>()
                .eq(DgDataLevelDO::getSensitiveLevel, dictType.getSensitiveLevel())) > 0) {
            throw new IllegalArgumentException("敏感等级不能重复");
        }
        dgDataLevelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDataLevel(DgDataLevelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据分级
        DgDataLevelDO updateObj = BeanUtils.toBean(updateReqVO, DgDataLevelDO.class);
        return dgDataLevelMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDataLevel(Collection<Long> idList) {
        // 批量删除数据分级
        return dgDataLevelMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDataLevelDO getDgDataLevelById(Long id) {
        return dgDataLevelMapper.selectById(id);
    }

    @Override
    public List<DgDataLevelDO> getDgDataLevelList() {
        return dgDataLevelMapper.selectList();
    }

    @Override
    public Map<Long, DgDataLevelDO> getDgDataLevelMap() {
        List<DgDataLevelDO> dgDataLevelList = dgDataLevelMapper.selectList();
        return dgDataLevelList.stream()
                .collect(Collectors.toMap(
                        DgDataLevelDO::getId,
                        dgDataLevelDO -> dgDataLevelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据分级数据
         *
         * @param importExcelList 数据分级数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDgDataLevel(List<DgDataLevelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDataLevelRespVO respVO : importExcelList) {
                try {
                    DgDataLevelDO dgDataLevelDO = BeanUtils.toBean(respVO, DgDataLevelDO.class);
                    Long dgDataLevelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDataLevelId != null) {
                            DgDataLevelDO existingDgDataLevel = dgDataLevelMapper.selectById(dgDataLevelId);
                            if (existingDgDataLevel != null) {
                                dgDataLevelMapper.updateById(dgDataLevelDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dgDataLevelId + " 的数据分级记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dgDataLevelId + " 的数据分级记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DgDataLevelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDataLevelId);
                        DgDataLevelDO existingDgDataLevel = dgDataLevelMapper.selectOne(queryWrapper);
                        if (existingDgDataLevel == null) {
                            dgDataLevelMapper.insert(dgDataLevelDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dgDataLevelId + " 的数据分级记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dgDataLevelId + " 的数据分级记录已存在。");
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

    @Override
    public List<DgDataLevelDO> getDgDataLevelListAll(DgDataLevelPageReqVO dgDataLevel) {
        return dgDataLevelMapper.selectList();
    }
}
