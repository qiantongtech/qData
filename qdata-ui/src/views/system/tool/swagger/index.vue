<template>
<!--   <i-frame v-model:src="url"></i-frame>-->
</template>

<script setup>
import iFrame from '@/components/iFrame/index.vue'
const { proxy } = getCurrentInstance();

const url = ref(import.meta.env.VITE_APP_BASE_API + "/doc.html")

// 打开外链 URL 在新的标签页中
window.open(url.value, "_blank");

proxy.$tab.closePage();
</script>
