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
<!--  AI 对话  -->
<template>
  <el-aside width="260px" class="conversation-container h-100%">
    <!-- 左顶部：对话 -->
    <div class="h-100%">
      <el-button
        class="w-1/1 btn-new-conversation"
        type="primary"
        @click="handleNewButtonClick"
      >
        <el-icon style="margin-right: 13px"><Plus /></el-icon>
        新建对话
      </el-button>

      <!-- 左顶部：搜索对话 -->
      <el-input
        v-model="searchName"
        size="large"
        class="mt-10px search-input"
        placeholder="搜索历史记录"
        @keyup="searchConversation"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>

      <!-- 左中间：对话列表 -->
      <div
        class="conversation-list"
        v-loading="loading && conversationList.length === 0"
      >
        <!-- 情况一：加载中且没有任何数据时才显示空状态 -->
        <el-empty
          v-if="!loading && conversationList.length === 0"
          description="暂无历史记录"
        />
        <!-- 情况二：按照 group 分组，展示聊天会话 list 列表 -->
        <div
          v-for="conversationKey in Object.keys(conversationMap)"
          :key="conversationKey"
        >
          <div
            class="conversation-item classify-title"
            v-if="conversationMap[conversationKey].length"
          >
            <el-text class="mx-1" size="small" tag="b">{{
              conversationKey
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
                    src="@/assets/ai/topC.png"
                    alt="置顶"
                    v-if="
                      !conversation.pinned &&
                      conversation.id === activeConversationId
                    "
                  />
                  <img
                    height="14"
                    src="@/assets/ai/top.png"
                    alt="置顶"
                    v-else-if="!conversation.pinned"
                  />
                  <img
                    height="14"
                    src="@/assets/ai/bottomC.png"
                    alt="取消置顶"
                    v-if="
                      conversation.pinned &&
                      conversation.id === activeConversationId
                    "
                  />
                  <img
                    height="14"
                    src="@/assets/ai/bottom.png"
                    alt="取消置顶"
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
                    src="@/assets/ai/editC.png"
                    v-if="conversation.id === activeConversationId"
                  />
                  <img height="14" src="@/assets/ai/edit.png" v-else />
                </el-button>
                <el-button
                  class="btn"
                  link
                  @click.stop="deleteChatConversation(conversation)"
                >
                  <img
                    height="14"
                    src="@/assets/ai/deleteC.png"
                    v-if="conversation.id === activeConversationId"
                  />
                  <img height="14" src="@/assets/ai/delete.png" v-else />
                </el-button>
              </div>
            </div>
          </div>
        </div>
        <!-- 底部占位  -->
        <div class="h-160px w-100%"></div>
      </div>
    </div>
  </el-aside>
  <el-dialog
    v-model="renameDialogVisible"
    title="修改标题"
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
        修改标题
      </span>
    </template>
    <el-form label-width="60px" v-loading="renameLoading">
      <el-form-item label="标题">
        <el-input
          v-model="renameTitle"
          placeholder="请输入标题"
          show-word-limit
          @keyup.enter="handleRenameConfirm"
          :disabled="renameLoading"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="renameDialogVisible = false" :disabled="renameLoading"
        >取消</el-button
      >
      <el-button
        type="primary"
        @click="handleRenameConfirm"
        :loading="renameLoading"
      >
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ChatConversationApi } from "@/api/ai/chat/conversation";
import roleAvatarDefaultImg from "@/assets/ai/gpt-new.svg";
import roleAvatartActiveImg from "@/assets/ai/gpt-new.svg";
import useUserStore from "@/store/system/user";
import moment from "moment/moment.js";

const { proxy } = getCurrentInstance();
const message = proxy.$modal; // 消息弹窗
const userStore = useUserStore();

// 定义属性
const searchName = ref(""); // 对话搜索
const activeConversationId = ref(null); // 选中的对话，默认为 null
const hoverConversationId = ref(null); // 悬浮上去的对话
const conversationList = ref([]); // 对话列表
const conversationMap = ref({}); // 对话分组 (置顶、今天、三天前、一星期前、一个月前)
const loading = ref(false); // 加载中
const loadingTime = ref(); // 加载中定时器
const renameDialogVisible = ref(false);
const renameConversationId = ref("");
const renameTitle = ref("");
const renameLoading = ref(false);
const dialogAppendTo = ref(document.body);

// 定义组件 props
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

// 定义钩子
const emits = defineEmits([
  "onConversationCreate",
  "onConversationClick",
  "onConversationClear",
  "onConversationDelete",
]);

/** 搜索对话 */
const searchConversation = async () => {
  // 恢复数据
  if (!searchName.value.trim().length) {
    conversationMap.value = await getConversationGroupByCreateTime(
      conversationList.value
    );
  } else {
    // 过滤
    const filterValues = conversationList.value.filter((item) => {
      return item.title.includes(searchName.value.trim());
    });
    conversationMap.value = await getConversationGroupByCreateTime(
      filterValues
    );
  }
};

/** 点击对话 */
const handleConversationClick = async (id) => {
  if (!id) {
    return;
  }
  // 过滤出选中的对话
  let filterConversation = conversationList.value.filter((item) => {
    return item.id === id;
  });

  // 如果在列表中没找到（可能是新创建的还没刷出来），则直接查详情
  if (filterConversation.length === 0) {
    const res = await ChatConversationApi.getChatConversationMy(id);
    if (res.data) {
      filterConversation = [res.data];
    }
  }

  // 回调 onConversationClick
  // noinspection JSVoidFunctionReturnValueUsed
  emits("onConversationClick", filterConversation[0]);
  // 切换对话
  activeConversationId.value = id;
};

/** 获取对话列表 */
const getChatConversationList = async () => {
  try {
    // 1. 如果没有数据，开启加载中状态
    if (conversationList.value.length === 0) {
      loadingTime.value = setTimeout(() => {
        loading.value = true;
      }, 50);
    }

    // 2.1 获取 对话数据
    let myData = await ChatConversationApi.getChatConversationMyList();
    const newData = myData.data || [];

    // 2.2 排序
    newData.sort((a, b) => {
      // 将时间字符串转换为时间戳进行比较
      const timeA = new Date(a.createTime).getTime();
      const timeB = new Date(b.createTime).getTime();
      return timeB - timeA; // 降序排列（最新的在前）
    });

    // 3. 对话根据时间分组 (先计算好再赋值，避免视图频繁更新导致的闪烁)
    const newMap = await getConversationGroupByCreateTime(newData);

    // 4. 更新响应式数据 (集中更新)
    conversationList.value = newData;
    conversationMap.value = newMap;

    // 5. 没有任何对话情况处理
    if (newData.length === 0) {
      activeConversationId.value = null;
    }
  } finally {
    // 清理定时器
    if (loadingTime.value) {
      clearTimeout(loadingTime.value);
    }
    // 加载完成
    loading.value = false;
  }
};

/** 按照 creteTime 创建时间，进行分组 */
const getConversationGroupByCreateTime = async (list) => {
  // 排序、指定、时间分组(今天、一天前、三天前、七天前、30天前)
  // noinspection NonAsciiCharacters
  const groupMap = {
    置顶: [],
    今天: [],
    一天前: [],
    三天前: [],
    七天前: [],
    三十天前: [],
  };
  // 当前时间的时间戳
  const now = Date.now();
  // 定义时间间隔常量（单位：毫秒）
  const oneDay = 24 * 60 * 60 * 1000;
  const threeDays = 3 * oneDay;
  const sevenDays = 7 * oneDay;
  const thirtyDays = 30 * oneDay;
  for (const conversation of list) {
    // 置顶
    if (conversation.pinned) {
      groupMap["置顶"].push(conversation);
      continue;
    }
    // 计算时间差（单位：毫秒）
    const diff = now - Date.parse(conversation.createTime);
    // 根据时间间隔判断
    if (diff < oneDay) {
      groupMap["今天"].push(conversation);
    } else if (diff < threeDays) {
      groupMap["一天前"].push(conversation);
    } else if (diff < sevenDays) {
      groupMap["三天前"].push(conversation);
    } else if (diff < thirtyDays) {
      groupMap["七天前"].push(conversation);
    } else {
      groupMap["三十天前"].push(conversation);
    }
  }
  return groupMap;
};

/** 新建对话 (UI 触发) */
const handleNewButtonClick = () => {
  emits("onConversationClear");
};

/** 新建对话 */
const createConversation = async (data) => {
  // 1. 新建对话
  const result = await ChatConversationApi.createChatConversationMy({
    userId: userStore.id,
    datasourceId: data?.datasourceId,
    factTableName: data?.factTableName,
    factTableComment: data?.factTableComment,
    dimensionTable: data?.dimensionTable,
    modelId: data?.modelId,
  });
  const { id: conversationId, code } = result.data;
  // 2. 获取对话内容
  await getChatConversationList();
  // 3. 选中对话
  await handleConversationClick(conversationId);
  // 4. 回调
  emits("onConversationCreate", { ...result.data, id: conversationId, code });
  return result.data;
};

/** 修改对话的标题 */
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
    message.msgError("标题不能为空");
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
    message.msgSuccess("重命名成功");
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

/** 删除聊天对话 */
const deleteChatConversation = async (conversation) => {
  if (!conversation?.id) {
    return;
  }
  try {
    // 删除的二次确认
    await message.confirm(`是否确认删除对话 - ${conversation.title}?`);
    // 发起删除
    await ChatConversationApi.deleteChatConversationMy(conversation.id);
    message.msgSuccess("对话已删除");
    // 刷新列表
    await getChatConversationList();
    // 回调
    emits("onConversationDelete", conversation);
  } catch {
    return;
  }
};

/** 对话置顶 */
const handleTop = async (conversation) => {
  if (!conversation?.id) {
    return;
  }
  // 更新对话置顶
  conversation.pinned = !conversation.pinned;
  conversation.pinnedTime = moment().format("YYYY-MM-DD HH:mm:ss");
  await ChatConversationApi.updateChatConversationMy({
    id: conversation.id,
    pinned: conversation.pinned,
    pinnedTime: conversation.pinnedTime,
  });
  // 刷新对话
  await getChatConversationList();
};

// ============ 角色仓库 ============

/** 监听选中的对话 */
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

// 定义 public 方法
defineExpose({ createConversation, getChatConversationList });

/** 初始化 */
onMounted(async () => {
  dialogAppendTo.value =
    document.querySelector(".app-container") || document.body;
  // 获取 对话列表
  await getChatConversationList();
  // 默认选中
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
  padding: 10px 10px 0;
  overflow: hidden;
  background-color: #edf5fe;

  .btn-new-conversation {
    width: 100%;
    //padding: 18px 0;
    height: 36px;
    background: #257fff;
    border: #257fff;
  }

  .search-input {
    margin-top: 15px;
    height: 36px;
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

      // 对话编辑、删除
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

  // 角色仓库、清空未设置对话
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
