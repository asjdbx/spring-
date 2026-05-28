/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-06-18 15:23:21
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2025-07-23 15:23:29
 * @FilePath: \vite-project\src\router.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */

import { createRouter, createWebHashHistory } from 'vue-router'
import login from './views/LoginView.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    // { path: '/', redirect: '/login' },
    { path: '/login', component: login },
    { path: '/register', component: () => import('./views/RegisterView.vue') },
    {
      path: '/',
      component: () => import('./views/HomeView.vue'),
      children: [
        { path: '/', redirect: '/art/Article'},
        { path: '/user/userPassword', component: () => import ('./views/users/UserPassword.vue') }, // 更简洁的默认重定向 
        { path: '/user/UserInfor', component: () => import('./views/users/UserInfor.vue') },
        { path: '/art/Article', component: () => import('./views/argic/Article.vue') },
        { path: '/art/ArticMa', component: () => import('./views/argic/ArtiMa.vue') },
         { path: '/art/ArticleAll', component: () => import('./views/argic/ArticleAll.vue') },
        { path: '/about', component: () => import('./views/abou.vue') },
        { path: '/admi/userdele', component: () => import('./views/admi/userDele.vue') },
        { path: '/admi/Review', component: () => import('./views/admi/Review.vue') },
        { path: '/other/Static', component: () => import('./views/other/Static.vue') },
        { path: '/other/poular', component: () => import('./views/other/Popular_Articles.vue') },
        { path: '/other/histrory', component: () => import('./views/other/History.vue') }
      ]
    }
  ]
})

export default router