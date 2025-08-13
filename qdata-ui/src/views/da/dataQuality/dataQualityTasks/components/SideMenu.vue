<template>
    <el-row>
        <el-col :span="5">
            <DeptTree :deptOptions="processedData" :leftWidth="leftWidth" :placeholder="'请输入规则类型'"
                @node-click="handleNodeClick" ref="DeptTreeRef" :default-expand="true" />
        </el-col>
        <div class="divider"></div>
        <el-col :span="18" class="content-col" v-loading="loading">
            <div class="content" ref="contentWrapper">
                <el-row>
                    <div class="cards-wrapper">
                        <template v-if="attAuditRuleList.length">
                            <div v-for="data in attAuditRuleList" :key="data.id" class="card-item"
                                :class="{ selected: selectedCard?.id === data.id }" @click="cardClick(data)">
                                <el-card class="box-card boxCard" shadow="never" :body-style="{ padding: '15px' }">
                                    <div class="card-icon" :class="{ 'is-disabled': data.validFlag == false }">
                                        <el-icon>
                                            <Document />
                                        </el-icon>
                                    </div>
                                    <div class="card-title ellipsis-8">{{ data.name }}</div>
                                    <div class="card-desc ellipsis-multi">{{ data.useCase }}</div>
                                </el-card>
                            </div>
                        </template>

                        <template v-else>
                            <div class="empty-wrapper">
                                暂无数据
                            </div>
                        </template>
                    </div>
                </el-row>
            </div>
        </el-col>
    </el-row>
</template>

<script setup>

import {
    Document,
    Menu,
    DataLine,
    Files,
    Monitor,
} from "@element-plus/icons-vue";
import { listAttAuditRule } from "@/api/att/rule/attAuditRule.js";
import DeptTree from './tree.vue';

const { proxy } = getCurrentInstance();
const { att_rule_audit_q_dimension } = proxy.useDict("att_rule_audit_q_dimension");
const loading = ref(false);
const contentWrapper = ref(null);
const selectedCard = ref(null);
const leftWidth = ref(250); // 初始左侧宽度
const emit = defineEmits(["card-click"]);

let queryParams = ref(({
    pageNum: 1,
    pageSize: 999,
    qualityDim: '',
    // validFlag: '1'
}))
const processedData = ref([]);
processedData.value = computed(() => {
    return [
        {
            name: '质量维度',
            children: Array.isArray(att_rule_audit_q_dimension.value)
                ? att_rule_audit_q_dimension.value.map((item) => ({
                    name: item.label,
                    id: item.value
                }))
                : []
        }
    ];
});
function handleNodeClick(data) {
    queryParams.value.qualityDim = data.id;
    queryParams.value.pageNum = 1;
    fetchRulesByDimension();
}

let attAuditRuleList = ref([])

async function fetchRulesByDimension() {
    loading.value = true;
    const res = await listAttAuditRule(queryParams.value);
    const list = res.data?.rows || [];
    attAuditRuleList.value = list.sort((a, b) => {
        return (b.validFlag == true) - (a.validFlag == true);
    });
    loading.value = false;
}


function cardClick(data) {
    if (data.validFlag == false) {
        return ElMessage.info('开发中')
    }
    selectedCard.value = data;
    emit("card-click", data);
}

onMounted(() => {
    fetchRulesByDimension();
});
</script>

<style lang="less" scoped>
.main-layout {
    height: 75vh;
    overflow: hidden;
}

.left-col {
    padding-right: 0;
}

.divider {
    width: 1px;
    height: 700px;
    background-color: #dcdfe6;
}

.content-col {
    width: 100%;
    overflow: hidden;
    padding-left: 0;
}

.content {
    overflow-y: auto;
    position: relative;
}

/* 右侧内容 */
.content-col {
    height: 75vh;
    overflow: hidden;
}

.content {
    height: 75vh;
    overflow-y: auto;
    padding: 20px 10px;
}

.cards-wrapper {
    padding-left: 40px;
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    justify-content: flex-start;
}

.card-item {
    width: 180px;
    box-sizing: border-box;
    transition: transform 0.2s;
    display: flex;
    justify-content: center;
    cursor: pointer;

    &.selected {
        transform: translateY(-2px);
        border: 2px solid #409eff;
        box-shadow: 0 0 10px rgba(64, 158, 255, 0.3);
    }

    &:hover {
        transform: translateY(-2px);
    }
}

.boxCard {
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: 150px;
    height: 150px;
    width: 100%;
    padding: 10px;
    transition: all 0.2s;
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);

    &:hover {
        box-shadow: 0 0 12px rgba(0, 0, 0, 0.2);
    }

    .card-icon {
        font-size: 45px;
        color: #409EFF;
        margin-bottom: 6px;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .card-title {
        font-weight: bold;
        font-size: 14px;
        margin-bottom: 5px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        cursor: pointer;
        text-align: center;
        width: 100%;
    }

    .card-desc {
        font-size: 12px;
        color: #666;
        text-align: center;
        padding: 0 5px;
        word-break: break-word;
    }
}

.dh {
    padding: 0;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.menu-icon {
    font-size: 20px;
    vertical-align: middle;
}

::v-deep .el-card__body {
    padding: 0 !important;
}

.empty-wrapper {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;

    display: flex;
    justify-content: center;
    align-items: center;

    /* 文字样式 */
    font-size: 16px;
    color: #999;
    user-select: none;
}

.card-icon {
    cursor: pointer;
    color: #409EFF;
}

.card-icon.is-disabled {
    color: #ccc;
    cursor: not-allowed;
}
</style>