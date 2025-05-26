package tech.qiantong.qdata.module.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    /**
     * 角色名字
     */
    private String name;
    private String id;
    private Integer status;


}
