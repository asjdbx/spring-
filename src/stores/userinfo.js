/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-04 16:15:27
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2025-07-13 18:52:49
 * @FilePath: \blong\src\stores\userinfo.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import { defineStore } from "pinia";
import {ref} from 'vue'

//存储用户的基本信息（如用户名、ID、头像等
const useUserStore = defineStore('userInfo',()=>{ 
    const info=ref({})

    const infos=ref({    
        todayReading:0
    })
    
    const setInfo = (newInfo)=>{
        info.value = newInfo
    }

    const updateTodayReading = (value) => {
    infos.value.todayReading = Number(value) || 0
  }
    const removeInfo = ()=>{    
        info.value = {}
    }
    return {
        info,infos,setInfo,removeInfo,updateTodayReading
    }
    
},{
    persist:true
});
export default useUserStore;