<template>
   <div>
      <i-frame v-model:src="url"></i-frame>
   </div>
</template>

<script setup>
import iFrame from '@/components/iFrame/index.vue'

import { ref } from 'vue';

const url = ref(import.meta.env.VITE_APP_BASE_API + '/druid/index.html');
</script>
