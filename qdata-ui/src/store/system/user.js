/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import { login, logout, getInfo } from '@/api/system/login.js';
import { getToken, setToken, removeToken } from '@/utils/auth';
import defAva from '@/assets/images/avatar-default.png';

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
        // Login
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
        // Get user information
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
        // Exit the system
        logOut() {
            return new Promise((resolve, reject) => {
                logout(this.token)
                    .then(() => {
                        this.token = '';
                        this.roles = [];
                        this.permissions = [];
                        localStorage.removeItem('qdataProjectId');
                        removeToken();
                        resolve();
                    })
                    .catch((error) => {
                        reject(error);
                    });
            });
        },
        // Set Token
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
