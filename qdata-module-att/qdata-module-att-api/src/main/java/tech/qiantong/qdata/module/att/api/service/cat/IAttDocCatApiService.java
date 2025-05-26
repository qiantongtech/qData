package tech.qiantong.qdata.module.att.api.service.cat;

import tech.qiantong.qdata.module.att.api.cat.dto.AttDocCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttDocCatRespDTO;

import java.util.List;

public interface IAttDocCatApiService {

    /**
     * 获得全部数据资产文档类目管理列表         服务资源模块使用
     *
     * @return 数据资产文档类目管理列表
     */
    List<AttDocCatRespDTO> getAttDocCatList(AttDocCatReqDTO reqDTO);
}
