/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-01 22:42:52
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2025-07-13 17:36:22
 * @FilePath: \blong\src\main.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import './assets/main.css'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import {createPinia} from 'pinia'
import './assets/main.scss';
import locale from 'element-plus/dist/locale/zh-cn.js'
import { createPersistedState } from 'pinia-plugin-persistedstate'




const app = createApp(App)
const pinia = createPinia();
const persistedState = createPersistedState()
pinia.use(persistedState)
app.use(router)
app.use(ElementPlus,{locale})
app.use(pinia)
app.mount('#app')
