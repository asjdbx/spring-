/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-02 16:53:53
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2025-07-07 22:48:55
 * @FilePath: \blong\src\utils\request.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-01 22:53:11
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2025-07-02 18:07:09
 * @FilePath: \blong\src\utils\request.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-02 16:53:53
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2026-06-10 21:32:11
 * @FilePath: \blong\src\utils\request.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-01 22:53:11
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2025-07-02 18:07:09
 * @FilePath: \blong\src\utils\request.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
//定制请求的实例

//导入axios  npm install axios
import axios from 'axios';

import { ElMessage } from 'element-plus'


// 1. 创建axios实例service（缺少这行就报service未定义）
const service = axios.create({
  baseURL: '',
  timeout: 5000
})

// // 请求拦截（可选）
// service.interceptors.request.use(config => {
//   return config
// })
//定义一个变量,记录公共的前缀  ,  baseURL
//const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({ baseURL })

import {useTokenStore} from '@/stores/token.js'


//添加请求拦截器
// instance.interceptors.request.use(
//     (config)=>{
//         //请求前的回调
//         //添加token
//         const tokenStore = useTokenStore();
//         //判断有没有token
//         if(tokenStore.token){
//             config.headers.Authorization = tokenStore.token
//         }
//         return config;
//     },
//     (err)=>{
//         //请求错误的回调
//         Promise.reject(err)
//     }
// )

// import router from '@/router'
// //添加响应拦截器
// instance.interceptors.response.use(
//     result => {
//         //判断业务状态码
//         if(result.data.code===0){
//             return result.data;
//         }

//         //操作失败
//         //alert(result.data.msg?result.data.msg:'服务异常')
//         ElMessage.error(result.data.msg?result.data.msg:'错误')
//         //异步操作的状态转换为失败
//         return Promise.reject(result.data)
        
//     },
//     err => {
//         //判断响应状态码,如果为401,则证明未登录,提示请登录,并跳转到登录页面
//         if(err.response.status===401){
//             ElMessage.error('请先登录')
//             router.push('/login')
//         }else{
//             ElMessage.error('服务异常')
//         }
       
//         return Promise.reject(err);//异步的状态转化成失败的状态
//     }
// )
// // 响应错误拦截（git page只能静态页面，拦截不报错）
// service.interceptors.response.use(
//   res => res.data,
//   err => {
//     console.log('接口异常', err)
//     // ✅ 只有打包上线PROD才返回假数据、关闭弹窗
//     if (import.meta.env.PROD) {
//       return Promise.resolve({ code: 200, data: [] })
//     }
//     // ✅ 本地DEV：正常抛出错误，页面弹出服务异常（本地需要弹窗提示接口崩了）
//     return Promise.reject(err)
//   }
// )

export default instance;
