package tech.qiantong.qdata.common.core.domain;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <P>
 * 用途:数结构数据
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-26 10:23
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TreeData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 父节点ID（可空）
     */
    private Long parentId;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点类型
     */
    private String type;

    /**
     * 其他扩展数据
     */
    private JSONObject otherData;

    /**
     * 子节点
     */
    private List<TreeData> children;
}
