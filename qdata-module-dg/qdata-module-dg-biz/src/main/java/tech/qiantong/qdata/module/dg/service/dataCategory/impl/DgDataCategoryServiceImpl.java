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

package tech.qiantong.qdata.module.dg.service.dataCategory.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategorySaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryTreeRespVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory.DgDataCategoryDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataLevel.DgDataLevelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.dataCategory.DgDataCategoryMapper;
import tech.qiantong.qdata.module.dg.dal.mapper.dataCategoryCat.DgDataCategoryCatMapper;
import tech.qiantong.qdata.module.dg.service.dataCategory.IDgDataCategoryService;
import tech.qiantong.qdata.module.dg.service.desensitizeRules.IDgDesensitizeRuleService;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * 数据分类Service业务层处理
 *
 * @author qdata
 * @date 2026-04-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDataCategoryServiceImpl extends ServiceImpl<DgDataCategoryMapper, DgDataCategoryDO> implements IDgDataCategoryService {
    @Resource
    private DgDataCategoryMapper dgDataCategoryMapper;

    @Resource
    private DgDataCategoryCatMapper dgDataCategoryCatMapper;

    @Resource
    private IDgDesensitizeRuleService dgDesensitizeRuleService;

    @Override
    public PageResult<DgDataCategoryDO> getDgDataCategoryPage(DgDataCategoryPageReqVO pageReqVO) {
        return dgDataCategoryMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDataCategory(DgDataCategorySaveReqVO createReqVO) {
        DgDataCategoryDO dictType = BeanUtils.toBean(createReqVO, DgDataCategoryDO.class);
        dgDataCategoryMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDataCategory(DgDataCategorySaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据分类
        DgDataCategoryDO updateObj = BeanUtils.toBean(updateReqVO, DgDataCategoryDO.class);
        return dgDataCategoryMapper.updateById(updateObj);
    }

    @Override
    public int removeDgDataCategory(Collection<Long> idList) {
        //判断在规则中是否被使用
        Long count = dgDesensitizeRuleService.getCountByCategoryIds(idList);
        if (count > 0) {
            throw new ServiceException("dg.error.delete.sensitive", "存在敏感规则，不允许删除");
        }
        // 批量删除数据分类
        return dgDataCategoryMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDataCategoryDO getDgDataCategoryById(Long id) {
        MPJLambdaWrapperX<DgDataCategoryDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper
                .select("l.short_name AS dataLevelShortName", "c.name AS catName", "(CASE WHEN r.id IS NOT NULL THEN '1' ELSE '0' END) AS desensitizationRulesFlag")
                .leftJoin("DG_DESENSITIZE_RULE r on t.id = r.DATA_CATEGORY_ID AND r.DEL_FLAG = '0'")
                .leftJoin(DgDataLevelDO.class, "l", DgDataLevelDO::getId, DgDataCategoryDO::getDataLevelId)
                .leftJoin(DgDataCategoryCatDO.class, "c", DgDataCategoryCatDO::getCode, DgDataCategoryDO::getCatCode);
        lambdaWrapper.selectAll(DgDataCategoryDO.class)
                .inIfPresent(DgDataCategoryDO::getId, id);
        return dgDataCategoryMapper.selectOne(lambdaWrapper);
    }

    @Override
    public List<DgDataCategoryDO> getDgDataCategoryList() {
        return dgDataCategoryMapper.selectList();
    }

    @Override
    public Map<Long, DgDataCategoryDO> getDgDataCategoryMap() {
        List<DgDataCategoryDO> dgDataCategoryList = dgDataCategoryMapper.selectList();
        return dgDataCategoryList.stream()
                .collect(Collectors.toMap(
                        DgDataCategoryDO::getId,
                        dgDataCategoryDO -> dgDataCategoryDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据分类数据
     *
     * @param importExcelList 数据分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDgDataCategory(List<DgDataCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dg.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DgDataCategoryRespVO respVO : importExcelList) {
            try {
                DgDataCategoryDO dgDataCategoryDO = BeanUtils.toBean(respVO, DgDataCategoryDO.class);
                Long dgDataCategoryId = respVO.getId();
                if (isUpdateSupport) {
                    if (dgDataCategoryId != null) {
                        DgDataCategoryDO existingDgDataCategory = dgDataCategoryMapper.selectById(dgDataCategoryId);
                        if (existingDgDataCategory != null) {
                            dgDataCategoryMapper.updateById(dgDataCategoryDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                    "数据更新成功，ID为 " + dgDataCategoryId + " 的数据分类记录。", dgDataCategoryId, "数据分类"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                    "数据更新失败，ID为 " + dgDataCategoryId + " 的数据分类记录不存在。", dgDataCategoryId, "数据分类"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DgDataCategoryDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dgDataCategoryId);
                    DgDataCategoryDO existingDgDataCategory = dgDataCategoryMapper.selectOne(queryWrapper);
                    if (existingDgDataCategory == null) {
                        dgDataCategoryMapper.insert(dgDataCategoryDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                "数据插入成功，ID为 " + dgDataCategoryId + " 的数据分类记录。", dgDataCategoryId, "数据分类"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                "数据插入失败，ID为 " + dgDataCategoryId + " 的数据分类记录已存在。", dgDataCategoryId, "数据分类"));
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
    public List<DgDataCategoryTreeRespVO> selectTree(String type) {
        //获取分类类目数据
        List<DgDataCategoryCatDO> dataCategoryCatDOList = dgDataCategoryCatMapper.selectList();
        List<DgDataCategoryTreeRespVO> dataCategoryTreeRespVOList = dataCategoryCatDOList.stream()
                .map(dataCategoryCatDO -> {
                    return DgDataCategoryTreeRespVO.builder()
                            .id(dataCategoryCatDO.getId())
                            .parentId(dataCategoryCatDO.getParentId())
                            .name(dataCategoryCatDO.getName())
                            .type("1")
                            .catCode(dataCategoryCatDO.getCode())
                            .build();
                })
                .collect(Collectors.toList());

        Map<Long, DgDataCategoryTreeRespVO> dataCategoryCatIdMap = dataCategoryTreeRespVOList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v));

        for (DgDataCategoryTreeRespVO dgDataCategoryTreeRespVO : dataCategoryTreeRespVOList) {
            DgDataCategoryTreeRespVO parent = dataCategoryCatIdMap.get(dgDataCategoryTreeRespVO.getParentId());
            if (parent != null) {
                List<DgDataCategoryTreeRespVO> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(dgDataCategoryTreeRespVO);
            }
        }
        Map<String, DgDataCategoryTreeRespVO> dataCategoryCatCodeMap = dataCategoryTreeRespVOList.stream().collect(Collectors.toMap(k -> k.getCatCode(), v -> v));

        //获取所有有效的分类


        MPJLambdaWrapperX<DgDataCategoryDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        String subQuery = "(CASE WHEN r.id IS NOT NULL THEN '1' ELSE '0' END) AS desensitizationRulesFlag";
        if (StringUtils.equals("2", type)) {
            subQuery = "(CASE WHEN w.id IS NOT NULL THEN '1' ELSE '0' END) AS desensitizationRulesFlag";
        }
        lambdaWrapper.selectAll(DgDataCategoryDO.class)
                .select(subQuery)
                .leftJoin("DG_DESENSITIZE_RULE r on t.id = r.DATA_CATEGORY_ID AND r.DEL_FLAG = '0'")
                .leftJoin("DG_DESENSITIZE_WHITELIST w on t.id = w.DATA_CATEGORY_ID AND w.DEL_FLAG = '0'")
                .eq(DgDataCategoryDO::getValidFlag, true);
        List<DgDataCategoryDO> dataCategoryDOList = dgDataCategoryMapper.selectList(lambdaWrapper);

        for (DgDataCategoryDO dgDataCategoryDO : dataCategoryDOList) {
            DgDataCategoryTreeRespVO dgDataCategoryTreeRespVO = dataCategoryCatCodeMap.get(dgDataCategoryDO.getCatCode());
            if (dgDataCategoryTreeRespVO != null) {
                List<DgDataCategoryTreeRespVO> child = dgDataCategoryTreeRespVO.getChildren();
                if (child == null) {
                    child = new ArrayList<>();
                    dgDataCategoryTreeRespVO.setChildren(child);
                }
                child.add(DgDataCategoryTreeRespVO.builder()
                        .id(dgDataCategoryDO.getId())
                        .name(dgDataCategoryDO.getName())
                        .type("2")
                        .desensitizationRulesFlag(dgDataCategoryDO.getDesensitizationRulesFlag())
                        .build());
            }
        }

        return dataCategoryTreeRespVOList.stream()
                .filter(dataCategoryTreeRespVO -> dataCategoryTreeRespVO.getParentId() == 0)
                .collect(Collectors.toList());
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DgDataCategoryDO.class)
                .likeRight(DgDataCategoryDO::getCatCode, catCode));
    }

    @Override
    public int updateCatCode(String oldCatCode, String newCatCode) {
        return baseMapper.update(
                null,
                Wrappers.<DgDataCategoryDO>lambdaUpdate()
                        .set(DgDataCategoryDO::getCatCode, newCatCode)
                        .eq(DgDataCategoryDO::getCatCode, oldCatCode)
        );
    }

    @Override
    public List<DgDataCategoryDO> getDgDataCategoryList(DgDataCategoryPageReqVO dgDataCategory) {
         //根据参数 dgDataCategory 只查询validFlag为true的 数据分类列表不分页
       List<DgDataCategoryDO> dataCategoryDOList = dgDataCategoryMapper.selectList(Wrappers.lambdaQuery(DgDataCategoryDO.class)
                .eq(DgDataCategoryDO::getValidFlag, true));
       return dataCategoryDOList;

    }
}
