package tech.qiantong.qdata.module.att.service.cat.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttQualityCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttQualityCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttQualityCatService;
import tech.qiantong.qdata.module.dpp.api.service.qa.DppQualityTaskApiService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 数据质量类目Service业务层处理
 *
 * @author qdata
 * @date 2025-07-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttQualityCatServiceImpl  extends ServiceImpl<AttQualityCatMapper,AttQualityCatDO> implements IAttQualityCatService {
    @Resource
    private AttQualityCatMapper attQualityCatMapper;
    @Resource
    private DppQualityTaskApiService taskApiService;

    @Override
    public PageResult<AttQualityCatDO> getAttQualityCatPage(AttQualityCatPageReqVO pageReqVO) {
        return attQualityCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttQualityCat(AttQualityCatSaveReqVO createReqVO) {
        AttQualityCatDO dictType = BeanUtils.toBean(createReqVO, AttQualityCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attQualityCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttQualityCat(AttQualityCatSaveReqVO updateReqVO) {
        AttQualityCatDO catDO = attQualityCatMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = taskApiService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("存在数据质量任务，不允许禁用");
            }
            attQualityCatMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttQualityCatDO parent = attQualityCatMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("须先启用父级");
            }
        }
        // 更新数据质量类目
        AttQualityCatDO updateObj = BeanUtils.toBean(updateReqVO, AttQualityCatDO.class);
        return attQualityCatMapper.updateById(updateObj);
    }
    @Override
    public int removeAttQualityCat(Collection<Long> idList) {
        //判断是否存在数据
        List<AttQualityCatDO> attQualityCatDOS = attQualityCatMapper.selectBatchIds(idList);
        for (AttQualityCatDO cat : attQualityCatDOS) {
            if (taskApiService.getCountByCatCode(cat.getCode()) > 0) {
                throw new ServiceException("存在数据质量任务，不允许删除");
            }
        }
        // 批量删除数据质量类目
        return attQualityCatMapper.deleteBatchIds(idList);
    }

    @Override
    public AttQualityCatDO getAttQualityCatById(Long id) {
        return attQualityCatMapper.selectById(id);
    }

    @Override
    public List<AttQualityCatDO> getAttQualityCatList(AttQualityCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttQualityCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttQualityCatDO::getName, reqVO.getName())
                .likeRightIfPresent(AttQualityCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttQualityCatDO::getValidFlag, reqVO.getValidFlag())
                .orderByAsc(AttQualityCatDO::getSortOrder);
        return attQualityCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttQualityCatDO> getAttQualityCatMap() {
        List<AttQualityCatDO> attQualityCatList = attQualityCatMapper.selectList();
        return attQualityCatList.stream()
                .collect(Collectors.toMap(
                        AttQualityCatDO::getId,
                        attQualityCatDO -> attQualityCatDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据质量类目数据
     *
     * @param importExcelList 数据质量类目数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importAttQualityCat(List<AttQualityCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttQualityCatRespVO respVO : importExcelList) {
            try {
                AttQualityCatDO attQualityCatDO = BeanUtils.toBean(respVO, AttQualityCatDO.class);
                Long attQualityCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attQualityCatId != null) {
                        AttQualityCatDO existingAttQualityCat = attQualityCatMapper.selectById(attQualityCatId);
                        if (existingAttQualityCat != null) {
                            attQualityCatMapper.updateById(attQualityCatDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attQualityCatId + " 的数据质量类目记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attQualityCatId + " 的数据质量类目记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttQualityCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attQualityCatId);
                    AttQualityCatDO existingAttQualityCat = attQualityCatMapper.selectOne(queryWrapper);
                    if (existingAttQualityCat == null) {
                        attQualityCatMapper.insert(attQualityCatDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attQualityCatId + " 的数据质量类目记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attQualityCatId + " 的数据质量类目记录已存在。");
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
        LambdaQueryWrapper<AttQualityCatDO> query = new LambdaQueryWrapper<AttQualityCatDO>()
                .eq(AttQualityCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttQualityCatDO::getCode, parentCode)
                .isNotNull(AttQualityCatDO::getCode)
                .orderByDesc(AttQualityCatDO::getCode);
        List<AttQualityCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttQualityCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
