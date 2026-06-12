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

<!-- 无聊天对话时，在 message 区域，可以新增对话 -->
<template>
  <div class="new-chat">
    <div class="box-center">
      <div class="tip">{{ td('ai.chat.startConversation') }}</div>
      <div class="btns">
        <el-button type="primary" round @click="handlerNewChat">{{ td('ai.chat.newConversation') }}</el-button
        >
      </div>
    </div>
  </div>
</template>
<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();

const emits = defineEmits(["onNewConversation"]);

/** 新建 conversation 聊天对话 */
const handlerNewChat = () => {
  emits("onNewConversation");
};
</script>
<style scoped lang="scss">
.new-chat {
  display: flex;
  flex-direction: row;
  justify-content: center;
  width: 100%;
  height: 100%;

  .box-center {
    display: flex;
    flex-direction: column;
    justify-content: center;

    .tip {
      font-size: 14px;
      color: #858585;
    }

    .btns {
      display: flex;
      flex-direction: row;
      justify-content: center;
      margin-top: 20px;
    }
  }
}
</style>
