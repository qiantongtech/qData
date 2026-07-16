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

<!-- qData intelligent data Q&A conversation -->
<template>
  <el-aside width="260px" class="conversation-container h-100%">
    <!-- Top left: Conversation -->
    <div class="h-100%">
      <el-button
        class="w-1/1 btn-new-conversation"
        type="primary"
        @click="handleNewButtonClick"
      >
        <el-icon class="icon-plus" :size="14"><Plus /></el-icon>
        <span class="btn-text">{{ td('ai.chat.newConversation') }}</span>
      </el-button>

      <!-- Top left: Search conversations -->
      <el-input
        v-model="searchName"
        size="large"
        class="mt-10px search-input"
        :placeholder="td('ai.chat.searchHistory')"
        @keyup="searchConversation"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>

      <!-- Middle left: Conversation list -->
      <div
        class="conversation-list"
        v-loading="loading && conversationList.length === 0"
      >
        <!-- Situation 1: The empty status is displayed only when loading and there is no data. -->
        <el-empty
          v-if="!loading && conversationList.length === 0"
          :description="td('common.noHistory')"
        />
        <!-- Scenario 2: Group by group and display the chat session list -->
        <div
          v-for="conversationKey in Object.keys(conversationMap)"
          :key="conversationKey"
        >
          <div
            class="conversation-item classify-title"
            v-if="conversationMap[conversationKey].length"
          >
            <el-text class="mx-1" size="small" tag="b">{{
              groupLabels[conversationKey] || conversationKey
            }}</el-text>
          </div>
          <div
            class="conversation-item"
            v-for="conversation in conversationMap[conversationKey]"
            :key="conversation.id"
            @click="handleConversationClick(conversation.id)"
            @mouseover="hoverConversationId = conversation.id"
            @mouseout="hoverConversationId = ''"
          >
            <div
              :class="
                conversation.id === activeConversationId
                  ? 'conversation active'
                  : 'conversation'
              "
            >
              <div class="title-wrapper">
                <img
                  class="avatar"
                  :src="
                    conversation.id === activeConversationId
                      ? roleAvatartActiveImg
                      : roleAvatarDefaultImg
                  "
                />
                <span class="title">{{ conversation.title }}</span>
              </div>
              <div
                class="button-wrapper"
                v-show="hoverConversationId === conversation.id"
              >
                <el-button
                  class="btn"
                  link
                  @click.stop="handleTop(conversation)"
                >
                  <img
                    height="14"
                    src="@/assets/images/ai/img-topc.png"
                    :alt="td('ai.chat.pinTop')"
                    v-if="
                      !conversation.pinned &&
                      conversation.id === activeConversationId
                    "
                  />
                  <img
                    height="14"
                    src="@/assets/images/ai/img-top.png"
                    :alt="td('ai.chat.pinTop')"
                    v-else-if="!conversation.pinned"
                  />
                  <img
                    height="14"
                    src="@/assets/images/ai/img-bottomc.png"
                    :alt="td('ai.chat.unpinTop')"
                    v-if="
                      conversation.pinned &&
                      conversation.id === activeConversationId
                    "
                  />
                  <img
                    height="14"
                    src="@/assets/images/ai/img-bottom.png"
                    :alt="td('ai.chat.unpinTop')"
                    v-else-if="conversation.pinned"
                  />
                </el-button>
                <el-button
                  class="btn"
                  link
                  @click.stop="updateConversationTitle(conversation)"
                >
                  <img
                    height="14"
                    src="@/assets/images/ai/action-editc.png"
                    v-if="conversation.id === activeConversationId"
                  />
                  <img height="14" src="@/assets/images/ai/action-edit.png" v-else />
                </el-button>
                <el-button
                  class="btn"
                  link
                  @click.stop="deleteChatConversation(conversation)"
                >
                  <img
                    height="14"
                    src="@/assets/images/ai/action-deletec.png"
                    v-if="conversation.id === activeConversationId"
                  />
                  <img height="14" src="@/assets/images/ai/action-delete.png" v-else />
                </el-button>
              </div>
            </div>
          </div>
        </div>
        <!-- Bottom placeholder  -->
        <div class="h-160px w-100%"></div>
      </div>
    </div>
  </el-aside>
  <el-dialog
    v-model="renameDialogVisible"
    :title="td('ai.chat.renameTitle')"
    width="600px"
    :append-to="dialogAppendTo"
    :close-on-click-modal="false"
    :show-close="!renameLoading"
    @closed="handleRenameDialogClosed"
  >
    <template #header="{ titleId }">
      <span
        :id="titleId"
        role="heading"
        aria-level="2"
        class="el-dialog__title"
      >
        {{ td('ai.chat.renameTitle') }}
      </span>
    </template>
    <el-form label-width="60px" v-loading="renameLoading" :label-position="labelPosition">
      <el-form-item :label="td('ai.chat.titleLabel')" :label-position="labelPosition">
        <el-input
          v-model="renameTitle"
          :placeholder="td('ai.chat.titlePlaceholder')"
          show-word-limit
          @keyup.enter="handleRenameConfirm"
          :disabled="renameLoading"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="renameDialogVisible = false" :disabled="renameLoading"
        >{{ td('common.button.cancel') }}</el-button
      >
      <el-button
        type="primary"
        @click="handleRenameConfirm"
        :loading="renameLoading"
      >
        {{ td('common.button.confirm') }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ChatConversationApi } from "@/api/ai/chat/conversation";
import roleAvatarDefaultImg from "@/assets/images/ai/icon-brand-gpt-new.svg";
import roleAvatartActiveImg from "@/assets/images/ai/icon-brand-gpt-new.svg";
import useUserStore from "@/store/system/user";
import moment from "moment/moment.js";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const message = proxy.$modal; // Message pop-up window
const userStore = useUserStore();

// Define properties
const searchName = ref(""); // Conversation search
const activeConversationId = ref(null); // The selected conversation, defaults to null
const hoverConversationId = ref(null); // floating conversation
const conversationList = ref([]); // Conversation list
const conversationMap = ref({}); // Conversation grouping (top, today, three days ago, one week ago, one month ago)
const loading = ref(false); // Loading
const loadingTime = ref(); // Loading timer
const renameDialogVisible = ref(false);
const renameConversationId = ref("");
const renameTitle = ref("");
const renameLoading = ref(false);
const dialogAppendTo = ref(document.body);

// Conversation time grouping labels
const groupLabels = {
  pinned: td('ai.chat.pinned', '置顶'),
  today: td('ai.chat.today', '今天'),
  oneDayAgo: td('ai.chat.oneDayAgo', '一天前'),
  threeDaysAgo: td('ai.chat.threeDaysAgo', '三天前'),
  sevenDaysAgo: td('ai.chat.sevenDaysAgo', '七天前'),
  thirtyDaysAgo: td('ai.chat.thirtyDaysAgo', '三十天前'),
};

// Define component props
const props = defineProps({
  activeId: {
    type: [String, Number, null],
    required: true,
  },
  datasourceId: {
    type: String,
    required: false,
    default: "",
  },
  factTableName: {
    type: String,
    required: false,
    default: "",
  },
  factTableComment: {
    type: String,
    required: false,
    default: "",
  },
  dimensionTable: {
    type: String,
    required: false,
    default: "[]",
  },
});

// Define hook
const emits = defineEmits([
  "onConversationCreate",
  "onConversationClick",
  "onConversationClear",
  "onConversationDelete",
]);

/** Search conversations */
const searchConversation = async () => {
  // Recover data
  if (!searchName.value.trim().length) {
    conversationMap.value = await getConversationGroupByCreateTime(
      conversationList.value
    );
  } else {
    // filter
    const filterValues = conversationList.value.filter((item) => {
      return item.title.includes(searchName.value.trim());
    });
    conversationMap.value = await getConversationGroupByCreateTime(
      filterValues
    );
  }
};

/** Click to talk */
const handleConversationClick = async (id) => {
  if (!id) {
    return;
  }
  // Filter selected conversations
  let filterConversation = conversationList.value.filter((item) => {
    return item.id === id;
  });

  // If it is not found in the list (it may be that the newly created one has not been refreshed yet), then directly check the details.
  if (filterConversation.length === 0) {
    const res = await ChatConversationApi.getChatConversationMy(id);
    if (res.data) {
      filterConversation = [res.data];
    }
  }

  // callback onConversationClick
  // noinspection JSVoidFunctionReturnValueUsed
  emits("onConversationClick", filterConversation[0]);
  // Switch conversation
  activeConversationId.value = id;
};

/** Get conversation list */
const getChatConversationList = async () => {
  try {
    // 1. If there is no data, turn on the loading state
    if (conversationList.value.length === 0) {
      loadingTime.value = setTimeout(() => {
        loading.value = true;
      }, 50);
    }

    // 2.1 Obtain conversation data
    let myData = await ChatConversationApi.getChatConversationMyList();
    const newData = myData.data || [];

    // 2.2 Sorting
    newData.sort((a, b) => {
      // Convert time string to timestamp for comparison
      const timeA = new Date(a.createTime).getTime();
      const timeB = new Date(b.createTime).getTime();
      return timeB - timeA; // Sort in descending order (newest first)
    });

    // 3. Group conversations according to time (calculate first and then assign values to avoid flickering caused by frequent view updates)
    const newMap = await getConversationGroupByCreateTime(newData);

    // 4. Update responsive data (centralized update)
    conversationList.value = newData;
    conversationMap.value = newMap;

    // 5. No dialogue situations are handled
    if (newData.length === 0) {
      activeConversationId.value = null;
    }
  } finally {
    // Cleanup timer
    if (loadingTime.value) {
      clearTimeout(loadingTime.value);
    }
    // Loading completed
    loading.value = false;
  }
};

/** Group according to creteTime creation time */
const getConversationGroupByCreateTime = async (list) => {
  // Sorting, specifying, time grouping (today, one day ago, three days ago, seven days ago, 30 days ago)
  // noinspection NonAsciiCharacters
  const groupMap = {
    pinned: [],
    today: [],
    oneDayAgo: [],
    threeDaysAgo: [],
    sevenDaysAgo: [],
    thirtyDaysAgo: [],
  };
  // timestamp of current time
  const now = Date.now();
  // Define time interval constant (unit: milliseconds)
  const oneDay = 24 * 60 * 60 * 1000;
  const threeDays = 3 * oneDay;
  const sevenDays = 7 * oneDay;
  const thirtyDays = 30 * oneDay;
  for (const conversation of list) {
    // pin to top
    if (conversation.pinned) {
      groupMap.pinned.push(conversation);
      continue;
    }
    // Calculate time difference (unit: milliseconds)
    const diff = now - Date.parse(conversation.createTime);
    // Determine based on time interval
    if (diff < oneDay) {
      groupMap.today.push(conversation);
    } else if (diff < threeDays) {
      groupMap.oneDayAgo.push(conversation);
    } else if (diff < sevenDays) {
      groupMap.threeDaysAgo.push(conversation);
    } else if (diff < thirtyDays) {
      groupMap.sevenDaysAgo.push(conversation);
    } else {
      groupMap.thirtyDaysAgo.push(conversation);
    }
  }
  return groupMap;
};

/** New conversation (UI triggered) */
const handleNewButtonClick = () => {
  emits("onConversationClear");
};

/** New conversation */
const createConversation = async (data) => {
  // 1. Create a new conversation
  const result = await ChatConversationApi.createChatConversationMy({
    userId: userStore.id,
    datasourceId: data?.datasourceId,
    factTableName: data?.factTableName,
    factTableComment: data?.factTableComment,
    dimensionTable: data?.dimensionTable,
    modelId: data?.modelId,
  });
  const { id: conversationId, code } = result.data;
  // 2. Get the conversation content
  await getChatConversationList();
  // 3. Select the conversation
  await handleConversationClick(conversationId);
  // 4. Callback
  emits("onConversationCreate", { ...result.data, id: conversationId, code });
  return result.data;
};

/** Change the title of the conversation */
const updateConversationTitle = (conversation) => {
  if (!conversation?.id) {
    return;
  }
  renameConversationId.value = conversation.id;
  renameTitle.value = conversation.title || "";
  renameDialogVisible.value = true;
};

const handleRenameConfirm = async () => {
  if (renameLoading.value) {
    return;
  }
  const title = renameTitle.value.trim();
  if (!title.length) {
    message.msgError(td('ai.chat.titleRequired'));
    return;
  }
  const id = renameConversationId.value;
  if (!id) {
    renameDialogVisible.value = false;
    return;
  }
  try {
    renameLoading.value = true;
    await ChatConversationApi.updateChatConversationMy({
      id,
      title,
    });
    message.msgSuccess(td('ai.chat.renameSuccess'));
    await getChatConversationList();
    const updatedConversation = conversationList.value.find(
      (item) => item.id === id
    );
    if (
      updatedConversation &&
      activeConversationId.value === updatedConversation.id
    ) {
      emits("onConversationClick", updatedConversation);
    }
    renameDialogVisible.value = false;
  } finally {
    renameLoading.value = false;
  }
};

const handleRenameDialogClosed = () => {
  renameConversationId.value = "";
  renameTitle.value = "";
};

/** Delete chat conversation */
const deleteChatConversation = async (conversation) => {
  if (!conversation?.id) {
    return;
  }
  try {
    // Secondary confirmation of deletion
    await message.confirm(td('ai.chat.confirmDeleteConversation', { title: conversation.title }));
    // Initiate deletion
    await ChatConversationApi.deleteChatConversationMy(conversation.id);
    message.msgSuccess(td('ai.chat.conversationDeleted'));
    // Refresh list
    await getChatConversationList();
    // callback
    emits("onConversationDelete", conversation);
  } catch {
    return;
  }
};

/** Conversation pinned */
const handleTop = async (conversation) => {
  if (!conversation?.id) {
    return;
  }
  // Update conversation on top
  conversation.pinned = !conversation.pinned;
  conversation.pinnedTime = moment().format("YYYY-MM-DD HH:mm:ss");
  await ChatConversationApi.updateChatConversationMy({
    id: conversation.id,
    pinned: conversation.pinned,
    pinnedTime: conversation.pinnedTime,
  });
  // Refresh conversation
  await getChatConversationList();
};

// ============ Character Warehouse ============

/** Listen to selected conversations */
const {
  activeId,
  datasourceId,
  factTableName,
  factTableComment,
  dimensionTable,
} = toRefs(props);
watch(activeId, async (newValue) => {
  activeConversationId.value = newValue;
});

// Define public methods
defineExpose({ createConversation, getChatConversationList });

/** initialization */
onMounted(async () => {
  dialogAppendTo.value =
    document.querySelector(".app-container") || document.body;
  // Get conversation list
  await getChatConversationList();
  // Selected by default
  if (props.activeId) {
    activeConversationId.value = props.activeId;
  }
});
</script>

<style scoped lang="scss">
.h-100\% {
  height: 100%;
}
.conversation-container {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 23px 10px 0;

  overflow: hidden;
  background-color: #f5f7fb;

  .btn-new-conversation {
    width: 100%;
    height: 42px;
    background: #3367fc;
    border: #3367fc;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 2px;
    .icon-plus {
      width: 14px;
      height: 14px;
      margin-right: 3px;
      display: flex;
      align-items: center;
      justify-content: center;

      :deep(svg) {
        color: #ffffff !important;
        fill: #ffffff !important;
        stroke: #ffffff !important;
        stroke-width: 60px;
      }
    }
  }

  .search-input {
    margin-top: 15px;
    height: 36px;

    :deep(.el-select__wrapper),
    :deep(.el-input__wrapper) {
      border-radius: 2px;
    }
  }
  .conversation-list {
    overflow: auto;
    height: 100%;

    .classify-title {
      padding-top: 10px;
      .mx-1 {
        font-family: MicrosoftYaHei, MicrosoftYaHei;
        font-weight: normal;
        font-size: 16px;
        color: #333333;
      }
    }

    .conversation-item {
      margin-top: 5px;
      height: 36px;
    }

    .conversation {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      flex: 1;
      padding: 0 5px;
      cursor: pointer;
      border-radius: 2px;
      align-items: center;

      &.active {
        background-color: #257fff;
        .title {
          color: #ffffff;
        }
        .button-wrapper i {
          color: #ffffff;
        }
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
        padding: 2px 10px;
        max-width: 144px;
        font-size: 16px;
        color: #333333;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }

      .avatar {
        width: 29px;
        height: 29px;
        border-radius: 2px;
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
        .btn {
          margin: 0;
        }
      }
    }
  }

  // Character warehouse, clear unset dialogues
  .tool-box {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    //width: 100%;
    padding: 0 20px;
    background-color: #f4f4f4;
    box-shadow: 0 0 1px 1px rgba(228, 228, 228, 0.8);
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
</style>
