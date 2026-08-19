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
  <div class="navbar" ref="navbar">
    <logo
      v-if="appStore.sidebar.hide && isOnlyLogoRoute"
      :collapse="false"
      class="navbar-logo"
      :current-route="route.path"
    />
    <hamburger
      id="hamburger-container"
      :is-active="appStore.sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />
    <breadcrumb
      id="breadcrumb-container"
      class="breadcrumb-container"
      v-if="!settingsStore.topNav"
    />
    <top-nav
      ref="topNavRef"
      @getRouter="getRouter"
      id="topmenu-container"
      class="topmenu-container"
      v-if="settingsStore.topNav"
      :class="{ 'has-navbar-logo': appStore.sidebar.hide && isOnlyLogoRoute }"
    />
    <div class="right-menu">
      <template v-if="appStore.device !== 'mobile'">
        <div style="margin-top: 10px" v-if="showProjectSelector">
          <el-form
            class="btn-style"
            :model="userStore"
            ref="queryRef"
            :inline="true"
          >
            <el-form-item
              :label="t('sys.dashboard.projectLabel')"
              prop="projectId"
              :rules="[
                {
                  required: true,
                  message: t('sys.dashboard.projectPlaceholder'),
                  trigger: 'change',
                },
              ]"
            >
              <el-select
                style="width: 150px"
                :fit-input-width="true"
                v-model="userStore.projectId"
                @change="projectIdChange"
                :placeholder="t('sys.dashboard.projectPlaceholder')"
                clearable
                popper-class="custom-option-style"
              >
                <el-option
                  v-for="item in projectOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                  <template #default>
                    <template v-if="item.name.length > 6">
                      <el-tooltip
                        placement="left"
                        :content="item.name"
                        effect="dark"
                      >
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
                                @change="projectIdChange" placeholder="Please select the project" clearable>
                                <el-option v-for="item in projectOptions" :key="item.id" :label="item.name"
                                    :value="item.id" />
                            </el-select> -->
            </el-form-item>
          </el-form>
        </div>
        <el-dropdown
          id="language-select"
          class="right-menu-item hover-effect"
          @command="handleLanguageChange"
        >
          <div
            class="language-select-wrapper"
            style="
              height: 100%;
              display: flex;
              align-items: center;
              justify-content: center;
            "
          >
            <svg-icon iconClass="i18n" style="font-size: 22px" />
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="zh-CN" :disabled="locale === 'zh-CN'">
                简体中文
              </el-dropdown-item>
              <el-dropdown-item command="en-US" :disabled="locale === 'en-US'">
                English
              </el-dropdown-item>
              <el-dropdown-item command="ja-JP" :disabled="locale === 'ja-JP'">
                日本語
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <div class="right-menu-item hover-effect" @click="openDocumentation">
          <svg-icon iconClass="bzzx" style="font-size: 18px" />
        </div>
        <!-- ------------------------------- Reporting for work ---------------------------------- -->
        <el-popover
          trigger="hover"
          popper-style="
                        width: 336px;
                        height: 360px;
                        background: #FFFFFF;
                        box-shadow: 0px 2px 8px 0px rgba(0,0,0,0.15);
                        padding:0;
                    "
        >
          <template #reference>
            <el-badge
              :value="msgCount"
              :max="99"
              class="badge"
              :class="msgCount > 0 ? 'flash' : ''"
              :offset="[0, 0]"
              :hidden="msgCount == 0"
            >
              <!-- <i class="iconfont right-menu-item hover-effect" style="font-size: 22px">&#xebe7;</i> -->
              <i
                class="iconfont icon-a-dingbulingdangxianxing right-menu-item hover-effect"
                style="font-size: 20px"
              ></i>
            </el-badge>
          </template>
          <template #default>
            <el-tabs
              v-model="activeMsg"
              stretch
              class="mag-tabs"
              @tab-click="handleClick"
            >
              <el-tab-pane :label="t('sys.dashboard.messageReminder')" name="first">
                <div class="message-list">
                  <div
                    class="msg-item"
                    v-for="(msg, index) in messages"
                    :key="index"
                    v-show="messages.length > 0"
                  >
                    <img
                      class="icon"
                      src="@/assets/images/system/layout/msg/toolbar-icon1.png"
                      alt=""
                    />
                    <div class="content">
                      <div class="title">{{ msg.title }}</div>
                      <div class="time">{{ msg.time }}</div>
                    </div>
                  </div>
                  <el-empty
                    v-show="
                      messages.length == 0 ||
                      messages == null ||
                      messages == 'undefined'
                    "
                    :image-size="100"
                    :description="td('common.noMessage')"
                    class="empty-block"
                  />
                </div>
              </el-tab-pane>
              <el-tab-pane :label="t('sys.dashboard.notification')" name="second">
                <!--                <message-list :msg-category="'first'"></message-list>-->
                <div class="message-list">
                  <div
                    class="msg-item"
                    v-for="(msg, index) in noticeList"
                    :key="index"
                    v-show="msg.entityType == 1"
                    @click="handleMessage(msg)"
                  >
                    <img
                      class="icon"
                      src="@/assets/images/system/layout/msg/toolbar-icon1.png"
                      alt=""
                    />
                    <div class="content">
                      <div class="title">{{ msg.title }}</div>
                      <div class="time">{{ msg.time }}</div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane :label="t('sys.dashboard.announcement')" name="third">
                <!--                <message-list :msg-category="'second'"></message-list>-->
                <div class="message-list">
                  <div
                    class="msg-item"
                    v-for="(msg, index) in noticeList"
                    :key="index"
                    v-show="msg.entityType == 2"
                    @click="handleMessage(msg)"
                  >
                    <img
                      class="icon"
                      src="@/assets/images/system/layout/msg/toolbar-icon1.png"
                      alt=""
                    />
                    <div class="content">
                      <div class="title">{{ msg.title }}</div>
                      <div class="time">{{ msg.time }}</div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
            <div class="msg-btns">
              <div class="btn-item" @click="clearNotification">{{ t('sys.dashboard.markAllRead') }}</div>
              <div class="btn-item" @click="messageDetail">{{ t('sys.dashboard.viewMore') }}</div>
            </div>
          </template>
        </el-popover>
        <div class="right-menu-item hover-effect" @click="handleRefreshClick">
          <!-- <el-icon size="22">
                        <Refresh />
                    </el-icon> -->
          <i
            class="iconfont icon-a-shuaxinxianxing"
            style="font-size: 20px"
          ></i>
        </div>

        <header-search id="header-search" class="right-menu-item" />

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <!-- <el-tooltip content="Layout size" effect="dark" placement="bottom">
                  <size-select id="size-select" class="right-menu-item hover-effect" />
                </el-tooltip> -->
      </template>
      <div class="avatar-container">
        <el-dropdown
          @command="handleCommand"
          class="right-menu-item hover-effect"
          trigger="click"
        >
          <div class="avatar-wrapper">
            <img :src="userStore.avatar" class="user-avatar" />
            <span class="nickName">{{ nickName }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <router-link to="/user/profile">
                <el-dropdown-item>{{ t('sys.dashboard.personalCenter') }}</el-dropdown-item>
              </router-link>
              <el-dropdown-item
                command="setLayout"
                v-if="settingsStore.showSettings"
              >
                <span>{{ t('sys.dashboard.layoutSettings') }}</span>
              </el-dropdown-item>
<!--               <el-dropdown-item command="about">-->
<!--                                <span>{{ t('sys.dashboard.aboutUs') }}</span>-->
<!--                            </el-dropdown-item>-->
              <el-dropdown-item divided command="logout">
                <span>{{ t('sys.dashboard.logout') }}</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <el-dialog
      :title="t('sys.dashboard.aboutUs')"
      class="about-dialog"
      v-model="activeOpen"
      append-to-body
      align-center
    >
      <div class="about-content-wrapper">
        <img
          src="@/assets/images/system/login/logo-qdata.png"
          alt="qData Logo"
          class="logo"
        />
        <div class="about-title">
          {{ t('sys.dashboard.version') }}:v{{ version }}
          <!-- <span class="version-badge"></span> -->
        </div>
        <div class="copyright">©{{ year }}{{ td('login.info.companyName') }}{{ td('login.info.copyrightOwner') }}</div>
      </div>

      <template #footer>
        <div class="about-footer">
          <div v-if="!needUpdate" class="status-text">
            {{ t('sys.dashboard.version') }}{{ version }}{{ t('sys.dashboard.isLatestVersion') }}
          </div>
          <div v-else class="status-text">
            {{ t('sys.dashboard.latestVersion') }}:
            <a
              href="javascript:void(0)"
              @click.prevent="openUpdateLog"
              rel="noopener noreferrer"
              class="update-link"
            >
              v{{ latestVersion }}
            </a>
          </div>
          <div class="head-btns">
            <el-button type="primary" @click="openUpdateLog">
              {{ t('sys.dashboard.updateLog') }}</el-button>
          </div>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Navbar">
import { useI18n } from "vue-i18n";
import { useWindowSize } from "@vueuse/core";
import { ElMessageBox } from "element-plus";
import Breadcrumb from "@/components/Breadcrumb";
import TopNav from "@/components/TopNav";
import Hamburger from "@/components/Hamburger";
import Logo from "./Sidebar/Logo";
import Screenfull from "@/components/Screenfull";
import SizeSelect from "@/components/SizeSelect";
import HeaderSearch from "@/components/HeaderSearch";
import useAppStore from "@/store/system/app";
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
import useUserStore from "@/store/system/user";
import useSettingsStore from "@/store/system/settings";
import useTagsViewStore from "@/store/system/tagsView";
import defaultSettings from "@/settings";
import {
  getNum,
  listMessage,
  readAll,
} from "@/api/system/system/message/message";
import { loginOut } from "@/api/system/sso-auth.js";
// import MessageList from "@/views/sys/system/message/components/messageList.vue";
import { onMounted, ref, watch } from "vue";
import moment from "moment";
import { listNotice } from "@/api/system/system/notice";
import { currentUser } from "@/api/att/project/project";
import { da, id } from "element-plus/es/locale/index.mjs";
import usePermissionStore from "@/store/system/permission";
import { getRoutersDpp } from "@/api/system/menu";
import { useLocale } from "@/composables/useLocale";
import useLocaleStore from "@/store/system/locale.js";
import packageInfo from "../../../package.json";

const { t, locale } = useI18n();
const { changeLocale } = useLocale();
const localeStore = useLocaleStore();
import { getCurrentAppVersion,addVersionTrack } from "@/api/system/update/update.js";
// import {listProject, getProject} from "@/api/project/projectBase/project";
// import {listReport, getReport, delReport, addReport, updateReport} from "@/api/project/report/report";
// Authentication mode
const authType = import.meta.env.VITE_APP_AUTH_TYPE;

const route = useRoute();
const router = useRouter();
const appStore = useAppStore();
const userStore = useUserStore();
const settingsStore = useSettingsStore();
const { proxy } = getCurrentInstance();
const visitedViews = computed(() => useTagsViewStore().visitedViews);
const isOnlyLogoRoute = computed(() => {
  const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
  return navbarLogoRoutes.some((logoPath) => route.path.startsWith(logoPath));
});

const nickName = computed(() => {
    const name = userStore.nickName;
    const userId = userStore.id;
    if (userId === 1) {
        return td('common.texts.superAdmin');
    }
    return name;
});
let isFlag = ref(false);
// Message type selected by default
const activeMsg = ref("first");
const projectId = ref("");
const permissionStore = usePermissionStore();

const needUpdate = ref(false);
const currentVersion = ref("");
const latestVersion = ref("");
const { name, version, description, author } = packageInfo;
// All routing information
const routers = computed(() => permissionStore.topbarRouters);
//-----------------------The following job application contents-------------------------
const handleMessage = (msg) => {
  console.log("Received message:", msg);
  router.push({
    path: "/sys/system/notice/detail",
    query: { id: msg.noticeId },
  });
};
const data = reactive({
  form: {
    reportExperience: null,
  },
  rules: {
    reportExperience: [
      { required: true, message: t("sys.report.experienceRequired"), trigger: "blur" },
    ],
  },
});
const { width } = useWindowSize();
const showProjectSelector = computed(() => width.value >= 1200 && isFlag.value);
const open = ref(false);
const title = ref(null);
const form = ref({});
const projectOptions = ref([]);

const tableData = ref([{ projectId: null, duration: null }]);

function resetFromWork() {
  tableData.value = [{ projectId: null, duration: null }];
  form.value.reportExperience = null;
}

//Asking for leave
function offFromWork() {
  proxy.$modal
    .confirm(t("sys.report.confirmLeave"))
    .then(function () {})
    .then(() => {
      const itemList = tableData.value;
      const req = {
        reportExperience: t("sys.report.onLeave"),
        status: 1,
        reportTime: new Date(),
        detailRespVOList: tableData.value,
      };
      console.log("---------Submit leave request-------", req);
      addReport(req)
        .then((response) => {
          proxy.$modal.msgSuccess(t("sys.report.submitSuccess"));
          open.value = false;
          getList();
        })
        .catch((error) => {});
    })
    .catch(() => {});
  // form.value.reportExperience = 'I asked for leave'
}

function getRouter(data) {
  if (data.includes("/dpp")) {
    isFlag.value = true;
  } else {
    isFlag.value = false;
  }
}

/** submit button */
function submitForm() {
  if (form.value.reportExperience == null) {
    proxy.$modal.msgWarning(t("sys.report.experienceEmpty"));
    return;
  }
  proxy.$refs["reportRef"].validate((valid) => {
    console.log("---------Validation-------", valid);
    if (valid) {
      const tempList = tableData.value;
      if (tempList.length == 0) {
        proxy.$modal.msgError(t("sys.report.projectEmpty"));
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
        proxy.$modal.msgWarning(t("sys.report.projectEmpty"));
        return;
      }
      if (timeStatus) {
        proxy.$modal.msgWarning(t("sys.report.durationEmpty"));
        return;
      }
      // Extract all non-empty projectIds and concatenate with commas
      form.value.reportContent = tempList
        .map((item) => item.projectId)
        .filter((id) => id != null) // Filter out null or undefined values
        .join(",");

      if (form.value.id != null) {
        const tempList = tableData.value.map((e) => {
          const date = new Date(e.reportTime);
          return {
            ...e,
            reportTime: isNaN(date.getTime()) ? null : date, // If invalid, set to null
          };
        });
        const req = {
          ...form.value,
          createTime: new Date(form.value.createTime),
          reportTime: new Date(form.value.reportTime),
          updateTime: new Date(),
          detailRespVOList: tempList,
        };
        updateReport(req)
          .then((response) => {
            proxy.$modal.msgSuccess(t("common.message.editSuccess"));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        const itemList = tableData.value;
        const req = {
          ...form.value,
          status: 0,
          reportTime: new Date(),
          detailRespVOList: tableData.value,
        };
        console.log("---------Submit request-------", req);
        addReport(req)
          .then((response) => {
            proxy.$modal.msgSuccess(t("sys.report.submitSuccess"));
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

// Delete operation
const deleteItem = (index) => {
  // Use splice method to delete data based on index
  tableData.value.splice(index, 1);
  console.log("Deleted item at index", index, "item");
};

const addItem = () => {
  tableData.value.push({ name: "aa" });
};

const popoverVisible = ref(false);

const handleFocus = () => {
  popoverVisible.value = true;
};
const handleBlur = () => {
  popoverVisible.value = false;
};
const handleSelectChange = (value) => {
  console.log("Selected option:", value);
};

const handlePopoverClick = (value) => {
  // If you don't want to close Popover, you can handle additional logic here
};

//Open the job reporting page
function openForWork() {
  tableData.value = [{ projectId: null, duration: null }];
  form.value.reportExperience = null;
  title.value = t("sys.report.newReport");
  open.value = true;
}

function cancel() {
  open.value = false;
}

//Reporting management
function reportingForWork() {
  router.push({ path: "/project/report" });
}

function projectIdChange(row, newValue) {
  // Get project code from projectOptions
  const project = projectOptions.value.find(
    (item) => item.id === userStore.projectId
  );
  if (project) {
    userStore.projectCode = project.code;
  }
  if (userStore.projectId) {
    localStorage.setItem("qdataProjectId", userStore.projectId);
    getRoutersDpp(userStore.projectId).then((res) => {
      // Update routing data in the store
      permissionStore.updateTopbarRoutes(res.data);
      let topMenus = [];
      routers.value.map((menu) => {
        if (menu.path === "/dpp") {
          topMenus = menu;
        }
      });
      const currentPath = router.currentRoute.value.path.split("/"); // Get the current routing address
      const menuPaths = topMenus.children.flatMap((child) =>
        child.children
          ? child.children.map((subChild) => subChild.path)
          : child.path
      ); // Get the path in the menu permissions. If there are sub-nodes, get the path of the sub-nodes.
      console.log("---------currentPath-------------", currentPath);
      console.log("---------menuPaths-------------", menuPaths);

      if (!menuPaths.includes(currentPath[currentPath.length - 1])) {
        //Clear tab
        proxy.$tab.closeAllPage();
        console.log("1");
        // If it does not exist, jump to the first menu
        if (
          topMenus.children[0].children &&
          topMenus.children[0].children.length > 0
        ) {
          console.log("11");

          const lastChild = JSON.parse(
            JSON.stringify(topMenus.children[0].children[0])
          );
          const fullPath = `${topMenus.path}/${topMenus.children[0].path}/${lastChild.path}`;
          lastChild.path = fullPath;
          proxy.$tab.refreshPage(lastChild);
        } else if (topMenus.query != null) {
          console.log("12");
          const lastChild = JSON.parse(JSON.stringify(topMenus));
          const query = JSON.parse(topMenus.query);
          lastChild.query = query;
          proxy.$tab.refreshPage(lastChild);
        } else {
          console.log("13");
          proxy.$tab.refreshPage(topMenus.children[0]);
        }
      } else {
        // If the current routing address exists in the menu permissions, refresh the page
        console.log("2");

        const currentPageData = {
          path: router.currentRoute.value.path,
          query: router.currentRoute.value.query,
          params: router.currentRoute.value.params,
          fullPath: router.currentRoute.value.fullPath,
          meta: router.currentRoute.value.meta,
        };
        console.log(currentPageData, "123123");
        if (currentPageData.path !== currentPath.join("/")) {
          proxy.$tab.refreshPage(currentPageData);
        }
      }
      // Refresh current page
      proxy.$refs["topNavRef"].handleSelect("/dpp", null, false);
    });
  }
}

// Determine whether an item is disabled
const isProjectDisabled = (projectId, currentRow) => {
  // Determine whether the current item has been selected and is not the current row
  return tableData.value.some(
    (row) => row.projectId === projectId && row !== currentRow
  );
};
//-----------------------The above job application content----------------------

// Number of message notifications
const msgCount = ref(0);
const messages = ref([]);
const noticeList = ref([]);
const sessionValue = ref(null);
getMessageNum(); // Mainly get news for the first time

const wsUri =
  import.meta.env.VITE_APP_WEBSOCKET_API +
  "/websocket/message/" +
  userStore.userId;
// Establish socket connection
const ws = new WebSocket(wsUri);

const initWebSocket = () => {
  console.log("---------initWebSocket-------------");

  //Inquiry notification announcement
  listNotice().then((response) => {
    console.log("---------- response.rows-------------", response);
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

  //Query unread message notifications
  listMessage({
    receiverId: userStore.userId,
    hasRead: 0,
    pageNum: 1,
    pageSize: 1000,
  }).then((response) => {
    response.data.rows?.forEach((item) => {
      item.time = item.updateTime;
      item.entityType = item.category;
      // item.title = item.title
    });
    messages.value = [...response.data.rows, ...messages.value];
    msgCount.value = messages.value ? messages.value.length : 0;
    console.log("------messages.value----", messages.value);
  });
  ws.onmessage = (event) => {
    // Server push data
    // console.log('====Server push data==========>',event.data)
    const messageData = JSON.parse(event.data);
    console.log("===Monitored messageData=========>", messageData);
    if (messageData) {
      messageData.time =
        messageData.updateTime != undefined && messageData.updateTime != null
          ? formatTimestamp(messageData.updateTime)
          : formatTimestamp(messageData.createTime);
      // messages.value.push(messageData)
      messages.value = [messageData, ...messages.value];
    }
    console.log("===Stored messages=========>", messages.value);
    // Message count update
    msgCount.value = messages.value ? messages.value.length : 0;
  };
};
const listProject = () => {
  if (userStore.id) {
    currentUser().then((response) => {
      console.log("---------- listProjectUserRel-------------", response);
      projectOptions.value = response.data;
      response.data[0] == null || response.data[0] == undefined
        ? (userStore.projectId = null)
        : (userStore.projectId = response.data[0].id);
      if (!response.data || response.data.length == 0) {
        localStorage.removeItem("qdataProjectId");
      }
      var qdataProjectId = localStorage.getItem("qdataProjectId");
      if (userStore.projectId) {
        if (qdataProjectId) {
          userStore.projectId = parseInt(qdataProjectId);
        } else {
          localStorage.setItem("qdataProjectId", userStore.projectId);
        }
        const project = projectOptions.value.find(
          (item) => item.id === userStore.projectId
        );
        if (project) {
          userStore.projectCode = project.code;
        }
        getRoutersDpp(userStore.projectId).then((res) => {
          // Update routing data in the store
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
  /*getCurrentAppVersion().then((res) => {
    if (res.data != null) {
      // Is it the latest version?
      needUpdate.value = res.data.needUpdate;
      //Local version number
      currentVersion.value = res.data.currentVersion;
      //Latest version number
      latestVersion.value = res.data.latestVersion;
    }
  });*/
});
// Page logout
onBeforeUnmount(() => {
  console.log("------Page logout----");
  ws.close(); // close socket
});

// Format the timestamp in YYYY-MM-DD HH:mm:ss format
function formatTimestamp(timestamp) {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0"); // Month starts from 0
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// Message query
function getMessageNum() {
  getNum();
}

// tab-click event handler
const handleClick = (tab) => {
  console.log("Currently selected tab:", tab.props); // tab is an object containing information about the currently clicked tab
  const label = tab.props.label;
  activeMsg.value = tab.props.name;
};
// Help documentation
function openDocumentation() {
  const lang = localeStore.getCurrentLocale.lang || locale.value;
  const localePathMap = {
    "en-US": "/en",
    "ja-JP": "/ja",
  };
  const localePath = localePathMap[lang] || "";
  window.open(`https://community.qdata.tech${localePath}/docs/overview/introduction.html`, "_blank");
}
function toggleSideBar() {
  appStore.toggleSideBar();
}

async function handleLanguageChange(lang) {
  if (lang === localeStore.getCurrentLocale.lang) return;
  localeStore.setCurrentLocale({ lang });
  await changeLocale(lang);
  window.location.reload();
}

const activeOpen = ref(false);

function handleAboutUs() {
  activeOpen.value = true;
}

function openUpdateLog() {
  addVersionTrack({
    name,
    currVersion: version,
    description,
    author,
  }).catch((error) => {
    console.error("Add version track failed:", error);
  });
  window.open("https://gitee.com/qiantongtech/qData/releases", "_blank");
}

function handleCommand(command) {
  switch (command) {
    case "setLayout":
      setLayout();
      break;
    case "logout":
      logout();
      break;
    case "about":
      // Jump to about us page
      // window.open('https://qiantong.tech/', '_blank');
      handleAboutUs();
      break;
    default:
      break;
  }
}

function logout() {
  ElMessageBox.confirm(t("sys.dashboard.confirmLogout"), t("common.message.prompt"), {
    confirmButtonText: t("common.button.confirm"),
    cancelButtonText: t("common.button.cancel"),
    type: "warning",
  })
    .then(() => {
      userStore.logOut().then(() => {
        if (authType === "sso") {
          // Exit the login status of the Unified Certification Center
          loginOut(userStore.userId).then(() => {
            location.href = "/index";
          });
        } else {
          location.href = "/index";
        }
      });
    })
    .catch(() => {});
}

const emits = defineEmits(["setLayout"]);

function setLayout() {
  emits("setLayout");
}

function handleRefreshClick() {
  const activeView = visitedViews.value.find(
    (view) => view.path === route.path
  );
  proxy.$tab.refreshPage(activeView);
  if (route.meta.link) {
    useTagsViewStore().delIframeView(route);
  }
}

function messageDetail() {
  if (activeMsg.value == "first") {
    router.push({ path: "/sys/system/bases/message" });
  } else {
    router.push({ path: "/sys/notice" });
  }
}

function clearNotification() {
  readAll().then(() => {
    messages.value = [];
    msgCount.value = 0;
    ElMessage.success(t("sys.report.markAllReadDone"));
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

  .navbar-logo {
    float: left;
    width: 200px !important;
    height: 100% !important;
    background-color: transparent !important;

    ::v-deep.sidebar-logo-link {
      background-color: transparent !important;
    }

    ::v-deep.sidebar-logo {
      height: 48px !important;
      margin-top: 6px !important;
      transform: none !important;
    }
  }

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

    &.has-navbar-logo {
      left: 200px;
    }
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
      content: "*";
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

  /* Define blinking animation */
  @keyframes twinkle {
    0% {
      opacity: 1;
      /* fully visible */
    }

    50% {
      opacity: 0.3;
      /* translucent */
    }

    100% {
      opacity: 1;
      /* fully visible */
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

/* Make sure the style is effective and increase the priority of the selector */
.rounded-button,
.rounded-button .el-button {
  border-radius: 2px !important;
}

.about-content-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  //padding: 27px 0;
  //gap: 16px;

  .logo {
    height: 34px;
    width: 146px;
    margin-top: 27px;
  }

  .about-title {
    margin-top: 20px;
    font-family: PingFang SC;
    font-weight: 600;
    font-size: 22px;
    color: #333333;

    .version-badge {
      background-color: #409eff;
      color: white;
      padding: 2px 8px;
      border-radius: 4px;
      margin-left: 6px;
    }
  }

  .copyright {
    margin-top: 27px;
    font-family: PingFang SC;
    font-weight: 500;
    font-size: 16px;
    color: #6e7a97;
  }
}

.about-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 35px;
  border-top: 1px solid var(--el-border-color-light); // Using Element Plus theme variables

  .status-text {
    font-family: PingFang SC;
    font-weight: 600;
    font-size: 18px;
    color: #333333;
  }
  .update-link {
    color: #126bed; // Element Plus primary color, you can also use var(--el-color-primary)
    text-decoration: underline;
    cursor: pointer;
    font-size: 18px;
    transition: color 0.2s;

    &:hover {
      color: #66b1ff; // Color brightens on mouseover
    }

    &:active {
      color: #3a8ee6; // The color becomes darker when clicked
    }
  }
  .head-btns {
    img {
      margin-right: 6px;
    }
    .currImg {
      display: inline-block;
    }

    .act {
      display: none;
    }

    .el-button {
      height: 34px;
      width: 114px;
      border-radius: 4px !important;
      font-size: 18px;
      font-family: PingFang SC;
      font-weight: 600;
      color: #ffffff;

      &:hover {
        .act {
          display: inline-block;
        }

        .currImg {
          display: none;
        }
      }
    }
  }
}
.markdown-content {
  padding: 0 15px 15px 15px;
}
</style>

<style lang="scss">
.about-dialog:not(.is-fullscreen) {
  margin: auto !important;
  width: 600px;
  height: 300px;
  padding: 0;
  .el-dialog__header {
    height: 47px !important;
    background: #f8f8f8 !important;
    line-height: 47px;
    padding-left: 27px;
    color: #333333;
    padding-bottom: 0px;
    font-family: PingFang SC;
    .el-dialog__close {
      font-size: 18px;
      color: #6a6a6a;
      font-weight: bold;
    }

    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
      color: #333333;
    }
  }
  .el-dialog__footer {
    padding-top: 0px;
  }
  .about-footer {
    padding: 11px 32px;
  }
}
</style>
