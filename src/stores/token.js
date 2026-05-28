/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-02 17:05:09
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2025-07-13 17:58:49
 * @FilePath: \blong\src\stores\token.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
//定义store
import {defineStore} from 'pinia'
import {ref} from 'vue'
/* 
    第一个参数:名字,唯一性
    第二个参数:函数,函数的内部可以定义状态的所有内容

    返回值: 函数
*/
//存储用户登录后的 JWT Token，用于后续 API 请求的身份验证
export const useTokenStore = defineStore('token',()=>{
    //定义状态的内容

    //1.响应式变量
    const token = ref('')

    //2.定义一个函数,修改token的值
    const setToken = (newToken)=>{
        token.value = newToken
    }

    //3.函数,移除token的值
    const removeToken = ()=>{
        token.value=''
    }

    return {
        token,setToken,removeToken
    }
},{
    persist:true//持久化存储
});