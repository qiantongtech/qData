package tech.qiantong.qdata.module.att.service.cat.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.att.api.cat.IAttCatService;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttCatMapper;

import javax.annotation.Resource;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-11 16:53
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttCatServiceImpl implements IAttCatService {

    @Resource
    private AttCatMapper attCatMapper;

    @Override
    public Long getCatIdByTableNameAndCatCode(String tableName, String catCode) {
        return attCatMapper.getCatIdByTableNameAndCatCode(tableName, catCode);
    }
}
