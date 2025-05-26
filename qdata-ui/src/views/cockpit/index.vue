<template>
  <div class="app-container cockpit-container" ref="app-container">
    <c-head />
    <div class="main">
      <c-Left />
      <c-Center />
      <c-right />
    </div>
    <c-bottom />
  </div>
</template>
<script setup name="Cockpit">
import CHead from "./components/head.vue";
import CLeft from "./components/left.vue";
import CCenter from "./components/center.vue";
import CRight from "./components/right.vue";
import CBottom from "./components/bottom.vue";
import autofit from "autofit.js";
onMounted(() => {
  autofit.init({
    el: ".cockpit-container",
  });
});
onUnmounted(() => {
  autofit.off();
});
</script>

<style scoped lang="scss">
.cockpit-container {
  padding: 0;
  margin: 0;
  height: 100vh;
  background: url("@/assets/cockpit/bg.png") no-repeat;
  background-size: 100% 100%;
  overflow: hidden;
  position: relative;

  //   background-size: 100% 100%;
  .main {
    display: flex;
    justify-content: space-between;
    padding: 0 20px 0 10px;
  }

  :deep(.box-wrap) {
    display: flex;
    flex-direction: column;
    padding-top: 10px;
    padding-left: 10px;

    .box-title {
      display: flex;
      justify-content: space-between;
      padding: 0 30px 0 74px;
      height: 60px;

      .box-name {
        padding-top: 12px;
        font-family: HYk2gj;
        font-size: 20px;
        color: #ffffff;
        text-shadow: 2px 0px 9px #003f85;
        letter-spacing: 1px;
      }
    }

    .box-con {
      height: calc(100% - 60px);
    }
  }
}
</style>
