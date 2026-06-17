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
 * 数据元类目管理Service业务层处理
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
        //判断是否选择了他自己
        if (dgDataElemCatDO.getId().equals(updateReqVO.getParentId())) {
            throw new ServiceException("dg.error.parent.self", "切换上级不能选择自身作为上级类目");
        }
        // 更新数据元类目管理
        DgDataElemCatDO updateObj = BeanUtils.toBean(updateReqVO, DgDataElemCatDO.class);
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            dgDataElemCatMapper.updateValidFlag(dgDataElemCatDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            DgDataElemCatDO parent = dgDataElemCatMapper.selectById(dgDataElemCatDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("dg.error.parent.disabled", "须先启用父级");
            }
        }

        //修改上下级判断
        boolean flag = false;
        if (updateReqVO.getParentId() != null && !dgDataElemCatDO.getParentId().equals(updateReqVO.getParentId())) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        int i = dgDataElemCatMapper.updateById(updateObj);

        //判断上下级是否发生了改变
        if (flag) {
            //更改所有下级
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
                throw new ServiceException("dg.error.delete.ref.elem", "被标准数据元引用，不可删除");
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
         * 分成三种情况
         * 1.数据库无数据 调用YouBianCodeUtil.getNextYouBianCode(null);
         * 2.添加子节点，无兄弟元素 YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3.添加子节点有兄弟元素 YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        //找同类 确定上一个最大的code值
        LambdaQueryWrapper<DgDataElemCatDO> query = new LambdaQueryWrapper<DgDataElemCatDO>()
                .eq(DgDataElemCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), DgDataElemCatDO::getCode, parentCode)
                .isNotNull(DgDataElemCatDO::getCode)
                .orderByDesc(DgDataElemCatDO::getCode);
        List<DgDataElemCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                DgDataElemCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
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
