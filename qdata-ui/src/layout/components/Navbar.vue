<template>
    <div class="navbar" ref="navbar">
        <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container"
            @toggleClick="toggleSideBar" />
        <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!settingsStore.topNav" />
        <top-nav ref="topNavRef" @getRouter="getRouter" id="topmenu-container" class="topmenu-container"
            v-if="settingsStore.topNav" />
        <div class="right-menu">
            <template v-if="appStore.device !== 'mobile'">

                <div style="width: 250px; margin-top: 10px" v-if="showProjectSelector">
                    <el-form class="btn-style" :model="userStore" ref="queryRef" :inline="true" label-width="93px">
                        <el-form-item label="所属项目" prop="projectId" :rules="[
                            { required: true, message: '请选择所属项目', trigger: 'change' }
                        ]">
                            <el-select style="width: 150px;" :fit-input-width="true" v-model="userStore.projectId"
                                @change="projectIdChange" placeholder="请选择所属项目" clearable
                                popper-class="custom-option-style">
                                <el-option v-for="item in projectOptions" :key="item.id" :label="item.name"
                                    :value="item.id">
                                    <template #default>
                                        <template v-if="item.name.length > 6">
                                            <el-tooltip placement="left" :content="item.name" effect="dark">
                                                <div class="ellipsis-option">{{ item.name }}</div>
                                            </el-tooltip>
                                        </template>
                                        <template v-else>
                                            <div class="ellipsis-option">{{ item.name }}</div>
                                        </template>
                                    </template>
                                </el-option>
                            </el-select>
                            <!-- <el-select style="width: 150px" class="el-form-input-width" v-model="userStore.projectId"
                                @change="projectIdChange" placeholder="请选择所属项目" clearable>
                                <el-option v-for="item in projectOptions" :key="item.id" :label="item.name"
                                    :value="item.id" />
                            </el-select> -->
                        </el-form-item>
                    </el-form>
                </div>
                <div class="right-menu-item hover-effect" @click="openDocumentation">
                    <svg-icon iconClass="bzzx" style="font-size: 18px;" />
                </div>
                <!-- ---------------------------- 报工 --------------------------------- -->
                <el-popover trigger="hover" popper-style="
                        width: 336px;
                        height: 360px;
                        background: #FFFFFF;
                        box-shadow: 0px 2px 8px 0px rgba(0,0,0,0.15);
                        padding:0;
                    ">
                    <template #reference>
                        <el-badge :value="msgCount" :max="99" class="badge" :class="msgCount > 0 ? 'flash' : ''"
                            :offset="[0, 0]" :hidden="msgCount == 0">
                            <!-- <i class="iconfont right-menu-item hover-effect" style="font-size: 22px">&#xebe7;</i> -->
                            <i class="iconfont icon-a-dingbulingdangxianxing right-menu-item hover-effect"
                                style="font-size: 20px"></i>
                        </el-badge>
                    </template>
                    <template #default>
                        <el-tabs v-model="activeMsg" stretch class="mag-tabs" @tab-click="handleClick">
                            <el-tab-pane label="消息提醒" name="first">
                                <div class="message-list">
                                    <div class="msg-item" v-for="(msg, index) in messages" :key="index"
                                        v-show="messages.length > 0">
                                        <img class="icon" src="@/assets/system/images/layout/msg/icon1.png" alt="" />
                                        <div class="content">
                                            <div class="title">{{ msg.title }}</div>
                                            <div class="time">{{ msg.time }}</div>
                                        </div>
                                    </div>
                                    <el-empty v-show="messages.length == 0 ||
                                        messages == null ||
                                        messages == 'undefined'
                                        " :image-size="100" description="暂无消息" class="empty-block" />
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="通知" name="second">
                                <!--                <message-list :msg-category="'first'"></message-list>-->
                                <div class="message-list">
                                    <div class="msg-item" v-for="(msg, index) in noticeList" :key="index"
                                        v-show="msg.entityType == 1" @click="handleMessage(msg)">
                                        <img class="icon" src="@/assets/system/images/layout/msg/icon1.png" alt="" />
                                        <div class="content">
                                            <div class="title">{{ msg.title }}</div>
                                            <div class="time">{{ msg.time }}</div>
                                        </div>
                                    </div>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="公告" name="third">
                                <!--                <message-list :msg-category="'second'"></message-list>-->
                                <div class="message-list">
                                    <div class="msg-item" v-for="(msg, index) in noticeList" :key="index"
                                        v-show="msg.entityType == 2" @click="handleMessage(msg)">
                                        <img class="icon" src="@/assets/system/images/layout/msg/icon1.png" alt="" />
                                        <div class="content">
                                            <div class="title">{{ msg.title }}</div>
                                            <div class="time">{{ msg.time }}</div>
                                        </div>
                                    </div>
                                </div>
                            </el-tab-pane>
                        </el-tabs>
                        <div class="msg-btns">
                            <div class="btn-item" @click="clearNotification">全部已读</div>
                            <div class="btn-item" @click="messageDetail">查看更多</div>
                        </div>
                    </template>
                </el-popover>
                <div class="right-menu-item hover-effect" @click="handleRefreshClick">
                    <!-- <el-icon size="22">
                        <Refresh />
                    </el-icon> -->
                    <i class="iconfont icon-a-shuaxinxianxing" style="font-size: 20px"></i>
                </div>


                <header-search id="header-search" class="right-menu-item" />

                <screenfull id="screenfull" class="right-menu-item hover-effect" />

                <!-- <el-tooltip content="布局大小" effect="dark" placement="bottom">
                  <size-select id="size-select" class="right-menu-item hover-effect" />
                </el-tooltip> -->
            </template>
            <div class="avatar-container">
                <el-dropdown @command="handleCommand" class="right-menu-item hover-effect" trigger="click">
                    <div class="avatar-wrapper">
                        <img :src="userStore.avatar" class="user-avatar" />
                        <span class="nickName">{{ userStore.nickName }}</span>
                    </div>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <router-link to="/user/profile">
                                <el-dropdown-item>个人中心</el-dropdown-item>
                            </router-link>
                            <el-dropdown-item command="setLayout" v-if="settingsStore.showSettings">
                                <span>布局设置</span>
                            </el-dropdown-item>
                            <el-dropdown-item command="about">
                                <span>关于我们</span>
                            </el-dropdown-item>
                            <el-dropdown-item divided command="logout">
                                <span>退出登录</span>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </div>
    </div>
</template>

<script setup name="Navbar">
import { useWindowSize } from '@vueuse/core'
import { ElMessageBox } from 'element-plus';
import Breadcrumb from '@/components/Breadcrumb';
import TopNav from '@/components/TopNav';
import Hamburger from '@/components/Hamburger';
import Screenfull from '@/components/Screenfull';
import SizeSelect from '@/components/SizeSelect';
import HeaderSearch from '@/components/HeaderSearch';
import useAppStore from '@/store/system/app';
import useUserStore from '@/store/system/user';
import useSettingsStore from '@/store/system/settings';
import useTagsViewStore from '@/store/system/tagsView';
import { getNum, listMessage, readAll } from '@/api/system/system/message/message';
import { loginOut } from '@/api/system/sso-auth.js';
// import MessageList from "@/views/sys/system/message/components/messageList.vue";
import { onMounted, ref, watch } from 'vue';
import moment from 'moment';
import { listNotice } from '@/api/system/system/notice';
import { currentUser } from '@/api/att/project/project';
import { da, id } from 'element-plus/es/locale/index.mjs';
import usePermissionStore from '@/store/system/permission';
import { getRoutersDpp } from '@/api/system/menu';
// import {listProject, getProject} from "@/api/project/projectBase/project";
// import {listReport, getReport, delReport, addReport, updateReport} from "@/api/project/report/report";
// 认证模式
const authType = import.meta.env.VITE_APP_AUTH_TYPE;

const route = useRoute();
const router = useRouter();
const appStore = useAppStore();
const userStore = useUserStore();
const settingsStore = useSettingsStore();
const { proxy } = getCurrentInstance();
const visitedViews = computed(() => useTagsViewStore().visitedViews);
let isFlag = ref(false);
// 默认选择的消息类型
const activeMsg = ref('first');
const projectId = ref('');
const permissionStore = usePermissionStore();
// 所有的路由信息
const routers = computed(() => permissionStore.topbarRouters);
//-----------------------以下报工内容-------------------------
const handleMessage = (msg) => {
    console.log('接收到的消息:', msg);
    router.push({ path: "/sys/system/notice/detail", query: { id: msg.noticeId } });
}
const data = reactive({
    form: {
        reportExperience: null
    },
    rules: {
        reportExperience: [{ required: true, message: '工作心得不能为空', trigger: 'blur' }]
    }
});
const { width } = useWindowSize()
const showProjectSelector = computed(() => width.value >= 1200 && isFlag.value)
const open = ref(false);
const title = ref(null);
const form = ref({});
const projectOptions = ref([]);

const tableData = ref([{ projectId: null, duration: null }]);

function resetFromWork() {
    tableData.value = [{ projectId: null, duration: null }];
    form.value.reportExperience = null;
}

//请假了
function offFromWork() {
    proxy.$modal
        .confirm('确认请假了？')
        .then(function () { })
        .then(() => {
            const itemList = tableData.value;
            const req = {
                reportExperience: '我请假了',
                status: 1,
                reportTime: new Date(),
                detailRespVOList: tableData.value
            };
            console.log('---------提交-请假----req-------', req);
            addReport(req)
                .then((response) => {
                    proxy.$modal.msgSuccess('提交成功');
                    open.value = false;
                    getList();
                })
                .catch((error) => { });
        })
        .catch(() => { });
    // form.value.reportExperience = '我请假了'
}

function getRouter(data) {
    if (data.includes('/dpp')) {
        isFlag.value = true;
    } else {
        isFlag.value = false;
    }
}

/** 提交按钮 */
function submitForm() {
    if (form.value.reportExperience == null) {
        proxy.$modal.msgWarning('工作心得为空');
        return;
    }
    proxy.$refs['reportRef'].validate((valid) => {
        console.log('---------校验----', valid);
        if (valid) {
            const tempList = tableData.value;
            if (tempList.length == 0) {
                proxy.$modal.msgError('报工项目为空');
                return;
            }
            let idStatus = false;
            let timeStatus = false;
            tempList.forEach((e) => {
                if (e.projectId == null) {
                    idStatus = true;
                }
                if (e.duration == null) {
                    timeStatus = true;
                }
            });
            if (idStatus) {
                proxy.$modal.msgWarning('报工项目为空');
                return;
            }
            if (timeStatus) {
                proxy.$modal.msgWarning('报工项目工作时长为空');
                return;
            }
            // 提取所有非空的 projectId 并用逗号连接
            form.value.reportContent = tempList
                .map((item) => item.projectId)
                .filter((id) => id != null) // 过滤掉 null 或 undefined 的值
                .join(',');

            if (form.value.id != null) {
                const tempList = tableData.value.map((e) => {
                    const date = new Date(e.reportTime);
                    return {
                        ...e,
                        reportTime: isNaN(date.getTime()) ? null : date // 如果无效，设置为 null
                    };
                });
                const req = {
                    ...form.value,
                    createTime: new Date(form.value.createTime),
                    reportTime: new Date(form.value.reportTime),
                    updateTime: new Date(),
                    detailRespVOList: tempList
                };
                updateReport(req)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                const itemList = tableData.value;
                const req = {
                    ...form.value,
                    status: 0,
                    reportTime: new Date(),
                    detailRespVOList: tableData.value
                };
                console.log('---------提交-----req-------', req);
                addReport(req)
                    .then((response) => {
                        proxy.$modal.msgSuccess('提交成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            }
        }
    });
}

// 删除操作
const deleteItem = (index) => {
    // 使用 splice 方法根据索引删除数据
    tableData.value.splice(index, 1);
    console.log('删除了索引为', index, '的项');
};

const addItem = () => {
    tableData.value.push({ name: 'aa' });
};

const popoverVisible = ref(false);

const handleFocus = () => {
    popoverVisible.value = true;
};
const handleBlur = () => {
    popoverVisible.value = false;
};
const handleSelectChange = (value) => {
    console.log('选中的选项:', value);
};

const handlePopoverClick = (value) => {
    // 如果不想关闭 Popover，可以在这里处理额外的逻辑
};

//打开报工页面
function openForWork() {
    tableData.value = [{ projectId: null, duration: null }];
    form.value.reportExperience = null;
    title.value = '新增报工';
    open.value = true;
}

function cancel() {
    open.value = false;
}

//报工管理
function reportingForWork() {
    router.push({ path: '/project/report' });
}

function projectIdChange(row, newValue) {
    // 从projectOptions中获取项目code
    const project = projectOptions.value.find((item) => item.id === userStore.projectId);
    if (project) {
        userStore.projectCode = project.code;
    }
    if (userStore.projectId) {
        getRoutersDpp(userStore.projectId).then((res) => {
            // 更新store中的路由数据
            permissionStore.updateTopbarRoutes(res.data);
            let topMenus = [];
            routers.value.map((menu) => {
                if (menu.path === '/dpp') {
                    topMenus = menu;
                }
            });
            const currentPath = router.currentRoute.value.path.split('/'); // 获取当前路由地址
            const menuPaths = topMenus.children.flatMap((child) =>
                child.children ? child.children.map((subChild) => subChild.path) : child.path
            ); // 获取菜单权限中的路径，如果有子节点就获取子节点的路径
            console.log('---------currentPath-------------', currentPath);
            console.log('---------menuPaths-------------', menuPaths);

            if (!menuPaths.includes(currentPath[currentPath.length - 1])) {
                console.log('1');
                // 如果不存在，跳转到第一个菜单
                if (topMenus.children[0].children && topMenus.children[0].children.length > 0) {
                    console.log('11');

                    const lastChild = JSON.parse(
                        JSON.stringify(topMenus.children[0].children[0])
                    );
                    const fullPath = `${topMenus.path}/${topMenus.children[0].path}/${lastChild.path}`;
                    lastChild.path = fullPath;
                    proxy.$tab.refreshPage(lastChild);
                } else if (topMenus.query != null) {
                    console.log('12');
                    const lastChild = JSON.parse(JSON.stringify(topMenus));
                    const query = JSON.parse(topMenus.query);
                    lastChild.query = query;
                    proxy.$tab.refreshPage(lastChild);
                } else {
                    console.log('13');
                    proxy.$tab.refreshPage(topMenus.children[0]);
                }
            } else {
                // 如果当前路由地址在菜单权限中存在，刷新页面
                console.log('2');

                const currentPageData = {
                    path: router.currentRoute.value.path,
                    query: router.currentRoute.value.query,
                    params: router.currentRoute.value.params,
                    fullPath: router.currentRoute.value.fullPath,
                    meta: router.currentRoute.value.meta
                };
                console.log(currentPageData, '123123');

                proxy.$tab.refreshPage(currentPageData);
            }
            // 刷新当前页面
            proxy.$refs['topNavRef'].handleSelect('/dpp', null, false);
        });
    }
}

// 判断项目是否被禁用
const isProjectDisabled = (projectId, currentRow) => {
    // 判断当前项目是否已被选中，并且不是当前行
    return tableData.value.some((row) => row.projectId === projectId && row !== currentRow);
};
//-----------------------以上报工内容-------------------------

// 消息通知数量
const msgCount = ref(0);
const messages = ref([]);
const noticeList = ref([]);
const sessionValue = ref(null);
getMessageNum(); // 第一次主要获取消息

const wsUri = import.meta.env.VITE_APP_WEBSOCKET_API + '/websocket/message/' + userStore.userId;
// 建立socket连接
const ws = new WebSocket(wsUri);

const initWebSocket = () => {
    console.log('---------initWebSocket-------------');

    //查询通知公告
    listNotice().then((response) => {
        console.log('---------- response.rows-------------', response);
        response.rows.forEach((item) => {
            item.title = item.noticeTitle;
            item.entityType = item.noticeType;
            item.time =
                item.updateTime != undefined && item.updateTime != null
                    ? formatTimestamp(item.updateTime)
                    : formatTimestamp(item.createTime);
        });
        noticeList.value = response.rows;
    });

    //查询未读消息通知
    listMessage({
        receiverId: userStore.userId,
        hasRead: 0,
        pageNum: 1,
        pageSize: 1000
    }).then((response) => {
        response.data.rows?.forEach((item) => {
            item.time = item.updateTime;
            item.entityType = item.category;
            // item.title = item.title
        });
        messages.value = [...response.data.rows, ...messages.value];
        msgCount.value = messages.value ? messages.value.length : 0;
        console.log('------messages.value----', messages.value);
    });
    ws.onmessage = (event) => {
        // 服务端推送数据
        // console.log('===服务端推送数据=========>',event.data)
        const messageData = JSON.parse(event.data);
        console.log('===监测数据 messageData=========>', messageData);
        if (messageData) {
            messageData.time =
                messageData.updateTime != undefined && messageData.updateTime != null
                    ? formatTimestamp(messageData.updateTime)
                    : formatTimestamp(messageData.createTime);
            // messages.value.push(messageData)
            messages.value = [messageData, ...messages.value];
        }
        console.log('===存储的数据 messages=========>', messages.value);
        // 消息数量更新
        msgCount.value = messages.value ? messages.value.length : 0;
    };
};
const listProject = () => {
    console.log(userStore);

    if (userStore.id) {
        currentUser().then((response) => {
            console.log('---------- listProjectUserRel-------------', response);
            projectOptions.value = response.data;
            response.data[0] == null || response.data[0] == undefined
                ? (userStore.projectId = null)
                : (userStore.projectId = response.data[0].id);
            if (userStore.projectId) {
                const project = projectOptions.value.find(
                    (item) => item.id === userStore.projectId
                );
                if (project) {
                    userStore.projectCode = project.code;
                }
                getRoutersDpp(userStore.projectId).then((res) => {
                    // 更新store中的路由数据
                    permissionStore.updateTopbarRoutes(res.data);
                });
            }
        });
    }
};

onMounted(() => {
    initWebSocket();
    console.log(userStore);

    listProject();
});
// 页面注销
onBeforeUnmount(() => {
    console.log('------页面注销----');
    ws.close(); // 关闭socket
});

// 格式化时间戳为 YYYY-MM-DD HH:mm:ss 格式
function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 消息查询
function getMessageNum() {
    getNum();
}

// tab-click 事件处理函数
const handleClick = (tab) => {
    console.log('当前选中的 tab:', tab.props); // tab 是一个对象，包含当前被点击的 tab 的信息
    const label = tab.props.label;
    activeMsg.value = tab.props.name;
};
// 帮助文档
function openDocumentation() {
    window.open('https://qdata.qiantong.tech/docs', '_blank');
}
function toggleSideBar() {
    appStore.toggleSideBar();
}

function handleCommand(command) {
    switch (command) {
        case 'setLayout':
            setLayout();
            break;
        case 'logout':
            logout();
            break;
        case 'about':
            // 跳转到关于我们页面
            window.open('https://qiantong.tech/', '_blank');
            break;
        default:
            break;
    }
}

function logout() {
    ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    })
        .then(() => {
            userStore.logOut().then(() => {
                if (authType === 'sso') {
                    // 退出统一认证中心的登录状态
                    loginOut(userStore.userId).then(() => {
                        location.href = '/index';
                    });
                } else {
                    location.href = '/index';
                }
            });
        })
        .catch(() => { });
}

const emits = defineEmits(['setLayout']);

function setLayout() {
    emits('setLayout');
}

function handleRefreshClick() {
    const activeView = visitedViews.value.find((view) => view.path === route.path);
    proxy.$tab.refreshPage(activeView);
    if (route.meta.link) {
        useTagsViewStore().delIframeView(route);
    }
}

function messageDetail() {
    if (activeMsg.value == 'first') {
        router.push({ path: '/sys/system/bases/message' });
    } else {
        router.push({ path: '/sys/notice' });
    }
}

function clearNotification() {
    readAll().then(() => {
        messages.value = [];
        msgCount.value = 0;
        ElMessage.success('已全部已读！');
    });
}
</script>

<style lang="scss" scoped>
.ellipsis-option {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    width: 100%;
}

.custom-option-style .el-select-dropdown__item {
    display: flex;
    align-items: center;
}

::v-deep {
    .el-select__wrapper {
        box-shadow: 0 0 0 1px #dcdfe6 inset;
        border-radius: 2px !important;
    }
}

.message-list {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    overflow-y: auto;
    overflow-x: hidden;
}

.msg-item {
    cursor: pointer;
    display: flex;
    align-items: center;
    width: 100%;
    padding: 10px 16px;
    margin-bottom: 10px;
    background: #f9f9f9;
    border-radius: 4px;
}

.icon {
    width: 34px;
    height: 34px;
    margin-right: 12px;
}

.content {
    .title {
        font-size: 14px;
        font-weight: 500;
        color: rgba(0, 0, 0, 0.85);
        margin-bottom: 6px;
    }

    .time {
        font-size: 12px;
        color: rgba(0, 0, 0, 0.45);
    }
}

.navbar {
    height: 60px;
    overflow: hidden;
    position: relative;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    text-align: center;
    line-height: 60px;

    ::v-deep .size-icon--style {
        line-height: 60px;
    }

    .hamburger-container {
        line-height: 60px;
        height: 100%;
        float: left;
        cursor: pointer;
        transition: background 0.3s;
        -webkit-tap-highlight-color: transparent;

        &:hover {
            background: rgba(0, 0, 0, 0.025);
        }
    }

    .breadcrumb-container {
        float: left;
    }

    .topmenu-container {
        position: absolute;
        left: 50px;
    }

    .errLog-container {
        display: inline-block;
        vertical-align: top;
    }

    .right-menu {
        float: right;
        height: 100%;
        line-height: 60px;
        display: flex;

        ::v-deep .el-form-item__label {
            color: var(--el-text-color-regular) !important;
        }

        ::v-deep .el-form-item__label:before {
            content: '*';
            color: red !important;
            margin-top: 3px !important;
        }

        &:focus {
            outline: none;
        }

        .right-menu-item {
            display: inline-block;
            padding: 0 8px;
            height: 100%;
            font-size: 18px;
            color: #5a5e66;
            vertical-align: text-bottom;

            &.hover-effect {
                cursor: pointer;
                transition: background 0.3s;

                &:hover {
                    background: rgba(0, 0, 0, 0.025);
                }
            }
        }

        .rwgl-item {
            display: flex !important;
            align-items: center;

            img {
                height: 18px;
                display: block;
            }
        }

        .avatar-container {
            margin: 0 15px 0 0;

            .avatar-wrapper {
                display: flex;
                align-items: center;
                margin-top: 10px;
                position: relative;

                .user-avatar {
                    cursor: pointer;
                    width: 40px;
                    height: 40px;
                    border-radius: 20px;
                }

                .nickName {
                    font-size: 15px;
                    /*font-weight: bold;*/
                    // color: rgba(0, 0, 0, 0.65);
                    color: var(--themeColor);
                    display: inline-block;
                    margin-left: 10px;
                }

                i {
                    cursor: pointer;
                    position: absolute;
                    right: -20px;
                    top: 25px;
                    font-size: 12px;
                }
            }
        }
    }

    .flash ::v-deep .el-badge__content.is-fixed {
        animation: twinkle 1s infinite;
        /*margin-top: 16px;*/
        margin-right: 6px;
    }

    /* 定义闪烁的动画 */
    @keyframes twinkle {
        0% {
            opacity: 1;
            /* 完全可见 */
        }

        50% {
            opacity: 0.3;
            /* 半透明 */
        }

        100% {
            opacity: 1;
            /* 完全可见 */
        }
    }

    .item {
        height: 60px;
        line-height: 60px;
        display: inline-block;
        cursor: pointer;
    }

    .badge :deep(.el-badge__content.is-fixed) {
        top: 20px;
        transform: translateY(-50%) translateX(64%);
    }
}

.mag-tabs {
    height: calc(100% - 50px);

    ::v-deep .el-tabs__item {
        height: 50px;
        line-height: 50px;
    }

    ::v-deep .el-tabs__header {
        margin-bottom: 6px;
    }

    ::v-deep .el-tabs__content {
        height: calc(100% - 56px);

        .el-tab-pane {
            height: 100%;
        }
    }
}

.msg-btns {
    display: flex;
    height: 50px;
    line-height: 50px;
    border-top: 1px solid #e6e6e6;

    .btn-item {
        width: 50%;
        text-align: center;
        cursor: pointer;
        color: rgba(0, 0, 0, 0.85);

        &:last-child {
            border-left: 1px solid #e6e6e6;
        }
    }
}

#custom-header {
    background-color: rgb(248, 248, 248);
}

.el-dialog__header.show-close {
    text-align: left !important;
    padding: 9px 620px 9px 20px !important;
    background: rgb(248, 248, 248) !important;
}

.el-dialog__body {
    height: 500px;
}

/* 确保样式生效，增加选择器的优先级 */
.rounded-button,
.rounded-button .el-button {
    border-radius: 2px !important;
}
</style>
