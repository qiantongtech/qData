<template>
    <div class="database-node">
        <div class="database-header">
            <span>{{ store.name }}</span>
        </div>
        <div class="database-content">
            <div class="black-item">
                <div class="label">状态：</div>
                <div class="content">
                    <el-button type="primary" size="small">启用</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup name="DatabaseNode">
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
    .database-node {
        display: flex;
        flex-direction: column;
        width: 100%;
        height: 100%;
        background: linear-gradient(135deg, #fff 0%, #f8f9fa 100%);
        border: 2px solid #1890ff;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;
        box-sizing: border-box;
    }

    .database-header {
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

    .black-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 5px 8px;
        font-size: 12px;
        transition: background-color 0.2s ease;
    }
</style>
