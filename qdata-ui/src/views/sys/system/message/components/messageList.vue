<template>
  <div class="message-list">
    <div class="msg-item" v-for="(msg, index) in messages" :key="index">
      <div class="icon">
        <img src="@/assets/system/images/layout/msg/icon1.png" alt="" />
      </div>
      <div class="content">
        <div class="title">{{ msg.title }}</div>
        <div class="time">{{ msg.time }}</div>
      </div>
    </div>
    <div>{{ messages }}</div>
  </div>
</template>

<script setup name="messageList">
// import Item from "@/views/sys/system/message/components/item.vue";
import { ref, nextTick, onMounted, onBeforeUnmount, watch } from 'vue';
import WebSocketService from '@/api/system/system/message/websocketService'; // 导入服务
import { getToken } from '../../../../../utils/auth'; // 引入token获取工具

const messages = ref([
  {
    title: '您有一条新消息',
    time: 'test'
  },
  {
    title: '您有一条新消息',
    time: 'test'
  },
]); // 用于保存接收到的站内信

// 获取当前用户的 token
const userId = ref('test11111111111');
const token = ref(getToken());
let message = ref('');

// 组件挂载时初始化 WebSocket 连接
onMounted(() => {
  // messages.value.push({
  //   title: '11111111111111',
  //   time: '11111111111'
  // },)
  // nextTick(() => {
  //   console.log(messages.value,'===messages.value')
  // });
  initWebSocket();
});
// const { proxy } = getCurrentInstance();
// 消息类型
// const props = defineProps(['msgCategory'])
// console.log('---------消息列表----------',props.msgCategory)



const webSocketService = ref(null); // WebSocket 服务实例

// 获取父组件传递的用户ID
// const props = defineProps({
//   userId: String,
//   token: String,
// });

// 初始化 WebSocket
const initWebSocket = () => {
  webSocketService.value = new WebSocketService(userId.value, token.value);

  if (userId.value) {
    // 初始化 WebSocket 并监听消息
    webSocketService.value.init();
    webSocketService.value.socket.onmessage = (event) => {
      const messageData = JSON.parse(event.data);
      // console.log('-----------监听消息 messageData----------', messageData);
      console.log('-----------原始数据 messages.value----------', messages.value);
      if (messageData) {
        messages.value.push({
          title: '11111111111111',
          time: '11111111111'
        },)
        // messages.value = [
        //   {
        //     title: '11111111111111',
        //     time: '11111111111'
        //   },
        // ]
        // 在 WebSocket 数据更新后，强制视图更新
        nextTick(() => {
          console.log(messages.value, '===messages.value')
        });
      }
      console.log('-----------数据更新 messages.value----------', messages.value);
    };
  }
};


// messageData.time = messageData.createTime;
// 用新数组替换原数组，触发 Vue 的视图更新
// messages.value = [...messages.value, {
//   title: messageData.title,
//   time: messageData.createTime
// }];

</script>

<style lang="scss" scoped>
.message-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  overflow-y: auto;
  overflow-x: hidden;
}

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
