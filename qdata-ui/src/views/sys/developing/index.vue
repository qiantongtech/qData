<template>
  <div class="app-container">
    <div class="emptyBg">
      <div class="empty-content">
        <img
          src="@/assets/images/sys/error/no-data.png"
          alt="功能开发中"
          class="empty-image"
        />
        <p class="empty-text">功能开发中</p>
      </div>
    </div>
  </div>
</template>

<script setup>
// 无需逻辑，仅展示组件
</script>

<style scoped>
.emptyBg {
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 124px);
  padding: 40px 0;
  background-color: #fff;
}

.empty-content {
  text-align: center;
}

.empty-image {
  max-width: 300px;
  width: 100%;
  height: auto;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 18px;
  color: #666;
}
</style>