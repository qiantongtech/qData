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
  <div>
    <textarea ref="mycode" className="codesql" :value="value" />
  </div>
</template>


<script>
import "codemirror/lib/codemirror.css";
import "codemirror/theme/blackboard.css";
import "codemirror/addon/hint/show-hint.css";
import CodeMirror from "codemirror/lib/codemirror";

// 导入所需的附加功能和模式
import "codemirror/addon/edit/matchbrackets";
import "codemirror/addon/selection/active-line";
import "codemirror/mode/sql/sql";
import "codemirror/addon/hint/show-hint";
import "codemirror/addon/hint/sql-hint";

// 如果需要引入样式文件（例如主题），也可以用同样的方式：
import "codemirror/lib/codemirror.css";
import "codemirror/addon/hint/show-hint.css";

export default {
  name: "SqlEditor",
  props: {
    value: {
      type: String,
      default: "",
    },
    sqlStyle: {
      type: String,
      default: "default",
    },
    readOnly: {
      type: [Boolean, String],
    },
  },
  data() {
    return {
      editor: null,
    };
  },
  computed: {
    // newVal() {
    //     if (this.editor) {
    //         return this.editor.getValue()
    //     }
    // }
  },
  watch: {
    //监听editor，发生变化就执行this.$emit('changeTextarea', this.editor)
    // editor(newV, oldV) {
    //     if (this.editor) {
    //         this.$emit('changeTextarea', this.editor)
    //     }
    // },
    // newVal(newV, oldV) {
    //     if (this.editor) {
    //         this.$emit('changeTextarea', this.editor)
    //     }
    // },
    // value(newVal) {
    //     console.log("我是value",newVal)
    //     console.log(this.editor.getValue())
    //     if (this.editor && newVal !== this.editor.getValue()) {
    //         this.$emit('changeTextarea', this.editor.getValue());
    //         this.editor.setValue(newVal);
    //     }
    // }
  },
  //设置失焦后自动执行 this.$emit('changeTextarea', this.editor.getValue())

  mounted() {
    const mime = "text/x-mariadb";
    const theme = "blackboard"; // 设置主题，不设置的会使用默认主题
    this.editor = CodeMirror.fromTextArea(this.$refs.mycode, {
      value: this.value,
      mode: mime, // 选择对应代码编辑器的语言，我这边选的是数据库，根据个人情况自行设置即可
      indentWithTabs: true,
      smartIndent: true,
      lineNumbers: true,
      matchBrackets: true,
      cursorHeight: 1,
      lineWrapping: true,
      readOnly: this.readOnly,
      theme: theme,
      autofocus: true,
      extraKeys: { Ctrl: "autocomplete" }, // 自定义快捷键
      hintOptions: {
        // 自定义提示选项
        completeSingle: false,
      },
    });
    // 代码自动提示功能，记住使用cursorActivity事件不要使用change事件，这是一个坑，那样页面直接会卡死
    // this.editor.on('inputRead', () => {
    //     this.editor.showHint()
    // })
    // 监听 blur 事件
    this.editor.on("blur", () => {
      this.$emit("changeTextarea", this.editor.getValue());
    });
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
.CodeMirror {
  border: 1px solid black;
  font-size: 13px;
}

// 这句为了解决匹配框显示有问题而加
.CodeMirror-hints {
  z-index: 9999 !important;
}
</style>
