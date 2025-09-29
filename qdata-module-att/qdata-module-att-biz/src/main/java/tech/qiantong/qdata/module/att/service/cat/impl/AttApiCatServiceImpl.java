package tech.qiantong.qdata.module.att.service.cat.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.cat.dto.AttApiCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttApiCatRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.IAttApiCatApiService;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttApiCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttApiCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttApiCatService;
import tech.qiantong.qdata.module.ds.api.service.api.DsApiService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据服务类目管理Service业务层处理
 *
 * @author qdata
 * @date 2025-03-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttApiCatServiceImpl extends ServiceImpl<AttApiCatMapper, AttApiCatDO> implements IAttApiCatService, IAttApiCatApiService {
    @Resource
    private AttApiCatMapper attApiCatMapper;
    @Resource
    private DsApiService dsApiService;

    @Override
    public PageResult<AttApiCatDO> getAttApiCatPage(AttApiCatPageReqVO pageReqVO) {
        return attApiCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttApiCat(AttApiCatSaveReqVO createReqVO) {
        AttApiCatDO dictType = BeanUtils.toBean(createReqVO, AttApiCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attApiCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttApiCat(AttApiCatSaveReqVO updateReqVO) {
        AttApiCatDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = dsApiService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("存在API服务，不允许禁用");
            }
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttApiCatDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("须先启用父级");
            }
        }
        // 更新数据服务类目管理
        AttApiCatDO updateObj = BeanUtils.toBean(updateReqVO, AttApiCatDO.class);
        return attApiCatMapper.updateById(updateObj);
    }

    @Override
    public int removeAttApiCat(Collection<Long> idList) {
        List<AttApiCatDO> attApiCatDOS = baseMapper.selectBatchIds(idList);
        for (AttApiCatDO catDO : attApiCatDOS) {
            Long countData = dsApiService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("存在API服务，不允许删除");
            }
        }
        // 批量删除数据服务类目管理
        return attApiCatMapper.deleteBatchIds(idList);
    }

    @Override
    public AttApiCatDO getAttApiCatById(Long id) {
        return attApiCatMapper.selectById(id);
    }

    @Override
    public List<AttApiCatDO> getAttApiCatList() {
        return attApiCatMapper.selectList();
    }

    @Override
    public List<AttApiCatDO> getAttApiCatList(AttApiCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttApiCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttApiCatDO::getName, reqVO.getName())
                .eqIfPresent(AttApiCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttApiCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttApiCatDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttApiCatDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(AttApiCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttApiCatDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(AttApiCatDO::getSortOrder);
        return attApiCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttApiCatDO> getAttApiCatMap() {
        List<AttApiCatDO> attApiCatList = attApiCatMapper.selectList();
        return attApiCatList.stream()
                .collect(Collectors.toMap(
                        AttApiCatDO::getId,
                        attApiCatDO -> attApiCatDO,
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
        LambdaQueryWrapper<AttApiCatDO> query = new LambdaQueryWrapper<AttApiCatDO>()
                .eq(AttApiCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttApiCatDO::getCode, parentCode)
                .isNotNull(AttApiCatDO::getCode)
                .orderByDesc(AttApiCatDO::getCode);
        List<AttApiCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttApiCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    /**
     * 导入数据服务类目管理数据
     *
     * @param importExcelList 数据服务类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttApiCat(List<AttApiCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttApiCatRespVO respVO : importExcelList) {
            try {
                AttApiCatDO attApiCatDO = BeanUtils.toBean(respVO, AttApiCatDO.class);
                Long attApiCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attApiCatId != null) {
                        AttApiCatDO existingAttApiCat = attApiCatMapper.selectById(attApiCatId);
                        if (existingAttApiCat != null) {
                            attApiCatMapper.updateById(attApiCatDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attApiCatId + " 的数据服务类目管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attApiCatId + " 的数据服务类目管理记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttApiCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attApiCatId);
                    AttApiCatDO existingAttApiCat = attApiCatMapper.selectOne(queryWrapper);
                    if (existingAttApiCat == null) {
                        attApiCatMapper.insert(attApiCatDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attApiCatId + " 的数据服务类目管理记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attApiCatId + " 的数据服务类目管理记录已存在。");
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
    public List<AttApiCatRespDTO> getAttApiCatList(AttApiCatReqDTO attApiCatReqDTO) {
        LambdaQueryWrapperX<AttApiCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttApiCatDO::getName, attApiCatReqDTO.getName())
                .eqIfPresent(AttApiCatDO::getParentId, attApiCatReqDTO.getParentId())
                .eqIfPresent(AttApiCatDO::getSortOrder, attApiCatReqDTO.getSortOrder())
                .eqIfPresent(AttApiCatDO::getDescription, attApiCatReqDTO.getDescription())
                .eqIfPresent(AttApiCatDO::getCode, attApiCatReqDTO.getCode())
                .orderByAsc(AttApiCatDO::getSortOrder);
        List<AttApiCatDO> attApiCatDOS = attApiCatMapper.selectList(queryWrapperX);
        if (CollectionUtils.isNotEmpty(attApiCatDOS)) {
            List<AttApiCatRespDTO> attApiCatRespDTOS = new ArrayList<>();
            for (int i = 0; i < attApiCatDOS.size(); i++) {
                AttApiCatDO attApiCatDO = attApiCatDOS.get(i);
                AttApiCatRespDTO attApiCatRespDTO = BeanUtils.toBean(attApiCatDO, AttApiCatRespDTO.class);
                attApiCatRespDTOS.add(attApiCatRespDTO);
            }
            return attApiCatRespDTOS;
        }
        return Collections.emptyList();
    }
}
