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
    <div class="app-container">
        <div class="pagecont-top" v-show="showSearch">
            <el-form
                class="btn-style"
                :model="queryParams"
                ref="queryRef"
                :inline="true"
                label-width="68px"
            >
                <el-form-item :label="td('sys.system.message.msgType')" prop="category">
                    <el-select
                        v-model="queryParams.category"
                        :placeholder="td('sys.system.message.msgTypePlaceholder')"
                        clearable
                        class="el-form-input-width"
                    >
                        <el-option
                            v-for="dict in message_category"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
                        />
                    </el-select>
                </el-form-item>

                <el-form-item :label="td('common.texts.createdTime')">
                    <el-date-picker
                        class="el-form-input-width"
                        v-model="queryParams.dateRange"
                        value-format="YYYY-MM-DD"
                        type="daterange"
                        range-separator="-"
                        :start-placeholder="td('common.form.startDatePlaceholder')"
                        :end-placeholder="td('common.form.endDatePlaceholder')"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button
                        plain
                        type="primary"
                        @click="handleQuery"
                        @mousedown="(e) => e.preventDefault()"
                    >
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
                    </el-button>
                    <el-button
                        @click="resetQuery"
                        @mousedown="(e) => e.preventDefault()"
                    >
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
                    </el-button>
                </el-form-item>
            </el-form>
        </div>
        <div  class="pagecont-bottom">

            <div class="justify-between mb15">
                <el-row :gutter="10" class="btn-style">
                    <el-col :span="1.5">
                        <el-button @click="readAllMsg" plain>
                            <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('sys.system.message.setAllRead') }}
                        </el-button>
                    </el-col>
                </el-row>
                <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>

            <el-table stripe v-loading="loading" :data="msgList">
                <el-table-column
                    :label="td('sys.system.message.msgTitle')"
                    align="center"
                    key="title"
                    prop="title"
                />
                <el-table-column :label="td('sys.system.message.msgType')" align="center" key="category">
                    <template #default="scope">
                        <dict-tag
                            :options="message_category"
                            :value="scope.row.category"
                        />
                    </template>
                </el-table-column>
                <el-table-column :label="td('sys.system.message.isRead')" align="center" key="hasRead">
                    <template #default="scope">
                        <el-tag :type="scope.row.hasRead ? 'success' : 'danger'">
                            {{ scope.row.hasRead ? td('sys.system.message.read') : td('sys.system.message.unread') }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    :label="td('sys.system.message.msgContent')"
                    align="center"
                    key="content"
                    prop="content"
                />

                <el-table-column
                    :label="td('common.texts.createdTime')"
                    align="center"
                    key="createTime"
                    prop="createTime"
                />

                <el-table-column
                    :label="td('common.texts.operation')"
                    align="center"
                    class-name="small-padding fixed-width"
                    fixed="right"
                    width="240"
                >
                    <template #default="scope">
                        <el-button
                            link
                            type="primary"
                            icon="View"
                            @click="handleView(scope.row)"
                        >
                            {{ td('common.button.details') }}
                        </el-button>
                        <el-button
                            link
                            type="danger"
                            icon="Delete"
                            @click="deleteMsg(scope.row.id)"
                        >
                            {{ td('common.button.delete') }}
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <pagination
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
            />
        </div>

        <el-dialog
            :title="td('sys.system.message.msgDetail')"
            v-model="openView"
            width="800px"
            draggable
            destroy-on-close
            class="msg-dialog"
        >
            <el-form label-width="100px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="td('sys.system.message.msgTitleLabel')">
                            <div class="form-value-ifon">
                                {{ viewData.title }}
                            </div>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item :label="td('sys.system.message.typeLabel')">
                            <div class="form-value-ifon">
                                <dict-tag
                                    :options="message_category"
                                    :value="viewData.category"
                                />
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('sys.system.message.isReadLabel')">
                            <div class="form-value-ifon">
                                <el-tag
                                    :type="
                                        viewData.hasRead ? 'success' : 'danger'
                                    "
                                >
                                    {{ viewData.hasRead ? td('sys.system.message.read') : td('sys.system.message.unread') }}
                                </el-tag>
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('sys.system.message.msgContentLabel')">
                            <div class="form-value-ifon">
                                {{ viewData.content }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item :label="td('sys.system.message.createTimeLabel')">
                            <div class="form-value-ifon">
                                {{ viewData.createTime }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="openView = false">{{ td('common.button.close') }}</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="Message">
import { getCurrentInstance, ref } from "vue"
import useDefaultLang from "@/composables/useDefaultLang";
import useUserStore from "@/store/system/user";

const { td } = useDefaultLang();
import {
    listMessage,
    delMessage,
    read,
    readAll,
    updateMessage
} from "@/api/system/system/message/message";

const openView = ref(false);
const viewData = ref({});
const userStore = useUserStore();
const queryParams = ref({
    dateRange: [],
    pageNum: 1,
    pageSize: 10,
    receiverId: userStore.userId,
});
const total = ref(0);

const loading = ref(false);
const showSearch = ref(true);
const msgList = ref([]);
const { proxy } = getCurrentInstance();
const { message_category } = proxy.useDict("message_category");

const handleQuery = () => {
    console.log(queryParams.value);
    getList()
};

const resetQuery = () => {
    queryParams.value = {};
};

const handleView = (e) => {
    e.hasRead = '1'
    e.delFlag = 1
    updateMessage(e);
    msgList.value = msgList.value.map(item => {
        if (e.id == item.id) {
            return { ...item, hasRead: '1' }; // 创建一个新对象，修改 hasRead
        }
        return item; // 保持其他项不变
    });
    openView.value = true;
    viewData.value = e;
};

const startTime = ref(null)
const endTime = ref(null)
const getList = () => {
    const reqData = {
        category: queryParams.value.category,
        receiverId: userStore.userId,
        pageNum: queryParams.value.pageNum,
        pageSize: queryParams.value.pageSize,
    }
    if(queryParams.value.dateRange && queryParams.value.dateRange.length > 0){
        reqData.startTime= queryParams.value.dateRange[0],
        reqData.endTime= queryParams.value.dateRange[1]
    }
    listMessage(reqData).then((response) => {
        msgList.value = response.data.rows;
        total.value = response.data.total;
    });
};
getList();

/** 全部已读 */
function readAllMsg() {
    ElMessageBox.confirm(td('sys.system.message.confirmSetAllRead'))
        .then(() => {
            return readAll();
        })
        .then((res) => {
            console.log('------设置为已读----',res)
            getList();
            ElMessage.success(td('common.message.msgOpSuccess'));
        })
        .catch(() => {});
}
/** 删除 */
function deleteMsg(id) {
    ElMessageBox.confirm(td('sys.system.message.confirmDelete'))
        .then(() => {
            return delMessage(id);
        })
        .then(() => {
            getList();
            ElMessage.success(td('common.message.msgOpSuccess'));
        })
        .catch(() => {});
}

// /**
//  * 修改状态为已读
//  * @param id
//  */
// function updateMsg(row) {
//     row.hasRead = '1'
//     updateMessage(row);
// }
</script>

<style scoped lang="scss">
.msg-dialog {
    .el-dialog__body {
        height: 300px !important;
    }
}
</style>
