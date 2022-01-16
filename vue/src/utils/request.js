import axios from 'axios'
import router from "../router";

const request = axios.create({
    // baseURL: '/api', // 注意！這裡是全局統一加上了 '/api' 前綴，也就是說所有接口都會加上'/api'前綴在，頁面裡面寫接口的時候就不要加 '/api'了，否則會出現2個'/api'，類似 '/api/api/user'這樣的報錯，切記！
    timeout: 5000
})

// request 攔截器
// 可以自請求發送前對請求做一些處理
// 比如統一加token，對請求參數統一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    // config.headers['token'] = user.token;  // 設定請求頭
    // 取出sessionStorage取出裡面緩存的帳戶資訊
    const userStr = sessionStorage.getItem("user")
    if (!userStr) {
        router.push("/login")  // 登出後 如果沒緩存信息 到登入頁面
    }

    return config
}, error => {
    return Promise.reject(error)
});

// response 攔截器
// 可以在接口響應後統一處理結果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服務器端返回的字符串數據
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)

export default request

