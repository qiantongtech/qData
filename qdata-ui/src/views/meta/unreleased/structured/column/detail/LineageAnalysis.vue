<template>
    <div class="shape-content" v-loading="!store.shape.loaded">
        <LineageShape
            :origins="store.shape.origins"
            :target="store.shape.target"
            :type="store.shape.type"
            v-if="store.shape.loaded"
        />
    </div>
</template>

<script setup name="LineageAnalysis">
    import LineageShape from '@/views/meta/components/LineageShape.vue';
    import { reactive } from 'vue';
    import { tableDataRes } from '@/views/meta/components/data';

    const store = reactive({
        dept: {
            leftWidth: 300,
            deptOptions: [],
            defaultExpand: true
        },
        shape: {
            loaded: false,
            origins: [],
            target: {},
            type: 'Table'
        }
    });

    function getList() {
        store.shape.loaded = false;
        setTimeout(() => {
            store.shape.target = tableDataRes.origin;
            store.shape.origins = tableDataRes.targets;
            store.shape.loaded = true;
            store.shape.type = 'Table';
        }, 1000);
    }

    getList();
</script>

<style lang="scss" scoped>
    .shape-content {
        width: 100%;
        height: 620px;
    }
</style>
