<template>
   <div> {{ td('sys.tool.build.title') }} <svg-icon icon-class="build" /> </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
</script>
