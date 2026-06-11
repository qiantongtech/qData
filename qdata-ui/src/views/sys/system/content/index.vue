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
    <div class="dataBody">
        <el-row class="form-container" :gutter="20">
            <!-- Logo 上传 -->
            <el-col :span="24">
                <div class="form-item">
                    <div class="form-label">{{ td('sys.system.content.loginLogo') }}</div>
                    <div class="form-input">
                        <ImageUpload v-model="loginLogoModelValue" :limit="1" :fileSize="10" :isShowTip="true" @update:modelValue="loginLogoUpdate" :platForm="platForm"  />  <!-- 使用组件 -->
                    </div>
                </div>
            </el-col>

            <el-col :span="24">
            <div class="form-item">
                <div class="form-label">{{ td('sys.system.content.systemLogo') }}</div>
                <div class="form-input">
                    <ImageUpload v-model="logoModelValue" :limit="1" :fileSize="10" :isShowTip="true" @update:modelValue="logoUpdate" :platForm="platForm"  />  <!-- 使用组件 -->
                </div>
            </div>
            </el-col>

            <el-col :span="24">
                <div class="form-item">
                    <div class="form-label">{{ td('sys.system.content.loginCarousel') }}</div>
                    <div class="form-input">
                        <ImageUpload v-model="carouselImageModelValue" :limit="3" :fileSize="10" :isShowTip="true" @update:modelValue="carouselImageUpdate" :platForm="platForm"  />  <!-- 使用组件 -->
                    </div>
                </div>
            </el-col>

            <!-- 联系电话 -->
            <el-col :span="24" :xs="24">
                <div class="form-item">
                    <div class="form-label">{{ td('sys.system.content.contactPhone') }}</div>
                    <div class="form-input-i">
                        <el-input v-model="contentDetail.contactNumber" :placeholder="td('sys.system.content.contactPhonePlaceholder')"></el-input>
                    </div>
                </div>
            </el-col>

            <!-- 电子邮箱 -->
            <el-col :span="24" :xs="24">
                <div class="form-item">
                    <div class="form-label">{{ td('sys.system.content.email') }}</div>
                    <div class="form-input-i">
                        <el-input v-model="contentDetail.email" :placeholder="td('sys.system.content.emailPlaceholder')"></el-input>
                    </div>
                </div>
            </el-col>

            <!-- 版权方 -->
            <el-col :span="24" :xs="24">
                <div class="form-item">
                    <div class="form-label">{{ td('sys.system.content.copyrightOwner') }}</div>
                    <div class="form-input-i">
                        <el-input v-model="contentDetail.copyright" :placeholder="td('sys.system.content.copyrightOwnerPlaceholder')"></el-input>
                    </div>
                </div>
            </el-col>

            <!-- 备案号 -->
            <el-col :span="24" :xs="24">
                <div class="form-item">
                    <div class="form-label">{{ td('sys.system.content.recordNo') }}</div>
                    <div class="form-input-i">
                        <el-input v-model="contentDetail.recordNumber" :placeholder="td('sys.system.content.recordNoPlaceholder')"></el-input>
                    </div>
                </div>
            </el-col>

<!--            {{contentDetail}}-->
            <div style="margin-top: 20px;">
                <!--                <el-button @click="update" v-show="status">{{ td('common.button.update') }}</el-button>-->
                <!--                <el-button @click="confirm" v-show="!status">{{ td('common.button.save') }}</el-button>-->
                <el-button @click="confirm">{{ td('common.button.save') }}</el-button>
            </div>

        </el-row>
    </div>

</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
import { ref } from 'vue';
    import { getContent,listContent, updateContent } from "@/api/system/system/content";
    import ImageUpload from "@/components/ImageUpload/index.vue"

    const { td } = useDefaultLang();
    const { proxy } = getCurrentInstance();

    const loginLogoModelValue = ref([])
    const logoModelValue = ref([])
    const carouselImageModelValue = ref([])

    //存储平台名称
    const platForm = ref('aliyun-oss-qt')
    //存储到服务器本地
    // const platForm = ref('')

    const status = ref(true)
    // 初始化 contentDetail 数据
    const contentDetail = ref({
        sysName: '',
        loginLogo: '',
        carouselImage: '',
        logo: '',
        contactNumber: '',
        email: '',
        copyright: '',
        recordNumber: '',
    });

    const loginLogoUpdate = (updatedFileList) => {
        contentDetail.value.loginLogo = updatedFileList
    };

    const logoUpdate = (updatedFileList) => {
        contentDetail.value.logo = updatedFileList
    };

    const carouselImageUpdate = (updatedFileList) => {
        contentDetail.value.carouselImage = updatedFileList
    };

    // 使用 getContent 来获取数据，而不是重新定义一个 getContent 函数
    const fetchContent = async () => {
        try {
            contentDetail.value = {}
            // 调用你从 API 导入的 getContent 方法
            const res = await getContent(1);  // 假设请求的是 id 为 1 的数据
            if(res.code == 200){
                const data = res.data
                if(data.loginLogo){
                    const loginLogoList = data.loginLogo.splitd(',')
                    const arr = []
                    loginLogoList.forEach(e=>{
                        arr.push({url: e})
                    })
                    loginLogoModelValue.value = arr
                }
                if(data.logo){
                    const logoList = data.logo.splitd(',')
                    const arr = []
                    logoList.forEach(e=>{
                        arr.push({url: e})
                    })
                    logoModelValue.value = arr
                }
                if(data.carouselImage){
                    const carouselImageList = data.carouselImage.splitd(',')
                    const arr = []
                    carouselImageList.forEach(e=>{
                        arr.push({url: e})
                    })
                    carouselImageModelValue.value = arr
                }
                contentDetail.value = {
                    id: data.id,
                    sysName: data.sysName,
                    loginLogo: data.loginLogo,
                    logo: data.logo,
                    carouselImage: data.carouselImage,
                    contactNumber: data.contactNumber,
                    email: data.email,
                    copyright: data.copyright,
                    recordNumber: data.recordNumber,
                };
                // console.log('------contentDetail.value-------',contentDetail.value)
            }

            // this.$message.success('内容加载成功');
        } catch (error) {
            // 错误处理
            // this.$message.error('内容加载失败');
            console.error(td('sys.system.content.dataLoadFailed'), error);
        }
    };

    // 在页面加载时自动调用 fetchContent
    onMounted(() => {
        fetchContent();
    });

    // 更新按钮点击事件
    const update = () => {
        status.value = !status.value
    };

    // 确认按钮点击事件
    const confirm =  () => {
        proxy.$modal.confirm(td('sys.system.content.confirmSave')).then(function() {}).then(async () => {
            status.value = !status.value
            try {
                const item = contentDetail.value
                const res = await updateContent(item)
                if (res.code == 200) {
                    fetchContent();
                    proxy.$modal.msgSuccess(td('sys.system.content.saveSuccess'));
                } else {
                    // 如果响应 code 不是 200，表示请求失败
                    proxy.$modal.msgError(td('sys.system.content.saveFailed'));
                }
            } catch (error) {
                // 捕获网络错误或请求失败的情况
                console.error("请求失败:", error);
                proxy.$modal.msgError(td('sys.system.content.saveException') + error.message);
            }
        }).catch(() => {});
    };
</script>

<style scoped>
    .dataBody {
        min-height: calc(100vh - 115px);
        margin: 20px 30px;
    }

    .form-container {
        padding: 20px;
        background-color: #f9f9f9;
        border-radius: 8px;
    }

    .form-item {
        margin-bottom: 20px;
        display: flex; /* 使用 Flexbox 布局 */
        align-items: center; /* 垂直居中对齐 */
    }

    .form-label {
        font-size: 16px;
        font-weight: 600;
        color: #333;
        width: 120px; /* 标签的宽度 */
        margin-right: 10px; /* 标签和输入框之间的间距 */
    }

    .form-input {
        display: flex;
        align-items: center; /* 输入框垂直居中对齐 */
        width: 100%; /* 使输入框占满剩余宽度 */
    }
    .form-input-i {
        display: flex;
        align-items: center; /* 输入框垂直居中对齐 */
        width: 30%; /* 使输入框占满剩余宽度 */
    }

    .el-input {
        flex-grow: 1; /* 使输入框占满剩余的空间 */
        width: 30%; /* 确保输入框占据100%宽度 */
    }

    .upload-demo {
        display: inline-block;
        margin-top: 10px;
    }

    .uploaded-img img {
        margin-top: 10px;
        border-radius: 8px;
    }

    .el-button {
        background-color: #2666FB;
        color: white;
    }

    .el-button:hover {
        background-color: #66b1ff;
    }

    @media (max-width: 768px) {
        .form-item {
            flex-direction: column; /* 屏幕较小时，标签和输入框竖排 */
            align-items: flex-start; /* 左对齐 */
        }

        .form-label {
            margin-right: 0;
            margin-bottom: 10px; /* 标签与输入框的垂直间距 */
        }
    }
</style>
