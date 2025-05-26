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
import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.IAttDataDevCatApiService;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataDevCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttDataDevCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttDataDevCatService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据开发类目管理Service业务层处理
 *
 * @author qdata
 * @date 2025-03-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttDataDevCatServiceImpl extends ServiceImpl<AttDataDevCatMapper, AttDataDevCatDO> implements IAttDataDevCatService, IAttDataDevCatApiService {
    @Resource
    private AttDataDevCatMapper attDataDevCatMapper;

    @Override
    public PageResult<AttDataDevCatDO> getAttDataDevCatPage(AttDataDevCatPageReqVO pageReqVO) {
        return attDataDevCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttDataDevCat(AttDataDevCatSaveReqVO createReqVO) {
        AttDataDevCatDO dictType = BeanUtils.toBean(createReqVO, AttDataDevCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attDataDevCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttDataDevCat(AttDataDevCatSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据开发类目管理
        AttDataDevCatDO updateObj = BeanUtils.toBean(updateReqVO, AttDataDevCatDO.class);
        return attDataDevCatMapper.updateById(updateObj);
    }

    @Override
    public List<AttDataDevCatRespDTO> getAttDataDevCatApiList(AttDataDevCatReqDTO reqVO) {
        MPJLambdaWrapper<AttDataDevCatDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(AttDataDevCatDO.class)
                .like(StringUtils.isNotBlank(reqVO.getName()), AttDataDevCatDO::getName, reqVO.getName());
        List<AttDataDevCatDO> attTaskCatDOS = attDataDevCatMapper.selectList(wrapper);
        return BeanUtils.toBean(attTaskCatDOS, AttDataDevCatRespDTO.class);
    }

    @Override
    public int removeAttDataDevCat(Collection<Long> idList) {
        // 批量删除数据开发类目管理
        return attDataDevCatMapper.deleteBatchIds(idList);
    }

    @Override
    public AttDataDevCatDO getAttDataDevCatById(Long id) {
        return attDataDevCatMapper.selectById(id);
    }

    @Override
    public List<AttDataDevCatDO> getAttDataDevCatList() {
        return attDataDevCatMapper.selectList();
    }

    @Override
    public List<AttDataDevCatDO> getAttDataDevCatList(AttDataDevCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttDataDevCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttDataDevCatDO::getName, reqVO.getName())
                .likeRightIfPresent(AttDataDevCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttDataDevCatDO::getProjectId,reqVO.getProjectId())
                .eqIfPresent(AttDataDevCatDO::getProjectCode,reqVO.getProjectCode())
                .orderByAsc(AttDataDevCatDO::getSortOrder);
        return attDataDevCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttDataDevCatDO> getAttDataDevCatMap() {
        List<AttDataDevCatDO> attDataDevCatList = attDataDevCatMapper.selectList();
        return attDataDevCatList.stream()
                .collect(Collectors.toMap(
                        AttDataDevCatDO::getId,
                        attDataDevCatDO -> attDataDevCatDO,
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
        LambdaQueryWrapper<AttDataDevCatDO> query = new LambdaQueryWrapper<AttDataDevCatDO>()
                .eq(AttDataDevCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttDataDevCatDO::getCode, parentCode)
                .isNotNull(AttDataDevCatDO::getCode)
                .orderByDesc(AttDataDevCatDO::getCode);
        List<AttDataDevCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttDataDevCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }


    /**
     * 导入数据开发类目管理数据
     *
     * @param importExcelList 数据开发类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttDataDevCat(List<AttDataDevCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttDataDevCatRespVO respVO : importExcelList) {
            try {
                AttDataDevCatDO attDataDevCatDO = BeanUtils.toBean(respVO, AttDataDevCatDO.class);
                Long attDataDevCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attDataDevCatId != null) {
                        AttDataDevCatDO existingAttDataDevCat = attDataDevCatMapper.selectById(attDataDevCatId);
                        if (existingAttDataDevCat != null) {
                            attDataDevCatMapper.updateById(attDataDevCatDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attDataDevCatId + " 的数据开发类目管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attDataDevCatId + " 的数据开发类目管理记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttDataDevCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attDataDevCatId);
                    AttDataDevCatDO existingAttDataDevCat = attDataDevCatMapper.selectOne(queryWrapper);
                    if (existingAttDataDevCat == null) {
                        attDataDevCatMapper.insert(attDataDevCatDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attDataDevCatId + " 的数据开发类目管理记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attDataDevCatId + " 的数据开发类目管理记录已存在。");
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
