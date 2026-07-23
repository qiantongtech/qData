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
  <div class="msg-item" v-for="(msg, index) in messages" :key="index">
    <div class="icon">
      <img src="@/assets/images/system/layout/msg/toolbar-icon1.png" alt="" />
    </div>
    <div class="content">
      <div class="title">{{ msg.title }}</div>
      <div class="time">{{ msg.time }}</div>
    </div>
  </div>
  <div>{{messages}}</div>
</template>

<script setup>
  const { td } = useDefaultLang();
  const {proxy} = getCurrentInstance();

  import { ref,nextTick, onMounted, onBeforeUnmount, watch } from 'vue';
  import WebSocketService from '@/api/system/system/message/websocketService'; // Import services
  import { getToken } from '../../../../../utils/auth'; // Introduce token acquisition tool

  // Get the current user's token
  const userId = ref('test11111111111');
  const token = ref(getToken());
  let message = ref('');

  const webSocketService = ref(null); // WebSocket service instance

  // Get the user ID passed by the parent component
  const props = defineProps({
    userId: String,
    token: String,
  });

  const messages = ref([
    {
      title: td('sys.system.message.newMessage'),
      time: 'test'
    },
    {
      title: td('sys.system.message.newMessage'),
      time: 'test'
    },
  ]); // Used to save received site messages

  // Initialize WebSockets
  const initWebSocket = () => {
    webSocketService.value = new WebSocketService(userId.value, token.value);

    if (userId.value) {
      // Initialize WebSocket and listen for messages
      webSocketService.value.init();
      webSocketService.value.socket.onmessage = (event) => {
        const messageData = JSON.parse(event.data);
        console.log("-----------Listening for messageData----------", messageData);
        if (messageData) {

          messages.value = [
            {
              title: '11111111111111',
              time: '11111111111'
            },
          ]
          // Force view update after WebSocket data is updated
          nextTick(() => {
            console.log("View updated:", messages.value);
          });
          // messageData.time = messageData.createTime;
          // Replace the original array with the new array to trigger Vue's view update
          // messages.value = [...messages.value, {
          //   title: messageData.title,
          //   time: messageData.createTime
          // }];
        }
        console.log("-----------Data updated----------", messages.value);
      };
    }
  };

  // Initialize the WebSocket connection when the component is mounted
  onMounted(() => {
    initWebSocket();
  });

  // // send message
  // const sendMessage = () => {
  //   console.log('-----------Send message----------',message.value)
  //   if (message.value) {
  //     webSocketService.value.sendMessage(message.value);
  //     message.value = ''; // Clear the input box
  //   }
  // };

  // Close WebSocket connection when component is uninstalled
  // onBeforeUnmount(() => {
  //   if (webSocketService.value) {
  //     webSocketService.value.close();
  //   }
  // });

  // Monitor token changes and update WebSocket connections
  // watch(() => props.token, (newToken) => {
  //   if (webSocketService.value) {
  //     webSocketService.value.updateToken(newToken); // Assume that WebSocketService has a method to update token
  //   }
  // });
</script>

<style scoped lang="scss">
  .msg-item {
    display: flex;
    align-items: center;
    width: 100%;
    padding: 10px 16px;
    margin-bottom: 10px;
    background: #f9f9f9;
    border-radius: 4px;
  }

  .icon {
    width: 34px;
    height: 34px;
    margin-right: 12px;
    img {
      width: 100%;
      border-radius: 50%;
    }
  }

  .content {
    .title {
      font-size: 14px;
      font-weight: 500;
      color: rgba(0, 0, 0, 0.85);
      margin-bottom: 6px;
    }

    .time {
      font-size: 12px;
      color: rgba(0, 0, 0, 0.45);
    }
  }
</style>
