<!-- 复杂详情路由模板
    {
        path: '/dp/dataElem',
        component: Layout,
        redirect: 'dataElem',
        hidden: true,
        children: [
            {
                path: 'dpDataElemDetail',
                component: () => import('@/views/dp/dataElem/detail/user.vue'),
                name: 'tree',
                meta: { title: '数据元详情', activeMenu: '/dp/dpDataElem'  }
            }
        ]
    }
 -->

<template>
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="infotop-title mb15">
                    {{ dpDataElemDetail.name || '-' }}
                </div>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">英文名称</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.engName || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">类目编码</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.catName || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">类型</div>
                            <div class="infotop-row-value">
                                <dict-tag :options="dp_data_elem_code_type" :value="dpDataElemDetail.type" />
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">责任人</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.personCharge || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">联系电话</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.contactNumber || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">字段类型</div>
                            <div class="infotop-row-value">
                                <dict-tag :options="column_type" :value="dpDataElemDetail.columnType" />
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">状态</div>
                            <div class="infotop-row-value">
                                <dict-tag :options="sys_disable" :value="dpDataElemDetail.status" />
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建时间</div>
                            <div class="infotop-row-value">
                                {{ parseTime(dpDataElemDetail.createTime, '{y}-{m}-{d}') }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建人</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.createBy || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="24">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">描述</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.description || '-' }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>

        <div class="pagecont-bottom">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                <el-tab-pane label="代码字典" name="1">
                    <codeDict />
                </el-tab-pane>
                <el-tab-pane label="代码映射" name="2">
                    <codeMap />
                </el-tab-pane>
                <el-tab-pane label="关联信息" name="3">
                    <asset />
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script setup name="DpDataElem">
import { getDpDataElem } from '@/api/dp/dataElem/dataElem';
import { useRoute } from 'vue-router';
import codeDict from '@/views/dp/dataElem/detail/dict/codeDict.vue';
import codeMap from '@/views/dp/dataElem/detail/dict/codeMap.vue';
import asset from "@/views/dp/dataElem/detail/components/asset.vue";

const { proxy } = getCurrentInstance();
const { column_type, sys_disable, dp_data_elem_code_type } = proxy.useDict(
    'column_type',
    'sys_disable',
    'dp_data_elem_code_type'
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
        id = newId || -1; // 如果 id 为空，使用默认值 1
        getDpDataElemDetailById();
    },
    { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
    dpDataElemDetail: {},
    form: {}
});

const { dpDataElemDetail, form } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getDpDataElemDetailById() {
    const _id = id;
    if (!id || id == -1) return;
    getDpDataElem(_id).then((response) => {
        if (!response.data) return;
        dpDataElemDetail.value = response.data;
        console.log(dpDataElemDetail.value);
    });
}
// 页面加载时获取数据
onMounted(() => {
    getDpDataElemDetailById();
});
</script>

<style scoped lang="scss">
.app-container {
    margin: 15px 15px 0px 15px;

    .pagecont-bottom {
        min-height: calc(100vh - 380px) !important;
    }
}
</style>
