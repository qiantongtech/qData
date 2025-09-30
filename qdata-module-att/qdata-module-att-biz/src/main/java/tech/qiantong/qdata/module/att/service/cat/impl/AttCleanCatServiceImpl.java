package tech.qiantong.qdata.module.att.service.cat.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.text.Convert;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttCleanCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttCleanCatService;
import tech.qiantong.qdata.module.att.service.rule.IAttCleanRuleService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 清洗规则类目Service业务层处理
 *
 * @author qdata
 * @date 2025-08-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttCleanCatServiceImpl  extends ServiceImpl<AttCleanCatMapper,AttCleanCatDO> implements IAttCleanCatService {
    @Resource
    private AttCleanCatMapper attCleanCatMapper;
    @Resource
    private IAttCleanRuleService attCleanRuleService;

    @Override
    public PageResult<AttCleanCatDO> getAttCleanCatPage(AttCleanCatPageReqVO pageReqVO) {
        return attCleanCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttCleanCat(AttCleanCatSaveReqVO createReqVO) {
        AttCleanCatDO dictType = BeanUtils.toBean(createReqVO, AttCleanCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attCleanCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttCleanCat(AttCleanCatSaveReqVO updateReqVO) {
        AttCleanCatDO catDO = attCleanCatMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = attCleanRuleService.getCount(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("存在清洗规则模型，不允许禁用");
            }
            attCleanCatMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttCleanCatDO parent = attCleanCatMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("须先启用父级");
            }
        }        // 更新清洗规则类目
        AttCleanCatDO updateObj = BeanUtils.toBean(updateReqVO, AttCleanCatDO.class);
        return attCleanCatMapper.updateById(updateObj);
    }
    @Override
    public int removeAttCleanCat(Long idList) {
        // 批量删除清洗规则类目
        int count = 0;
        AttCleanCatDO cat = baseMapper.selectById(idList);

        //判断是否存在数据
        if (attCleanRuleService.getCount(cat.getCode()) > 0) {
            throw new RuntimeException("存在清洗规则模型，不允许删除");
        }

        if (cat != null) {
            count += baseMapper.delete(Wrappers.lambdaQuery(AttCleanCatDO.class)
                    .likeRight(AttCleanCatDO::getCode, cat.getCode()));
        }
        return count;
    }

    @Override
    public AttCleanCatDO getAttCleanCatById(Long id) {
        return attCleanCatMapper.selectById(id);
    }

    @Override
    public List<AttCleanCatDO> getAttCleanCatList(AttCleanCatPageReqVO attCleanCat) {
        LambdaQueryWrapperX<AttCleanCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttCleanCatDO::getName, attCleanCat.getName())
                .likeRightIfPresent(AttCleanCatDO::getCode, attCleanCat.getCode())
                .eqIfPresent(AttCleanCatDO::getValidFlag, attCleanCat.getValidFlag())
                .orderByAsc(AttCleanCatDO::getSortOrder);
        return attCleanCatMapper.selectList(queryWrapperX);
    }

    @Override
    public List<AttCleanCatDO> getAttCleanCatList() {
        return attCleanCatMapper.selectList();
    }

    @Override
    public Map<Long, AttCleanCatDO> getAttCleanCatMap() {
        List<AttCleanCatDO> attCleanCatList = attCleanCatMapper.selectList();
        return attCleanCatList.stream()
                .collect(Collectors.toMap(
                        AttCleanCatDO::getId,
                        attCleanCatDO -> attCleanCatDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入清洗规则类目数据
     *
     * @param importExcelList 清洗规则类目数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importAttCleanCat(List<AttCleanCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttCleanCatRespVO respVO : importExcelList) {
            try {
                AttCleanCatDO attCleanCatDO = BeanUtils.toBean(respVO, AttCleanCatDO.class);
                Long attCleanCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attCleanCatId != null) {
                        AttCleanCatDO existingAttCleanCat = attCleanCatMapper.selectById(attCleanCatId);
                        if (existingAttCleanCat != null) {
                            attCleanCatMapper.updateById(attCleanCatDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attCleanCatId + " 的清洗规则类目记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attCleanCatId + " 的清洗规则类目记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttCleanCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attCleanCatId);
                    AttCleanCatDO existingAttCleanCat = attCleanCatMapper.selectOne(queryWrapper);
                    if (existingAttCleanCat == null) {
                        attCleanCatMapper.insert(attCleanCatDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attCleanCatId + " 的清洗规则类目记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attCleanCatId + " 的清洗规则类目记录已存在。");
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
        LambdaQueryWrapper<AttCleanCatDO> query = new LambdaQueryWrapper<AttCleanCatDO>()
                .eq(AttCleanCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttCleanCatDO::getCode, parentCode)
                .isNotNull(AttCleanCatDO::getCode)
                .orderByDesc(AttCleanCatDO::getCode);
        List<AttCleanCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttCleanCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
