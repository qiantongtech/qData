<template>
  <div id="loader-wrapper">
    <div id="loader"></div>
    <div class="loader-section section-left"></div>
    <div class="loader-section section-right"></div>
    <div class="load_title">正在登录统一身份认证中心，请耐心等待~~</div>
  </div>
</template>

<script>
import {codeLogin} from "@/api/system/sso-auth.js";
import useUserStore from '@/store/system/user.js'

const userStore = useUserStore()
export default {
  name: "sso",
  components: {
  },
  data() {
    return {
      code: null,
      fullPath: ""
    };
  },
  created() {
  },
  mounted() {
  },
  beforeRouteEnter(to, from, next) {
    const code = to.query.code
    const fullPath = to.query.state
    next(vm => {
      vm.code = code
      vm.fullPath = fullPath
      vm.checkCode()
    });
  },
  methods: {
    // 检测code值
    checkCode() {
      if (this.code != null && this.code !== '') {
        // todo 使用授权码去获取Token
        codeLogin(this.code).then(res => {
          console.log("Token :" + res.msg)

          // 调用 action 的登录方法
          userStore.setToken(res.msg).then(() => {
            this.$router.push({path: this.fullPath === "" ? "/index" : this.fullPath})
          })
        }).catch(err => {
          console.log(err)
          // 退出本系统的状态
          if (confirm(err)) {
            // 执行注销逻辑
            useUserStore().logOut().then(() => {
              location.href = '/index'
            })
          }
        })
      }
    }
  }
};
</script>

<style scoped lang="scss">
  .chromeframe {
    margin: 0.2em 0;
    background: #ccc;
    color: #000;
    padding: 0.2em 0;
  }

  #loader-wrapper {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 999999;
  }

  #loader {
    display: block;
    position: relative;
    left: 50%;
    top: 50%;
    width: 150px;
    height: 150px;
    margin: -75px 0 0 -75px;
    border-radius: 50%;
    border: 3px solid transparent;
    border-top-color: #FFF;
    -webkit-animation: spin 2s linear infinite;
    -ms-animation: spin 2s linear infinite;
    -moz-animation: spin 2s linear infinite;
    -o-animation: spin 2s linear infinite;
    animation: spin 2s linear infinite;
    z-index: 1001;
  }

  #loader:before {
    content: "";
    position: absolute;
    top: 5px;
    left: 5px;
    right: 5px;
    bottom: 5px;
    border-radius: 50%;
    border: 3px solid transparent;
    border-top-color: #FFF;
    -webkit-animation: spin 3s linear infinite;
    -moz-animation: spin 3s linear infinite;
    -o-animation: spin 3s linear infinite;
    -ms-animation: spin 3s linear infinite;
    animation: spin 3s linear infinite;
  }

  #loader:after {
    content: "";
    position: absolute;
    top: 15px;
    left: 15px;
    right: 15px;
    bottom: 15px;
    border-radius: 50%;
    border: 3px solid transparent;
    border-top-color: #FFF;
    -moz-animation: spin 1.5s linear infinite;
    -o-animation: spin 1.5s linear infinite;
    -ms-animation: spin 1.5s linear infinite;
    -webkit-animation: spin 1.5s linear infinite;
    animation: spin 1.5s linear infinite;
  }


  @-webkit-keyframes spin {
    0% {
      -webkit-transform: rotate(0deg);
      -ms-transform: rotate(0deg);
      transform: rotate(0deg);
    }
    100% {
      -webkit-transform: rotate(360deg);
      -ms-transform: rotate(360deg);
      transform: rotate(360deg);
    }
  }

  @keyframes spin {
    0% {
      -webkit-transform: rotate(0deg);
      -ms-transform: rotate(0deg);
      transform: rotate(0deg);
    }
    100% {
      -webkit-transform: rotate(360deg);
      -ms-transform: rotate(360deg);
      transform: rotate(360deg);
    }
  }


  #loader-wrapper .loader-section {
    position: fixed;
    top: 0;
    width: 51%;
    height: 100%;
    background: #aad1f8;
    z-index: 1000;
    -webkit-transform: translateX(0);
    -ms-transform: translateX(0);
    transform: translateX(0);
  }

  #loader-wrapper .loader-section.section-left {
    left: 0;
  }

  #loader-wrapper .loader-section.section-right {
    right: 0;
  }


  .loaded #loader-wrapper .loader-section.section-left {
    -webkit-transform: translateX(-100%);
    -ms-transform: translateX(-100%);
    transform: translateX(-100%);
    -webkit-transition: all 0.7s 0.3s cubic-bezier(0.645, 0.045, 0.355, 1.000);
    transition: all 0.7s 0.3s cubic-bezier(0.645, 0.045, 0.355, 1.000);
  }

  .loaded #loader-wrapper .loader-section.section-right {
    -webkit-transform: translateX(100%);
    -ms-transform: translateX(100%);
    transform: translateX(100%);
    -webkit-transition: all 0.7s 0.3s cubic-bezier(0.645, 0.045, 0.355, 1.000);
    transition: all 0.7s 0.3s cubic-bezier(0.645, 0.045, 0.355, 1.000);
  }

  .loaded #loader {
    opacity: 0;
    -webkit-transition: all 0.3s ease-out;
    transition: all 0.3s ease-out;
  }

  .loaded #loader-wrapper {
    visibility: hidden;
    -webkit-transform: translateY(-100%);
    -ms-transform: translateY(-100%);
    transform: translateY(-100%);
    -webkit-transition: all 0.3s 1s ease-out;
    transition: all 0.3s 1s ease-out;
  }

  .no-js #loader-wrapper {
    display: none;
  }

  .no-js h1 {
    color: #222222;
  }

  #loader-wrapper .load_title {
    font-family: 'Open Sans';
    color: #FFF;
    font-size: 19px;
    width: 100%;
    text-align: center;
    z-index: 9999999999999;
    position: absolute;
    top: 60%;
    opacity: 1;
    line-height: 30px;
  }

  #loader-wrapper .load_title span {
    font-weight: normal;
    font-style: italic;
    font-size: 13px;
    color: #FFF;
    opacity: 0.5;
  }
</style>
