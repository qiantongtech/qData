<template>
    <el-select v-bind="$attrs">
        <el-option
            v-for="type in typeList"
            :key="type.key"
            :label="type.label"
            :value="type.value"
        />
    </el-select>
</template>

<script setup name="DatasourceTypes">
    import { getDatasourceTypes } from './utils';

    const props = defineProps({
        flag: {
            type: Object
        }
    });

    const typeList = computed(() => {
        return getDatasourceTypes(props.flag);
    });
</script>
