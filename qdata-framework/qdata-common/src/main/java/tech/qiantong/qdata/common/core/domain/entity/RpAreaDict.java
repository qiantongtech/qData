package tech.qiantong.qdata.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务资源门户区域字典 DO 对象 RP_AREA_DICT
 *
 * @author qdata
 * @date 2025-04-21
 */
@Data
public class RpAreaDict extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 区域名称 */
    private String name;

    /** 编码 */
    private String code;

    /** 父级id */
    private Long parentId;

    /** 排序 */
    private Long sortOrder;

    /** 类型 */
    private String type;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    private List<RpAreaDict> children = new ArrayList<RpAreaDict>();

}
