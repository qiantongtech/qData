package tech.qiantong.qdata.module.dpp.utils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-29 15:24
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsResource implements Serializable {
    private static final long serialVersionUID = -2629332208479320L;

    private String resourceName;
}
