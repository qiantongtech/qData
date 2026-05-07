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
                <el-form-item label="字段名称：">
                    <el-input
                        v-model="dialog.row.columnName"
                        placeholder="请输入字段名称"
                        disabled
                    />
                </el-form-item>
                <el-form-item label="字段注释：">
                    <el-input
                        v-model="dialog.row.columnComment"
                        placeholder="请输入字段注释"
                        disabled
                    />
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
                label: '字段名称',
                prop: 'columnName',
                showOverflowTooltip: {
                    effect: 'light'
                },
                minWidth: 140
            },
            {
                label: '字段注释',
                prop: 'columnComment',
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
                                    version: '1.0.0',
                                    updateType: '新增',
                                    updateMsg:
                                        '新增用户姓名字段，用于存储用户真实姓名信息，支持中文和英文字符，最大长度50个字符',
                                    activeVersion: 'N',
                                    createBy: 'admin',
                                    createTime: '2021-09-01 10:00:00',
                                    columnName: 'user_name',
                                    columnComment: '用户姓名'
                                },
                                {
                                    id: 2,
                                    version: '1.0.1',
                                    updateType: '新增',
                                    updateMsg:
                                        '添加邮箱字段，用于用户邮箱验证和密码找回功能，采用标准邮箱格式验证规则',
                                    activeVersion: 'N',
                                    createBy: 'dev001',
                                    createTime: '2021-09-02 11:30:00',
                                    columnName: 'email',
                                    columnComment: '邮箱地址'
                                },
                                {
                                    id: 3,
                                    version: '1.0.2',
                                    updateType: '删除',
                                    updateMsg:
                                        '删除废弃的临时字段temp_data，该字段已不再使用，清理冗余数据结构提高表性能',
                                    activeVersion: 'N',
                                    createBy: 'user001',
                                    createTime: '2021-09-03 14:20:00',
                                    columnName: 'temp_data',
                                    columnComment: '临时数据'
                                },
                                {
                                    id: 4,
                                    version: '1.0.3',
                                    updateType: '回滚',
                                    updateMsg:
                                        '回滚至1.0.1版本，撤销1.0.2版本的字段删除操作，恢复temp_data字段以兼容旧系统接口',
                                    activeVersion: 'N',
                                    createBy: 'admin',
                                    createTime: '2021-09-04 16:45:00',
                                    columnName: 'temp_data',
                                    columnComment: '临时数据'
                                },
                                {
                                    id: 5,
                                    version: '1.0.4',
                                    updateType: '新增',
                                    updateMsg:
                                        '创建时间戳字段，自动记录数据创建时间，支持毫秒级精度，用于数据追踪和审计',
                                    activeVersion: 'Y',
                                    createBy: 'dev002',
                                    createTime: '2021-09-05 09:15:00',
                                    columnName: 'create_time',
                                    columnComment: '创建时间'
                                },
                                {
                                    id: 6,
                                    version: '1.0.5',
                                    updateType: '删除',
                                    updateMsg:
                                        '移除过时的字段old_status，该字段已被新的status字段替代，简化数据结构设计',
                                    activeVersion: 'N',
                                    createBy: 'admin',
                                    createTime: '2021-09-06 13:30:00',
                                    columnName: 'old_status',
                                    columnComment: '旧状态字段'
                                },
                                {
                                    id: 7,
                                    version: '1.0.6',
                                    updateType: '新增',
                                    updateMsg:
                                        '新增索引字段，为提升查询性能而添加，支持多字段联合查询，优化系统响应速度',
                                    activeVersion: 'N',
                                    createBy: 'dev001',
                                    createTime: '2021-09-07 15:20:00',
                                    columnName: 'idx_field',
                                    columnComment: '索引字段'
                                }
                            ],
                            total: 7
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
