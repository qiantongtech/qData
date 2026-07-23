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

package tech.qiantong.qdata.generator.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.velocity.VelocityContext;
import tech.qiantong.qdata.common.constant.GenConstants;
import tech.qiantong.qdata.common.core.domain.entity.SysDictData;
import tech.qiantong.qdata.common.core.domain.entity.SysDictType;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.generator.domain.GenTable;
import tech.qiantong.qdata.generator.domain.GenTableColumn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Template processing tools
 *
 * @author qdata
 */
public class VelocityUtils
{
    /** Project space path */
    private static final String PROJECT_PATH = "main/java";

    /** mybatis space path */
    private static final String MYBATIS_PATH = "main/resources/mapper";

    /** Default upper-level menu, system tools */
    private static final String DEFAULT_PARENT_MENU_ID = "3";

    /**
     * Set template variable information
     *
     * @return template list
     */
    public static VelocityContext prepareContext(GenTable genTable)
    {
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tplCategory", genTable.getTplCategory());
        velocityContext.put("tableName", genTable.getTableName());
        velocityContext.put("functionName", StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        velocityContext.put("ClassName", genTable.getClassName());
        velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
        velocityContext.put("moduleName", genTable.getModuleName());
        velocityContext.put("BusinessName", StringUtils.capitalize(genTable.getBusinessName()));
        velocityContext.put("businessName", genTable.getBusinessName());
        velocityContext.put("basePackage", getPackagePrefix(packageName));
        velocityContext.put("packageName", packageName);
        // Top-level module name such as: system
        velocityContext.put("topModule", packageName.substring(packageName.lastIndexOf(".") + 1));
        velocityContext.put("controllerPrefix", packageName.replaceFirst("^.*?\\.module\\.", "").replace(".", "/"));
        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("datetime", DateUtils.getDate());
        velocityContext.put("pkColumn", genTable.getPkColumn());
        velocityContext.put("importList", getImportList(genTable));
        // Package module name such as: example
        String sysModule = velocityContext.get("controllerPrefix").toString().replace("/", ":");
        velocityContext.put("permissionPrefix", getPermissionPrefix(sysModule, moduleName, businessName.replace(sysModule, "").toLowerCase()));
        velocityContext.put("columns", genTable.getColumns());
        velocityContext.put("table", genTable);
        velocityContext.put("dicts", getDicts(genTable));
        setMenuVelocityContext(velocityContext, genTable);
        if (GenConstants.TPL_TREE.equals(tplCategory))
        {
            setTreeVelocityContext(velocityContext, genTable);
        }
        if (GenConstants.TPL_SUB.equals(tplCategory))
        {
            setSubVelocityContext(velocityContext, genTable);
        }
        return velocityContext;
    }


    /**
     * Set dictionary template variable information
     *
     * @return template list
     */
    public static VelocityContext prepareDictContext(SysDictType sysDictType)
    {
        String dictName = sysDictType.getDictName();
        String dictType = sysDictType.getDictType();
        String status = sysDictType.getStatus();
        List<SysDictData> sysDictData = sysDictType.getSysDictData();


        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("dictName", sysDictType.getDictName());
        velocityContext.put("dictTypeOriginal", dictType);
        velocityContext.put("dictType", StringUtils.toCamelCase(dictType));
        velocityContext.put("DictType", StringUtils.convertToCamelCase(dictType));
        velocityContext.put("sysDictData", sysDictData);
        velocityContext.put("datetime", DateUtils.getDate());

        return velocityContext;
    }

    public static void setMenuVelocityContext(VelocityContext context, GenTable genTable)
    {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSON.parseObject(options);
        String parentMenuId = getParentMenuId(paramsObj);
        context.put("parentMenuId", parentMenuId);
    }

    public static void setTreeVelocityContext(VelocityContext context, GenTable genTable)
    {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSON.parseObject(options);
        String treeCode = getTreecode(paramsObj);
        String treeParentCode = getTreeParentCode(paramsObj);
        String treeName = getTreeName(paramsObj);

        context.put("treeCode", treeCode);
        context.put("treeParentCode", treeParentCode);
        context.put("treeName", treeName);
        context.put("expandColumn", getExpandColumn(genTable));
        if (paramsObj.containsKey(GenConstants.TREE_PARENT_CODE))
        {
            context.put("tree_parent_code", paramsObj.getString(GenConstants.TREE_PARENT_CODE));
        }
        if (paramsObj.containsKey(GenConstants.TREE_NAME))
        {
            context.put("tree_name", paramsObj.getString(GenConstants.TREE_NAME));
        }
    }

    public static void setSubVelocityContext(VelocityContext context, GenTable genTable)
    {
        GenTable subTable = genTable.getSubTable();
        String subTableName = genTable.getSubTableName();
        String subTableFkName = genTable.getSubTableFkName();
        String subClassName = genTable.getSubTable().getClassName();
        String subTableFkClassName = StringUtils.convertToCamelCase(subTableFkName);

        context.put("subTable", subTable);
        context.put("subTableName", subTableName);
        context.put("subTableFkName", subTableFkName);
        context.put("subTableFkClassName", subTableFkClassName);
        context.put("subTableFkclassName", StringUtils.uncapitalize(subTableFkClassName));
        context.put("subClassName", subClassName);
        context.put("subclassName", StringUtils.uncapitalize(subClassName));
        context.put("subImportList", getImportList(genTable.getSubTable()));
    }

    /**
     * Get template information
     * @param tplCategory generated template
     * @param tplWebType front-end type
     * @return template list
     */
    public static List<String> getTemplateList(String tplCategory, String tplWebType)
    {
        String useWebType = "vm/vue";
        if ("element-plus".equals(tplWebType))
        {
            useWebType = "vm/vue/v3";
        }
        List<String> templates = new ArrayList<String>();
        templates.add("vm/java/do.java.vm");
        templates.add("vm/java/reqDTO.java.vm");
        templates.add("vm/java/respDTO.java.vm");
        templates.add("vm/java/respVO.java.vm");
        templates.add("vm/java/pageReqVO.java.vm");
        templates.add("vm/java/saveReqVO.java.vm");
        templates.add("vm/java/convert.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/java/mapper.java.vm");
        templates.add("vm/xml/mapper.xml.vm");
        templates.add("vm/sql/sql.vm");
        templates.add("vm/js/api.js.vm");
        templates.add("vm/i18n/messages.properties.vm");
        templates.add("vm/i18n/messages_zh_CN.properties.vm");
        templates.add("vm/i18n/messages_en_US.properties.vm");
        templates.add("vm/i18n/messages_ja_JP.properties.vm");
        templates.add("vm/i18n/zh_CN.js.vm");
        templates.add("vm/i18n/en_US.js.vm");
        templates.add("vm/i18n/ja_JP.js.vm");
        if (GenConstants.TPL_CRUD.equals(tplCategory))
        {
            templates.add(useWebType + "/index.vue.vm");
            templates.add(useWebType + "/selection/single-selection.vue.vm");
            templates.add(useWebType + "/selection/multiple-selection.vue.vm");
            templates.add(useWebType + "/detail/complex-detail.vue.vm");
            templates.add(useWebType + "/detail/componentOne.vue.vm");
            templates.add(useWebType + "/detail/componentTwo.vue.vm");
        }
        else if (GenConstants.TPL_TREE.equals(tplCategory))
        {
            templates.add(useWebType + "/index-tree.vue.vm");
        }
        else if (GenConstants.TPL_SUB.equals(tplCategory))
        {
            templates.add(useWebType + "/index.vue.vm");
            templates.add("vm/java/sub-domain.java.vm");
        }
        return templates;
    }


    /**
     * Get template information
     * @return enum class template list
     */
    public static List<String> getTemplateListForDict()
    {

        List<String> templates = new ArrayList<>();
        templates.add("vm/java/dict.java.vm");

        return templates;
    }

    public static String getEnumFileName(String dictType){
        // File name
        String fileName = "";
        fileName = StringUtils.format("{}Enum.java",  StringUtils.convertToCamelCase(dictType));
        return fileName;
    }


    /**
     * Get file name
     */
    public static String getFileName(String template, GenTable genTable)
    {
        // File name
        String fileName = "";
        // Package path
        String packageName = genTable.getPackageName();
        // Module name
        String moduleName = genTable.getModuleName();
        // Uppercase class name
        String className = genTable.getClassName();
        // Business name
        String businessName = genTable.getBusinessName();

        String javaPath = PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
        String javaPathApi = javaPath;

        String module = packageName.replaceFirst("^.*?\\.module\\.", "").replaceFirst("^[^.]+\\.", "").replace(".", "/");
        String module1 = "qdata-module-"+module+"-biz";
        String module0 = "qdata-module-"+module;
        javaPath = module0 + "-biz/src/" + javaPath;
        javaPathApi = module0 + "-api/src/" + javaPathApi;
        String mybatisPath = module0 + "-biz/src/" + MYBATIS_PATH + "/" + module + "/" + moduleName;
        String vuePath = "vue";

        if (template.contains("do.java.vm"))
        {
            fileName = StringUtils.format("{}/dal/dataobject/{}/{}.java", javaPath, moduleName, className + "DO");
        }
        if (template.contains("reqDTO.java.vm"))
        {
            fileName = StringUtils.format("{}/api/{}/dto/{}.java", javaPathApi, moduleName, className + "ReqDTO");
        }
        if (template.contains("respDTO.java.vm"))
        {
            fileName = StringUtils.format("{}/api/{}/dto/{}.java", javaPathApi, moduleName, className + "RespDTO");
        }
        if (template.contains("respVO.java.vm"))
        {
            fileName = StringUtils.format("{}/controller/admin/{}/vo/{}.java", javaPath, moduleName, className + "RespVO");
        }
        if (template.contains("pageReqVO.java.vm"))
        {
            fileName = StringUtils.format("{}/controller/admin/{}/vo/{}.java", javaPath, moduleName, className + "PageReqVO");
        }
        if (template.contains("convert.java.vm"))
        {
            fileName = StringUtils.format("{}/convert/{}/{}.java", javaPath, moduleName, className + "Convert");
        }
        if (template.contains("saveReqVO.java.vm"))
        {
            fileName = StringUtils.format("{}/controller/admin/{}/vo/{}.java", javaPath, moduleName, className + "SaveReqVO");
        }
        if (template.contains("sub-do.java.vm") && StringUtils.equals(GenConstants.TPL_SUB, genTable.getTplCategory()))
        {
            fileName = StringUtils.format("{}/domain/{}.java", javaPath, genTable.getSubTable().getClassName());
        }
        else if (template.contains("mapper.java.vm"))
        {
            fileName = StringUtils.format("{}/dal/mapper/{}/{}Mapper.java", javaPath, moduleName, className);
        }
        else if (template.contains("service.java.vm"))
        {
            fileName = StringUtils.format("{}/service/{}/I{}Service.java", javaPath, moduleName, className);
        }
        else if (template.contains("serviceImpl.java.vm"))
        {
            fileName = StringUtils.format("{}/service/{}/impl/{}ServiceImpl.java", javaPath, moduleName, className);
        }
        else if (template.contains("controller.java.vm"))
        {
            fileName = StringUtils.format("{}/controller/admin/{}/{}Controller.java", javaPath, moduleName, className);
        }
        else if (template.contains("mapper.xml.vm"))
        {
            fileName = StringUtils.format("{}/{}Mapper.xml", mybatisPath, className);
        }
        else if (template.contains("sql.vm"))
        {
            fileName = businessName + "Menu.sql";
        }
        else if (template.contains("api.js.vm"))
        {
            fileName = StringUtils.format("{}/api/{}/{}/{}.js", vuePath, javaPath.substring(javaPath.lastIndexOf("/") + 1), moduleName, businessName);
        }
        else if (template.contains("index.vue.vm"))
        {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, javaPath.substring(javaPath.lastIndexOf("/") + 1), moduleName);
        }
        else if (template.contains("index-tree.vue.vm"))
        {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, javaPath.substring(javaPath.lastIndexOf("/") + 1), moduleName);
        }
        else if (template.contains("single-selection.vue.vm"))
        {
            fileName = StringUtils.format("{}/views/{}/{}/selection/{}Single.vue", vuePath, javaPath.substring(javaPath.lastIndexOf("/") + 1), moduleName, businessName);
        }
        else if (template.contains("multiple-selection.vue.vm"))
        {
            fileName = StringUtils.format("{}/views/{}/{}/selection/{}Multiple.vue", vuePath, javaPath.substring(javaPath.lastIndexOf("/") + 1), moduleName, businessName);
        }
        else if (template.contains("complex-detail.vue.vm"))
        {
            fileName = StringUtils.format("{}/views/{}/{}/detail/index.vue", vuePath, javaPath.substring(javaPath.lastIndexOf("/") + 1), moduleName);
        }
        else if (template.contains("componentOne.vue.vm"))
        {
            fileName = StringUtils.format("{}/views/{}/{}/detail/componentOne.vue", vuePath, javaPath.substring(javaPath.lastIndexOf("/") + 1), moduleName);
        }
        else if (template.contains("componentTwo.vue.vm"))
        {
            fileName = StringUtils.format("{}/views/{}/{}/detail/componentTwo.vue", vuePath, javaPath.substring(javaPath.lastIndexOf("/") + 1), moduleName);
        }
        else if (template.contains("vm/i18n/messages.properties.vm"))
        {
            fileName = StringUtils.format("{}/src/main/resources/i18n/{}-messages.properties", module1, moduleName);
        }
        else if (template.contains("vm/i18n/messages_zh_CN.properties.vm"))
        {
            fileName = StringUtils.format("{}/src/main/resources/i18n/{}-messages_zh_CN.properties", module1, moduleName);
        }
        else if (template.contains("vm/i18n/messages_en_US.properties.vm"))
        {
            fileName = StringUtils.format("{}/src/main/resources/i18n/{}-messages_en_US.properties", module1, moduleName);
        }
        else if (template.contains("vm/i18n/messages_ja_JP.properties.vm"))
        {
            fileName = StringUtils.format("{}/src/main/resources/i18n/{}-messages_ja_JP.properties", module1, moduleName);
        }
        else if (template.contains("vm/i18n/zh_CN.js.vm"))
        {
           // fileName = StringUtils.format("{}/src/main/resources/i18n/{}-zh_CN.js", module1, moduleName);
            fileName = StringUtils.format("{}/locales/zh-CN/{}/{}-zh_CN.js", vuePath, moduleName,businessName);
        }
        else if (template.contains("vm/i18n/en_US.js.vm"))
        {
            //fileName = StringUtils.format("{}/src/main/resources/i18n/{}-en_US.js", module1, moduleName);
            fileName = StringUtils.format("{}/locales/en-US/{}/{}-en_US.js", vuePath, moduleName,businessName);
        }
        else if (template.contains("vm/i18n/ja_JP.js.vm"))
        {
            //fileName = StringUtils.format("{}/src/main/resources/i18n/{}-ja_JP.js", module1, moduleName);
            fileName = StringUtils.format("{}/locales/ja-JP/{}/{}-ja_JP.js", vuePath, moduleName,businessName);
        }
        return fileName;
    }

    /**
     * Get package prefix
     *
     * @param packageName package name
     * @return package prefix name
     */
    public static String getPackagePrefix(String packageName)
    {
        int lastIndex = packageName.lastIndexOf(".");
        return StringUtils.substring(packageName, 0, lastIndex);
    }

    /**
     * Get import package based on column type
     *
     * @param genTable business table object
     * @return Returns the list of packages that need to be imported
     */
    public static HashSet<String> getImportList(GenTable genTable)
    {
        List<GenTableColumn> columns = genTable.getColumns();
        GenTable subGenTable = genTable.getSubTable();
        HashSet<String> importList = new HashSet<String>();
        if (StringUtils.isNotNull(subGenTable))
        {
            importList.add("java.util.List");
        }
        for (GenTableColumn column : columns)
        {
            if (!column.isSuperColumn() && GenConstants.TYPE_DATE.equals(column.getJavaType()))
            {
                importList.add("java.util.Date");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
            }
            else if (!column.isSuperColumn() && GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType()))
            {
                importList.add("java.math.BigDecimal");
            }
        }
        return importList;
    }

    /**
     * Get dictionary group based on column type
     *
     * @param genTable business table object
     * @return dictionary group
     */
    public static String getDicts(GenTable genTable)
    {
        List<GenTableColumn> columns = genTable.getColumns();
        Set<String> dicts = new HashSet<String>();
        addDicts(dicts, columns);
        if (StringUtils.isNotNull(genTable.getSubTable()))
        {
            List<GenTableColumn> subColumns = genTable.getSubTable().getColumns();
            addDicts(dicts, subColumns);
        }
        return StringUtils.join(dicts, ", ");
    }

    /**
     * Add dictionary list
     *
     * @param dicts dictionary list
     * @param columns column collection
     */
    public static void addDicts(Set<String> dicts, List<GenTableColumn> columns)
    {
        for (GenTableColumn column : columns)
        {
            if (!column.isSuperColumn() && StringUtils.isNotEmpty(column.getDictType()) && StringUtils.equalsAny(
                    column.getHtmlType(),
                    new String[] { GenConstants.HTML_SELECT, GenConstants.HTML_RADIO, GenConstants.HTML_CHECKBOX }))
            {
                dicts.add("'" + column.getDictType() + "'");
            }
        }
    }

    /**
     * Get permission prefix
     *
     * @param topModuleName subsystem name
     * @param moduleName module name in subsystem
     * @return return permission prefix
     */
    public static String getPermissionPrefix(String topModuleName, String moduleName, String businessName)
    {
        return StringUtils.format("{}:{}:{}", topModuleName, moduleName, businessName);
    }

    /**
     * Get the upper-level menu ID field
     *
     * @param paramsObj generates other options
     * @return Parent menu ID field
     */
    public static String getParentMenuId(JSONObject paramsObj)
    {
        if (StringUtils.isNotEmpty(paramsObj) && paramsObj.containsKey(GenConstants.PARENT_MENU_ID)
                && StringUtils.isNotEmpty(paramsObj.getString(GenConstants.PARENT_MENU_ID)))
        {
            return paramsObj.getString(GenConstants.PARENT_MENU_ID);
        }
        return DEFAULT_PARENT_MENU_ID;
    }

    /**
     * Get tree code
     *
     * @param paramsObj generates other options
     * @return tree code
     */
    public static String getTreecode(JSONObject paramsObj)
    {
        if (paramsObj.containsKey(GenConstants.TREE_CODE))
        {
            return StringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_CODE));
        }
        return StringUtils.EMPTY;
    }

    /**
     * Get parent tree code
     *
     * @param paramsObj generates other options
     * @return parent tree code
     */
    public static String getTreeParentCode(JSONObject paramsObj)
    {
        if (paramsObj.containsKey(GenConstants.TREE_PARENT_CODE))
        {
            return StringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_PARENT_CODE));
        }
        return StringUtils.EMPTY;
    }

    /**
     * Get tree name
     *
     * @param paramsObj generates other options
     * @return tree name
     */
    public static String getTreeName(JSONObject paramsObj)
    {
        if (paramsObj.containsKey(GenConstants.TREE_NAME))
        {
            return StringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_NAME));
        }
        return StringUtils.EMPTY;
    }

    /**
     * Get the column on which the expand button needs to be displayed
     *
     * @param genTable business table object
     * @return Expand button column number
     */
    public static int getExpandColumn(GenTable genTable)
    {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSON.parseObject(options);
        String treeName = paramsObj.getString(GenConstants.TREE_NAME);
        int num = 0;
        for (GenTableColumn column : genTable.getColumns())
        {
            if (column.isList())
            {
                num++;
                String columnName = column.getColumnName();
                if (columnName.equals(treeName))
                {
                    break;
                }
            }
        }
        return num;
    }
}
