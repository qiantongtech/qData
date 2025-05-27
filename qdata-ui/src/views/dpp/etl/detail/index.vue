<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ dppEtlTaskDetail.id }}
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">ID</div>
              <div class="infotop-row-value">{{ dppEtlTaskDetail.id }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">任务类型</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.type || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">任务名称</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.name || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">任务编码</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.code || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">任务版本</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.version || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">项目id</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.projectId || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">项目编码</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.projectCode || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">责任人</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.personCharge || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">节点坐标信息</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.locations || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.description || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">超时时间</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.timeout || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">抽取量</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.extractionCount || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">写入量</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.writeCount || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">任务状态</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.status || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">DolphinScheduler的id</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.dsId || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建人</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.createBy || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建时间</div>
              <div class="infotop-row-value">{{ parseTime(dppEtlTaskDetail.createTime, '{y}-{m}-{d}') }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">备注</div>
              <div class="infotop-row-value">
                {{ dppEtlTaskDetail.remark || '-' }}
              </div>
            </div>
          </el-col>
        </el-row>

      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="组件一" name="1">
          <component-one></component-one>
        </el-tab-pane>
        <el-tab-pane label="组件二" name="2">
          <component-two></component-two>
        </el-tab-pane>
      </el-tabs>
    </div>


  </div>
</template>

<script setup name="DppEtlTask">
import { getDppEtlTask } from "@/api/dpp/etl/dppEtlTask";
import { useRoute } from 'vue-router';
import ComponentOne from "@/views/dpp/etl/detail/componentOne.vue";
import ComponentTwo from "@/views/dpp/etl/detail/componentTwo.vue";

const { proxy } = getCurrentInstance();

const activeName = ref('1')

const handleClick = (tab, event) => {
  console.log(tab, event)
}

const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;  // 如果 id 为空，使用默认值 1
    getDppEtlTaskDetailById();

  },
  { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  dppEtlTaskDetail: {
  },
  form: {},
});

const { dppEtlTaskDetail, rules } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getDppEtlTaskDetailById() {
  const _id = id;
  getDppEtlTask(_id).then(response => {
    dppEtlTaskDetail.value = response.data;
  });
}

getDppEtlTaskDetailById();

</script>
