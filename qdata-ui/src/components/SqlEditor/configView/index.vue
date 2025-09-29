<template>
  <div class="container configView" :style="{ width: `${currWidth}px` }">
    <div class="move" @mousedown="resizeCurrDialog"></div>
    <div class="container-header">
      <span class="title">{{ props.currValue.name }}</span>
      <!-- <div class="icon">
        <el-icon><Operation /></el-icon>
        <span>{{ props.currValue.name }}</span>
      </div> -->
      <span class="close" @click="closeCurrDialog">
        <el-icon>
          <Minus />
        </el-icon>
      </span>
    </div>
    <div class="container-content">
      <template v-if="props.currValue.type == 'attrConfig'">
        <el-form ref="configRef" :model="form" :rules="rules" label-width="142px" @submit.prevent :disabled="readOnly">
          <div class="h2"><img class="icon" src="@/assets/da/asset/h2 (1).svg" alt="" />基础配置</div>
          <el-form-item label="任务优先级:" prop="taskPriority">
            <el-select v-model="form.taskPriority" placeholder="请选择任务优先级">
              <el-option v-for="(item, index) in dpp_etl_task_priority" :key="index" :label="item.label"
                :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="Worker分组:" prop="workerGroup">
            <el-input v-model="form.workerGroup" placeholder="请输入Worker分组" disabled />
          </el-form-item>
          <el-form-item label="失败重试次数:" prop="failRetryTimes">
            <el-input-number style="width: 85%; margin-right: 5px" controls-position="right" :min="0"
              v-model="form.failRetryTimes" placeholder="请输入失败重试次数"> </el-input-number>
            <span>次</span>
          </el-form-item>
          <el-form-item label="失败重试间隔:" prop="failRetryInterval">
            <el-input-number style="width: 85%; margin-right: 5px" controls-position="right" :min="0"
              v-model="form.failRetryInterval" placeholder="请输入失败重试间隔"> </el-input-number>
            <span>分</span>
          </el-form-item>
          <el-form-item label="延迟执行时间:" prop="delayTime">
            <el-input-number style="width: 85%; margin-right: 5px" controls-position="right"
              :min="isShowWithTypeName('DM,Oracle,MYSQL,Kingbase') ? 1 : 0" v-model="form.delayTime"
              placeholder="请输入延迟执行时间">
            </el-input-number>
            <span>分</span>
          </el-form-item>
          <template v-if="isShowWithTypeName('Flink批,Flink流')">
            <el-form-item label="JobManager内存数" prop="jobManagerMemory">
              <el-input v-model="form.jobManagerMemory" placeholder="请输入JobManager内存数"> </el-input>
            </el-form-item>
            <el-form-item label="TaskManager内存数" prop="taskManagerMemory">
              <el-input v-model="form.taskManagerMemory" placeholder="请输入TaskManager内存数"> </el-input>
            </el-form-item>
            <el-form-item label="Slot数量" prop="slot">
              <el-input-number placeholder="请输入Slot数量" v-model="form.slot" controls-position="right" :min="0" />
            </el-form-item>
            <el-form-item label="TaskManager数量" prop="taskManager">
              <el-input v-model="form.taskManager" placeholder="请输入TaskManager数量"> </el-input>
            </el-form-item>
            <el-form-item label="并行度" prop="parallelism">
              <el-input-number placeholder="请输入并行度" v-model="form.parallelism" controls-position="right" :min="0" />
            </el-form-item>
            <el-form-item label="Yarn队列" prop="yarnQueue">
              <el-input v-model="form.yarnQueue" placeholder="请输入Yarn队列(选填)"> </el-input>
            </el-form-item>
          </template>
          <template v-if="isShowWithTypeName('SparkSql')">
            <el-form-item label="Driver核心数" prop="driverCores">
              <el-input-number placeholder="请输入Driver核心数" v-model="form.driverCores" controls-position="right"
                :min="0" />
            </el-form-item>
            <el-form-item label="Driver内存数" prop="driverMemory">
              <el-input v-model="form.driverMemory" placeholder="请输入Driver内存数"> </el-input>
            </el-form-item>
            <el-form-item label="Executor数量" prop="numExecutors">
              <el-input-number placeholder="请输入Executor数量" v-model="form.numExecutors" controls-position="right"
                :min="0" />
            </el-form-item>
            <el-form-item label="Executor内存数" prop="executorMemory">
              <el-input v-model="form.executorMemory" placeholder="请输入Executor内存数"> </el-input>
            </el-form-item>
            <el-form-item label="Executor核心数" prop="executorCores">
              <el-input-number placeholder="请输入Executor核心数" v-model="form.executorCores" controls-position="right"
                :min="0" />
            </el-form-item>
            <el-form-item label="Yarn队列" prop="yarnQueue">
              <el-input v-model="form.yarnQueue" placeholder="请输入Yarn队列(选填)"> </el-input>
            </el-form-item>
          </template>
          <div class="h2"><img class="icon" src="@/assets/da/asset/h2 (1).svg" alt="" />其他配置</div>
          <el-form-item label="数据连接类型:" prop="typaCode"> {{ typaName }} </el-form-item>
          <el-form-item label="数据源连接:" prop="datasourceId" v-if="isShowWithTypeName('SparkSql,Flink批,Flink流', false)">
            <el-select v-model="form.datasourceId" placeholder="请选择数据源连接" @change="handleDatasourceChange" filterable>
              <el-option v-for="dict in createTypeList" :key="dict.id" :label="dict.datasourceName"
                :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="SQL类型:" prop="sqlType" v-if="isShowWithTypeName('SparkSql,Flink批,Flink流', false)">
            <el-radio-group v-model="form.sqlType" inline>
              <el-radio v-for="option in visibleRadioOptions" :key="option.id" :value="option.id">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="分段执行符号:" prop="segm">
            <el-input v-model="form.segm" placeholder="请输入分段执行符号"></el-input>
          </el-form-item>
          <div class="h2"><img class="icon" src="@/assets/da/asset/h2 (1).svg" alt="" />参数配置</div>
          <el-form-item label="自定义参数:" prop="localParams"> </el-form-item>
          <div class="wrap" v-for="(item, index) in form.localParams" :key="index">
            <el-input style="width: 30%" v-model="item.prop" placeholder="参数名称"></el-input>
            <el-select style="width: 40%; margin: 0 4px" v-model="item.type" placeholder="请选择参数类型">
              <el-option v-for="dict in columnType" :key="dict.value" :label="dict.label"
                :value="dict.value"></el-option>
            </el-select>
            <el-input style="width: 30%; margin-right: 4px" v-model="item.value" placeholder="值"></el-input>
            <div class="del-btn" @click="handleDelDiy(index)">
              <el-icon class="icon">
                <Delete />
              </el-icon>
            </div>
          </div>
          <div class="add-btn" @click="handleAddDiy">
            <el-icon class="icon">
              <Plus />
            </el-icon> 添加配置项
          </div>
        </el-form>
      </template>
    </div>
  </div>
</template>
<script setup name="EditorConfigView">
// 
import { treeData } from "@/views/dpp/task/developTask/data";
import { listDaDatasourceNoKafkaByProjectCode } from "@/api/da/dataSource/dataSource";
const { proxy } = getCurrentInstance();
const { dpp_etl_task_priority } = proxy.useDict("dpp_etl_task_priority");
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
const emits = defineEmits(["close"]);
const props = defineProps({
  currValue: {
    type: Object,
    default: () => {
      return {
        name: "",
        taskDefinitionList: [],
      };
    },
  },
  readOnly: {
    type: Boolean,
    default: false,
  },

});
const isShowWithTypeName = (row, boo = true) => {
  if (boo) {
    return row.includes(typaName.value);
  } else {
    return !row.includes(typaName.value);
  }
};
const columnType = ref([
  {
    value: "VARCHAR",
    label: "VARCHAR",
  },
  {
    value: "INTEGER",
    label: "INTEGER",
  },
  {
    value: "LONG",
    label: "LONG",
  },
  {
    value: "FLOAT",
    label: "FLOAT",
  },
  {
    value: "DOUBLE",
    label: "DOUBLE",
  },
  {
    value: "DATE",
    label: "DATE",
  },
  {
    value: "TIME",
    label: "TIME",
  },
  {
    value: "TIMESTAMP",
    label: "TIMESTAMP",
  },
  {
    value: "BOOLEAN",
    label: "BOOLEAN",
  },
]);

const typaName = computed(() => {
  let typeNmae = treeData.find((item) => item.value == form.value.typaCode)?.label;
  return typeNmae;
});
const configRef = ref();
const createTypeList = ref([]);
function getDaDatasource() {
  listDaDatasourceNoKafkaByProjectCode({
    projectCode: userStore.projectCode,
    projectId: userStore.projectId,
    datasourceType: form.value.typaCode,
  }).then((response) => {
    createTypeList.value = response.data;
  });
}
const handleDatasourceChange = async (value) => {
  const selectedDatasource = createTypeList.value.find((item) => item.id == value);
  let { datasourceType, datasourceConfig, ip, port, id } = selectedDatasource;
  let code = JSON.parse(datasourceConfig);
  form.value.datasources = {
    datasourceType,
    datasourceConfig,
    ip,
    port,
    dbname: code.dbname,
    datasource_id: id,
    datasourceId: id,
  };
};

const radioOptions = ref([
  { componentType: "51", label: "查询", taskType: "SQL", id: "0", show: true },
  {
    componentType: "51",
    label: "非查询",
    taskType: "SQL",
    id: "1",
    show: true,
  },
  {
    componentType: "52",
    label: "储存过程",
    taskType: "PROCEDURE",
    id: "2",
    show: true,
  },
  {
    componentType: "53",
    label: "SparkSql开发",
    taskType: "SPARK",
    id: "4",
    show: false,
  },
  {
    componentType: "55",
    label: "FlinkSql开发",
    taskType: "FLINK",
    id: "5",
    show: false,
  },
]);

const visibleRadioOptions = computed(() => radioOptions.value.filter((option) => option.show));
const closeCurrDialog = () => {
  emits("close");
};

// #region curr弹框拖拽
const currWidth = ref(340); // 初始左侧宽度
const isCurrResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置
const resizeCurrDialog = (event) => {
  isCurrResizing.value = true;
  startX = event.clientX;
  // 使用 requestAnimationFrame 减少重绘频率
  document.addEventListener("mousemove", updateCurrResize);
  document.addEventListener("mouseup", stopCurrResize);
};
const updateCurrResize = (event) => {
  if (isCurrResizing.value) {
    const delta = startX - event.clientX; // 计算鼠标移动距离
    currWidth.value += delta; // 修改左侧宽度
    startX = event.clientX; // 更新起始位置
    if (currWidth.value > 650) {
      currWidth.value = 650;
      return;
    } else if (currWidth.value < 300) {
      currWidth.value = 300;
      return;
    }
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => { });
  }
};
const stopCurrResize = () => {
  isCurrResizing.value = false;
  document.removeEventListener("mousemove", resizeCurrDialog);
  document.removeEventListener("mouseup", stopCurrResize);
};
// #endregion

const data = reactive({
  form: {
    // 基础配置
    taskPriority: "MEDIUM",
    workerGroup: "default",
    failRetryTimes: "0",
    failRetryInterval: "1",
    delayTime: "0",
    // Fink配置
    jobManagerMemory: "1G",
    taskManagerMemory: "2G",
    slot: 1,
    taskManager: 2,
    parallelism: 1,
    yarnQueue: "",
    // Spark配置
    driverCores: 1,
    driverMemory: "512M",
    numExecutors: 1,
    executorMemory: "1G",
    executorCores: 1,
    // 其他配置
    typaCode: "",
    datasourceId: "",
    sqlType: "0",
    taskType: "",
    segm: "",
    componentType: "",
    // 参数配置
    localParams: [],
  },
  rules: {
    taskPriority: [{ required: true, message: "任务优先级不能为空", trigger: "change" }],
    workerGroup: [{ required: true, message: "Worker分组不能为空", trigger: "change" }],
    datasourceId: [{ required: true, message: "数据源连接不能为空", trigger: "change" }],
    sqlType: [{ required: true, message: "SQL类型不能为空", trigger: "change" }],
  },
});
const { form, rules } = toRefs(data);
watch(
  () => props.currValue,
  (val) => {
    if (val.type == "attrConfig") {
      let taskParams = val.data.taskDefinitionList.length > 0 && val.data.taskDefinitionList[0].taskParams;
      form.value = {
        ...form.value,
        ...taskParams,
        typaCode: val.data.draftJson ? JSON.parse(val.data.draftJson).typaCode : "",
      };
      // 执行时间默认为1分钟
      if (isShowWithTypeName("DM,Oracle,MYSQL,Kingbase") && form.value.delayTime == 0) {
        form.value.delayTime = 1;
      }
      // Flink特殊字段
      form.value.executeMode = form.value.typaCode == "FlinkBatch" ? "BATCH" : form.value.typaCode == "FlinkStream" ? "STREAM" : "";
      let obj;
      if (form.value.typaCode == "SparkSql") {
        obj = radioOptions.value?.find((option) => option.id == 4);
      } else if (form.value.typaCode == "FlinkBatch" || form.value.typaCode == "FlinkStream") {
        obj = radioOptions.value?.find((option) => option.id == 5);
      } else {
        obj = radioOptions.value?.find((option) => option.id == form.value.sqlType);
      }
      form.value.taskType = obj?.taskType;
      form.value.componentType = obj?.componentType;
      // 获取： 数据源连接
      getDaDatasource();
      console.log("🚀 ~ form.value:", form.value);
    }
  },
  {
    immediate: true,
  }
);

const handleAddDiy = () => {
  form.value.localParams.push({
    prop: "",
    type: "",
    value: "",
    direct: "IN",
  });
};
const handleDelDiy = (index) => {
  form.value.localParams.splice(index, 1);
};
defineExpose({ currWidth, form, configRef });
</script>
<style lang="scss" scoped>
.configView {
  //   position: absolute;
  //   bottom: 15px;
  position: relative;
  user-select: auto;
  height: 100%;
  margin-top: 0px;
  border-radius: 5px;
  background-color: #fbfbfb;
  // max-width: 650px;
  // min-width: 200px;
  box-sizing: border-box;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-left: none;
  border-top: none;

  .move {
    position: absolute;
    user-select: none;
    width: 10px;
    height: 100%;
    top: 0px;
    left: -5px;
    cursor: col-resize;
  }

  .container-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 40px;
    padding: 0 15px;
    padding-block: 1px;
    border-bottom: 1px solid rgb(147 147 147 / 6%);
    background-color: #f9f9f9;

    .title {
      overflow: visible;
      font-weight: 500;
      font-size: 16px;
      font-family: xPingFang SC;
      white-space: nowrap;
      text-overflow: ellipsis;
      color: #333;
      display: flex;
      align-items: center;

      &::before {
        display: inline-block;
        content: "";
        width: 6px;
        height: 16px;
        border-radius: 3px;
        background: var(--el-color-primary);
        margin-right: 8px;
      }
    }

    .close {
      cursor: pointer;
      display: inline-flex;
      justify-content: center;
      align-items: center;
      width: 32px;
      height: 32px;
      border-radius: 50%;
      font-size: 16px;
      color: var(--el-color-primary);

      &:hover {
        background-color: rgb(0, 0, 0, 0.06);
      }
    }

    .icon {
      width: 110px;
      height: 32px;
      display: flex;
      justify-content: center;
      align-items: center;
      background: #eaf0ff;
      border-radius: 2px;
      border: 1px solid var(--el-color-primary);
      color: var(--el-color-primary);
      font-size: 14px;
    }
  }

  .container-content {
    height: calc(100% - 40px);
    overflow: hidden auto;
    padding: 15px;
    background-color: #F9F9F9;

    &::-webkit-scrollbar {
      width: 4px;
    }

    .h2 {
      height: 36px;
      padding: 0 15px;
      background: rgba(51, 103, 252, 0.06);
      border-radius: 2px;
      display: flex;
      align-items: center;
      font-weight: 500;
      font-size: 14px;
      font-family: PingFang SC;
      margin-bottom: 15px;
      color: var(--el-color-primary);

      // span {
      //   display: inline-block;
      //   width: 20px;
      //   height: 20px;
      //   line-height: 18px;
      //   border-radius: 50%;
      //   background-color: var(--el-color-primary);
      //   text-align: center;
      //   font-size: 14px;
      //   margin-right: 5px;
      //   color: #fff;
      // }
      .icon {
        display: inline-block;
        width: 20px;
        margin-right: 5px;
        // height: 20px;
        // background: url("@/assets/da/asset/h2 (1).svg") no-repeat;
      }
    }

    :deep(.el-form-item) {
      margin-bottom: 10px;

      .el-form-item__label {
        color: rgba(0, 0, 0, 0.65);
      }
    }

    .add-btn {
      cursor: pointer;
      width: 100%;
      height: 34px;
      border-radius: 4px;
      display: flex;
      justify-content: center;
      align-items: center;
      border: 1px dashed #dcdfe6;
      font-size: 14px;
      color: rgba(0, 0, 0, 0.65);

      .icon {
        margin-right: 5px;
      }
    }

    .del-btn {
      cursor: pointer;

      .icon {
        color: #f00;
      }
    }

    .wrap {
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 10px;
    }
  }
}
</style>
