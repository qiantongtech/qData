<template>
  <div class="weather">
    <div class="address">
      <img src="@/assets/index/weather/address.png" alt="" />
      <span>{{ weatherData.location.name }}</span>
    </div>
    <div class="climate">
      <img class="climate-icon" :src="getWeatherIcon(weatherData.now.text)" alt="" />
      <div class="wendu">{{ weatherData.now.temperature }}<span>℃</span></div>
      <div class="today">
        <div class="label">
          <span>今天</span>
          <span>{{ weatherData.now.text }}</span>
        </div>
        <div class="value">
          <span class="value-text">{{ future.daily[0].high }}/{{ future.daily[0].low }}℃</span>
          <div class="quality">
            <span class="quality-icon"></span>
            <span class="quality-text">优</span>
          </div>
        </div>
      </div>
    </div>
    <div class="future">
      <template v-for="(item, index) in future.daily" :key="index">
        <div class="future-item">
          <img :src="getWeatherIcon(item.text_day)" alt="" />
          <div class="future-box">
            <div class="box-label">{{ parseTime(item.date, "{m}月{d}日") }}</div>
            <div class="box-value">{{ item.high }}/{{ item.low }}℃</div>
          </div>
        </div>
        <div class="future-line" v-if="index !== future.daily.length - 1"></div>
      </template>
    </div>
  </div>
</template>
<script setup name="Weather">
import axios from "axios";
const weatherData = ref({
  location: {},
  now: {},
});
const future = ref({
  location: {},
  daily: [
    {
      high: "30",
      low: "10",
    },
  ],
});
const getWeatherIcon = (text) => {
  let img = "晴";
  switch (text) {
    case "晴":
      img = "晴";
      break;
    case "暴雨":
      img = "暴雨";
      break;
    case "雷阵雨":
      img = "雷阵雨";
      break;
    case "雷阵雨伴有冰雹":
      img = "雷阵雨伴有冰雹";
      break;
    case "大暴雨":
      img = "特大暴雨";
      break;
    case "特大暴雨":
      img = "特大暴雨";
      break;
    case "雾":
      img = "雾";
      break;
    case "小雨":
    case "中雨":
    case "大雨":
      img = "下雨";
      break;
    case "阵雪":
      img = "阵雪";
      break;
    case "小雪":
    case "中雪":
    case "大雪":
      img = "雪";
      break;
    case "暴雪":
      img = "暴雪";
      break;
    case "风":
    case "大风":
      img = "风";
      break;
    case "飓风":
    case "热带风暴":
    case "龙卷风":
      img = "风";
      break;
    case "雨夹雪":
      img = "雨夹雪";
      break;
    default:
      break;
  }
  return new URL(`../../assets/index/weather/${img}.png`, import.meta.url).href;
};
const getList = () => {
  axios.get("https://api.seniverse.com/v3/weather/now.json?key=SjyiLD_odjCGOsHoF&location=ip&language=zh-Hans&unit=c").then((res) => {
    weatherData.value = res.data.results[0];
  });
  axios.get("https://api.seniverse.com/v3/weather/daily.json?key=SjyiLD_odjCGOsHoF&location=ip&language=zh-Hans&unit=c&start=0&days=5").then((res) => {
    future.value = res.data.results[0];
  });
};
getList();
</script>
<style lang="scss" scoped>
.weather {
  width: 100%;
  height: 100%;
  padding: 5px 15px;
  // padding-top: 6px;
  background: url("@/assets/index/weather/bg.png") no-repeat;
  background-size: 100% 100%;

  .address {
    display: flex;
    align-items: center;

    img {
      width: 10px;
      height: 12px;
      margin-right: 5px;
    }

    span {
      font-family: PingFang SC;
      font-weight: normal;
      font-size: 12px;
      color: #cce6f8;
    }
  }

  .climate {
    display: flex;
    align-items: center;
    margin: 3px 0 3px;

    .climate-icon {
      width: 64px;
      margin-right: 9px;
    }

    .wendu {
      line-height: 1;
      font-family: DIN-Regular;
      font-size: 50px;
      font-weight: 600;
      color: #ffffff;
      margin-right: 20px;

      span {
        font-family: PingFang SC;
        font-weight: 400;
        font-size: 20px;
      }
    }

    .today {
      .label {
        width: 75px;
        font-family: PingFang SC;
        font-weight: normal;
        font-size: 16px;
        color: #ffffff;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .value {
        display: flex;
        align-items: center;

        .value-text {
          font-family: DIN-Regular;
          font-size: 18px;
          color: #ffffff;
          margin-right: 19px;
        }

        .quality {
          padding: 0 10px;
          height: 20px;
          background: #67baee;
          border-radius: 10px 10px 10px 10px;

          .quality-icon {
            display: inline-block;
            width: 9px;
            height: 9px;
            border-radius: 50%;
            background: #51cfa4;
            border: 1px solid #ffffff;
            margin-right: 4px;
          }

          .quality-text {
            font-family: PingFang SC;
            font-weight: normal;
            font-size: 14px;
            color: #ffffff;
          }
        }
      }
    }
  }

  .future {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .future-item {
      display: flex;
      align-items: center;

      img {
        width: 36px;
        height: 36px;
        margin-right: 3px;
      }

      .future-box {
        .box-label {
          font-family: DIN-Regular;
          font-weight: normal;
          font-size: 11px;
          color: #cce6f8;
        }

        .box-value {
          font-family: DIN-Regular;
          font-weight: normal;
          font-size: 12px;
          color: #ffffff;
        }
      }
    }

    .future-line {
      width: 1px;
      height: 25px;
      background: #2794ea;
      margin: 0 auto;
    }
  }
}

@media screen and (max-width: 1650px) {
  .weather .climate {
    .wendu {
      margin-right: 15px;
    }

    .today .value .quality {
      padding: 0 6px;
    }
  }
}
</style>
