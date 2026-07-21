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

package tech.qiantong.qdata.generator.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.constant.GenConstants;
import tech.qiantong.qdata.common.core.domain.entity.SysDictType;
import tech.qiantong.qdata.common.core.text.CharsetKit;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.generator.domain.GenTable;
import tech.qiantong.qdata.generator.domain.GenTableColumn;
import tech.qiantong.qdata.generator.mapper.GenTableColumnMapper;
import tech.qiantong.qdata.generator.mapper.GenTableMapper;
import tech.qiantong.qdata.generator.util.GenUtils;
import tech.qiantong.qdata.generator.util.VelocityInitializer;
import tech.qiantong.qdata.generator.util.VelocityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Business service layer implementation
 *
 * @author qdata
 */
@Service
public class GenTableServiceImpl implements IGenTableService
{
    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Autowired
    private GenTableMapper genTableMapper;

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    /**
     * Query business information
     *
     * @param id business ID
     * @return business information
     */
    @Override
    public GenTable selectGenTableById(Long id)
    {
        GenTable genTable = genTableMapper.selectGenTableById(id);
        setTableFromOptions(genTable);
        return genTable;
    }

    /**
     * Query business list
     *
     * @param genTable business information
     * @return business collection
     */
    @Override
    public List<GenTable> selectGenTableList(GenTable genTable)
    {
        return genTableMapper.selectGenTableList(genTable);
    }

    /**
     * Query database list
     *
     * @param genTable business information
     * @return database table collection
     */
    @Override
    public List<GenTable> selectDbTableList(GenTable genTable)
    {
        return genTableMapper.selectDbTableList(genTable);
    }

    /**
     * Query database list
     *
     * @param tableNames table name group
     * @return database table collection
     */
    @Override
    public List<GenTable> selectDbTableListByNames(String[] tableNames)
    {
        return genTableMapper.selectDbTableListByNames(tableNames);
    }

    /**
     * Query all table information
     *
     * @return table information collection
     */
    @Override
    public List<GenTable> selectGenTableAll()
    {
        return genTableMapper.selectGenTableAll();
    }

    /**
     * Modify business
     *
     * @param genTable business information
     * @return result
     */
    @Override
    @Transactional
    public void updateGenTable(GenTable genTable)
    {
        String options = JSON.toJSONString(genTable.getParams());
        genTable.setOptions(options);
        int row = genTableMapper.updateGenTable(genTable);
        if (row > 0)
        {
            for (GenTableColumn cenTableColumn : genTable.getColumns())
            {
                genTableColumnMapper.updateGenTableColumn(cenTableColumn);
            }
        }
    }

    /**
     * Delete business object
     *
     * @param tableIds Data ID to be deleted
     * @return result
     */
    @Override
    @Transactional
    public void deleteGenTableByIds(Long[] tableIds)
    {
        genTableMapper.deleteGenTableByIds(tableIds);
        genTableColumnMapper.deleteGenTableColumnByIds(tableIds);
    }

    /**
     * Create table
     *
     * @param sql create table statement
     * @return result
     */
    @Override
    public boolean createTable(String sql)
    {
        return genTableMapper.createTable(sql) == 0;
    }

    /**
     * Import table structure
     *
     * @param tableList import table list
     */
    @Override
    @Transactional
    public void importGenTable(List<GenTable> tableList, String operName)
    {
        try
        {
            for (GenTable table : tableList)
            {
                String tableName = table.getTableName();
                GenUtils.initTable(table, operName);
                int row = genTableMapper.insertGenTable(table);
                if (row > 0)
                {
                    // Save column information
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
                    for (GenTableColumn column : genTableColumns)
                    {
                        // Format field names
                        String comment = column.getColumnComment();
                        if (comment != null && comment.contains(";")) {
                            comment = comment.substring(0, comment.indexOf(";"));
                        }
                        column.setColumnComment(comment);

                        GenUtils.initColumnField(column, table);
                        genTableColumnMapper.insertGenTableColumn(column);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new ServiceException("sys.error.generator.import.fail", "Import failed: {0}", e.getMessage());
        }
    }

    /**
     * Preview code
     *
     * @param tableId table number
     * @return preview data list
     */
    @Override
    public Map<String, String> previewCode(Long tableId)
    {
        Map<String, String> dataMap = new LinkedHashMap<>();
        // Query table information
        GenTable table = genTableMapper.selectGenTableById(tableId);
        // Set master and child table information
        setSubTable(table);
        // Set primary key column information
        setPkColumn(table);
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // Get template list
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
        for (String template : templates)
        {
            // Render template
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    /**
     * Generate code (download method)
     *
     * @param tableName table name
     * @return data
     */
    @Override
    public byte[] downloadCode(String tableName)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableName, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * Generate code (custom path)
     *
     * @param tableName table name
     */
    @Override
    public void generatorCode(String tableName)
    {
        // Query table information
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // Set master and child table information
        setSubTable(table);
        // Set primary key column information
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // Get template list
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
        for (String template : templates)
        {
            if (!StringUtils.containsAny(template, "sql.vm", "api.js.vm", "index.vue.vm", "index-tree.vue.vm"))
            {
                // Render template
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                try
                {
                    String path = getGenPath(table, template);
                    FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                }
                catch (IOException e)
                {
                    throw new ServiceException("sys.error.generator.template.render.fail",
                            "Failed to render template for table: {0}", table.getTableName());
                }
            }
        }
    }

    /**
     * Sync database
     *
     * @param tableName table name
     */
    @Override
    @Transactional
    public void synchDb(String tableName)
    {
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        List<GenTableColumn> tableColumns = table.getColumns();
        Map<String, GenTableColumn> tableColumnMap = tableColumns.stream().collect(Collectors.toMap(GenTableColumn::getColumnName, Function.identity()));

        List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
        if (StringUtils.isEmpty(dbTableColumns))
        {
            throw new ServiceException("sys.error.generator.sync.source.table.notfound",
                    "Failed to synchronize data because the source table structure does not exist");
        }
        List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

        dbTableColumns.forEach(column -> {
            GenUtils.initColumnField(column, table);
            if (tableColumnMap.containsKey(column.getColumnName()))
            {
                GenTableColumn prevColumn = tableColumnMap.get(column.getColumnName());
                column.setColumnId(prevColumn.getColumnId());
                if (column.isList())
                {
                    // If it is a list, continue to retain the query mode/dictionary type option
                    column.setDictType(prevColumn.getDictType());
                    column.setQueryType(prevColumn.getQueryType());
                }
                if (StringUtils.isNotEmpty(prevColumn.getIsRequired()) && !column.isPk()
                        && (column.isInsert() || column.isEdit())
                        && ((column.isUsableColumn()) || (!column.isSuperColumn())))
                {
                    // If it is (new/modified & non-primary key/non-ignored and parent attributes), continue to retain the required/display type options
                    column.setIsRequired(prevColumn.getIsRequired());
                    column.setHtmlType(prevColumn.getHtmlType());
                }
                genTableColumnMapper.updateGenTableColumn(column);
            }
            else
            {
                genTableColumnMapper.insertGenTableColumn(column);
            }
        });

        List<GenTableColumn> delColumns = tableColumns.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delColumns))
        {
            genTableColumnMapper.deleteGenTableColumns(delColumns);
        }
    }

    /**
     * Generate codes in batches (download method)
     *
     * @param tableNames table array
     * @return data
     */
    @Override
    public byte[] downloadCode(String[] tableNames)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames)
        {
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }


    /**
     * Generate enumeration classes in batches (download method)
     *
     * @param dictTypeList dictionary array
     * @return data
     */
    @Override
    public byte[] downloadEnums(List<SysDictType> dictTypeList)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (SysDictType SysDictType : dictTypeList)
        {
            generatorEnums(SysDictType, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * Query table information and generate code
     */
    private void generatorCode(String tableName, ZipOutputStream zip)
    {
        // Query table information
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // Set master and child table information
        setSubTable(table);
        // Set primary key column information
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // Get template list
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
        for (String template : templates)
        {
            // Render template
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try
            {
                // Add to zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            }
            catch (IOException e)
            {
                log.error(MessageUtils.messageEn("log.template.render.error", table.getTableName()), e);
            }
        }
    }


    /**
     * Query dictionary information and generate enumeration classes
     */
    private void generatorEnums(SysDictType dictType, ZipOutputStream zip)
    {

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareDictContext(dictType);

        // Get template list
        List<String> templates = VelocityUtils.getTemplateListForDict();
        for (String template : templates)
        {
            // Render template
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try
            {
                // Add to zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getEnumFileName(dictType.getDictType())));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            }
            catch (IOException e)
            {
                log.error(MessageUtils.message("log.template.render.dict.error", dictType.getDictType()), e);
            }
        }
    }

    /**
     * Modify and save parameter verification
     *
     * @param genTable business information
     */
    @Override
    public void validateEdit(GenTable genTable)
    {
        if (GenConstants.TPL_TREE.equals(genTable.getTplCategory()))
        {
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSON.parseObject(options);
            if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_CODE)))
            {
                throw new ServiceException("sys.error.generator.tree.code.empty", "Tree code field cannot be empty");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_PARENT_CODE)))
            {
                throw new ServiceException("sys.error.generator.tree.parent.code.empty",
                        "Tree parent code field cannot be empty");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_NAME)))
            {
                throw new ServiceException("sys.error.generator.tree.name.empty", "Tree name field cannot be empty");
            }
            else if (GenConstants.TPL_SUB.equals(genTable.getTplCategory()))
            {
                if (StringUtils.isEmpty(genTable.getSubTableName()))
                {
                    throw new ServiceException("sys.error.generator.subtable.name.empty",
                            "Associated subtable name cannot be empty");
                }
                else if (StringUtils.isEmpty(genTable.getSubTableFkName()))
                {
                    throw new ServiceException("sys.error.generator.subtable.foreign.key.empty",
                            "Subtable foreign key name cannot be empty");
                }
            }
        }
    }

    /**
     * Set primary key column information
     *
     * @param table business table information
     */
    public void setPkColumn(GenTable table)
    {
        for (GenTableColumn column : table.getColumns())
        {
            if (column.isPk())
            {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn()))
        {
            table.setPkColumn(table.getColumns().get(0));
        }
        if (GenConstants.TPL_SUB.equals(table.getTplCategory()))
        {
            for (GenTableColumn column : table.getSubTable().getColumns())
            {
                if (column.isPk())
                {
                    table.getSubTable().setPkColumn(column);
                    break;
                }
            }
            if (StringUtils.isNull(table.getSubTable().getPkColumn()))
            {
                table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
            }
        }
    }

    /**
     * Set master and child table information
     *
     * @param table business table information
     */
    public void setSubTable(GenTable table)
    {
        String subTableName = table.getSubTableName();
        if (StringUtils.isNotEmpty(subTableName))
        {
            table.setSubTable(genTableMapper.selectGenTableByName(subTableName));
        }
    }

    /**
     * Set code generation additional option values
     *
     * @param genTable generated object after setting
     */
    public void setTableFromOptions(GenTable genTable)
    {
        JSONObject paramsObj = JSON.parseObject(genTable.getOptions());
        if (StringUtils.isNotNull(paramsObj))
        {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            String parentMenuId = paramsObj.getString(GenConstants.PARENT_MENU_ID);
            String parentMenuName = paramsObj.getString(GenConstants.PARENT_MENU_NAME);

            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
            genTable.setParentMenuId(parentMenuId);
            genTable.setParentMenuName(parentMenuName);
        }
    }

    /**
     * Get code generation address
     *
     * @param table business table information
     * @param template template file path
     * @return generate address
     */
    public static String getGenPath(GenTable table, String template)
    {
        String genPath = table.getGenPath();
        if (StringUtils.equals(genPath, "/"))
        {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
        }
        return genPath + File.separator + VelocityUtils.getFileName(template, table);
    }
}
