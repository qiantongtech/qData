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

<script setup name="DaReportExcel">
const src = ref("/jmreport/list");
const iframeRef = ref(null);
const loadFinished = ref(false);

onMounted(() => {
  // 等 iframe 页面加载完成后再访问其内容
  iframeRef.value.addEventListener('load', () => {
    const innerDoc = iframeRef.value.contentWindow.document;

    // 顶部 logo
    const targetEl = innerDoc.querySelector('.layout');
    const firstDiv = targetEl.querySelector('div');
    if (targetEl && firstDiv) {
      firstDiv.style.display = 'none';
    }

    // 底部
    const footer = innerDoc.querySelector('.layout-footer');
    if (footer) {
      footer.style.display = 'none';
    }
    loadFinished.value = true;
  });
});
</script>

<style scoped lang="scss">
.app-container {
  height: 86vh;
  overflow: hidden;
}

.custom-iframe {
  height: 100%;
  width: 100%;
  border: none;
}
</style>
