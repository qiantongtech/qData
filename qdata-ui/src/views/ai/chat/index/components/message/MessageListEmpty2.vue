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

<!-- Display the prompt list when the message list is empty -->
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
]; // prompt list

const emits = defineEmits(["onPrompt"]);

/** Select prompt and click */
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
