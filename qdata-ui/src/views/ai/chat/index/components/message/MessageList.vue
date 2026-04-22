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
  <div ref="messageContainer" class="h-100% overflow-y-auto relative">
    <!-- 10001：关联关系无法自动识别提示 -->
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
            <el-text class="time">系统提示</el-text>
          </div>
          <div class="left-text-container">
            <div class="left-text">关联关系无法自动识别</div>
          </div>
        </div>
      </div>
    </div>
    <template v-for="(item, index) in list" :key="index">
      <div class="chat-list" v-if="!isErrorMessage(item)">
        <!-- 靠左 message：system、assistant 类型 -->
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
            <div>
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
                  问题分析中...
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
                  <img class="btn-image" src="@/assets/ai/copy.png" />
                </el-button>
                <el-divider direction="vertical" class="btn-divider" />
              </template>
              <el-button
                v-if="item.id > 0"
                class="btn-cus"
                link
                @click="onDelete(item.id)"
              >
                <img class="btn-image h-17px" src="@/assets/ai/delete.png" />
              </el-button>
            </div>
          </div>
        </div>
        <!-- 靠右 message：user 类型 -->
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
                <img class="btn-image" src="@/assets/ai/copy.png" />
              </el-button>
              <el-divider direction="vertical" class="btn-divider" />
              <el-button class="btn-cus" link @click="onDelete(item.id)">
                <img
                  class="btn-image h-17px mr-12px"
                  src="@/assets/ai/delete.png"
                />
              </el-button>
              <el-divider direction="vertical" class="btn-divider" />
              <el-button class="btn-cus" link @click="onRefresh(item)">
                <img
                  class="btn-image h-17px mr-12px"
                  src="@/assets/ai/refresh.png"
                />
              </el-button>
              <el-divider direction="vertical" class="btn-divider" />
              <el-button class="btn-cus" link @click="onEdit(item)">
                <img
                  class="btn-image h-17px mr-12px"
                  src="@/assets/ai/edit.png"
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
      >试着问问</el-divider
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
  <!-- 回到底部 -->
  <div v-if="isScrolling" class="to-bottom" @click="handleGoBottom">
    <el-button icon="ArrowDownBold" circle />
  </div>
</template>
<script setup>
import MarkdownView from "@/components/MarkdownView/index.vue";
import AssistantReportCard from "./AssistantReportCard.vue";
import { ChatMessageApi } from "@/api/ai/chat/message";
import useUserStore from "@/store/system/user";
import userAvatarDefaultImg from "@/assets/images/defaultAvatar.png";
import roleAvatarDefaultImg from "@/assets/ai/gpt-new.svg";
import { useClipboard } from "@vueuse/core";

const { proxy } = getCurrentInstance();
const message = proxy.$modal; // 消息弹窗
const userStore = useUserStore();
const { copy } = useClipboard(); // 初始化 copy 到粘贴板

// 判断“消息列表”滚动的位置(用于判断是否需要滚动到消息最下方)
const messageContainer = ref(null);
const isScrolling = ref(false); //用于判断用户是否在滚动
const markdownViewRef = ref(null);

const userAvatar = computed(() => userStore.avatar || userAvatarDefaultImg);
const roleAvatar = computed(
  () => props.conversation.roleAvatar ?? roleAvatarDefaultImg
);

// 定义 props
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

const { list } = toRefs(props); // 消息列表

const emits = defineEmits([
  "onDeleteSuccess",
  "onRefresh",
  "onEdit",
  "onPrompt",
]); // 定义 emits

// ============ 处理对话滚动 ==============

/** 滚动到底部 */
const scrollToBottom = async (isIgnore) => {
  // 注意要使用 nextTick 以免获取不到 dom
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
    // 用户开始滚动并在最底部之上，取消保持在最底部的效果
    isScrolling.value = true;
  } else {
    // 用户停止滚动并滚动到最底部，开启保持到最底部的效果
    isScrolling.value = false;
  }
}

/** 回到底部 */
const handleGoBottom = () => {
  const scrollContainer = messageContainer.value;
  scrollContainer.scrollTop = scrollContainer.scrollHeight;
};

/** 回到顶部 */
const handlerGoTop = () => {
  const scrollContainer = messageContainer.value;
  scrollContainer.scrollTop = 0;
};

defineExpose({ scrollToBottom, handlerGoTop, handleGoBottom }); // 提供方法给 parent 调用

// ============ 处理消息操作 ==============

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
  // replyType 为 1 或 2 时都显示卡片样式
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

  // 如果是错误消息，直接返回错误摘要并停止加载
  if (item.isError) {
    return {
      header: "智能洞察",
      summary: getDisplayContent(item) || "对话异常",
      tabs: [],
      isLoading: false,
      code: 500,
      conversationId: props.conversation.id,
      messageId: item.id,
    };
  }

  // 处理正在加载的状态
  if ((content === "loading" || content === "") && isNewMessage) {
    const tabs = [];
    // 如果是智能图表，预设页签
    if (rt === 2) {
      tabs.push({ key: "viz", label: "可视化" });
      tabs.push({ key: "detail", label: "明细数据" });
    }
    return {
      header: "智能洞察",
      summary: rt === 2 ? "正在分析数据并生成报表，请稍候..." : "正在思考中...",
      isLoading: true,
      tabs: tabs,
    };
  }

  let raw = safeJsonParse(content, null);

  // 如果解析失败且 content 不为空，说明可能正在流式传输纯文本，或者返回的就是纯文本
  if (!raw && content) {
    raw = { msg: content };
  }

  const header = "智能洞察";
  const summary = raw?.msg || (toNumber(raw?.code) === 500 ? "对话异常" : "");
  console.log("🚀 ~ toReportCard ~ raw:", raw);

  // 如果返回 code 为 500，则停止加载并显示错误信息
  if (toNumber(raw?.code) === 500) {
    return {
      header,
      summary: summary || "对话异常",
      tabs: [],
      isLoading: false,
      code: 500,
      conversationId: props.conversation.id,
      messageId: item.id,
    };
  }

  // 如果是智能问答 (rt === 1)，且没有 chatData、detailData、sql 等结构化数据，则直接返回 summary
  // 这样在流式输出过程中，MessageList 会实时渲染文字
  const hasStructuralData =
    (raw?.chatData?.xAxisData?.length > 0 &&
      (raw?.chatData?.yAxisData?.length > 0 ||
        raw?.chatData?.yAxisDataArr?.length > 0)) ||
    raw?.detailData?.list?.length > 0 ||
    raw?.sql;

  // 如果是智能图表 (rt === 2) 或者是带结构化数据的智能问答，则构建 Tabs
  const tabs = [];
  if (rt === 2 || hasStructuralData) {
    // 如果是图表且还没有结构化数据，显式标记加载中
    if (rt === 2 && !hasStructuralData && isNewMessage) {
      tabs.push({ key: "viz", label: "可视化" });
      tabs.push({ key: "detail", label: "明细数据" });
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
    const dataType = toNumber(raw?.dataType); // 1:柱状图 2:折线图 3:饼状图

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

    // 检查 chatData 是否有效（如果全是 null 则视为无效）
    const isChatDataValid = (data) =>
      Array.isArray(data) && data.length > 0 && data.some((v) => v !== null);

    // 如果 chatData 无效但有明细数据，尝试从明细数据中提取图表数据
    if (!isChatDataValid(xAxisData) && rows.length > 0) {
      const keys = Object.keys(rows[0]);
      // 通常第一列是 ID/代码，第二列是名称（适合做 X 轴），最后一列通常是数值（适合做 Y 轴）
      if (keys.length >= 2) {
        xAxisData = rows.map((row) => row[keys[1]] || row[keys[0]]);
        yAxisData = rows.map((row) => row[keys[keys.length - 1]]);
        yAxisDataArr = []; // 重置多维数据
      }
    }

    // 可视化页签
    const hasChartData =
      isChatDataValid(xAxisData) &&
      (isChatDataValid(yAxisData) ||
        (yAxisDataArr.length > 0 && yAxisDataArr.some(isChatDataValid)));

    // 如果是智能图表 (rt === 2) 或者是带结构化数据的智能问答，则构建 Tabs
    if (rt === 2 || hasChartData) {
      let chartType = "bar";
      if (dataType === 2) chartType = "line";
      if (dataType === 3) chartType = "pie";

      const series = [];
      if (yAxisDataArr.length > 0) {
        yAxisDataArr.forEach((data, index) => {
          series.push({
            name: `数据${index + 1}`,
            data: data,
          });
        });
      } else {
        series.push({
          name: "数据",
          data: yAxisData,
        });
      }

      tabs.push({
        key: "viz",
        label: "可视化",
        chart: hasChartData
          ? {
              type: chartType,
              xAxis: xAxisData,
              series: series,
            }
          : null,
      });
    }

    // 详情页签
    if (rt === 2 || rows.length > 0) {
      tabs.push({
        key: "detail",
        label: "明细数据",
        table: {
          rows: rows,
          columns: columns,
        },
      });
    }

    // SQL 页签
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

/** 复制 */
const copyContent = (index) => {
  if (isStringRobust(index)) {
    copy(index).then(() => {
      message.msgSuccess("复制成功！");
    });
    return;
  }
  const item = list.value[index];
  if (isReportCard(item)) {
    // 智能问答、图表等卡片形式，直接从 content 提取 msg 进行复制
    const contentToCopy = getDisplayContent(item);
    if (contentToCopy) {
      copy(contentToCopy).then(() => {
        message.msgSuccess("复制成功！");
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

/** 删除 */
const onDelete = async (id) => {
  // 删除 message
  await ChatMessageApi.deleteChatMessage(id);
  message.msgSuccess("删除成功！");
  // 回调
  emits("onDeleteSuccess");
};

/** 刷新 */
const onRefresh = (message) => {
  emits("onRefresh", message);
};

/** 编辑 */
const onEdit = (message) => {
  emits("onEdit", message);
};

/** 尝试问问 */
const handlerSuggested = (item) => {
  emits("onPrompt", item);
};

/** 初始化 */
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

// 中间
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

  // 复制、删除按钮
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

// 回到底部
.to-bottom {
  position: absolute;
  z-index: 1000;
  bottom: 0;
  right: 50%;
}
</style>
