<template>
    <div class="app-container" ref="app-container">

        <!-- 新用户引导内容展示 -->
        <GuideTip tip-id="da/daDatasource.list" />

        <div class="pagecont-top" v-show="showSearch">
            <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="100px"
                v-show="showSearch" @submit.prevent>
                <el-form-item label="数据源名称" prop="datasourceName">
                    <el-input class="el-form-input-width" v-model="queryParams.datasourceName" placeholder="请输入数据源名称"
                        clearable @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="数据源类型" prop="datasourceType">
                    <el-select class="el-form-input-width" v-model="queryParams.datasourceType" placeholder="请选择数据源类型"
                        clearable>
                        <el-option v-for="dict in datasource_type" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                    </el-button>
                    <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                    </el-button>
                </el-form-item>
            </el-form>
        </div>

        <div class="pagecont-bottom">
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['da:datasource:datasource:add']"
                            @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                    <!--         <el-col :span="1.5">-->
                    <!--           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['da:datasource:datasource:edit']"-->
                    <!--                      @mousedown="(e) => e.preventDefault()">-->
                    <!--             <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改-->
                    <!--           </el-button>-->
                    <!--         </el-col>-->
                    <!--         <el-col :span="1.5">-->
                    <!--           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['da:datasource:datasource:remove']"-->
                    <!--                      @mousedown="(e) => e.preventDefault()">-->
                    <!--             <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除-->
                    <!--           </el-button>-->
                    <!--         </el-col>-->
                </el-row>
                <div class="justify-end top-right-btn">
                    <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"
                        :columns="columns"></right-toolbar>
                </div>
            </div>
            <el-table stripe height="60vh" v-loading="loading" :data="daDatasourceList"
                @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
                <el-table-column v-if="getColumnVisibility(1)" width="80" label="编号" align="center" prop="id"
                    show-overflow-tooltip>
                    <template #default="scope">
                        {{ scope.row.id || '-' }}
                    </template>
                </el-table-column>
                <!--       <el-table-column type="selection" width="55" align="center" />-->
                <el-table-column v-if="getColumnVisibility(1)" width="340" label="数据源名称" align="left"
                    prop="datasourceName" show-overflow-tooltip>
                    <template #default="scope">
                        {{ scope.row.datasourceName || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(9)" label="数据源描述" align="left" prop="description"
                    show-overflow-tooltip>
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(4)" width="130" label="数据库IP" align="left" prop="ip"
                    show-overflow-tooltip>
                    <template #default="scope">
                        {{ scope.row.ip || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(2)" width="120" label="数据源类型" align="center"
                    prop="datasourceType">
                    <template #default="scope">
                        <dict-tag :options="datasource_type" :value="scope.row.datasourceType" />
                    </template>
                </el-table-column>
                <!-- <el-table-column
                    v-if="getColumnVisibility(2) && type == 1"
                    width="120"
                    label="所属项目"
                    align="center"
                    prop="projectName"
                >
                    <template #default="scope">
                        {{ scope.row.projectName || '-' }}
                    </template>
                </el-table-column> -->
                <el-table-column v-if="getColumnVisibility(13)" label="创建时间" align="center" prop="createTime"
                    width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
                    <template #default="scope">
                        <span>{{
                            parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}')
                            }}</span>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(14)" label="更新时间" align="center" prop="updateTime"
                    width="180">
                    <template #default="scope">
                        <span>{{
                            parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}')
                            }}</span>
                    </template>
                </el-table-column>

                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="205">
                    <template #default="scope">
                        <el-button link type="primary" icon="Connection" @click="handleTestConnection(scope.row)"
                            v-hasPermi="['da:datasource:datasource:edit']">测试连接</el-button>

                        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                            v-hasPermi="['da:datasource:datasource:edit']">详情</el-button>
                        <el-popover placement="bottom" :width="100" trigger="click">
                            <template #reference>
                                <el-button link type="primary" :disabled="scope.row.isAdminAddTo == false"
                                    icon="ArrowDown">
                                    <el-tooltip class="box-item" effect="dark" content="暂无权限" placement="top"
                                        :disabled="scope.row.isAdminAddTo != false">
                                        更多
                                    </el-tooltip>
                                </el-button>
                            </template>
                            <div class="butgdlist">
                                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                                    v-hasPermi="['da:datasource:datasource:edit']">修改</el-button>
                                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                                    v-hasPermi="['da:datasource:datasource:remove']">删除</el-button>
                            </div>
                        </el-popover>
                        <!--           <el-button link type="primary" icon="view" @click="routeTo('/da/datasource/daDatasourceDetail',scope.row)"-->
                        <!--                      v-hasPermi="['da:datasource:datasource:edit']">复杂详情</el-button>-->
                    </template>
                </el-table-column>

                <template #empty>
                    <div class="emptyBg">
                        <img src="@/assets/system/images/no_data/noData.png" alt="" />
                        <p>暂无记录</p>
                    </div>
                </template>
            </el-table>

            <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>

        <!-- 新增或修改数据源对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="daDatasourceRef" :model="form" :rules="rules" label-width="110px" @submit.prevent>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="数据源名称" prop="datasourceName">
                            <el-input v-model="form.datasourceName" placeholder="请输入数据源名称" />
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="数据源类型" prop="datasourceType">
                            <el-select v-model="form.datasourceType" placeholder="请选择数据源类型"
                                @change="handleDatasourceChange">
                                <el-option v-for="dict in datasource_type" :key="dict.value" :label="dict.label"
                                    :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20" v-if="form.datasourceType !== 'Kafka' && form.datasourceType !== 'HDFS'">
                    <el-col :span="12">
                        <el-form-item label="账号" prop="username">
                            <el-input v-model="form.username" placeholder="请输入账号" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="密码" prop="password">
                            <el-input v-model="form.password" placeholder="请输入密码" v-if="title === '新增数据源'" />
                            <el-input type="password" v-model="form.password" placeholder="请输入密码"
                                v-if="title !== '新增数据源'" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20" v-if="form.datasourceType !== 'Kafka' && form.datasourceType !== 'HDFS'">
                    <el-col :span="12" v-if="form.datasourceType !== null">
                        <el-form-item label="数据库名称" prop="dbname">
                            <el-input v-model="form.dbname" placeholder="请输入数据库名称" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" v-if="
                        form.datasourceType !== null &&
                        (form.datasourceType == 'Oracle' ||
                            form.datasourceType == 'Oracle11' ||
                            form.datasourceType == 'Kingbase8')
                    ">
                        <el-form-item label="模式" prop="sid">
                            <el-input v-model="form.sid" placeholder="请输入模式" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="数据库IP" prop="ip">
                            <el-input v-model="form.ip" placeholder="请输入数据库IP" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="端口号" prop="port">
                            <el-input v-model="form.port" placeholder="请输入端口号" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20"
                    v-if="form.datasourceType !== null && (form.datasourceType === 'Kafka' || form.datasourceType === 'HDFS')">
                    <el-col :span="20">
                        <el-form-item label="配置参数" prop="config">
                            <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" v-model="form.config"
                                :placeholder="form.datasourceType === 'Kafka' ? '例如: {&quot;security.protocol&quot;&colon;&quot;SASL_PLAINTEXT&quot;}' : '例如: {&quot;kerberosKeytabFilePath&quot;&colon;&quot;/path/to/keytab/file&quot;}'" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20" v-if="type == 0">
                    <el-col :span="24">
                        <el-form-item label="所属项目" prop="projectNameList">
                            <el-input style="width: 83.5%" v-model="form.projectNameList" placeholder="请选择项目" disabled>
                            </el-input>
                            <el-button style="margin-left: 12px" type="primary" @click="getListProject">选择项目</el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="数据源描述" prop="description">
                            <el-input type="textarea" :min-height="192" v-model="form.description"
                                placeholder="请输入数据源描述" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="备注">
                            <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" :min-height="192" />
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

        <!-- 数据源详情对话框 -->
        <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="daDatasourceRef" :model="form" label-width="110px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="数据源名称" prop="datasourceName">
                            <div>
                                {{ form.datasourceName }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="数据源类型" prop="datasourceType">
                            <dict-tag :options="datasource_type" :value="form.datasourceType" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="账号" prop="username">
                            <div>
                                {{ form.username }}
                            </div>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="密码" prop="password">
                            <div>***********</div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="数据库名称" prop="dbname">
                            <div>
                                {{ form.dbname }}
                            </div>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12" v-if="
                        form.datasourceType !== null &&
                        (form.datasourceType == 'Oracle' ||
                            form.datasourceType == 'Oracle11' ||
                            form.datasourceType == 'Kingbase8')
                    ">
                        <el-form-item label="模式" prop="sid">
                            <div>
                                {{ form.sid }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="数据库IP" prop="ip">
                            <div>
                                {{ form.ip }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="端口号" prop="port">
                            <div>
                                {{ form.port }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="数据源描述" prop="description">
                            <div>
                                {{ form.description }}
                            </div>
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

        <!-- 用户导入对话框 -->
        <el-dialog :title="upload.title" v-model="upload.open" width="800px" :append-to="$refs['app-container']"
            draggable destroy-on-close>
            <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
                :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
                :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
                <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <template #tip>
                    <div class="el-upload__tip text-center">
                        <div class="el-upload__tip">
                            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据源数据
                        </div>
                        <span>仅允许导入xls、xlsx格式文件。</span>
                        <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
                            @click="importTemplate">下载模板</el-link>
                    </div>
                </template>
            </el-upload>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="upload.open = false">取 消</el-button>
                    <el-button type="primary" @click="submitFileForm">确 定</el-button>
                </div>
            </template>
        </el-dialog>

        <el-dialog title="项目选择" v-model="openProject" width="1000px" draggable>
            <template>
                <span role="heading" aria-level="2" class="el-dialog__title"> 项目选择 </span>
            </template>
            <!--用户数据-->
            <el-form class="btn-style" :model="queryParamsProject" ref="queryRef" :inline="true" label-width="68px">
                <el-form-item label="项目名称" prop="name">
                    <el-input class="el-form-input-width" v-model="queryParamsProject.name" placeholder="请输入项目名称"
                        clearable @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="负责人" prop="managerId">
                    <el-select v-model="queryParamsProject.managerId" class="el-form-input-width" filterable
                        placeholder="请选择">
                        <el-option v-for="item in projectOptions" :key="item.userId" :label="item.nickName"
                            :value="item.userId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button plain type="primary" @click="handleQueryProject" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                    </el-button>
                    <el-button @click="resetQueryProject" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                    </el-button>
                </el-form-item>
            </el-form>
            <el-table ref="projectTableRef" stripe v-loading="loadingProject" :data="projectList"
                @selection-change="handleSelectionChangeProject">
                <el-table-column type="selection" width="55" :selectable="selectable" align="center" />
                <el-table-column label="编号" prop="id" width="80">
                    <template #default="scope">
                        {{ scope.row.id || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="项目名称" align="center" prop="name">
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>

                <el-table-column label="负责人" align="center" prop="managerId">
                    <template #default="scope">
                        {{ scope.row.nickName || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="联系方式" align="center" prop="managerPhone">
                    <template #default="scope">
                        {{ scope.row.managerPhone || '-' }}
                    </template>
                </el-table-column>
            </el-table>
            <pagination v-show="totalProject > 0" :total="totalProject" v-model:page="queryParamsProject.pageNum"
                v-model:limit="queryParamsProject.pageSize" @pagination="getListProject" />
            <template #footer>
                <div class="dialog-footer">
                    <el-button size="mini" @click="openProject = false">取 消</el-button>
                    <el-button type="primary" size="mini" @click="submitFormProject">确 定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="DaDatasource">
import {
    listDaDatasource,
    getDaDatasource,
    clientsTest,
    delDaDatasource,
    removeDppOrDa,
    addDaDatasource,
    updateDaDatasource,
    listDaDatasourceByProjectCode,
    noDppAdd
} from '@/api/da/datasource/daDatasource';
import { deptUserTree } from '@/api/system/system/user.js';
import { getToken } from '@/utils/auth.js';
import useUserStore from '@/store/system/user';

const userStore = useUserStore();
const { proxy } = getCurrentInstance();
const { datasource_type } = proxy.useDict('datasource_type');
const daDatasourceList = ref([]);

// 列显隐信息
const columns = ref([
    { key: 1, label: '数据源名称', visible: true },
    { key: 2, label: '数据源类型', visible: true },
    { key: 3, label: '数据源配置(json字符串)', visible: true },
    { key: 4, label: '数据库IP', visible: true },
    { key: 5, label: '端口号', visible: true },
    { key: 6, label: '数据库表数（预留）', visible: true },
    { key: 7, label: '同步记录数（预留）', visible: true },
    { key: 8, label: '同步数据量大小（预留）', visible: true },
    { key: 9, label: '数据源描述', visible: true },
    { key: 11, label: '创建人', visible: true },
    { key: 13, label: '创建时间', visible: true },
    { key: 17, label: '备注', visible: true }
]);

const getColumnVisibility = (key) => {
    const column = columns.value.find((col) => col.key === key);
    // 如果没有找到对应列配置，默认显示
    if (!column) return true;
    // 如果找到对应列配置，根据visible属性来控制显示
    return column.visible;
};

const open = ref(false);
const openProject = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const defaultSort = ref({ prop: 'createTime', order: 'desc' });
const router = useRouter();
const projectOptions = ref([]);
const projectList = ref([]);
const totalProject = ref(0);
const projectTableRef = ref(null);
const loadingProject = ref(false);
const projectIdAndCodeList = ref([]);
const route = useRoute();
let type = route.query.type || null;

/*** 用户导入参数 */
const upload = reactive({
    // 是否显示弹出层（用户导入）
    open: false,
    // 弹出层标题（用户导入）
    title: '',
    // 是否禁用上传
    isUploading: false,
    // 是否更新已经存在的用户数据
    updateSupport: 0,
    // 设置上传的请求头部
    headers: { Authorization: 'Bearer ' + getToken() },
    // 上传的地址
    url: import.meta.env.VITE_APP_BASE_API + '/da/daDatasource/importData'
});

const data = reactive({
    form: {
        projectNameList: [],
        projectIdList: [],
        projectList: []
    },
    queryParamsProject: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        managerId: null,
        datasourceId: null
    },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        datasourceName: null,
        datasourceType: null,
        datasourceConfig: null,
        ip: null,
        port: null,
        listCount: null,
        syncCount: null,
        dataSize: null,
        description: null,
        createTime: null
    },
    rules: {
        datasourceName: [{ required: true, message: '数据源名称不能为空', trigger: 'blur' }],
        datasourceType: [{ required: true, message: '数据源类型不能为空', trigger: 'change' }],
        datasourceConfig: [
            { required: true, message: '数据源配置(json字符串)不能为空', trigger: 'blur' }
        ],
        ip: [
            { required: true, message: '数据库IP不能为空', trigger: 'blur' },
            { pattern: /^[^\u4e00-\u9fa5]+$/, message: '数据库IP不能包含中文', trigger: 'blur' }
        ],
        port: [
            { required: true, message: '端口号不能为空', trigger: 'blur' },
            { pattern: /^\d{1,9}$/, message: '端口号必须为1-9位数字', trigger: 'blur' }
        ],
        username: [{ required: true, message: '账号不能为空', trigger: 'blur' }],
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
        dbname: [
            { required: true, message: '数据库名称不能为空', trigger: 'blur' },
            {
                pattern: /^[^\u4e00-\u9fa5]+$/,
                message: '数据库名称不能包含中文',
                trigger: 'blur'
            }
        ],
        sid: [{ required: true, message: '模式不能为空', trigger: 'blur' }],
        config: [
            {
                trigger: 'blur',
                validator: (rule, value, callback) => {
                    if (value === null || value === undefined || value === '') {
                        callback();
                        return;
                    }
                    var flag = false;
                    if (typeof value === 'string') {
                        try {
                            const obj = JSON.parse(value);
                            if (typeof obj === 'object' && obj) {
                                flag = true;
                            }
                        } catch (e) { }
                    }
                    if (flag) {
                        callback();
                    } else {
                        callback('不是一个正确的JSON格式');
                    }
                }
            }
        ]
    }
});

const { queryParams, form, rules, queryParamsProject } = toRefs(data);
const selectable = (row) => !row.dppAssigned;
// 监听 id 变化
watch(
    () => userStore.projectCode,
    (newCode) => {
        getList();
    },
    { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);

function getProjectOptions() {
    deptUserTree().then((response) => {
        projectOptions.value = response.data;
    });
}

//数据源类型change事件
function handleDatasourceChange(type) {
    if (type == 'Hive') {
        rules.value.password[0].required = false
    }
    if (type != 'Hive') {
        rules.value.password[0].required = true
    }
}

function getListProject() {
    openProject.value = true;
    loadingProject.value = true;
    noDppAdd(queryParamsProject.value).then((response) => {
        projectList.value = response.data.rows;
        totalProject.value = response.data.total;
        loadingProject.value = false;

        // 在表格加载完成后，设置之前选中的项目
        nextTick(() => {
            projectList.value.forEach((project) => {
                form.value.projectList.forEach((item) => {
                    if (item.projectId === project.id) {
                        proxy.$refs.projectTableRef.toggleRowSelection(project, true);
                    }
                });
            });
        });
    });
}

function handleSelectionChangeProject(selection) {
    projectIdAndCodeList.value = [];
    for (let i = 0; i < selection.length; i++) {
        const element = selection[i];
        let project = {
            projectId: element.id,
            projectCode: element.code
        };
        projectIdAndCodeList.value.push(project);
    }

    form.value.projectNameList = selection.map((item) => item.name);
}

function submitFormProject() {
    openProject.value = false;
    form.value.projectList = projectIdAndCodeList.value;
}

function handleQueryProject() {
    queryParamsProject.value.pageNum = 1;
    getListProject();
}

function resetQueryProject() {
    queryParamsProject.value.pageNum = 1;
    queryParamsProject.value.pageSize = 10;
    queryParamsProject.value.name = null;
    queryParamsProject.value.managerId = null;
    getListProject();
}

/** 查询数据源列表 */
function getList() {
    loading.value = true;
    if (type == 1) {
        queryParams.value.projectId = userStore.projectId;
        queryParams.value.projectCode = userStore.projectCode;
        listDaDatasourceByProjectCode(queryParams.value).then((response) => {
            daDatasourceList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    } else {
        listDaDatasource(queryParams.value).then((response) => {
            daDatasourceList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }
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
        projectNameList: [],
        projectIdList: [],
        projectList: [],
        datasourceName: null,
        datasourceType: null,
        datasourceConfig: null,
        ip: null,
        port: null,
        listCount: null,
        syncCount: null,
        dataSize: null,
        description: null,
        validFlag: null,
        createBy: null,
        creatorId: null,
        createTime: null,
        updateBy: null,
        updaterId: null,
        updateTime: null,
        remark: null
    };
    proxy.resetForm('daDatasourceRef');
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
    if (type == 1) {
        form.value.isDaOrDpp = true;
        form.value.projectList = [
            {
                projectId: userStore.projectId,
                projectCode: userStore.projectCode,
                dppAssigned: true
            }
        ];
    } else {
        form.value.isDaOrDpp = false;
        form.value.projectList = [];
    }
    open.value = true;
    title.value = '新增数据源';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDaDatasource(_id).then((response) => {
        form.value = response.data;
        form.value.projectIdList = form.value.projectList.map((item) => item.projectId);
        form.value.projectNameList = form.value.projectList.map((item) => item.projectName);
        // 拆解 datasourceConfig
        if (form.value.datasourceConfig) {
            const config = JSON.parse(form.value.datasourceConfig);
            console.log(config, 'config');
            form.value.username = config.username;
            form.value.password = config.password;
            form.value.dbname = config.dbname;
            form.value.sid = config.sid;
            if (form.value.datasourceType !== null && (form.value.datasourceType === 'Kafka' || form.value.datasourceType === 'HDFS')){
              form.value.config = form.value.datasourceConfig;
            }
        }
        form.value.projectListOld = form.value.projectIdList;
        queryParamsProject.value.datasourceId = form.value.id;
        open.value = true;
        title.value = '修改数据源';
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _id = row.id || ids.value;
    getDaDatasource(_id).then((response) => {
        form.value = response.data;
        if (form.value.datasourceConfig) {
            const config = JSON.parse(form.value.datasourceConfig);
            form.value.username = config.username;
            form.value.password = config.password;
            form.value.dbname = config.dbname;
            form.value.sid = config.sid;
        }
        openDetail.value = true;
        title.value = '数据源详情';
    });
}

/** 详情按钮操作 */
function handleTestConnection(row) {
    loading.value = true; // 开始加载
    reset();
    const _id = row.id || ids.value;
    clientsTest(_id).then((response) => {
        console.log(response);
        proxy.$modal.msgSuccess(response.msg);
    }).finally(() => {
        loading.value = false; // 结束加载
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['daDatasourceRef'].validate((valid) => {
        if (valid) {
            if (form.value.id != null) {
                form.value.datasourceConfig = JSON.stringify({
                    username: form.value.username,
                    password: form.value.password,
                    dbname: form.value.dbname,
                    sid: form.value.sid
                });
                let projectListOld = [];
                form.value.projectListOld.forEach((item) => {
                    if (!form.value.projectList.includes(item)) {
                        projectListOld.push(item);
                    }
                });
                if (form.value.datasourceType !== null && (form.value.datasourceType === 'Kafka' || form.value.datasourceType === 'HDFS')){
                  form.value.datasourceConfig = form.value.config;
                }
                form.value.projectListOld = projectListOld;
                updateDaDatasource(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                form.value.datasourceConfig = JSON.stringify({
                    username: form.value.username,
                    password: form.value.password,
                    dbname: form.value.dbname,
                    sid: form.value.sid
                });
              if (form.value.datasourceType !== null && (form.value.datasourceType === 'Kafka' || form.value.datasourceType === 'HDFS')){
                form.value.datasourceConfig = form.value.config;
              }
                addDaDatasource(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('新增成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            }
        }
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal
        .confirm('是否确认删除数据源编号为"' + _ids + '"的数据项？')
        .then(function () {
            return removeDppOrDa(_ids, type);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess('删除成功');
        })
        .catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download(
        'da/daDatasource/export',
        {
            ...queryParams.value
        },
        `daDatasource_${new Date().getTime()}.xlsx`
    );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
    upload.title = '数据源导入';
    upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
    proxy.download(
        'system/user/importTemplate',
        {},
        `daDatasource_template_${new Date().getTime()}.xlsx`
    );
}

/** 提交上传文件 */
function submitFileForm() {
    proxy.$refs['uploadRef'].submit();
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
    upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
    upload.open = false;
    upload.isUploading = false;
    proxy.$refs['uploadRef'].handleRemove(file);
    proxy.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
        response.msg +
        '</div>',
        '导入结果',
        { dangerouslyUseHTMLString: true }
    );
    getList();
};
/** ---------------------------------**/

function routeTo(link, row) {
    if (link !== '' && link.indexOf('http') !== -1) {
        window.location.href = link;
        return;
    }
    if (link !== '') {
        if (link === router.currentRoute.value.path) {
            window.location.reload();
        } else {
            router.push({
                path: link,
                query: {
                    id: row.id
                }
            });
        }
    }
}

queryParams.value.orderByColumn = defaultSort.value.prop;
queryParams.value.isAsc = defaultSort.value.order;
getList();
getProjectOptions();
</script>
