<template>
  <!-- 页面内容 -->
  <el-watermark style="width: 100%; height: 100%; position: ''" v-if="watermarkText" :font="config.font"
    :content="watermarkText" :gap="[200, 200]">
    <router-view />
  </el-watermark>
  <router-view v-else />
</template>

<script setup>
import useSettingsStore from "@/store/system/settings";
import { handleThemeStyle } from "@/utils/theme";
import { useRoute } from "vue-router"; // 引入 useRoute 钩子
import useUserStore from "@/store/system/user";
// import useAppStore from "@/store/system/app";
// const appStore = useAppStore();
const userStore = useUserStore();
// import { alertEffects } from "element-plus";
// 使用 useRoute 钩子获取当前路由对象
const route = useRoute();
// const storedUser = useUserStore();
;
const whiteList = ["/login", "/register", "/sso/login", "/sso",];
// 计算水印文本，动态获取当前路由的名称
const watermarkText = computed(() => {
  if (localStorage.getItem("username")) {
    if (route.path != "/login" && route.path != "/sso/login") {
      return localStorage.getItem("username") || "默认水印"; //需要水印赋值不需要给空
    } else {
      return "";
    }
  }
});
const config = reactive({
  content: "Element Plus",
  font: {
    fontSize: 16,
    color: "rgba(0, 0, 0, 0.15)",
  },
});
onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme);
  });
});
</script>
