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
