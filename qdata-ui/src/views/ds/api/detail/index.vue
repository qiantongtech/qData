<template>
  <div class="app-container" ref="app-container" v-if="dsApiDetail">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ dsApiDetail.name }}
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">编号</div>
              <div class="infotop-row-value">{{ dsApiDetail.id }}</div>
            </div>
          </el-col>
          <el-col :span="8">
              <div class="infotop-row border-top">
                  <div class="infotop-row-lable">所属类目</div>
                  <div class="infotop-row-value">
                      {{ dsApiDetail.catName || '-' }}
                  </div>
              </div>
          </el-col>
          <el-col :span="8">
              <div class="infotop-row border-top">
                  <div class="infotop-row-lable">状态</div>
                  <div class="infotop-row-value">
                      <dict-tag :options="sys_disable" :value="dsApiDetail.status" />
                  </div>
              </div>
          </el-col>
          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">API版本</div>
              <div class="infotop-row-value">
                {{ dsApiDetail.apiVersion || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">请求方式</div>
              <div class="infotop-row-value">
                <dict-tag :options="ds_api_bas_info_api_method_type" :value="dsApiDetail.reqMethod" />
              </div>
            </div>
          </el-col>
          <el-col :span="8" >
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">返回结果类型</div>
              <div class="infotop-row-value">
                <dict-tag :options="ds_api_bas_info_res_data_type" :value="dsApiDetail.resDataType" />
              </div>
            </div>
          </el-col>
          <el-col :span="24" >
              <div class="infotop-row border-top">
                  <div class="infotop-row-lable">描述</div>
                  <div class="infotop-row-value">
                     <span class="ellipsis-2">
                         {{ dsApiDetail.description || '-' }}
                     </span>
                  </div>
              </div>
          </el-col>
          <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                  <div class="infotop-row-lable">创建人</div>
                  <div class="infotop-row-value">{{ dsApiDetail.createBy || '-' }}</div>
              </div>
          </el-col>
          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建时间</div>
              <div class="infotop-row-value">{{ parseTime(dsApiDetail.createTime, '{y}-{m}-{d} {h}:{i}') }}</div>
            </div>
          </el-col>

          <el-col :span="8" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">API请求地址</div>
              <div class="infotop-row-value">
                {{ '/services/' + dsApiDetail.apiVersion + dsApiDetail.apiUrl || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="24" >
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">备注</div>
              <div class="infotop-row-value">
                {{ dsApiDetail.remark || '-' }}
              </div>
            </div>
          </el-col>
        </el-row>

      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="参数信息" name="1">
          <component-one :form2="form2" v-if="activeName === '1'"></component-one>
        </el-tab-pane>
        <el-tab-pane label="测试信息" name="2">
          <component-two :form1="form1" v-if="activeName === '2'"></component-two>
        </el-tab-pane>
        <!--        <el-tab-pane label="授权信息" name="2">-->
        <!--          <component-two ></component-two>-->
        <!--        </el-tab-pane>-->
      </el-tabs>
    </div>


  </div>
</template>

<script setup name="DsApi">
import { getDsApi } from "@/api/ds/api/api.js";
import { onBeforeRouteLeave, useRoute } from 'vue-router';
import ComponentOne from "@/views/ds/api/detail/parameter.vue";
import ComponentTwo from "@/views/ds/api/detail/simulation.vue";



const { proxy } = getCurrentInstance();
const { ds_api_log_status, ds_api_bas_info_api_service_type,
    ds_api_bas_info_api_method_type, ds_api_bas_info_res_data_type,sys_disable }
    = proxy.useDict('ds_api_log_status', 'ds_api_bas_info_api_service_type',
    'ds_api_bas_info_api_method_type', 'ds_api_bas_info_res_data_type','sys_disable');

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
    getDsApiDetailById();

  },
  { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  dsApiDetail: {
  },
  form: {},
  form1: {

  },
  form2: {},
});

const { dsApiDetail, rules, form1, form2 } = toRefs(data);
function safeParse(str, defaultVal) {
  if (!str) return defaultVal;
  try {
    return JSON.parse(str);
  } catch (e) {
    console.warn('JSON 解析失败，返回原值：', str, e);
    return str;  // 返回原始字符串而不是 defaultVal
  }
}
/** 复杂详情页面上方表单查询 */
function getDsApiDetailById() {
  const _ID = id;
  getDsApi(_ID).then(response => {
    if (response.data.apiServiceType == 3) {
      dsApiDetail.value = response.data;
      form2.value = response.data;
      form2.value.resParams = safeParse(response.data.resParams, []);
      form2.value.reqParams = safeParse(response.data.reqParams, []);
      form2.value.headerJson = safeParse(response.data.headerJson, []);
      form1.value = response.data;
      form1.value.apiId = response.data?.apiId;
      form1.value.transmitType = response.data?.transmitType;
      form1.value.executeConfig = safeParse(response.data.configJson, {});
      form1.value.reqParams = safeParse(response.data.reqParams, []);
      form1.value.resParams = safeParse(response.data.resParams, []);
      form1.value.headerJson = safeParse(response.data.headerJson, []);
    } else {
      dsApiDetail.value = response.data;
      form2.value.resParams = JSON.parse(response.data.resParams)
      form2.value.reqParams = JSON.parse(response.data.reqParams)
      console.log('123123', dsApiDetail.value)
      form1.value = response.data;
      form1.value.rateLimit = { enable: "1", times: 5, seconds: 60 }
      form1.value.executeConfig = JSON.parse(response.data.configJson);
      form1.value.reqParams = JSON.parse(response.data.reqParams);
      form1.value.resParams = JSON.parse(response.data.resParams);
    }

  });

}

getDsApiDetailById();

onBeforeRouteLeave((to, from) => {
  // 监听路由变化，如果路由变化，销毁当前页面
  if (to.path !== from.path) {
    console.log('路由变化，销毁当前页面')
    form2.value = {}
    form1.value = {}
  }
});

</script>
