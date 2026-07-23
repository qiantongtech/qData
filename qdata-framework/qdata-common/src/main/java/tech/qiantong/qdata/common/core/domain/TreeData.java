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
 * Purpose: number structure data
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
     * Node ID
     */
    private Long id;

    /**
     * Parent node ID (nullable)
     */
    private Long parentId;

    /**
     * Node name
     */
    private String name;

    /**
     * Node type
     */
    private String type;

    /**
     * Other extended data
     */
    private JSONObject otherData;

    /**
     * Child node
     */
    private List<TreeData> children;
}
