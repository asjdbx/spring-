<template>
  <div class="login-container">
    <!-- 动画元素：数字雨背景 -->
    <canvas ref="digitalRain" class="digital-rain"></canvas>
    <!-- 装饰元素：科技网格背景 -->
    <div class="tech-grid"></div>
    
    <div class="login-box">
      <!-- 装饰元素：科技风格圆圈和线条 -->
      <div class="tech-circle"></div>
      <div class="tech-line"></div>
      
      <!-- 主要内容：登录表单 -->
      <div class="form-container">
        <div class="login-header">
          <div class="tech-logo">
            <span class="logo-circle"></span>
          </div>
          <h2>博客<span class="highlight">登录</span></h2>
        </div>
        
        <!-- Element Plus 表单 -->
        <el-form 
          ref="formRef"
          :model="formData"
          :rules="rules"
          @keyup.enter="handleLogin"
        >
          <!-- 用户名输入框 -->
          <el-form-item prop="username">
            <el-input
              v-model="formData.username"
              placeholder="用户名"
              :prefix-icon="User"
              class="custom-input"
            />
          </el-form-item>
          
          <!-- 密码输入框 -->
          <el-form-item prop="password">
            <el-input
              v-model="formData.password"
              type="password"
              placeholder="密码"
              :prefix-icon="Lock"
              class="custom-input"
              show-password
            />
          </el-form-item>
          
          <!-- 按钮区域 -->
          <div class="button-group">
            <el-button
              type="primary"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登录' }}
            </el-button>
            
            <router-link to="/register" class="register-link">
              <span class="register-icon">→</span> 没有账号？立即注册
            </router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useTokenStore } from '@/stores/token'
import { LoginService } from '@/api/auth.js'

// 路由实例
const router = useRouter()
// 表单引用
const formRef = ref(null)
// 加载状态
const loading = ref(false)
// 数字雨画布引用
const digitalRain = ref(null)

// 表单数据
const formData = ref({
  username: '',
  password: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 5 到 12 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 5 到 12 个字符', trigger: 'blur' }
  ]
}

// 数字雨动画间隔引用
let rainInterval = null

/**
 * 初始化数字雨动画
 * Matrix风格的下落字符效果
 */

const initRain = () => {
  const canvas = digitalRain.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  canvas.width = window.innerWidth
  canvas.height = window.innerHeight
  
  const fontSize = 14
  const columns = Math.floor(canvas.width / fontSize)
  const drops = Array(columns).fill(1)
  
  const draw = () => {
    ctx.fillStyle = 'rgba(0, 0, 0, 0.05)'
    ctx.fillRect(0, 0, canvas.width, canvas.height)
    ctx.fillStyle = '#0fa'
    ctx.font = `${fontSize}px monospace`
    
    drops.forEach((drop, i) => {
      const char = String.fromCharCode(0x30A0 + Math.random() * 96)
      ctx.fillText(char, i * fontSize, drop * fontSize)
      if (drop * fontSize > canvas.height && Math.random() > 0.975) {
        drops[i] = 0
      }
      drops[i]++
    })
  }
  
  rainInterval = setInterval(draw, 50)
}

/**
 * 处理登录逻辑
 * 包含表单验证、API调用和错误处理
 */
const token=useTokenStore();
const handleLogin = async () => {
  // 表单验证
  console.log(formData.value);
 let result = await LoginService(formData.value);
    ElMessage.success(result.message ? result.message : '登录成功')
    token.setToken(result.data);
    router.push('/')
}

onMounted(() => {
  initRain()
  window.addEventListener('resize', initRain)
})

onUnmounted(() => {
  if (rainInterval) clearInterval(rainInterval)
  window.removeEventListener('resize', initRain)
})
</script>

<style scoped>
/* 自定义 Element Plus 组件样式 */
:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.05) !important;
  box-shadow: none !important;
  border: 1px solid rgba(16, 185, 129, 0.2) !important;
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #10b981 !important;
  box-shadow: 0 0 0 2px rgba(16, 185, 129, 0.2) !important;
}

:deep(.el-input__inner) {
  color: #fff !important;
  height: 42px !important;
}

:deep(.el-input__prefix-inner) {
  color: rgba(16, 185, 129, 0.8) !important;
}

/* 容器样式 */
.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1a1f35 0%, #0d1117 100%);
  position: relative;
  overflow: hidden;
}

.digital-rain {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  opacity: 0.3;
}

.tech-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(16, 185, 129, 0.1) 1px, transparent 1px),
    linear-gradient(90deg, rgba(16, 185, 129, 0.1) 1px, transparent 1px);
  background-size: 50px 50px;
  z-index: 0;
}

.login-box {
  width: 420px;
  padding: 40px;
  background: rgba(17, 25, 40, 0.75);
  backdrop-filter: blur(16px);
  border-radius: 20px;
  border: 1px solid rgba(16, 185, 129, 0.2);
  box-shadow: 0 8px 32px rgba(16, 185, 129, 0.2);
  position: relative;
  z-index: 1;
}

.tech-circle {
  position: absolute;
  width: 150px;
  height: 150px;
  top: -75px;
  right: -75px;
  border: 2px solid rgba(16, 185, 129, 0.2);
  border-radius: 50%;
  border-top-color: rgba(16, 185, 129, 0.4);
}

.tech-line {
  position: absolute;
  width: 100px;
  height: 2px;
  bottom: 30px;
  left: -50px;
  background: linear-gradient(90deg, transparent, rgba(16, 185, 129, 0.4), transparent);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.tech-logo {
  width: 60px;
  height: 60px;
  margin: 0 auto 20px;
  position: relative;
}

.logo-circle {
  position: absolute;
  width: 100%;
  height: 100%;
  border: 3px solid rgba(16, 185, 129, 0.8);
  border-radius: 50%;
}

h2 {
  color: #fff;
  font-size: 28px;
  font-weight: 600;
  text-shadow: 0 0 10px rgba(16, 185, 129, 0.5);
}

.highlight {
  color: #10b981;
}

/* 按钮样式 */
:deep(.login-btn) {
  width: 100%;
  padding: 12px;
  background: linear-gradient(45deg, #10b981, #34d399);
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
}

:deep(.login-btn:hover) {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(16, 185, 129, 0.4);
}

:deep(.login-btn:disabled) {
  background: #4b5563;
  cursor: not-allowed;
}

.register-link {
  display: inline-flex;
  align-items: center;
  margin-top: 15px;
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  font-size: 14px;
}

.register-icon {
  margin-left: 5px;
  color: #10b981;
}

.register-link:hover {
  color: #10b981;
}
</style>