/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.ai.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.qiantong.qdata.ai.core.enums.ChatMessageTypeEnum;
import tech.qiantong.qdata.ai.core.enums.ReplyTypeEnum;
import tech.qiantong.qdata.ai.core.prompt.params.DimensionTable;
import tech.qiantong.qdata.ai.core.prompt.params.FactDimensionRelation;
import tech.qiantong.qdata.ai.core.prompt.params.FactTable;
import tech.qiantong.qdata.ai.core.prompt.StatisticsDataMsgPromptBuilder;
import tech.qiantong.qdata.ai.core.prompt.StatisticsPromptBuilder;
import tech.qiantong.qdata.ai.core.service.IChatMessageService;
import tech.qiantong.qdata.ai.core.service.IChatModelService;
import tech.qiantong.qdata.ai.core.utils.AiUtils;
import tech.qiantong.qdata.ai.core.utils.LlmUtils;
import tech.qiantong.qdata.ai.core.vo.ChatMessageExportDetailDataReqVO;
import tech.qiantong.qdata.ai.core.vo.ChatMessageSendReqVO;
import tech.qiantong.qdata.ai.core.vo.ChatMessageSendRespVO;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageSaveReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatConversationDO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatMessageDO;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatConversationService;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatMessageService;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-07 14:18
 **/
@Slf4j
@Service
public class ChatMessageServiceImpl implements IChatMessageService {

    private static final int MAX_CONTEXT_MESSAGES = 10;
    private static final int TITLE_MAX_LENGTH = 10;
    private static final String USER_MESSAGE_TYPE = "1";
    private static final String AI_MESSAGE_TYPE = "2";
    private static final String CONTEXT_FLAG_ENABLED = "1";
    private static final String CONTEXT_FLAG_DISABLED = "0";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final int EXCEL_COLUMN_WIDTH = 25 * 256;
    private static final short HEADER_ROW_HEIGHT = 2 * 200;
    private static final short DATA_ROW_HEIGHT = 4 * 200;
    private static final short HEADER_FONT_SIZE = 24;
    private static final short CONTENT_FONT_SIZE = 11;
    private static final String FONT_NAME = "宋体";
    private static final int MAX_DATA_ROWS_FOR_ANALYSIS = 100;

    @Resource
    private IChatModelService chatModelService;

    @Resource
    private IAiChatMessageService aiChatMessageService;

    @Resource
    private IAiChatConversationService aiChatConversationService;

    @Resource
    private StatisticsPromptBuilder promptBuilder;

    @Resource
    private StatisticsDataMsgPromptBuilder statisticsDataMsgPromptBuilder;

    @Resource
    private IDaDatasourceApiService daDatasourceApiService;

    @Resource
    private DataSourceFactory dataSourceFactory;


    @Override
    public Flux<ChatMessageSendRespVO> sendChatMessageStream(ChatMessageSendReqVO sendReqVO, Long userId) {
        //用户问题内容
        JSONObject contentObj = JSONObject.parseObject(sendReqVO.getContent());

        // 获取对话信息
        AiChatConversationDO conversation = aiChatConversationService.getAiChatConversationById(sendReqVO.getConversationId());
        if (conversation == null) {
            return Flux.error(new ServiceException("对话不存在"));
        }
        if (!conversation.getJoinConditionMatchFlag()) {
            return Flux.error(new ServiceException("请手动配置关联信息"));
        }

        //获取数据源信息
        DaDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(conversation.getDatasourceId());
        if (datasource == null) {
            return Flux.error(new ServiceException("数据源不存在"));
        }
        String datasourceType = datasource.getDatasourceType();
        JSONObject datasourceConfig = JSONObject.parseObject(datasource.getDatasourceConfig());

        // 获取对话历史消息列表，用于上下文关联
        List<AiChatMessageDO> messageHistory = aiChatMessageService.getChatMessageListByConversationId(sendReqVO.getConversationId());

        // 如果是新对话，使用用户消息的前10个字符作为标题
        if (messageHistory.isEmpty()) {
            conversation.setTitle(StringUtils.substring(contentObj.getString("msg"), 0, TITLE_MAX_LENGTH));
            aiChatConversationService.updateById(conversation);
        }

        // 保存用户消息到数据库，返回保存后的消息信息
        AiChatMessageSaveReqVO userMessage = saveUserMessage(sendReqVO, userId);

        // 准备AI回复的消息对象
        AiChatMessageSaveReqVO aiMessageTemplate = new AiChatMessageSaveReqVO();
        aiMessageTemplate.setReplyId(userMessage.getId());      // 关联的用户消息ID
        aiMessageTemplate.setReplyType(sendReqVO.getReplyType());
        aiMessageTemplate.setContextFlag(sendReqVO.getContextFlag() ? CONTEXT_FLAG_ENABLED : CONTEXT_FLAG_DISABLED);    // 是否使用上下文
        aiMessageTemplate.setConversationId(sendReqVO.getConversationId()); // 对话ID

        // 第二步：本地LLM调用（核心处理逻辑）
        // 1. 获取LLM模型客户端 - 这里使用DeepSeek平台
        ChatModel chatModel = chatModelService.getChatModel(sendReqVO.getModelId());

        // 2. 构建消息列表（遵循OpenAI的message格式）
        List<Message> contextMessages = new ArrayList<>();

        // 3. 添加上下文历史消息（如果有上下文标志）
        if (Boolean.TRUE.equals(sendReqVO.getContextFlag()) && !messageHistory.isEmpty()) {
            // 获取最近的历史消息（例如最近的10组对话）
            List<AiChatMessageDO> filteredMessages = filterContextMessages(messageHistory, MAX_CONTEXT_MESSAGES, sendReqVO.getConversationId());
            for (AiChatMessageDO msg : filteredMessages) {
                if (USER_MESSAGE_TYPE.equals(msg.getType())) {
                    // 用户消息
                    contextMessages.add(new UserMessage(msg.getContent()));
                } else {
                    // AI回复
                    contextMessages.add(new UserMessage("助手回复: " + msg.getContent()));
                }
            }
        }

        // 4. 构建提示词
        //4.1 获取事实表
        FactTable factTable = genFactTable(conversation, datasourceConfig);

        //4.2 获取维度列表
        List<DimensionTable> dimensionTables = genDimensionTableList(conversation, datasourceConfig);

        //4.3 组装关联条件数据
        List<FactDimensionRelation> relations = genRelations(conversation);

        //4.4 构建Prompt
        String promptStr = promptBuilder.buildPrompt(
                ReplyTypeEnum.getByType(sendReqVO.getReplyType()),
                factTable,
                dimensionTables,
                relations,
                datasourceType
        );

        List<Message> promptMessages = new ArrayList<>();
        //添加提示词
        promptMessages.add(new SystemMessage(promptStr));
        //添加用户需求
        promptMessages.add(new UserMessage("\n【统计需求】\n" + contentObj.getString("msg")));
        //添加上下文
        promptMessages.addAll(contextMessages);
        Prompt prompt = new Prompt(promptMessages);

        // 第三步：处理数据
        StringBuilder contentBuffer = new StringBuilder();
        // 流式调用LLM（实时获取AI回复）
        return LlmUtils.streamLlmResponse(chatModel, prompt)
                .map(AiUtils::getChatResponseContent) // 提取响应内容
                .filter(content -> content != null && !content.isEmpty()) // 过滤空内容
                .map(content -> {
                    // 收集AI回复的每个片段
                    contentBuffer.append(content);

                    // 发送流式响应到前端（模拟message事件）
                    return messageSent(content, userMessage);
                })
                .concatWith(Mono.defer(() -> {
                    JSONObject content = JSONObject.parseObject(contentBuffer.toString());
                    if (!content.getBoolean("success")) {
                        content.put("msg", "对话异常: " + content.getString("msg"));
                        content.put("type", ChatMessageTypeEnum.ERROR.getType());
                    } else {
                        //判断是否是智能图表
                        if (ReplyTypeEnum.CHART.getType().equals(sendReqVO.getReplyType())) {
                            content.put("type", ChatMessageTypeEnum.CHAT.getType());
                            String sql = content.getString("sql");
                            //维度数据字段
                            String dimension = content.getString("dimension");
                            //度量数据字段
                            List<String> measures = JSONArray.parseArray(content.getString("measure"), String.class);

                            //创建查询器
                            DbQueryProperty dbQueryProperty = new DbQueryProperty(
                                    datasource.getDatasourceType(),
                                    datasource.getIp(),
                                    datasource.getPort(),
                                    datasource.getDatasourceConfig()
                            );
                            DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);

                            //校验sql语法是否正确
                            verifySql(sql, dbQuery);

                            List<Map<String, Object>> dataList = dbQuery.queryList(sql);
                            JSONObject detailData = new JSONObject();
                            detailData.put("label", content.getJSONArray("selectColumnDescription"));
                            detailData.put("list", dataList);
                            content.put("detailData", detailData);

                            JSONObject chatData = new JSONObject();
                            chatData.put("xAxisData", dataList.stream().map(item -> item.get(dimension)).collect(Collectors.toList()));
                            JSONArray yAxisDataArr = new JSONArray();
                            measures.forEach(measure -> {
                                yAxisDataArr.add(dataList.stream().map(item -> item.get(measure)).collect(Collectors.toList()));
                            });
                            chatData.put("yAxisDataArr", yAxisDataArr);
                            content.put("chatData", chatData);

                            //构建统计数据分析提示词
                            String statisticsDataPrompt = statisticsDataMsgPromptBuilder.buildPrompt(
                                    content.getList("selectColumn", String.class),
                                    content.getList("selectColumnDescription", String.class),
                                    dimension,
                                    measures,
                                    content.getString("timeGrain"),
                                    dataList,
                                    MAX_DATA_ROWS_FOR_ANALYSIS
                            );
                            String statisticsDataRes = chatModel.call(statisticsDataPrompt);
                            JSONObject statisticsDataObj = JSONObject.parseObject(statisticsDataRes);
                            content.put("msg", statisticsDataObj.getString("summary"));
                        }

                        // 流式调用完成后，保存完整的机器人消息
                        aiMessageTemplate.setContent(content.toString()); // AI完整回复
                    }
                    // 保存AI回复到数据库
                    AiChatMessageSaveReqVO savedMessage = saveRobotMessage(aiMessageTemplate, userId);

                    return Mono.just(messageEnd(
                            savedMessage.getId(),  // 保存的AI消息ID
                            content.toString(), // AI完整回复内容
                            sendReqVO.getReplyType()
                    ));
                }))
                .publishOn(Schedulers.boundedElastic())
                .doOnError(error -> {
                    error.printStackTrace();
                    log.error("LLM调用失败", error);
                    // 保存错误信息到数据库
                    JSONObject errorContent = new JSONObject();
                    errorContent.put("msg", "对话异常: " + error.getMessage());
                    errorContent.put("type", ChatMessageTypeEnum.ERROR.getType());
                    aiMessageTemplate.setContent(errorContent.toString());
                    saveRobotMessage(aiMessageTemplate, userId);
                });
    }

    @Override
    public void exportDetailData(HttpServletResponse response, ChatMessageExportDetailDataReqVO exportDetailDataReqVO) {
        //获取消息数据
        AiChatMessageDO message = aiChatMessageService.getById(exportDetailDataReqVO.getMessageId());
        if (message == null) {
            throw new ServiceException("消息不存在");
        }
        if (StringUtils.isBlank(message.getReplyType()) || !ReplyTypeEnum.CHART.getType().equals(message.getReplyType())) {
            throw new ServiceException("此消息不支持导出");
        }
        if (StringUtils.isBlank(message.getContent())) {
            throw new ServiceException("消息内容为空");
        }

        JSONObject content = JSONObject.parseObject(message.getContent());
        JSONObject detailData = content.getJSONObject("detailData");
        if (detailData == null) {
            throw new ServiceException("消息内容错误");
        }

        List<String> label = detailData.getList("label", String.class);
        List<JSONObject> dataList = detailData.getList("list", JSONObject.class);
        if (dataList == null || dataList.isEmpty()) {
            throw new ServiceException("消息内容错误");
        }

        //导出
        exportByList(response, label, dataList, "明细列表");
    }

    List<FactDimensionRelation> genRelations(AiChatConversationDO conversation) {
        JSONArray associations = JSONArray.parse(conversation.getAssociations());

        List<FactDimensionRelation> relations = new ArrayList<>();
        for (Object obj : associations) {
            JSONObject association = (JSONObject) obj;
            relations.add(FactDimensionRelation.builder()
                    .dimensionTable(association.getString("dimensionTable"))
                    .factColumn(association.getString("factColumnName"))
                    .dimensionColumn(association.getString("dimensionColumnName"))
                    .build());
        }
        return relations;
    }

    List<DimensionTable> genDimensionTableList(AiChatConversationDO conversation, JSONObject datasourceConfig) {
        List<DimensionTable> dimensions = new ArrayList<>();
        if (StringUtils.isBlank(conversation.getDimensionTable())) {
            return dimensions;
        }

        JSONArray dimensionTables = JSONArray.parse(conversation.getDimensionTable());
        if (dimensionTables == null || dimensionTables.isEmpty()) {
            return dimensions;
        }

        for (Object obj : dimensionTables) {
            JSONObject dimensionTable = (JSONObject) obj;
            //维度表名称
            String tableName = dimensionTable.getString("tableName");
            //维度表注释
            String tableComment = dimensionTable.getString("tableComment");

            //获取维度表字段
            List<DbColumn> dbColumns = daDatasourceApiService.getDbTableColumns(
                    conversation.getDatasourceId(), tableName);

            //生成维度表实体列表
            List<DimensionTable.Column> columns = new ArrayList<>();
            //维度表主键
            List<String> primaryKeys = new ArrayList<>();

            for (DbColumn dbColumn : dbColumns) {
                if (dbColumn.getColKey()) {
                    primaryKeys.add(dbColumn.getColName());
                }
                columns.add(DimensionTable.Column.builder()
                        .name(dbColumn.getColName())
                        .type(dbColumn.getDataType())
                        .description(dbColumn.getColComment())
                        .build());
            }

            dimensions.add(DimensionTable.builder()
                    .tableName(tableName)
                    .databaseName(datasourceConfig.getString("dbname"))
                    .schemaName(datasourceConfig.getString("sid"))
                    .alias(StrUtil.toCamelCase(tableName))
                    .description(tableComment)
                    .columns(columns)
                    .primaryKeys(primaryKeys)
                    .build());
        }

        return dimensions;
    }

    FactTable genFactTable(AiChatConversationDO conversation, JSONObject datasourceConfig) {
        //事实表名称
        String factTableName = conversation.getFactTableName();
        //事实表注释
        String factTableComment = conversation.getFactTableComment();

        //获取事实表字段
        List<DbColumn> dbColumns = daDatasourceApiService.getDbTableColumns(
                conversation.getDatasourceId(), factTableName);

        //组装事实表实体
        List<FactTable.Column> columns = new ArrayList<>();
        //事实表主键
        List<String> primaryKeys = new ArrayList<>();

        for (DbColumn dbColumn : dbColumns) {
            if (dbColumn.getColKey()) {
                primaryKeys.add(dbColumn.getColName());
            }
            columns.add(FactTable.Column.builder()
                    .name(dbColumn.getColName())
                    .type(dbColumn.getDataType())
                    .description(dbColumn.getColComment())
                    .build());
        }

        return FactTable.builder()
                .tableName(factTableName)
                .databaseName(datasourceConfig.getString("dbname"))
                .schemaName(datasourceConfig.getString("sid"))
                .alias(StrUtil.toCamelCase(factTableName))
                .description(factTableComment)
                .columns(columns)
                .primaryKeys(primaryKeys)
                .build();
    }

    /**
     * 校验sql语法
     *
     * @param sql
     * @param dbQuery
     * @throws ServiceException sql语法错误或非查询sql
     */
    void verifySql(String sql, DbQuery dbQuery) {
        try (Connection connection = dbQuery.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // 使用 PreparedStatement 预编译，不实际执行
            // 获取元数据，如果不是查询语句，某些驱动在此处或执行时会返回 null 或报错
            ResultSetMetaData metaData = ps.getMetaData();
            if (metaData == null) {
                throw new ServiceException("该 SQL 不是查询语句或语法有误");
            }
        } catch (SQLException e) {
            throw new ServiceException("SQL 语法错误: " + e.getMessage());
        }
    }

    @SneakyThrows
    private static void exportByList(HttpServletResponse response, List<String> labelList, List<JSONObject> dataList, String sheetName) {
        if (dataList == null || dataList.isEmpty()) {
            throw new ServiceException("暂无表单信息");
        }

        // 获取第一行数据的所有列名作为 order
        Map<String, Object> firstRow = dataList.get(0);
        // 使用 Set 可以确保列名唯一性
        List<String> order = new ArrayList<>(firstRow.keySet());

        //1.创建工作博
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            //头部字段字体
            XSSFFont headFont = workbook.createFont();
            //字体高度
            headFont.setFontHeightInPoints(HEADER_FONT_SIZE);
            //字体
            headFont.setFontName(FONT_NAME);
            headFont.setBold(true);

            // 设置单元格类型
            XSSFCellStyle headCellStyle = workbook.createCellStyle();
            headCellStyle.setFont(headFont);
            //水平布局：居中
            headCellStyle.setAlignment(HorizontalAlignment.CENTER);
            //垂直居中
            headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headCellStyle.setWrapText(true);

            //标注划字体
            XSSFFont font = workbook.createFont();
            //字体高度
            font.setFontHeightInPoints(CONTENT_FONT_SIZE);
            //字体
            font.setFontName(FONT_NAME);

            //列样式
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            //水平布局：居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setWrapText(true);

            //2。创建工作表
            XSSFSheet sheet = workbook.createSheet(sheetName);

            //冻结第一行
            sheet.createFreezePane(0, 1, 0, 1);

            //3。创建字段行
            XSSFRow headerRow = sheet.createRow(0);
            headerRow.setHeight(HEADER_ROW_HEIGHT);

            for (int i = 0; i < labelList.size(); i++) {
                //设置默认宽度
                sheet.setColumnWidth(i, EXCEL_COLUMN_WIDTH);
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(labelList.get(i));
            }

            //4。数据行
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> map = dataList.get(i);
                //数据行
                XSSFRow dataRow = sheet.createRow(i + 1);
                dataRow.setHeight(DATA_ROW_HEIGHT);

                int columnIndex = 0;
                for (String key : order) {
                    Object valueObj = map.get(key);
                    String value = "";
                    if (valueObj instanceof Date) {
                        // 如果是日期类型，转换为固定格式的字符串
                        value = dateFormat.format((Date) valueObj);
                    } else {
                        value = String.valueOf(valueObj);
                    }

                    XSSFCell cell = dataRow.createCell(columnIndex);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(value);
                    columnIndex++;
                }
            }

            if (response != null) {
                //5.输出流 输出
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    workbook.write(baos);
                    baos.flush();

                    byte[] data = baos.toByteArray();
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(sheetName + ".xlsx", "UTF-8"));
                    response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setContentLength(data.length);
                    response.getOutputStream().write(data);
                    response.getOutputStream().flush();
                }
            }
        }
    }

    /**
     * 消息结束，引用文档片段存储
     */
    public ChatMessageSendRespVO messageEnd(Long saveId, String content, String replyType) {
        // 发送完整的回答数据
        ChatMessageSendRespVO sendRespVO = new ChatMessageSendRespVO();
        ChatMessageSendRespVO.Message message = new ChatMessageSendRespVO.Message();
        message.setId(saveId);
        message.setContent(content);
        message.setType(AI_MESSAGE_TYPE);
        message.setReplyType(replyType);
        // 接收消息
        sendRespVO.setReceive(message);
        return sendRespVO;
    }

    /**
     * 流式数据处理
     *
     * @param content
     * @param chatMessageSaveReqVO
     * @return
     */
    public ChatMessageSendRespVO messageSent(String content, AiChatMessageSaveReqVO chatMessageSaveReqVO) {
        ChatMessageSendRespVO sendRespVO = new ChatMessageSendRespVO();
        // 机器人回复消息
        ChatMessageSendRespVO.Message message = new ChatMessageSendRespVO.Message();
        message.setType(AI_MESSAGE_TYPE);
        message.setContent(content);
        message.setCreateTime(DateUtils.getNowDate());
        message.setReplyType(chatMessageSaveReqVO.getReplyType());
        sendRespVO.setReceive(message); // 接收消息

        // 用户发送消息
        ChatMessageSendRespVO.Message messageUser = new ChatMessageSendRespVO.Message();
        messageUser.setType(USER_MESSAGE_TYPE);
        messageUser.setContent(chatMessageSaveReqVO.getContent());
        messageUser.setCreateTime(chatMessageSaveReqVO.getCreateTime());
        messageUser.setId(chatMessageSaveReqVO.getId());
        sendRespVO.setSend(messageUser); // 发送消息

        return sendRespVO;
    }

    /**
     * 过滤上下文消息
     *
     * @param messages               所有历史消息列表
     * @param maxContexts            最大上下文数量（对话轮次）
     * @param requiredConversationId 必需的对话ID，确保只获取当前对话的消息
     * @return List<AppChatMessageDO> 过滤后的上下文消息列表，按时间顺序排列
     */
    private List<AiChatMessageDO> filterContextMessages(List<AiChatMessageDO> messages, int maxContexts, Long requiredConversationId) {

        if (messages == null || messages.isEmpty()) {
            return Collections.emptyList();
        }

        List<AiChatMessageDO> contextMessages = new ArrayList<>();
        int contextCount = 0;

        // 从后往前遍历，找到最近的对话对（用户消息 + AI回复）
        for (int i = messages.size() - 1; i >= 0 && contextCount < maxContexts; i--) {
            AiChatMessageDO assistantMsg = messages.get(i);

            // 关键：确保消息属于指定的对话
            if (!requiredConversationId.equals(assistantMsg.getConversationId())) {
                continue;
            }

            if (assistantMsg.getReplyId() == null) {
                continue; // 不是AI回复，跳过
            }

            // 找到对应的用户消息
            if (i > 0) {
                AiChatMessageDO userMsg = messages.get(i - 1);

                // 确保用户消息也属于同一对话，并且是AI回复对应的消息
                if (requiredConversationId.equals(userMsg.getConversationId())
                        && assistantMsg.getReplyId().equals(userMsg.getId())) {

                    // 添加到列表开头，保持时间顺序
                    contextMessages.add(0, userMsg);      // 用户消息
                    contextMessages.add(0, assistantMsg); // AI回复
                    contextCount++;
                    i--; // 跳过用户消息
                }
            }
        }

        return contextMessages;
    }

    /**
     * 保存用户发起消息
     *
     * @param sendReqVO
     * @param userId
     * @return
     */
    public AiChatMessageSaveReqVO saveUserMessage(ChatMessageSendReqVO sendReqVO, Long userId) {
        AiChatMessageSaveReqVO chatMessageSaveReqVO = new AiChatMessageSaveReqVO();
        chatMessageSaveReqVO.setConversationId(sendReqVO.getConversationId());
        chatMessageSaveReqVO.setUserId(userId);
        chatMessageSaveReqVO.setType(USER_MESSAGE_TYPE);
        chatMessageSaveReqVO.setReplyType(sendReqVO.getReplyType());
        chatMessageSaveReqVO.setContent(sendReqVO.getContent());
        chatMessageSaveReqVO.setContextFlag(sendReqVO.getContextFlag() ? CONTEXT_FLAG_ENABLED : CONTEXT_FLAG_DISABLED);
        chatMessageSaveReqVO.setCreateTime(DateUtils.getNowDate());

        Long id = aiChatMessageService.createAiChatMessage(chatMessageSaveReqVO);
        chatMessageSaveReqVO.setId(id);
        return chatMessageSaveReqVO;
    }

    /**
     * 保存机器人消息
     *
     * @param userId
     * @return
     */
    public AiChatMessageSaveReqVO saveRobotMessage(AiChatMessageSaveReqVO chatMessageSaveReqVO, Long userId) {
        chatMessageSaveReqVO.setUserId(userId);
        chatMessageSaveReqVO.setType(AI_MESSAGE_TYPE);
        chatMessageSaveReqVO.setReplyType(chatMessageSaveReqVO.getReplyType());

        Long id = aiChatMessageService.createAiChatMessage(chatMessageSaveReqVO);
        chatMessageSaveReqVO.setId(id);
        return chatMessageSaveReqVO;
    }
}
