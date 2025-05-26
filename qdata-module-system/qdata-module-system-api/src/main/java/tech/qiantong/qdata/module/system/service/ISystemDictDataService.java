package tech.qiantong.qdata.module.system.service;

import tech.qiantong.qdata.common.core.domain.entity.SysDictData;

import java.util.List;

public interface ISystemDictDataService {

    /**
     * 根据字典类型查询字典数据    提供服务资源门户字典值查询
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType);
}
