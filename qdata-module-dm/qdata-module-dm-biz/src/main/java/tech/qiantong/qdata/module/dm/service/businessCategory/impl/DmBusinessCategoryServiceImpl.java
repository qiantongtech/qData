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

package tech.qiantong.qdata.module.dm.service.businessCategory.impl;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.TreeData;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dm.api.service.businessCategory.IDmBusinessCategoryApiService;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategorySaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessCategoryDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.dal.mapper.businessCategory.DmBusinessCategoryMapper;
import tech.qiantong.qdata.module.dm.dal.mapper.businessCategory.DmBusinessDomainRelMapper;
import tech.qiantong.qdata.module.dm.dal.mapper.dm.DmDataDomainMapper;
import tech.qiantong.qdata.module.dm.service.businessCategory.IDmBusinessCategoryService;
import tech.qiantong.qdata.module.system.mapper.SysUserMapper;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务分类Service业务层处理
 *
 * @author qdata
 * @date 2026-04-08
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmBusinessCategoryServiceImpl extends ServiceImpl<DmBusinessCategoryMapper, DmBusinessCategoryDO> implements IDmBusinessCategoryService, IDmBusinessCategoryApiService {
    @Resource
    private DmBusinessCategoryMapper dmBusinessCategoryMapper;

    @Resource
    private DmBusinessDomainRelMapper dmBusinessDomainRelMapper;

    @Resource
    private DmDataDomainMapper dmDataDomainMapper;

    @Resource
    private SysUserMapper dmUserMapper;


    @Override
    public PageResult<DmBusinessCategoryDO> getDmBusinessCategoryPage(DmBusinessCategoryPageReqVO pageReqVO) {
        PageResult<DmBusinessCategoryDO> pageResult = dmBusinessCategoryMapper.selectPage(pageReqVO);
      /*  //根据业务分类id 查询数据域集合 存入DmBusinessCategoryDO
        pageResult.getRows().forEach(item -> {
            item.setDataDomainList(dmDataDomainMapper.selectlistBybusinessDomainId(item.getId()));
        });*/
        return pageResult;
    }

    @Override
    public Long createDmBusinessCategory(DmBusinessCategorySaveReqVO createReqVO) {
        DmBusinessCategoryDO dictType = BeanUtils.toBean(createReqVO, DmBusinessCategoryDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        dmBusinessCategoryMapper.insert(dictType);
        // 插入数据域关联关系
        if (dictType.getDomainList() != null && !dictType.getDomainList().isEmpty()) {
            dictType.getDomainList().forEach(domain -> {
                domain.setBusinessCategoryId(dictType.getId());
                domain.setBusinessCategoryName(dictType.getName());
            });
            dmBusinessDomainRelMapper.insertBatch(dictType.getDomainList());
        }
        return dictType.getId();
    }

    @Override
    public int updateDmBusinessCategory(DmBusinessCategorySaveReqVO updateReqVO) {
        DmBusinessCategoryDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        //判断是否选择了他自己
        if (catDO.getId().equals(updateReqVO.getParentId())) {
            throw new tech.qiantong.qdata.common.exception.ServiceException("切换上级不能选择自身作为上级类目");
        }

        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            DmBusinessCategoryDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new tech.qiantong.qdata.common.exception.ServiceException("须先启用父级");
            }
        }
        //修改上下级判断
        boolean flag = false;
        if (!catDO.getParentId().equals(updateReqVO.getParentId())) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        // 更新业务分类
        DmBusinessCategoryDO updateObj = BeanUtils.toBean(updateReqVO, DmBusinessCategoryDO.class);
        // 插入新的数据域关联关系
        if (updateObj.getDomainList() != null && !updateObj.getDomainList().isEmpty()) {
            //先根据业务分类id 删除旧的关联域
            dmBusinessDomainRelMapper.delete(new LambdaQueryWrapper<DmBusinessDomainRelDO>().eq(DmBusinessDomainRelDO::getBusinessCategoryId, updateObj.getId()));
            updateObj.getDomainList().forEach(domain -> {
                domain.setBusinessCategoryId(updateObj.getId());
                domain.setBusinessCategoryName(updateObj.getName());
            });
            dmBusinessDomainRelMapper.insertBatch(updateObj.getDomainList());
        }

        int i = dmBusinessCategoryMapper.updateById(updateObj);
        //判断上下级是否发生了改变
        if (flag) {
            //更改所有下级
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }

        return i;
    }

    @Override
    public int removeDmBusinessCategory(Collection<Long> idList) {
        //xxxi先查询是否包含子业务分类  如果包含 则提示删除子业务分类后在删除
        if (idList.stream()
                .anyMatch(id -> dmBusinessCategoryMapper.selectCount(new LambdaQueryWrapper<DmBusinessCategoryDO>().eq(DmBusinessCategoryDO::getParentId, id)) > 0)) {
            throw new IllegalArgumentException("业务分类下存在子业务分类，不能删除");
        }
        // 先删除业务分类关联的域域关系业务分类
        dmBusinessDomainRelMapper.delete(new LambdaQueryWrapper<DmBusinessDomainRelDO>().in(DmBusinessDomainRelDO::getBusinessCategoryId, idList));
        // 删除业务分类关联
        return dmBusinessCategoryMapper.deleteBatchIds(idList);
    }

    @Override
    public DmBusinessCategoryDO getDmBusinessCategoryById(Long id) {
        DmBusinessCategoryDO dmBusinessCategoryDO = dmBusinessCategoryMapper.selectById(id);
        //查询用户表 将ownerId 转换为ownerName
        if (dmBusinessCategoryDO.getOwnerId() != null) {
            SysUser sysUser = dmUserMapper.selectUserById(dmBusinessCategoryDO.getOwnerId());
            if (sysUser != null) {
                dmBusinessCategoryDO.setOwnerName(sysUser.getNickName());
            }
        }
        //查询业务分类表 将parentId 转换为parentName
        if (dmBusinessCategoryDO.getParentId() != null) {
            DmBusinessCategoryDO categoryDO = dmBusinessCategoryMapper.selectById(dmBusinessCategoryDO.getParentId());
            if (categoryDO != null) {
                dmBusinessCategoryDO.setParentName(categoryDO.getName());
            }
        }
        //根据业务分类id 查询数据域ID集合 存入DmBusinessCategoryDO
        dmBusinessCategoryDO.setDomainIds(dmBusinessDomainRelMapper.selectList(new LambdaQueryWrapper<DmBusinessDomainRelDO>().eq(DmBusinessDomainRelDO::getBusinessCategoryId, id))
                .stream()
                .map(DmBusinessDomainRelDO::getDataDomainId)
                .map(String::valueOf)
                .collect(Collectors.toList()));
        /*if (dmBusinessCategoryDO != null) {
            dmBusinessCategoryDO.setDataDomainList(dmDataDomainMapper.selectlistBybusinessDomainId(id));
        }*/
        return dmBusinessCategoryDO;
    }

    @Override
    public List<DmBusinessCategoryDO> getDmBusinessCategoryList(DmBusinessCategoryPageReqVO dmBusinessCategory) {
        List<DmBusinessCategoryDO> pageResult = dmBusinessCategoryMapper.selectAllList(dmBusinessCategory);
        //根据业务域id 查询数据域集合 存入DmBusinessCategoryDO
        pageResult.forEach(item -> {
            List<DmBusinessDomainRelDO> cc = dmBusinessDomainRelMapper.selectList(new LambdaQueryWrapper<DmBusinessDomainRelDO>().eq(DmBusinessDomainRelDO::getBusinessCategoryId, item.getId()));
            item.setDomainList(cc);
        });
        return pageResult;
    }


    @Override
    public Map<Long, DmBusinessCategoryDO> getDmBusinessCategoryMap() {
        List<DmBusinessCategoryDO> dmBusinessCategoryList = dmBusinessCategoryMapper.selectList();
        return dmBusinessCategoryList.stream()
                .collect(Collectors.toMap(DmBusinessCategoryDO::getId, dmBusinessCategoryDO -> dmBusinessCategoryDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing));
    }


    /**
     * 导入业务分类数据
     *
     * @param importExcelList 业务分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDmBusinessCategory(List<DmBusinessCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmBusinessCategoryRespVO respVO : importExcelList) {
            try {
                DmBusinessCategoryDO dmBusinessCategoryDO = BeanUtils.toBean(respVO, DmBusinessCategoryDO.class);
                Long dmBusinessCategoryId = respVO.getId();
                if (isUpdateSupport) {
                    if (dmBusinessCategoryId != null) {
                        DmBusinessCategoryDO existingDmBusinessCategory = dmBusinessCategoryMapper.selectById(dmBusinessCategoryId);
                        if (existingDmBusinessCategory != null) {
                            dmBusinessCategoryMapper.updateById(dmBusinessCategoryDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dmBusinessCategoryId + " 的业务分类记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dmBusinessCategoryId + " 的业务分类记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DmBusinessCategoryDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmBusinessCategoryId);
                    DmBusinessCategoryDO existingDmBusinessCategory = dmBusinessCategoryMapper.selectOne(queryWrapper);
                    if (existingDmBusinessCategory == null) {
                        dmBusinessCategoryMapper.insert(dmBusinessCategoryDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dmBusinessCategoryId + " 的业务分类记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dmBusinessCategoryId + " 的业务分类记录已存在。");
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
    public String createCode(Long parentId, String parentCode) {
        String categoryCode = null;
        /*
         * 分成三种情况
         * 1.数据库无数据 调用YouBianCodeUtil.getNextYouBianCode(null);
         * 2.添加子节点，无兄弟元素 YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3.添加子节点有兄弟元素 YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        //找同类 确定上一个最大的code值
        LambdaQueryWrapper<DmBusinessCategoryDO> query = new LambdaQueryWrapper<DmBusinessCategoryDO>().eq(DmBusinessCategoryDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), DmBusinessCategoryDO::getCode, parentCode)
                .isNotNull(DmBusinessCategoryDO::getCode)
                .orderByDesc(DmBusinessCategoryDO::getCode);
        List<DmBusinessCategoryDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                DmBusinessCategoryDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    @Override
    public void changeCodeByPid(Long pid, String parentCode) {
        List<DmBusinessCategoryDO> list = baseMapper.selectList(Wrappers.lambdaQuery(DmBusinessCategoryDO.class)
                .eq(DmBusinessCategoryDO::getParentId, pid)
                .orderByAsc(DmBusinessCategoryDO::getCreateTime));
        if (list != null && list.size() > 0) {
            list.forEach(e -> {
                String codeOld = e.getCode();
                String codeNew = createCode(e.getParentId(), parentCode);
                e.setCode(codeNew);
                baseMapper.updateById(e);
                this.changeCodeByPid(e.getId(), e.getCode());
            });
        }
    }


    @Override
    public List<TreeData> getTreeData(String type) {
        //获取所有开启的业务分类
        List<DmBusinessCategoryDO> dmBusinessCategoryDOList = baseMapper.selectList(Wrappers.lambdaQuery(DmBusinessCategoryDO.class)
                .eq(DmBusinessCategoryDO::getValidFlag, true)
                .orderByAsc(DmBusinessCategoryDO::getSortOrder));

        //组装业务分类树
        List<TreeData> treeDataList = dmBusinessCategoryDOList.stream()
                .map(v -> TreeData.builder()
                        .id(v.getId())
                        .parentId(v.getParentId())
                        .name(v.getName())
                        .type("1")
                        .otherData(JSONObject.of(
                                "code", v.getCode(),
                                "engName", v.getEngName()
                        ))
                        .build())
                .collect(Collectors.toList());

        Map<Long, TreeData> dmBusinessCategoryMap = treeDataList.stream()
                .collect(Collectors.toMap(k -> k.getId(), v -> v));

        for (TreeData treeData : treeDataList) {
            TreeData parent = dmBusinessCategoryMap.get(treeData.getParentId());
            if (parent != null) {
                List<TreeData> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(treeData);
            }
        }

        //获取跟业务分类有关联的数据域
        MPJLambdaWrapperX<DmDataDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        String statisticsSql = null;
        if (StringUtils.isNotBlank(type)) {
            switch (type) {
                case "1":
                    statisticsSql = "(SELECT COUNT(1) FROM DA_ASSET a WHERE t.ID = a.DATA_DOMAIN_ID) AS num";
                    break;
            }
            if (StringUtils.isNotBlank(statisticsSql)) {
                lambdaWrapper.select(statisticsSql);
            }
        }
        lambdaWrapper.selectAll(DmDataDomainDO.class)
                .selectAs(DmBusinessDomainRelDO::getBusinessCategoryId, "businessCategoryId")
                .innerJoin(DmBusinessDomainRelDO.class, DmBusinessDomainRelDO::getDataDomainId, DmDataDomainDO::getId)
                .innerJoin(DmBusinessCategoryDO.class, DmBusinessCategoryDO::getId, DmBusinessDomainRelDO::getBusinessCategoryId)
                .eq(DmBusinessCategoryDO::getValidFlag, true);
        List<DmDataDomainDO> dataDomainDOList = dmDataDomainMapper.selectList(lambdaWrapper);

        //将数据域和业务分类关联起来
        for (DmDataDomainDO dmDataDomainDO : dataDomainDOList) {
            TreeData parent = dmBusinessCategoryMap.get(dmDataDomainDO.getBusinessCategoryId());
            if (parent != null) {
                List<TreeData> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(TreeData.builder()
                        .id(dmDataDomainDO.getId())
                        .parentId(dmDataDomainDO.getBusinessCategoryId())
                        .name(dmDataDomainDO.getName())
                        .type("2")
                        .otherData(JSONObject.of(
                                "code", parent.getOtherData().getString("code"),
                                "engName", dmDataDomainDO.getEngName(),
                                "num", dmDataDomainDO.getNum()))
                        .build());
            }
        }
        return treeDataList.stream()
                .filter(dataCategoryTreeRespVO -> dataCategoryTreeRespVO.getParentId() == 0)
                .collect(Collectors.toList());
    }
}
