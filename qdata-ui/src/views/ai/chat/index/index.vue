<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <el-container class="ai-layout">
    <!-- Left: Conversation list -->
    <ConversationList
      :active-id="activeConversationId"
      :datasource-id="datasourceId"
      :fact-table-name="factTableName"
      :fact-table-comment="factTableComment"
      :dimension-table="dimensionTable"
      ref="conversationListRef"
      @on-conversation-create="handleConversationCreateSuccess"
      @on-conversation-click="handleConversationClick"
      @on-conversation-clear="handleConversationClear"
      @on-conversation-delete="handlerConversationDelete"
    />
    <!-- Right: Conversation details -->
    <el-container class="detail-container app-container" direction="vertical">
      <DataScopeConfig
        v-model:datasourceId="datasourceId"
        v-model:factTableName="factTableName"
        v-model:factTableComment="factTableComment"
        v-model:dimensionTableNames="dimensionTableNames"
        :title="activeConversation?.title || td('ai.chat.newChat')"
        :disabled="!!activeConversationId"
        :initialShowConfig="!activeConversationId"
        :joinConditionMatchFlag="joinConditionMatchFlag"
        :conversationId="activeConversationId"
        :tableCommentMap="tableCommentMap"
        ref="dataScopeConfigRef"
        @confirm="handleConfigConfirm"
        @confirm-associations="handleAssociationsConfirm"
        @open-association-dialog="handleOpenAssociationDialog"
      >
        <template #extra>
          <div v-if="messageList.length !== 0" class="header-btns">
            <img
              src="../../../../assets/images/ai/img-group-23248.png"
              class="btn"
              @click="handlerMessageClear"
              :alt="td('ai.chat.clearSession')"
            />
            <img
              src="../../../../assets/images/ai/img-group-one.png"
              class="btn"
              @click="handleGoBottomMessage"
              :alt="td('ai.chat.navDown')"
            />
            <img
              src="../../../../assets/images/ai/img-group-23249.png"
              :alt="td('ai.chat.navUp')"
              class="btn"
              @click="handleGoTopMessage"
            />
          </div>
        </template>
      </DataScopeConfig>

      <!-- main: message list -->
      <el-main class="main-container">
        <div>
          <div class="message-container">
            <!-- Scenario 3: Skeleton screen displayed during loading -->
            <MessageLoading v-if="activeMessageListLoading" />
            <!-- Situation 4: When the message list is empty or there is no chat conversation -->
            <MessageListEmpty
              v-else-if="!activeConversationId && messageList.length === 0"
              v-model="prompt"
              v-model:selectedModelId="selectedModelId"
              v-model:chatType="chatType"
              :datasource-id="datasourceId"
              :fact-table-name="factTableName"
              :dimension-table-names="dimensionTableNames"
              :modelList="modelList"
              @on-prompt="doSendMessage"
              @enter="handleSendByKeydown"
              @shift-enter="addNewLine"
            />
            <!-- Case 5: The message list is not empty -->
            <MessageList
              v-else
              ref="messageRef"
              :conversation="activeConversation"
              :list="messageList"
              :suggestedList="suggestedList"
              @on-prompt="doSendMessage"
              @on-delete-success="handleMessageDelete"
              @on-edit="handleMessageEdit"
              @on-refresh="handleMessageRefresh"
            />
          </div>
        </div>
      </el-main>

      <!-- bottom -->
      <el-footer
        class="footer-container"
        v-if="
          !activeMessageListLoading &&
          (activeConversationId || messageList.length > 0)
        "
      >
        <!--        <el-divider />-->
        <form class="prompt-from">
          <el-input
            type="textarea"
            class="prompt-input"
            :autosize="{ minRows: 3 }"
            v-model="prompt"
            @keydown.enter.prevent="handleSendByKeydown"
            @keydown.shift.enter="addNewLine"
            :placeholder="td('ai.chat.inputPlaceholder')"
          />
          <div class="prompt-btns">
            <div class="footer-left">
              <el-select
                v-model="selectedModelId"
                :placeholder="td('ai.chat.selectModel')"
                size="default"
                class="model-select"
                popper-class="ai-model-select-popper"
              >
                <template #prefix>
                  <img :src="selectedModelIcon" class="model-icon" />
                </template>
                <el-option
                  v-for="item in modelList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                  <template #default>
                    <div class="model-option">
                      <img
                        :src="getModelIconByPlatform(item.platform)"
                        class="model-option-icon"
                      />
                      <span>{{ item.name }}</span>
                    </div>
                  </template>
                </el-option>
              </el-select>
              <el-select
                v-model="chatType"
                :placeholder="td('ai.chat.answerType')"
                size="default"
                class="chat-type-select"
                popper-class="ai-chat-type-select-popper"
              >
                <template #prefix>
                  <el-icon class="chat-type-icon">
                    <component
                      :is="
                        chatType
                          ? CHAT_TYPES.find((t) => t.value === chatType)
                              ?.icon || Plus
                          : Plus
                      "
                    />
                  </el-icon>
                </template>
                <el-option
                  v-for="item in CHAT_TYPES"
                  :key="item.value"
                  :value="item.value"
                  :label="td(item.labelKey, item.label)"
                  :disabled="item.disabled"
                >
                  <template #default>
                    <div class="chat-type-option">
                      <el-icon class="chat-type-option-icon">
                        <component :is="item.icon" />
                      </el-icon>
                      <span>{{ td(item.labelKey, item.label) }}</span>
                    </div>
                  </template>
                </el-option>
              </el-select>
            </div>
            <div class="footer-right">
              <el-button
                type="primary"
                class="btn-send"
                @click="handleSendByButton"
                v-if="conversationInProgress == false"
              >
                {{ td('ai.chat.send') }}
              </el-button>
              <el-button
                type="danger"
                class="btn-stop"
                @click="stopStream()"
                v-if="conversationInProgress == true"
              >
                {{ td('ai.chat.stop') }}
              </el-button>
            </div>
          </div>
        </form>
        <div class="ai-disclaimer">
          {{ td('ai.chat.disclaimer') }}
        </div>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
import { ChatConversationApi } from "@/api/ai/chat/conversation";
import { ChatMessageApi } from "@/api/ai/message/index";
import { getModelLists } from "@/api/ai/chat/message";
import ConversationList from "./components/conversation/ConversationList.vue";
import MessageList from "./components/message/MessageList.vue";
import MessageListEmpty from "./components/message/MessageListEmpty.vue";
import MessageLoading from "./components/message/MessageLoading.vue";
import DataScopeConfig from "./components/DataScopeConfig.vue";
import { CHAT_TYPES } from "./constants";
import defaultModelIcon from "@/assets/images/ai/icon-brand-gpt-new.svg";
import deepseekIcon from "@/assets/images/ai/icon-brand-deepseek.svg";
import tongyiIcon from "@/assets/images/ai/icon-brand-tongyi.svg";
import {
  ref,
  onMounted,
  getCurrentInstance,
  computed,
  nextTick,
  watch,
} from "vue";
import DatasourceList from "@/components/Datasource/List.vue";
import { getTablesByDataSourceId } from "@/api/dpp/task/index.js";
import { Plus } from "@element-plus/icons-vue";
import useDefaultLang from "@/composables/useDefaultLang";

/** qData intelligent question chat conversation list */
defineOptions({ name: "AiChat" });

const route = useRoute(); // routing
const { proxy } = getCurrentInstance();
const message = proxy.$modal; // Message pop-up window
const { td } = useDefaultLang();

// chat conversation
const conversationListRef = ref();
const dataScopeConfigRef = ref();
const activeConversationId = ref(null); // Selected conversation number
const activeConversation = ref(null); // Selected Conversation
const conversationInProgress = ref(false); // Is the conversation ongoing? Currently, only when sending a message, it will be updated to true to avoid operations such as switching conversations and deleting conversations.

// Message list
const messageRef = ref();
const activeMessageList = ref([]); // Message list of selected conversation
const activeMessageListLoading = ref(false); // Is activeMessageList loading?
const activeMessageListLoadingTimer = ref(); // activeMessageListLoading Timer timer. If the loading speed is very fast, it will not enter the loading process.
const suggestedList = ref([]); // Suggestion list

// Model selection and question and answer type
const modelList = ref([]);
const selectedModelId = ref(null);
const chatType = ref("chart");

const getModelIconByPlatform = (platform) => {
  const p = String(platform || "")
    .trim()
    .toLowerCase();
  if (!p) return defaultModelIcon;
  if (p.includes("deepseek")) return deepseekIcon;
  if (
    p.includes("tongyi") ||
    p.includes("qwen") ||
    p.includes("dashscope") ||
    p.includes("aliyun") ||
    p.includes("alibaba")
  ) {
    return tongyiIcon;
  }
  return defaultModelIcon;
};

const selectedModel = computed(() => {
  const id = selectedModelId.value;
  if (id == null) return null;
  return (
    modelList.value.find((m) => m?.id === id) ||
    modelList.value.find((m) => String(m?.id) === String(id)) ||
    null
  );
});

const selectedModelIcon = computed(() =>
  getModelIconByPlatform(selectedModel.value?.platform)
);

/** Get model list */
const getModelList = async () => {
  const res = await getModelLists();
  if (res.code === 200) {
    modelList.value = res.data || [];
    if (modelList.value.length > 0) {
      selectedModelId.value = modelList.value[0].id;
    }
  }
};

onMounted(() => {
  getModelList();
});

// Send message input box
const conversationInAbortController = ref(); // Conversation in progress abort controller (control stream conversation)
const prompt = ref(); // prompt
const enableContext = ref(true); // Whether to enable context
const datasourceId = ref("");
const factTableName = ref("");
const factTableComment = ref("");
const dimensionTableNames = ref([]);
const dimensionTable = ref("[]");
const tableCommentMap = ref({});
const joinConditionMatchFlag = ref(1); // 0: Matching is required, 1: Matching is not required

watch(
  dimensionTableNames,
  (names) => {
    const arr = (names || []).map((name) => ({
      tableName: name,
      tableComment: tableCommentMap.value[name] || "",
    }));
    dimensionTable.value = JSON.stringify(arr);
  },
  { deep: true }
);

const handleConfigConfirm = (config) => {
  datasourceId.value = config.datasourceId;
  factTableName.value = config.factTableName;
  factTableComment.value = config.factTableComment;

  if (config.tableCommentMap) {
    tableCommentMap.value = {
      ...tableCommentMap.value,
      ...config.tableCommentMap,
    };
  }

  dimensionTableNames.value = config.dimensionTableNames;
  // You can add other logic as needed, such as prompts to start Q&A, etc.
};

const handleAssociationsConfirm = async () => {
  // After the association relationship is successfully set, update the flag bit
  joinConditionMatchFlag.value = true;
  console.log("Association configured successfully");
  // Refresh session information
  if (activeConversationId.value) {
    const res = await ChatConversationApi.getChatConversationMy(
      activeConversationId.value
    );
    if (res.data) {
      await handleConversationClick(res.data);
    }
  }
  // Refresh the conversation list on the left
  if (conversationListRef.value) {
    await conversationListRef.value.getChatConversationList();
  }
};

const handleOpenAssociationDialog = (conversationId) => {
  dataScopeConfigRef.value.openAssociationDialog(conversationId);
};

const parseDimensionTableNames = (jsonStr) => {
  try {
    const arr = JSON.parse(jsonStr || "[]");
    if (!Array.isArray(arr)) return [];
    return arr.map((x) => x?.tableName).filter(Boolean);
  } catch {
    return [];
  }
};

function toNumber(v) {
  if (v == null) return v;
  if (typeof v === "number") return v;
  const n = Number(v);
  return Number.isNaN(n) ? v : n;
}

// =========== [Chat Dialogue] Related ===========
/** Get conversation information */
const getConversation = async (id) => {
  if (!id) {
    return;
  }
  const info = await ChatConversationApi.getChatConversationMy(id);
  const conversation = info.data;
  if (!conversation) {
    return;
  }
  activeConversation.value = conversation;
  activeConversationId.value = conversation.id;
};

/**
 * Click on a conversation
 *
 * @param conversation selected conversation
 * @return Whether the switch is successful
 */
const handleConversationClick = async (conversation) => {
  // Conversation in progress, switching is not allowed
  if (conversationInProgress.value) {
    message.alert(td('ai.chat.cannotSwitchDuringConversation'));
    return false;
  }

  if (!conversation) {
    return false;
  }

  console.log("handleConversationClick", conversation);
  // Update selected conversation id
  activeConversationId.value = conversation.id;
  activeConversation.value = conversation;
  datasourceId.value = conversation.datasourceId || "";
  factTableName.value = conversation.factTableName || "";
  factTableComment.value = conversation.factTableComment || "";
  dimensionTable.value = conversation.dimensionTable || "[]";

  // Parse dimension tables and synchronize to tableCommentMap
  try {
    const arr = JSON.parse(dimensionTable.value);
    if (Array.isArray(arr)) {
      arr.forEach((item) => {
        if (item.tableName) {
          tableCommentMap.value[item.tableName] = item.tableComment || "";
        }
      });
    }
  } catch (e) {
    console.error("Failed to parse dimensionTable:", e);
  }

  dimensionTableNames.value = parseDimensionTableNames(dimensionTable.value);
  joinConditionMatchFlag.value =
    conversation.joinConditionMatchFlag !== undefined
      ? conversation.joinConditionMatchFlag
      : true;

  // Refresh message list
  await getMessageList();
  // scroll bottom
  scrollToBottom(true);
  // Clear input box
  prompt.value = "";
  return true;
};

/** Delete a conversation*/
const handlerConversationDelete = async (delConversation) => {
  // If the deleted conversation is currently selected, it will be reset.
  if (activeConversationId.value === delConversation.id) {
    await handleConversationClear();
  }
};
/** Clear selected conversations */
const handleConversationClear = async () => {
  // Conversation in progress, switching is not allowed
  if (conversationInProgress.value) {
    message.alert(td('ai.chat.cannotSwitchDuringConversation'));
    return false;
  }
  activeConversationId.value = null;
  activeConversation.value = null;
  activeMessageList.value = [];
  suggestedList.value = [];
  // Clear configuration
  datasourceId.value = "";
  factTableName.value = "";
  factTableComment.value = "";
  dimensionTableNames.value = [];
  dimensionTable.value = "[]";
  tableCommentMap.value = {};
  joinConditionMatchFlag.value = 1;
  prompt.value = "";
};

/** Handle chat conversation creation successfully */
const handleConversationCreate = async (mId) => {
  // Create conversation
  return await conversationListRef.value.createConversation({
    datasourceId: datasourceId.value,
    factTableName: factTableName.value,
    factTableComment: factTableComment.value,
    dimensionTable: dimensionTable.value,
    modelId: mId || selectedModelId.value,
  });
};
/** Handle chat conversation creation successfully */
const handleConversationCreateSuccess = async (data) => {
  // Create a new conversation. If it is 10001, the input box will not be cleared.
  if (data?.code !== 10001) {
    prompt.value = "";
    if (data?.id) {
      activeConversationId.value = data.id;
      activeConversation.value = data;
    }
  }
  if (activeConversation.value) {
    activeConversation.value.code = data?.code;
  }
  if (data?.joinConditionMatchFlag !== undefined) {
    joinConditionMatchFlag.value = data.joinConditionMatchFlag;
  }
  if (data?.code === 10001) {
    // When creating a session and returning 10001, stop the session and pop up a "confirmation pop-up window"
    stopStream();
    dataScopeConfigRef.value.handleOpenAssociationConfirm(
      data.id || activeConversationId.value
    );
  }
};

// =========== [Message List] Related ===========

/** Get message list */
const getMessageList = async () => {
  try {
    console.log("🚀 Message:", activeConversationId);
    if (activeConversationId.value === null) {
      return;
    }

    activeMessageListLoading.value = true;
    const messageList = await ChatMessageApi.getChatMessageListByConversationId(
      activeConversationId.value
    );
    // Get message list
    activeMessageList.value = messageList.data;
    activeMessageListLoading.value = false;

    // Scroll to the bottom
    await nextTick();
    await scrollToBottom();

    // Get advice
    // await getSuggested();
  } finally {
    // time timer, if the loading speed is very fast, it will not enter the loading process
    if (activeMessageListLoadingTimer.value) {
      clearTimeout(activeMessageListLoadingTimer.value);
    }
    // Loading ends
    activeMessageListLoading.value = false;
  }
};

/**
 * Get advice
 * @returns {Promise<void>}
 */
const getSuggested = async () => {
  // Get suggestions (interface is temporarily called)
  suggestedList.value = [];
  /*
  if (activeMessageList.value.length > 0) {
    const lastMessage = activeMessageList.value.findLast(
      (item) => item.type === 2
    );
    if (lastMessage) {
      const suggested = await ChatMessageApi.getSuggested(lastMessage.id);
      suggestedList.value = suggested.data;
    }
  }
  */
  // Scroll to the bottom
  await nextTick();
  await scrollToBottom();
};

/**
 * Message list
 *
 * The difference from {@link #getMessageList()} is that systemMessage is taken into account
 */
const messageList = computed(() => {
  if (activeMessageList.value.length > 0) {
    return activeMessageList.value;
  }
  // When there is no message, if there is systemMessage, display it
  if (activeConversation.value?.systemMessage) {
    return [
      {
        id: 0,
        type: 2,
        content: activeConversation.value.systemMessage,
      },
    ];
  }
  return [];
});

/** Handle deletion of message messages */
const handleMessageDelete = () => {
  if (conversationInProgress.value) {
    message.alert(td('ai.chat.cannotDeleteDuringAnswer'));
    return;
  }
  // Refresh message list
  getMessageList();
};

/** Process message clear */
const handlerMessageClear = async () => {
  if (!activeConversationId.value) {
    return;
  }
  try {
    // Confirmation prompt
    await message.confirm(td('ai.chat.confirmClearMessages'));
    // Clear conversation
    await ChatMessageApi.deleteByConversationId(activeConversationId.value);
    // Refresh message list
    activeMessageList.value = [];
  } catch {
    return;
  }
};

/** Return to the top of the message list */
const handleGoBottomMessage = () => {
  messageRef.value.handleGoBottom();
};

/** Return to the top of the message list */
const handleGoTopMessage = () => {
  messageRef.value.handlerGoTop();
};

// =========== [Send Message] Related ===========

/** Handle send messages from keydown */
const handleSendByKeydown = async (event, mId, cType) => {
  if (event.shiftKey) {
    return; // If the Shift key is pressed, the sending logic is not executed
  }
  // Not allowed to send while in progress
  if (conversationInProgress.value) {
    return;
  }
  const content = prompt.value?.trim();
  // Send message
  await doSendMessage(content, mId, cType);
  // event.preventDefault(); //Prevent default line wrapping behavior
};

const addNewLine = (event) => {
  // Insert newline
  prompt.value += "\r\n";
  event.preventDefault(); //Prevent default newline behavior
};

/** Process the send message from the [Send] button */
const handleSendByButton = () => {
  doSendMessage(prompt.value?.trim());
};

/** Actually execute the [Send] message operation */
const doSendMessage = async (content, mId, cType) => {
  // Add a layer of protection: make sure content is unpacked plain text, and also unpack it if it is recursively nested
  const rawContent = getDisplayContent({ content: content });

  // Verification
  if (rawContent.length < 1) {
    message.msgError(td('ai.chat.sendFailedEmpty'));
    return;
  }

  // Check data range
  if (
    !datasourceId.value ||
    !factTableName.value ||
    !dimensionTableNames.value?.length
  ) {
    message.msgError(td('ai.chat.configureDataScopeFirst'));
    return;
  }

  // Verification answer method
  const currentChatType = cType || chatType.value;
  if (!currentChatType) {
    message.msgError(td('ai.chat.selectAnswerType'));
    return;
  }

  // Verify association
  if (
    joinConditionMatchFlag.value === false ||
    joinConditionMatchFlag.value === null
  ) {
    dataScopeConfigRef.value.handleOpenAssociationConfirm(
      activeConversationId.value
    );
    return;
  }

  suggestedList.value = [];
  if (activeConversationId.value == null) {
    // First send: display loading status, used for MessageListEmpty to display skeleton screen
    activeMessageListLoading.value = true;
    try {
      const createRes = await handleConversationCreate(mId);
      if (createRes && createRes.code === 10001) {
        conversationInProgress.value = false;
        activeMessageListLoading.value = false; // 10001 status needs to restore the display of the input box
        return;
      }
      await getMessageList();
    } catch (e) {
      activeMessageListLoading.value = false;
      throw e;
    }
    // message.msgError('The conversation has not been created yet and cannot be sent!')
    // return
  }
  if (activeMessageList.value.length <= 0 && activeConversation.value) {
    // name truncation
    activeConversation.value.title = rawContent.substring(0, 10);
  }
  // Clear input box
  prompt.value = "";
  // Execute send
  await doSendMessageStream({
    conversationId: activeConversationId.value,
    content: rawContent,
    modelId: mId || selectedModelId.value,
    chatType: cType || chatType.value,
  });
};

/** Actually execute the [Send] message operation */
const doSendMessageStream = async (userMessage) => {
  // Create an AbortController instance to abort requests
  conversationInAbortController.value = new AbortController();
  // Mark conversation in progress
  conversationInProgress.value = true;

  try {
    // Scenario 1: Display a message locally first
    const newUserMessage = {
      id: null, // The id has not been returned yet
      type: 1,
      content: userMessage.content,
      createTime: Date.now(),
    };
    activeMessageList.value.push(newUserMessage);
    const newAssistantMessage = {
      id: null, // The id has not been returned yet
      type: 2,
      content: "loading",
      replyType: userMessage.chatType === "chart" ? 2 : 1,
      createTime: Date.now(),
    };
    activeMessageList.value.push(newAssistantMessage);
    // scroll bottom
    scrollToBottom();

    // Case 2: Execute Stream sending
    let isFirstChunk = true; // Whether it is the first chunk message segment
    await ChatMessageApi.sendChatMessageStream(
      userMessage.conversationId,
      JSON.stringify({ msg: userMessage.content }),
      conversationInAbortController.value,
      enableContext.value,
      userMessage.chatType === "smart"
        ? "1"
        : userMessage.chatType === "chart"
        ? "2"
        : "1", // replyType: 1-knowledge question and answer, 2-knowledge graph
      userMessage.modelId,
      async (res) => {
        const { code, data, msg } = JSON.parse(res.data);
        if (code !== 200) {
          // No more error pop-up windows, instead display error prompts in the message list
          const lastMessage =
            activeMessageList.value[activeMessageList.value.length - 1];
          lastMessage.content = msg || td('ai.chat.dialogError');
          lastMessage.isError = true;
          message.msgError(msg);
          // await getMessageList();
          stopStream();
          return;
        }

        const rt = toNumber(data.receive?.replyType);
        // If it is reportCard type (replyType === 2)
        if (rt === 2) {
          // If data.send is not empty, it means it is still streaming and loading is displayed.
          if (data.send !== null) {
            if (isFirstChunk) {
              isFirstChunk = false;
              // Two fake data pop up
              activeMessageList.value.pop();
              activeMessageList.value.pop();
              // Add formal user message and assistant loading message
              activeMessageList.value.push(data.send);
              activeMessageList.value.push({
                ...data.receive,
                content: "loading", // Special tag used to display loading on the front end
              });
            }
            return;
          }
          // If data.send is empty, the transfer is completed and the final data is updated.
          if (data.send === null && data.receive.id !== null) {
            activeMessageList.value[activeMessageList.value.length - 1] =
              data.receive;
            // await getSuggested();
            await scrollToBottom();
            return;
          }
        }

        // Normal text type (replyType !== 2)
        if (data.receive.id !== null) {
          activeMessageList.value[activeMessageList.value.length - 1] =
            data.receive;
          // await getSuggested();
          return;
        }

        // If the content is empty, it will not be processed.
        const chunkContent = data.receive.content || data.receive.msg || "";
        if (chunkContent === "") {
          return;
        }
        // When you return for the first time, you need to add a message to the page, and the rest will be updated.
        if (isFirstChunk) {
          isFirstChunk = false;
          // Two fake data pop up
          activeMessageList.value.pop();
          activeMessageList.value.pop();
          // Update the returned data
          const receive = { ...data.receive, content: chunkContent };
          activeMessageList.value.push(data.send);
          activeMessageList.value.push(receive);
        } else {
          const lastMessage =
            activeMessageList.value[activeMessageList.value.length - 1];
          lastMessage.content += chunkContent;
        }
        await scrollToBottom();
      },
      (error) => {
        getMessageList();
        stopStream();
        throw error;
      },
      () => {
        stopStream();
      }
    );
  } catch (e) {
    console.error(e);
  }
};

/** Stop stream streaming call */
const stopStream = async () => {
  // Tip: If the stream is in progress, you need to call the controller to end it.
  if (conversationInAbortController.value) {
    conversationInAbortController.value.abort();
  }
  // set to false
  conversationInProgress.value = false;
};

function safeJsonParse(str, defVal = {}) {
  try {
    if (typeof str === "object" && str !== null) return str;
    return JSON.parse(str || "");
  } catch {
    return defVal;
  }
}

function getDisplayContent(item) {
  let content = item?.content || "";
  // Parse recursively until it is no longer a JSON string containing msg
  while (true) {
    const raw = safeJsonParse(content, null);
    if (raw && typeof raw === "object" && raw.msg) {
      content = raw.msg;
    } else {
      break;
    }
  }
  return content;
}

/** Edit message: Set to prompt and can be edited again */
const handleMessageEdit = (message) => {
  prompt.value = getDisplayContent(message);
};

/** Refresh message: Based on the specified message, initiate the conversation again */
const handleMessageRefresh = (message) => {
  doSendMessage(getDisplayContent(message));
};

// ============== [Message scrolling] related =============

/** Scroll to bottom of message */
const scrollToBottom = async (isIgnore) => {
  await nextTick();
  if (messageRef.value) {
    messageRef.value.scrollToBottom(isIgnore);
  }
};

// initialization
onMounted(async () => {
  // If there is a conversationId parameter, it is selected by default
  if (route.query.conversationId) {
    const id = route.query.conversationId;
    activeConversationId.value = id;
    await getConversation(id);
  }

  // If there is no activeConversationId, set the default data range
  if (!activeConversationId.value) {
    datasourceId.value = defaultDataScope.datasourceId;
    factTableName.value = defaultDataScope.factTableName;
    factTableComment.value = defaultDataScope.factTableComment;
    dimensionTable.value = defaultDataScope.dimensionTable;

    // Parse dimension tables and synchronize to tableCommentMap
    try {
      const arr = JSON.parse(dimensionTable.value);
      if (Array.isArray(arr)) {
        arr.forEach((item) => {
          if (item.tableName) {
            tableCommentMap.value[item.tableName] = item.tableComment || "";
          }
        });
      }
    } catch (e) {
      console.error("Failed to parse dimensionTable:", e);
    }
    dimensionTableNames.value = parseDimensionTableNames(dimensionTable.value);
  }

  // Get list data
  activeMessageListLoading.value = true;
  await getMessageList();
});
</script>

<style lang="scss" scoped>
:deep(.el-select--large .el-select__wrapper) {
  min-height: auto;
}

.ai-layout {
  position: absolute;
  flex: 1;
  left: 0;
  height: calc(100vh - 96px);
  width: 100%;
  padding: 13px 16px 0 13px;
  :deep(::-webkit-scrollbar) {
    width: 6px;
    height: 6px;
    background-color: white;
  }

  :deep(::-webkit-scrollbar-track) {
    background-color: white;
  }

  :deep(::-webkit-scrollbar-thumb) {
    background-color: #ccc;
    border-radius: 3px;
  }
}

.conversation-container {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 10px 10px 0;

  .btn-new-conversation {
    padding: 18px 0;
  }

  .search-input {
    margin-top: 20px;
  }

  .conversation-list {
    margin-top: 20px;

    .conversation {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      flex: 1;
      padding: 0 5px;
      margin-top: 10px;
      cursor: pointer;
      border-radius: 5px;
      align-items: center;
      line-height: 30px;

      &.active {
        background-color: #e6e6e6;

        .button {
          display: inline-block;
        }
      }

      .title-wrapper {
        display: flex;
        flex-direction: row;
        align-items: center;
      }

      .title {
        padding: 5px 10px;
        max-width: 220px;
        font-size: 14px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }

      .avatar {
        width: 28px;
        height: 28px;
        display: flex;
        flex-direction: row;
        justify-items: center;
      }

      // Conversation editing and deletion
      .button-wrapper {
        right: 2px;
        display: flex;
        flex-direction: row;
        justify-items: center;
        color: #606266;

        .el-icon {
          margin-right: 5px;
        }
      }
    }
  }

  // Character warehouse, clear unset dialogues
  .tool-box {
    line-height: 35px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: var(--el-text-color);

    > div {
      display: flex;
      align-items: center;
      color: #606266;
      padding: 0;
      margin: 0;
      cursor: pointer;

      > span {
        margin-left: 5px;
      }
    }
  }
}

// head
.detail-container {
  background: #ffffff;
  flex-direction: column;
  flex: 1;

  .main-container {
    margin-left: 0 !important;
  }
}
.app-container {
  margin: 0 !important;
}
.header-btns {
  display: flex;
  align-items: center;
  margin-left: 12px;
  border-radius: 2px;
  .btn {
    height: 30px;
    margin-right: 8px;
    cursor: pointer;
  }
}

// main container
.main-container {
  margin: 0 !important;
  padding: 0;
  position: relative;
  height: 100%;
  width: 100%;

  .message-container {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    //overflow-y: hidden;
    padding: 0;
    margin: 0;
  }
}

.footer-container {
  display: flex;
  flex-direction: column;
  height: auto;
  margin: 0;
  padding: 0;
  background: transparent;

  .prompt-from {
    display: flex;
    flex-direction: column;
    height: auto;
    margin: 10px 20px 10px;
    padding: 9px 10px;
    background: #ffffff;
    border-radius: 4px;
    border: 1px solid #eef1f5;
    &:focus-within {
      border-color: #535bf2;
      box-shadow: 0 4px 16px rgba(64, 158, 255, 0.08);
    }
  }

  .ai-disclaimer {
    text-align: center;
    font-size: 12px;
    color: #999;
    margin-bottom: 15px;
  }

  .prompt-input {
    margin-bottom: 8px;
    :deep(.el-textarea__inner) {
      box-shadow: none;
      resize: none;
      padding: 0;
      background: transparent;
      color: #303133;
      &::placeholder {
        font-size: 15px;
      }
    }
  }

  .prompt-input:focus {
    outline: none;
  }

  .prompt-btns {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 8px;

    .footer-left {
      display: flex;
      align-items: center;
      gap: 12px;

      .chat-type-tag-doubao {
        display: flex;
        align-items: center;
        background: #ffffff;
        border-radius: 2px;
        padding: 0 10px;
        height: 32px;
        cursor: pointer;
        transition: all 0.2s;
        border: 1px solid #dcdfe6;
        gap: 8px;
        box-sizing: border-box;

        &:hover {
          background: #f4f6f8;
          border-color: #409eff;
        }

        &.placeholder {
          width: auto;
          padding: 0 10px;
          justify-content: center;
          border-radius: 2px;
          background: #ffffff;
          .icon-switch-only {
            color: #606266;
            font-size: 16px;
          }
        }

        .content-part {
          display: flex;
          align-items: center;
          gap: 6px;

          .icon-light {
            color: #409eff;
            font-size: 14px;
            display: flex;
            align-items: center;
          }

          .type-text {
            color: #303133;
            font-size: 13px;
            font-weight: 500;
          }

          .icon-close {
            color: #909399;
            font-size: 12px;
            padding: 2px;
            border-radius: 4px;
            transition: all 0.2s;
            &:hover {
              background-color: #ff4d4f;
              color: #ffffff;
            }
          }
        }
      }
    }

    .footer-right {
      display: flex;
      align-items: center;
      gap: 12px;

      .btn-send,
      .btn-stop {
        height: 32px;
        border-radius: 6px;
      }
    }

    .model-select {
      width: 150px;
      :deep(.el-input__wrapper) {
        background-color: #ffffff;
        box-shadow: none !important;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        padding: 0 10px;
        height: 32px;
        transition: all 0.2s;
        &:hover {
          border-color: #409eff;
        }
      }
      :deep(.el-input__prefix) {
        display: flex;
        align-items: center;
      }
      :deep(.el-input__inner) {
        font-size: 13px;
        color: #606266;
        font-weight: 500;
      }
      .model-icon {
        width: 16px;
        height: 16px;
        margin-right: 4px;
        display: block;
        transform: translateY(2px);
      }
    }

    .chat-type-select {
      width: 130px;
      :deep(.el-input__wrapper) {
        background-color: #ffffff;
        box-shadow: none !important;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        padding: 0 10px;
        height: 32px;
        transition: all 0.2s;
        &:hover {
          border-color: #409eff;
        }
      }
      :deep(.el-input__prefix) {
        display: flex;
        align-items: center;
      }
      :deep(.el-input__inner) {
        font-size: 13px;
        color: #606266;
        font-weight: 500;
      }
      :deep(.chat-type-icon) {
        color: #409eff;
        font-size: 14px;
        display: flex;
        align-items: center;
        margin-right: 4px;
        transform: translateY(1px);
      }
    }
  }
}

:global(.ai-model-select-popper .el-select-dropdown__item) {
  display: flex;
  align-items: center;
}

:global(.ai-model-select-popper .model-option) {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

:global(.ai-model-select-popper .model-option-icon) {
  width: 16px;
  height: 16px;
  flex: 0 0 16px;
  display: block;
  transform: translateY(2px);
}

:global(.ai-model-select-popper .model-option span) {
  line-height: 16px;
}

:global(.ai-chat-type-select-popper .el-select-dropdown__item) {
  display: flex;
  align-items: center;
}

:global(.ai-chat-type-select-popper .chat-type-option) {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

:global(.ai-chat-type-select-popper .chat-type-option-icon) {
  font-size: 14px;
  display: flex;
  align-items: center;
}

:global(.ai-chat-type-select-popper .chat-type-option span) {
  line-height: 16px;
}
</style>
