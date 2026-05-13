<template>
  <div :class="['app-container', { 'is-tab': type === 'tab' }]">
    <div class="developing-wrapper">
      <img
        src="@/assets/images/sys/error/building.png"
        alt="功能开发中"
        class="developing-img"
      />
    </div>
  </div>
</template>

<script setup>
defineProps({
  type: {
    type: String,
    default: "page",
  },
});
</script>

<style scoped>
.developing-wrapper {
  width: 100%;
  height: calc(100vh - 125px);
  background-color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  overflow: hidden;
}

.developing-img {
  width: 100%;
  max-width: 500px;
  height: auto;
  object-fit: contain;
  display: block;
  margin-top: -46px;
}

.is-tab.app-container {
  padding: 0;
}

.is-tab .developing-wrapper {
  height: 500px;
}

.is-tab .developing-img {
  max-width: 400px;
  margin-top: 0;
}
</style>