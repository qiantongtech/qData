<template>
  <div class="c-left">
    <div class="box-wrap left1">
      <div class="box-title">
        <div class="box-name">数据统计</div>
      </div>
      <div class="box-con">
        <div class="left-item" v-for="(item, index) in leftData" :key="index"
          :style="{ backgroundImage: `url('${item.bg}')` }">
          <div class="left-name">{{ item.name }}</div>
          <div class="left-val" @click="handleToggle(index)"
            :style="item.val2 !== undefined && item.val2 !== null ? 'cursor: pointer' : ''">
            <template v-if="toggledIndex === index && item.val2 !== undefined && item.val2 !== null">
              {{ item.val2 }}<span>T</span>
            </template>
            <template v-else>
              {{ item.val }}<span>条</span>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup name="CockpitLeft">
const getAssetsFile = (url) => {
  return new URL(`../../../assets/cockpit/${url}`, import.meta.url).href;
};
const leftData = ref([
  {
    name: "数据量（条数）",
    val: "234512",
    bg: getAssetsFile("left (2).png"),
  },
  {
    name: "基础数据（条数）",
    val: "2142",
    bg: getAssetsFile("left (3).png"),
  },
  {
    name: "监测数据（条数）",
    val: "6231",
    bg: getAssetsFile("left (5).png"),
  },
  {
    name: "地理空间数据（条数）",
    val: "6543",
    val2: "213",
    bg: getAssetsFile("left (4).png"),
  },

  {
    name: "业务数据（条数）",
    val: "1432",
    bg: getAssetsFile("left (6).png"),
  },
  {
    name: "跨行业共享数据（条数）",
    val: "712",
    bg: getAssetsFile("left (1).png"),
  },
]);
const showVal2 = ref(false)

const toggledIndex = ref(null)

function handleToggle(index) {
  const item = leftData.value[index]
  if (item.val2 !== undefined && item.val2 !== null) {
    toggledIndex.value = toggledIndex.value === index ? null : index
  }
}
</script>

<style scoped lang="scss">
.c-left {
  .left1 {
    width: 502px;
    height: 648px;
    background: url("@/assets/cockpit/box (2).png") no-repeat;
    background-size: 100% 100%;

    .box-con {
      padding-top: 15px;
    }

    .left-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 0 auto 30px;
      width: 426px;
      height: 63px;
      font-family: HYk2gj;
      background-repeat: no-repeat;
      background-size: 100% 100%;
      padding-left: 92px;

      .left-name {
        font-size: 16px;
        color: #ffffff;
      }

      .left-val {
        width: 150px;
        text-align: center;
        font-size: 24px;
        color: #00eeff;
        text-shadow: 2px 0px 8px #051c37;
        letter-spacing: 1px;

        span {
          font-size: 12px;
        }
      }
    }
  }
}
</style>
