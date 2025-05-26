package tech.qiantong.qdata.module.system.dal.dataobject.message;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;


/**
 * 消息模板 DO 对象 message_template
 *
 * @author qdata
 * @date 2024-10-31
 */
@Data
@TableName(value = "message_template")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("message_template_seq")
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplateDO extends BaseEntity {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 消息标题 */
    private String title;

    /** 消息模板内容 */
    private String content;

    /** 消息类别 */
    private Integer category;

    /** 消息等级 */
    private Integer msgLevel;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
