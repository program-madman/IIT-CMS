import axios from 'axios';
//import store from '@/store';
import { showLoginAlert } from "@/utils/snackbar.js";
//import { Debounce } from "@/utils/tools.js";
//import { isNull} from "@/utils/common.js"

//import {baseUrl} from './env';

//  让请求在浏览器中允许跨域携带cookie
//axios.defaults.withCredentials = true;
// axios.defaults.post['Content-type'] = 'application/json' // 请求头对应数据格式


const instance = axios.create({
    // baseUrl
    baseURL: process.env.VUE_APP_BASE_API,
    // 超时
    timeout: 20000,

})


instance.interceptors.request.use(function (config) {
    console.debug('请求参数：', config);
    console.log("#### request url is ##### "+config.url)
   
    return config;
}, error => {
    return Promise.reject(error);
});

//返回拦截
instance.interceptors.response.use(response => {
    // 200正确，返回结果 
    console.debug(response.data)
    console.debug("请求结果:\n"+JSON.stringify(response.data));
    console.debug("on response url ===> [["+JSON.stringify(response.config.url)+"] server headers ==> "+JSON.stringify(response.headers))
    console.debug("on response url ===> [["+JSON.stringify(response.config.url)+"] server ticket ==> "+JSON.stringify(response.headers['ticket']))
    console.debug("on response url ===> [["+JSON.stringify(response.config.url)+"] server exp_time ==> "+JSON.stringify(response.headers['ticket_exp']))
    console.debug("## on server response ## ",response.config)
    if (response.status === 200) {
        return Promise.resolve(response.data);
    } else {
        return Promise.reject(response);
    }
}, error => {
    //超时处理  
    const { message } = error;
    console.debug("=====request error==>"+JSON.stringify(error.response))
    if(axios.isCancel(error)){
        console.log("用户取消网络请求");
        return;
    }
    if (error.code === 'ECONNABORTED' && message.indexOf('timeout') > -1) {
        console.error("请求超时！请检查网络问题");
        return;
    }

    if (
        400 <= error.response.status < 500
    ) {
        let code = error.response.data.code;
        console.error("服务器错误，code ：" + error.response.status);
        console.error("业务错误，code ：" + code);
        console.error("on error =======> "+JSON.stringify(error.response.headers))

        if(code === 3007 || code === 4002 || code === 3005 || code === 41 || code === 40) {
            console.log("===> on token expire <=== ")
            showLoginAlert()
        } 
        return Promise.reject(error)
      
    } else {
        if (error.response.status >= 500) {
            console.debug("服务器错误，code ： " + error.response.status);
        } else {
            console.debug("服务器错误，code ： " + error.response.status);
            return Promise.reject(error)
        }
    }
});

export default instance;


// const refreshTicket = Debounce(function () {
//     let expireTime = store.state.user.expireTime
//     if(typeof expireTime === 'undefined' || expireTime === null || expireTime === 0 || expireTime === "") {
//         console.log("[[[[the expire time is null]]]]")   
//         return;
//     }
//     //剩余10分钟过期的时候，刷新ticket
//     if(expireTime <= 1000 * 60 * 10) {
//         console.log("we need refresh the ticket expire time ====> ["+expireTime+"]")   
//         refreshTicketIfNeed().then((resp) => {
//             if(isNull(resp) || resp.code != 0 || isNull(resp.data)) {
//                 console.error("[[[refresh]]] error]]]")
//                 return;
//             }
//             let ticket = resp.data.value
//             if(typeof ticket !== "undefined" && ticket !== null && ticket !== "") {
//                 console.log("[[[[ refresh ticket is ====> [[[["+ticket+"]]]]]") 
//                 store.commit("user/setTicket",ticket); 
//             }
//         })
//     }else {
//      console.log("[[[[The refresh time enough. ignore it ==> ["+expireTime+"]]]]]")   
//     } 
//   }, 5000)

