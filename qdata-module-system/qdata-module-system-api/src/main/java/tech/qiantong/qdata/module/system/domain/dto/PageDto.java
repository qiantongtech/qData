package tech.qiantong.qdata.module.system.domain.dto;

import lombok.Data;

/**
 * 分页对象
 */
@Data
public class PageDto {

    /**
     * 页码
     */

    private Integer pageNum;
    /**
     * 每页的数量
     */

    private Integer pageSize;


}
