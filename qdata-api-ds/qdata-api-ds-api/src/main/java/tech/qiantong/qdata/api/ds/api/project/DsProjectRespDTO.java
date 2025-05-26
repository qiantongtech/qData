package tech.qiantong.qdata.api.ds.api.project;

import lombok.Data;
import tech.qiantong.qdata.api.ds.api.base.DsResultDTO;

/**
 * <P>
 * 用途:项目保存修改响应DTO
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:20
 **/
@Data
public class DsProjectRespDTO extends DsResultDTO {

    private Data data;

    @lombok.Data
    public class Data{
        /**
         * id
         */
        private Long id;

        /**
         * 项目编码
         */
        private Long code;

        /**
         * 项目名称
         */
        private String name;

        /**
         * 项目描述
         */
        private String description;
    }
}
