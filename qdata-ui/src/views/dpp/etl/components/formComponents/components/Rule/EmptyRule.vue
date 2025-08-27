<template>
    <div></div>
</template>

<script setup>
defineProps({
    form: Object,
    falg: Boolean,
    inputFields: Array,
    columnList: Array
});

// 占位组件校验，始终返回通过
const validate = async () => {
    return { valid: true, data: {} };
};

defineExpose({ validate });
</script>
