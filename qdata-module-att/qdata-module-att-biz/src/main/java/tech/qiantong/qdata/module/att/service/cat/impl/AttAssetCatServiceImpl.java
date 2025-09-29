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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttAssetCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttAssetCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttAssetCatService;
import tech.qiantong.qdata.module.da.api.service.asset.IDaAssetApiOutService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据资产类目管理Service业务层处理
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttAssetCatServiceImpl extends ServiceImpl<AttAssetCatMapper, AttAssetCatDO> implements IAttAssetCatService {
    @Resource
    private AttAssetCatMapper attAssetCatMapper;

    @Resource
    private IDaAssetApiOutService daAssetApiService;

    @Override
    public PageResult<AttAssetCatDO> getAttAssetCatPage(AttAssetCatPageReqVO pageReqVO) {
        return attAssetCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttAssetCat(AttAssetCatSaveReqVO createReqVO) {
        AttAssetCatDO dictType = BeanUtils.toBean(createReqVO, AttAssetCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attAssetCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttAssetCat(AttAssetCatSaveReqVO updateReqVO) {
        AttAssetCatDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = daAssetApiService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("存在数据资产，不允许禁用");
            }
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttAssetCatDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("须先启用父级");
            }
        }
        // 更新数据资产类目管理
        AttAssetCatDO updateObj = BeanUtils.toBean(updateReqVO, AttAssetCatDO.class);
        return attAssetCatMapper.updateById(updateObj);
    }

    @Override
    public int removeAttAssetCat(Collection<Long> idList) {
        int count = 0;
        for (Long id : idList) {
            AttAssetCatDO cat = baseMapper.selectById(id);
            //判断是否存在数据资产
            if (daAssetApiService.getCountByCatCode(cat.getCode()) > 0) {
                throw new ServiceException("存在数据资产，不允许删除");
            }
            if (cat != null) {
                count += baseMapper.delete(Wrappers.lambdaQuery(AttAssetCatDO.class)
                        .likeRight(AttAssetCatDO::getCode, cat.getCode()));
            }
        }
        return count;
    }


    @Override
    public AttAssetCatDO getAttAssetCatById(Long id) {
        return attAssetCatMapper.selectById(id);
    }

    @Override
    public List<AttAssetCatDO> getAttAssetCatList() {
        return attAssetCatMapper.selectList();
    }

    @Override
    public List<AttAssetCatDO> getAttAssetCatList(AttAssetCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttAssetCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttAssetCatDO::getName, reqVO.getName())
                .likeRightIfPresent(AttAssetCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttAssetCatDO::getValidFlag, reqVO.getValidFlag())
                .orderByAsc(AttAssetCatDO::getSortOrder);
        return attAssetCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttAssetCatDO> getAttAssetCatMap() {
        List<AttAssetCatDO> attAssetCatList = attAssetCatMapper.selectList();
        return attAssetCatList.stream()
                .collect(Collectors.toMap(
                        AttAssetCatDO::getId,
                        attAssetCatDO -> attAssetCatDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据资产类目管理数据
     *
     * @param importExcelList 数据资产类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttAssetCat(List<AttAssetCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttAssetCatRespVO respVO : importExcelList) {
            try {
                AttAssetCatDO attAssetCatDO = BeanUtils.toBean(respVO, AttAssetCatDO.class);
                Long attAssetCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attAssetCatId != null) {
                        AttAssetCatDO existingAttAssetCat = attAssetCatMapper.selectById(attAssetCatId);
                        if (existingAttAssetCat != null) {
                            attAssetCatMapper.updateById(attAssetCatDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attAssetCatId + " 的数据资产类目管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attAssetCatId + " 的数据资产类目管理记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttAssetCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attAssetCatId);
                    AttAssetCatDO existingAttAssetCat = attAssetCatMapper.selectOne(queryWrapper);
                    if (existingAttAssetCat == null) {
                        attAssetCatMapper.insert(attAssetCatDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attAssetCatId + " 的数据资产类目管理记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attAssetCatId + " 的数据资产类目管理记录已存在。");
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
        LambdaQueryWrapper<AttAssetCatDO> query = new LambdaQueryWrapper<AttAssetCatDO>()
                .eq(AttAssetCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttAssetCatDO::getCode, parentCode)
                .isNotNull(AttAssetCatDO::getCode)
                .orderByDesc(AttAssetCatDO::getCode);
        List<AttAssetCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttAssetCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

}
