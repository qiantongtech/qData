<template>
    <div class="version-management">
        <qt-wrap
            :columns="tableStroe.columns"
            :tableRef="tableRef"
            :config="{ fullContent: false, actions: { table: { search: false } } }"
        >
            <qt-table v-bind="tableStroe" ref="tableRef">
                <template #active-version="{ row }">
                    <el-icon v-show="row.activeVersion == 'Y'"><Select /></el-icon>
                </template>
                <template #handle="{ row }">
                    <el-button link type="primary" icon="view" @click="handleDetailClick(row)">
                        详情
                    </el-button>
                    <el-button link type="primary" icon="Edit"> 恢复 </el-button>
                </template>
            </qt-table>
        </qt-wrap>

        <el-dialog v-model="dialog.open" title="版本详情" width="800" draggable>
            <el-form label-width="auto">
                <el-form-item label="库名：">
                    <el-input v-model="dialog.row.dbName" placeholder="请输入库名" disabled />
                </el-form-item>
                <el-form-item label="版本号：">
                    <el-input v-model="dialog.row.version" placeholder="请输入版本号" disabled />
                </el-form-item>
                <el-form-item label="变更类型：">
                    <el-input
                        v-model="dialog.row.updateType"
                        placeholder="请输入变更类型"
                        disabled
                    />
                </el-form-item>
                <el-form-item label="变更说明：">
                    <el-input
                        v-model="dialog.row.updateMsg"
                        placeholder="请输入变更说明"
                        disabled
                        type="textarea"
                        :min-height="192"
                        show-word-limit
                        maxlength="500个字符"
                    />
                </el-form-item>
                <el-form-item label="当前版本：">
                    <dict-tag
                        :options="toValue(dicts.sys_yes_no)"
                        :value="dialog.row.activeVersion"
                    />
                </el-form-item>
                <el-form-item label="创建人：">
                    <el-input v-model="dialog.row.name" placeholder="请输入修改人" disabled />
                </el-form-item>
                <el-form-item label="创建时间：">
                    <el-input v-model="dialog.row.time" placeholder="请输入创建时间" disabled />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialog.open = false">关闭</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="VersionManagement">
    import { reactive, toValue, getCurrentInstance } from 'vue';

    const { proxy } = getCurrentInstance();
    const dicts = proxy.useDict('sys_yes_no');

    const tableStroe = reactive({
        columns: [
            {
                label: '编号',
                prop: 'id',
                sortable: true,
                width: 60
            },
            {
                label: '库名',
                prop: 'dbName',
                showOverflowTooltip: {
                    effect: 'light'
                },
                minWidth: 140
            },
            {
                label: '版本号',
                prop: 'version',
                width: 90
            },
            {
                label: '变更类型',
                prop: 'updateType',
                width: 90
            },
            {
                label: '变更说明',
                prop: 'updateMsg',
                minWidth: 240,
                showOverflowTooltip: {
                    effect: 'light'
                }
            },
            {
                label: '当前版本',
                prop: 'activeVersion',
                slot: 'active-version',
                width: 90
            },
            {
                label: '创建人',
                prop: 'createBy',
                width: 90
            },
            {
                label: '创建时间',
                prop: 'createTime',
                sortable: true,
                width: 160,
                date: true
            },
            {
                label: '操作',
                width: 240,
                fixed: 'right',
                slot: 'handle'
            }
        ],
        func: function () {
            return new Promise((resolve) => {
                setTimeout(() => {
                    resolve({
                        data: {
                            rows: [
                                {
                                    id: 1,
                                    dbName: 'qdata_meta',
                                    version: '1.0.0',
                                    updateType: '新增',
                                    updateMsg:
                                        '新增用户表字段，包括用户姓名、联系方式、注册时间等基础信息，优化数据结构设计',
                                    activeVersion: 'Y',
                                    createBy: 'admin',
                                    createTime: '2021-09-01 10:00:00'
                                },
                                {
                                    id: 2,
                                    dbName: 'qdata_meta',
                                    version: '1.0.1',
                                    updateType: '修改',
                                    updateMsg: '修改表结构',
                                    activeVersion: 'N',
                                    createBy: 'admin',
                                    createTime: '2021-09-01 10:00:00'
                                },
                                {
                                    id: 3,
                                    dbName: 'qdata_meta',
                                    version: '1.0.2',
                                    updateType: '回滚',
                                    updateMsg: '回滚表结构',
                                    activeVersion: 'N',
                                    createBy: 'admin',
                                    createTime: '2021-09-01 10:00:00'
                                }
                            ],
                            total: 3
                        }
                    });
                }, 1000);
            });
        }
    });

    const dialog = reactive({
        open: false,
        row: {}
    });

    function handleDetailClick(row) {
        dialog.row = row;
        dialog.open = true;
    }
</script>
