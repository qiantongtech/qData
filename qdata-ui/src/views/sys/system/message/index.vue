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
                <el-form-item label="消息类型" prop="category">
                    <el-select
                        v-model="queryParams.category"
                        placeholder="消息类型"
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

                <el-form-item label="创建时间">
                    <el-date-picker
                        class="el-form-input-width"
                        v-model="queryParams.dateRange"
                        value-format="YYYY-MM-DD"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button
                        plain
                        type="primary"
                        @click="handleQuery"
                        @mousedown="(e) => e.preventDefault()"
                    >
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                    </el-button>
                    <el-button
                        @click="resetQuery"
                        @mousedown="(e) => e.preventDefault()"
                    >
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                    </el-button>
                </el-form-item>
            </el-form>
        </div>
        <div  class="pagecont-bottom">

            <div class="justify-between mb15">
                <el-row :gutter="10" class="btn-style">
                    <el-col :span="1.5">
                        <el-button @click="readAllMsg" plain>
                            <i class="iconfont-mini icon-a-zu22378 mr5"></i>全部设为已读
                        </el-button>
                    </el-col>
                </el-row>
                <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>

            <el-table stripe v-loading="loading" :data="msgList">
                <el-table-column
                    label="消息标题"
                    align="center"
                    key="title"
                    prop="title"
                />
                <el-table-column label="消息类型" align="center" key="category">
                    <template #default="scope">
                        <dict-tag
                            :options="message_category"
                            :value="scope.row.category"
                        />
                    </template>
                </el-table-column>
                <el-table-column label="是否已读" align="center" key="hasRead">
                    <template #default="scope">
                        <el-tag :type="scope.row.hasRead ? 'success' : 'danger'">
                            {{ scope.row.hasRead ? "已读" : "未读" }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    label="消息内容"
                    align="center"
                    key="content"
                    prop="content"
                />

                <el-table-column
                    label="创建时间"
                    align="center"
                    key="createTime"
                    prop="createTime"
                />

                <el-table-column
                    label="操作"
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
                            详情
                        </el-button>
                        <el-button
                            link
                            type="danger"
                            icon="Delete"
                            @click="deleteMsg(scope.row.id)"
                        >
                            删除
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
            title="消息详情"
            v-model="openView"
            width="800px"
            draggable
            destroy-on-close
            class="msg-dialog"
        >
            <el-form label-width="100px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="消息标题：">
                            <div class="form-value-ifon">
                                {{ viewData.title }}
                            </div>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="类型：">
                            <div class="form-value-ifon">
                                <dict-tag
                                    :options="message_category"
                                    :value="viewData.category"
                                />
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否已读：">
                            <div class="form-value-ifon">
                                <el-tag
                                    :type="
                                        viewData.hasRead ? 'success' : 'danger'
                                    "
                                >
                                    {{ viewData.hasRead ? "已读" : "未读" }}
                                </el-tag>
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="消息内容：">
                            <div class="form-value-ifon">
                                {{ viewData.content }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="创建时间：">
                            <div class="form-value-ifon">
                                {{ viewData.createTime }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="openView = false">关 闭</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="Message">
import { getCurrentInstance, ref } from "vue";
import useUserStore from "@/store/system/user";
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
    ElMessageBox.confirm("确定全部设为已读吗？")
        .then(() => {
            return readAll();
        })
        .then((res) => {
            console.log('------设置为已读----',res)
            getList();
            ElMessage.success("操作成功");
        })
        .catch(() => {});
}
/** 删除 */
function deleteMsg(id) {
    ElMessageBox.confirm("确定删除改条消息吗？")
        .then(() => {
            return delMessage(id);
        })
        .then(() => {
            getList();
            ElMessage.success("操作成功");
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
