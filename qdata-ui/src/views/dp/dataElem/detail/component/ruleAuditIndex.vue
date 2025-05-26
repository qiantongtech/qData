<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    @click="handleAdd"
                    @mousedown="(e) => e.preventDefault()"
                >
                    <i class="iconfont-mini icon-xinzeng mr5"></i>关联
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </div>
    </div>
    <el-table
        stripe
        height="33.5vh"
        v-loading="loading"
        :data="dataList"
        :default-sort="defaultSort"
    >
        <el-table-column label="序号" type="index" width="80" align="center">
            <template #default="scope">
                <span>{{
                    (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1
                }}</span>
            </template>
        </el-table-column>
        <el-table-column label="规则名称" prop="name" align="left" width="200">
            <template #default="scope">
                {{ scope.row.name || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            label="规则描述"
            prop="description"
            align="left"
            :show-overflow-tooltip="true"
        >
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="规则级别" prop="level" align="center" width="100">
            <template #default="scope">
                {{ formatValue(scope.row.level, att_rule_level) || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="规则类型" prop="type" align="center" width="100">
            <template #default="scope">
                {{ formatValue(scope.row.type, att_rule_audit_type) || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="质量维度" prop="qualityDim" align="center" width="100">
            <template #default="scope">
                {{ formatValue(scope.row.qualityDim, att_rule_audit_q_dimension) || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="关联时间" prop="createTime" align="center" width="180">
            <template #default="scope">
                {{ scope.row.createTime || '-' }}
            </template>
        </el-table-column>
    </el-table>

    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 新增或修改数据元对话框 -->
    <el-dialog
        class="rule-dialog"
        :title="title"
        v-model="open"
        width="900px"
        :append-to="$refs['app-container']"
        draggable
    >
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form label-width="80px">
            <el-row :gutter="24">
                <el-col :span="8">
                    <div class="head-container">
                        <el-input
                            v-model="sourceName"
                            placeholder="请输入关键词搜索"
                            clearable
                            :prefix-icon="Search"
                            style="margin-bottom: 20px"
                        ></el-input>
                    </div>
                    <div class="head-div">
                        <div class="head-container">
                            <el-tree
                                ref="tree"
                                :data="filteredTreeOptionslist"
                                node-key="id"
                                :props="defaultProps"
                                highlight-current
                                style="font-size: 14px"
                                default-expand-all
                                :default-checked-keys="[...checkedKeys]"
                                :current-node-key="currentNodeKey"
                                :expand-on-click-node="false"
                                :check-strictly="true"
                                :filter-node-method="filterNode"
                                @node-click="handleNodeClick"
                                @check="handleNodeCheckChange"
                                show-checkbox
                            />
                        </div>
                    </div>
                </el-col>
                <el-col :span="15" :offset="1">
                    <div>
                        <div class="base-info">
                            <div class="type-name">
                                <span>{{ typeName }}</span>
                            </div>
                            <div class="base-content">
                                <el-form label-position="left">
                                    <el-row :gutter="20">
                                        <el-col :span="12">
                                            <el-form-item
                                                label="规则编号："
                                                prop="id"
                                                label-width="70px"
                                            >
                                                <div>{{ form.id }}</div>
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="12">
                                            <el-form-item
                                                label="规则名称："
                                                prop="name"
                                                label-width="70px"
                                            >
                                                <div>{{ form.name }}</div>
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="12">
                                            <el-form-item
                                                label="规则级别："
                                                prop="level"
                                                label-width="70px"
                                            >
                                                <div>
                                                    {{
                                                        formatValue(form.level, att_rule_level) ||
                                                        '-'
                                                    }}
                                                </div>
                                            </el-form-item>
                                        </el-col>
                                        <el-col :span="12">
                                            <el-form-item
                                                label="规则类型："
                                                prop="type"
                                                label-width="70px"
                                            >
                                                <div>
                                                    {{
                                                        formatValue(
                                                            form.type,
                                                            att_rule_audit_type
                                                        ) || '-'
                                                    }}
                                                </div>
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="12">
                                            <el-form-item
                                                label="质量维度："
                                                prop="qualityDim"
                                                label-width="70px"
                                            >
                                                <div>
                                                    {{
                                                        formatValue(
                                                            form.qualityDim,
                                                            att_rule_audit_q_dimension
                                                        ) || '-'
                                                    }}
                                                </div>
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20">
                                        <el-col :span="23">
                                            <el-form-item
                                                label="描述："
                                                prop="description"
                                                label-width="45px"
                                            >
                                                <div>{{ form.description }}</div>
                                            </el-form-item>
                                        </el-col>
                                    </el-row>
                                </el-form>
                            </div>
                        </div>
                        <div class="hint-div">
                            <el-icon color="#2A7BFD" size="16px"><InfoFilled /></el-icon>
                            <span>请根据数据元的属性信息，给选择的规则设置参数信息</span>
                        </div>
                        <div>
                            <el-form
                                ref="dpDataElemRef"
                                label-position="top"
                                label-width="auto"
                                :model="form"
                                :rules="rules"
                                @submit.prevent
                            >
                                <el-row :gutter="20">
                                    <el-col :span="24">
                                        <el-form-item
                                            label="非空类型"
                                            prop="ruleConfig.nullCheck.type"
                                        >
                                            <el-select
                                                v-model="form.ruleConfig.nullCheck.type"
                                                placeholder="请选择非空类型"
                                                clearable
                                                class="el-form-input-width"
                                            >
                                                <el-option label="空字符串及NULL" value="1" />
                                                <el-option label="空字符串" value="2" />
                                                <el-option label="NULL" value="3" />
                                            </el-select>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                            </el-form>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="cancel">取 消</el-button>
                <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="dataElemAudit">
    import { ref, watch } from 'vue';

    const { proxy } = getCurrentInstance();

    const props = defineProps({
        dataElemId: {
            required: true,
            type: String
        },
        ruleType: {
            required: true,
            type: String,
            default: 1
        }
    });

    import { listDpDataElemRuleRel, save } from '@/api/dp/dataElem/dpDataElemRuleRel';
    import { treeAttAuditRule } from '@/api/att/rule/attAuditRule';
    import { Search } from '@element-plus/icons-vue';

    const { att_rule_audit_q_dimension, att_rule_audit_type, att_rule_level } = proxy.useDict(
        'att_rule_audit_q_dimension',
        'att_rule_audit_type',
        'att_rule_level'
    );

    const loading = ref(true);
    const showSearch = ref(true);
    const defaultSort = ref({ prop: 'createTime', order: 'desc' });
    const total = ref(0);
    const dataList = ref([]);
    const filteredTreeOptionslist = ref([]);
    const checkedKeys = ref([]);
    const currentNodeKey = ref(null);
    const typeName = ref(null);

    const data = reactive({
        form: {
            name: null,
            level: null,
            type: null,
            qualityDim: null,
            ruleConfig: {
                nullCheck: {
                    type: '1'
                }
            }
        },
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            dataElemId: null,
            ruleType: 1
        },
        rules: {
            'ruleConfig.nullCheck.type': [{ required: true, message: '请选择', trigger: 'change' }]
        },
        defaultProps: {
            children: 'children',
            label: 'name',
            isLeaf: 'isLeaf' // 指定是否是叶子节点的字段名
        }
    });
    const { queryParams, form, rules, defaultProps } = toRefs(data);
    const open = ref(false);
    const title = ref('');
    const sourceName = ref('');
    queryParams.value.dataElemId = props.dataElemId;

    watch(sourceName, (val) => {
        proxy.$refs.tree.filter(val);
    });

    //表单提交
    function submitForm() {
        //获取选中后的节点
        var nodes = proxy.$refs.tree.getCheckedNodes();

        var saveData = [];
        nodes.forEach((node) => {
            var data = {
                id: node.ruleRelId || null,
                dataElemId: props.dataElemId,
                ruleType: props.ruleType,
                ruleId: node.id,
                ruleConfig: JSON.stringify(node.ruleConfig)
            };
            saveData.push(data);
        });
        //保存数据
        save(props.dataElemId, props.ruleType, saveData).then((res) => {
            if (res.code == 200) {
                proxy.$message.success('保存成功');
                open.value = false;
                getList();
            } else {
                proxy.$message.error(res.msg);
            }
        });
    }

    //节点选中事件
    async function handleNodeClick(data) {
        if (data.dataType != '2') {
            return;
        }
        //校验表单
        var flag = false;
        await proxy.$refs.dpDataElemRef.validate((valid) => {
            if (valid) {
                flag = true;
            }
        });
        if (!flag) {
            proxy.$message.error('请完善规则');
            return;
        }
        //保存上一个规则的数据
        var oldData = form.value;
        if (oldData) {
            updateTreeData(
                filteredTreeOptionslist.value,
                oldData.id,
                JSON.parse(JSON.stringify(oldData)).ruleConfig
            );
        }
        typeName.value = data.name;
        form.value = data;
        updateTreeDisable(filteredTreeOptionslist.value, data.id);
    }

    function updateTreeDisable(list, id) {
        list.forEach((item) => {
            if (item.children != null && item.children.length > 0) {
                updateTreeDisable(item.children, id);
                return;
            }
            if (item.id == id) {
                item.disabled = false;
            }
        });
    }

    function updateTreeData(list, id, ruleConfig) {
        list.forEach((item) => {
            if (item.children != null && item.children.length > 0) {
                updateTreeData(item.children, id, ruleConfig);
                return;
            }
            if (item.id == id) {
                item.ruleConfig = ruleConfig;
            }
        });
    }

    /** 字典翻译 */
    function formatValue(value, dict) {
        return proxy.selectDictLabel(dict, value);
    }

    /** 新增按钮操作 */
    function handleAdd() {
        reset();
        getTree();
        open.value = true;
        title.value = '关联稽核规则';
    }

    // 取消按钮
    function cancel() {
        open.value = false;
        reset();
    }

    // 表单重置
    function reset() {
        form.value = {
            name: null,
            level: null,
            type: null,
            qualityDim: null,
            ruleConfig: {
                nullCheck: {
                    type: '1'
                }
            }
        };
        typeName.value = null;
    }

    //查询数据元列表
    function getList() {
        loading.value = true;
        listDpDataElemRuleRel(queryParams.value).then((response) => {
            dataList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

    //获取tree数据
    function getTree() {
        treeAttAuditRule({ dataElemId: queryParams.value.dataElemId }).then((response) => {
            const tree = {};
            tree.id = 0;
            tree.dataType = '0';
            tree.name = '规则类型';
            if (response.data == undefined || response.data == null) {
                response.data = [];
            }
            var checkedKeyArr = [];
            buildTree(response.data, checkedKeyArr);
            tree.children = response.data;
            filteredTreeOptionslist.value = [];
            filteredTreeOptionslist.value.push(tree);
            checkedKeys.value = checkedKeyArr;
        });
    }

    function buildTree(list, checkedKeyArr) {
        list.forEach((item) => {
            if (item.children != null && item.children.length > 0) {
                buildTree(item.children, checkedKeyArr);
                return;
            }
            if (item.dataType != '2') {
                return;
            }
            if (!item.ruleRelId) {
                item.disabled = true;
            }
            if (item.ruleConfig) {
                item.ruleConfig = JSON.parse(item.ruleConfig);
            } else {
                item.ruleConfig = JSON.parse(JSON.stringify(form.value.ruleConfig));
            }
            if (!typeName.value) {
                typeName.value = item.name;
                nextTick(() => {
                    form.value = item;
                    currentNodeKey.value = item.id;
                    updateTreeDisable(filteredTreeOptionslist.value, item.id);
                });
            }
            if (item.ruleRelId) {
                checkedKeyArr.push(item.id);
            }
        });
    }

    function filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
    }

    getList();
</script>
<style lang="scss" scoped>
    .base-info {
        margin-top: 5px;
        .type-name {
            color: #000;
            font-size: 18px;
            font-weight: bold;
        }

        .base-content {
            margin-top: 20px;
            padding-left: 25px;
            :deep(.el-form-item__label) {
                padding: 0 0 0 0 !important;
            }
            :deep(.el-form-item) {
                margin-bottom: 5px;
            }
        }
    }

    .hint-div {
        margin: 10px 0px 20px 20px;
        border-top: 1px solid rgba(204, 204, 204, 0.5);
        border-right: 1px solid rgba(204, 204, 204, 0.5);
        border-bottom: 1px solid #e5f1f8;
        border-left: 1px solid #e5f1f8;
        border-radius: 2px;
        padding: 10px;
        box-shadow: -1px 1px 2px #e5f1f8;
        display: flex;
        align-items: center;
        span {
            margin-left: 5px;
        }
    }

    // 设置只有叶子节点有多选框
    :deep(.el-tree-node) {
        .is-leaf + .el-checkbox .el-checkbox__inner {
            display: inline-block !important;
        }

        .el-checkbox__input > .el-checkbox__inner {
            display: none;
        }
    }
</style>
