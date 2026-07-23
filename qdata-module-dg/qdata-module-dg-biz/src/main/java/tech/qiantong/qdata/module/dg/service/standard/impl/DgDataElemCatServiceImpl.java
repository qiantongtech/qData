package tech.qiantong.qdata.module.dg.service.standard.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemCatDO;
import tech.qiantong.qdata.module.dg.dal.mapper.standard.DgDataElemCatMapper;
import tech.qiantong.qdata.module.dg.dal.mapper.standard.DgDataElemMapper;
import tech.qiantong.qdata.module.dg.service.standard.IDgDataElemCatService;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Data Element Category Management Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class DgDataElemCatServiceImpl extends ServiceImpl<DgDataElemCatMapper, DgDataElemCatDO> implements IDgDataElemCatService {
    private final DgDataElemCatMapper dgDataElemCatMapper;
    private final DgDataElemMapper dataElemMapper;

    @Override
    public Long createDgDataElemCat(DgDataElemCatSaveReqVO createReqVO) {
        DgDataElemCatDO dictType = BeanUtils.toBean(createReqVO, DgDataElemCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        dgDataElemCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDataElemCat(DgDataElemCatSaveReqVO updateReqVO) {
        DgDataElemCatDO dgDataElemCatDO = dgDataElemCatMapper.selectById(updateReqVO.getId());
        if (dgDataElemCatDO == null) {
            return 0;
        }
        // Check if it selected itself
        if (dgDataElemCatDO.getId().equals(updateReqVO.getParentId())) {
            throw new ServiceException("dg.error.parent.self", "Cannot select self as parent category");
        }
        // Update data element category management
        DgDataElemCatDO updateObj = BeanUtils.toBean(updateReqVO, DgDataElemCatDO.class);
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            dgDataElemCatMapper.updateValidFlag(dgDataElemCatDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            DgDataElemCatDO parent = dgDataElemCatMapper.selectById(dgDataElemCatDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("dg.error.parent.disabled", "Please enable the parent category first");
            }
        }

        // Check if parent-child relationship has changed
        boolean flag = false;
        if (updateReqVO.getParentId() != null && !dgDataElemCatDO.getParentId().equals(updateReqVO.getParentId())) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        int i = dgDataElemCatMapper.updateById(updateObj);

        // Check if parent-child relationship has changed
        if (flag) {
            // Update all children
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }

        return i;
    }

    @Override
    public void changeCodeByPid(Long pid, String parentCode) {
        List<DgDataElemCatDO> list = baseMapper.selectList(Wrappers.lambdaQuery(DgDataElemCatDO.class)
                .eq(DgDataElemCatDO::getParentId, pid)
                .orderByAsc(DgDataElemCatDO::getCreateTime));
        if (list != null && !list.isEmpty()) {
            list.forEach(e -> {
                String codeNew = createCode(e.getParentId(), parentCode);
                e.setCode(codeNew);
                baseMapper.updateById(e);
                this.changeCodeByPid(e.getId(), e.getCode());
            });
        }
    }

    @Override
    public int removeDgDataElemCat(Collection<Long> idList) {
        int count = 0;
        List<DgDataElemCatDO> list = baseMapper.selectBatchIds(idList);
        for (DgDataElemCatDO cat : list) {
            if (dataElemMapper.existsByCatCode(cat.getCode())) {
                throw new ServiceException("dg.error.delete.ref.elem", "Referenced by standard data element, cannot be deleted");
            }
        }
        for (DgDataElemCatDO cat : list) {
            count += baseMapper.delete(Wrappers.lambdaQuery(DgDataElemCatDO.class)
                    .likeRight(DgDataElemCatDO::getCode, cat.getCode()));
        }
        return count;
    }

    @Override
    public DgDataElemCatDO getDgDataElemCatById(Long id) {
        return dgDataElemCatMapper.selectById(id);
    }

    @Override
    public List<DgDataElemCatDO> getDgDataElemCatList(DgDataElemCatPageReqVO reqVO) {
        return dgDataElemCatMapper.selectList(reqVO);
    }

    @Override
    public String createCode(Long parentId, String parentCode) {
        String categoryCode = null;
        /*
         * Divided into three cases
         * 1. No data in database, call YouBianCodeUtil.getNextYouBianCode(null);
         * 2. Add child node without sibling, YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3. Add child node with sibling, YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        // Find the largest code value among siblings
        LambdaQueryWrapper<DgDataElemCatDO> query = new LambdaQueryWrapper<DgDataElemCatDO>()
                .eq(DgDataElemCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), DgDataElemCatDO::getCode, parentCode)
                .isNotNull(DgDataElemCatDO::getCode)
                .orderByDesc(DgDataElemCatDO::getCode);
        List<DgDataElemCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Case 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Case 2
                DgDataElemCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // Case 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    @Override
    public BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids) {
        List<DgDataElemCatDO> list = baseMapper.selectBatchIds(ids);
        int cannotDeleteCount = 0;
        List<Long> canDeleteIds = new ArrayList<>();
        for (DgDataElemCatDO one : list) {
            if (one.getValidFlag()) {
                cannotDeleteCount++;
                continue;
            }
            boolean exists = dataElemMapper.existsByCatCode(one.getCode());
            if (exists) {
                cannotDeleteCount++;
            } else {
                canDeleteIds.add(one.getId());
            }
        }
        return new BatchDeleteCheck<>(cannotDeleteCount, canDeleteIds);
    }

}
