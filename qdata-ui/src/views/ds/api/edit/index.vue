<template>
    <div class="app-container" ref="app-container">
        <div
            class="pagecont-top"
            v-loading="loading"
            v-show="showSearch"
            style="padding-bottom: 15px"
        >
            <div class="infotop">
                <div class="steps-inner">
                    <ul class="zl-step" style="width: 99.5%">
                        <li
                            :id="'li' + item.id"
                            :class="
                                active >= index && activeReult != index
                                    ? 'cur'
                                    : activeReult == index
                                    ? 'statusEnd'
                                    : 'cur'
                            "
                            v-for="(item, index) in stepsList"
                            :style="{
                                width: '33%'
                            }"
                            :key="index"
                        >
                            <span>{{ item.name }}</span>
                            <div
                                :id="'div' + item.id"
                                v-if="index < stepsList.length - 1"
                                :class="
                                    active >= index && activeReult != index
                                        ? 'jiaoActive'
                                        : activeReult == index
                                        ? 'titleItem'
                                        : 'jiaoActive'
                                "
                            ></div>
                            <div class="interval"></div>
                        </li>
                    </ul>
                </div>

                <div class="main">
                    <Base
                        ref="base"
                        v-if="activeReult === 0"
                        :form1="form1"
                        :rules1="rules1"
                        :reqMethodOptions="reqMethodOptions"
                        :resTypeOptions="resTypeOptions"
                        :whetherOptions="whetherOptions"
                        :statusOptions="statusOptions"
                        :isChange="true"
                        :treeOptions="treeOptions"
                        :typeNames="typeName"
                    />
                    <Parameter
                        ref="parameter"
                        v-show="splReult === true || activeReult === 1"
                        :form2="form2"
                        :rules="rules2"
                        :configTypeOptions="configTypeOptions"
                        :sourceOptions="sourceOptions"
                        :paramTypeOptions="paramTypeOptions"
                        :whereTypeOptions="whereTypeOptions"
                        :splReult="splReult"
                        :activeReult="activeReult"
                    />
                    <Test
                        ref="test"
                        v-if="activeReult == 2 && form1.apiServiceType != 3"
                        :form="form1"
                        :resTypeOptions="resTypeOptions"
                        :whetherOptions="whetherOptions"
                        :statusOptions="statusOptions"
                    />
                    <testapi
                        ref="test"
                        v-if="activeReult == 2 && form1.apiServiceType == 3"
                        :data="form1"
                        :resTypeOptions="resTypeOptions"
                        :whetherOptions="whetherOptions"
                        :statusOptions="statusOptions"
                    />
                </div>
                <div slot="footer" class="button-style">
                    <el-button
                        type="primary"
                        v-if="activeReult === 1 || activeReult === 2"
                        style="margin-top: 12px"
                        @click="submitForm"
                        :loading="loadingOptions.loading"
                        >保存并退出
                    </el-button>
                    <el-button
                        :disabled="activeReult === 0"
                        style="margin-top: 12px"
                        @click="handleLastStep"
                        >上一步
                    </el-button>
                    <el-button
                        v-if="activeReult !== 2"
                        style="margin-top: 12px"
                        @click="handleNextStep"
                        >下一步
                    </el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup name="DsApi">
    import {
        getDsApi,
        updateDataApi,
        addDataApi,
        listDataTable,
        repeatFlag
    } from '@/api/ds/api/dsApi';
    import {
        getDaDatasourceList,
        getTablesByDataSourceId
    } from '@/api/da/datasource/daDatasource.js';
    import { onBeforeRouteLeave, useRoute } from 'vue-router';
    import Base from '@/views/ds/api/edit/components/base.vue';
    import Parameter from '@/views/ds/api/edit/components/parameter.vue';
    import Test from '@/views/ds/api/edit/components/test.vue';
    import { getCurrentInstance, reactive, ref, toRefs, watch } from 'vue';
    import testapi from '@/views/ds/api/edit/components/testapi.vue';

    const components = { Base, Parameter, Test };
    const { proxy } = getCurrentInstance();

    const {
        ds_api_log_status,
        ds_api_bas_info_api_service_type,
        ds_api_bas_info_api_method_type,
        ds_api_bas_info_res_data_type
    } = proxy.useDict(
        'ds_api_log_status',
        'ds_api_bas_info_api_service_type',
        'ds_api_bas_info_api_method_type',
        'ds_api_bas_info_res_data_type'
    );

    const activeName = ref('1');

    const showSearch = ref(true);
    const route = useRoute();
    let id = route.query.id || null;
    const loading = ref(false);

    const data = reactive({
        dsApiDetail: {},
        form1: {
            status: '0',
            rateLimit: {
                enable: '0'
            }
        },
        form2: {},
        typeOption: [],
        stepsList: [
            {
                name: '属性配置',
                id: 1
            },
            {
                name: '参数配置',
                id: 2
            },
            {
                name: '测试',
                id: 3
            }
        ],
        rules1: {
            name: [{ required: true, message: 'API名称不能为空', trigger: 'blur' }],
            apiVersion: [{ required: true, message: 'API版本不能为空', trigger: 'blur' }],
            catCode: [{ required: true, message: '类目不能为空', trigger: 'blur' }],
            apiUrl: [
                { required: true, message: '请输入API路径', trigger: 'blur' },
                {
                    pattern: /^\/[a-zA-Z0-9\/]+$/,
                    message: '请输入有效的请求路径,例：/user/list',
                    trigger: 'blur'
                }
            ],
            //请输入有效的请求路径。路径必须以斜杠（/）开头，只能包含字母、数字、连字符(-)、点号(.)、波浪号(~)、下划线(_)、
            // 以及URL安全的特殊字符如感叹号(!)、美元符号($)、和号(&)、单引号(')、括号(()())、星号(*)、加号(+)、逗号(,)、
            // 分号(;)、等号(=)、at符号(@)，还可以包含百分号后跟两位十六进制数表示的URL编码字符。路径不能以双斜杠(//)开始，
            // 并且可以以斜杠(/)结尾
            reqMethod: [{ required: true, message: '请求方式不能为空', trigger: 'change' }],
            resType: [{ required: true, message: '返回格式不能为空', trigger: 'change' }],
            resDataType: [{ required: true, message: '返回格式不能为空', trigger: 'change' }],
            cacheSwitch: [{ required: true, message: '返回格式不能为空', trigger: 'change' }]
        },
        rules2: {
            apiServiceType: [{ required: true, message: '配置方式不能为空', trigger: 'change' }],
            sourceId: [{ required: true, message: '数据源不能为空', trigger: 'change' }]
        },
        // 请求方式数据字典
        reqMethodOptions: [],
        // 返回格式数据字典
        resTypeOptions: [
            {
                itemValue: '1',
                itemText: '分页'
            },
            {
                itemValue: '2',
                itemText: '列表'
            },
            {
                itemValue: '3',
                itemText: '详情'
            }
        ],
        active: 0,
        activeReult: 0,
        splReult: false,
        // 是否数据字典
        whetherOptions: [],
        // 状态数据字典
        statusOptions: [],
        // 数据源数据字典
        sourceOptions: [],
        // 数据库表数据字典
        tableOptions: [],
        // 配置方式数据字典
        configTypeOptions: [],
        // 操作符数据字典
        whereTypeOptions: [],
        // 参数类型数据字典
        paramTypeOptions: [],
        filteredTableOptions: [], // 过滤后的数据库表选项
        typeName: '',
        treeOptions: [],
        // 保存按钮
        loadingOptions: {
            loading: false
        },
        headerJson: []
    });

    const {
        form1,
        form2,
        dsApiDetail,
        rules1,
        rules2,
        stepsList,
        active,
        activeReult,
        splReult,
        reqMethodOptions,
        resTypeOptions,
        whetherOptions,
        statusOptions,
        sourceOptions,
        tableOptions,
        configTypeOptions,
        whereTypeOptions,
        paramTypeOptions,
        filteredTableOptions,
        typeName,
        typeOption,
        treeOptions,
        loadingOptions
    } = toRefs(data);

    watch(
        () => form1.value.id,
        (newVal) => {}
    );

    /** 复杂详情页面上方表单查询 */
    function getDsApiDetailById() {
        const _ID = id;
    }

    /** 步骤条上一步 */
    function handleLastStep() {
        activeReult.value--;
    }

    const handleClick = (tab, event) => {
        console.log(tab, event);
    };

    /**
     * 步骤条下一步
     * proxy.$refs["dsApiRef"].validate(valid => {
     * */
    function handleNextStep() {
        if (activeReult.value === 0) {
            proxy.$refs.base.validateFormBase('form1', (f1) => {
                form1.value = { ...f1 };
            });
            repeatFlag({
                id: form1.value.id,
                name: form1.value.name,
                apiVersion: form1.value.apiVersion,
                apiUrl: form1.value.apiUrl
            }).then((response) => {
                if (response.code === 200) {
                    activeReult.value++;
                } else {
                    proxy.$message.error(response.msg);
                }
            });
        } else if (activeReult.value === 1) {
            proxy.$refs.parameter.validateFormParameter('form2', (f2) => {
                form2.value = { ...f2 };
                form1.value = {
                    ...form1.value,
                    apiId: form2.value?.apiId,
                    transmitType: form2.value?.transmitType,
                    sourceId: form2.sourceId,
                    executeConfig: form2,
                    isIntegrate: '0',
                    reqParams: form2.value.reqParams,
                    resParams: form2.value.resParams,
                    apiServiceType: form2.value.apiServiceType,
                    sortBy: form2.value.sortBy
                };
                activeReult.value++;
            });
        }
    }
    const router = useRouter();
    /** 提交按钮 */
    function submitForm() {
        try {
            loading.value = true;
            if (activeReult.value >= 1) {
                proxy.$refs.parameter.validateFormParameter('form2', (f2) => {
                    form2.value = { ...f2 };
                    form1.value = {
                        ...form1.value,
                        sourceId: form2.sourceId,
                        executeConfig: form2,
                        isIntegrate: '0',
                        reqParams: form2.value.reqParams,
                        resParams: form2.value.resParams,
                        apiServiceType: form2.value.apiServiceType,
                        sortBy: form2.value.sortBy,
                        headerJson: form2.value.headerJson,
                        apiId: form2.value.apiId,
                        transmitType: form2.value.transmitType
                    };
                });
                let params = {};
                Object.assign(params, form1.value);
                params.reqParamsList = params.reqParams;
                params.resParamsList = params.resParams;
                //删除reqParams和resParams
                delete params.reqParams;
                delete params.resParams;
                if (params.resParamsList == null) {
                    params.resParamsList = params.executeConfig.resParams;
                }
                if (params.reqParamsList == null) {
                    params.reqParamsList = params.executeConfig.reqParams;
                }
                params.resParams = JSON.stringify(params.executeConfig.resParams);
                params.reqParams = JSON.stringify(params.executeConfig.reqParams);
                params.headerJson = JSON.stringify(params.executeConfig.headerJson);
                params.configJson = JSON.stringify(params.executeConfig);
                if (form1.value.id) {
                    //删除创建、更新时间字段
                    delete params.createTime;
                    delete params.updateTime;
                    updateDataApi(params)
                        .then((response) => {
                            if (response.code === 200) {
                                router.push({ path: '/ds/dsApi' });
                                proxy.$message.success('保存成功');
                            } else {
                                proxy.$message.error(response.msg);
                            }
                        })
                        .catch(() => {
                            loadingOptions.value.loading = false;
                        });
                } else {
                    addDataApi(params)
                        .then((response) => {
                            if (response.code === 200) {
                                proxy.$message.success('保存成功');
                                router.push({ path: '/ds/dsApi' });
                            } else {
                                proxy.$message.error(response.msg);
                                loadingOptions.value.loading = false;
                            }
                        })
                        .catch(() => {
                            loadingOptions.value.loading = false;
                        });
                }
            }
        } catch (e) {
            console.log(e);

            loading.value = false;
            loadingOptions.value.loading = false;
        }
    }

    function getFullPathAndIdArray(optionList, targetObject) {
        typeName.value = '';
        idArray.value = [];
        const path = [];
        const idArray = [];

        function traverse(currentObject) {
            if (currentObject) {
                idArray.unshift(currentObject.id);
                path.unshift(currentObject.name);

                const parentObject = optionList.find((item) => item.id == currentObject.parentId);
                if (parentObject) {
                    traverse(parentObject);
                }
            }
        }

        traverse(targetObject);
        return {
            fullPath: path.join(' - '), // 拼接路径
            idArray: idArray // ID数组
        };
    }

    function getDataApiById(id) {
        console.log('🚀 ~ getDataApiById ~ id:', id);
        if (id == null) {
            form1.value = {};
            form2.value = {};
            activeReult.value = 0;
            form1.value.rateLimit = { enable: '0' };
            form1.status = 0;
            return;
        }
        getDsApi(id).then(async (response) => {
            if (response.code === 200) {
                form1.value = response.data;
                form1.value.rateLimit = { enable: '1', times: 5, seconds: 60 };
                form1.value.executeConfig = JSON.parse(form1.value.configJson);
                form1.value.reqParams = JSON.parse(form1.value.reqParams);
                form1.value.resParams = JSON.parse(form1.value.resParams);
                form1.value.headerJson = JSON.parse(form1.value.headerJson);
                form2.value = form1.value.executeConfig;
                form2.reqParams = form1.value.reqParams;
                form2.resParams = form1.value.resParams;
                form2.value.headerJson = form1.value.headerJson;
                if (form2.value.apiServiceType != '3') {
                    await getTablesByDataSourceId({
                        datasourceId: form2.value.sourceId
                    }).then((response) => {
                        if (response.code === 200) {
                            tableOptions.value = response.data;
                            form2.value.filteredTableOptions = response.data;
                        } else {
                            tableOptions.value = [];
                            form2.value.filteredTableOptions = [];
                        }
                    });
                }
                const targetObject = typeOption.value.find((item) => item.id == form1.typeId);
                if (targetObject) {
                    const { fullPath, idArray } = getFullPathAndIdArray(typeOption, targetObject);
                    // 设置typeName和idArray
                    typeName.value = fullPath;
                    idArray.value = idArray;
                }
            } else {
                form1.value = {};
            }
        });
    }

    watch(
        () => route.query.id,
        (newId) => {
            id = newId || null;
            getDataApiById(id);
            let title = id ? 'API服务修改' : 'API服务新增';
            const _route = Object.assign({}, route, { title: title });
            proxy.$tab.updatePage(_route);
        },
        { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
    );

    //查询数据源信息
    function getDatasource() {
        getDaDatasourceList().then((response) => {
            sourceOptions.value = response.data;
        });
    }

    getDatasource();

    getDataApiById(id);

    //写一个离开这个页面时的路由监听，vue3

    onBeforeRouteLeave((to, from) => {
        // 监听路由变化，如果路由变化，销毁当前页面
        if (to.path !== from.path) {
            getDataApiById(null);
        }
    });
</script>
<style lang="scss" scoped>
    .el-card ::v-deep .el-card__body {
        overflow-y: auto;
    }

    .steps-wrap {
        //width: 87.5vw;
        height: 80px;
        padding: 20px 20px;
        step-height: 40px;
        //box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        border-radius: 4px;
        border: 0px solid #ebeef5;
        background-color: #fff;
        margin: 15px 15px -34px 15px;
    }

    .steps-inner {
        display: flex;
        width: auto;
        //overflow: auto hidden;
        color: #303133;
        transition: 0.3s;

        // padding: 20px 0;
        //margin-left: 200px;
        &::-webkit-scrollbar {
            height: 5px;
        }

        .zl-step {
            list-style: none;
            font-size: 14px;
            height: 20px;
            display: flex;
            align-items: flex-end;
            margin: 20px auto;
            cursor: pointer;
            padding: 0;

            li {
                height: 40px;
                background: #d7d8da;
                color: #666;
                text-align: center;
                line-height: 40px;
                font-weight: 500;
                /*width: 20%;*/
                /*flex-basis: 100%;*/
                position: relative;
                padding-left: 10px;
            }

            .statusEnd {
                width: 33%;
                color: rgb(255, 255, 255);
                background: var(--el-color-primary) !important;
            }

            .cur {
                color: rgb(255, 255, 255);
                border-left-color: rgba(120, 140, 160, 0.2) !important;
            }

            .reult {
                color: rgb(102, 102, 102);
                background: rgb(215, 216, 218);
            }

            /*三角形绘制*/

            .jiao {
                width: 0;
                height: 0;
                border-top: 15px solid transparent;
                /*高度一半*/
                border-left: 20px solid #e8514a;
                /*调整宽度*/
                border-bottom: 15px solid transparent;
                /*高度一半*/
                color: rgb(102, 102, 102);
                border-left-color: rgb(215, 216, 218);
                position: absolute;
                right: -20px;
                /*跟宽度保持一致*/
                top: 0;
                z-index: 9999;
            }

            .interval {
                width: 0;
                height: 0;
                border-top: 26px solid transparent;
                /*高度一半*/
                border-left: 26px solid #fff;
                /*调整宽度*/
                border-bottom: 26px solid transparent;
                /*高度一半*/
                position: absolute;
                right: -26px;
                /*跟宽度保持一致*/
                top: -6px;
                z-index: 1;
            }

            .titleItem {
                width: 0;
                height: 0;
                border-top: 20px solid transparent;
                /*高度一半*/
                border-left: 20px solid #e8514a;
                /*调整宽度*/
                border-bottom: 20px solid transparent;
                /*高度一半*/
                color: rgb(255, 255, 255);
                border-left-color: var(--el-color-primary) !important;
                position: absolute;
                right: -20px;
                /*跟宽度保持一致*/
                top: 0;
                z-index: 2;
            }

            .jiaoActive {
                width: 0;
                height: 0;
                border-top: 20px solid transparent;
                /*高度一半*/
                border-left: 20px solid #e8514a;
                /*调整宽度*/
                border-bottom: 20px solid transparent;
                /*高度一半*/
                color: rgb(255, 255, 255);
                border-left-color: #d7d8da !important;

                position: absolute;
                right: -20px;
                /*跟宽度保持一致*/
                top: 0;
                z-index: 2;
            }
        }
    }

    .button-style {
        margin: -15px 15px 15px 15px;
        background-color: white;
        text-align: right;
        padding: 20px;
    }

    .main {
        flex: 1;
        overflow-y: scroll;
        margin: 15px;
        background-color: white;
        padding: 15px 25px 0;
    }

    .home {
        display: flex;
        flex-direction: column;
        height: 88vh;

        .clearfix {
            width: 100%;
            height: 36px;
            background-color: #f8f8f9;
            display: flex;
            align-items: center;
            padding-left: 10px;
            margin-bottom: 10px;
        }

        .clearfix span {
            display: flex;
            align-items: center;
        }

        .blue-bar {
            background-color: #2666fb; // 蓝条颜色
            width: 5px; // 宽度5px
            height: 20px; // 高度20px
            margin-right: 10px; // 图片与文字之间的间距
        }
    }

    .option-item {
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
    }
</style>
