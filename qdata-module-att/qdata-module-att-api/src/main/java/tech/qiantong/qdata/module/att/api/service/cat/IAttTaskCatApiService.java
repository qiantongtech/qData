package tech.qiantong.qdata.module.att.api.service.cat;

import tech.qiantong.qdata.module.att.api.cat.dto.AttTaskCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttTaskCatRespDTO;

import java.util.List;

public interface IAttTaskCatApiService {
    List<AttTaskCatRespDTO> getAttTaskCatApiList(AttTaskCatReqDTO attTaskCatReqDTO);
}
