<template>
    <div class="table-node">
        <div class="table-header">
            <span>{{ store.name }}</span>
        </div>
        <div class="table-fields">
            <div class="table-field" v-for="(field, index) in store.fields" :key="index">
                <div class="field-icon">
                    {{ field.icon }}
                </div>
                <div class="field-name">{{ field.cnName }}({{ field.enName }})</div>
                <div class="field-type">{{ field.type }}</div>
            </div>
        </div>
    </div>
</template>

<script setup name="TableNode">
    import { reactive, inject, onMounted } from 'vue';

    const store = reactive({
        name: '',
        fields: []
    });

    const getNode = inject('getNode');

    function setupData(data) {
        store.name = data.name;
        store.fields = data.fields;
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
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;
    }

    .table-node:hover {
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
    }

    .table-header {
        background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
        color: white;
        padding: 6px 12px;
        font-weight: 600;
        font-size: 14px;
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

    .table-fields {
        flex: 1;
        overflow: hidden;
        border-radius: 0 0 6px 6px;
    }

    .table-field {
        display: flex;
        align-items: center;
        padding: 0px 8px;
        font-size: 12px;
        line-height: 28px;
        border-bottom: 1px solid #e8e8e8;
        transition: background-color 0.2s ease;
    }

    .table-field:hover {
        background-color: #f5f5f5;
    }
    .table-field.even {
        background-color: #fafafa;
    }
    .table-field:last-child {
        border-bottom: none;
        border-radius: 0 0 6px 6px;
    }
    .table-field.primary {
        background-color: #e6f7ff;
    }
    .table-field.foreign {
        background-color: #fff7e6;
    }
    .table-field.unique {
        background-color: #f6ffed;
    }

    .field-icon {
        margin-right: 4px;
        font-size: 10px;
    }
    .field-name {
        flex: 1;
        font-weight: 500;
        color: #262626;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-right: 20px;
    }
    .field-type {
        color: #666;
        font-family: 'Monaco', 'Menlo', monospace;
        font-size: 10px;
    }
</style>
