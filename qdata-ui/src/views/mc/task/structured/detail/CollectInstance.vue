<template>
    <div class="collect-instance" v-loading="store.loading">
        <qt-wrap
            :columns="tableStroe.columns"
            :tableRef="tableRef"
            :config="{ fullContent: false, actions: { table: { search: false } } }"
        >
            <qt-table v-bind="tableStroe">
                <template #table-count="{ row }">
                    {{ row.totalCount }}（成功 {{ row.successCount }}，失败 {{ row.failCount }}）
                </template>

                <template #change-count="{ row }">
                    新增 {{ row.addCount }}，删减 {{ row.delCount }}，变更 {{ row.updateCount }}
                </template>

                <template #date-range="{ row }"> {{ row.startTime }} ~ {{ row.endTime }} </template>

                <template #handle="{ row }">
                    <el-button
                        link
                        type="primary"
                        icon="View"
                        @click="handleViewClick(row)"
                        v-hasPermi="['mc:instance:structured:log:view']"
                    >
                        查看日志
                    </el-button>
                    <el-button
                        link
                        type="warning"
                        icon="Download"
                        @click="handleDownloadClick(row)"
                        v-hasPermi="['mc:instance:structured:log:download']"
                    >
                        下载日志
                    </el-button>
                </template>
            </qt-table>
        </qt-wrap>

        <LogDialog v-model="dialog.open" v-bind="dialog" />
    </div>
</template>

<script setup name="CollectInstance">
    import { listTaskInstance } from '@/api/mc/task/taskInstance';
    import { getTaskInstanceLog } from '@/api/mc/task/taskInstanceLog';
    import LogDialog from '@/components/LogDialog/index.vue';
    import { getCurrentInstance, reactive } from 'vue';

    const { proxy } = getCurrentInstance();

    const props = defineProps({
        detail: {
            type: Object,
            required: true
        }
    });

    const store = reactive({
        loading: false
    });

    const tableStroe = reactive({
        config: {
            sort: true,
            table: {
                stripe: true,
                border: true,
                rowKey: 'dictId',
                defaultSort: { prop: 'create_time', order: 'descending' }
            }
        },
        columns: [
            {
                label: '编号',
                prop: 'id',
                width: 60
            },
            {
                label: '采集范围',
                prop: 'collectionScope',
                dict: 'mc_collect_scope',
                width: 120
            },
            {
                label: '采集表数量',
                slot: 'table-count',
                minWidth: 240,
                showOverflowTooltip: {
                    effect: 'light'
                }
            },
            {
                label: '变更数量',
                slot: 'change-count',
                minWidth: 240,
                showOverflowTooltip: {
                    effect: 'light'
                }
            },
            {
                label: '运行状态',
                prop: 'status',
                dict: 'mc_task_instance_status',
                width: 90
            },
            {
                label: '失败原因',
                prop: 'failCause',
                minWidth: 240,
                showOverflowTooltip: {
                    effect: 'light'
                }
            },
            {
                label: '采集耗时(s)',
                prop: 'duration',
                width: 120
            },
            {
                label: '采集起止时间',
                slot: 'date-range',
                width: 340,
                showOverflowTooltip: {
                    effect: 'light'
                }
            },
            {
                label: '操作',
                slot: 'handle',
                width: 220,
                fixed: 'right'
            }
        ],
        func: listTaskInstance,
        params: {
            taskId: props.detail.id
        }
    });

    const dialog = reactive({
        open: false,
        content: ''
    });

    function handleViewClick(row) {
        store.loading = true;
        getTaskInstanceLog(row.id).then((res) => {
            dialog.content = res.data?.logContent || '暂无日志';
            store.loading = false;
            dialog.open = true;
        });
    }

    // 下载日志
    function handleDownloadClick(row) {
        store.loading = true;
        getTaskInstanceLog(row.id).then((res) => {
            const content = res.data?.logContent || '暂无日志';
            proxy.downloadContent(content, `${props.detail.name}_${row.id}_日志.log`);
            store.loading = false;
        });
    }
</script>
