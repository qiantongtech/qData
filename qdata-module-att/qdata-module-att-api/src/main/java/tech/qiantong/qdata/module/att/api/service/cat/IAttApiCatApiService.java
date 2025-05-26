package tech.qiantong.qdata.module.att.api.service.cat;

import tech.qiantong.qdata.module.att.api.cat.dto.AttApiCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttApiCatRespDTO;

import java.util.List;

public interface IAttApiCatApiService {

    /**
     * 获得全部数据服务类目管理列表  服务资源模块使用
     *
     * @return 数据服务类目管理列表
     */
    List<AttApiCatRespDTO> getAttApiCatList(AttApiCatReqDTO attApiCatReqDTO);
}
