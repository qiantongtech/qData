<template>
  <div class="c-head">
    <div class="time">{{ formattedTime }}</div>
    <div class="name">
      <!-- 自治区水利数据底板驾驶舱 -->
      <img src="@/assets/cockpit/title.png" alt="" />
    </div>
    <div class="user">责任人一览表</div>
  </div>
</template>

<script setup name="CockpitHead">
import moment from "moment";
// 响应式时间变量
const currentTime = ref(new Date());
let timer = null;

// 更新时间函数
const updateTime = () => {
  currentTime.value = new Date();
};

// 格式化显示时间（使用moment）
const formattedTime = ref(moment(currentTime.value).format("YYYY年 MM月DD HH:mm:ss"));

// 生命周期钩子
onMounted(() => {
  timer = setInterval(() => {
    updateTime();
    // 使用moment格式化时间
    formattedTime.value = moment(currentTime.value).format("YYYY年 MM月DD HH:mm:ss");
  }, 1000);
});

onUnmounted(() => {
  clearInterval(timer);
});
</script>

<style scoped lang="scss">
.c-head {
  height: 82px;
  padding: 0 20px;
  display: flex;
  //   align-items: center;
  justify-content: space-between;
  .time {
    padding-top: 30px;
    line-height: 1;
    font-family: PingFangSC;
    font-weight: bold;
    font-size: 22px;
    color: #aecaf5;
    text-align: center;
  }
  .name {
    font-family: sharp;
    font-size: 42px;
    color: #eff8fc;
    line-height: 29px;
    text-shadow: 2px 0px 1px #000000;
    text-align: center;
    font-style: normal;
    text-transform: none;
    -webkit-text-stroke: 1px #ffffff;
    img {
      width: 603px;
    }
  }
  .user {
    width: 208px;
    height: 40px;
    line-height: 40px;
    margin-top: 20px;
    padding-left: 68px;
    background: url("@/assets/cockpit/user-btn.png") no-repeat;
    background-size: 100% 100%;
    letter-spacing: 1px;
    font-family: HYk2gj;
    font-weight: bold;
    font-size: 18px;
    color: #ffffff;
  }
}
</style>
