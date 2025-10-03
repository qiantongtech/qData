<template>
  <div class="app-container" ref="app-container">

    <GuideTip tip-id="dp/dpDocument" />

    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
        v-show="showSearch" @submit.prevent>
        <el-form-item label="标准名称" prop="search">
          <el-input class="el-form-input-width" v-model="queryParams.search" placeholder="请输入标准名称" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()"> <i
              class="iconfont-mini icon-a-zu22377 mr5"></i>查询 </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()"> <i
              class="iconfont-mini icon-a-zu22378 mr5"></i>重置 </el-button>
        </el-form-item>
      </el-form>
      <div class="list-title">
        共
        <span style="color: #ff9800"> {{ total }} </span>
        个标准，
        <span style="color: #0091ea">0</span>
        个逻辑模型，
        <span style="color: #0baa84">0</span>
        个数据元，
        <span style="color: #8876de">0</span>
        个代码表
      </div>
    </div>

    <div class="pagecont-bottom" v-loading="loading">
      <div class="page-list" v-if="total > 0">
        <el-row :gutter="15">
          <el-col :span="12" v-for="(item, index) in searchList" :key="index">
            <div class="page-item">
              <div class="item-title">
                <div class="item-title-left">
                  <img :src="getFileIcon(item.fileUrl)" alt="" />
                  <div class="item-name">
                    <span class="item-name-title" :title="item.name">{{ item.name }}</span>
                    <span class="item-name-code ellipsis" :title="item.fileName">{{ item.fileName }}</span>
                  </div>
                </div>
                <div class="item-title-right">
                  <div class="form-btn" @click="handleFilePreview(item.fileUrl)">
                    <!-- <img src="@/assets/dp/standardSearch/icon (2).svg" alt="" /> -->
                    <span>查看</span>
                  </div>
                  <div class="form-btn" @click="handleView(item)">
                    <!-- <img src="@/assets/dp/standardSearch/icon (1).svg" alt="" /> -->
                    <span>详情</span>
                  </div>
                </div>
              </div>
              <div class="item-con">
                <div class="item-form">
                  <div class="form-label">标准分类:</div>
                  <div class="form-value">
                    <dict-tag :options="dp_document_type" :value="item.type" />
                  </div>
                </div>
                <div class="item-form">
                  <div class="form-label">实施状态:</div>
                  <div class="form-value">
                    <dict-tag :options="dp_document_status" :value="item.status" />
                  </div>
                </div>
                <div class="item-form">
                  <div class="form-label">发布日期:</div>
                  <div class="form-value">
                    <div class="ellipsis">{{ item.releaseDate || "-" }}</div>
                  </div>
                </div>
                <div class="item-form">
                  <div class="form-label">实施日期:</div>
                  <div class="form-value">
                    <div class="ellipsis">{{ item.implementationDate || "-" }}</div>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="empty" v-else>
        <img src="@/assets/da/asset/empty.png" alt="" />
        <span>暂无搜索内容～</span>
      </div>
      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </div>
  </div>
</template>
<script setup name="Search">
import { dpDocumentList } from "@/api/dp/document/search";
import handleFilePreview from "@/utils/filePreview.js";
// search
const { proxy } = getCurrentInstance();
const { dp_document_standard_type, dp_document_type, dp_document_status } = proxy.useDict("dp_document_standard_type", "dp_document_type", "dp_document_status");
const searchList = ref([]);
const column1 = ref([
  {
    label: "标准分类",
    prop: "type",
  },
  {
    label: "标准状态",
    prop: "status",
  },
  {
    label: "发布日期",
    prop: "releaseDate",
  },
  {
    label: "实施日期",
    prop: "implementationDate",
  },
  {
    label: "标准文件",
    prop: "fileName",
    width: "100%",
  },
]);
const column2 = ref([
  {
    label: "归属标准",
    prop: "name",
  },
  {
    label: "发布日期",
    prop: "releaseDate",
  },
  {
    label: "实施日期",
    prop: "implementationDate",
  },
  {
    label: "模型名称",
    prop: "createTime",
  },
  {
    label: "模型代号",
    prop: "createTime",
  },
]);
const column3 = ref([
  {
    label: "归属标准",
    prop: "createTime",
  },
  {
    label: "发布日期",
    prop: "releaseDate",
  },
  {
    label: "实施日期",
    prop: "implementationDate",
  },
]);
const column4 = ref([
  {
    label: "归属标准",
    prop: "createTime",
  },
  {
    label: "代码表名称",
    prop: "createTime",
  },
  {
    label: "代码表代号",
    prop: "createTime",
  },
  {
    label: "发布日期",
    prop: "releaseDate",
  },
  {
    label: "实施日期",
    prop: "implementationDate",
  },
]);
const loading = ref(false);
const showSearch = ref(true);
const total = ref(0);
const router = useRouter();

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    search: null,
  },
});

const { queryParams } = toRefs(data);
const typeFormat1 = (row) => {
  return proxy.selectDictLabel(dp_document_type.value, row.type);
};
const typeFormat2 = (row) => {
  return proxy.selectDictLabel(dp_document_status.value, row.status);
};
const getFileIcon = (fileUrl) => {
  let type = fileUrl.split(".")[fileUrl.split(".").length - 1];
  switch (type) {
    case "pdf":
      return new URL("@/assets/dp/standardSearch/file (2).svg", import.meta.url).href;
    case "doc":
    case "docx":
      return new URL("@/assets/dp/standardSearch/file (1).svg", import.meta.url).href;
    case "ppt":
      return new URL("@/assets/dp/standardSearch/file (3).svg", import.meta.url).href;
    default:
      return new URL("@/assets/dp/standardSearch/file (4).svg", import.meta.url).href;
  }
};
/** 查询应用API服务关联列表 */
function getList() {
  loading.value = true;
  dpDocumentList(queryParams.value)
    .then((response) => {
      searchList.value = response.data.rows;
      total.value = response.data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

function handleView(row) {
  routeTo("/dp/document/search/detail", row);
}

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id,
        },
      });
    }
  }
}

getList();
</script>

<style lang="scss" scoped>
.pagecont-top {
  padding: 0 25px 0 15px;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .el-form-item {
    margin-bottom: 0;
  }

  .list-title {
    font-size: 14px;
    font-family: PingFang SC;
    font-weight: 500;
    color: #262626;

    span {
      font-family: DINPro-Bold;
      font-size: 18px;
    }
  }
}

.searchlist {
  padding: 15px;
  // height: 64vh;
  overflow-y: auto;

  &::-webkit-scrollbar {
    width: 2px;
  }

  .search-item {
    height: 146px;
    background: #ffffff;
    border-radius: 2px;
    margin-bottom: 16px;
    padding: 15px;
    border: 1px solid #ebeef5;

    .item-title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 10px;

      .item-title-left {
        display: flex;
        align-items: center;

        span {
          margin-left: 15px;
          font-size: 14px;
          font-family: PingFang SC;
          font-weight: 500;
          color: var(--el-color-primary);
        }
      }

      .item-title-right {
        display: flex;
        align-items: center;

        .form-btn {
          cursor: pointer;
          min-width: 50px;
          height: 24px;
          padding: 0 10px;
          border-radius: 2px;
          margin-left: 10px;
          display: flex;
          justify-content: center;
          align-items: center;
          border: 1px solid var(--el-color-primary);

          &.warn {
            border: 1px solid #ffab47;

            span {
              color: #ffab47;
            }
          }

          &.error {
            border: 1px solid #ff5353;

            span {
              color: #ff5353;
            }
          }

          img {
            width: 14px;
            height: 14px;
            margin-right: 6px;
          }

          span {
            font-family: PingFangSC, PingFang SC;
            font-weight: 500;
            font-size: 12px;
            color: var(--el-color-primary);
          }

          .el-icon {
            font-size: 14px;
            color: var(--el-color-primary);
            margin-right: 6px;
          }
        }
      }
    }

    .item-con {
      padding: 10px;
      background-color: #f5f7f9;
      height: calc(100% - 34px);

      .form-item {
        width: calc(25% - 15px);
        display: inline-flex;
        align-items: center;
        line-height: 30px;
        font-family: PingFang SC;
        font-weight: 400;
        font-size: 14px;
        margin-right: 20px;

        &:nth-child(4n) {
          margin-right: 0;
        }

        .form-label {
          color: rgba(0, 0, 0, 0.45);
        }

        .form-value {
          color: rgba(0, 0, 0, 0.85);
          flex: 1;
        }
      }
    }
  }
}

.pagecont-bottom {
  padding: 0;
  background-color: transparent;
  box-shadow: none;
}

.page-list {
  height: 69.8vh;
  height: auto;
  /* 或者直接删掉这行 */
  max-height: none;
  /* 保证不被限制高度 */
  overflow: visible;

  /* 不产生内部滚动条 */
  &::-webkit-scrollbar {
    width: 2px;
  }

  .page-item {
    padding: 18px 30px;
    background: #fff;
    margin-bottom: 15px;
    border-radius: 2px;

    .item-title {
      display: flex;
      padding-bottom: 15px;
      margin-bottom: 5px;
      border-bottom: 1px solid #eeeeee;

      .item-title-left {
        display: flex;
        align-items: center;
        width: 66%;

        img {
          width: 40px;
          height: 40px;
          margin-right: 20px;
        }

        .item-name {
          width: calc(100% - 72px);
          display: flex;
          flex-direction: column;

          .item-name-title {
            display: block;
            font-family: PingFang SC;
            font-weight: 600;
            font-size: 16px;
            color: #3d446e;
            line-height: 24px;
          }

          .item-name-code {
            display: block;
            font-family: PingFang SC;
            // font-weight: bold;
            font-size: 14px;
            color: rgba(88, 88, 88, 0.85);
            line-height: 22px;
          }
        }
      }

      .item-title-right {
        margin: 5px 0 0 auto;
        display: flex;

        .form-btn {
          cursor: pointer;
          min-width: 50px;
          height: 24px;
          padding: 0 12px;
          border-radius: 2px;
          margin-left: 12px;
          display: flex;
          justify-content: center;
          align-items: center;
          background: #e6f4ff;

          img {
            margin-right: 5px;
          }

          span {
            font-family: PingFang SC;
            font-weight: 500;
            font-size: 12px;
            color: #2666fb;
          }
        }
      }
    }

    .item-con {
      display: flex;
      flex-wrap: wrap;

      .item-form {
        width: 50%;
        display: flex;
        align-items: center;
        margin-top: 10px;

        .form-label {
          width: 76px;
          font-family: PingFang SC;
          font-weight: 400;
          font-size: 14px;
          color: #717171;
        }

        .form-value {
          width: calc(100% - 76px);
          font-family: PingFang SC;
          font-weight: 400;
          font-size: 14px;
          color: #262626;
        }

        .value-tag {
          width: 67px;
          height: 22px;
          display: flex;
          justify-content: center;
          align-items: center;
          font-family: PingFang SC;
          font-weight: 400;
          font-size: 13px;
          color: #ffffff;
          border-radius: 10px 10px 10px 0;

          &.type1 {
            background: #e23d3d;
          }

          &.type2 {
            background: #ff9800;
          }

          &.type3 {
            background: #3062f2;
          }

          &.type4 {
            background: #05a5a0;
          }
        }

        .value-tag2 {
          min-width: 56px;
          max-width: max-content;
          height: 24px;
          padding: 0 6px;
          display: flex;
          justify-content: center;
          align-items: center;
          font-family: PingFang SC;
          font-weight: 400;
          font-size: 13px;
          border-radius: 2px;

          &.status1 {
            background: #f8f2d9;
            color: #ff6f00;
            border: 1px solid #fca75e;
          }

          &.status2 {
            background: #e7f7f9;
            color: #05a5a0;
            border: 1px solid #85e5e2;
          }

          &.status3 {
            background: #e6f7ff;
            color: #1890ff;
            border: 1px solid #7ac3ff;
          }

          &.status4 {
            background: #fff1f0;
            color: #cf1322;
            border: 1px solid #ffa39e;
          }

          &.status5 {
            background: #e1f8e3;
            color: #219f09;
            border: 1px solid #82cb77;
          }

          &.status6 {
            background: #f4f4f5;
            color: #565656;
            border: 1px solid #cfcece;
          }
        }
      }
    }
  }
}

.pagination-container {
  height: 60px;
  background: #ffffff;
  border-radius: 2px;
  margin: 0px 0 0;
  padding: 14px 20px !important;

  :deep(.el-pagination) {
    right: 20px;
  }
}

.empty {
  min-height: calc(100vh - 250px);
  background: #ffffff;
  border-radius: 2px;
  display: flex;
  flex-direction: column;
  align-items: center;

  img {
    width: 200px;
    height: 180px;
    margin: 240px 0 40px;
  }

  span {
    font-family: PingFang SC;
    font-weight: 400;
    font-size: 18px;
    color: rgba(0, 0, 0, 0.65);
  }
}
</style>
