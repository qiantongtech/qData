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
-->

<!-- 消息列表为空时，展示 prompt 列表 -->
<template>
  <div class="chat-empty">
    <!-- title -->
    <div class="center-container">
      <div class="title">{{ td('ai.chat.lingtongAI') }}</div>
      <div class="role-list">
        <div
          class="role-item"
          v-for="prompt in promptList"
          :key="prompt.prompt"
          @click="handlerPromptClick(prompt)"
        >
          {{ prompt.prompt }}
        </div>
      </div>
      <div class="ai-disclaimer">
        {{ td('ai.chat.disclaimer') }}
      </div>
    </div>
  </div>
</template>
<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();

const promptList = [
  {
    prompt: td('ai.chat.promptWeather'),
  },
  {
    prompt: td('ai.chat.promptPoem'),
  },
]; // prompt 列表

const emits = defineEmits(["onPrompt"]);

/** 选中 prompt 点击 */
const handlerPromptClick = async ({ prompt }) => {
  emits("onPrompt", prompt);
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

    .title {
      font-size: 28px;
      font-weight: bold;
      text-align: center;
    }

    .role-list {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;
      align-items: center;
      justify-content: center;
      width: 460px;
      margin-top: 20px;

      .role-item {
        display: flex;
        justify-content: center;
        width: 180px;
        line-height: 50px;
        border: 1px solid #e4e4e4;
        border-radius: 10px;
        margin: 10px;
        cursor: pointer;
      }

      .role-item:hover {
        background-color: rgba(243, 243, 243, 0.73);
      }
    }

    .ai-disclaimer {
      text-align: center;
      font-size: 12px;
      color: #999;
      margin-top: 15px;
    }
  }
}
</style>
