package tech.qiantong.qdata.module.att.api.service.cat;

import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatRespDTO;

import java.util.List;

public interface IAttDataDevCatApiService {

    /**
     * 获得全部数据开发类目管理列表
     *
     * @return 数据开发类目管理列表
     */
    List<AttDataDevCatRespDTO> getAttDataDevCatApiList(AttDataDevCatReqDTO attDataDevCatReqDTO);
}
