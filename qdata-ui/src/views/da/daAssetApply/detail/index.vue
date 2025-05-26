<!-- 复杂详情路由模板
    {
        path: '/da/daAssetApply',
        component: Layout,
        redirect: 'daAssetApply',
        hidden: true,
        children: [
            {
                path: 'daAssetApplyDetail',
                component: () => import('@/views/da/daAssetApply/detail/index.vue'),
                name: 'tree',
                meta: { title: '数据资产申请详情', activeMenu: '/da/daAssetApply'  }
            }
        ]
    }
 -->



<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop" >
        <div class="infotop-title mb15">
              {{ daAssetApplyDetail.id }}
        </div>
        <el-row :gutter="20">
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">ID</div>
                <div class="infotop-row-value">{{ daAssetApplyDetail.id }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">资产id</div>
                <div class="infotop-row-value">
                  {{ daAssetApplyDetail.assetId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">项目id</div>
                <div class="infotop-row-value">
                  {{ daAssetApplyDetail.projectId || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">项目编码</div>
                <div class="infotop-row-value">
                  {{ daAssetApplyDetail.projectCode || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">申请理由</div>
                <div class="infotop-row-value">
                  {{ daAssetApplyDetail.applyReason || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">审批理由</div>
                <div class="infotop-row-value">
                  {{ daAssetApplyDetail.approvalReason || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">状态</div>
                <div class="infotop-row-value">
                  {{ daAssetApplyDetail.status || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建人</div>
                <div class="infotop-row-value">
                  {{ daAssetApplyDetail.createBy || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">创建时间</div>
                <div class="infotop-row-value">{{ parseTime(daAssetApplyDetail.createTime, '{y}-{m}-{d}') }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">备注</div>
                <div class="infotop-row-value">
                  {{ daAssetApplyDetail.remark || '-' }}
                </div>
              </div>
            </el-col>
        </el-row>

      </div>
    </div>

    <div  class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="组件一" name="1">
          <component-one ></component-one>
        </el-tab-pane>
        <el-tab-pane label="组件二" name="2">
          <component-two ></component-two>
        </el-tab-pane>
      </el-tabs>
    </div>


  </div>
</template>

<script setup name="DaAssetApply">
  import {getDaAssetApply } from "@/api/da/daAssetApply/daAssetApply";
  import { useRoute } from 'vue-router';
  import ComponentOne from "@/views/da/daAssetApply/detail/componentOne.vue";
  import ComponentTwo from "@/views/da/daAssetApply/detail/componentTwo.vue";

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
            getDaAssetApplyDetailById();

          },
          { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
  );
  const data = reactive({
      daAssetApplyDetail: {
    },
    form: {},
  });

  const {  daAssetApplyDetail, rules } = toRefs(data);

  /** 复杂详情页面上方表单查询 */
  function getDaAssetApplyDetailById() {
        const _id = id ;
    getDaAssetApply(_id).then(response => {
        daAssetApplyDetail.value = response.data;
    });
  }

  getDaAssetApplyDetailById();

</script>
