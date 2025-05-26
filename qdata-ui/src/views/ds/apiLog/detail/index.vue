<!-- 复杂详情路由模板
    {
        path: '/ds/apiLog',
        component: Layout,
        redirect: 'apiLog',
        hidden: true,
        children: [
            {
                path: 'apiLogDetail',
                component: () => import('@/views/ds/apiLog/detail/index.vue'),
                name: 'tree',
                meta: { title: 'API服务调用日志详情', activeMenu: '/ds/apiLog'  }
            }
        ]
    }
 -->

<template>
    <div class="app-container" ref="app-container" v-if="apiLogDetail">
        <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="infotop-title mb15">接口日志详情</div>
                <el-row :gutter="20">
                    <el-col :span="20">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable infotop-row-lable-height">
                                API服务名称
                            </div>
                            <div class="infotop-row-value infotop-row-value-height">
                                {{ apiLogDetail.apiName || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="20">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable infotop-row-lable-height">调用者ip</div>
                            <div class="infotop-row-value infotop-row-value-height">
                                {{ apiLogDetail.callerIp || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="20">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable infotop-row-lable-height">
                                调用接口地址
                            </div>
                            <div class="infotop-row-value infotop-row-value-height">
                                {{ apiLogDetail.callerUrl || '-' }}
                            </div>
                        </div>
                    </el-col>

                    <el-col :span="20">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable infotop-row-lable-height">调用时间</div>
                            <div class="infotop-row-value infotop-row-value-height">
                                {{ parseTime(apiLogDetail.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="20">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable infotop-row-lable-height">
                                调用耗时(秒)
                            </div>
                            <div class="infotop-row-value infotop-row-value-height">
                                {{ apiLogDetail.callerTime / 1000 || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="20">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable infotop-row-lable-height">状态</div>
                            <div class="infotop-row-value infotop-row-value-height">
                                <dict-tag
                                    :options="ds_api_log_res_status"
                                    :value="apiLogDetail.status"
                                />
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="20">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable infotop-row-lable-height">请求方式</div>
                            <div class="infotop-row-value infotop-row-value-height">
                                <dict-tag
                                    :options="ds_api_bas_info_api_method_type"
                                    :value="apiLogDetail.reqMethod"
                                />
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="20">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable infotop-row-lable-height">
                                异常信息记录
                            </div>
                            <div class="infotop-row-value infotop-row-value-height">
                                {{ apiLogDetail.msg || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="20">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable infotop-row-lable-height">调用参数</div>
                            <div class="infotop-row-value infotop-row-value-height">
                                {{ apiLogDetail.callerParams || '-' }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>

        <!--    <div  class="pagecont-bottom">-->
        <!--      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">-->
        <!--        <el-tab-pane label="接口日志详情" name="1">-->
        <!--          <component-one ></component-one>-->
        <!--        </el-tab-pane>-->

        <!--      </el-tabs>-->
        <!--    </div>-->
    </div>
</template>

<script setup name="ApiLog">
    import { getApiLog } from '@/api/ds/apiLog/apiLog';
    import { useRoute } from 'vue-router';
    import ComponentOne from '@/views/ds/apiLog/detail/componentOne.vue';
    import ComponentTwo from '@/views/ds/apiLog/detail/componentTwo.vue';

    const { proxy } = getCurrentInstance();
    const { ds_api_log_res_status, ds_api_bas_info_api_method_type } = proxy.useDict(
        'ds_api_log_res_status',
        'ds_api_bas_info_api_method_type'
    );

    const activeName = ref('1');

    const handleClick = (tab, event) => {
        console.log(tab, event);
    };

    const showSearch = ref(true);
    const route = useRoute();
    let id = route.query.id || 1;
    // 监听 id 变化
    watch(
        () => route.query.id,
        (newId) => {
            id = newId || 1; // 如果 id 为空，使用默认值 1
            getApiLogDetailById();
        },
        { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
    );
    const data = reactive({
        apiLogDetail: {},
        form: {}
    });

    const { apiLogDetail, rules } = toRefs(data);

    /** 复杂详情页面上方表单查询 */
    function getApiLogDetailById() {
        const _ID = id;
        getApiLog(_ID).then((response) => {
            apiLogDetail.value = response.data;
        });
    }

    getApiLogDetailById();
</script>
<style scoped lang="scss">
    .infotop-row-lable-height {
        height: 70px !important;
        line-height: 45px !important;
    }
    .infotop-row-value-height {
        height: 70px !important;
        word-break: break-all !important;
        white-space: pre-wrap !important;
        overflow-y: auto !important;
        &::-webkit-scrollbar {
            width: 2px;
        }
        &::-webkit-scrollbar-thumb {
            background: #c0c4cc;
            border-radius: 2px;
        }
    }
</style>
