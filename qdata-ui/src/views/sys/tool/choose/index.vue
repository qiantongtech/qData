<template>
  <div class="app-container">
    <el-input
      v-model="input1"
      style="max-width: 300px; margin-right: 20px"
      placeholder="单选"
      class="input-with-select"
    >
      <template #append>
        <el-button :icon="Search" @click="radioShow" />
      </template>
    </el-input>

    <el-input
      v-model="input2"
      style="max-width: 300px"
      placeholder="多选"
      class="input-with-select"
    >
      <template #append>
        <el-button :icon="Search" @click="checkShow" />
      </template>
    </el-input>

    <!-- 单选  字典管理 -->
    <Current ref="dictRef1" @confirm="radioSubmit" />
    <!-- 多选  字典管理 -->
    <Selection ref="dictRef2" @confirm="checkSubmit" />
  </div>
</template>

<script setup name="ToolChoose">
import Current from "./temp-current.vue";
import Selection from "./userTypeMultiple.vue";
import { Search } from "@element-plus/icons-vue";
const dictRef1 = ref();
const dictRef2 = ref();
const input1 = ref("");
const input2 = ref("");
const radioVal = ref(null);
const checkVal = ref([]);

// 单选
function radioShow() {
  dictRef1.value.open(radioVal.value);
}

function radioSubmit(val) {
  radioVal.value = val;
  input1.value = val.dictName;
}

// 多选
function checkShow() {
  dictRef2.value.open(checkVal.value);
}
function checkSubmit(val) {
  checkVal.value = [...val];
  input2.value = val.map((item) => item.id);
}
</script>
