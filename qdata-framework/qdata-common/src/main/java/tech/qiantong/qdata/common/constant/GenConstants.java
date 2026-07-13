/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.common.constant;

/**
 * Code generation common constants
 *
 * @author qdata
 */
public class GenConstants
{
    /** Single table (add, delete, modify, query) */
    public static final String TPL_CRUD = "crud";

    /** Tree table (add, delete, modify, query) */
    public static final String TPL_TREE = "tree";

    /** Master and child tables (add, delete, modify and query) */
    public static final String TPL_SUB = "sub";

    /** Tree encoding field */
    public static final String TREE_CODE = "treeCode";

    /** Tree parent encoding field */
    public static final String TREE_PARENT_CODE = "treeParentCode";

    /** Tree name field */
    public static final String TREE_NAME = "treeName";

    /** Upper level menu ID field */
    public static final String PARENT_MENU_ID = "parentMenuId";

    /** Upper level menu name field */
    public static final String PARENT_MENU_NAME = "parentMenuName";

    /** Database string type */
    public static final String[] COLUMNTYPE_STR = { "char", "varchar", "nvarchar", "varchar2" };

    /** Database text type */
    public static final String[] COLUMNTYPE_TEXT = { "tinytext", "text", "mediumtext", "longtext" };

    /** Database time type */
    public static final String[] COLUMNTYPE_TIME = { "datetime", "time", "date", "timestamp" };

    /** Database numeric type */
    public static final String[] COLUMNTYPE_NUMBER = { "tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal", "int8", "int4" };

    /** The page does not require editing fields */
    public static final String[] COLUMNNAME_NOT_EDIT = { "id", "create_by", "create_time", "del_flag", "valid_flag",
            "creator_id", "update_by", "updater_id", "update_time" };

    /** List fields that do not need to be displayed on the page */
    public static final String[] COLUMNNAME_NOT_LIST ={"del_flag", "valid_flag", "creator_id", "update_by", "updater_id", "update_time" };

    /** The page does not require query fields */
    public static final String[] COLUMNNAME_NOT_QUERY = { "id", "create_by", "del_flag", "valid_flag", "creator_id",
            "update_by", "updater_id", "update_time" , "remark" };

    /** Entity base class fields */
    public static final String[] BASE_ENTITY = { "createBy", "createTime", "updateBy", "updateTime", "remark", "searchValue",
            "creatorId", "creatorId", "updaterId", "remark", "params" };

    /** Tree base class field */
    //public static final String[] TREE_ENTITY = { "parentName", "parentId", "orderNum", "ancestors", "children" };
    public static final String[] TREE_ENTITY = { "parentName", "orderNum", "ancestors", "children" };

    /** text box */
    public static final String HTML_INPUT = "input";

    /** Text field */
    public static final String HTML_TEXTAREA = "textarea";

    /** Drop-down box */
    public static final String HTML_SELECT = "select";

    /** Radio button */
    public static final String HTML_RADIO = "radio";

    /** Checkbox */
    public static final String HTML_CHECKBOX = "checkbox";

    /** Date control */
    public static final String HTML_DATETIME = "datetime";

    /** Image upload control */
    public static final String HTML_IMAGE_UPLOAD = "imageUpload";

    /** File upload control */
    public static final String HTML_FILE_UPLOAD = "fileUpload";

    /** Rich text control */
    public static final String HTML_EDITOR = "editor";

    /** String type */
    public static final String TYPE_STRING = "String";

    /** Integer type */
    public static final String TYPE_INTEGER = "Integer";

    /** Long integer type */
    public static final String TYPE_LONG = "Long";

    /** Floating point type */
    public static final String TYPE_DOUBLE = "Double";

    /** High-precision calculation type */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /** Time type */
    public static final String TYPE_DATE = "Date";

    /** Fuzzy query */
    public static final String QUERY_LIKE = "LIKE";

    /** Equality query */
    public static final String QUERY_EQ = "EQ";

    /** Equality range */
    public static final String QUERY_BETWEEN = "BETWEEN";

    /** required */
    public static final String REQUIRE = "1";
}
