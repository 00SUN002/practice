import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const request = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 30000
})

request.interceptors.request.use(config => {
    if(!(config.data instanceof FormData)){
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
    }
    let user = JSON.parse(localStorage.getItem('user') || '{}')
    config.headers['token'] = user.token
    return config
}, error => {
    return Promise.reject(error)
});

request.interceptors.response.use(
    response => {
        let res = response.data;
        if(typeof res == "string"){
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401'){
            ElMessage.error(res.msg)
            router.push('/login')
        }
        else{
            return res
        }
    },
    error => {
        if(error.response.status === 404){
            ElMessage.error("未找到请求接口")
        }
        else if(error.response.status === 500){
            ElMessage.error("系统异常，请查看后端控制台报错")
        }
        else{
            console.error(error.message)
        }
        return Promise.reject(error)
    }
)

export default request