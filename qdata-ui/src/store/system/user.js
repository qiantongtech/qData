/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

import { login, logout, getInfo } from '@/api/system/login.js';
import { getToken, setToken, removeToken } from '@/utils/auth';
import defAva from '@/assets/images/defaultAvatar.png';

const useUserStore = defineStore('user', {
    state: () => ({
        token: getToken(),
        id: '',
        name: '',
        nickName: '',
        phonenumber: '',
        avatar: '',
        roles: [],
        permissions: [],
        projectId: ''
    }),
    actions: {
        // 登录
        login(userInfo) {
            const username = userInfo.username.trim();
            const password = userInfo.password;
            const code = userInfo.code;
            const uuid = userInfo.uuid;
            return new Promise((resolve, reject) => {
                login(username, password, code, uuid)
                    .then((res) => {
                        setToken(res.token);
                        this.token = res.token;
                        resolve();
                    })
                    .catch((error) => {
                        reject(error);
                    });
            });
        },
        // 获取用户信息
        getInfo() {
            return new Promise((resolve, reject) => {
                getInfo()
                    .then((res) => {
                        const user = res.user;
                        const avatar =
                            user.avatar == '' || user.avatar == null
                                ? defAva
                                : import.meta.env.VITE_APP_BASE_API + user.avatar;

                        if (res.roles && res.roles.length > 0) {
                            this.roles = res.roles;
                            this.permissions = res.permissions;
                        } else {
                            this.roles = ['ROLE_DEFAULT'];
                        }
                        this.id = user.userId;
                        this.name = user.userName;
                        this.avatar = avatar;
                        this.nickName = user.nickName;
                        this.phonenumber = user.phonenumber;

                        resolve(res);
                    })
                    .catch((error) => {
                        reject(error);
                    });
            });
        },
        // 退出系统
        logOut() {
            return new Promise((resolve, reject) => {
                logout(this.token)
                    .then(() => {
                        this.token = '';
                        this.roles = [];
                        this.permissions = [];
                        removeToken();
                        resolve();
                    })
                    .catch((error) => {
                        reject(error);
                    });
            });
        },
        // 设置 Token
        setToken(token) {
            return new Promise((resolve, reject) => {
                setToken(token);
                this.token = token;
                resolve();
            });
        }
    }
});

export default useUserStore;
