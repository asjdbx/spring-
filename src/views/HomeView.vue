<!--
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-02 17:38:02
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2026-05-08 16:00:59
 * @FilePath: \blong\src\views\HomeView.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="home-container">
    <!-- 添加半透明黑色遮罩 -->
    <div class="overlay"></div>
    <el-container class="layout-container">
      <!-- 左侧菜单 -->
      <el-aside :width="isCollapse ? '64px' : '200px'" class="aside">
        <!-- 顶部Logo -->
        <div class="logo-container">
          <span class="logo-text" v-show="!isCollapse">博客管理</span>
          <span class="logo-mini" v-show="isCollapse">博</span>
        </div>
        
        <!-- 菜单 -->
        <el-menu
          :default-active="activeMenu"
          class="menu"
          :collapse="isCollapse"
          background-color="transparent"
          text-color="rgba(200, 200, 200, 0.8)"
          active-text-color="#10b981"
          router
        >
          <!-- 文章分类 -->
          <el-menu-item index="/art/Article" >
            <el-icon><Collection /></el-icon>
            <template #title>文章分类</template>
          </el-menu-item>

          <!-- 文章信息 -->
           <el-sub-menu index="/art">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>文章信息</span>
            </template>
            <el-menu-item index="/art/ArticleAll">
             <el-icon><Document /></el-icon>
              <span>全部文章</span>
            </el-menu-item>
            <el-menu-item index="/art/ArticMa">
              <el-icon><List /></el-icon>
              <span>文章列表</span>
            </el-menu-item>
          </el-sub-menu>
         
            <!-- 其他 -->
           <el-sub-menu index="/other">
            <template #title>
              <el-icon><Tools /></el-icon>
              <span>其他</span>
            </template>
            <el-menu-item index="/other/Static" ">
             <el-icon><Histogram /></el-icon>
              <span>阅读量</span>
            </el-menu-item>
            <el-menu-item index="/other/poular">
              <el-icon><List /></el-icon>
              <span>热门文章</span>
            </el-menu-item>
            <el-menu-item index="/other/histrory">
              <el-icon><List /></el-icon>
              <span>阅读记录</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 用户信息 -->
          <el-sub-menu index="/user">
            <template #title>
              <el-icon><User /></el-icon>
              <span>个人中心</span>
            </template>
            <el-menu-item index="/user/UserInfor">
            <el-icon><UserFilled /></el-icon>
              <span>修改用户</span>
            </el-menu-item>
            <el-menu-item index="/user/userPassword">
              <el-icon><Edit /></el-icon>
              <span>修改密码</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 管理员信息 -->
           <el-sub-menu index="/admi"v-if="userStore.info.role === '2'">
            <template #title>
              <el-icon><SetUp /></el-icon>
              <span>设置</span>
            </template>
            <el-menu-item index="/admi/userdele">
             <el-icon><Avatar /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/admi/Review">
              <el-icon><List /></el-icon>
              <span>文章管理</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 关于我们 -->
          <el-menu-item index="/about">
            <el-icon><InfoFilled /></el-icon>
            <template #title>关于我们</template>
          </el-menu-item>
        </el-menu>

        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="toggleCollapse">
          <el-icon :size="20">
            <component :is="isCollapse ? 'Expand' : 'Fold'" />
          </el-icon>
        </div>
      </el-aside>

      <!-- 右侧内容区 -->
      <el-container>
        <!-- 顶部栏 -->
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb>
              <el-breadcrumb-item>首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ activeMenuTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <el-dropdown trigger="click" @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="32" :src="userStore.info && userStore.info.userPic ? userStore.info.userPic : userAvatar" />
                <span class="username">{{ userStore.info.role=='1'?'普通用户':'管理员'}}:{{ userStore.info.username }}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="UserInfor">
                    <el-icon><Picture /></el-icon>
                    <span>个人信息</span>
                  </el-dropdown-item>
                  <el-dropdown-item command="userPassword">
                    <el-icon><Edit /></el-icon>
                    <span>修改密码</span>
                  </el-dropdown-item>
                  <el-dropdown-item divided command="loginOut">
                    <el-icon><SwitchButton /></el-icon>
                    <span>退出登录</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 主要内容区 -->
        <el-main class="main">
          <router-view></router-view>
        </el-main>
        
        <!-- 底部信息 -->
        <el-footer class="footer">
          <div class="footer-content">
            © {{ new Date().getFullYear() }} 博客管理系统 - 版权所有
          </div>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  Collection,SetUp,UserFilled,
  Avatar,
  Document,
  User,
  InfoFilled,
  List,
  Lock,
  SwitchButton,
  Picture,
  Tools,
  Histogram,
  Edit
} from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import {userInfoService} from '@/api/auth.js'
import useUserStore from '@/stores/userinfo.js'
import {useTokenStore} from '@/stores/token.js'
const token = useTokenStore();
const userStore = useUserStore();
console.log(userStore.info)
const getUserInfo = async () => {
  let result = await userInfoService()
  userStore.setInfo(result.data);
}
getUserInfo();
// 路由实例
const router = useRouter()
const route = useRoute()

// 菜单折叠状态
const isCollapse = ref(false)

// 用户信息
 const userAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1e.png')

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 当前菜单标题
const activeMenuTitle = computed(() => {
  const path = route.path
  const menuMap = {
    '/art/ArticMa': '文章列表',
    '/user/UserInfor': '用户信息',
    '/user/userPassword': '修改密码',
    '/art/ArticleAll': '文章详情',
    '/art/Article': '文章分类',
    '/admi/Review': '文章审核',
    '/admi/userdele': '用户管理',
    '/other/Static':'文章统计',
    '/other/poular': '热门文章',
    '/about': '关于我们'
  }
  return menuMap[path] || '文章分类'
})

// 切换菜单折叠状态
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand=(command) => {
  if (command === 'loginOut') {
    // 弹出确认对话框
  ElMessageBox.confirm(
    '确定要退出登录？',  // 对话框内容（提示文本）
    '提示',  // 对话框标题
    { 
      confirmButtonText: '确定',  // 确认按钮文字
      cancelButtonText: '取消',  // 取消按钮文字
      type: 'warning'  // 对话框类型（warning 会显示警告图标）
    }
  ).then(async () => { //当用户点击 确认 按钮对话框时触发。
    //退出登录
    //删除pinia中的用户信息和token信息
    token.removeToken();
    userStore.removeInfo();

    //跳转到登录页面
    router.push('/login')

    ElMessage({
      type: 'success',
      message: '退出成功',
    })
    
}).catch(() => { //当用户点击 “取消” 按钮，或关闭对话框时触发。
    ElMessage({
      type: 'info',
      message: '用户取消了操作'
    }) 
  })
  }else{
    router.push('/user/'+command);
  }
}


// 处理退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确认退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    router.push('/login')
  })
}
</script>

<style scoped>
.home-container {
  height: 100vh;
  /* background: linear-gradient(365deg, #7372a5 0%, #02c480 100%); */
  background: rgb(16 25 38);
  position: relative;
}

.overlay {
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.2);
  pointer-events: none;
  z-index: 1;
}

.layout-container {
  height: 100%;
  position: relative;
  z-index: 2;
}

.aside {
  background: rgb(16 25 38);
  border-right: 1px solid rgba(16, 185, 129, 0.2);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(16, 185, 129, 0.2);
}

.logo-text, .logo-mini {
  color: #10b981;
  font-size: 20px;
  font-weight: bold;
  text-shadow: 0 0 10px rgba(16, 185, 129, 0.3);
}

.menu { border: none; }
.collapse-btn {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: rgba(140, 241, 208, 0.8);
  background: rgba(16, 185, 129, 0.1);
  border-radius: 50%;
  transition: all 0.3s;
  box-shadow: 0 0 10px rgba(16, 185, 129, 0.1);
  z-index: 10;
}

.collapse-btn:hover {
  background: rgba(16, 185, 129, 0.2);
  color: #10b981;
  transform: translateX(-50%) scale(1.05);
}

.collapse-btn:active {
  transform: translateX(-50%) scale(0.95);
}

/* 顶部栏样式 */
.header {
  background: #ffffff; /* 白灰色背景 */
  border-bottom: 1px solid rgba(16, 185, 129, 0.2);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  color: #333; /* 深色文字，确保在浅色背景上可读 */
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(16, 185, 129, 0.1);
}

.username {
  margin-left: 8px;
  color: #0c0c0c;
}

/* 主要内容区样式 */
.main {
  background: #f5f5f7; /* 白灰色背景 */
  padding: 20px;
  color: #333; /* 深色文字，确保在浅色背景上可读 */
}

/* Element Plus 组件样式 */
:deep(.el-menu) { background-color: transparent !important; }
:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) { background-color: rgba(16, 185, 129, 0.1) !important; }
:deep(.el-menu-item.is-active) { background-color: rgba(16, 185, 129, 0.2) !important; }
:deep(.el-breadcrumb__inner) { color: rgba(200, 200, 200, 0.8) !important; }
:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) { color: #10b981 !important; }
:deep(.el-dropdown-menu) {
  background: rgba(17, 25, 40, 0.75) !important;
  border: 1px solid rgba(16, 185, 129, 0.2) !important;
  backdrop-filter: blur(16px);
}
:deep(.el-dropdown-menu__item) { color: rgba(112, 112, 112, 0.8) !important; }
:deep(.el-dropdown-menu__item:hover) {
  background-color: rgba(16, 185, 129, 0.1) !important;
  color: #10b981 !important;
}
:deep(.el-dropdown-menu__item i) { margin-right: 5px; }

/* 底部信息样式 */
.footer {
  background: #f5f5f7; /* 白灰色背景 */
  border-top: 1px solid rgba(16, 185, 129, 0.2);
  padding: 0;
  height: 60px !important;
}

.footer-content {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #555; /* 深色文字，确保在浅色背景上可读 */
  font-size: 15px;
  padding: 0 20px;
  transition: all 0.3s;
  background: rgba(16, 185, 129, 0.05);
}

.footer-content:hover {
  color: #333;
  background: rgba(16, 185, 129, 0.1);
}
</style>