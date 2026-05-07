<template>
    <div class="table-node">
        <div class="table-header">
            <span>{{ store.name }}</span>
        </div>
    </div>
</template>

<script setup name="tableInfoNode">
    const store = reactive({
        name: ''
    });

    const getNode = inject('getNode');

    function setupData(data) {
        store.name = data.name;
    }

    onMounted(() => {
        const node = getNode();
        setupData(node.data);
        node.on('change:data', (data) => {
            setupData(data);
        });
    });
</script>

<style lang="scss" scoped>
    .table-node {
        display: flex;
        flex-direction: column;
        width: 100%;
        height: 100%;
        background: linear-gradient(135deg, #fff 0%, #f8f9fa 100%);
        border: 2px solid #1890ff;
        border-radius: 8px;
        transition: all 0.3s ease;
        box-sizing: border-box;
    }

    .table-header {
        background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
        color: white;
        padding: 6px 12px;
        font-weight: 600;
        font-size: 13px;
        text-align: center;
        border-radius: 6px 6px 0 0;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 6px;
        line-height: 20px;
        span {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    }
</style>
