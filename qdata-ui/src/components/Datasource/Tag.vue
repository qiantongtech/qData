<template>
    <el-tag :type="tag?.type ? tag.type : 'primary'">{{ tag.label }}</el-tag>
</template>

<script setup name="DatasourceTag">
    import { computed } from 'vue';
    import { getDatasourceActive } from './utils';
    const props = defineProps({
        value: {
            type: String,
            required: true
        }
    });

    const tag = computed(() => {
        return getDatasourceActive(props.value);
    });
</script>
