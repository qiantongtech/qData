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
 * 模板处理工具类
 *
 * @author qdata
 */
public class VelocityUtils
{
    /** 项目空间路径 */
    private static final String PROJECT_PATH = "main/java";

    /** mybatis空间路径 */
    private static final String MYBATIS_PATH = "main/resources/mapper";

    /** 默认上级菜单，系统工具 */
    private static final String DEFAULT_PARENT_MENU_ID = "3";

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
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
        // 顶级模块名称如：system
        velocityContext.put("topModule", packageName.substring(packageName.lastIndexOf(".") + 1));
        velocityContext.put("controllerPrefix", packageName.replaceFirst("^.*?\\.module\\.", "").replace(".", "/"));
        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("datetime", DateUtils.getDate());
        velocityContext.put("pkColumn", genTable.getPkColumn());
        velocityContext.put("importList", getImportList(genTable));
        // 包模块名称如：example
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
     * 设置字典模板变量信息
     *
     * @return 模板列表
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
     * 获取模板信息
     * @param tplCategory 生成的模板
     * @param tplWebType 前端类型
     * @return 模板列表
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
     * 获取模板信息
     * @return 枚举类模板列表
     */
    public static List<String> getTemplateListForDict()
    {

        List<String> templates = new ArrayList<>();
        templates.add("vm/java/dict.java.vm");

        return templates;
    }

    public static String getEnumFileName(String dictType){
        // 文件名称
        String fileName = "";
        fileName = StringUtils.format("{}Enum.java",  StringUtils.convertToCamelCase(dictType));
        return fileName;
    }


    /**
     * 获取文件名
     */
    public static String getFileName(String template, GenTable genTable)
    {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();

        String javaPath = PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");

        String module = packageName.replaceFirst("^.*?\\.module\\.", "").replaceFirst("^[^.]+\\.", "").replace(".", "/");
        String mybatisPath = MYBATIS_PATH + "/" + module + "/" + moduleName;
        String vuePath = "vue";

        if (template.contains("do.java.vm"))
        {
            fileName = StringUtils.format("{}/biz/dal/dataobject/{}/{}.java", javaPath, moduleName, className + "DO");
        }
        if (template.contains("reqDTO.java.vm"))
        {
            fileName = StringUtils.format("{}/api/{}/dto/{}.java", javaPath, moduleName, className + "ReqDTO");
        }
        if (template.contains("respDTO.java.vm"))
        {
            fileName = StringUtils.format("{}/api/{}/dto/{}.java", javaPath, moduleName, className + "RespDTO");
        }
        if (template.contains("respVO.java.vm"))
        {
            fileName = StringUtils.format("{}/biz/controller/admin/{}/vo/{}.java", javaPath, moduleName, className + "RespVO");
        }
        if (template.contains("pageReqVO.java.vm"))
        {
            fileName = StringUtils.format("{}/biz/controller/admin/{}/vo/{}.java", javaPath, moduleName, className + "PageReqVO");
        }
        if (template.contains("convert.java.vm"))
        {
            fileName = StringUtils.format("{}/biz/convert/{}/{}.java", javaPath, moduleName, className + "Convert");
        }
        if (template.contains("saveReqVO.java.vm"))
        {
            fileName = StringUtils.format("{}/biz/controller/admin/{}/vo/{}.java", javaPath, moduleName, className + "SaveReqVO");
        }
        if (template.contains("sub-do.java.vm") && StringUtils.equals(GenConstants.TPL_SUB, genTable.getTplCategory()))
        {
            fileName = StringUtils.format("{}/domain/{}.java", javaPath, genTable.getSubTable().getClassName());
        }
        else if (template.contains("mapper.java.vm"))
        {
            fileName = StringUtils.format("{}/biz/dal/mapper/{}/{}Mapper.java", javaPath, moduleName, className);
        }
        else if (template.contains("service.java.vm"))
        {
            fileName = StringUtils.format("{}/biz/service/{}/I{}Service.java", javaPath, moduleName, className);
        }
        else if (template.contains("serviceImpl.java.vm"))
        {
            fileName = StringUtils.format("{}/biz/service/{}/impl/{}ServiceImpl.java", javaPath, moduleName, className);
        }
        else if (template.contains("controller.java.vm"))
        {
            fileName = StringUtils.format("{}/biz/controller/admin/{}/{}Controller.java", javaPath, moduleName, className);
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
        return fileName;
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName)
    {
        int lastIndex = packageName.lastIndexOf(".");
        return StringUtils.substring(packageName, 0, lastIndex);
    }

    /**
     * 根据列类型获取导入包
     *
     * @param genTable 业务表对象
     * @return 返回需要导入的包列表
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
     * 根据列类型获取字典组
     *
     * @param genTable 业务表对象
     * @return 返回字典组
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
     * 添加字典列表
     *
     * @param dicts 字典列表
     * @param columns 列集合
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
     * 获取权限前缀
     *
     * @param topModuleName 子系统名称
     * @param moduleName 子系统中的模块名称
     * @return 返回权限前缀
     */
    public static String getPermissionPrefix(String topModuleName, String moduleName, String businessName)
    {
        return StringUtils.format("{}:{}:{}", topModuleName, moduleName, businessName);
    }

    /**
     * 获取上级菜单ID字段
     *
     * @param paramsObj 生成其他选项
     * @return 上级菜单ID字段
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
     * 获取树编码
     *
     * @param paramsObj 生成其他选项
     * @return 树编码
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
     * 获取树父编码
     *
     * @param paramsObj 生成其他选项
     * @return 树父编码
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
     * 获取树名称
     *
     * @param paramsObj 生成其他选项
     * @return 树名称
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
     * 获取需要在哪一列上面显示展开按钮
     *
     * @param genTable 业务表对象
     * @return 展开按钮列序号
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
