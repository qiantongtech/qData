<template>
   <div> {{ t('sys.tool.build.title') }} <svg-icon icon-class="build" /> </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
</script>
