<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>

    <div class="app-container" style="background-color: #f0f2f5;" ref="app-container">
        <div class="custom-card">
            <div class="steps-inner">
                <ul class="zl-step" style="width: 100%">
                    <li v-for="(item, index) in stepsList" :key="index" :id="'li' + item.id" :class="{
                        statusEnd: activeReult === index,
                        prevStep: index < activeReult,
                        cur: index > activeReult
                    }" :style="{ width: 100 / stepsList.length + '%' }">
                        <!-- circle -->
                        <div class="step-circle" :class="{
                            active: activeReult === index,
                            prev: index < activeReult
                        }">
                            <span style="margin-top:-2px;">
                                {{ index + 1 }}
                            </span>
                        </div>

                        <!-- step name -->
                        <span>{{ item.name }}</span>

                        <!-- arrow -->
                        <div v-if="index < stepsList.length - 1" :id="'div' + item.id" :class="{
                            titleItem: activeReult === index,
                            prevJiao: index < activeReult,
                            jiaoActive: index > activeReult
                        }"></div>

                        <div class="interval"></div>
                    </li>
                </ul>

            </div>
        </div>

        <div class="pagecont-top" v-loading="loading" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="main">

                    <Base ref="base" v-if="activeReult === 0" :form1="form1" :rules1="rules1"
                        style="padding-right: 116px;" :reqMethodOptions="reqMethodOptions"
                        :resTypeOptions="resTypeOptions" :whetherOptions="whetherOptions" :statusOptions="statusOptions"
                        :isChange="true" :treeOptions="treeOptions" :typeNames="typeName" />
                    <Parameter ref="parameter" v-show="splReult === true || activeReult === 1" :form2="form2"
                        :rules="rules2" :configTypeOptions="configTypeOptions" :sourceOptions="sourceOptions"
                        :paramTypeOptions="paramTypeOptions" :whereTypeOptions="whereTypeOptions" :splReult="splReult"
                        :activeReult="activeReult" />
                    <Test ref="test" v-if="activeReult == 2 && form1.apiServiceType != 3" :form="form1"
                        :resTypeOptions="resTypeOptions" :whetherOptions="whetherOptions"
                        :statusOptions="statusOptions" />
                    <testapi ref="test" v-if="activeReult == 2 && form1.apiServiceType == 3" :data="form1"
                        :resTypeOptions="resTypeOptions" :whetherOptions="whetherOptions"
                        :statusOptions="statusOptions" />
                </div>
                <div slot="footer" class="button-style">
                    <el-button type="primary" @click="handleSuccess">{{ td('ds.apiEdit.returnToList') }}</el-button>
                    <el-button v-if="activeReult !== 0" @click="handleLastStep">{{ td('common.button.previousStep') }}
                    </el-button>
                    <el-button v-if="activeReult !== 2" @click="handleNextStep">{{ td('common.button.nextStep') }}
                    </el-button>
                    <el-button type="primary" v-if="activeReult === 2" @click="submitForm"
                        :loading="loadingOptions.loading">{{ td('ds.apiEdit.confirmAndExit') }}
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
} from '@/api/ds/api/api.js';
import {
    getDaDatasourceList,
    getTablesByDataSourceId
} from '@/api/da/dataSource/dataSource.js';
import { onBeforeRouteLeave, useRoute, useRouter } from 'vue-router';
import Base from '@/views/ds/api/edit/components/base.vue';
import Parameter from '@/views/ds/api/edit/components/parameter.vue';
import Test from '@/views/ds/api/edit/components/simulation.vue';
import { getCurrentInstance, reactive, ref, toRefs, watch } from 'vue';
import testapi from '@/views/ds/api/edit/components/testApi.vue';
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const components = { Base, Parameter, Test };
const { proxy } = getCurrentInstance();
const router = useRouter();

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
const handleSuccess = () => {
    router.push("/ds/api");
};
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
            name: td('ds.apiEdit.attributeConfig'),
            id: 1
        },
        {
            name: td('ds.apiEdit.parameterConfig'),
            id: 2
        },
        {
            name: td('ds.apiEdit.test'),
            id: 3
        }
    ],
    rules1: {
        name: [{ required: true, message: td('ds.apiEdit.apiNameRequired'), trigger: 'blur' }],
        apiVersion: [{ required: true, message: td('ds.apiEdit.apiVersionRequired'), trigger: 'blur' }],
        catCode: [{ required: true, message: td('ds.apiEdit.apiCategoryRequired'), trigger: 'blur' }],
        apiUrl: [
            { required: true, message: td('ds.apiEdit.apiUrlRequired'), trigger: 'input' },
            {
                pattern: '^\\/[a-zA-Z0-9_\\-]+(\\/[a-zA-Z0-9_\\-]+)*$',
                message: td('ds.apiEdit.apiUrlInvalid'),
                trigger: 'blur'
            }
        ],
        //Please enter a valid request path. The path must start with a slash (/) and can only contain letters, numbers, hyphens (-), periods (.), tildes (~), underscores (_),
        // And URL-safe special characters such as exclamation mark (!), dollar sign ($), ampersand (&), single quote ('), bracket (()()), asterisk (*), plus sign (+), comma (,),
        // Semicolon (;), equal sign (=), at symbol (@), and can also include a percent sign followed by two hexadecimal digits representing URL-encoded characters. The path cannot start with double slashes (//).
        // and can end with a slash (/)
        reqMethod: [{ required: true, message: td('ds.apiEdit.requestMethodRequired'), trigger: 'change' }],
        resType: [{ required: true, message: td('ds.apiEdit.returnFormatRequired'), trigger: 'change' }],
        resDataType: [{ required: true, message: td('ds.apiEdit.returnFormatRequired'), trigger: 'change' }],
        cacheSwitch: [{ required: true, message: td('ds.apiEdit.returnFormatRequired'), trigger: 'change' }]
    },
    rules2: {
        apiServiceType: [{ required: true, message: td('ds.apiEdit.configMethodRequired'), trigger: 'change' }],
        sourceId: [{ required: true, message: td('ds.apiEdit.dataSourceRequired'), trigger: 'change' }]
    },
    // Request method data dictionary
    reqMethodOptions: [],
    // Return format data dictionary
    resTypeOptions: [
        {
            itemValue: '1',
            itemText: td('ds.apiEdit.pagination')
        },
        {
            itemValue: '2',
            itemText: td('ds.apiEdit.list')
        },
        {
            itemValue: '3',
            itemText: td('common.button.details')
        }
    ],
    active: 0,
    activeReult: 0,
    splReult: false,
    // Whether data dictionary
    whetherOptions: [],
    // Status data dictionary
    statusOptions: [],
    // Data source data dictionary
    sourceOptions: [],
    // Database table data dictionary
    tableOptions: [],
    // Configuration mode data dictionary
    configTypeOptions: [],
    // operator data dictionary
    whereTypeOptions: [],
    // Parameter type data dictionary
    paramTypeOptions: [],
    filteredTableOptions: [], // Filtered database table options
    typeName: '',
    treeOptions: [],
    // save button
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
    (newVal) => { }
);

/** Form query at the top of the complex details page */
function getDsApiDetailById() {
    const _ID = id;
}

/** step bar previous step */
function handleLastStep() {
    activeReult.value--;
}

const handleClick = (tab, event) => {
    console.log(tab, event);
};

/**
 * step bar next step
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
                proxy.$message.warning(td('ds.apiEdit.opFailedCheck'));
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
                dbType: form2.sourceId,
                dbName: form2.sourceId,
                sid: form2.sourceId,
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
/** submit button */
function submitForm() {
    try {
        loading.value = true;
        if (activeReult.value >= 1) {
            proxy.$refs.parameter.validateFormParameter('form2', (f2) => {
                form2.value = { ...f2 };
                form1.value = {
                    ...form1.value,
                    sourceId: form2.value.sourceId,
                    executeConfig: form2.value,
                    isIntegrate: '0',
                    reqParams: form2.value.reqParams,
                    resParams: form2.value.resParams,
                    apiServiceType: form2.value.apiServiceType,
                    sortBy: form2.value.sortBy,
                    headerJson: form2.value.headerJson,
                    apiId: form2.value.apiId,
                    transmitType: form2.value.transmitType,
                };
            });
            let params = {};
            Object.assign(params, form1.value);
            params.reqParamsList = params.reqParams;
            params.resParamsList = params.resParams;
            //Remove reqParams and resParams
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
                //Delete creation and update time fields
                delete params.createTime;
                delete params.updateTime;
                updateDataApi(params)
                    .then((response) => {
                        if (response.code === 200) {
                            router.push({ path: '/ds/api' });
                            proxy.$message.success(td('ds.apiEdit.saveSuccess'));
                        } else {
                            proxy.$message.warning(td('ds.apiEdit.saveFailedRetry'));
                        }
                    })
                    .catch(() => {
                        loadingOptions.value.loading = false;
                    });
            } else {
                addDataApi(params)
                    .then((response) => {
                        if (response.code === 200) {
                            proxy.$message.success(td('ds.apiEdit.saveSuccess'));
                            router.push({ path: '/ds/api' });
                        } else {
                            proxy.$message.warning(td('ds.apiEdit.saveFailedRetry'));
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
        fullPath: path.join(' - '), // splicing path
        idArray: idArray // ID array
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
                // Set typeName and idArray
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
        let title = id ? td('ds.apiEdit.apiEditTitle') : td('ds.apiEdit.apiAddTitle');
        const _route = Object.assign({}, route, { title: title });
        proxy.$tab.updatePage(_route);
    },
    { immediate: true } // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);

//Query data source information
function getDatasource() {
    getDaDatasourceList().then((response) => {
        sourceOptions.value = response.data;
    });
}

getDatasource();

getDataApiById(id);

//Write a routing monitor when leaving this page, vue3

onBeforeRouteLeave((to, from) => {
    // Monitor route changes and destroy the current page if the route changes.
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
    height: 80px;
    padding: 20px 20px;
    step-height: 40px;
    border-radius: 4px;
    border: 0px solid #ebeef5;
    background-color: #fff;
    margin: 15px 15px -34px 15px;
}

.custom-card {
    width: 100%;
    height: 100px;
    padding: 34px 177px 26px 189px;
    background: #fff;
    box-sizing: border-box;
    margin-bottom: 15px;

    .steps-inner {
        padding: 0 10px;
        padding-left: 20px;
        display: flex;
        width: auto;
        color: #303133;
        transition: 0.3s;
        transform: translateZ(0);

        &::-webkit-scrollbar {
            height: 5px;
        }

        .zl-step {
            list-style: none;
            width: 100%;
            height: 20px;
            padding: 0;
            margin: 20px auto;
            cursor: pointer;
            display: flex;
            align-items: flex-end;

            li {
                position: relative;
                flex: 1;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                background: #d7d8da;
                color: #666;
                font-weight: 500;
                transition: background 0.3s;

                &:first-child {
                    z-index: 2;
                    clip-path: polygon(0 0, calc(100% - 20px) 0, 100% 50%, calc(100% - 20px) 100%, 0 100%);
                }

                &:not(:first-child):not(:last-child) {
                    margin-left: -10px;
                    clip-path: polygon(0 0, calc(100% - 20px) 0, 100% 50%, calc(100% - 20px) 100%, 0 100%);
                    z-index: 1;

                    &::before {
                        content: '';
                        position: absolute;
                        left: 0;
                        top: 0;
                        width: 20px;
                        height: 100%;
                        background: #fff;
                        clip-path: polygon(0 0, 100% 50%, 0 100%);
                        z-index: 2;
                    }
                }

                &:last-child {
                    margin-left: -10px;
                    z-index: 0;
                    clip-path: polygon(0 0, 100% 0, 100% 100%, 0 100%);

                    &::before {
                        content: '';
                        position: absolute;
                        left: 0;
                        top: 0;
                        width: 20px;
                        height: 100%;
                        background: #fff;
                        clip-path: polygon(0 0, 100% 50%, 0 100%);
                        z-index: 2;
                    }
                }

                &.statusEnd {
                    background: linear-gradient(270deg, #e9effe 0%, #5589FA 100%);
                    color: #2666FB !important;
                }

                &.prevStep {
                    background: #E9EFFE !important;
                    font-weight: normal;
                    font-size: 16px !important;
                    color: #2666FB !important;
                }

                &.cur {
                    background: #F1F1F5;
                    color: #404040;
                    font-weight: 500;
                }
            }
        }

        .step-circle {
            width: 26px;
            height: 26px;
            border-radius: 50%;
            background: #f1f1f5;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 18px;
            font-weight: bold;
            margin-right: 11px;
            border: 1px solid #b2b2b2;
            flex-shrink: 0;
            transition: all 0.3s;

            &.active {
                background: #2666fb;
                color: #fff;
                border: 1px solid #fff;
            }

            &.prev {
                background: #f1f1f5 !important;
                border: 1px solid #2666fb !important;
                color: #2666fb !important;
            }
        }

        .step-name {
            font-family: PingFang SC, PingFang SC;
            font-weight: 500;
            font-size: 16px;
        }
    }
}

.button-style {
    padding: 15px 35px 0px 0px;
    background: #fff;
    text-align: right;
    z-index: 10;
}

.main {
    flex: 1;
    // margin: 15px;
    background-color: white;
    padding: 10px 25px 0;
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

    // .blue-bar {
    //     background-color: #2666FB; // blue bar color
    //     width: 5px; // width 5px
    //     height: 20px; // height 20px
    //     margin-right: 10px; // Space between image and text
    // }
}

.option-item {
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
}

.pagecont-top {
    min-height: 600px;
    position: relative;
    padding-bottom: 40px;
}

.el-textarea__inner::-webkit-resizer {
    background: transparent;
    /* background transparent */
    border-width: 3px;
    /* line thickness */
    border-style: solid;
    border-color: transparent #2666FB #2666FB transparent;
}
</style>