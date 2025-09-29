package tech.qiantong.qdata.module.att.service.cat.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDocumentCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttDocumentCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttDocumentCatService;
import tech.qiantong.qdata.module.dp.api.service.document.IDpDocumentApiService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标准信息分类管理Service业务层处理
 *
 * @author qdata
 * @date 2025-08-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttDocumentCatServiceImpl extends ServiceImpl<AttDocumentCatMapper, AttDocumentCatDO> implements IAttDocumentCatService {
    @Resource
    private IDpDocumentApiService dpDocumentApiService;
    @Resource
    private AttDocumentCatMapper attDocumentCatMapper;

    @Override
    public PageResult<AttDocumentCatDO> getAttDocumentCatPage(AttDocumentCatPageReqVO pageReqVO) {
        return attDocumentCatMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AttDocumentCatDO> getAttDocumentCatList(AttDocumentCatPageReqVO reqVO) {
        MPJLambdaWrapper<AttDocumentCatDO> mpjLambdaWrapper = new MPJLambdaWrapper();
        mpjLambdaWrapper.selectAll(AttDocumentCatDO.class)
                .like(StringUtils.isNotEmpty(reqVO.getName()), AttDocumentCatDO::getName, reqVO.getName())
                .eq(reqVO.getParentId() != null, AttDocumentCatDO::getParentId, reqVO.getParentId())
                .eq(reqVO.getSortOrder() != null, AttDocumentCatDO::getSortOrder, reqVO.getSortOrder())
                .eq(reqVO.getValidFlag() != null, AttDocumentCatDO::getValidFlag, reqVO.getValidFlag())
                .eq(StringUtils.isNotEmpty(reqVO.getDescription()), AttDocumentCatDO::getDescription, reqVO.getDescription())
                .eq(StringUtils.isNotEmpty(reqVO.getCode()), AttDocumentCatDO::getCode, reqVO.getCode());
        return attDocumentCatMapper.selectList(mpjLambdaWrapper);
    }

    @Override
    public Long createAttDocumentCat(AttDocumentCatSaveReqVO createReqVO) {
        AttDocumentCatDO dictType = BeanUtils.toBean(createReqVO, AttDocumentCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attDocumentCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttDocumentCat(AttDocumentCatSaveReqVO updateReqVO) {
        AttDocumentCatDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = dpDocumentApiService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("存在标准，不允许禁用");
            }
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttDocumentCatDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("须先启用父级");
            }
        }
        // 更新标准信息分类管理
        AttDocumentCatDO updateObj = BeanUtils.toBean(updateReqVO, AttDocumentCatDO.class);
        return baseMapper.updateById(updateObj);
    }

    @Override
    public int removeAttDocumentCat(Long id) {
        AttDocumentCatDO catDO = baseMapper.selectById(id);
        Long countData = dpDocumentApiService.getCountByCatCode(catDO.getCode());
        if (countData > 0) {
            throw new ServiceException("存在标准，不允许删除");
        }
        // 单独删除标准信息分类管理
        return baseMapper.deleteById(id);
    }

    @Override
    public AttDocumentCatDO getAttDocumentCatById(Long id) {
        return attDocumentCatMapper.selectById(id);
    }

    @Override
    public List<AttDocumentCatDO> getAttDocumentCatList() {
        return attDocumentCatMapper.selectList();
    }

    @Override
    public Map<Long, AttDocumentCatDO> getAttDocumentCatMap() {
        List<AttDocumentCatDO> attDocumentCatList = attDocumentCatMapper.selectList();
        return attDocumentCatList.stream()
                .collect(Collectors.toMap(
                        AttDocumentCatDO::getId,
                        attDocumentCatDO -> attDocumentCatDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public boolean hasChildByAttDocumentCatId(Long id) {
        return attDocumentCatMapper.selectCount(AttDocumentCatDO::getParentId, id) > 0;
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
        LambdaQueryWrapper<AttDocumentCatDO> query = new LambdaQueryWrapper<AttDocumentCatDO>()
                .eq(AttDocumentCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttDocumentCatDO::getCode, parentCode)
                .isNotNull(AttDocumentCatDO::getCode)
                .orderByDesc(AttDocumentCatDO::getCode);
        List<AttDocumentCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttDocumentCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
