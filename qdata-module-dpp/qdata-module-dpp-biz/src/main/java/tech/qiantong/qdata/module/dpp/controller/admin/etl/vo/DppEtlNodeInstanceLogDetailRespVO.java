package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "Data development node instance formatted log detail Response VO")
public class DppEtlNodeInstanceLogDetailRespVO {

    private Long nodeInstanceId;
    private String taskName;
    private String status;
    private String statusName;
    private String currentStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refreshTime;

    private String duration;
    private List<DppEtlTaskInstanceLogLineRespVO> logList;
    private String log;
}
