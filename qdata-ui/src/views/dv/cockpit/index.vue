<template>
  <div class="app-container">
    <iframe
        v-show="loadFinished"
        ref="iframeRef"
        :src="src"
        class="custom-iframe"
        frameborder="0"
    ></iframe>
  </div>
</template>

<script setup name="DaReportDrag">
import {ref, onMounted} from 'vue';
// import {red} from "chalk";

const src = ref("/drag/list");
const iframeRef = ref(null);
const loadFinished = ref(false);

onMounted(() => {
  iframeRef.value.addEventListener('load', () => {
    const innerDoc = iframeRef.value.contentWindow.document;

    // 隐藏顶部 logo
    const jimuLogo = innerDoc.querySelector('.header-bg-color');
    if (jimuLogo) {
      jimuLogo.style.display = 'none';
    }

    // 隐藏底部 footer（如果有）
    const floatingMenu = innerDoc.querySelector('.floating-menu');
    const footer = innerDoc.querySelector('.layout-footer');
    // const rightBgColor = innerDoc.querySelector('.right-bg-color');
    // const tabContent = innerDoc.querySelector('.tab-content');
    if (floatingMenu && footer) {
      floatingMenu.style.display = 'none';
      footer.style.display = 'none';
      // rightBgColor.style.backgroundColor = "#fff";
      // rightBgColor.style.setProperty("background-color", "#fff", "important");
      // tabContent.style.setProperty("background-color", "#fff", "important");
    }

    const styleEl = innerDoc.createElement("style");
    styleEl.innerHTML = `
  .top-right-panel {
    background-color: #fff !important;
  }
  .tab-content {
    height: 100% !important;
    background-color: #fff !important;
  }
  .right-bg-color {
    background-color: #fff !important;
  }`;
    innerDoc.head.appendChild(styleEl);

    // 顶部 tab切换
    const tabs = innerDoc.querySelectorAll('.top-menu-li');
    if (tabs.length > 0) {
      tabs[0].click();

      // 设置一个延时，确保 Tab 切换 DOM 生效后再展示 iframe
      setTimeout(() => {
        loadFinished.value = true;
      }, 30);
    } else {
      // 没有 tab 也不要卡住页面展示
      loadFinished.value = true;
    }
  });
});
</script>

<style scoped lang="scss">
.app-container {
  height: 86vh;
  overflow: hidden;
}

.custom-iframe {
  width: 100%;
  height: 100%;
  border: none;
}
</style>
