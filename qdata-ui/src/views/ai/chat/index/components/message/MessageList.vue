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
  <div ref="messageContainer" class="h-100% overflow-y-auto relative">
    <!-- 10001: The association relationship cannot be automatically recognized. -->
    <div
      class="chat-list"
      v-if="toNumber(conversation.code) === 10001"
      style="margin-bottom: -40px"
    >
      <div class="left-message message-item">
        <div class="avatar">
          <el-avatar
            :src="roleAvatar"
            :size="46"
            style="background-color: transparent"
          />
        </div>
        <div class="message">
          <div>
            <el-text class="time">{{ td('common.message.systemPrompt') }}</el-text>
          </div>
          <div class="left-text-container">
            <div class="left-text">{{ td('ai.chat.relationshipNotIdentified') }}</div>
          </div>
        </div>
      </div>
    </div>
    <template v-for="(item, index) in list" :key="index">
      <div class="chat-list" v-if="!isErrorMessage(item)">
        <!-- Left message: system, assistant type -->
        <div
          class="left-message message-item"
          v-if="toNumber(item.type) === 2 || toNumber(item.type) === 0"
        >
          <div class="avatar">
            <el-avatar
              :src="roleAvatar"
              :size="46"
              style="background-color: transparent"
            />
          </div>
          <div class="message">
            <div style="text-align: left;">
              <el-text class="time">{{ parseTime(item.createTime) }}</el-text>
            </div>
            <div
              :class="[
                isReportCard(item) ? '' : 'left-text-container',
                { 'is-error': item.isError || isErrorMessage(item) },
              ]"
            >
              <AssistantReportCard
                v-if="isReportCard(item)"
                :data="toReportCard(item)"
              />
              <template v-else>
                <div v-if="!getDisplayContent(item)" class="left-text-loading">
                  {{ td('ai.chat.analyzing') }}
                </div>
                <MarkdownView
                  v-else
                  class="left-text"
                  ref="markdownViewRef"
                  :messageId="item.id"
                  :documentNameList="item.documentNameList"
                  :content="getDisplayContent(item)"
                  :documentIdList="item.documentIdList"
                />
              </template>
            </div>
            <div class="left-btns">
              <template v-if="true">
                <el-button class="btn-cus" link @click="copyContent(index)">
                  <img class="btn-image" src="@/assets/images/ai/action-copy.png" />
                </el-button>
                <el-divider direction="vertical" class="btn-divider" />
              </template>
              <el-button
                v-if="item.id > 0"
                class="btn-cus"
                link
                @click="onDelete(item.id)"
              >
                <img class="btn-image h-17px" src="@/assets/images/ai/action-delete.png" />
              </el-button>
            </div>
          </div>
        </div>
        <!-- Right message: user type -->
        <div
          class="right-message message-item"
          v-if="toNumber(item.type) === 1"
        >
          <div class="avatar">
            <el-avatar :src="userAvatar" :size="50" />
          </div>
          <div class="message">
            <div>
              <el-text class="time">{{ parseTime(item.createTime) }}</el-text>
            </div>
            <div class="right-text-container">
              <div class="right-text">{{ getDisplayContent(item) }}</div>
            </div>
            <div class="right-btns">
              <el-button
                style="margin-left: 12px"
                class="btn-cus"
                link
                @click="copyContent(getDisplayContent(item))"
              >
                <img class="btn-image" src="@/assets/images/ai/action-copy.png" />
              </el-button>
              <el-divider direction="vertical" class="btn-divider" />
              <el-button class="btn-cus" link @click="onDelete(item.id)">
                <img
                  class="btn-image h-17px mr-12px"
                  src="@/assets/images/ai/action-delete.png"
                />
              </el-button>
              <el-divider direction="vertical" class="btn-divider" v-if="isLastUserMessage(index)" />
              <el-button class="btn-cus" link @click="onRefresh(item)" v-if="isLastUserMessage(index)">
                <img
                  class="btn-image h-17px mr-12px"
                  src="@/assets/images/ai/action-refresh.png"
                />
              </el-button>
              <el-divider direction="vertical" class="btn-divider" v-if="isLastUserMessage(index)" />
              <el-button class="btn-cus" link @click="onEdit(item)" v-if="isLastUserMessage(index)">
                <img
                  class="btn-image h-17px mr-12px"
                  src="@/assets/images/ai/action-edit.png"
                />
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </template>
    <el-divider
      content-position="center"
      v-show="suggestedList.length > 0"
      border-style="dotted"
      >{{ td('ai.chat.suggestedAsk') }}</el-divider
    >
    <div class="suggested-list" v-show="suggestedList.length > 0">
      <el-check-tag
        @click="handlerSuggested(item + '?')"
        type="info"
        v-for="item in suggestedList"
        >{{ item }}</el-check-tag
      >
    </div>
  </div>
  <!-- back to bottom -->
  <div v-if="isScrolling" class="to-bottom" @click="handleGoBottom">
    <el-button icon="ArrowDownBold" circle />
  </div>
</template>
<script setup>
import MarkdownView from "@/components/MarkdownView/index.vue";
import AssistantReportCard from "./AssistantReportCard.vue";
import { ChatMessageApi } from "@/api/ai/chat/message";
import useUserStore from "@/store/system/user";
import userAvatarDefaultImg from "@/assets/images/avatar-default.png";
import roleAvatarDefaultImg from "@/assets/images/ai/icon-brand-gpt-new.svg";
import { useClipboard } from "@vueuse/core";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const message = proxy.$modal; // Message pop-up window
const userStore = useUserStore();
const { copy } = useClipboard(); // Initialize copy to pasteboard

// Determine the scrolling position of the "Message List" (used to determine whether it is necessary to scroll to the bottom of the message)
const messageContainer = ref(null);
const isScrolling = ref(false); //Used to determine if the user is scrolling
const markdownViewRef = ref(null);

const userAvatar = computed(() => userStore.avatar || userAvatarDefaultImg);
const roleAvatar = computed(
  () => props.conversation.roleAvatar ?? roleAvatarDefaultImg
);

// Define props
const props = defineProps({
  conversation: {
    type: Object,
    required: true,
  },
  list: {
    type: Array,
    required: true,
  },
  suggestedList: {
    type: Array,
    required: true,
  },
});

const { list } = toRefs(props); // Message list

// Determine whether the current message is the last user message
const isLastUserMessage = (currentIndex) => {
  for (let i = list.value.length - 1; i >= 0; i--) {
    if (toNumber(list.value[i].type) === 1) {
      return i === currentIndex;
    }
  }
  return false;
};

const emits = defineEmits([
  "onDeleteSuccess",
  "onRefresh",
  "onEdit",
  "onPrompt",
]); // Definition emits

// ============ Handling dialogue scrolling ==============

/** scroll to bottom */
const scrollToBottom = async (isIgnore) => {
  // Be careful to use nextTick to avoid not getting the dom
  await nextTick();
  if (isIgnore || !isScrolling.value) {
    messageContainer.value.scrollTop =
      messageContainer.value.scrollHeight - messageContainer.value.offsetHeight;
  }
};

function handleScroll() {
  const scrollContainer = messageContainer.value;
  const scrollTop = scrollContainer.scrollTop;
  const scrollHeight = scrollContainer.scrollHeight;
  const offsetHeight = scrollContainer.offsetHeight;
  if (scrollTop + offsetHeight < scrollHeight - 100) {
    // The user starts scrolling and is above the bottom, canceling the effect of staying at the bottom
    isScrolling.value = true;
  } else {
    // The user stops scrolling and scrolls to the bottom, turning on the hold-to-bottom effect
    isScrolling.value = false;
  }
}

/** back to bottom */
const handleGoBottom = () => {
  const scrollContainer = messageContainer.value;
  scrollContainer.scrollTop = scrollContainer.scrollHeight;
};

/** back to top */
const handlerGoTop = () => {
  const scrollContainer = messageContainer.value;
  scrollContainer.scrollTop = 0;
};

defineExpose({ scrollToBottom, handlerGoTop, handleGoBottom }); // Provide methods for parent to call

// ============ Processing message operations ==============

function isStringRobust(value) {
  if (value == null) return false;
  return typeof value === "string" || value instanceof String;
}

function toNumber(v) {
  if (v == null) return v;
  if (typeof v === "number") return v;
  const n = Number(v);
  return Number.isNaN(n) ? v : n;
}

function isReportCard(item) {
  const rt = item?.replyType;
  const num = toNumber(rt);
  // When replyType is 1 or 2, the card style is displayed.
  return num === 1 || num === 2 || item?.content === "loading";
}

function isSmartQA(item) {
  const rt = toNumber(item?.replyType);
  return rt === 1;
}

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

function isErrorMessage(item) {
  if (item?.isError) return true;
  const content = item?.content || "";
  const raw = safeJsonParse(content, null);
  return raw && typeof raw === "object" && raw.code && raw.code !== 200;
}

function toReportCard(item) {
  const content = item?.content || "";
  const rt = toNumber(item?.replyType);
  const isNewMessage = !item.id || item.id <= 0;

  // If it is an error message, return the error summary directly and stop loading.
  if (item.isError) {
    return {
      header: td('ai.chat.insight'),
      summary: getDisplayContent(item) || td('ai.chat.dialogError'),
      tabs: [],
      isLoading: false,
      code: 500,
      conversationId: props.conversation.id,
      messageId: item.id,
    };
  }

  // Handle loading status
  if ((content === "loading" || content === "") && isNewMessage) {
    const tabs = [];
    // If it is a smart chart, the default tab
    if (rt === 2) {
      tabs.push({ key: "viz", label: td('ai.chat.visualization') });
      tabs.push({ key: "detail", label: td('ai.chat.detailData') });
    }
    return {
      header: td('ai.chat.insight'),
      summary: rt === 2 ? td('ai.chat.analyzingData') : td('ai.chat.thinking'),
      isLoading: true,
      tabs: tabs,
    };
  }

  let raw = safeJsonParse(content, null);

  // If parsing fails and content is not empty, plain text may be streaming, or plain text may be returned
  if (!raw && content) {
    raw = { msg: content };
  }

  const header = td('ai.chat.insight');
  const summary = raw?.msg || (toNumber(raw?.code) === 500 ? td('ai.chat.dialogError') : "");
  console.log("🚀 ~ toReportCard ~ raw:", raw);

  // If the return code is 500, stop loading and display an error message
  if (toNumber(raw?.code) === 500) {
    return {
      header,
      summary: summary || td('ai.chat.dialogError'),
      tabs: [],
      isLoading: false,
      code: 500,
      conversationId: props.conversation.id,
      messageId: item.id,
    };
  }

  // If it is an intelligent question and answer (rt === 1) and there is no structured data such as chatData, detailData, sql, etc., summary will be returned directly.
  // In this way, during the streaming output process, MessageList will render text in real time.
  const hasStructuralData =
    (raw?.chatData?.xAxisData?.length > 0 &&
      (raw?.chatData?.yAxisData?.length > 0 ||
        raw?.chatData?.yAxisDataArr?.length > 0)) ||
    raw?.detailData?.list?.length > 0 ||
    raw?.sql;

  // If it is a smart chart (rt === 2) or a smart question and answer with structured data, build Tabs
  const tabs = [];
  if (rt === 2 || hasStructuralData) {
    // If it is a chart and there is no structured data yet, explicitly mark loading
    if (rt === 2 && !hasStructuralData && isNewMessage) {
      tabs.push({ key: "viz", label: td('ai.chat.visualization') });
      tabs.push({ key: "detail", label: td('ai.chat.detailData') });
      return {
        header,
        summary,
        tabs,
        isLoading: true,
        conversationId: props.conversation.id,
        messageId: item.id,
      };
    }

    let xAxisData = raw?.chatData?.xAxisData || [];
    let yAxisData = raw?.chatData?.yAxisData || [];
    let yAxisDataArr = raw?.chatData?.yAxisDataArr || [];
    const dataType = toNumber(raw?.dataType); // 1: Bar chart 2: Line chart 3: Pie chart

    const rows = Array.isArray(raw?.detailData?.list)
      ? raw.detailData.list
      : [];
    let columns = [];
    if (rows.length > 0) {
      const keys = Object.keys(rows[0] || {});
      const labels = Array.isArray(raw?.detailData?.label)
        ? raw.detailData.label
        : [];
      columns = keys.map((k, i) => {
        return { prop: k, label: labels[i] || k };
      });
    } else if (
      Array.isArray(raw?.selectColumn) &&
      raw.selectColumn.length > 0
    ) {
      const labels =
        Array.isArray(raw?.detailData?.label) && raw.detailData.label.length > 0
          ? raw.detailData.label
          : Array.isArray(raw?.selectColumnDescription)
          ? raw.selectColumnDescription
          : [];
      columns = raw.selectColumn.map((k, i) => {
        return { prop: k, label: labels[i] || k };
      });
    }

    // Check whether chatData is valid (if it is all null, it is considered invalid)
    const isChatDataValid = (data) =>
      Array.isArray(data) && data.length > 0 && data.some((v) => v !== null);

    // If chatData is invalid but has detail data, try to extract chart data from the detail data
    if (!isChatDataValid(xAxisData) && rows.length > 0) {
      const keys = Object.keys(rows[0]);
      // Usually the first column is the ID/code, the second column is the name (suitable for the X-axis), and the last column is usually the value (suitable for the Y-axis)
      if (keys.length >= 2) {
        xAxisData = rows.map((row) => row[keys[1]] || row[keys[0]]);
        yAxisData = rows.map((row) => row[keys[keys.length - 1]]);
        yAxisDataArr = []; // Reset multidimensional data
      }
    }

    // Visual tab
    const hasChartData =
      isChatDataValid(xAxisData) &&
      (isChatDataValid(yAxisData) ||
        (yAxisDataArr.length > 0 && yAxisDataArr.some(isChatDataValid)));

    // If it is a smart chart (rt === 2) or a smart question and answer with structured data, build Tabs
    if (rt === 2 || hasChartData) {
      let chartType = "bar";
      if (dataType === 2) chartType = "line";
      if (dataType === 3) chartType = "pie";

      const series = [];
      if (yAxisDataArr.length > 0) {
        yAxisDataArr.forEach((data, index) => {
          series.push({
            name: td('ai.chat.dataLabel', { n: index + 1 }),
            data: data,
          });
        });
      } else {
        series.push({
          name: td('ai.chat.data'),
          data: yAxisData,
        });
      }

      tabs.push({
        key: "viz",
        label: td('ai.chat.visualization'),
        chart: hasChartData
          ? {
              type: chartType,
              xAxis: xAxisData,
              series: series,
            }
          : null,
      });
    }

    // Details tab
    if (rt === 2 || rows.length > 0) {
      tabs.push({
        key: "detail",
        label: td('ai.chat.detailData'),
        table: {
          rows: rows,
          columns: columns,
        },
      });
    }

    // SQL tab
    if (raw?.sql) {
      tabs.push({
        key: "sql",
        label: "Text2SQL",
        code: raw.sql,
      });
    }
  }

  return {
    header,
    summary,
    tabs,
    conversationId: props.conversation.id,
    messageId: item.id,
  };
}

/** Copy */
const copyContent = (index) => {
  if (isStringRobust(index)) {
    copy(index).then(() => {
      message.msgSuccess(td('ai.chat.copySuccess'));
    });
    return;
  }
  const item = list.value[index];
  if (isReportCard(item)) {
    // Intelligent Q&A, charts and other card formats, directly extract msg from content and copy it
    const contentToCopy = getDisplayContent(item);
    if (contentToCopy) {
      copy(contentToCopy).then(() => {
        message.msgSuccess(td('ai.chat.copySuccess'));
      });
    }
    return;
  }
  let count = -1;
  for (let i = 0; i <= index; i++) {
    const it = list.value[i];
    if (
      (toNumber(it.type) === 2 || toNumber(it.type) === 0) &&
      !isReportCard(it)
    ) {
      count++;
    }
  }
  if (count !== -1 && markdownViewRef.value[count]) {
    markdownViewRef.value[count].copyContent();
  }
};

/** Delete */
const onDelete = async (id) => {
  // Confirm
  proxy.$modal.confirm(td('ai.chat.confirmDeleteMessage')).then(async () => {
    // delete message
    await ChatMessageApi.deleteChatMessage(id);
    message.msgSuccess(td('common.message.deleteSuccess'));
    // callback
    emits("onDeleteSuccess");
  });

};

/** Refresh */
const onRefresh = (message) => {
  emits("onRefresh", message);
};

/** Edit */
const onEdit = (message) => {
  emits("onEdit", message);
};

/** try asking */
const handlerSuggested = (item) => {
  emits("onPrompt", item);
};

/** initialization */
onMounted(() => {
  messageContainer.value.addEventListener("scroll", handleScroll);
});
</script>

<style scoped lang="scss">
.message-container {
  position: relative;
  overflow-y: scroll;
}
.h-100\% {
  height: 100%;
}

.overflow-y-auto {
  overflow-y: auto;
}
.relative {
  position: relative;
}

// middle
.chat-list {
  display: flex;
  flex-direction: column;
  overflow-y: hidden;
  padding: 0 20px;
  .message-item {
    margin-top: 50px;
  }

  .left-message {
    display: flex;
    flex-direction: row;
  }

  .right-message {
    display: flex;
    flex-direction: row-reverse;
    justify-content: flex-start;
  }

  .message {
    display: flex;
    flex-direction: column;
    text-align: right;
    margin: 0 15px;

    .time {
      text-align: left;
      line-height: 22px;
      font-weight: normal;
      font-size: 14px;
      color: rgba(0, 0, 0, 0.65);
      font-style: normal;
      text-transform: none;
    }

    .left-text-container {
      position: relative;
      display: flex;
      flex-direction: column;
      overflow-wrap: break-word;
      background-color: #f0f0f6;
      padding: 10px 10px 5px 10px;
      border-radius: 2px;
      margin-top: 8px;
      &.is-error {
        background-color: #fff2f0;
        border: 1px solid #ffccc7;
        .left-text {
          color: #cf1322;
        }
      }
      .left-text {
        color: #707070;
        font-size: 14px;
      }
    }

    .right-text-container {
      display: flex;
      flex-direction: row-reverse;
      margin-top: 8px;

      .right-text {
        display: inline;
        background: #257fff;
        border-radius: 2px;
        padding: 10px;
        width: auto;
        overflow-wrap: break-word;
        white-space: pre-wrap;
        font-weight: 400;
        font-size: 14px;
        color: #ffffff;
        text-align: left;
        font-style: normal;
        text-transform: none;
      }
    }

    .left-btns {
      display: flex;
      flex-direction: row;
      margin-top: 8px;
      align-items: center;
    }

    .right-btns {
      display: flex;
      flex-direction: row-reverse;
      margin-top: 8px;
      align-items: center;
    }
  }

  .btn-divider {
    --el-border-color: #d9d8e8;
    margin: 11px;
  }

  // Copy and delete buttons
  .btn-cus {
    display: flex;
    background-color: transparent;
    align-items: center;

    .btn-image {
      height: 14px;
    }
  }

  .btn-cus:hover {
    cursor: pointer;
    background-color: #f6f6f6;
  }
}

.suggested-list {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.left-text-loading {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
  padding: 10px;
}

// back to bottom
.to-bottom {
  position: absolute;
  z-index: 1000;
  bottom: 0;
  right: 50%;
}
</style>
