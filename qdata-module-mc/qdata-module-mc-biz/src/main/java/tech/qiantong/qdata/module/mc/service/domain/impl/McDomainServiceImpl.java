package tech.qiantong.qdata.module.mc.service.domain.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.domain.McDomainDO;
import tech.qiantong.qdata.module.mc.dal.mapper.domain.McDomainMapper;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McDbMapper;
import tech.qiantong.qdata.module.mc.service.domain.IMcDomainService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务域Service业务层处理
 *
 * @author qdata
 * @date 2026-02-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McDomainServiceImpl  extends ServiceImpl<McDomainMapper,McDomainDO> implements IMcDomainService {
    @Resource
    private McDomainMapper mcDomainMapper;
    @Resource
    private McDbMapper dbMapper;

    @Override
    public PageResult<McDomainDO> getMcDomainPage(McDomainPageReqVO pageReqVO) {
        return mcDomainMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createMcDomain(McDomainSaveReqVO createReqVO) {
        McDomainDO dictType = BeanUtils.toBean(createReqVO, McDomainDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        mcDomainMapper.insert(dictType);
        return dictType.getId();
    }

    public String createCode(Long parentId, String parentCode) {
        String categoryCode = null;
        /*
         * 分成三种情况
         * 1.数据库无数据 调用YouBianCodeUtil.getNextYouBianCode(null);
         * 2.添加子节点，无兄弟元素 YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3.添加子节点有兄弟元素 YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        //找同类 确定上一个最大的code值
        LambdaQueryWrapper<McDomainDO> query = new LambdaQueryWrapper<McDomainDO>()
                .eq(McDomainDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), McDomainDO::getCode, parentCode)
                .isNotNull(McDomainDO::getCode)
                .orderByDesc(McDomainDO::getCode);
        List<McDomainDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                McDomainDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }


    @Override
    public int updateMcDomain(McDomainSaveReqVO updateReqVO) {
//        // 相关校验
//
//        // 更新业务域
//        McDomainDO updateDomainDO = BeanUtils.toBean(updateReqVO, McDomainDO.class);
//        return mcDomainMapper.updateById(updateDomainDO);
        McDomainDO mcDomainDO = mcDomainMapper.selectById(updateReqVO.getId());
        if (mcDomainDO == null) {
            return 0;
        }
        //判断是否选择了他自己
        if (mcDomainDO.getId().equals(updateReqVO.getParentId())) {
            throw new ServiceException("mc.error.parent.self", "切换上级不能选择自身作为上级类目");
        }
        // 更新业务域管理
        McDomainDO updateObj = BeanUtils.toBean(updateReqVO, McDomainDO.class);
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            mcDomainMapper.updateValidFlag(mcDomainDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            McDomainDO parent = mcDomainMapper.selectById(mcDomainDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("mc.error.parent.disabled", "须先启用父级");
            }
        }
        //修改上下级判断
        boolean flag = false;
        if (updateReqVO.getParentId() != null && !mcDomainDO.getParentId().equals(updateReqVO.getParentId())) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        int i = mcDomainMapper.updateById(updateObj);

        //判断上下级是否发生了改变
        if (flag) {
            //更改所有下级
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }

        return i;

    }

    public void changeCodeByPid(Long pid, String parentCode) {
        List<McDomainDO> list = baseMapper.selectList(Wrappers.lambdaQuery(McDomainDO.class)
                .eq(McDomainDO::getParentId, pid)
                .orderByAsc(McDomainDO::getCreateTime));
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
    public int removeMcDomain(Collection<Long> idList) {
        // 批量删除业务域
       // return mcDomainMapper.deleteBatchIds(idList);
        int count = 0;
        List<McDomainDO> list = baseMapper.selectBatchIds(idList);
        for (McDomainDO one : list) {
//            if (mcTaskApiService.existsByDomainCode(one.getCode())) {
//                throw new ServiceException("被元数据采集引用，不可删除");
//            }
            if (dbMapper.existsBySourceSystemName(one.getCode())) {
                throw new ServiceException("mc.error.ref.db", "被库元数据引用，不可删除");
            }
        }
        for (McDomainDO one : list) {
            count += baseMapper.delete(Wrappers.lambdaQuery(McDomainDO.class)
                    .likeRight(McDomainDO::getCode, one.getCode()));
        }
        return count;
    }

    @Override
    public McDomainDO getMcDomainById(Long id) {
        return mcDomainMapper.selectById(id);
    }

    @Override
    public List<McDomainDO> getMcDomainList(McDomainPageReqVO mcDomain) {
        return mcDomainMapper.selectList(mcDomain);
    }

    @Override
    public Map<Long, McDomainDO> getMcDomainMap() {
        List<McDomainDO> mcDomainList = mcDomainMapper.selectList();
        return mcDomainList.stream()
                .collect(Collectors.toMap(
                        McDomainDO::getId,
                        mcDomainDO -> mcDomainDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids) {
        List<McDomainDO> list = baseMapper.selectBatchIds(ids);
        int cannotDeleteCount = 0;
        List<Long> canDeleteIds = new ArrayList<>();
        for (McDomainDO one : list) {
            if (one.getValidFlag()) {
                cannotDeleteCount++;
                continue;
            }
            boolean exists = dbMapper.existsBySourceSystemName(one.getCode());
            if (exists) {
                cannotDeleteCount++;
            } else {
                canDeleteIds.add(one.getId());
            }
        }
        return new BatchDeleteCheck<>(cannotDeleteCount, canDeleteIds);
    }
}
