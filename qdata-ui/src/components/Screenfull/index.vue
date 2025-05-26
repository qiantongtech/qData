<template>
  <div>
    <!--    <svg-icon :icon-class="isFullscreen ? 'exit-fullscreen-new' : 'fullscreen_new_icon'" @click="toggle" :style="{width:'20px',height:'20px',paddingTop:'2px'}"/>-->

    <i :class="isFullscreen ? 'iconfont icon-fullscreen-exit-line' : 'iconfont icon-a-quanpingxianxing'"
      style="font-size: 20px" @click="toggle"></i>

  </div>
</template>

<script setup>
import { useFullscreen } from '@vueuse/core'

const { isFullscreen, enter, exit, toggle } = useFullscreen();
</script>

<style lang='scss' scoped>
.screenfull-svg {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
</style>
