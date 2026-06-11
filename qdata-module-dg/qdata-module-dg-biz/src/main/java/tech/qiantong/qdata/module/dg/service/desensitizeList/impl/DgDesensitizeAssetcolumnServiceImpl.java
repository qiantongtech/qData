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

package tech.qiantong.qdata.module.dg.service.desensitizeList.impl;

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
                throw new ServiceException("导入数据不能为空！");
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
                                successMessages.add("数据更新成功，ID为 " + dgDesensitizeAssetcolumnId + " 的脱敏清单关联关系记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dgDesensitizeAssetcolumnId + " 的脱敏清单关联关系记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DgDesensitizeAssetcolumnDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeAssetcolumnId);
                        DgDesensitizeAssetcolumnDO existingDgDesensitizeAssetcolumn = dgDesensitizeAssetcolumnMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeAssetcolumn == null) {
                            dgDesensitizeAssetcolumnMapper.insert(dgDesensitizeAssetcolumnDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dgDesensitizeAssetcolumnId + " 的脱敏清单关联关系记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dgDesensitizeAssetcolumnId + " 的脱敏清单关联关系记录已存在。");
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
