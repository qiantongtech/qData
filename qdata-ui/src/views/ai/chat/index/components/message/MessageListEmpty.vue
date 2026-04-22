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
<!-- 消息列表为空时，展示 prompt 列表 -->
<template>
  <div class="chat-empty">
    <!-- title -->
    <div class="center-container">
      <div class="title">
        <img src="@/assets/ai/gpt-new.svg" width="44px" />
        <span>Hello，我是 qData 智能助手，很高兴见到你!</span>
      </div>
      <div class="subheading">智启未来，一触即发</div>
      <el-footer class="footer-container">
        <form class="prompt-from">
          <el-input
            type="textarea"
            :autosize="{ minRows: 3 }"
            class="prompt-input"
            v-model="value"
            @keydown.enter.native="handleSendByKeydown"
            @keydown.shift.enter.native="addNewLine"
            placeholder="问问 qData 智能助手...（Shift+Enter 换行，按下 Enter 发送）"
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
              <img
                src="@/assets/ai/send.png"
                class="btn-send"
                @click="handlerPromptClick"
              />
            </div>
          </div>
        </form>
      </el-footer>
    </div>
    <div class="ai-disclaimer">
      qData 智能助手 是一款 AI 工具，其回答未必正确无误。
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted, getCurrentInstance } from "vue";
import { CHAT_TYPES } from "../../constants";
import defaultModelIcon from "@/assets/ai/gpt-new.svg";
import deepseekIcon from "@/assets/ai/deepseek.svg";
import tongyiIcon from "@/assets/ai/TongYi.svg";
import { Plus } from "@element-plus/icons-vue";

const emits = defineEmits([
  "onPrompt",
  "enter",
  "shift-enter",
  "update:modelValue",
  "update:selectedModelId",
  "update:chatType",
]);
const props = defineProps({
  modelValue: String,
  datasourceId: [String, Number],
  factTableName: String,
  dimensionTableNames: Array,
  modelList: {
    type: Array,
    default: () => [],
  },
  selectedModelId: [String, Number],
  chatType: String,
});

const { proxy } = getCurrentInstance();
const message = proxy.$modal; // 消息弹窗

const value = computed({
  get: () => props.modelValue,
  set: (val) => emits("update:modelValue", val),
});

const selectedModelId = computed({
  get: () => props.selectedModelId,
  set: (val) => emits("update:selectedModelId", val),
});

const chatType = computed({
  get: () => props.chatType,
  set: (val) => emits("update:chatType", val),
});

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
    props.modelList.find((m) => m?.id === id) ||
    props.modelList.find((m) => String(m?.id) === String(id)) ||
    null
  );
});

const selectedModelIcon = computed(() =>
  getModelIconByPlatform(selectedModel.value?.platform)
);

onMounted(() => {
  // getModelList(); // 不再在内部获取模型列表，而是通过 props 传入
});

/** 选中 prompt 点击 */
const handlerPromptClick = async () => {
  if (!props.datasourceId || !props.factTableName) {
    message.msgWarning("请先配置当前数据范围！");
    return;
  }
  emits("onPrompt", value.value, selectedModelId.value, chatType.value);
};

const handleSendByKeydown = async (event) => {
  if (!props.datasourceId || !props.factTableName) {
    message.msgWarning("请先配置当前数据范围！");
    return;
  }
  emits("enter", event, selectedModelId.value, chatType.value);
};

const addNewLine = async (event) => {
  emits("shift-enter", event);
};
</script>
<style scoped lang="scss">
.chat-empty {
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: center;
  width: 100%;
  height: 100%;

  .center-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .title {
      text-align: center;
      font-weight: 600;
      font-size: 22px;
      color: #000000;
      font-style: normal;
      text-transform: none;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 11px;
      img {
        margin-right: 13px;
      }
    }
    .subheading {
      font-weight: 500;
      font-size: 15px;
      color: #6b6b6b;
      text-align: center;
      font-style: nor mal;
      text-transform: none;
    }
    // 底部
    .footer-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      height: auto;
      margin: 0;
      padding: 0;

      .prompt-from {
        display: flex;
        flex-direction: column;
        margin: 10px 20px 10px;
        padding: 9px 10px;
        width: 676px;
        min-height: 131px;
        background: #f0f0f6;
        border-radius: 4px;
        border: 1px solid #eef1f5;
        &:focus-within {
          border-color: #535bf2;
          box-shadow: 0 4px 16px rgba(64, 158, 255, 0.08);
        }
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

          .btn-send {
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
  }

  .ai-disclaimer {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    text-align: center;
    font-size: 12px;
    color: #999;
    margin-bottom: 15px;
  }
}

.skeleton-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  padding: 20px;
  box-sizing: border-box;

  .skeleton-image {
    width: 44px;
    height: 44px;
    margin-bottom: 11px;
    border-radius: 50%;
  }

  .skeleton-title {
    width: 300px;
    height: 22px;
    margin-bottom: 11px;
  }

  .skeleton-subtitle {
    width: 200px;
    height: 15px;
    margin-bottom: 50px;
  }

  .skeleton-footer {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 676px;
    padding: 9px 10px;
    background: #f0f0f6;
    border-radius: 4px;
    border: 1px solid #eef1f5;

    .skeleton-textarea {
      width: 100%;
      height: 70px;
      margin-bottom: 8px;
    }

    .skeleton-buttons {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      padding-top: 8px;

      .skeleton-select {
        width: 150px;
        height: 32px;
        border-radius: 4px;
      }

      .skeleton-send-button {
        width: 32px;
        height: 32px;
        border-radius: 6px;
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
