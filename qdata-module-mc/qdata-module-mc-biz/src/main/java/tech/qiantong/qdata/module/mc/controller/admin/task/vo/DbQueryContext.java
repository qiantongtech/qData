package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;

@Data
@AllArgsConstructor
public class DbQueryContext {

    /**
     * Validated, working DbQuery
     */
    private DbQuery dbQuery;

    /**
     * Query attributes corresponding to the current library/schema
     */
    private DbQueryProperty property;
}
