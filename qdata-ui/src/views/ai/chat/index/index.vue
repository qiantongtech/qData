<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <el-container class="ai-layout">
    <!-- 左侧：对话列表 -->
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
    <!-- 右侧：对话详情 -->
    <el-container class="detail-container app-container" direction="vertical">
      <DataScopeConfig
        v-model:datasourceId="datasourceId"
        v-model:factTableName="factTableName"
        v-model:factTableComment="factTableComment"
        v-model:dimensionTableNames="dimensionTableNames"
        :title="activeConversation?.title || '新对话'"
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
              src="@/assets/ai/组 23248.png"
              class="btn"
              @click="handlerMessageClear"
              alt="清空会话"
            />
            <img
              src="@/assets/ai/组 -1.png"
              class="btn"
              @click="handleGoBottomMessage"
              alt="下"
            />
            <img
              src="@/assets/ai/组 23249.png"
              alt="上"
              class="btn"
              @click="handleGoTopMessage"
            />
          </div>
        </template>
      </DataScopeConfig>

      <!-- main：消息列表 -->
      <el-main class="main-container">
        <div>
          <div class="message-container">
            <!-- 情况三：加载中展示骨架屏 -->
            <MessageLoading v-if="activeMessageListLoading" />
            <!-- 情况四：消息列表为空或无聊天对话时 -->
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
            <!-- 情况五：消息列表不为空 -->
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

      <!-- 底部 -->
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
            placeholder="问问 qData 智能问数...（Shift+Enter 换行，按下 Enter 发送）"
          />
          <div class="prompt-btns">
            <div class="footer-left">
              <el-select
                v-model="selectedModelId"
                placeholder="选择模型"
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
                placeholder="回答方式"
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
                  :label="item.label"
                  :disabled="item.disabled"
                >
                  <template #default>
                    <div class="chat-type-option">
                      <el-icon class="chat-type-option-icon">
                        <component :is="item.icon" />
                      </el-icon>
                      <span>{{ item.label }}</span>
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
                发送
              </el-button>
              <el-button
                type="danger"
                class="btn-stop"
                @click="stopStream()"
                v-if="conversationInProgress == true"
              >
                停止
              </el-button>
            </div>
          </div>
        </form>
        <div class="ai-disclaimer">本功能由 AI 生成，其回答未必正确无误。</div>
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
import defaultModelIcon from "@/assets/ai/gpt-new.svg";
import deepseekIcon from "@/assets/ai/deepseek.svg";
import tongyiIcon from "@/assets/ai/TongYi.svg";
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

/** AI 聊天对话 列表 */
defineOptions({ name: "AiChat" });

const route = useRoute(); // 路由
const { proxy } = getCurrentInstance();
const message = proxy.$modal; // 消息弹窗

// 聊天对话
const conversationListRef = ref();
const dataScopeConfigRef = ref();
const activeConversationId = ref(null); // 选中的对话编号
const activeConversation = ref(null); // 选中的 Conversation
const conversationInProgress = ref(false); // 对话是否正在进行中。目前只有【发送】消息时，会更新为 true，避免切换对话、删除对话等操作

// 消息列表
const messageRef = ref();
const activeMessageList = ref([]); // 选中对话的消息列表
const activeMessageListLoading = ref(false); // activeMessageList 是否正在加载中
const activeMessageListLoadingTimer = ref(); // activeMessageListLoading Timer 定时器。如果加载速度很快，就不进入加载中
const suggestedList = ref([]); // 建议列表

// 模型选择和问答类型
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

/** 获取模型列表 */
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

// 发送消息输入框
const conversationInAbortController = ref(); // 对话进行中 abort 控制器(控制 stream 对话)
const prompt = ref(); // prompt
const enableContext = ref(true); // 是否开启上下文
const datasourceId = ref("");
const factTableName = ref("");
const factTableComment = ref("");
const dimensionTableNames = ref([]);
const dimensionTable = ref("[]");
const tableCommentMap = ref({});
const joinConditionMatchFlag = ref(1); // 0: 需要匹配，1: 不需要匹配

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
  // 可以根据需要添加其他逻辑，比如开始问答的提示等
};

const handleAssociationsConfirm = async () => {
  // 关联关系设置成功后，更新标志位
  joinConditionMatchFlag.value = true;
  console.log("关联关系设置成功");
  // 刷新会话信息
  if (activeConversationId.value) {
    const res = await ChatConversationApi.getChatConversationMy(
      activeConversationId.value
    );
    if (res.data) {
      await handleConversationClick(res.data);
    }
  }
  // 刷新左侧会话列表
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

// =========== 【聊天对话】相关 ===========
/** 获取对话信息 */
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
 * 点击某个对话
 *
 * @param conversation 选中的对话
 * @return 是否切换成功
 */
const handleConversationClick = async (conversation) => {
  // 对话进行中，不允许切换
  if (conversationInProgress.value) {
    message.alert("对话中，不允许切换!");
    return false;
  }

  if (!conversation) {
    return false;
  }

  console.log("handleConversationClick", conversation);
  // 更新选中的对话 id
  activeConversationId.value = conversation.id;
  activeConversation.value = conversation;
  datasourceId.value = conversation.datasourceId || "";
  factTableName.value = conversation.factTableName || "";
  factTableComment.value = conversation.factTableComment || "";
  dimensionTable.value = conversation.dimensionTable || "[]";

  // 解析维表并同步到 tableCommentMap
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
    console.error("解析 dimensionTable 失败:", e);
  }

  dimensionTableNames.value = parseDimensionTableNames(dimensionTable.value);
  joinConditionMatchFlag.value =
    conversation.joinConditionMatchFlag !== undefined
      ? conversation.joinConditionMatchFlag
      : true;

  // 刷新 message 列表
  await getMessageList();
  // 滚动底部
  scrollToBottom(true);
  // 清空输入框
  prompt.value = "";
  return true;
};

/** 删除某个对话*/
const handlerConversationDelete = async (delConversation) => {
  // 删除的对话如果是当前选中的，那么就重置
  if (activeConversationId.value === delConversation.id) {
    await handleConversationClear();
  }
};
/** 清空选中的对话 */
const handleConversationClear = async () => {
  // 对话进行中，不允许切换
  if (conversationInProgress.value) {
    message.alert("对话中，不允许切换!");
    return false;
  }
  activeConversationId.value = null;
  activeConversation.value = null;
  activeMessageList.value = [];
  suggestedList.value = [];
  // 清空配置
  datasourceId.value = "";
  factTableName.value = "";
  factTableComment.value = "";
  dimensionTableNames.value = [];
  dimensionTable.value = "[]";
  tableCommentMap.value = {};
  joinConditionMatchFlag.value = 1;
  prompt.value = "";
};

/** 处理聊天对话的创建成功 */
const handleConversationCreate = async (mId) => {
  // 创建对话
  return await conversationListRef.value.createConversation({
    datasourceId: datasourceId.value,
    factTableName: factTableName.value,
    factTableComment: factTableComment.value,
    dimensionTable: dimensionTable.value,
    modelId: mId || selectedModelId.value,
  });
};
/** 处理聊天对话的创建成功 */
const handleConversationCreateSuccess = async (data) => {
  // 创建新的对话，如果是10001就不清空输入框
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
    // 创建会话返回 10001 时，停止会话并弹出“确认弹窗”
    stopStream();
    dataScopeConfigRef.value.handleOpenAssociationConfirm(
      data.id || activeConversationId.value
    );
  }
};

// =========== 【消息列表】相关 ===========

/** 获取消息 message 列表 */
const getMessageList = async () => {
  try {
    console.log("🚀 消息理你", activeConversationId);
    if (activeConversationId.value === null) {
      return;
    }

    activeMessageListLoading.value = true;
    const messageList = await ChatMessageApi.getChatMessageListByConversationId(
      activeConversationId.value
    );
    // 获取消息列表
    activeMessageList.value = messageList.data;
    activeMessageListLoading.value = false;

    // 滚动到最下面
    await nextTick();
    await scrollToBottom();

    // 获取建议
    // await getSuggested();
  } finally {
    // time 定时器，如果加载速度很快，就不进入加载中
    if (activeMessageListLoadingTimer.value) {
      clearTimeout(activeMessageListLoadingTimer.value);
    }
    // 加载结束
    activeMessageListLoading.value = false;
  }
};

/**
 * 获取建议
 * @returns {Promise<void>}
 */
const getSuggested = async () => {
  // 获取建议 (接口暂缓调用)
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
  // 滚动到最下面
  await nextTick();
  await scrollToBottom();
};

/**
 * 消息列表
 *
 * 和 {@link #getMessageList()} 的差异是，把 systemMessage 考虑进去
 */
const messageList = computed(() => {
  if (activeMessageList.value.length > 0) {
    return activeMessageList.value;
  }
  // 没有消息时，如果有 systemMessage 则展示它
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

/** 处理删除 message 消息 */
const handleMessageDelete = () => {
  if (conversationInProgress.value) {
    message.alert("回答中，不能删除!");
    return;
  }
  // 刷新 message 列表
  getMessageList();
};

/** 处理 message 清空 */
const handlerMessageClear = async () => {
  if (!activeConversationId.value) {
    return;
  }
  try {
    // 确认提示
    await message.confirm("确认清空对话消息？");
    // 清空对话
    await ChatMessageApi.deleteByConversationId(activeConversationId.value);
    // 刷新 message 列表
    activeMessageList.value = [];
  } catch {
    return;
  }
};

/** 回到 message 列表的顶部 */
const handleGoBottomMessage = () => {
  messageRef.value.handleGoBottom();
};

/** 回到 message 列表的顶部 */
const handleGoTopMessage = () => {
  messageRef.value.handlerGoTop();
};

// =========== 【发送消息】相关 ===========

/** 处理来自 keydown 的发送消息 */
const handleSendByKeydown = async (event, mId, cType) => {
  if (event.shiftKey) {
    return; // 如果 Shift 键被按下，不执行发送逻辑
  }
  // 进行中不允许发送
  if (conversationInProgress.value) {
    return;
  }
  const content = prompt.value?.trim();
  // 发送消息
  await doSendMessage(content, mId, cType);
  // event.preventDefault(); //防止默认的换行行为
};

const addNewLine = (event) => {
  // 插入换行
  prompt.value += "\r\n";
  event.preventDefault(); //防止默认的换行行为
};

/** 处理来自【发送】按钮的发送消息 */
const handleSendByButton = () => {
  doSendMessage(prompt.value?.trim());
};

/** 真正执行【发送】消息操作 */
const doSendMessage = async (content, mId, cType) => {
  // 增加一层保护：确保 content 是解开后的纯文本，且如果是递归嵌套也要解开
  const rawContent = getDisplayContent({ content: content });

  // 校验
  if (rawContent.length < 1) {
    message.msgError("发送失败，原因：内容为空！");
    return;
  }

  // 校验数据范围
  if (
    !datasourceId.value ||
    !factTableName.value ||
    !dimensionTableNames.value?.length
  ) {
    message.msgError("请先配置当前数据范围！");
    return;
  }

  // 校验回答方式
  const currentChatType = cType || chatType.value;
  if (!currentChatType) {
    message.msgError("请先选择回答方式（智能问答或智能图表）！");
    return;
  }

  // 校验关联关系
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
    // 首次发送：显示加载状态，用于 MessageListEmpty 展示骨架屏
    activeMessageListLoading.value = true;
    try {
      const createRes = await handleConversationCreate(mId);
      if (createRes && createRes.code === 10001) {
        conversationInProgress.value = false;
        activeMessageListLoading.value = false; // 10001 状态需要恢复展示输入框
        return;
      }
      await getMessageList();
    } catch (e) {
      activeMessageListLoading.value = false;
      throw e;
    }
    // message.msgError('还没创建对话，不能发送!')
    // return
  }
  if (activeMessageList.value.length <= 0 && activeConversation.value) {
    // 名称截取
    activeConversation.value.title = rawContent.substring(0, 10);
  }
  // 清空输入框
  prompt.value = "";
  // 执行发送
  await doSendMessageStream({
    conversationId: activeConversationId.value,
    content: rawContent,
    modelId: mId || selectedModelId.value,
    chatType: cType || chatType.value,
  });
};

/** 真正执行【发送】消息操作 */
const doSendMessageStream = async (userMessage) => {
  // 创建 AbortController 实例，以便中止请求
  conversationInAbortController.value = new AbortController();
  // 标记对话进行中
  conversationInProgress.value = true;

  try {
    // 情况一：本地先展示一条消息
    const newUserMessage = {
      id: null, // 还没返回 id
      type: 1,
      content: userMessage.content,
      createTime: Date.now(),
    };
    activeMessageList.value.push(newUserMessage);
    const newAssistantMessage = {
      id: null, // 还没返回 id
      type: 2,
      content: "loading",
      replyType: userMessage.chatType === "chart" ? 2 : 1,
      createTime: Date.now(),
    };
    activeMessageList.value.push(newAssistantMessage);
    // 滚动底部
    scrollToBottom();

    // 情况二：执行 Stream 发送
    let isFirstChunk = true; // 是否是第一个 chunk 消息段
    await ChatMessageApi.sendChatMessageStream(
      userMessage.conversationId,
      JSON.stringify({ msg: userMessage.content }),
      conversationInAbortController.value,
      enableContext.value,
      userMessage.chatType === "smart"
        ? "1"
        : userMessage.chatType === "chart"
        ? "2"
        : "1", // replyType: 1-知识问答, 2-知识图表
      userMessage.modelId,
      async (res) => {
        const { code, data, msg } = JSON.parse(res.data);
        if (code !== 200) {
          // 不再弹窗报错，改为在消息列表中展示错误提示
          const lastMessage =
            activeMessageList.value[activeMessageList.value.length - 1];
          lastMessage.content = msg || "对话异常";
          lastMessage.isError = true;
          message.msgError(msg);
          // await getMessageList();
          stopStream();
          return;
        }

        const rt = toNumber(data.receive?.replyType);
        // 如果是 reportCard 类型 (replyType === 2)
        if (rt === 2) {
          // 如果 data.send 不为空，说明还在流式传输中，显示加载中
          if (data.send !== null) {
            if (isFirstChunk) {
              isFirstChunk = false;
              // 弹出两个假数据
              activeMessageList.value.pop();
              activeMessageList.value.pop();
              // 添加正式的用户消息和助手的加载中消息
              activeMessageList.value.push(data.send);
              activeMessageList.value.push({
                ...data.receive,
                content: "loading", // 特殊标记，用于前端显示加载中
              });
            }
            return;
          }
          // 如果 data.send 为空，说明传输完成，更新最终数据
          if (data.send === null && data.receive.id !== null) {
            activeMessageList.value[activeMessageList.value.length - 1] =
              data.receive;
            // await getSuggested();
            await scrollToBottom();
            return;
          }
        }

        // 普通文本类型 (replyType !== 2)
        if (data.receive.id !== null) {
          activeMessageList.value[activeMessageList.value.length - 1] =
            data.receive;
          // await getSuggested();
          return;
        }

        // 如果内容为空，就不处理。
        const chunkContent = data.receive.content || data.receive.msg || "";
        if (chunkContent === "") {
          return;
        }
        // 首次返回需要添加一个 message 到页面，后面的都是更新
        if (isFirstChunk) {
          isFirstChunk = false;
          // 弹出两个假数据
          activeMessageList.value.pop();
          activeMessageList.value.pop();
          // 更新返回的数据
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

/** 停止 stream 流式调用 */
const stopStream = async () => {
  // tip：如果 stream 进行中的 message，就需要调用 controller 结束
  if (conversationInAbortController.value) {
    conversationInAbortController.value.abort();
  }
  // 设置为 false
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
  // 递归解析，直到不再是包含 msg 的 JSON 字符串
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

/** 编辑 message：设置为 prompt，可以再次编辑 */
const handleMessageEdit = (message) => {
  prompt.value = getDisplayContent(message);
};

/** 刷新 message：基于指定消息，再次发起对话 */
const handleMessageRefresh = (message) => {
  doSendMessage(getDisplayContent(message));
};

// ============== 【消息滚动】相关 =============

/** 滚动到 message 底部 */
const scrollToBottom = async (isIgnore) => {
  await nextTick();
  if (messageRef.value) {
    messageRef.value.scrollToBottom(isIgnore);
  }
};

// 初始化
onMounted(async () => {
  // 如果有 conversationId 参数，则默认选中
  if (route.query.conversationId) {
    const id = route.query.conversationId;
    activeConversationId.value = id;
    await getConversation(id);
  }

  // 如果没有 activeConversationId，则设置默认的数据范围
  if (!activeConversationId.value) {
    datasourceId.value = defaultDataScope.datasourceId;
    factTableName.value = defaultDataScope.factTableName;
    factTableComment.value = defaultDataScope.factTableComment;
    dimensionTable.value = defaultDataScope.dimensionTable;

    // 解析维表并同步到 tableCommentMap
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
      console.error("解析 dimensionTable 失败:", e);
    }
    dimensionTableNames.value = parseDimensionTableNames(dimensionTable.value);
  }

  // 获取列表数据
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

      // 对话编辑、删除
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

  // 角色仓库、清空未设置对话
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

// 头部
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

// main 容器
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
