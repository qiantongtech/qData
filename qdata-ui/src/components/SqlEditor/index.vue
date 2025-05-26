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
