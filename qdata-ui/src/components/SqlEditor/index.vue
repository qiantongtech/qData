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
  <div>
    <textarea ref="mycode" className="codesql" :value="value" />
  </div>
</template>


<script>
import "codemirror/lib/codemirror.css";
import "codemirror/theme/blackboard.css";
import "codemirror/addon/hint/show-hint.css";
import CodeMirror from "codemirror/lib/codemirror";

// Import required additional functions and modes
import "codemirror/addon/edit/matchbrackets";
import "codemirror/addon/selection/active-line";
import "codemirror/mode/sql/sql";
import "codemirror/addon/hint/show-hint";
import "codemirror/addon/hint/sql-hint";

// If you need to introduce style files (such as themes), you can also use the same method:
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
    //Monitor the editor and execute this.$emit('changeTextarea', this.editor) when changes occur.
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
    //     console.log("I am value",newVal)
    //     console.log(this.editor.getValue())
    //     if (this.editor && newVal !== this.editor.getValue()) {
    //         this.$emit('changeTextarea', this.editor.getValue());
    //         this.editor.setValue(newVal);
    //     }
    // }
  },
  //Automatically execute this.$emit('changeTextarea', this.editor.getValue()) after setting the defocus

  mounted() {
    const mime = "text/x-mariadb";
    const theme = "blackboard"; // Set the theme. If not set, the default theme will be used.
    this.editor = CodeMirror.fromTextArea(this.$refs.mycode, {
      value: this.value,
      mode: mime, // Select the language corresponding to the code editor. I chose the database here. You can set it according to your personal situation.
      indentWithTabs: true,
      smartIndent: true,
      lineNumbers: true,
      matchBrackets: true,
      cursorHeight: 1,
      lineWrapping: true,
      readOnly: this.readOnly,
      theme: theme,
      autofocus: true,
      extraKeys: { Ctrl: "autocomplete" }, // Custom shortcut keys
      hintOptions: {
        // Custom prompt options
        completeSingle: false,
      },
    });
    // Code automatic prompt function, remember to use the cursorActivity event and not the change event. This is a pitfall, and the page will freeze directly.
    // this.editor.on('inputRead', () => {
    //     this.editor.showHint()
    // })
    // Listen to the blur event
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

// This sentence is added to solve the problem of matching box display.
.CodeMirror-hints {
  z-index: 9999 !important;
}
</style>
