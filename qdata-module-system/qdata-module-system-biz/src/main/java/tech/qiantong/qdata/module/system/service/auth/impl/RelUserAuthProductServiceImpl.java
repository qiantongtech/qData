package tech.qiantong.qdata.module.system.service.auth.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.system.dal.dataobject.auth.RelUserAuthProductDO;
import tech.qiantong.qdata.module.system.dal.mapper.auth.RelUserAuthProductMapper;
import tech.qiantong.qdata.module.system.service.auth.IRelUserAuthProductService;

import javax.annotation.Resource;

/**
 * 用户与认证中心关系Service业务层处理
 *
 * @author qdata
 * @date 2024-11-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RelUserAuthProductServiceImpl  extends ServiceImpl<RelUserAuthProductMapper, RelUserAuthProductDO> implements IRelUserAuthProductService {
    @Resource
    private RelUserAuthProductMapper relUserAuthProductMapper;

}
