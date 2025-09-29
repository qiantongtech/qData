package tech.qiantong.qdata.module.att.service.cat.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttModelCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttModelCatService;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 逻辑模型类目管理Service业务层处理
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttModelCatServiceImpl extends ServiceImpl<AttModelCatMapper, AttModelCatDO> implements IAttModelCatService {
    @Resource
    private AttModelCatMapper attModelCatMapper;

    @Resource
    private IDpModelApiService dpModelApiService;

    @Override
    public PageResult<AttModelCatDO> getAttModelCatPage(AttModelCatPageReqVO pageReqVO) {
        return attModelCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttModelCat(AttModelCatSaveReqVO createReqVO) {
        AttModelCatDO dictType = BeanUtils.toBean(createReqVO, AttModelCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attModelCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttModelCat(AttModelCatSaveReqVO updateReqVO) {
        AttModelCatDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = dpModelApiService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("存在逻辑模型，不允许禁用");
            }
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttModelCatDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("须先启用父级");
            }
        }
        // 更新逻辑模型类目管理
        AttModelCatDO updateObj = BeanUtils.toBean(updateReqVO, AttModelCatDO.class);
        return baseMapper.updateById(updateObj);
    }

    @Override
    public int removeAttModelCat(Collection<Long> idList) {
        int count = 0;
        for (Long id : idList) {
            AttModelCatDO cat = baseMapper.selectById(id);
            //判断是否存在数据
            if (dpModelApiService.getCountByCatCode(cat.getCode()) > 0) {
                throw new ServiceException("存在逻辑模型，不允许删除");
            }
            if (cat != null) {
                count += baseMapper.delete(Wrappers.lambdaQuery(AttModelCatDO.class)
                        .likeRight(AttModelCatDO::getCode, cat.getCode()));
            }
        }
        return count;
    }

    @Override
    public int removeAttModelCat(Long id) {
        int count = 0;
        AttModelCatDO cat = baseMapper.selectById(id);
        //判断是否存在数据
        if (dpModelApiService.getCountByCatCode(cat.getCode()) > 0) {
            throw new ServiceException("存在逻辑模型，不允许删除");
        }
        if (cat != null) {
            count += baseMapper.delete(Wrappers.lambdaQuery(AttModelCatDO.class)
                    .likeRight(AttModelCatDO::getCode, cat.getCode()));
        }
        return count;
    }

    @Override
    public AttModelCatDO getAttModelCatById(Long id) {
        return attModelCatMapper.selectById(id);
    }

    @Override
    public List<AttModelCatDO> getAttModelCatList() {
        return attModelCatMapper.selectList();
    }

    @Override
    public List<AttModelCatDO> getAttModelCatList(AttModelCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttModelCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttModelCatDO::getName, reqVO.getName())
                .likeRightIfPresent(AttModelCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttModelCatDO::getValidFlag, reqVO.getValidFlag())
                .orderByAsc(AttModelCatDO::getSortOrder);
        return attModelCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttModelCatDO> getAttModelCatMap() {
        List<AttModelCatDO> attModelCatList = attModelCatMapper.selectList();
        return attModelCatList.stream()
                .collect(Collectors.toMap(
                        AttModelCatDO::getId,
                        attModelCatDO -> attModelCatDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入逻辑模型类目管理数据
     *
     * @param importExcelList 逻辑模型类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttModelCat(List<AttModelCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttModelCatRespVO respVO : importExcelList) {
            try {
                AttModelCatDO attModelCatDO = BeanUtils.toBean(respVO, AttModelCatDO.class);
                Long attModelCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attModelCatId != null) {
                        AttModelCatDO existingAttModelCat = attModelCatMapper.selectById(attModelCatId);
                        if (existingAttModelCat != null) {
                            attModelCatMapper.updateById(attModelCatDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attModelCatId + " 的逻辑模型类目管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attModelCatId + " 的逻辑模型类目管理记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttModelCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attModelCatId);
                    AttModelCatDO existingAttModelCat = attModelCatMapper.selectOne(queryWrapper);
                    if (existingAttModelCat == null) {
                        attModelCatMapper.insert(attModelCatDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attModelCatId + " 的逻辑模型类目管理记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attModelCatId + " 的逻辑模型类目管理记录已存在。");
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
        LambdaQueryWrapper<AttModelCatDO> query = new LambdaQueryWrapper<AttModelCatDO>()
                .eq(AttModelCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttModelCatDO::getCode, parentCode)
                .isNotNull(AttModelCatDO::getCode)
                .orderByDesc(AttModelCatDO::getCode);
        List<AttModelCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttModelCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
