<template>
  <div class="container" id="top">
    <div class="header-line"></div>
    <p class="title">{{ notice.noticeTitle }}</p>
    <p class="time">时间：{{ notice.createTime }}丨作者：{{ notice.createBy }}</p>
    <div class="content" id="ces">
      <div class="content-line">
        <template v-if="notice.noticeContentText != null">
          <div v-html="notice.noticeContentText"></div>
        </template>
        <div class="empty" v-else>
          <img src="@/assets/system/images/no_data/noData.png" alt="" />
          <!-- <span>暂无公告内容</span> -->
        </div>
      </div>
    </div>
    <div class="attachment" v-if="notice.attachmentUrl != null">
      附件：<a :href="notice.attachmentUrl">{{ notice.attachmentTitle }}</a>
    </div>
  </div>
</template>

<script setup name="NoticeDetail">
import { getNoticeOne } from "@/api/system/system/notice.js";
import moment from "moment";

const route = useRoute();
const notice = ref({});
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    if (newId !== null && newId !== undefined) {
      getViewById(newId);
    }
  },
  { immediate: true }
);
function getViewById(noticeId) {
  getNoticeOne(noticeId).then((res) => {
    notice.value = res.data;
    /** 展示的日期 */
    let format = moment(notice.value.createTime).format("YYYY-MM-DD");
    notice.value.createTime = format;
  });
}
</script>

<style scoped lang="scss">
.container {
  margin: 15px;
  scroll-behavior: smooth;
  min-height: calc(100vh - 94px) !important;
  background-color: #ffffff;
  flex-direction: row;
  padding-top: 20px;
  padding-bottom: 20px;
}
.form-div {
  width: 60%;
  margin-left: 20%;
}
.title {
  font-weight: bold;
  font-size: 28px;
  text-align: center;
  color: #333333;
  display: inline-block;
  width: 100%;
}

.time {
  text-align: center;
  font-size: 14px;
  width: 100%;
  color: #999;
}

p {
  margin: 10px 0 10px;
}

.attachment {
  margin-top: 20px;
  width: 100%;
  padding: 0 200px 0 200px;
  font-size: 14px;
  color: #3a71a8;
}

.content {
  line-height: 1.8;
  font-size: 14px;
  color: #333;
  overflow-wrap: break-word;
  max-width: 920px;
  margin: 0 auto;
  padding: 0 10px;

  ::v-deep &-line {
    border-top: 1px dashed #ddd;
    border-bottom: 1px dashed #ddd;
    padding: 10px 0;
  }

  ::v-deep h1, ::v-deep h2, ::v-deep h3 {
    font-weight: 600;
    margin: 0.67em 0;
  }

  ::v-deep h1 { font-size: 2em; }
  ::v-deep h2 { font-size: 1.5em; margin: 0.83em 0; }
  ::v-deep h3 { font-size: 1.17em; margin: 1em 0; }

  ::v-deep p {
    margin: 1em 0;
  }

  ::v-deep ul, ::v-deep ol {
    margin: 1em 0;
    padding-left: 2em;
  }

  ::v-deep img {
    display: block; // 避免下方空白间隙
    width: 100%;
    height: auto;
    margin: 1em 0;
    border-radius: 2px;
    text-align: center;
  }

  ::v-deep a {
    color: var(--el-color-primary);
    text-decoration: underline;
  }

  ::v-deep pre {
    background: #23241f;
    color: #f8f8f2;
    padding: 12px;
    border-radius: 2px;
    overflow-x: auto;
    white-space: pre-wrap; // 更友好的换行支持
    word-wrap: break-word;
  }

  ::v-deep blockquote {
    border-left: 4px solid #ccc;
    margin-bottom: 5px;
    margin-top: 5px;
    margin-left: 0px;
    padding-left: 16px;
  }

  ::v-deep .empty {
    background: #fff;
    border-radius: 2px;
    display: flex;
    flex-direction: column;
    align-items: center;

    img {
      width: 300px;
      height: 300px;
    }
  }
}
</style>
