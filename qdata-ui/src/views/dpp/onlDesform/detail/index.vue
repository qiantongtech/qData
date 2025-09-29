<template>
  <div class="div-container">
    <div class="toptitle">
      <h1 class="htitle">
        {{ title }}
      </h1>
      <el-tooltip v-if="isPC" class="item" effect="dark" placement="left-end">
        <template #content><img :src="qrCodeData" /></template>
        <img src="@/assets/images/qrIcon.png" alt="QR Code" class="qrCodeImage" />
      </el-tooltip>
    </div>
    <div class="form-container">
      <FormCreate v-if="hasData" v-model="formData" :rule="rule" :option="option" disabled></FormCreate>
      <el-skeleton v-if="loading" />
      <div v-if="!hasForm">暂无表单</div>
      <template v-if="!hasData">
        <h1 class="error-box">
          <span>表单数据不存在，dataId={{ dataId }}</span>
        </h1>
        <div class="button">
          <el-button id="closeBtn" class="ant-btn ant-btn-default" style="" @click="handleAutoClose()">关闭</el-button>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import { getByDesformCode } from "@/api/dpp/onlDesform/desForm.js";
import { updatedata, getdata } from "@/api/dpp/onlDesformData/desformData.js";
import qrcode from "qrcode";

let BrowserTypes = {
  isWeChat: function () {
    let ua = navigator.userAgent.toLowerCase();
    return ua.indexOf("micromessenger") !== -1;
  },
};

export default {
  data() {
    return {
      rule: [],
      option: {},
      formData: {},
      formConfig: "",
      desformCode: "", //表单编码
      dataId: "", //数据id
      loading: false,
      hasForm: true,
      hasData: true,
      title: "",
      qrCodeData: "",
      isPC: false,
    };
  },
  mounted() {
    this.desformCode = this.$route.params.desformCode;
    this.dataId = this.$route.params.dataId;
    this.getData();
    //生成二维码
    qrcode.toDataURL(window.location.href, (err, url) => {
      if (err) {
        console.error(err);
      } else {
        this.qrCodeData = url;
      }
    });
  },
  created() {
    this.getScreen();
  },
  methods: {
    getScreen() {
      this.isPC = !navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
    },
    getData() {
      this.formData = {};
      this.formConfig = "";
      // 模拟异步加载
      this.loading = true;
      getByDesformCode(this.desformCode)
        .then((res) => {
          if (res.code == 200) {
            this.title = res.data.desformName;
            let desformJson = JSON.parse(res.data.desformJson);
            if (!desformJson || desformJson.rule.length == 0) {
              this.hasForm = false;
            } else {
              this.rule = JSON.parse(desformJson.rule);
              this.option = {
                submitBtn: {
                  show: false,
                  innerText: "提交",
                  click: () => {
                    this.handleSubmit();
                  },
                },
                resetBtn: {
                  show: true,
                  innerText: "关闭",
                  click: () => {
                    this.handleAutoClose();
                  },
                },
                form: {
                  inline: false,
                  hideRequiredAsterisk: false,
                  labelPosition: "right",
                  size: "default",
                  labelWidth: "125px",
                },
              };
              this.hasForm = true;
            }
          } else {
            this.$message.warning('操作失败，请检查后重试');
            this.hasForm = false;
          }
        })
        .then(() => {
          getdata(this.dataId).then((res) => {
            if (res.code == 200) {
              if (!res.data.desformData || res.data.desformData.length == 0) {
                this.hasData = false;
                return;
              }
              const fixedString = res.data.desformData.replace(/^"|"$/g, "'");
              this.$nextTick(() => {
                this.formData = Object.assign({}, JSON.parse(fixedString));
                console.log(this.formData);
              });
            } else {
              this.$message.warning('操作失败，请检查后重试');
            }
          });
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handleSubmit() {
      let params = {
        id: this.dataId,
        desformCode: this.desformCode,
        desformData: JSON.stringify(this.formData),
      };
      updatedata(params).then((res) => {
        if (res.code == 200) {
          this.$message.success("提交成功");
          this.$router.push("/dpp/onlDesform/data/success");
        } else {
          this.$message.warning("提交失败，请检查后重试");
        }
      });
      return true;
    },
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
<style lang="scss" scoped>
.error-box {
  text-align: center;
  padding: 40px 0;
  text-shadow: 0 0 12px #ffffff;
}

.button {
  width: 200px;
  margin: 0 auto;
}

.ant-btn {
  width: 200px;
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

.div-container {
  margin: 0 auto !important;
  padding: 15px;
  background-color: #f3f4f5;
  width: 100vw;
  height: 100vh;

  .form-container {
    margin: 0 auto;
    padding: 0px 25px 50px 25px;
    max-width: 850px;
    background: #fff;
  }
}

.toptitle {
  margin: 0 auto;
  max-width: 850px;
  color: #0095ff;
  padding: 50px 100px 20px;
  background: #fff;
  position: relative;

  .htitle {
    line-height: 40px;
    font-weight: 600;
    -webkit-font-smoothing: antialiased;

    font-size: 24px;
    font-weight: bold;
    margin: 0;
    padding: 0;
    text-align: center;
  }

  .qrCodeImage {
    right: 10px;
    top: 10px;
    width: 30px;
    height: 30px;
    position: absolute;
  }
}
</style>
