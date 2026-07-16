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
import tech.qiantong.qdata.common.utils.MessageUtils;
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
 * Purpose:
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
        //User question content
        JSONObject contentObj = JSONObject.parseObject(sendReqVO.getContent());

        // Get conversation information
        AiChatConversationDO conversation = aiChatConversationService.getAiChatConversationById(sendReqVO.getConversationId());
        if (conversation == null) {
            return Flux.error(new ServiceException("对话不存在"));
        }
        if (!conversation.getJoinConditionMatchFlag()) {
            return Flux.error(new ServiceException("请手动配置关联信息"));
        }

        //Get data source information
        DaDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(conversation.getDatasourceId());
        if (datasource == null) {
            return Flux.error(new ServiceException("ai.error.datasource.notfound", "数据源不存在"));
        }
        String datasourceType = datasource.getDatasourceType();
        JSONObject datasourceConfig = JSONObject.parseObject(datasource.getDatasourceConfig());

        // Get a list of conversation history messages for contextual association
        List<AiChatMessageDO> messageHistory = aiChatMessageService.getChatMessageListByConversationId(sendReqVO.getConversationId());

        // If it's a new conversation, use the first 10 characters of the user's message as the title
        if (messageHistory.isEmpty()) {
            conversation.setTitle(StringUtils.substring(contentObj.getString("msg"), 0, TITLE_MAX_LENGTH));
            aiChatConversationService.updateById(conversation);
        }

        // Save user messages to the database and return the saved message information
        AiChatMessageSaveReqVO userMessage = saveUserMessage(sendReqVO, userId);

        // Message object to prepare AI reply to
        AiChatMessageSaveReqVO aiMessageTemplate = new AiChatMessageSaveReqVO();
        aiMessageTemplate.setReplyId(userMessage.getId());      // Associated user message ID
        aiMessageTemplate.setReplyType(sendReqVO.getReplyType());
        aiMessageTemplate.setContextFlag(sendReqVO.getContextFlag() ? CONTEXT_FLAG_ENABLED : CONTEXT_FLAG_DISABLED);    // Whether to use context
        aiMessageTemplate.setConversationId(sendReqVO.getConversationId()); // Conversation ID

        // Step 2: Local LLM call (core processing logic)
        // 1. Obtain the LLM model client - the DeepSeek platform is used here
        ChatModel chatModel = chatModelService.getChatModel(sendReqVO.getModelId());

        // 2. Build a message list (following OpenAI’s message format)
        List<Message> contextMessages = new ArrayList<>();

        // 3. Add context history message (if there is context flag)
        if (Boolean.TRUE.equals(sendReqVO.getContextFlag()) && !messageHistory.isEmpty()) {
            // Get recent historical messages (such as the last 10 groups of conversations)
            List<AiChatMessageDO> filteredMessages = filterContextMessages(messageHistory, MAX_CONTEXT_MESSAGES, sendReqVO.getConversationId());
            for (AiChatMessageDO msg : filteredMessages) {
                if (USER_MESSAGE_TYPE.equals(msg.getType())) {
                    // User messages
                    contextMessages.add(new UserMessage(msg.getContent()));
                } else {
                    // AI reply
                    contextMessages.add(new UserMessage("助手回复: " + msg.getContent()));
                }
            }
        }

        // 4. Construct prompt words
        //4.1 Get the fact table
        FactTable factTable = genFactTable(conversation, datasourceConfig);

        //4.2 Get dimension list
        List<DimensionTable> dimensionTables = genDimensionTableList(conversation, datasourceConfig);

        //4.3 Assembling associated condition data
        List<FactDimensionRelation> relations = genRelations(conversation);

        //4.4 Build Prompt
        String promptStr = promptBuilder.buildPrompt(
                ReplyTypeEnum.getByType(sendReqVO.getReplyType()),
                factTable,
                dimensionTables,
                relations,
                datasourceType
        );

        List<Message> promptMessages = new ArrayList<>();
        //Add prompt word
        promptMessages.add(new SystemMessage(promptStr));
        //Add user requirements
        promptMessages.add(new UserMessage("\n【统计需求】\n" + contentObj.getString("msg")));
        //Add context
        promptMessages.addAll(contextMessages);
        Prompt prompt = new Prompt(promptMessages);

        // Step 3: Process the data
        StringBuilder contentBuffer = new StringBuilder();
        // Streaming LLM call (get AI reply in real time)
        return LlmUtils.streamLlmResponse(chatModel, prompt)
                .map(AiUtils::getChatResponseContent) // Extract response content
                .filter(content -> content != null && !content.isEmpty()) // Filter empty content
                .map(content -> {
                    // Collect every piece of AI reply
                    contentBuffer.append(content);

                    // Send streaming response to the front end (simulate message event)
                    return messageSent(content, userMessage);
                })
                .concatWith(Mono.defer(() -> {
                    JSONObject content = JSONObject.parseObject(contentBuffer.toString());
                    if (!content.getBoolean("success")) {
                        content.put("msg", "对话异常: " + content.getString("msg"));
                        content.put("type", ChatMessageTypeEnum.ERROR.getType());
                    } else {
                        //Determine whether it is a smart chart
                        if (ReplyTypeEnum.CHART.getType().equals(sendReqVO.getReplyType())) {
                            content.put("type", ChatMessageTypeEnum.CHAT.getType());
                            String sql = content.getString("sql");
                            //Dimension data fields
                            String dimension = content.getString("dimension");
                            //Measure data fields
                            List<String> measures = JSONArray.parseArray(content.getString("measure"), String.class);

                            //Create query
                            DbQueryProperty dbQueryProperty = new DbQueryProperty(
                                    datasource.getDatasourceType(),
                                    datasource.getIp(),
                                    datasource.getPort(),
                                    datasource.getDatasourceConfig()
                            );
                            DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);

                            //Verify whether the sql syntax is correct
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

                            //Construct statistical data analysis prompt words
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

                        // After the streaming call is completed, save the complete bot message
                        aiMessageTemplate.setContent(content.toString()); // AI complete reply
                    }
                    // Save AI reply to database
                    AiChatMessageSaveReqVO savedMessage = saveRobotMessage(aiMessageTemplate, userId);

                    return Mono.just(messageEnd(
                            savedMessage.getId(),  // Saved AI message ID
                            content.toString(), // AI complete reply content
                            sendReqVO.getReplyType()
                    ));
                }))
                .publishOn(Schedulers.boundedElastic())
                .doOnError(error -> {
                    error.printStackTrace();
                    log.error("LLM调用失败", error);
                    // Save error information to database
                    JSONObject errorContent = new JSONObject();
                    errorContent.put("msg", "对话异常: " + error.getMessage());
                    errorContent.put("type", ChatMessageTypeEnum.ERROR.getType());
                    aiMessageTemplate.setContent(errorContent.toString());
                    saveRobotMessage(aiMessageTemplate, userId);
                });
    }

    @Override
    public void exportDetailData(HttpServletResponse response, ChatMessageExportDetailDataReqVO exportDetailDataReqVO) {
        //Get message data
        AiChatMessageDO message = aiChatMessageService.getById(exportDetailDataReqVO.getMessageId());
        if (message == null) {
            throw new ServiceException("ai.error.message.notfound", "消息不存在");
        }
        if (StringUtils.isBlank(message.getReplyType()) || !ReplyTypeEnum.CHART.getType().equals(message.getReplyType())) {
            throw new ServiceException("ai.error.message.export.unsupported", "此消息不支持导出");
        }
        if (StringUtils.isBlank(message.getContent())) {
            throw new ServiceException("ai.error.message.content.empty", "消息内容为空");
        }

        JSONObject content = JSONObject.parseObject(message.getContent());
        JSONObject detailData = content.getJSONObject("detailData");
        if (detailData == null) {
            throw new ServiceException("ai.error.message.content.error", "消息内容错误");
        }

        List<String> label = detailData.getList("label", String.class);
        List<JSONObject> dataList = detailData.getList("list", JSONObject.class);
        if (dataList == null || dataList.isEmpty()) {
            throw new ServiceException("ai.error.message.content.error", "消息内容错误");
        }

        //Export
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
            //Dimension table name
            String tableName = dimensionTable.getString("tableName");
            //Dimension table annotations
            String tableComment = dimensionTable.getString("tableComment");

            //Get dimension table fields
            List<DbColumn> dbColumns = daDatasourceApiService.getDbTableColumns(
                    conversation.getDatasourceId(), tableName);

            //Generate dimension table entity list
            List<DimensionTable.Column> columns = new ArrayList<>();
            //Dimension table primary key
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
        //Fact table name
        String factTableName = conversation.getFactTableName();
        //Fact table annotation
        String factTableComment = conversation.getFactTableComment();

        //Get fact table fields
        List<DbColumn> dbColumns = daDatasourceApiService.getDbTableColumns(
                conversation.getDatasourceId(), factTableName);

        //Assemble fact table entities
        List<FactTable.Column> columns = new ArrayList<>();
        //Fact table primary key
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
     * Verify sql syntax
     *
     * @param sql
     * @param dbQuery
     * @throws ServiceException sql syntax error or non-query sql
     */
    void verifySql(String sql, DbQuery dbQuery) {
        try (Connection connection = dbQuery.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Use PreparedStatement to precompile without actual execution
            // Get metadata. If it is not a query statement, some drivers will return null or report an error here or during execution.
            ResultSetMetaData metaData = ps.getMetaData();
            if (metaData == null) {
                throw new ServiceException("ai.error.sql.not.query", "该 SQL 不是查询语句或语法有误");
            }
        } catch (SQLException e) {
            throw new ServiceException("ai.error.sql.syntax", "SQL 语法错误: " + e.getMessage(), e.getMessage());
        }
    }

    @SneakyThrows
    private static void exportByList(HttpServletResponse response, List<String> labelList, List<JSONObject> dataList, String sheetName) {
        if (dataList == null || dataList.isEmpty()) {
            throw new ServiceException("ai.error.form.notfound", "暂无表单信息");
        }

        // Get all column names of the first row of data as order
        Map<String, Object> firstRow = dataList.get(0);
        // Use Set to ensure column name uniqueness
        List<String> order = new ArrayList<>(firstRow.keySet());

        //1. Create a workbook
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            //Header field font
            XSSFFont headFont = workbook.createFont();
            //Font height
            headFont.setFontHeightInPoints(HEADER_FONT_SIZE);
            //Font
            headFont.setFontName(FONT_NAME);
            headFont.setBold(true);

            // Set cell type
            XSSFCellStyle headCellStyle = workbook.createCellStyle();
            headCellStyle.setFont(headFont);
            //Horizontal layout: centered
            headCellStyle.setAlignment(HorizontalAlignment.CENTER);
            //Center vertically
            headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headCellStyle.setWrapText(true);

            //Annotation and stroke font
            XSSFFont font = workbook.createFont();
            //Font height
            font.setFontHeightInPoints(CONTENT_FONT_SIZE);
            //Font
            font.setFontName(FONT_NAME);

            //Column style
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            //Horizontal layout: centered
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //Center vertically
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setWrapText(true);

            //2. Create worksheet
            XSSFSheet sheet = workbook.createSheet(sheetName);

            //Freeze first row
            sheet.createFreezePane(0, 1, 0, 1);

            //3. Create field row
            XSSFRow headerRow = sheet.createRow(0);
            headerRow.setHeight(HEADER_ROW_HEIGHT);

            for (int i = 0; i < labelList.size(); i++) {
                //Set default width
                sheet.setColumnWidth(i, EXCEL_COLUMN_WIDTH);
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(labelList.get(i));
            }

            //4. data row
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> map = dataList.get(i);
                //Data row
                XSSFRow dataRow = sheet.createRow(i + 1);
                dataRow.setHeight(DATA_ROW_HEIGHT);

                int columnIndex = 0;
                for (String key : order) {
                    Object valueObj = map.get(key);
                    String value = "";
                    if (valueObj instanceof Date) {
                        // If it is a date type, convert it to a fixed format string
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
                //5. Output stream output
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
     * The message ends and the reference document fragment is stored
     */
    public ChatMessageSendRespVO messageEnd(Long saveId, String content, String replyType) {
        // Send complete answer data
        ChatMessageSendRespVO sendRespVO = new ChatMessageSendRespVO();
        ChatMessageSendRespVO.Message message = new ChatMessageSendRespVO.Message();
        message.setId(saveId);
        message.setContent(content);
        message.setType(AI_MESSAGE_TYPE);
        message.setReplyType(replyType);
        // Receive messages
        sendRespVO.setReceive(message);
        return sendRespVO;
    }

    /**
     * Streaming data processing
     *
     * @param content
     * @param chatMessageSaveReqVO
     * @return
     */
    public ChatMessageSendRespVO messageSent(String content, AiChatMessageSaveReqVO chatMessageSaveReqVO) {
        ChatMessageSendRespVO sendRespVO = new ChatMessageSendRespVO();
        // Bot reply to message
        ChatMessageSendRespVO.Message message = new ChatMessageSendRespVO.Message();
        message.setType(AI_MESSAGE_TYPE);
        message.setContent(content);
        message.setCreateTime(DateUtils.getNowDate());
        message.setReplyType(chatMessageSaveReqVO.getReplyType());
        sendRespVO.setReceive(message); // Receive messages

        // User sends message
        ChatMessageSendRespVO.Message messageUser = new ChatMessageSendRespVO.Message();
        messageUser.setType(USER_MESSAGE_TYPE);
        messageUser.setContent(chatMessageSaveReqVO.getContent());
        messageUser.setCreateTime(chatMessageSaveReqVO.getCreateTime());
        messageUser.setId(chatMessageSaveReqVO.getId());
        sendRespVO.setSend(messageUser); // Send message

        return sendRespVO;
    }

    /**
     * Filter contextual messages
     *
     * @param messages List of all historical messages
     * @param maxContexts Maximum number of contexts (conversation rounds)
     * @param requiredConversationId required conversation ID, ensuring that only messages from the current conversation are obtained
     * @return List<AppChatMessageDO> Filtered context message list, arranged in chronological order
     */
    private List<AiChatMessageDO> filterContextMessages(List<AiChatMessageDO> messages, int maxContexts, Long requiredConversationId) {

        if (messages == null || messages.isEmpty()) {
            return Collections.emptyList();
        }

        List<AiChatMessageDO> contextMessages = new ArrayList<>();
        int contextCount = 0;

        // Traverse from back to front to find the most recent conversation pair (user message + AI reply)
        for (int i = messages.size() - 1; i >= 0 && contextCount < maxContexts; i--) {
            AiChatMessageDO assistantMsg = messages.get(i);

            // Key: Make sure the message belongs to the specified conversation
            if (!requiredConversationId.equals(assistantMsg.getConversationId())) {
                continue;
            }

            if (assistantMsg.getReplyId() == null) {
                continue; // Not an AI reply, skip it
            }

            // Find the corresponding user message
            if (i > 0) {
                AiChatMessageDO userMsg = messages.get(i - 1);

                // Ensure that user messages also belong to the same conversation and that the AI replies to the corresponding message
                if (requiredConversationId.equals(userMsg.getConversationId())
                        && assistantMsg.getReplyId().equals(userMsg.getId())) {

                    // Add to the beginning of the list, keeping chronological order
                    contextMessages.add(0, userMsg);      // User messages
                    contextMessages.add(0, assistantMsg); // AI reply
                    contextCount++;
                    i--; // Skip user messages
                }
            }
        }

        return contextMessages;
    }

    /**
     * Save user-initiated messages
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
     * Save bot message
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
