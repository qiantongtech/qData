<template>
  <div class="c-center">
    <div class="nums">
      <div class="num-item" v-for="(item, index) in numData" :key="index">
        <div class="num-name">{{ item.name }}</div>
        <div class="num-val">{{ item.val }}</div>
      </div>
    </div>
    <div class="center">
      <div class="title">自治区水利厅</div>
      <div :class="['center-item', { error: item.status == 0 }]" :style="{ top: item.top, left: item.left, right: item.right }" v-for="(item, index) in centerData" :key="index">
        <div class="center-name ellipsis" :title="item.label">{{ item.label }}</div>
      </div>
    </div>
    <div class="center-select">
      <div :class="['btn', { act: btnAct == index }]" @click="btnAct = index" v-for="(item, index) in btns" :key="index">{{ item.label }}</div>
    </div>
    <div class="error-list" v-if="errorData.length > 0">
      <el-carousel height="34px" direction="vertical">
        <el-carousel-item v-for="(item, index) in errorData" :key="index" indicator-position="outside">
          <div class="error-item">
            <img src="@/assets/cockpit/center-icon.png" alt="" />
            <span class="ellipsis" :title="`${item.label}数据断开连接`">{{ item.label }}数据断开连接</span>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script setup name="CockpitCenter">
import moment from "moment";

const numData = ref([
  {
    name: "监测数据总条数",
    val: "271413",
  },
  {
    name: moment().format("YYYY年") + "总条数",
    val: "123121",
  },
  {
    name: moment().format("MM月") + "总条数",
    val: "41413",
  },
  {
    name: moment().format("DD日") + "总条数",
    val: "3121",
  },
]);
const btnAct = ref(0);
const btns = ref([
  {
    label: "直属单位",
    data: [
      {
        label: "伊犁哈萨克自治州塔城地区水利局",
        status: 1,
        top: "-10px",
        right: "520px",
      },
      {
        label: "伊犁哈萨克自治州阿勒泰地区水利局",
        status: 1,
        top: "-10px",
        left: "530px",
      },
      {
        label: "克孜勒苏柯尔克孜自治州水利局",
        status: 1,
        top: "50px",
        right: "590px",
      },
      {
        label: "伊犁哈萨克自治州水利局",
        status: 1,
        top: "50px",
        left: "600px",
      },
      {
        label: "新疆维吾尔自治区水利厅",
        status: 1,
        top: "110px",
        right: "630px",
      },
      {
        label: "昌吉回族自治州水利局",
        status: 1,
        top: "110px",
        left: "640px",
      },
      {
        label: "乌鲁木齐市水务局",
        status: 1,
        top: "170px",
        right: "650px",
      },
      {
        label: "吐鲁番市水利局",
        status: 1,
        top: "170px",
        left: "658px",
      },
      {
        label: "阿克苏地区水利局",
        status: 1,
        top: "230px",
        right: "650px",
      },
      {
        label: "喀什地区水利局",
        status: 1,
        top: "230px",
        left: "660px",
      },
      {
        label: "哈密市水利局",
        status: 1,
        top: "290px",
        right: "632px",
      },
      {
        label: "克拉玛依市水务局",
        status: 1,
        top: "290px",
        left: "640px",
      },
      {
        label: "博尔塔拉蒙古自治州水利局",
        status: 1,
        top: "350px",
        right: "595px",
      },
      {
        label: "巴音郭楞蒙古自治州水利局",
        status: 1,
        top: "350px",
        left: "602px",
      },
      {
        label: "新疆维吾尔自治区和田地区水利局",
        status: 1,
        top: "410px",
        right: "520px",
      },
    ],
  },
  {
    label: "地（州、区）市",
    data: [
      {
        label: "乌鲁木齐市",
        status: 1,
        top: "-10px",
        left: "180px",
      },
      {
        label: "头屯河流域管理局",
        status: 1,
        top: "-10px",
        right: "130px",
      },
      {
        label: "克拉玛依市",
        status: 1,
        top: "35px",
        left: "120px",
      },
      {
        label: "伊犁哈萨克自治州",
        status: 1,
        top: "35px",
        right: "70px",
      },
      {
        label: "吐鲁番市",
        status: 1,
        top: "85px",
        left: "100px",
      },
      {
        label: "塔城地区",
        status: 1,
        top: "85px",
        right: "90px",
      },
      {
        label: "哈密市",
        status: 1,
        top: "135px",
        left: "90px",
      },
      {
        label: "阿勒泰地区",
        status: 1,
        top: "135px",
        right: "50px",
      },
      {
        label: "昌吉回族自治州",
        status: 1,
        top: "185px",
        left: "20px",
      },
      {
        label: "塔里木河流域管理局",
        status: 0,
        top: "185px",
        right: "-15px",
      },
      {
        label: "博尔塔拉蒙古自治州",
        status: 1,
        top: "235px",
        left: "-6px",
      },
      {
        label: "玛纳斯河流域管理局",
        status: 1,
        top: "235px",
        right: "-14px",
      },
      {
        label: "巴音郭楞蒙古自治州",
        status: 0,
        top: "285px",
        left: "10px",
      },
      {
        label: "金沟河流域管理局",
        status: 1,
        top: "285px",
        right: "15px",
      },
      {
        label: "阿克苏地区",
        status: 1,
        top: "335px",
        left: "96px",
      },
      {
        label: "白杨河流域管理局",
        status: 1,
        top: "335px",
        right: "46px",
      },
      {
        label: "克孜勒苏柯尔克孜自治州",
        status: 1,
        top: "385px",
        left: "65px",
      },
      {
        label: "水文局",
        status: 1,
        top: "385px",
        right: "174px",
      },
      {
        label: "喀什地区",
        status: 1,
        top: "435px",
        left: "246px",
      },
      {
        label: "和田地区",
        status: 1,
        top: "435px",
        right: "246px",
      },
    ],
  },
  {
    label: "流域单位",
    data: [
      {
        label: "新疆维吾尔自治区塔里木河流域管理局",
        status: 1,
        top: "5px",
        left: "-22px",
      },
      {
        label: "新疆玛纳斯河流域管理局",
        status: 1,
        top: "5px",
        right: "60px",
      },
      {
        label: "新疆金沟河流域管理局",
        status: 1,
        top: "170px",
        left: "-20px",
      },
      {
        label: "新疆头屯河流域管理局",
        status: 1,
        top: "170px",
        right: "-30px",
      },
      {
        label: "新疆喀什噶尔河流域管理局",
        status: 1,
        top: "350px",
        left: "0px",
      },
      {
        label: "新疆白杨河流域管理局",
        status: 1,
        top: "350px",
        right: "20px",
      },
    ],
  },
]);
const centerData = computed(() => {
  return btns.value[btnAct.value].data;
});
const errorData = computed(() => {
  return btns.value[btnAct.value].data.filter((item) => item.status == 0);
});
</script>

<style scoped lang="scss">
.c-center {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 15px 0;
  position: relative;
  .nums {
    width: 820px;
    height: 78px;
    margin: 0 auto 20px;
    display: flex;
    justify-content: space-between;
    background: url("@/assets/cockpit/top.png") no-repeat;
    background-size: 100% 100%;
    padding: 0 88px;
    .num-item {
      width: 130px;
      text-align: center;
      .num-name {
        font-family: PingFangSC;
        font-weight: 500;
        font-size: 16px;
        color: #3fd0ff;
        text-shadow: 2px 2px 0px #11141673, 0 0 2px #fafeff;
        margin-bottom: 5px;
      }
      .num-val {
        font-family: HYk2gj;
        font-size: 24px;
        color: #ffffff;
      }
    }
  }
  .center {
    width: 829px;
    height: 480px;
    background: url("@/assets/cockpit/center.png") no-repeat;
    background-size: 100% 100%;
    position: relative;
    .title {
      position: absolute;
      left: 355px;
      top: 50px;
      font-family: HYk2gj;
      font-size: 20px;
      color: #e8f4ff;
      line-height: 18px;
      text-shadow: 2px 0px 1px #000000, 0 0 5px #fff;
    }
    .center-item {
      display: inline-block;
      width: auto;
      height: 28px;
      line-height: 26px;
      background: rgba(0, 19, 114, 0.4);
      border-radius: 14px;
      border: 1px solid rgba(64, 134, 204, 0.8);
      position: absolute;
      &.error {
        &:nth-child(n) {
          background: rgba(75, 75, 75, 0.4);
          border: 1px solid #7f7f7f;
          &::after {
            content: "";
            background: #7f7f7f;
            border: 1px solid #b7b7b7;
          }
        }
      }
      &:nth-child(2n) {
        padding: 0 35px 0 15px;
        &::after {
          content: "";
          position: absolute;
          top: -2px;
          right: -2px;
          display: block;
          width: 30px;
          height: 30px;
          background: #00a2ff;
          border: 2px solid #79ceff;
          border-radius: 50%;
        }
      }
      &:nth-child(2n + 1) {
        padding: 0 15px 0 35px;
        &::after {
          content: "";
          position: absolute;
          top: -2px;
          left: -2px;
          display: block;
          width: 30px;
          height: 30px;
          background: #00a2ff;
          border: 2px solid #79ceff;
          border-radius: 50%;
        }
      }
      .center-name {
        // max-width: 200px;
        font-family: FZLTZHK--GBK1;
        font-weight: 500;
        font-size: 15px;
        color: #ffffff;
      }
    }
  }
  .center-select {
    position: absolute;
    right: 0;
    bottom: 0;
    .btn {
      cursor: pointer;
      width: 138px;
      height: 42px;
      line-height: 42px;
      text-align: center;
      background: url("@/assets/cockpit/center-btn.png") no-repeat;
      background-size: 100% 100%;
      margin-top: 6px;
      font-family: HYk2gj;
      font-size: 14px;
      color: #ffffff;

      &.act {
        background: url("@/assets/cockpit/center-btn-act.png") no-repeat;
        background-size: 100% 100%;
      }
    }
  }
  .error-list {
    width: 282px;
    height: 34px;
    border-radius: 4px 4px 4px 4px;
    border: 1px solid #ff2626;
    margin: 20px auto 0;
    padding: 0 5px 0 0;
    :deep(.el-carousel__indicators--right) {
      right: -2px;
      .el-carousel__indicator--vertical {
        padding: 1px;
        .el-carousel__button {
          width: 4px;
          height: 4px;
        }
      }
    }
    .error-item {
      height: 100%;
      display: flex;
      align-items: center;
      img {
        width: 35px;
        margin-top: 5px;
        // margin-right: 6px;
      }
      span {
        display: inline-block;
        font-family: HYk2gj;
        font-size: 18px;
        color: #ffffff;
        text-shadow: 2px 0px 8px #051c37;
      }
    }
  }
}
</style>
