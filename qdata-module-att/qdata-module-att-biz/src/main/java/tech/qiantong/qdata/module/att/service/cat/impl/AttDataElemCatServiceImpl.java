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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataElemCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttDataElemCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttDataElemCatService;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemApiService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据元类目管理Service业务层处理
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttDataElemCatServiceImpl extends ServiceImpl<AttDataElemCatMapper, AttDataElemCatDO> implements IAttDataElemCatService {
    @Resource
    private AttDataElemCatMapper attDataElemCatMapper;
    @Resource
    private IDataElemApiService dataElemApiService;

    @Override
    public PageResult<AttDataElemCatDO> getAttDataElemCatPage(AttDataElemCatPageReqVO pageReqVO) {
        return attDataElemCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttDataElemCat(AttDataElemCatSaveReqVO createReqVO) {
        AttDataElemCatDO dictType = BeanUtils.toBean(createReqVO, AttDataElemCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attDataElemCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttDataElemCat(AttDataElemCatSaveReqVO updateReqVO) {
        AttDataElemCatDO attDataElemCatDO = attDataElemCatMapper.selectById(updateReqVO.getId());
        if (attDataElemCatDO == null) {
            return 0;
        }
        // 更新数据元类目管理
        AttDataElemCatDO updateObj = BeanUtils.toBean(updateReqVO, AttDataElemCatDO.class);
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = dataElemApiService.getCountByCatCode(attDataElemCatDO.getCode());
            if (countData > 0) {
                throw new ServiceException("存在数据元，不允许禁用");
            }
            attDataElemCatMapper.updateValidFlag(attDataElemCatDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttDataElemCatDO parent = attDataElemCatMapper.selectById(attDataElemCatDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("须先启用父级");
            }
        }
        return attDataElemCatMapper.updateById(updateObj);
    }

    @Override
    public int removeAttDataElemCat(Collection<Long> idList) {
        int count = 0;
        for (Long id : idList) {
            AttDataElemCatDO cat = baseMapper.selectById(id);
            //判断是否存在数据资产
            if (dataElemApiService.getCountByCatCode(cat.getCode()) > 0) {
                throw new ServiceException("存在数据元，不允许删除");
            }
            if (cat != null) {
                count += baseMapper.delete(Wrappers.lambdaQuery(AttDataElemCatDO.class)
                        .likeRight(AttDataElemCatDO::getCode, cat.getCode()));
            }
        }
        return count;
    }

    @Override
    public AttDataElemCatDO getAttDataElemCatById(Long id) {
        return attDataElemCatMapper.selectById(id);
    }

    @Override
    public List<AttDataElemCatDO> getAttDataElemCatList() {
        return attDataElemCatMapper.selectList();
    }

    @Override
    public List<AttDataElemCatDO> getAttDataElemCatList(AttDataElemCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttDataElemCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttDataElemCatDO::getName, reqVO.getName())
                .eqIfPresent(AttDataElemCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttDataElemCatDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(AttDataElemCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttDataElemCatDO::getDescription, reqVO.getDescription())
                .likeRightIfPresent(AttDataElemCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttDataElemCatDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(AttDataElemCatDO::getSortOrder);
        return attDataElemCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttDataElemCatDO> getAttDataElemCatMap() {
        List<AttDataElemCatDO> attDataElemCatList = attDataElemCatMapper.selectList();
        return attDataElemCatList.stream()
                .collect(Collectors.toMap(
                        AttDataElemCatDO::getId,
                        attDataElemCatDO -> attDataElemCatDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据元类目管理数据
     *
     * @param importExcelList 数据元类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttDataElemCat(List<AttDataElemCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttDataElemCatRespVO respVO : importExcelList) {
            try {
                AttDataElemCatDO attDataElemCatDO = BeanUtils.toBean(respVO, AttDataElemCatDO.class);
                Long attDataElemCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attDataElemCatId != null) {
                        AttDataElemCatDO existingAttDataElemCat = attDataElemCatMapper.selectById(attDataElemCatId);
                        if (existingAttDataElemCat != null) {
                            attDataElemCatMapper.updateById(attDataElemCatDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attDataElemCatId + " 的数据元类目管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attDataElemCatId + " 的数据元类目管理记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttDataElemCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attDataElemCatId);
                    AttDataElemCatDO existingAttDataElemCat = attDataElemCatMapper.selectOne(queryWrapper);
                    if (existingAttDataElemCat == null) {
                        attDataElemCatMapper.insert(attDataElemCatDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attDataElemCatId + " 的数据元类目管理记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attDataElemCatId + " 的数据元类目管理记录已存在。");
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
        LambdaQueryWrapper<AttDataElemCatDO> query = new LambdaQueryWrapper<AttDataElemCatDO>()
                .eq(AttDataElemCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttDataElemCatDO::getCode, parentCode)
                .isNotNull(AttDataElemCatDO::getCode)
                .orderByDesc(AttDataElemCatDO::getCode);
        List<AttDataElemCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttDataElemCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
