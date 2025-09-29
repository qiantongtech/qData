<template>
  <div class="container">
    <i class="icon-success">
      <img src="../../../../../assets/images/success.svg" alt="success" />
    </i>
    <div class="info">
      <h2>提交成功</h2>
    </div>

    <div class="button">
      <el-button class="ant-btn ant-btn-default" @click="handleBack()">返回</el-button>
      <el-button id="closeBtn" class="ant-btn ant-btn-default" style="" @click="handleAutoClose()">关闭</el-button>
    </div>
  </div>
</template>

<script>
let BrowserTypes = {
  isWeChat: function () {
    let ua = navigator.userAgent.toLowerCase();
    return ua.indexOf("micromessenger") !== -1;
  },
};
export default {
  data() {
    return {};
  },
  methods: {
    //返回
    handleBack() {
      this.$router.back();
    },
    //关闭
    handleAutoClose() {
      // 如果是在微信内，就等待微信的JS注入完成之后再执行关闭
      if (BrowserTypes.isWeChat()) {
        let count = 0;

        function handler() {
          // 如果一秒后还未注入完成就无论如何都强行关闭网页
          if (count++ === 10 || window.WeixinJSBridge) {
            clearInterval(timer);
            doClose();
          }
        }

        let timer = setInterval(handler, 100);
        handler();
      } else {
        this.doClose();
      }
    },
    doClose() {
      let ua = navigator.userAgent.toLowerCase();
      // App
      let uaApp = ua.includes("bestore");
      // 安卓浏览器
      let uaAndroid = ua.includes("android");
      // IOS浏览器
      let uaIos = /iphone|ipad|ipod/i.test(ua);
      // 判断关闭方式
      if (window.WeixinJSBridge) {
        window.WeixinJSBridge.call("closeWindow"); // 微信
      } else if (window.AlipayJSBridge) {
        window.AlipayJSBridge.call("closeWebview"); // 支付宝
      } else if (uaApp && uaAndroid) {
        window.obj.closePageLppzRequest(""); // 安卓app
      } else if (uaApp && uaIos) {
        window.webkit.messageHandlers.closePageLppzRequest.postMessage(""); //ios app
      } else {
        window.close();
      }
      let that = this;
      setTimeout(function () {
        // 如果是在 iframe里面，则走返回逻辑
        that.handleBack();
      }, 300);
    },
  },
};
</script>
<style scoped>
.container {
  margin: 0;
  padding: 80px 80px 60px;
  width: 100%;
  height: 100%;
  background: #ffffff;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.icon-success {
  color: #52c41a;
  font-size: 72px;
  line-height: 72px;
  margin-bottom: 24px;

  display: inline-block;
  font-style: normal;
  vertical-align: -0.125em;
  text-align: center;
  text-transform: none;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
}

.icon-success img {
  width: 100px;
  height: 100px;
}

.info {
  margin-bottom: 32px;
}

.info h2 {
  font-size: 24px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  line-height: 32px;
}

.ant-btn {
  line-height: 1.499;
  display: inline-block;
  font-weight: 400;
  text-align: center;
  -ms-touch-action: manipulation;
  touch-action: manipulation;
  cursor: pointer;
  background-image: none;
  border: 1px solid #d9d9d9;
  white-space: nowrap;
  padding: 0 15px;
  font-size: 14px;
  border-radius: 4px;
  height: 32px;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  position: relative;
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.015);
  color: rgba(0, 0, 0, 0.65);
  background-color: #fff;
  -webkit-appearance: button;
  outline: 0;
}

.ant-btn + .ant-btn {
  margin-left: 8px;
}

.ant-btn:hover,
.ant-btn:focus {
  color: #40a9ff;
  background-color: #fff;
  border-color: #40a9ff;
}

.ant-btn:active,
.ant-btn.active {
  color: #096dd9;
  background-color: #fff;
  border-color: #096dd9;
}

@media screen and (max-width: 768px) {
  body {
    background: white;
  }

  .container {
    width: 100%;
    margin: 0;
    padding: 80px 20px 0;
    box-shadow: none;
  }

  .ant-btn {
    width: 100%;
    display: block;
  }

  .ant-btn + .ant-btn {
    margin-top: 8px;
    margin-left: 0;
  }
}
</style>
