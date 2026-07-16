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
  <!-- Add or modify data asset map task dialog box -->
  <el-dialog
      :title="dialogTitle"
      v-model="visibleDialog"
      class="medium-dialog"
      draggable
      destroy-on-close
  >
    <el-form
        ref="daDiscoveryTaskRef"
        :model="form"
        label-width="110px"
        @submit.prevent
        class="column-form"
     :label-position="labelPosition">
      <el-form-item :label="td('dpp.asset.add.assetType')" prop="type" :label-position="labelPosition">
        <el-select
            v-model="form.type"
            :placeholder="td('dpp.asset.add.assetTypePlaceholder')"
            filterable
            disabled
            style="width: 100%"
        >
          <el-option
              v-for="dict in da_asset_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <component
          :is="currentFormComponent"
          v-model:form="form"
          ref="ApiConfigRef"
          v-if="form.createType == '2'"
          :isRegister="props.isRegister"
          :type="props.type"
      />

      <qt-form-item
          :label="td('dpp.asset.add.assetStatus')"
          prop="status"
          :rules="[
          {
            required: true,
            message: td('dpp.asset.add.assetStatusRequired'),
            trigger: 'change',
          },
        ]"
          :tip="{
          content: td('dpp.asset.add.assetStatusTip'),
          custom: true,
        }"
      >
        <el-radio-group v-model="form.status">
          <el-radio value="1">{{ td('dpp.asset.add.assetStatusUnpublished') }}</el-radio>
          <el-radio value="2">{{ td('dpp.asset.add.assetStatusPublished') }}</el-radio>
        </el-radio-group>
      </qt-form-item>
<!--      <el-form-item-- :label-position="labelPosition">
<!--          v-if="!props.isRegister"-->
<!--          label="Creation type"-->
<!--          prop="status"-->
<!--          :rules="[-->
<!--          {-->
<!--            required: true,-->
<!--            message: 'Please select the creation type',-->
<!--            trigger: 'change',-->
<!--          },-->
<!--        ]"-->
<!--          :tip="{-->
<!--          content: `Registered assets: formally included in asset management, supporting subsequent governance, quality monitoring, and authority control<br/> Temporarily not registering assets: only recording information, not participating in subsequent processes, suitable for temporary testing or non-core assets [uncertain]`,-->
<!--          custom: true,-->
<!--        }"-->
<!--      >-->
<!--        <el-radio-group-->
<!--            v-model="form.createType"-->
<!--            :disabled="form.id"-->
<!--            @change="handleCreateChange"-->
<!--        >-->
<!--          <el-radio value="1">No asset registration yet</el-radio>-->
<!--          <el-radio value="2">Registered assets</el-radio>-->
<!--        </el-radio-group>-->
<!--      </el-form-item>-->

      <template
          v-if="
          form.type == '111' && (form.id != undefined || form.createType == '2')
        "
      >
        <el-form-item
            :label="td('dpp.asset.add.serviceType')"
            prop="daAssetGis.type"
            :rules="[
            { required: true, message: td('dpp.asset.add.serviceTypeRequired'), trigger: 'blur' },
          ]"
         :label-position="labelPosition">
          <el-select
              v-model="form.daAssetGis.type"
              :placeholder="td('dpp.asset.add.paramTypePlaceholder')"
          >
            <el-option
                v-for="dict in da_asset_gis_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
            :label="td('dpp.asset.add.serviceUrl')"
            prop="daAssetGis.url"
            :rules="[
            { required: true, message: td('dpp.asset.add.serviceUrlRequired'), trigger: 'blur' },
          ]"
         :label-position="labelPosition">
          <el-input
              v-model="form.daAssetGis.url"
              :placeholder="td('dpp.asset.add.serviceUrlPlaceholder')"
          />
        </el-form-item>
        <el-form-item
            :label="td('dpp.asset.add.httpMethod')"
            prop="daAssetGis.type"
            :rules="[
            { required: true, message: td('dpp.asset.add.httpMethodRequired'), trigger: 'blur' },
          ]"
         :label-position="labelPosition">
          <el-select
              v-model="form.daAssetGis.httpMethod"
              :placeholder="td('dpp.asset.add.httpMethodPlaceholder')"
          >
            <el-option
                v-for="dict in da_asset_api_method"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </template>

      <template
          v-if="
          form.type == '111' && (form.id != undefined || form.createType == '2')
        "
      >
        <el-form-item
            :label="td('dpp.asset.add.fileType')"
            prop="daAssetGeo.fileType"
            :rules="[
            { required: true, message: td('dpp.asset.add.fileTypeRequired'), trigger: 'blur' },
          ]"
         :label-position="labelPosition">
          <el-select
              v-model="form.daAssetGeo.fileType"
              :placeholder="td('dpp.asset.add.paramTypePlaceholder')"
          >
            <el-option
                v-for="dict in da_asset_geo_file_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
            :label="td('dpp.asset.add.uploadFile')"
            prop="daAssetGeo.fileUrl"
            :rules="[
            { required: true, message: td('dpp.asset.add.uploadFileRequired'), trigger: 'fileUrl' },
          ]"
         :label-position="labelPosition">
          <FileUploadbtn
              :limit="1"
              v-model="form.daAssetGeo.fileUrl"
              :dragFlag="false"
              :fileType="['geojson']"
              :fileSize="50"
              :isShowTip="false"
              v-model:fileSize="form.fileSize"
              v-model:fileExt="form.fileType"
          />
        </el-form-item>
      </template>

      <excelAdd ref="excelAddRef" class="row-full" />

      <el-form-item :label="td('common.texts.description')" prop="description" class="row-full" :label-position="labelPosition">
        <el-input
            type="textarea"
            :maxlength="500"
            show-word-limit
            :min-height="192"
            v-model="form.description"
            :placeholder="td('common.form.descriptionPlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full" :label-position="labelPosition">
        <el-input
            type="textarea"
            :maxlength="500"
            show-word-limit
            :min-height="192"
            v-model="form.remark"
            :placeholder="td('common.form.remarkPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <!-- close button -->
        <el-button @click="closeDialog">{{ td('common.button.cancel') }}</el-button>
        <!-- save button -->
        <el-button type="primary" @click="saveData" :loading="loading"
        >{{ td('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { defineProps, defineEmits, ref, computed, watch } from "vue";
// import Crontab from "@/components/Crontab/ruleBase.vue";
// import { getDaDiscoveryTask } from "@/api/da/discovery/discoveryTask";
// database table
import tableConfigForm from "./tableAdd.vue";
// external api
import apiConfigForm from "./apiAdd.vue";
// video
import daAssetVideo from "./videoAdd.vue";
// Vector data upload
import excelAdd from "./excelAdd.vue";
// unstructured data
import Unstructured from "./unstructuredAdd.vue";
import { getThemeList } from "@/api/att/theme/theme.js";
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
const { proxy } = getCurrentInstance();
const {
  da_asset_type,
  da_asset_gis_type,
  da_asset_geo_file_type,
  da_asset_api_method,
} = proxy.useDict(
    "da_asset_type",
    "da_asset_gis_type",
    "da_asset_geo_file_type",
    "da_asset_api_method"
);
import { addDaAsset, updateDaAsset, bindResources } from "@/api/da/asset/asset";

const { td } = useDefaultLang();
// import ExcelUploadDialog from "@/views/dpp/etl/components/formComponents/components/ExcelUploadDialog.vue";
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: '' },
  data: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  type: { type: Boolean, default: false },
  isEdit: { type: Boolean, default: false },
  isRegister: { type: Boolean, default: false },
});

const dialogTitle = computed(() => props.title || td('dpp.asset.add.formTitle'));
const excelAddRef = ref(null);
let loading = ref(false); // Loading status (global)
const isInitializing = ref(false);
const isResetting = ref(false);

const emit = defineEmits(["update:visible", "confirm"]);

const daDiscoveryTaskRef = ref(null);
let ApiConfigRef = ref();

// Create a local responsive data to modify the form content
const form = ref({
  type: "1",
  createType: "2",
  catCode: "",
  sourceType: "0",
  name: "",
  themeIdList: [],
  status: "1",
  description: "",
  source: "3",
  // 1
  tableName: "",
  datasourceId: "",
  tableComment: "",
  datasourceType: "",
  dbname: "",

  // Add new field
  tableType: "1",
  dataLayerId: null,
  businessDomainId: null,
  businessCategoryCode: "",
  dataDomainId: null,
  themeDomainId: null,
  themeDomainCode: "",
  tableCase: 1,
  namingSpec: "",

  // 2
  daAssetApi: {
    appName: "",
    url: "",
    httpMethod: "",
    developerName: "",
  },
  daAssetApiParamList: [],
  // 4
  daAssetGeo: {
    fileUrl: "",
    fileType: "",
    elementType: "",
    coordinateSystem: "",
    example: "",
    fileName: "",
  },
  daAssetGis: {
    url: "",
    type: "",
    httpMethod: "",
    coordinateSystem: "",
    remark: "",
  },
  daAssetVideo: {
    ip: "",
    port: "",
    protocol: "",
    platform: "",

    config: {
      cameraName: "",
      cameraCode: "",
      appkey: "",
      appSecret: "",
      artemisPath: "",
    },
  },
  daAssetFiles: {
    url: null,
    startData: "",
    tableFields: [],
    startColumn: "",
  },
  //   7
  fileInfo: {},
  filePath: "",
});

const currentFormComponent = computed(() => {
  switch (form.value.type) {
    case "1":
      return tableConfigForm;
    case "2":
      return apiConfigForm;
      // case "5":
      //   return daAssetVideo;
      // case "6":
      //   return excelAdd;
    case "7":
      return Unstructured;
    default:
      return null;
  }
});

let themeList = ref([]);
async function getAssetThemeList() {
  const response = await getThemeList();
  themeList.value = response.data;
  excelAddRef.value.show(form.value);
}
// eslint-disable-next-line no-unused-vars
const createTypeList = ref([]); // Data source list
// const getDatasourceList = async () => {
//   try {
//     loading.value = true;
//     const response = await listDaDatasourceNoKafkaByProjectCode({
//       projectCode: userStore.projectCode,
//       projectId: userStore.projectId,
//     });
//     createTypeList.value = response.data || [];
//   } finally {
//     loading.value = false;
//   }
// };
let openCron = ref(false);
const expression = ref("");
/** Scheduling cycle button operation */
// eslint-disable-next-line no-unused-vars
function handleShowCron() {
  expression.value = form.value.cronExpression;
  openCron.value = true;
}
/** Return value after confirmation */
// eslint-disable-next-line no-unused-vars
function crontabFill(value) {
  form.value.cronExpression = value;
}
watch(
    () => props.visible,
    async (newVal) => {
      if (newVal) {
        isInitializing.value = true;
        getAssetThemeList();
        if (props.data.id) {
          // props.data.id
        } else {
          form.value.catCode = props.data.catCode || "";
          console.log("🚀 ~ props.data:", props.data.catCode);
        }
        isInitializing.value = false;
      } else {
        clearForm();
      }
    }
);
const handleTypeChange = () => {
  // Clear form fields
  form.value.tableName = "";
  form.value.datasourceId = "";
  form.value.tableComment = "";
  form.value.datasourceType = "";
  form.value.dbname = "";
  form.value.daAssetApi = {
    appName: "",
    url: "",
    httpMethod: "",
    developerName: "",
  };
  form.value.daAssetApiParamList = [];
  form.value.daAssetGeo = {
    fileUrl: "",
    fileType: "",
    elementType: "",
    coordinateSystem: "",
    example: "",
    fileName: "",
  };
  form.value.daAssetGis = {
    url: "",
    type: "",
    httpMethod: "",
    coordinateSystem: "",
    remark: "",
  };
  form.value.daAssetVideo = {
    ip: "",
    port: "",
    protocol: "",
    platform: "",
    cameraName: "",
    cameraCode: "",
    artemisPath: "",
    config: {
      cameraName: "",
      cameraCode: "",
      appkey: "",
      appSecret: "",
      artemisPath: "",
    },
  };
  form.value.daAssetFiles = {
    url: null,
    startData: "",
    tableFields: [],
    startColumn: "",
  };
  excelAddRef.value && excelAddRef.value.show(form.value);
};
const handleCreateChange = () => {
  excelAddRef.value && excelAddRef.value.show(form.value);
};
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// How to close a dialog box
const closeDialog = async () => {
  emit("update:visible", false);
};

watch(
    () => props.data,
    (newVal) => {
      if (newVal && Object.keys(newVal).length > 0) {
        isInitializing.value = true;
        if (props.data.id) {
          form.value = JSON.parse(JSON.stringify(newVal));
          form.value.datasourceId = Number(form.value.datasourceId) || "";
          form.value.misfirePolicy = Number(form.value.misfirePolicy) || "";
          form.value.concurrent = Number(form.value.concurrent) || "";
          // form.value.status = Number(form.value.status) || "";
          form.value.themeIdList =
              form.value?.daAssetThemeRelList?.map((item) =>
                  Number(item.themeId)
              ) || [];
          form.value.createType = props.isRegister ? "2" : form.value.createType;

          // Echo new fields
          form.value.tableType = form.value.tableType || "1";
          form.value.dataLayerId = form.value.dataLayerId
              ? Number(form.value.dataLayerId)
              : null;
          form.value.businessDomainId = form.value.businessDomainId
              ? Number(form.value.businessDomainId)
              : form.value.businessCategoryId
                  ? Number(form.value.businessCategoryId)
                  : null;
          form.value.dataDomainId = form.value.dataDomainId
              ? Number(form.value.dataDomainId)
              : null;
          form.value.themeDomainId = form.value.themeDomainId
              ? Number(form.value.themeDomainId)
              : null;
          form.value.tableCase =
              form.value.tableCase !== undefined ? Number(form.value.tableCase) : 1;

          // Video configuration processing
          if (props.data.type == "5") {
            if (form.value.daAssetVideo == null) {
              form.value.daAssetVideo = {
                ip: "",
                port: "",
                protocol: "",
                platform: "",
                cameraName: "",
                cameraCode: "",
                artemisPath: "", //service context
                config: JSON.stringify({
                  cameraName: "",
                  cameraCode: "",
                  artemisPath: "",
                }),
              };
            } else {
              form.value.daAssetVideo.config = JSON.parse(
                  props.data?.daAssetVideo?.config
              );
            }
          }
          if (form.value.daAssetGis == null) {
            form.value.daAssetGis = {
              url: "",
              type: "",
              httpMethod: "",
              coordinateSystem: "",
              remark: "",
            };
          }
          if (form.value.daAssetApi == null) {
            form.value.daAssetApi = {
              appName: "",
              url: "",
              httpMethod: "",
              developerName: "",
            };
          }
          if (form.value.daAssetGeo == null) {
            form.value.daAssetGeo = {
              fileUrl: "",
              fileType: "",
              elementType: "",
              coordinateSystem: "",
              example: "",
              fileName: "",
            };
          }
        } else {
          // Add a new mode. If type is passed in, it will be synchronized to form.
          if (newVal.type) {
            form.value.type = newVal.type;
          }
          if (newVal.catCode) {
            form.value.catCode = newVal.catCode;
          }
        }
        setTimeout(() => {
          isInitializing.value = false;
        }, 200);
      }
    },
    {
      immediate: true,
      deep: true,
    }
);
function removeIdFields(data) {
  if (!Array.isArray(data)) return [];
  return data.map((item) => {
    if (item == null || typeof item != "object") return item;
    const newItem = { ...item };
    delete newItem.id;
    delete newItem.parentId;
    for (const key in newItem) {
      if (Array.isArray(newItem[key])) {
        newItem[key] = removeIdFields(newItem[key]);
      }
    }
    return newItem;
  });
}

function getFormDataByType(type) {
  const commonFields = {
    type: form.value.type,
    catCode: form.value.catCode,
    sourceType: form.value.sourceType,
    name: form.value.name,
    status: form.value.status,
    createType: form.value.createType,
    description: form.value.description,
    source: form.value.source,
    id: form.value.id,
    themeIdList: form.value.themeIdList,

    // Add new field
    tableType: form.value.tableType,
    dataLayerId: form.value.dataLayerId,
    businessDomainId: form.value.businessDomainId,
    businessCategoryId: form.value.businessDomainId, // Compatibility mapping
    businessCategoryCode: form.value.businessCategoryCode,
    dataDomainId: form.value.dataDomainId,
    themeDomainId: form.value.themeDomainId,
    themeDomainCode: form.value.themeDomainCode,
    tableCase: form.value.tableCase,
    namingSpec: form.value.namingSpec,
  };

  switch (type) {
    case "1":
      return {
        ...commonFields,
        tableName: form.value.tableName,
        datasourceId: form.value.datasourceId,
        tableComment: form.value.tableComment,
        datasourceType: form.value.datasourceType,
        dbname: form.value.dbname,
      };
    case "2":
      return {
        ...commonFields,
        daAssetApi: { ...form.value.daAssetApi },
        daAssetApiParamList: form.value.daAssetApiParamList,
      };
    case "3":
      return {
        ...commonFields,
        daAssetGis: { ...form.value.daAssetGis },
      };
    case "4":
      return {
        ...commonFields,
        daAssetGeo: { ...form.value.daAssetGeo },
      };
    case "5":
      return {
        ...commonFields,
        daAssetVideo: {
          ip: form.value.daAssetVideo.ip,
          port: form.value.daAssetVideo.port,
          protocol: form.value.daAssetVideo.protocol,
          platform: form.value.daAssetVideo.platform,
          cameraName: form.value.daAssetVideo.config.cameraName,
          cameraCode: form.value.daAssetVideo.config.cameraCode,
          artemisPath: form.value.daAssetVideo.config.artemisPath,
          config: JSON.stringify({
            cameraName: form.value.daAssetVideo.config.cameraName,
            cameraCode: form.value.daAssetVideo.config.cameraCode,
            artemisPath: form.value.daAssetVideo.config.artemisPath,
          }),
        },
      };
    case "6":
      return {
        ...commonFields,

        daAssetFiles: {
          url: proxy.$refs.excelAddRef.form.daAssetFiles.url,
          startData: proxy.$refs.excelAddRef.form.daAssetFiles.startData,
          startColumn: proxy.$refs.excelAddRef.form.daAssetFiles.startColumn,
          type: proxy.$refs.excelAddRef.form.daAssetFiles.type,
          name: proxy.$refs.excelAddRef.form.daAssetFiles.name,
        },
      };
    case "7": {
      const fileInfo = ApiConfigRef.value
          ? Object.fromEntries(
              ApiConfigRef.value.fileDesc.map((item) => [item.key, item.value])
          )
          : {};
      return {
        ...commonFields,
        datasourceId: form.value.datasourceId,
        fileInfo: fileInfo,
        filePath: fileInfo.path,
      };
    }
    default:
      return commonFields;
  }
}

const saveData = async () => {
  loading.value = true; // Start loading
  try {
    if (form.value.type === "2" && form.value.createType == "2") {
      const valid = await ApiConfigRef.value.validateForms();
      if (!valid) {
        proxy.$message.warning(td('dpp.asset.add.validateIncomplete'));
        return;
      }
    }
    const valid = await proxy.$refs["daDiscoveryTaskRef"].validate();
    if (valid) {
      if (props.data.type == "5") {
        if (form.value.daAssetVideo == null) {
          form.value.daAssetVideo = {
            ip: "",
            port: "",
            protocol: "",
            platform: "",
            cameraName: "",
            cameraCode: "",
            artemisPath: "", //service context
            config: JSON.stringify({
              cameraName: "",
              cameraCode: "",
              artemisPath: "",
            }),
          };
        } else {
          form.value.daAssetVideo.config = JSON.stringify(
              form.value?.daAssetVideo?.config
          );
        }
      }
      form.value = getFormDataByType(form.value.type);
      if (form.value.id != null) {
        if (form.value.createType == "1") {
          await bindResources(form.value);
          proxy.$modal.msgSuccess(td('common.message.operationSuccess'));
        } else {
          await updateDaAsset(form.value);
          proxy.$modal.msgSuccess(td('common.message.editSuccess'));
        }
      } else {
        if (form.value.type == 2) {
          form.value.daAssetApiParamList = removeIdFields(
              form.value.daAssetApiParamList
          );
        }
        let payload = {
          ...form.value,
        };
        if (props.type == 1) {
          payload.projectCode = userStore.projectCode;
          payload.projectId = userStore.projectId;
        }
        await addDaAsset({
          ...payload,
        });
        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
      }
      emit("update:visible", false);
      emit("confirm", form.value);
    } else {
      proxy.$message.warning(td('dpp.asset.add.validateFormFailed'));
    }
  } finally {
    loading.value = false; // end loading
  }
};

// Clear form data
const clearForm = () => {
  form.value = {
    type: "1",
    catCode: "",
    sourceType: "0",
    name: "",
    themeIdList: [],
    status: "1",
    createType: "2",
    description: "",
    source: "3",

    // Add new field
    tableType: "1",
    dataLayerId: null,
    businessDomainId: null,
    businessCategoryCode: "",
    dataDomainId: null,
    themeDomainId: null,
    themeDomainCode: "",
    tableCase: 1,
    namingSpec: "",

    // 1
    tableName: "",
    datasourceId: "",
    tableComment: "",
    datasourceType: "",
    dbname: "",
    // 2
    daAssetApi: {
      appName: "",
      url: "",
      httpMethod: "",
      developerName: "",
    },
    daAssetApiParamList: [],
    // 4
    daAssetGeo: {
      fileUrl: "",
      fileType: "",
      elementType: "",
      coordinateSystem: "",
      example: "",
      fileName: "",
    },
    daAssetGis: {
      url: "",
      type: "",
      httpMethod: "",
      coordinateSystem: "",
      remark: "",
    },
    // 5
    daAssetVideo: {
      ip: "",
      port: "",
      protocol: "",
      platform: "",
      cameraName: "",
      cameraCode: "",
      artemisPath: "", //service context
      config: JSON.stringify({
        cameraName: "",
        cameraCode: "",
        artemisPath: "",
      }),
    },
    daAssetFiles: {
      url: null,
      startData: "",
      startColumn: "",
      tableFields: [],
    },
    //   7
    filePath: "",
  };
};
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
