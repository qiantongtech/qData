<template>
    <!-- // 视频  -->
    <el-row :gutter="20" v-if="form.type == '5'">
        <el-col :span="12">
            <el-form-item label="平台" prop="daAssetVideo.platform" :rules="[
                {
                    required: true,
                    message: '请选择平台',
                    trigger: 'blur',
                },
            ]">
                <el-select v-model="form.daAssetVideo.platform" placeholder="请选择平台">
                    <el-option v-for="dict in da_asset_video_platform" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="平台ip" prop="daAssetVideo.ip" :rules="[
                {
                    required: true,
                    message: '请输入平台IP',
                    trigger: 'blur',
                },
            ]">
                <el-input v-model="form.daAssetVideo.ip" placeholder="请输入平台ip" />
            </el-form-item>
        </el-col>

    </el-row>
    <el-row :gutter="20" v-if="form.type == '5'">
        <el-col :span="12">
            <el-form-item label="平台端口" prop="daAssetVideo.port" :rules="[
                {
                    required: true,
                    message: '请输入平台端口',
                    trigger: 'blur',
                },
            ]">
                <el-input v-model="localForm.daAssetVideo.port" placeholder="请输入平台端口" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="摄像头编码" prop="daAssetVideo.config.cameraCode" :rules="[
                {
                    required: true,
                    message: '请输入摄像头编码',
                    trigger: 'blur',
                },
            ]">
                <el-input v-model="localForm.daAssetVideo.config.cameraCode" placeholder="请输入摄像头编码" />
            </el-form-item>
        </el-col>
    </el-row>
    <el-row :gutter="20" v-if="form.type == '5'">
        <el-col :span="12">
            <el-form-item label="摄像头名称" prop="daAssetVideo.config.cameraName" :rules="[
                {
                    required: true,
                    message: '请输入摄像头名称',
                    trigger: 'blur',
                },
            ]">
                <el-input v-model="localForm.daAssetVideo.config.cameraName" placeholder="请输入摄像头名称" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="服务上下文" prop="daAssetVideo.config.artemisPath" :rules="[
                {
                    required: true,
                    message: '请输入服务上下文',
                    trigger: 'blur',
                },
            ]">
                <el-input v-model="localForm.daAssetVideo.config.artemisPath" placeholder="/artemis" />
            </el-form-item>
        </el-col>

    </el-row>
    <el-row :gutter="20" v-if="form.type == '5'">
        <el-col :span="12">
            <el-form-item label="公钥" prop="daAssetVideo.config.appkey" :rules="[
                {
                    required: true,
                    message: '请输入公钥',
                    trigger: 'blur',
                },
            ]">
                <el-input v-model="localForm.daAssetVideo.config.appkey" placeholder="请输入公钥" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="私钥" prop="daAssetVideo.config.appSecret" :rules="[
                {
                    required: true,
                    message: '请输入私钥',
                    trigger: 'blur',
                },
            ]">
                <el-input v-model="localForm.daAssetVideo.config.appSecret" placeholder="请输入私钥" />
            </el-form-item>
        </el-col>

    </el-row>
    <!-- tableComment -->
</template>

<script setup>
import { ref, watch, getCurrentInstance } from "vue";
import { ElMessage } from "element-plus";
import useUserStore from "@/store/system/user.js";
import {
    getTablesByDataSourceId,
    getColumnByAssetId,
} from "@/api/dpp/task/index.js";
import { dppNoPageList } from "@/api/da/asset/asset.js";
import { getDaDatasource, listDaDatasourceNoKafkaByProjectCode } from "@/api/da/dataSource/dataSource.js";
const props = defineProps({
    form: Object,
});
const emit = defineEmits(["update:form"]);

const { proxy } = getCurrentInstance();


let localForm = ref({});

watchEffect(() => {
    localForm.value = { ...props.form };

});

</script>
