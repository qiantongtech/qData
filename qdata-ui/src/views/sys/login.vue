<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
    <!-- Last logged in user login page login page style 2 -->
    <div class="app-container login-two sysInfo sysInfo-wrap" ref="app-container">
        <div class="left-content">
            <div class="swiper leftSwiper">
                <div class="swiper-wrapper">
                    <!--          <el-carousel  style="width:100%;heght:100%;" arrow="never" autoplay>-->
                    <!--            <el-carousel-item v-for="(item,index) in loginimglist" :key="index">-->
                    <!--              <div :class="'swiper-slide swiper-slide-'+item.id"></div>-->
                    <!--            </el-carousel-item>-->
                    <!--          </el-carousel>-->
                  <el-carousel class="login-carousel" arrow="never" autoplay indicator-position="none">
                    <el-carousel-item v-for="(item, index) in loginimglist" :key="index">
                      <div v-if="item.animated" class="swiper-slide qdata-scene">
                        <svg
                            class="qdata-scene__canvas"
                            viewBox="0 0 1305 1206"
                            preserveAspectRatio="xMidYMid slice"
                            aria-hidden="true"
                        >
                          <defs>
                            <clipPath id="left-node-only" clipPathUnits="userSpaceOnUse">
                              <rect x="135" y="620" width="56" height="72"/>
                            </clipPath>
                            <radialGradient id="cloud-route-orb" cx="50%" cy="50%" r="50%">
                              <stop offset="0%" stop-color="#ffffff" stop-opacity="1"/>
                              <stop offset="35%" stop-color="#bff4ff" stop-opacity="0.95"/>
                              <stop offset="100%" stop-color="#25b9ff" stop-opacity="0"/>
                            </radialGradient>
                            <path
                                id="cloud-route-path"
                                d="M 1060 545 L 1108 573 Q 1120 580 1108 588 L 879 750 Q 865 760 852 751 L 750 682"
                            />
                            <path
                                id="left-route-upper-path"
                                d="M 212 650 L 350 568"
                            />
                            <path
                                id="left-route-lower-path"
                                d="M 232 670 L 329 737 Q 340 745 351 738 L 365 730 L 400 708"
                            />
                            <path
                                id="left-route-orb-path"
                                d="M 400 708 L 365 730 L 351 738 Q 340 745 329 737 L 232 670 L 212 650 L 350 568"
                            />
                            <path
                                id="bottom-chip-route-path"
                                d="M 440 724 L 397 749 Q 385 755 398 762 L 605 885"
                            />
                            <path
                                id="bottom-chip-orb-path"
                                d="M 625 885 L 398 762 Q 385 755 397 749 L 445 724"
                            />
                          </defs>
                          <image :href="item.image" x="0" y="0" width="1305" height="1206"/>

                          <g class="qdata-scene__left-routes">
                            <use href="#left-route-upper-path"/>
                            <use href="#left-route-lower-path"/>
                            <use href="#bottom-chip-route-path"/>
                          </g>

                          <use
                              class="qdata-scene__cloud-route"
                              href="#cloud-route-path"
                          />

                          <g class="qdata-scene__route-orb">
                            <circle class="qdata-scene__route-orb-halo" r="9"/>
                            <circle class="qdata-scene__route-orb-core" r="3"/>
                            <animateMotion
                                dur="4.8s"
                                repeatCount="indefinite"
                            >
                              <mpath href="#cloud-route-path"/>
                            </animateMotion>
                            <animate
                                attributeName="opacity"
                                values="0;1;1;0"
                                keyTimes="0;0.08;0.9;1"
                                dur="4.8s"
                                repeatCount="indefinite"
                            />
                          </g>

                          <g class="qdata-scene__route-orb qdata-scene__left-route-orb">
                            <circle class="qdata-scene__route-orb-halo" r="9"/>
                            <circle class="qdata-scene__route-orb-core" r="3"/>
                            <animateMotion dur="5.2s" repeatCount="indefinite">
                              <mpath href="#left-route-orb-path"/>
                            </animateMotion>
                            <animate
                                attributeName="opacity"
                                values="0;1;1;0"
                                keyTimes="0;0.08;0.9;1"
                                dur="5.2s"
                                repeatCount="indefinite"
                            />
                          </g>

                          <g class="qdata-scene__route-orb qdata-scene__bottom-route-orb">
                            <circle class="qdata-scene__route-orb-halo" r="9"/>
                            <circle class="qdata-scene__route-orb-core" r="3"/>
                            <animateMotion dur="4s" repeatCount="indefinite">
                              <mpath href="#bottom-chip-orb-path"/>
                            </animateMotion>
                            <animate
                                attributeName="opacity"
                                values="0;1;1;0"
                                keyTimes="0;0.08;0.9;1"
                                dur="4.6s"
                                repeatCount="indefinite"
                            />
                          </g>

                          <image
                              v-for="sceneItem in sceneItems"
                              :key="sceneItem.name"
                              :href="sceneItem.src"
                              :x="sceneItem.x"
                              :y="sceneItem.y"
                              :width="sceneItem.width"
                              :height="sceneItem.height"
                              :clip-path="sceneItem.clipPath"
                              :class="['qdata-scene__item', `qdata-scene__item--${sceneItem.motion}`]"
                              :style="{
                                            '--float-duration': sceneItem.duration,
                                            '--float-delay': sceneItem.delay
                                        }"
                          />
                        </svg>
                      </div>
                      <div v-else class="swiper-slide" :style="getBackgroundStyle(item)"></div>
                        </el-carousel-item>
                    </el-carousel>
                </div>
                <div class="swiper-pagination"></div>
            </div>
        </div>
        <div class="right-content">
            <div class="logo">
              <img :src="logo" />
              <div ref="target" :class="['language', langOpen ? 'language-open' : 'language-close']"
                   @click="langOpen = !langOpen"
              >
                <img class="language-icon" src="@/assets/images/system/login/icon-language.svg" alt="" />
                <div class="language-name">{{ currentLocaleName }}</div>
                <el-icon class="language-switch">
                  <ArrowRight />
                </el-icon>

                <div class="language-list">
                  <div
                    v-for="item in localeMap"
                    :key="item.lang"
                    class="lang-item"
                    @click.stop="handleLangClick(item.lang)"
                  >{{ item.name }}</div>
                </div>
              </div>
            </div>
          <div>
                <div class="greeting">
                    <div class="entry_period">{{ td('login.greeting.greetingHello') }}，{{ greeting }}！</div>
                    <div class="entry_greeting">{{ td('login.greeting.greetingMessage') }}</div>
                </div>
                <div class="login-panel">
                    <el-form ref="loginRef" :model="loginForm" :rules="loginRules">
                        <p class="titles">{{ td('login.title') }}</p>
                        <div class="titles-bar"></div>
                        <el-form-item prop="username">
                            <el-input v-model="loginForm.username" type="text" auto-complete="off" :placeholder="td('login.username')">
                                <template #prefix>
                                    <i class="iconfont">&#xebc0;</i>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password">
                            <el-input v-model="loginForm.password" type="password" auto-complete="off" :placeholder="td('login.password')"
                                @keyup.enter="handleLogin">
                                <template #prefix>
                                    <i class="iconfont">&#xeb8d;</i>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="code" v-if="captchaEnabled">
                            <el-input v-model="loginForm.code" auto-complete="off" :placeholder="td('login.code')" class="code-class"
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
                                <span v-if="!loading">{{ td('login.loginBtn') }}</span>
                                <span v-else>{{ td('login.logining') }}</span>
                            </el-button>
                        </el-form-item>

                        <div class="form-actions">
                            <el-checkbox v-model="loginForm.rememberMe">{{ td('login.remember') }}</el-checkbox>
                            <el-text type="primary" @click="dialogVisible = true">{{ td('login.forgetPassword') }}</el-text>
                        </div>
                    </el-form>
                </div>
            </div>
            <div>
                <div class="description">
                    <div class="contact" style="float: left">
                        <img src="@/assets/images/system/login/form-phone.png" />
                        <div>
                            <p>{{ td('login.info.phoneContact') }}</p>
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
                        <img src="@/assets/images/system/login/form-email.png" />
                        <div>
                            <p>{{ td('login.info.emailContact') }}</p>
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
                    <img src="@/assets/images/system/login/img-goge-icon.png" style="height: 20px" />
                    <span style="color: #888; font-size: 12px; line-height: 0; margin-left: 10px">{{ td('login.info.chromeHint') }}</span>
                    <span style="color: #ee2223; font-size: 12px; line-height: 0">Chrome</span>
                    <span style="color: #888; font-size: 12px; line-height: 0">{{ td('login.info.chromeHint2') }}</span>
                    <a href="https://www.google.cn/chrome/" target="_blank">
                        <div style="
                                margin-left: 15px;
                                display: flex;
                                flex-direction: column;
                                align-items: center;
                            ">
                            <img id="window_img" src="@/assets/images/system/login/icon-window.svg"
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
                            <img id="mac_img" src="@/assets/images/system/login/icon-mac.svg" style="height: 25px" />
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
                            <span style="color: #2666fb">{{ td('login.info.companyName') }}</span>
                            {{ td('login.info.copyrightOwner') }}
                        </a>
                    </div>

                    <div class="record" @click="goKtPage()">
                        <img src="https://www.asktempo.com/statics/images/an.png" alt="" />
                        <!--            Su ICP No. 2022008519-1-->
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

    <el-dialog v-model="dialogVisible" :title="td('login.forgetPassword')" class="fp-form-dialog" width="650px"
        :append-to="$refs['app-container']" draggable destroy-on-close>
        <el-form :model="fpForm" label-width="auto" style="padding: 10px 60px 0" :label-position="labelPosition">
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="td('login.username')" :label-position="labelPosition">
                        <el-input v-model="fpForm.name" :placeholder="td('login.usernameInputPlaceholder')" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item :label="td('login.code')" :label-position="labelPosition">
                        <div class="wrapper">
                            <el-input v-model="fpForm.code" :placeholder="td('login.codePlaceholder')" />
                            <el-button type="primary" :disabled="codeFlag" style="margin-left: 10px"
                                @click="handleFPCodeClick">{{ codeFlag ? `${codeTime}s` : td('login.getCode') }}</el-button>
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item :label="td('login.newPassword')" :label-position="labelPosition">
                        <el-input v-model="fpForm.password" :placeholder="td('login.newPasswordPlaceholder')" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item :label="td('login.confirmPassword')" :label-position="labelPosition">
                        <el-input v-model="fpForm.password2" :placeholder="td('login.confirmPasswordPlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="dialogVisible = false"> {{ td('login.resetPassword') }} </el-button>
            </div>
        </template>
    </el-dialog>
    <!-- </div> -->
</template>

<script setup>
import { ref, computed } from 'vue';
import useDefaultLang from "@/composables/useDefaultLang";
import { getCodeImg } from '@/api/system/login';
import Cookies from 'js-cookie';
import { encrypt, decrypt } from '@/utils/jsencrypt';
import Swiper from 'swiper';
import 'swiper/swiper-bundle.min.css';
import useUserStore from '@/store/system/user.js';
import useLocaleStore from '@/store/system/locale.js';
import { useLocale } from '@/composables/useLocale';
import { useLocaleImage } from '@/composables/useLocaleImage';
import { useTimeGreeting } from '@/composables/useTimeGreeting';
import { getContent } from '@/api/system/system/content';
import defaultLogo from '@/assets/images/system/login/logo-qdata.png';
import analyticsCard from '@/assets/images/system/login/qdata-scene/analytics-card.png';
import skylineCard from '@/assets/images/system/login/qdata-scene/data-skyline-card.png';
import codeCard from '@/assets/images/system/login/qdata-scene/code-card.png';
import cloudCard from '@/assets/images/system/login/qdata-scene/cloud-card.png';
import largeCloud from '@/assets/images/system/login/qdata-scene/large-cloud.png';
import topChip from '@/assets/images/system/login/qdata-scene/chip-bottom.png';
import bottomChip from '@/assets/images/system/login/qdata-scene/chip-bottom.png';
import leftNode from '@/assets/images/system/login/qdata-scene/network-node-left.png';
import lowerLeftNode from '@/assets/images/system/login/qdata-scene/network-node-lower-left.png';
import rightNode from '@/assets/images/system/login/qdata-scene/network-node-right.png';
import opennessIcon from '@/assets/images/system/login/qdata-scene/openness-shield-no-border.png';
import governanceIcon from '@/assets/images/system/login/qdata-scene/governance-network-no-border.png';
import valueIcon from '@/assets/images/system/login/qdata-scene/value-bars-no-border.png';

const { td } = useDefaultLang();
const { changeLocale } = useLocale();
const { getImage } = useLocaleImage();
const { greeting, message } = useTimeGreeting();
const userStore = useUserStore();
const localeStore = useLocaleStore();
const router = useRouter();
const dialogVisible = ref(false);
const { proxy } = getCurrentInstance();
const loading = ref(false);
const codeUrl = ref('');

const langOpen = ref(false);
const localeMap = computed(() => localeStore.getLocaleMap);
const currentLocaleName = computed(() => {
  const lang = localeStore.getCurrentLocale.lang;
  return localeMap.value.find((v) => v.lang === lang)?.name || lang;
});

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
const sceneItems = [
    { name: 'analytics', src: analyticsCard, x: 240, y: 462, width: 132, height: 177, motion: 'a', duration: '7.9s', delay: '-2.4s' },
    { name: 'skyline', src: skylineCard, x: 355, y: 345, width: 93, height: 216, motion: 'b', duration: '9.7s', delay: '-5.1s' },
    { name: 'top-chip', src: topChip, x: 760, y: 345, width: 179, height: 135, motion: 'f', duration: '8.9s', delay: '-4.6s' },
    { name: 'code', src: codeCard, x: 701, y: 385, width: 102, height: 145, motion: 'c', duration: '8.6s', delay: '-1.3s' },
    { name: 'cloud-card', src: cloudCard, x: 805, y: 455, width: 117, height: 162, motion: 'd', duration: '10.8s', delay: '-6.7s' },
    { name: 'large-cloud', src: largeCloud, x: 909, y: 365, width: 226, height: 243, motion: 'e', duration: '11.9s', delay: '-3.8s' },
    { name: 'bottom-chip', src: bottomChip, x: 454, y: 775, width: 215, height: 181, motion: 'c', duration: '12.7s', delay: '-8.2s' },
    { name: 'left-node', src: leftNode, x: 180, y: 600, width: 94, height: 113, motion: 'static', duration: '0s', delay: '0s' },
    { name: 'lower-left-node', src: lowerLeftNode, x: 285, y: 620, width: 91, height: 131, motion: 'static', duration: '0s', delay: '0s' },
    { name: 'right-node', src: rightNode, x: 800, y: 684, width: 131, height: 148, motion: 'static', duration: '0s', delay: '0s' },
    { name: 'openness', src: opennessIcon, x: 138, y: 992, width: 90, height: 93, motion: 'vertical-a', duration: '9.8s', delay: '-3.3s' },
    { name: 'governance', src: governanceIcon, x: 505, y: 989, width: 90, height: 100, motion: 'vertical-b', duration: '11.4s', delay: '-6.1s' },
    { name: 'value', src: valueIcon, x: 852, y: 984, width: 120, height: 100, motion: 'vertical-c', duration: '6.3s', delay: '-10.2s' }
];

const defaltImglist = ref([
    { id: 1, image: getImage('login/banner-zt.png'), animated: true },
]);
const loginimglist = ref([]);

const getBackgroundStyle = (item) => {
    return {
        background: `url(${item.image}) center center / cover no-repeat`,
    };
};

const getAssetsFile = (url) => {
    return new URL(`@/assets/images/system/login/${url}`, import.meta.url).href;
};

const loginRules = computed(() => ({
    username: [{ required: true, trigger: 'blur', message: td('login.usernameRequired') }],
    password: [{ required: true, trigger: 'blur', message: td('login.passwordRequired') }],
    code: [{ required: true, trigger: 'change', message: td('login.codeRequired') }]
}));

const logo = ref(null);
const contentDetail = ref(null);

onMounted(() => {
    fetchContent();
});
const fetchContent = async () => {
    try {
        const res = await getContent(1);
        if (res.code == 200) {
            const data = res.data;
            contentDetail.value = data;
            const sysLogo = data.loginLogo;
            logo.value = sysLogo ? sysLogo : defaultLogo;
            const carouselImageList = data.carouselImage ? data.carouselImage.split(',') : [];
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
            if (carouselImgList.length > 0) {
                loginimglist.value = carouselImgList;
            } else {
                loginimglist.value = defaltImglist.value;
            }
        } else {
            loginimglist.value = defaltImglist.value;
        }
    } catch (error) {
        logo.value = defaultLogo;
        loginimglist.value = defaltImglist.value;
    }
};

// Switch language: update store + load language pack + refresh page
async function handleLangClick(lang) {
  langOpen.value = false;
  if (lang === localeStore.getCurrentLocale.lang) return;
  localeStore.setCurrentLocale({ lang });
  await changeLocale(lang);
  window.location.reload();
}

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
            // Check the Require to remember password setting and set it in cookies to remember username and password.
            if (loginForm.value.rememberMe) {
                Cookies.set('username', loginForm.value.username, { expires: 30 });
                Cookies.set('password', encrypt(loginForm.value.password), { expires: 30 });
                Cookies.set('rememberMe', loginForm.value.rememberMe, { expires: 30 });
            } else {
                // Otherwise remove
                Cookies.remove('username');
                Cookies.remove('password');
                Cookies.remove('rememberMe');
            }
            // Call the login method of action
            userStore
                .login(loginForm.value)
                .then(() => {
                    window.location.href = '/index';
                })
                .catch(() => {
                    loading.value = false;
                    // Get verification code again
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
//Click on the registration number to adjust the Ministry of Industry and Information Technology
function goKtPage() {
    // Open link in new window
    window.open('https://beian.miit.gov.cn/#/Integrated/index', '_blank'); // Open link in new window
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
.language {
  position: relative;
  display: flex;
  align-items: center;
  font-family: PingFang SC;
  color: #666666;
  cursor: pointer;
  user-select: none;
  font-weight: 600;
  margin-left: auto;
  margin-right: 4%;

  .language-icon {
    display: block;
    width: 16px;
    height: 16px;
    margin-right: 6px;
  }

  .language-name {
    font-size: 15px;
    margin-right: 8px;
  }

  ::v-deep(.language-switch) {
    display: block;
    transition: all 0.3s ease-in-out;
  }

  .language-list {
    display: none;
    background: #ffffff;
    box-shadow: 0px 5px 10px 0px rgba(51, 51, 51, 0.2);
    border-radius: 8px;
    padding: 8px 0 9px;
    position: absolute;
    top: calc(100% + 10px);
    right: 0;
    font-weight: 500;

    .lang-item {
      width: 100%;
      height: 31px;
      font-weight: 400;
      font-size: 15px;
      color: #333333;
      line-height: 31px;
      padding: 0 27px;
      white-space: nowrap;

      &:hover {
        font-weight: 500;
        color: var(--login-primary-color);
        background-color: rgba($color: #eff6fe, $alpha: 0.5);
      }
    }
  }

  &.language-open {
    ::v-deep(.language-switch) {
      transform: rotate(90deg);
    }

    .language-list {
      display: block;
    }
  }
}

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
    background: url(@/assets/images/system/login/banner.png) center center / cover no-repeat;
  }
  .swiper-slide-2{
    background: url(@/assets/images/system/login/banner2.png) center center / cover no-repeat;
  }
  .swiper-slide-3{
    background: url(@/assets/images/system/login/banner-gy.jpg) center center / cover no-repeat;
  }
  .swiper-slide-4{
    background: url(@/assets/images/system/login/banner-sl.png) center center / cover no-repeat;
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

            .swiper-wrapper,
            .login-carousel,
            .swiper-slide {
                width: 100%;
                height: 100%;
            }

            ::v-deep(.el-carousel__container),
            ::v-deep(.el-carousel__item) {
                height: 100%;
            }

            .qdata-scene {
                position: relative;
                overflow: hidden;
                background: #eaf3ff;
            }

            .qdata-scene__canvas {
                display: block;
                width: 100%;
                height: 100%;
            }

            .qdata-scene__cloud-route {
                fill: none;
                stroke: rgba(70, 196, 255, 0.68);
                stroke-width: 2.2;
                stroke-linecap: round;
                stroke-linejoin: round;
                stroke-dasharray: 1 5;
                filter: drop-shadow(0 0 1px rgba(60, 190, 255, 0.2));
            }

            .qdata-scene__left-routes {
                fill: none;
                stroke: rgba(70, 196, 255, 0.68);
                stroke-width: 2.2;
                stroke-linecap: round;
                stroke-linejoin: round;
                stroke-dasharray: 1 5;
                filter: drop-shadow(0 0 1px rgba(60, 190, 255, 0.2));
            }

            .qdata-scene__route-orb {
                pointer-events: none;
                filter: drop-shadow(0 0 5px rgba(65, 205, 255, 0.95));
            }

            .qdata-scene__route-orb-halo {
                fill: url(#cloud-route-orb);
            }

            .qdata-scene__route-orb-core {
                fill: #ffffff;
                stroke: rgba(151, 235, 255, 0.95);
                stroke-width: 1.5;
            }

            .qdata-scene__item {
                pointer-events: none;
                transform-box: fill-box;
                transform-origin: center;
                will-change: transform;
                animation-duration: var(--float-duration);
                animation-delay: var(--float-delay);
                animation-timing-function: cubic-bezier(0.45, 0.05, 0.55, 0.95);
                animation-iteration-count: infinite;
                animation-direction: alternate;
            }

            .qdata-scene__item--a { animation-name: qdata-float-a; }
            .qdata-scene__item--b { animation-name: qdata-float-b; }
            .qdata-scene__item--c { animation-name: qdata-float-c; }
            .qdata-scene__item--d { animation-name: qdata-float-d; }
            .qdata-scene__item--e { animation-name: qdata-float-e; }
            .qdata-scene__item--f { animation-name: qdata-float-f; }
            .qdata-scene__item--vertical-a { animation-name: qdata-float-vertical-a; }
            .qdata-scene__item--vertical-b { animation-name: qdata-float-vertical-b; }
            .qdata-scene__item--vertical-c { animation-name: qdata-float-vertical-c; }

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
            //width: 100px;
            height: 33px;
            margin-left: 20%;
            display: flex;

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

@keyframes qdata-float-a {
    0% { transform: translateY(0); }
    19% { transform: translateY(-7px); }
    47% { transform: translateY(-2px); }
    73% { transform: translateY(5px); }
    100% { transform: translateY(-5px); }
}

@keyframes qdata-float-b {
    0% { transform: translateY(0); }
    23% { transform: translateY(3px); }
    51% { transform: translateY(-6px); }
    78% { transform: translateY(2px); }
    100% { transform: translateY(6px); }
}

@keyframes qdata-float-c {
    0% { transform: translateY(0); }
    17% { transform: translateY(6px); }
    42% { transform: translateY(-3px); }
    69% { transform: translateY(-7px); }
    100% { transform: translateY(3px); }
}

@keyframes qdata-float-d {
    0% { transform: translateY(0); }
    28% { transform: translateY(-5px); }
    55% { transform: translateY(-1px); }
    81% { transform: translateY(6px); }
    100% { transform: translateY(2px); }
}

@keyframes qdata-float-e {
    0% { transform: translateY(0); }
    21% { transform: translateY(4px); }
    49% { transform: translateY(7px); }
    76% { transform: translateY(-4px); }
    100% { transform: translateY(-6px); }
}

@keyframes qdata-float-f {
    0% { transform: translateY(0); }
    16% { transform: translateY(5px); }
    38% { transform: translateY(-2px); }
    67% { transform: translateY(-6px); }
    86% { transform: translateY(3px); }
    100% { transform: translateY(6px); }
}

@keyframes qdata-float-vertical-a {
    0% { transform: translateY(0); }
    18% { transform: translateY(-3px); }
    43% { transform: translateY(2px); }
    71% { transform: translateY(-4px); }
    100% { transform: translateY(1px); }
}

@keyframes qdata-float-vertical-b {
    0% { transform: translateY(0); }
    24% { transform: translateY(3px); }
    52% { transform: translateY(-4px); }
    79% { transform: translateY(1px); }
    100% { transform: translateY(-4px); }
}

@keyframes qdata-float-vertical-c {
    0% { transform: translateY(0); }
    16% { transform: translateY(-1px); }
    39% { transform: translateY(3px); }
    68% { transform: translateY(-4px); }
    87% { transform: translateY(1px); }
    100% { transform: translateY(-2px); }
}

@media (prefers-reduced-motion: reduce) {
    .login-two .qdata-scene__item {
        animation: none;
    }

    .login-two .qdata-scene__route-orb {
        display: none;
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
