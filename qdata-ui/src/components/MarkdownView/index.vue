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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <div ref="contentRef" class="markdown-view markdown-body">
    <!-- 深度思考   -->
    <div
      v-html="deepThinking"
      v-if="deepThinking !== ''"
      style="background-color: #ddd; padding: 5px; border-radius: 5px"
    ></div>
    <!-- 对话输出 -->
    <div v-html="renderedMarkdown" ref="dialogue"></div>
    <!-- 文章引用 -->
    <div
      class="quote"
      v-if="documentIdList != null && documentIdList.length > 0"
    >
      <el-divider content-position="left">引用</el-divider>
      <el-popover
        placement="top"
        :width="400"
        trigger="click"
        popper-class="popover"
        popper-style="padding: 0;border-radius: 4px"
      >
        <template #reference>
          <div
            class="content"
            ref="quoteRef"
            v-for="(item, index) in documentIdList"
            @click="showDetail(item, index)"
          >
            <img :src="getFileType(documentNameList[index])" />
            <span>{{ documentNameList[index] }}</span>
          </div>
        </template>
        <div class="title">
          <img :src="getFileType(title)" />
          <span>{{ title }}</span>
        </div>
        <div class="content-body">
          <div class="item" v-for="(item, index) in resourcesList">
            <div class="segment"># {{ item.segmentPosition }}</div>
            <div class="content">
              {{ item.content }}
            </div>
            <el-divider v-if="index + 1 < resourcesList.length" />
          </div>
        </div>
      </el-popover>
    </div>
  </div>
</template>

<script setup>
import { useClipboard } from "@vueuse/core";
import MarkdownIt from "markdown-it";
import "highlight.js/styles/xcode.min.css";
import "@/assets/ai/style/dify_table.css";
import hljs from "highlight.js";
import { renderContent, getFileFormat } from "@/utils/app/chat/chat.js";
import { listByMessage } from "@/api/ai/retriever/resources";
import word from "@/assets/ai/office/WORD.png";
import excel from "@/assets/ai/office/ECEL.png";
import pdf from "@/assets/ai/office/PDF.png";
import ppt from "@/assets/ai/office/PPT.png";
import tet from "@/assets/ai/office/TET.png";

// 定义组件属性
const props = defineProps({
  messageId: {
    type: Number,
    required: false,
  },
  content: {
    type: String,
    required: true,
  },
  documentIdList: {
    type: Array,
    required: false,
    default: () => {
      return [];
    },
  },
  documentNameList: {
    type: Array,
    required: false,
    default: () => {
      return [];
    },
  },
});
const { proxy } = getCurrentInstance();
const router = useRouter();
const message = proxy.$modal; // 消息弹窗

const { copy } = useClipboard(); // 初始化 copy 到粘贴板
const title = ref("");
const resourcesList = ref([]);
const dialogue = ref();

const contentRef = ref();
const popoverRef = ref();
const quoteRef = ref();
const fileImg = {
  doc: word,
  docx: word,
  xlsx: excel,
  xls: excel,
  ppt: ppt,
  pptx: ppt,
  pdf: pdf,
  txt: tet,
};

const md = new MarkdownIt({
  html: true, // 允许解析 HTML（可选）
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        const copyHtml = `<div id="copy" data-copy='${str}' style="position: absolute; right: 10px; top: 5px; color: #fff;cursor: pointer;">复制</div>`;
        return `<pre style="position: relative;">${copyHtml}<code class="hljs">${
          hljs.highlight(lang, str, true).value
        }</code></pre>`;
      } catch (__) {}
    }
    return ``;
  },
});

const showDetail = (id, index) => {
  title.value = props.documentNameList[index];
  listByMessage({
    messageId: props.messageId,
    qtDocumentId: id,
  }).then((response) => {
    resourcesList.value = response.data;
    unref(popoverRef).popperRef?.delayHide?.();
  });
};

const getFileType = (name) => {
  return fileImg[getFileFormat(name)];
};

/** 复制 */
const copyContent = async () => {
  await copy(dialogue.value.textContent);
  message.msgSuccess("复制成功！");
};

const deepThinking = computed(() => {
  const content = props.content;
  const startTag = "<think";
  const endTag = "</think>";
  const startIndex = content.indexOf(startTag);

  if (startIndex === -1) {
    return "";
  }

  const afterStart = content.substring(startIndex, content.length);
  const endIndex = afterStart.indexOf(endTag);
  let remainingContent = "";

  if (endIndex !== -1) {
    remainingContent = afterStart.substring(0, endIndex);
  } else {
    remainingContent = afterStart;
  }
  return (
    '<span style="font-size: 12px;">思考中……</br></span>' + remainingContent
  );
});

/** 渲染 markdown */
const renderedMarkdown = computed(() => {
  const content = props.content;
  let remainingContent = renderContent(content);
  return md.render(remainingContent);
});

/** 初始化 **/
onMounted(async () => {
  // 添加 copy 监听
  contentRef.value.addEventListener("click", (e) => {
    if (e.target.id === "copy") {
      copy(e.target?.dataset?.copy);
      message.msgSuccess("复制成功!");
    }
  });
});

defineExpose({ copyContent }); // 提供方法给 parent 调用
</script>

<style lang="scss" scoped>
// 引入css

.markdown-view {
  font-family: PingFang SC;
  font-size: 0.95rem;
  font-weight: 400;
  line-height: 1.6rem;
  letter-spacing: 0em;
  text-align: left;
  color: #f0f0f6;
  max-width: 100%;

  pre {
    position: relative;
  }

  pre code.hljs {
    width: auto;
  }

  code.hljs {
    border-radius: 6px;
    padding-top: 20px;
    width: auto;
    @media screen and (min-width: 1536px) {
      width: 960px;
    }

    @media screen and (max-width: 1536px) and (min-width: 1024px) {
      width: calc(100vw - 400px - 64px - 32px * 2);
    }

    @media screen and (max-width: 1024px) and (min-width: 768px) {
      width: calc(100vw - 32px * 2);
    }

    @media screen and (max-width: 768px) {
      width: calc(100vw - 16px * 2);
    }
  }

  p,
  code.hljs {
    margin-bottom: 16px;
  }

  p {
    //margin-bottom: 1rem !important;
    margin: 0;
    margin-bottom: 3px;
  }

  /* 标题通用格式 */
  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    color: var(--color-G900);
    margin: 24px 0 8px;
    font-weight: 600;
  }

  h1 {
    font-size: 22px;
    line-height: 32px;
  }

  h2 {
    font-size: 20px;
    line-height: 30px;
  }

  h3 {
    font-size: 18px;
    line-height: 28px;
  }

  h4 {
    font-size: 16px;
    line-height: 26px;
  }

  h5 {
    font-size: 16px;
    line-height: 24px;
  }

  h6 {
    font-size: 16px;
    line-height: 24px;
  }

  /* 列表（有序，无序） */
  ul,
  ol {
    margin: 0 0 8px 0;
    padding: 0;
    font-size: 16px;
    line-height: 24px;
    color: #3b3e55; // var(--color-CG600);
  }

  li {
    margin: 4px 0 0 20px;
    margin-bottom: 1rem;
  }

  ol > li {
    list-style-type: decimal;
    margin-bottom: 1rem;
    // 表达式,修复有序列表序号展示不全的问题
    // &:nth-child(n + 10) {
    //     margin-left: 30px;
    // }

    // &:nth-child(n + 100) {
    //     margin-left: 30px;
    // }
  }

  ul > li {
    list-style-type: disc;
    font-size: 16px;
    line-height: 24px;
    margin-right: 11px;
    margin-bottom: 1rem;
    color: #3b3e55; // var(--color-G900);
  }

  ol ul,
  ol ul > li,
  ul ul,
  ul ul li {
    // list-style: circle;
    font-size: 16px;
    list-style: none;
    margin-left: 6px;
    margin-bottom: 1rem;
  }

  ul ul ul,
  ul ul ul li,
  ol ol,
  ol ol > li,
  ol ul ul,
  ol ul ul > li,
  ul ol,
  ul ol > li {
    list-style: square;
  }

  //  引用
  .quote {
    :deep(.el-divider__text) {
      background-color: #f0f0f6;
      padding: 0 10px;
    }
    .content {
      display: inline-flex;
      align-items: center;
      background: #fff;
      width: auto;
      padding: 5px;
      border-radius: 5px;
      cursor: pointer;
      img {
        width: 20px;
        margin-right: 5px;
      }
    }
  }

  // 资源文件
  :deep(.file-resources),
  :deep(.system-resources),
  :deep(.other-resources) {
    cursor: pointer;
    color: var(--el-color-primary);
    &:hover {
      text-decoration: underline;
    }
  }
}
.popover {
  .title {
    display: flex;
    align-items: center;
    background-color: rgb(249, 250, 251);
    padding: 12px 16px 8px 16px;
    border-top-left-radius: 4px;
    border-top-right-radius: 4px;
    img {
      width: 20px;
      margin-right: 5px;
    }
    span {
      --tw-text-opacity: 1;
      color: rgb(52 64 84 / var(--tw-text-opacity));
      font-weight: 500;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
    }
  }
  .content-body {
    padding: 8px 16px 8px 16px;
    --tw-bg-opacity: 1;
    background-color: rgb(255 255 255 / var(--tw-bg-opacity));
    border-radius: 4px;
    overflow-y: auto;
    max-height: 450px;
    .item {
      .segment {
        justify-content: space-between;
        align-items: center;
        display: inline-flex;
        border-width: 1px;
        --tw-border-opacity: 1;
        border-color: rgb(234 236 240 / var(--tw-border-opacity));
        border-radius: 6px;
        height: 20px;
        --tw-text-opacity: 1;
        color: rgb(102 112 133 / var(--tw-text-opacity));
        font-weight: 500;
        font-size: 11px;
        border-style: solid;
        padding: 5px;
        margin-bottom: 5px;
      }
      .content {
        --tw-text-opacity: 1;
        color: rgb(29 41 57 / var(--tw-text-opacity));
        font-size: 13px;
        overflow-wrap: break-word;
      }
    }
  }
}
</style>
