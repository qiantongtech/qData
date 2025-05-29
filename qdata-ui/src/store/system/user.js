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
