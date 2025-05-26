<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    @click="handleAdd"
                    v-hasPermi="['genStudent:student:add']"
                    @mousedown="(e) => e.preventDefault()"
                >
                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="warning"
                    plain
                    @click="handleExport"
                    v-hasPermi="['genStudent:student:export']"
                    @mousedown="(e) => e.preventDefault()"
                >
                    <i class="iconfont-mini icon-download-line mr5"></i>导出
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar
                v-model:showSearch="showSearch"
                @queryTable="getList"
                :columns="columns"
            ></right-toolbar>
        </div>
    </div>
    <el-table
        stripe
        height="374px"
        v-loading="loading"
        :data="studentList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
        <el-table-column v-if="columns[1].visible" label="姓名" align="center" prop="name">
            <template #default="scope">
                {{ scope.row.name || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[2].visible"
            label="学生照"
            align="center"
            prop="pictureUrl"
            width="100"
        >
            <template #default="scope">
                <image-preview :src="scope.row.pictureUrl" :width="50" :height="50" />
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[3].visible"
            label="教育经历"
            align="center"
            prop="experience"
        >
            <template #default="scope">
                {{ scope.row.experience || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[4].visible" label="性别" align="center" prop="sex">
            <template #default="scope">
                <dict-tag :options="sys_user_sex" :value="scope.row.sex" />
            </template>
        </el-table-column>
        <el-table-column v-if="columns[5].visible" label="年龄" align="center" prop="age">
            <template #default="scope">
                {{ scope.row.age || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[6].visible" label="学号" align="center" prop="studentNumber">
            <template #default="scope">
                {{ scope.row.studentNumber || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[7].visible" label="班级" align="center" prop="grade">
            <template #default="scope">
                {{ scope.row.grade || '-' }}
            </template>
        </el-table-column>
        <el-table-column v-if="columns[8].visible" label="爱好" align="center" prop="hobby">
            <template #default="scope">
                <dict-tag
                    :options="message_level"
                    :value="scope.row.hobby ? scope.row.hobby.split(',') : []"
                />
            </template>
        </el-table-column>
        <el-table-column v-if="columns[11].visible" label="创建人" align="center" prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="columns[13].visible"
            label="创建时间"
            align="center"
            prop="createTime"
            width="180"
            sortable="custom"
            :sort-orders="['descending', 'ascending']"
        >
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
        </el-table-column>
        <el-table-column v-if="columns[17].visible" label="备注" align="center" prop="remark">
            <template #default="scope">
                {{ scope.row.remark || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            fixed="right"
            width="240"
        >
            <template #default="scope">
                <el-button
                    link
                    type="primary"
                    icon="Edit"
                    @click="handleUpdate(scope.row)"
                    v-hasPermi="['genStudent:student:edit']"
                    >修改</el-button
                >
                <el-button
                    link
                    type="danger"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['genStudent:student:remove']"
                    >删除</el-button
                >
                <el-button
                    link
                    type="primary"
                    icon="view"
                    @click="handleDetail(scope.row)"
                    v-hasPermi="['genStudent:student:edit']"
                    >详情</el-button
                >
            </template>
        </el-table-column>

        <template #empty>
            <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>暂无记录</p>
            </div>
        </template>
    </el-table>

    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改学生对话框 -->
    <el-dialog
        :title="title"
        v-model="open"
        width="800px"
        :append-to="$refs['app-container']"
        draggable
    >
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="studentRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="姓名" prop="name">
                        <el-input v-model="form.name" placeholder="请输入姓名" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="学生照" prop="pictureUrl">
                        <image-upload v-model="form.pictureUrl" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="教育经历">
                        <editor v-model="form.experience" :min-height="192" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="性别" prop="sex">
                        <el-select v-model="form.sex" placeholder="请选择性别">
                            <el-option
                                v-for="dict in sys_user_sex"
                                :key="dict.value"
                                :label="dict.label"
                                :value="parseInt(dict.value)"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="年龄" prop="age">
                        <el-input v-model="form.age" placeholder="请输入年龄" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="学号" prop="studentNumber">
                        <el-input v-model="form.studentNumber" placeholder="请输入学号" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="班级" prop="grade">
                        <el-input v-model="form.grade" placeholder="请输入班级" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="爱好" prop="hobby">
                        <el-checkbox-group v-model="form.hobby">
                            <el-checkbox
                                v-for="dict in message_level"
                                :key="dict.value"
                                :label="dict.value"
                            >
                                {{ dict.label }}
                            </el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
                    </el-form-item>
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

    <!-- 学生详情对话框 -->
    <el-dialog
        :title="title"
        v-model="openDetail"
        width="800px"
        :append-to="$refs['app-container']"
        draggable
    >
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="studentRef" :model="form" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="姓名" prop="name">
                        <div>
                            {{ form.name }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="学生照" prop="pictureUrl">
                        <image-preview :src="form.pictureUrl" :width="50" :height="50" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="教育经历">
                        <div v-html="form.experience"></div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="性别" prop="sex">
                        <dict-tag :options="sys_user_sex" :value="form.sex" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="年龄" prop="age">
                        <div>
                            {{ form.age }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="学号" prop="studentNumber">
                        <div>
                            {{ form.studentNumber }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="班级" prop="grade">
                        <div>
                            {{ form.grade }}
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="爱好" prop="hobby">
                        <dict-tag :options="message_level" :value="form.hobby" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="备注" prop="remark">
                        <div>
                            {{ form.remark }}
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="cancel">关 闭</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="ComponentOne">
    import {
        listStudent,
        getStudent,
        delStudent,
        addStudent,
        updateStudent
    } from '@/api/example/genStudent/student';

    const { proxy } = getCurrentInstance();
    const { sys_user_sex, message_level } = proxy.useDict('sys_user_sex', 'message_level');

    const studentList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 0, label: 'ID', visible: true },
        { key: 1, label: '姓名', visible: true },
        { key: 2, label: '学生照', visible: true },
        { key: 3, label: '教育经历', visible: true },
        { key: 4, label: '性别', visible: true },
        { key: 5, label: '年龄', visible: true },
        { key: 6, label: '学号', visible: true },
        { key: 7, label: '班级', visible: true },
        { key: 8, label: '爱好', visible: true },
        { key: 9, label: '是否有效', visible: true },
        { key: 10, label: '删除标志', visible: true },
        { key: 11, label: '创建人', visible: true },
        { key: 12, label: '创建人id', visible: true },
        { key: 13, label: '创建时间', visible: true },
        { key: 14, label: '更新人', visible: true },
        { key: 15, label: '更新人id', visible: true },
        { key: 16, label: '更新时间', visible: true },
        { key: 17, label: '备注', visible: true }
    ]);

    const open = ref(false);
    const openDetail = ref(false);
    const loading = ref(true);
    const showSearch = ref(true);
    const ids = ref([]);
    const single = ref(true);
    const multiple = ref(true);
    const total = ref(0);
    const title = ref('');
    const defaultSort = ref({ prop: 'createTime', order: 'desc' });

    const data = reactive({
        studentDetail: {},
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            name: null,
            pictureUrl: null,
            experience: null,
            sex: null,
            age: null,
            studentNumber: null,
            grade: null,
            hobby: null,
            createTime: null
        },
        rules: {
            name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
            age: [{ required: true, message: '年龄不能为空', trigger: 'blur' }],
            validFlag: [{ required: true, message: '是否有效不能为空', trigger: 'blur' }],
            delFlag: [{ required: true, message: '删除标志不能为空', trigger: 'blur' }],
            createTime: [{ required: true, message: '创建时间不能为空', trigger: 'blur' }],
            updateTime: [{ required: true, message: '更新时间不能为空', trigger: 'blur' }]
        }
    });

    const { queryParams, form, studentDetail, rules } = toRefs(data);

    /** 查询学生列表 */
    function getList() {
        loading.value = true;
        listStudent(queryParams.value).then((response) => {
            studentList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

    // 取消按钮
    function cancel() {
        open.value = false;
        openDetail.value = false;
        reset();
    }

    // 表单重置
    function reset() {
        form.value = {
            id: null,
            name: null,
            pictureUrl: null,
            experience: null,
            sex: null,
            age: null,
            studentNumber: null,
            grade: null,
            hobby: [],
            validFlag: null,
            delFlag: null,
            createBy: null,
            creatorId: null,
            createTime: null,
            updateBy: null,
            updaterId: null,
            updateTime: null,
            remark: null
        };
        proxy.resetForm('studentRef');
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        queryParams.value.pageNum = 1;
        getList();
    }

    /** 重置按钮操作 */
    function resetQuery() {
        proxy.resetForm('queryRef');
        handleQuery();
    }

    // 多选框选中数据
    function handleSelectionChange(selection) {
        ids.value = selection.map((item) => item.id);
        single.value = selection.length != 1;
        multiple.value = !selection.length;
    }

    /** 排序触发事件 */
    function handleSortChange(column, prop, order) {
        queryParams.value.orderByColumn = column.prop;
        queryParams.value.isAsc = column.order;
        getList();
    }

    /** 新增按钮操作 */
    function handleAdd() {
        reset();
        open.value = true;
        title.value = '新增学生';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getStudent(_id).then((response) => {
            form.value = response.data;
            form.value.hobby = form.value.hobby.split(',');
            open.value = true;
            title.value = '修改学生';
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value;
        getStudent(_id).then((response) => {
            form.value = response.data;
            form.value.hobby = form.value.hobby.split(',');
            openDetail.value = true;
            title.value = '学生详情';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['studentRef'].validate((valid) => {
            if (valid) {
                form.value.hobby = form.value.hobby.join(',');
                if (form.value.id != null) {
                    updateStudent(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {
                            form.value.hobby = form.value.hobby.split(',').map(String);
                        });
                } else {
                    addStudent(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('新增成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {
                            form.value.hobby = form.value.hobby.split(',').map(String);
                        });
                }
            }
        });
    }

    /** 删除按钮操作 */
    function handleDelete(row) {
        const _ids = row.id || ids.value;
        proxy.$modal
            .confirm('是否确认删除学生编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delStudent(_ids);
            })
            .then(() => {
                getList();
                proxy.$modal.msgSuccess('删除成功');
            })
            .catch(() => {});
    }

    /** 导出按钮操作 */
    function handleExport() {
        proxy.download(
            'example/student/export',
            {
                ...queryParams.value
            },
            `student_${new Date().getTime()}.xlsx`
        );
    }

    getList();
</script>
