<template>
    <div class="shape-content" v-loading="!store.shape.loaded">
        <ImpactShape
            :origin="store.shape.origin"
            :targets="store.shape.targets"
            :type="store.shape.type"
            v-if="store.shape.loaded"
        />
    </div>
</template>

<script setup name="ImpactAnalysis">
    import ImpactShape from '@/views/meta/components/ImpactShape.vue';
    import { reactive } from 'vue';
    import { tableDataRes } from '@/views/meta/components/data.js';

    const store = reactive({
        dept: {
            leftWidth: 300,
            deptOptions: [],
            defaultExpand: true
        },
        shape: {
            loaded: false,
            origin: {},
            targets: [],
            type: 'Table'
        }
    });

    function getList() {
        store.shape.loaded = false;
        setTimeout(() => {
            store.shape.origin = tableDataRes.origin;
            store.shape.targets = tableDataRes.targets;
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
