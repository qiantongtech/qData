<template>
  <div class="c-right">
    <div class="box-wrap right1">
      <div class="box-title">
        <div class="box-name">数据服务共享情况</div>
      </div>
      <div class="box-con">
        <div class="right-item" v-for="(item, index) in rightData1" :key="index">
          <div class="right-val">{{ item.val }}</div>
          <div class="right-name">{{ item.name }}</div>
        </div>
      </div>
    </div>
    <div class="box-wrap right2">
      <div class="box-title">
        <div class="box-name">视频监视情况<span>(16742路)</span></div>
        <!-- <el-select class="box-select" v-model="act" placeholder="请选择">
          <el-option v-for="item in videoData" :key="item.value" :label="item.label" :value="item.value" />
        </el-select> -->
      </div>
      <div class="box-con">
        <div class="r-tabs">
          <div :class="['tab', { act: tabAct == item.value }]" @click="tabAct = item.value"
            v-for="(item, index) in tabsData" :key="index">{{ item.label }}</div>
        </div>
        <div class="r-table">
          <div class="r-thead">
            <div class="tr">
              <div :class="['td']" :style="{ width: item.width }" v-for="(item, index) in thead" :key="index">{{
                item.label }}</div>
            </div>
          </div>
          <div class="r-tbody">
            <div class="tr" v-for="(item, index) in tdata" :key="index">
              <div class="td ellipsis" :style="{ width: key.width }" v-for="(key, keyIndex) in thead"
                :key="'tbody' + keyIndex" :title="item[key.value]">
                <span :class="['dot', { success: item[key.value] == '在线', error: item[key.value] == '离线' }]"
                  v-if="key.value == 'isLine'"></span>
                <span
                  :class="['odds', { warn: item[key.value] >= 20 && item[key.value] <= 50, error: item[key.value] < 20 }]"
                  v-if="key.value == 'odds'">{{ item[key.value] }}%</span>
                <span v-else>{{ item[key.value] }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="CockpitRight">
const rightData1 = ref([
  {
    name: "数据服务数量（个）",
    val: "28",
  },
  {
    name: "数据服务调用统计（万次）",
    val: "16",
  },
  {
    name: "数据申请统计（次）",
    val: "96",
  },
  {
    name: "数据审批统计（次）",
    val: "66",
  },
]);
const act = ref(0);
const videoData = ref([
  {
    label: "按地州",
    value: 0,
    children: [
      {
        label: "水库",
        value: 0,
        children: [
          {
            label: "水库",
            value: "val0",
            width: "30%",
          },
          {
            label: "摄像头点位名称",
            value: "val1",
            width: "40%",
          },
          {
            label: "是否在线",
            value: "isLine",
            width: "30%",
          },
        ],
        data: [
          {
            val0: "阿尔塔什水库",
            val1: "坝顶监控",
            isLine: "在线",
          },
          {
            val0: "克孜尔水库",
            val1: "全景监控",
            isLine: "在线",
          },
          {
            val0: "小海子水库",
            val1: "水尺监控",
            isLine: "在线",
          },
          {
            val0: "塔尔朗水库",
            val1: "泄洪口监控",
            isLine: "在线",
          },
          {
            val0: "乌恰水库",
            val1: "全景监控",
            isLine: "在线",
          },
        ],
      },
      {
        label: "农村供水工程",
        value: 1,
        children: [
          {
            label: "单位名称",
            value: "val0",
            width: "35%",
          },
          {
            label: "农村供水工程数量",
            value: "val1",
            width: "20%",
          },
          {
            label: "视频监视点数量",
            value: "val2",
            width: "25%",
          },
          {
            label: "在线视频监视点数量",
            value: "val3",
            width: "20%",
          },
          {
            label: "在线率",
            value: "odds",
            width: "20%",
          },
        ],
        data: [
          {
            val0: "塔里木河流域管理局",
            val1: "12",
            val2: "4",
            val3: "2",
            odds: Math.round((2 / 4) * 100),
          },
          {
            val0: "玛纳斯河流域管理局",
            val1: "33",
            val2: "18",
            val3: "15",
            odds: Math.round((15 / 18) * 100),
          },
          {
            val0: "金沟河流域管理局",
            val1: "21",
            val2: "12",
            val3: "8",
            odds: Math.round((8 / 12) * 100),
          },
          {
            val0: "头屯河流域管理局",
            val1: "62",
            val2: "62",
            val3: "28",
            odds: Math.round((28 / 62) * 100),
          },
          {
            val0: "喀什噶尔河流域管理局",
            val1: "22",
            val2: "21",
            val3: "17",
            odds: Math.round((17 / 21) * 100),
          },
        ],
      },
      {
        label: "小水电站",
        value: 2,
        children: [
          {
            label: "单位名称",
            value: "val0",
            width: "25%",
          },
          {
            label: "小水电站数量",
            value: "val1",
            width: "15%",
          },
          {
            label: "视频监视点数量",
            value: "val2",
            width: "20%",
          },
          {
            label: "在线视频监视点数量",
            value: "val3",
            width: "25%",
          },
          {
            label: "在线率",
            value: "odds",
            width: "15%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "12",
            val2: "19",
            val3: "18",
            odds: Math.round((18 / 19) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "15",
            val2: "87",
            val3: "85",
            odds: Math.round((85 / 87) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "16",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "12",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "16",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "12",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
    ],
  },
  {
    label: "按流域",
    value: 1,
    children: [
      {
        label: "水库名称",
        value: 0,
        children: [
          {
            label: "水库名称",
            value: "val0",
            width: "30%",
          },
          {
            label: "摄像头点位名称",
            value: "val1",
            width: "40%",
          },
          {
            label: "是否在线",
            value: "isLine",
            width: "30%",
          },
        ],
        data: [
          {
            val0: "小海子水库",
            val1: "水尺监控",
            isLine: "在线",
          },
          {
            val0: "塔尔朗水库",
            val1: "泄洪口监控",
            isLine: "在线",
          },
          {
            val0: "阿尔塔什水库",
            val1: "坝顶监控",
            isLine: "在线",
          },
          {
            val0: "克孜尔水库",
            val1: "全景监控",
            isLine: "离线",
          },
          {
            val0: "乌恰水库",
            val1: "全景监控",
            isLine: "在线",
          },
        ],
      },
      {
        label: "点位",
        value: 1,
        children: [
          {
            label: "水库名称",
            value: "val0",
            width: "35%",
          },
          {
            label: "点位数量",
            value: "val1",
            width: "20%",
          },
          {
            label: "在线点位数量",
            value: "val2",
            width: "25%",
          },
          {
            label: "点位在线率",
            value: "odds",
            width: "20%",
          },
        ],
        data: [
          {
            val0: "阿尔塔什水库",
            val1: "12",
            val2: "12",
            odds: Math.round((12 / 12) * 100),
          },
          {
            val0: "克孜尔水库",
            val1: "33",
            val2: "11",
            odds: Math.round((11 / 33) * 100),
          },
          {
            val0: "小海子水库",
            val1: "21",
            val2: "12",
            odds: Math.round((12 / 21) * 100),
          },
          {
            val0: "塔尔朗水库",
            val1: "62",
            val2: "62",
            odds: Math.round((62 / 62) * 100),
          },
          {
            val0: "乌恰水库",
            val1: "22",
            val2: "21",
            odds: Math.round((21 / 22) * 100),
          },
        ],
      },
      {
        label: "水库数量",
        value: 2,
        children: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "15%",
          },
          {
            label: "水库数量",
            value: "val2",
            width: "20%",
          },
          {
            label: "实际到报数量",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "15%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "12",
            val2: "19",
            val3: "18",
            odds: Math.round((18 / 19) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "15",
            val2: "87",
            val3: "85",
            odds: Math.round((85 / 87) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "16",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "12",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "16",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "12",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
    ],
  },
  {
    label: "按类别",
    value: 2,
    children: [
      {
        label: "水库名称",
        value: 0,
        children: [
          {
            label: "水库名称",
            value: "val0",
            width: "30%",
          },
          {
            label: "摄像头点位名称",
            value: "val1",
            width: "40%",
          },
          {
            label: "是否在线",
            value: "isLine",
            width: "30%",
          },
        ],
        data: [
          {
            val0: "乌恰水库",
            val1: "全景监控",
            isLine: "离线",
          },
          {
            val0: "阿尔塔什水库",
            val1: "坝顶监控",
            isLine: "在线",
          },
          {
            val0: "克孜尔水库",
            val1: "全景监控",
            isLine: "在线",
          },
          {
            val0: "小海子水库",
            val1: "水尺监控",
            isLine: "在线",
          },
          {
            val0: "塔尔朗水库",
            val1: "泄洪口监控",
            isLine: "在线",
          },
        ],
      },
      {
        label: "点位",
        value: 1,
        children: [
          {
            label: "水库名称",
            value: "val0",
            width: "35%",
          },
          {
            label: "点位数量",
            value: "val1",
            width: "20%",
          },
          {
            label: "在线点位数量",
            value: "val2",
            width: "25%",
          },
          {
            label: "点位在线率",
            value: "odds",
            width: "20%",
          },
        ],
        data: [
          {
            val0: "阿尔塔什水库",
            val1: "12",
            val2: "12",
            odds: Math.round((12 / 12) * 100),
          },
          {
            val0: "克孜尔水库",
            val1: "33",
            val2: "11",
            odds: Math.round((11 / 33) * 100),
          },
          {
            val0: "小海子水库",
            val1: "21",
            val2: "12",
            odds: Math.round((12 / 21) * 100),
          },
          {
            val0: "塔尔朗水库",
            val1: "62",
            val2: "62",
            odds: Math.round((62 / 62) * 100),
          },
          {
            val0: "乌恰水库",
            val1: "22",
            val2: "21",
            odds: Math.round((21 / 22) * 100),
          },
        ],
      },
      {
        label: "水库数量",
        value: 2,
        children: [
          {
            label: "地州",
            value: "val0",
            width: "25%",
          },
          {
            label: "备案数",
            value: "val1",
            width: "15%",
          },
          {
            label: "水库数量",
            value: "val2",
            width: "20%",
          },
          {
            label: "实际到报数量",
            value: "val3",
            width: "25%",
          },
          {
            label: "到报率",
            value: "odds",
            width: "15%",
          },
        ],
        data: [
          {
            val0: "和田地区",
            val1: "12",
            val2: "19",
            val3: "18",
            odds: Math.round((18 / 19) * 100),
          },
          {
            val0: "乌鲁木齐市",
            val1: "15",
            val2: "87",
            val3: "85",
            odds: Math.round((85 / 87) * 100),
          },
          {
            val0: "克拉玛依市",
            val1: "16",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "吐鲁番地区",
            val1: "12",
            val2: "31",
            val3: "31",
            odds: Math.round((31 / 31) * 100),
          },
          {
            val0: "塔城地区",
            val1: "16",
            val2: "38",
            val3: "30",
            odds: Math.round((30 / 38) * 100),
          },
          {
            val0: "哈密市",
            val1: "12",
            val2: "41",
            val3: "32",
            odds: Math.round((32 / 41) * 100),
          },
        ],
      },
    ],
  },
]);
const tabAct = ref(0);
const tabsData = computed(() => {
  return videoData.value[act.value].children;
});
const thead = computed(() => {
  return tabsData.value[tabAct.value].children;
});
const tdata = computed(() => {
  return tabsData.value[tabAct.value].data;
});
</script>

<style scoped lang="scss">
.c-right {
  .right1 {
    width: 502px;
    height: 205px;
    background: url("@/assets/cockpit/box (3).png") no-repeat;
    background-size: 100% 100%;
    margin-bottom: 10px;

    .box-con {
      display: flex;
      justify-content: space-between;
      padding: 2px 32px 0;

      .right-item {
        width: 103px;
        height: 95px;

        .right-val {
          width: 103px;
          height: 90px;
          background: url("@/assets/cockpit/right1.png") no-repeat;
          background-size: cover;

          text-align: center;
          font-family: HYk2gj;
          font-size: 30px;
          color: #ffffff;
          line-height: 55px;
          letter-spacing: 1px;
          text-shadow: 2px 0px 1px #000000, 0px 0px 20px #fff;
        }

        .right-name {
          width: 103px;
          text-align: center;
          font-family: HYk2gj;
          font-size: 16px;
          color: #ffffff;
          line-height: 18px;
          text-shadow: 2px 0px 8px #051c37;
          word-wrap: break-word;
          white-space: normal;
        }
      }
    }
  }

  .right2 {
    width: 502px;
    height: 433px;
    background: url("@/assets/cockpit/box (4).png") no-repeat;
    background-size: 100% 100%;

    .box-name {
      span {
        margin-left: 10px;
        color: #00eeff;
      }
    }

    :deep(.box-select) {
      margin-top: 8px;
      width: 103px;
      height: 32px;

      .el-select__wrapper {
        background: #094b92;
        border: 1px solid #3a86cb;
        box-shadow: none;

        .el-select__placeholder {
          font-family: PingFangSC;
          font-weight: 500;
          font-size: 14px;
          color: #ffffff;
        }

        .el-select__icon {
          font-size: 20px;
          background: url("@/assets/cockpit/sel-icon.png") no-repeat;
          background-size: 100% 100%;

          svg {
            display: none;
          }
        }
      }
    }

    .box-con {
      padding: 16px 24px 0;
    }

    .r-tabs {
      width: 429px;
      height: 44px;
      padding: 0 8px;
      margin: 0 auto 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: url("@/assets/cockpit/tabs.png") no-repeat;
      background-size: 100% 100%;

      .tab {
        user-select: none;
        cursor: pointer;
        padding: 0 20px;
        min-width: 130px;
        height: 37px;
        line-height: 37px;
        text-align: center;
        font-family: PingFangSC;
        font-weight: 500;
        font-size: 14px;
        color: #fdffff;
        text-shadow: 4px 0px 6px #02293e;
        letter-spacing: 1px;

        &.act {
          background: url("@/assets/cockpit/tab.png") no-repeat;
          background-size: 100% 100%;
        }
      }
    }

    .r-table {
      width: 448px;

      .tr {
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 45px;
        padding: 0 5px 0 10px;

        .td {
          padding: 0 6px;
          text-align: center;
        }
      }

      .r-thead {
        width: 450px;
        background: url("@/assets/cockpit/thead.png") no-repeat;
        background-size: 100% 100%;
        margin-left: -5px;
        margin-bottom: 5px;

        .td {
          font-family: sharp;
          font-weight: bold;
          font-size: 14px;
          color: #ffffff;
          text-shadow: 2px 0px 8px #051c37;
        }
      }

      .r-tbody {
        height: 210px;
        overflow: hidden auto;

        &::-webkit-scrollbar {
          width: 0px;
        }

        .tr {
          width: 444px;
          height: 34px;
          background: url("@/assets/cockpit/tbody.png") no-repeat;
          background-size: 100% 100%;
          margin-bottom: 10px;
          padding: 0 0;

          &:last-child {
            margin-bottom: 0;
          }

          .td {
            font-family: PingFangSC;
            font-weight: 400;
            font-size: 14px;
            color: #cae5ff;
          }
        }
      }

      .dot {
        display: inline-block;
        border-radius: 50%;
        width: 6px;
        height: 6px;
        margin-right: 4px;

        &.success {
          background: #2af267;
        }

        &.error {
          background: #ff2626;
        }

        &.warn {
          background: #e7c125;
        }
      }

      .odds {
        &.success {
          color: #2af267;
        }

        &.error {
          color: #ff2626;
        }

        &.warn {
          color: #e7c125;
        }
      }
    }
  }
}
</style>
