<template>
    <!-- 上次登录用户登录页面登录页面样式二 -->
    <div class="app-container login-two sysInfo sysInfo-wrap" ref="app-container">
        <div class="left-content">
            <div class="swiper leftSwiper">
                <div class="swiper-wrapper">
                    <!--          <el-carousel  style="width:100%;heght:100%;" arrow="never" autoplay>-->
                    <!--            <el-carousel-item v-for="(item,index) in loginimglist" :key="index">-->
                    <!--              <div :class="'swiper-slide swiper-slide-'+item.id"></div>-->
                    <!--            </el-carousel-item>-->
                    <!--          </el-carousel>-->

                    <el-carousel style="width: 100%; heght: 100%" arrow="never" autoplay indicator-position="none">
                        <el-carousel-item v-for="(item, index) in loginimglist" :key="index">
                            <div class="swiper-slide" :style="getBackgroundStyle(item)"></div>
                        </el-carousel-item>
                    </el-carousel>
                </div>
                <div class="swiper-pagination"></div>
            </div>
        </div>
        <div class="right-content">
            <div class="logo">
                <img :src="logo" />
            </div>
            <div>
                <div class="greeting">
                    <div class="entry_period">亲爱的朋友，{{ greeting }}！</div>
                    <div class="entry_greeting"> 🌟 每一次登录，都是向数据价值更进一步！</div>
                </div>
                <div class="login-panel">
                    <el-form ref="loginRef" :model="loginForm" :rules="loginRules">
                        <p class="titles">账号登录</p>
                        <div class="titles-bar"></div>
                        <el-form-item prop="username">
                            <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
                                <template #prefix>
                                    <i class="iconfont">&#xebc0;</i>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password">
                            <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"
                                @keyup.enter="handleLogin">
                                <template #prefix>
                                    <i class="iconfont">&#xeb8d;</i>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="code" v-if="captchaEnabled">
                            <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" class="code-class"
                                @keyup.enter.native="handleLogin">
                                <template #prefix>
                                    <i class="iconfont">&#xeb9e;</i>
                                </template>
                            </el-input>
                            <div class="login-code">
                                <img :src="codeUrl" @click="getCode" class="login-code-img" />
                            </div>
                        </el-form-item>

                        <el-form-item style="width: 100%">
                            <el-button :loading="loading" type="primary" style="width: 100%"
                                @click.native.prevent="handleLogin">
                                <span v-if="!loading">登 录</span>
                                <span v-else>登 录 中...</span>
                            </el-button>
                        </el-form-item>

                        <div class="form-actions">
                            <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
                            <el-text type="primary" @click="dialogVisible = true">忘记密码</el-text>
                        </div>
                    </el-form>
                </div>
            </div>
            <div>
                <div class="description">
                    <div class="contact" style="float: left">
                        <img src="@/assets/system/images/login/phone.png" />
                        <div>
                            <p>联系电话：</p>
                            <!--              <p>400-660-8208</p>-->
                            <p>
                                {{
                                    contentDetail && contentDetail.contactNumber
                                        ? contentDetail.contactNumber
                                        : '400-660-8208'
                                }}
                            </p>
                        </div>
                    </div>
                    <div class="contact" style="margin-left: 24px">
                        <img src="@/assets/system/images/login/email.png" />
                        <div>
                            <p>电子邮箱：</p>
                            <!--              <p>sales@qiantong.tech</p>-->
                            <p>
                                {{
                                    contentDetail && contentDetail.email
                                        ? contentDetail.email
                                        : 'sales@qiantong.tech'
                                }}
                            </p>
                        </div>
                    </div>
                </div>
                <div class="chrome-wrap">
                    <img src="@/assets/system/images/login/goge-icon.png" style="height: 20px" />
                    <span style="color: #888; font-size: 12px; line-height: 0; margin-left: 10px">为保证最佳浏览效果，请使用</span>
                    <span style="color: #ee2223; font-size: 12px; line-height: 0">Chrome</span>
                    <span style="color: #888; font-size: 12px; line-height: 0">浏览器，点击下载安装</span>
                    <a href="https://www.google.cn/chrome/" target="_blank">
                        <div style="
                                margin-left: 15px;
                                display: flex;
                                flex-direction: column;
                                align-items: center;
                            ">
                            <img id="window_img" src="@/assets/system/images/login/window-icon.svg"
                                style="height: 25px" />
                            <span style="
                                    color: #888;
                                    font-size: 12px;
                                    line-height: 0;
                                    margin-top: 7px;
                                ">Window</span>
                        </div>
                    </a>
                    <a href="https://www.google.cn/chrome/" target="_blank">
                        <div style="
                                margin-left: 15px;
                                display: flex;
                                flex-direction: column;
                                align-items: center;
                            ">
                            <img id="mac_img" src="@/assets/system/images/login/mac-icon.svg" style="height: 25px" />
                            <span style="
                                    color: #888;
                                    font-size: 12px;
                                    line-height: 0;
                                    margin-top: 7px;
                                ">Mac</span>
                        </div>
                    </a>
                </div>
                <div class="bottom-info">
                    <div class="copy-right" style="cursor: pointer;">
                        <a href="https://qiantong.tech/" target="_blank">
                            Copyright© {{ new Date().getFullYear() }}
                            <span style="color: #2666fb">江苏千桐科技有限公司</span>
                            版权所有
                        </a>
                    </div>

                    <div class="record" @click="goKtPage()">
                        <img src="https://www.asktempo.com/statics/images/an.png" alt="" />
                        <!--            &nbsp;&nbsp; 苏ICP备2022008519号-1-->
                        &nbsp;&nbsp;
                        {{
                            contentDetail && contentDetail.recordNumber
                                ? contentDetail.recordNumber
                                : '苏ICP备2022008519号-3'
                        }}
                    </div>
                </div>
            </div>
        </div>
    </div>

    <el-dialog v-model="dialogVisible" title="忘记密码" class="fp-form-dialog" width="650px"
        :append-to="$refs['app-container']" draggable destroy-on-close>
        <el-form :model="fpForm" label-width="auto" style="padding: 10px 60px 0">
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="用户名">
                        <el-input v-model="fpForm.name" placeholder="请输入手机号或用户名" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="验证码">
                        <div class="wrapper">
                            <el-input v-model="fpForm.code" placeholder="请输入验证码" />
                            <el-button type="primary" :disabled="codeFlag" style="margin-left: 10px"
                                @click="handleFPCodeClick">{{ codeFlag ? `${codeTime}s` : '获取验证码' }}</el-button>
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="新密码">
                        <el-input v-model="fpForm.password" placeholder="请输入新密码" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="确认密码">
                        <el-input v-model="fpForm.password2" placeholder="请输入确认密码" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="dialogVisible = false"> 重置密码 </el-button>
            </div>
        </template>
    </el-dialog>
    <!-- </div> -->
</template>

<script setup>
import { ref } from 'vue';
import { getCodeImg } from '@/api/system/login';
import Cookies from 'js-cookie';
import { encrypt, decrypt } from '@/utils/jsencrypt';
import Swiper from 'swiper';
import 'swiper/swiper-bundle.min.css';
import useUserStore from '@/store/system/user.js';
import { getContent } from '@/api/system/system/content';
import defaultLogo from '@/assets/system/images/login/qData-logo.png';
import { useTimeGreeting } from '@/composables/useTimeGreeting';
const userStore = useUserStore();
const dialogVisible = ref(false);
const { proxy } = getCurrentInstance();
const loading = ref(false);
const codeUrl = ref('');
// const greetingsTitle = ref('');
const { greeting, message } = useTimeGreeting()
const captchaEnabled = ref(true);
const codeFlag = ref(false);
const loginForm = ref({
    username: 'qData',
    password: 'qData123',
    rememberMe: false,
    code: '',
    uuid: ''
});
const fpForm = ref({
    username: '',
    password: '',
    password2: '',
    code: ''
});
// const loginimglist=ref([
//   {
//     id:1,
//     imgurl:'banner.png',
//   },
//   // {
//   //   id:2,
//   //   imgurl:'banner2.png',
//   // },
//   // {
//   //   id:3,
//   //   imgurl:'banner-gy.png',
//   // },
//   {
//     id:4,
//     imgurl:'banner-sl.png',
//   }
// ])
const defaltImglist = ref([
    { id: 1, image: new URL('@/assets/system/images/login/banner-zt.png', import.meta.url).href },

]);
const loginimglist = ref([]);

const getBackgroundStyle = (item) => {
    return {
        background: `url(${item.image}) center center / cover no-repeat`,

    };
};

const getAssetsFile = (url) => {
    return new URL(`@/assets/system/images/login/${url}`, import.meta.url).href;
};

const loginRules = {
    username: [{ required: true, trigger: 'blur', message: '请输入您的账号' }],
    password: [{ required: true, trigger: 'blur', message: '请输入您的密码' }],
    code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
};

const logo = ref(null);
const contentDetail = ref(null);

onMounted(() => {
    fetchContent();
});
// 使用 getContent 来获取数据，而不是重新定义一个 getContent 函数
const fetchContent = async () => {
    // logo.value = defaultLogo;
    // loginimglist.value = defaltImglist.value
    try {
        // 调用你从 API 导入的 getContent 方法
        const res = await getContent(1); // 假设请求的是 id 为 1 的数据
        if (res.code == 200) {
            const data = res.data;
            console.log("🚀 ~ fetchContent ~  res.data:", res.data)
            contentDetail.value = data;
            const sysLogo = data.loginLogo;
            logo.value = sysLogo ? sysLogo : defaultLogo;
            const carouselImageList = data.carouselImage.split(',');
            console.log('-----login-----0----0--0--------0-', carouselImageList);
            const carouselImgList = [];
            for (let i = 0; i <= carouselImageList.length; i++) {
                let item = carouselImageList[i];
                if (item) {
                    carouselImgList.push({
                        id: i + 1,
                        image: item
                    });
                }
            }
            console.log('-----login-----1----1--1--------1-', defaltImglist.value);
            if (carouselImgList.length > 0) {
                loginimglist.value = carouselImgList;
            } else {
                loginimglist.value = defaltImglist.value;
            }
        } else {
            loginimglist.value = defaltImglist.value;
        }

        // this.$message.success('内容加载成功');
    } catch (error) {
        logo.value = defaultLogo;
        loginimglist.value = defaltImglist.value;
    }
};

// function judgeDate() {
//     var currentTime = new Date();
//     var currentHour = currentTime.getHours();
//     if (currentHour < 12) {
//         greetingsTitle.value = '上午好';
//     } else if (currentHour < 18) {
//         greetingsTitle.value = '下午好';
//     } else {
//         greetingsTitle.value = '晚上好';
//     }
// }
// judgeDate();

function getCookie() {
    const username = Cookies.get('username');
    const password = Cookies.get('password');
    const rememberMe = Cookies.get('rememberMe');
    loginForm.value = {
        username: username === undefined ? loginForm.value.username : username,
        password: password === undefined ? loginForm.value.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
    };
}
getCookie();

function getCode() {
    getCodeImg().then((res) => {
        captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (captchaEnabled.value) {
            codeUrl.value = 'data:image/gif;base64,' + res.img;
            loginForm.value.uuid = res.uuid;
        }
    });
}
getCode();

function handleLogin() {
    localStorage.setItem('username', loginForm.value.username);
    proxy.$refs.loginRef.validate((valid) => {
        if (valid) {
            loading.value = true;
            // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
            if (loginForm.value.rememberMe) {
                Cookies.set('username', loginForm.value.username, { expires: 30 });
                Cookies.set('password', encrypt(loginForm.value.password), { expires: 30 });
                Cookies.set('rememberMe', loginForm.value.rememberMe, { expires: 30 });
            } else {
                // 否则移除
                Cookies.remove('username');
                Cookies.remove('password');
                Cookies.remove('rememberMe');
            }
            // 调用action的登录方法
            userStore
                .login(loginForm.value)
                .then(() => {
                    window.location.href = '/index';
                })
                .catch(() => {
                    loading.value = false;
                    // 重新获取验证码
                    if (captchaEnabled.value) {
                        getCode();
                    }
                });
        }
    });
}
const timeValue = 3;
const codeTime = ref(timeValue);
function handleFPCodeClick() {
    if (codeFlag.value) return;
    codeFlag.value = !codeFlag.value;
    setInterval(() => {
        if (codeTime.value > 1) {
            codeTime.value--;
        } else {
            clearInterval();
            codeTime.value = timeValue;
            codeFlag.value = false;
        }
    }, 1000);
}
//点击备案号调整工信部
function goKtPage() {
    // 在新窗口打开链接
    window.open('https://beian.miit.gov.cn/#/Integrated/index', '_blank'); // 在新窗口打开链接
}
</script>

<style lang="scss">
.el-carousel__button {
    background-color: #2666fb;
}

.fp-form-dialog {
    .wrapper {
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .el-button {
            width: 102px;
            min-width: 102px;
        }
    }

    .el-dialog__body {
        height: 316px !important;
    }

    .el-button {
        background: #2666fb;
        border-radius: 2px 2px 2px 2px;
        border: 1px solid #2666fb;
    }
}
</style>

<style scoped lang="scss">
.login-two {
    height: 100%;
    width: 100%;
    min-width: 1300px;
    overflow: auto;
    display: flex;

    ::v-deep {
        .el-carousel__container {
            height: 100%;
        }
    }

    /*  .swiper-slide-1{
    background: url(@/assets/system/images/login/banner.png) center center / cover no-repeat;
  }
  .swiper-slide-2{
    background: url(@/assets/system/images/login/banner2.png) center center / cover no-repeat;
  }
  .swiper-slide-3{
    background: url(@/assets/system/images/login/banner-gy.jpg) center center / cover no-repeat;
  }
  .swiper-slide-4{
    background: url(@/assets/system/images/login/banner-sl.png) center center / cover no-repeat;
  }*/

    .form-actions {
        display: flex;
        align-items: center;
        justify-content: space-between;
        cursor: pointer;
    }

    ::v-deep .el-form-item__content {
        justify-content: space-between;
    }

    ::v-deep .el-input--medium .el-input__inner {
        height: 40px;
        line-height: 40px;
        padding-left: 35px;
        border-radius: 4px;
        border: 1px solid #e6e6e6;
    }

    ::v-deep .el-input__icon.input-icon.svg-icon {
        height: 45px;
        width: 14px;
        margin-left: 5px;
    }

    ::v-deep .el-form-item__error {
        color: #ff4949;
        font-size: 12px;
        line-height: 1;
        // padding-top: 9px;
        position: absolute;
        top: 100%;
        left: 0;
    }

    ::v-deep .el-checkbox {
        padding: 0 !important;
    }

    .login-code-img {
        height: 100%;
        width: 102px;
    }

    .login-code {
        width: 104px;
        height: 34px;
        float: right;
    }

    ::v-deep .el-form-item {
        margin-bottom: 24px;
    }

    ::v-deep .el-input__prefix {
        top: 1px !important;
        left: 15px !important;
    }

    .left-content {
        width: 55%;
        min-height: 750px;

        .contents {
            position: absolute;
            margin: 6% 0px 0px 13%;
            letter-spacing: 0em;
            font-family: 'sharp';

            .title {
                font-size: 53px;
                margin-bottom: -5px;
                color: #000000;
            }

            .enTitle {
                font-size: 19px;
                margin-bottom: 70px;
                color: #000000;
            }

            .digest {
                font-size: 28px;
                color: #000000;
            }

            .content {
                width: 78%;
                font-size: 18px;
                line-height: 40px;
                color: #000000;
            }
        }

        .leftSwiper {
            width: 100%;
            height: 100%;

            .swiper-imagesize {
                width: 100%;
                height: 100%;
                // object-fit: cover;
            }

            ::v-deep .swiper-pagination-bullets .swiper-pagination-bullet {
                width: 36px;
                height: 4px;
                background: rgba(0, 0, 0, 0.5);
                border-radius: 3px;
                margin-bottom: 30px;
                //opacity: 0.5;
            }
        }
    }

    .iconfont {
        font-size: 17px !important;
    }

    .right-content {
        width: 45%;
        min-height: 750px;
        background: #fff;
        position: relative;
        padding: 30px 0 20px 0;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .logo {
            width: 100px;
            height: 33px;
            margin-left: 20%;

            img {
                // width: 100%;
                height: 100%;
            }
        }

        .greeting {
            margin-left: 20%;

            .entry_period {
                font-size: 25px;
                font-weight: 400;
                margin-bottom: 6px;
                color: rgba(57, 63, 79, 1);
            }

            .entry_greeting {
                font-size: 12px;
                font-weight: 400;
                color: #888;
            }
        }

        .login-panel {
            margin-left: 20%;
            box-sizing: border-box;
            margin-top: 30px;
            width: 400px;
            height: 400px;
            box-shadow: -22px -22px 44px 0px rgba(255, 255, 255, 1),
                22px 22px 44px 0px rgba(217, 217, 217, 1);
            border-radius: 8px;
            padding: 0 38px;
            position: relative;
            overflow: hidden;

            ::v-deep .el-form-item {
                margin-bottom: 20px;
            }

            .titles {
                text-align: center;
                line-height: 65px;
                height: 65px;
                border-bottom: 1px solid rgba(0, 0, 0, 0.1);
                font-size: 18px;
                font-weight: bold;
                color: #333;
                margin-bottom: 30px;
            }

            .titles-bar {
                width: 65px;
                height: 2px;
                background: #2666fb;
                top: 66px;
                position: absolute;
                left: 50%;
                transform: translateX(-50%);
            }

            .code-class {
                width: 63%;
            }
        }

        ::v-deep .el-button--medium {
            padding: 12px 20px;
            // margin-top: 10px;
        }

        .description {
            margin-top: 30px;
            margin-left: 15%;

            .contact {
                font-size: 14px;
                color: #666666;
                display: flex;
                align-items: center;
                height: 42px;
                line-height: 42px;
                width: 173px;

                img {
                    width: 32px;
                    height: 32px;
                    margin-right: 16px;
                }

                p {
                    height: 20px;
                    line-height: 20px;
                    font-size: 16px;
                    // font-weight: 500px;
                    color: #000000;
                    margin: 0;
                }

                p:first-child {
                    font-weight: 500px;
                    font-size: 14px;
                    color: #6d6d6d;
                }
            }
        }

        .chrome-wrap {
            display: flex;
            margin-left: 15%;
            align-items: center;
            margin-top: 8px;
        }

        .bottom-info {
            margin-top: 20px;
            margin-left: 15%;
            height: 17px;
            font-size: 12px;
            font-weight: 400px;
            left: 0px;
            color: #6d6d6d;
            line-height: 17px;
            display: flex;
            align-items: center;

            .record {
                cursor: pointer;
                margin-left: 20px;
                display: flex;
                align-items: center;
            }
        }
    }
}
</style>
<style lang="scss" scoped>
@media screen and (max-width: 1280px) {}

@media screen and (max-width: 992px) {}

@media screen and (max-width: 768px) {}

@media screen and (max-width: 576px) {
    .login-two {
        min-width: 300px;

        .left-content {
            display: none;
        }

        .right-content {
            width: 100%;
            min-height: 300px;
            justify-content: flex-start;
            align-items: center;
            padding: 20px 14px 20px 14px;

            .logo {
                margin-left: 0;
                text-align: center;
            }

            .greeting {
                margin-left: 0;
                margin-top: 20px;
                text-align: center;

                .entry_period {
                    font-size: 16px;
                }
            }

            .login-panel {
                margin-left: 0;
                margin-top: 20px;
                width: auto;
                height: 360px;
                padding: 0 20px;
                box-shadow: 5px 5px 10px 2px #eee;

                .titles {
                    margin: 0 0 20px 0;
                }

                .titles-bar {
                    top: 48px;
                }

                .code-class {
                    width: 55%;
                }
            }

            .description {
                width: 100%;
                margin-left: 0;
                margin-top: 10px;

                .contact {
                    width: 48%;

                    img {
                        width: 28px;
                        height: 28px;
                        margin-right: 12px;
                    }

                    p {
                        font-size: 12px;
                    }
                }
            }

            .chrome-wrap {
                margin-left: 0;
                display: none;
            }

            .bottom-info {
                margin-left: 0;
                margin-top: 10px;
                flex-direction: column;
            }
        }
    }
}

.app-container {
    padding: 0px;
    margin: 0px;
    background-color: #ffffff;
    min-height: 100%;
}
</style>
