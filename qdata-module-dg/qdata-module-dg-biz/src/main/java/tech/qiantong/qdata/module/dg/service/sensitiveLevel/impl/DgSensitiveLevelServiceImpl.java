package tech.qiantong.qdata.module.dg.service.sensitiveLevel.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.sensitiveLevel.DgSensitiveLevelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.sensitiveLevel.DgSensitiveLevelMapper;
import tech.qiantong.qdata.module.dg.service.sensitiveLevel.IDgSensitiveLevelService;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 敏感等级Service业务层处理
 *
 * @author Chaos
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgSensitiveLevelServiceImpl extends ServiceImpl<DgSensitiveLevelMapper, DgSensitiveLevelDO> implements IDgSensitiveLevelService {
    @Resource
    private DgSensitiveLevelMapper mapper;

    @Override
    public PageResult<DgSensitiveLevelDO> getDgSensitiveLevelPage(DgSensitiveLevelPageReqVO pageReqVO) {
        return mapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgSensitiveLevel(DgSensitiveLevelSaveReqVO createReqVO) {
        DgSensitiveLevelDO dictType = BeanUtils.toBean(createReqVO, DgSensitiveLevelDO.class);
        mapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgSensitiveLevel(DgSensitiveLevelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新敏感等级
        DgSensitiveLevelDO updateObj = BeanUtils.toBean(updateReqVO, DgSensitiveLevelDO.class);
        return mapper.updateById(updateObj);
    }

    @Override
    public int removeDgSensitiveLevel(Collection<Long> idList) {
        // 批量删除敏感等级
        return mapper.deleteBatchIds(idList);
    }

    @Override
    public DgSensitiveLevelDO getDgSensitiveLevelById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public Boolean updateStatus(Long id, Long status) {
        return this.update(Wrappers.lambdaUpdate(DgSensitiveLevelDO.class)
                .eq(DgSensitiveLevelDO::getId, id)
                .set(DgSensitiveLevelDO::getOnlineFlag, status));
    }
}
