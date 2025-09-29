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
      <FormCreate v-if="hasForm" v-model="formData" :rule="rule" :option="option"></FormCreate>
      <el-skeleton v-if="loading" />
      <div v-if="!hasForm">暂无表单</div>
    </div>
  </div>
</template>

<script>
import { getByDesformCode } from "@/api/dpp/onlDesform/desForm.js";
import { adddata } from "@/api/dpp/onlDesformData/desformData.js";
import qrcode from "qrcode";

export default {
  data() {
    return {
      rule: [],
      option: {},
      formData: {},
      formConfig: "",
      desformCode: "", //表单编码
      loading: false,
      hasForm: true,
      title: "",
      qrCodeData: "",
      isPC: false,
    };
  },
  created() {
    this.getScreen();
  },
  methods: {
    getScreen() {
      this.isPC = !navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
    },
    handleSubmit() {
      let params = {
        desformCode: this.desformCode,
        desformData: JSON.stringify(this.formData),
      };
      adddata(params).then((res) => {
        if (res.code == 200) {
          this.$message.success("提交成功");
          this.$router.push("/dpp/onlDesform/data/success");
        } else {
          this.$message.warning("提交失败，请检查后重试");
        }
      });
    },
    cancel() {
      this.$emit("cancel");
    },
  },
  mounted() {
    // 模拟异步加载
    this.loading = true;
    this.desformCode = this.$route.params.desformCode;
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
                show: true,
                innerText: "提交",
                click: () => {
                  this.handleSubmit();
                },
              },
              resetBtn: {
                show: false,
                innerText: "取消",
                click: () => {},
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
          this.$message.warning("操作失败，请检查后重试");
          this.hasForm = false;
        }
      })
      .finally(() => {
        this.loading = false;
      });

    //生成二维码
    qrcode.toDataURL(window.location.href, (err, url) => {
      if (err) {
        console.error(err);
      } else {
        this.qrCodeData = url;
      }
    });
  },
};
</script>
<style lang="scss" scoped>
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
