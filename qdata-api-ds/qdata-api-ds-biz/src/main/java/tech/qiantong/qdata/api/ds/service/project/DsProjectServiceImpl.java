package tech.qiantong.qdata.api.ds.service.project;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.project.DsProjectCreateReqDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectDeleteRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectUpdateReqDTO;
import tech.qiantong.qdata.api.ds.api.service.project.IDsProjectService;
import tech.qiantong.qdata.common.httpClient.DsRequestUtils;
import tech.qiantong.qdata.common.httpClient.constants.QianTongDCApiType;

/**
 * <P>
 * 用途:ds项目service实现
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:27
 **/
@Slf4j
@Service
public class DsProjectServiceImpl implements IDsProjectService {

    @Override
    public DsProjectRespDTO saveProject(DsProjectCreateReqDTO dsProjectCreateReqDTO) {
        QianTongDCApiType apiType = QianTongDCApiType.CREATE_PROJECT;
        return DsRequestUtils.request(apiType.getUrl(),
                apiType.getMethod(),
                dsProjectCreateReqDTO, null,
                DsProjectRespDTO.class);
    }

    @Override
    public DsProjectRespDTO updateProject(DsProjectUpdateReqDTO dsProjectUpdateReqDTO) {
        QianTongDCApiType apiType = QianTongDCApiType.UPDATE_PROJECT;
        return DsRequestUtils.request(StringUtils.replace(apiType.getUrl(), "{code}", String.valueOf(dsProjectUpdateReqDTO.getProjectCode())),
                apiType.getMethod(),
                dsProjectUpdateReqDTO, null,
                DsProjectRespDTO.class);
    }

    @Override
    public DsProjectDeleteRespDTO deleteProject(Long projectCode) {
        QianTongDCApiType apiType = QianTongDCApiType.DELETE_PROJECT;
        return DsRequestUtils.request(StringUtils.replace(apiType.getUrl(), "{code}", String.valueOf(projectCode)),
                apiType.getMethod(),
                null, null,
                DsProjectDeleteRespDTO.class);
    }
}
