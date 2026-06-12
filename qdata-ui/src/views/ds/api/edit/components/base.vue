<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
-->

<template>
    <el-form ref="form1" :model="form1" :rules="rules1" label-width="170px">
        <!--        <el-row :gutter="20">-->
        <!--            <el-col :span="12">-->
        <!--                <el-form-item label="数据所属目录" prop="typeId" v-if="isChange">-->
        <!--                    <el-tree-select filterable v-model="form1.catCode"  :data="deptOptions"-->
        <!--                                    :props="{ value: 'code', label: 'name', children: 'children' }"   value-key="ID"-->
        <!--                                    placeholder="请选择所属类目" check-strictly/>-->
        <!--                </el-form-item>-->
        <!--            </el-col>-->
        <!--        </el-row>-->
        <el-row :gutter="20">
            <el-col :span="12">
                <el-form-item :label="td('ds.apiEdit.base.apiName')" prop="name">
                    <el-input v-model="form1.name" :placeholder="td('ds.apiEdit.base.apiNamePlaceholder')" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item :label="td('ds.apiEdit.base.apiVersion')" prop="apiVersion">
                    <el-input v-model="form1.apiVersion" :placeholder="td('ds.apiEdit.base.apiVersionPlaceholder')" />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <el-form-item :label="td('ds.apiEdit.base.apiAddress')" prop="apiUrl">
                    <el-input v-model="form1.apiUrl" :placeholder="td('ds.apiEdit.base.apiAddressPlaceholder')"
                        @input="handleApiUrlInput" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item :label="td('ds.apiEdit.base.apiCategory')" prop="catCode">
                    <el-tree-select filterable v-model="form1.catCode" :data="deptOptions"
                        :props="{ value: 'code', label: 'name', children: 'children' }" value-key="id"
                        :placeholder="td('ds.apiEdit.base.apiCategoryPlaceholder')" check-strictly @change="handleCatSelect" />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <el-form-item :label="td('ds.apiEdit.base.requestMethod')" prop="reqMethod">
                    <el-select v-model="form1.reqMethod" :placeholder="td('ds.apiEdit.base.requestMethodPlaceholder')">
                        <el-option v-for="dict in ds_api_bas_info_api_method_type" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item :label="td('ds.apiEdit.base.returnFormat')" prop="resDataType">
                    <el-select v-model="form1.resDataType" :placeholder="td('ds.apiEdit.base.returnFormatPlaceholder')">
                        <el-option v-for="dict in ds_api_bas_info_res_data_type" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="24">
                <el-form-item :label="td('common.texts.description')" prop="description">
                    <el-input v-model="form1.description" type="textarea" :placeholder="td('common.form.descriptionPlaceholder')" />
                </el-form-item>
            </el-col>
        </el-row>
        <!--        <el-form-item label="是否开启缓存：" prop="cacheSwitch">-->
        <!--            <el-radio-group v-model="form1.cacheSwitch">-->
        <!--                <el-radio v-for="dict in whetherOptions" :key="dict.id" :label="dict.itemText">{{ dict.itemValue-->
        <!--                    }}</el-radio>-->
        <!--            </el-radio-group>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="是否显示JSON样例" prop="sortColumn">-->
        <!--            <el-radio-group v-model="form1.sortColumn">-->
        <!--                <el-radio v-for="dict in cacheOptions" :key="dict.id" :label="dict.itemValue">-->
        <!--                    {{ dict.itemText }}-->
        <!--                </el-radio>-->
        <!--            </el-radio-group>-->
        <!--        </el-form-item>-->
        <el-row :gutter="20">
            <el-col :span="24">
                <el-form-item :label="td('ds.apiEdit.base.ipBlacklist')" prop="deny">
                    <el-input v-model="form1.deny" type="textarea" :placeholder="td('ds.apiEdit.base.ipBlacklistPlaceholder')" />
                </el-form-item>
            </el-col>
        </el-row>

        <el-row :gutter="20">
            <el-col :span="12">
                <el-form-item :label="td('ds.apiEdit.base.rateLimit')" prop="rateLimit">
                    <el-radio-group v-model="form1.rateLimit.enable">
                        <el-radio v-for="dict in ds_api_limit_status" :key="dict.value" :value="dict.value">{{
                            dict.label
                            }}</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item :label="td('common.texts.status')" prop="status">
                    <el-radio-group v-model="form1.status">
                        <el-radio v-for="dict in ds_api_status" :key="dict.value" :value="dict.value">{{
                            dict.label
                        }}</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
        </el-row>

        <el-row :gutter="20">
            <el-col :span="24" style="color: #333333;">
                <!-- class="input-number" -->
                <el-form-item v-if="form1.rateLimit.enable === '1'" :label="td('ds.apiEdit.base.rateLimitConfig')">
                    {{td('ds.apiEdit.base.seconds')}}&nbsp;
                    <el-input-number v-model="form1.rateLimit.seconds" :min="1" />
                    &nbsp;{{td('ds.apiEdit.base.withinSeconds')}}&nbsp;
                    <el-input-number v-model="form1.rateLimit.times" :min="1" />
                    &nbsp;{{td('ds.apiEdit.base.times')}}
                </el-form-item>
            </el-col>

        </el-row>
        <el-row :gutter="20">
            <el-col :span="24">
                <el-form-item :label="td('common.texts.remark')" prop="remark">
                    <el-input v-model="form1.remark" type="textarea" :placeholder="td('ds.apiEdit.base.remarkPlaceholder')" />
                </el-form-item>
            </el-col>
        </el-row>
    </el-form>
</template>

<script setup name="base">
import { listAttApiCat } from '@/api/ds/apiCat/apiCat';
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const {
    ds_api_bas_info_res_data_type,
    da_sensitive_status,
    ds_api_bas_info_api_method_type,
    ds_api_status,
    ds_api_limit_status
} = proxy.useDict(
    'ds_api_bas_info_res_data_type',
    'da_sensitive_status',
    'ds_api_bas_info_api_method_type',
    'ds_api_status',
    'ds_api_limit_status'
);

const props = defineProps({
    form1: {
        type: Object,
        default: () => {
            return {
                status: 1
            };
        }
    },
    rules1: {
        type: Object,
        required: true
    },
    reqMethodOptions: {
        type: Array,
        required: true
    },
    resTypeOptions: {
        type: Array,
        required: true
    },
    whetherOptions: {
        type: Array,
        required: true
    },
    statusOptions: {
        type: Array,
        required: true
    },
    isChange: {
        type: Boolean,
        default: false
    },
    treeOptions: {
        type: Array,
        default: () => []
    },
    idArray: {
        type: Array,
        default: () => []
    },
    typeNames: {
        type: String,
        default: () => ''
    }
});

const data = reactive({
    // 定义一个 data 用来存储 props 中的值
    localForm: { ...props.form1 }, // 用于管理表单数据
    typeName: props.typeNames, // 用于存储目录名称
    defaultProps: {
        children: 'children',
        label: 'name',
        isLeaf: 'isLeaf' // 指定是否是叶子节点的字段名
    },
    cacheOptions: [
        { id: 1, itemText: td('ds.apiEdit.base.cacheYes'), itemValue: 0 },
        { id: 2, itemText: td('ds.apiEdit.base.cacheNo'), itemValue: 1 }
    ],
    deptOptions: []
});

const { localForm, typeName, defaultProps, cacheOptions, deptOptions } = toRefs(data);

watch(
    () => props.form1,
    (newValue, oldValue) => {
        // 当 props 中的 form1 发生变化时，更新 localForm
        localForm.value = { ...newValue };
    }
);
props.form1.status = props.form1.status || '0';

function getApiCatList() {
    listAttApiCat().then((response) => {
        deptOptions.value = proxy.handleTree(response.data, 'id', 'parentId');
        deptOptions.value = [
            {
                name: td('ds.api.apiCategory'),
                value: '',
                id: 0,
                children: deptOptions.value
            }
        ];
    });
}
// 树形选项数据的规范化
function normalizeOptions(node) {
    if (node.children && !node.children.length) {
        delete node.children; // 去除没有子节点的空children属性
    }
    return {
        id: node.id,
        label: node.name,
        children: node.children
    };
}
function validateFormBase(formName, callback) {
    proxy.$refs[formName].validate((valid) => {
        if (valid) {
            callback(props.form1);
        } else {
            return false;
        }
    });
}
getApiCatList();
defineExpose({
    validateFormBase
});

// 处理API地址输入，过滤非法字符
const handleApiUrlInput = (value) => {
    console.log('value', value);

    if (value) {
        // 只保留字母、数字、下划线、中划线和斜杠
        const filteredValue = value.replace(/[^\w\-\/]/g, '');
        // 直接更新表单值
        props.form1.apiUrl = filteredValue;
    }
};

// 处理类目选择
const handleCatSelect = (value) => {
    // 在所有选项中查找匹配的类目
    const findCategory = (options, code) => {
        for (const option of options) {
            if (option.code === code) {
                return option;
            }
            if (option.children) {
                const found = findCategory(option.children, code);
                if (found) return found;
            }
        }
        return null;
    };

    const selectedCat = findCategory(deptOptions.value, value);
    console.log('selectedCat', selectedCat);

    if (selectedCat) {
        // 同时设置 catCode 和 catId
        props.form1.catCode = selectedCat.code;
        props.form1.catId = selectedCat.id;
    }
};
</script>

<style scoped>
.input-number {
    width: auto;
    max-width: 150px;
}
</style>