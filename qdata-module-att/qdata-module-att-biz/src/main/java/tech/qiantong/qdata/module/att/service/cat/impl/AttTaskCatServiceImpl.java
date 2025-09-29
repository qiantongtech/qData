package tech.qiantong.qdata.module.att.service.cat.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.cat.dto.AttTaskCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttTaskCatRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.IAttTaskCatApiService;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttTaskCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttTaskCatService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据集成任务类目管理Service业务层处理
 *
 * @author qdata
 * @date 2025-03-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttTaskCatServiceImpl extends ServiceImpl<AttTaskCatMapper, AttTaskCatDO> implements IAttTaskCatService, IAttTaskCatApiService {
    @Resource
    private AttTaskCatMapper attTaskCatMapper;

    @Override
    public PageResult<AttTaskCatDO> getAttTaskCatPage(AttTaskCatPageReqVO pageReqVO) {
        return attTaskCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttTaskCat(AttTaskCatSaveReqVO createReqVO) {
        AttTaskCatDO dictType = BeanUtils.toBean(createReqVO, AttTaskCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attTaskCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public List<AttTaskCatRespDTO> getAttTaskCatApiList(AttTaskCatReqDTO reqVO) {
        MPJLambdaWrapper<AttTaskCatDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(AttTaskCatDO.class)
                .like(StringUtils.isNotBlank(reqVO.getName()), AttTaskCatDO::getName, reqVO.getName());
        List<AttTaskCatDO> attTaskCatDOS = attTaskCatMapper.selectList(wrapper);
        return BeanUtils.toBean(attTaskCatDOS, AttTaskCatRespDTO.class);
    }

    @Override
    public int updateAttTaskCat(AttTaskCatSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据集成任务类目管理
        AttTaskCatDO updateObj = BeanUtils.toBean(updateReqVO, AttTaskCatDO.class);
        return attTaskCatMapper.updateById(updateObj);
    }

    @Override
    public int removeAttTaskCat(Collection<Long> idList) {
        // 批量删除数据集成任务类目管理
        return attTaskCatMapper.deleteBatchIds(idList);
    }

    @Override
    public AttTaskCatDO getAttTaskCatById(Long id) {
        return attTaskCatMapper.selectById(id);
    }

    @Override
    public List<AttTaskCatDO> getAttTaskCatList() {
        return attTaskCatMapper.selectList();
    }

    @Override
    public List<AttTaskCatDO> getAttTaskCatList(AttTaskCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttTaskCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttTaskCatDO::getName, reqVO.getName())
                .eqIfPresent(AttTaskCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttTaskCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttTaskCatDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttTaskCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttTaskCatDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AttTaskCatDO::getProjectId,reqVO.getProjectId())
                .eqIfPresent(AttTaskCatDO::getProjectCode,reqVO.getProjectCode())
                .orderByAsc(AttTaskCatDO::getSortOrder);
        return attTaskCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttTaskCatDO> getAttTaskCatMap() {
        List<AttTaskCatDO> attTaskCatList = attTaskCatMapper.selectList();
        return attTaskCatList.stream()
                .collect(Collectors.toMap(
                        AttTaskCatDO::getId,
                        attTaskCatDO -> attTaskCatDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
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
        LambdaQueryWrapper<AttTaskCatDO> query = new LambdaQueryWrapper<AttTaskCatDO>()
                .eq(AttTaskCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttTaskCatDO::getCode, parentCode)
                .isNotNull(AttTaskCatDO::getCode)
                .orderByDesc(AttTaskCatDO::getCode);
        List<AttTaskCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttTaskCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    /**
     * 导入数据集成任务类目管理数据
     *
     * @param importExcelList 数据集成任务类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttTaskCat(List<AttTaskCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttTaskCatRespVO respVO : importExcelList) {
            try {
                AttTaskCatDO attTaskCatDO = BeanUtils.toBean(respVO, AttTaskCatDO.class);
                Long attTaskCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attTaskCatId != null) {
                        AttTaskCatDO existingAttTaskCat = attTaskCatMapper.selectById(attTaskCatId);
                        if (existingAttTaskCat != null) {
                            attTaskCatMapper.updateById(attTaskCatDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attTaskCatId + " 的数据集成任务类目管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attTaskCatId + " 的数据集成任务类目管理记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttTaskCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attTaskCatId);
                    AttTaskCatDO existingAttTaskCat = attTaskCatMapper.selectOne(queryWrapper);
                    if (existingAttTaskCat == null) {
                        attTaskCatMapper.insert(attTaskCatDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attTaskCatId + " 的数据集成任务类目管理记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attTaskCatId + " 的数据集成任务类目管理记录已存在。");
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
}
