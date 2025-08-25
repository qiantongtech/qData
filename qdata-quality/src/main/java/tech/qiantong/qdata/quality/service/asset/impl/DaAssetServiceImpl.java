package tech.qiantong.qdata.quality.service.asset.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetReqDTO;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaAssetApiOutService;
import tech.qiantong.qdata.quality.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.quality.dal.mapper.asset.DaAssetMapper;
import tech.qiantong.qdata.quality.service.asset.IDaAssetService;

/**
 * 数据资产Service业务层处理
 *
 * @author lhs
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetServiceImpl extends ServiceImpl<DaAssetMapper, DaAssetDO> implements IDaAssetService, IDaAssetApiOutService {

    @Override
    public DaAssetRespDTO insertDaAsset(DaAssetReqDTO daAssetReqDTO) {
        return null;
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return null;
    }
}
