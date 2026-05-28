<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>修改密码</span>
      </div>
    </template>
      
      <el-form 
        :model="passwordForm" 
        :rules="rules" 
        ref="passwordFormRef" 
        label-width="100px"
        status-icon
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input 
            v-model="passwordForm.oldPassword" 
            type="password" 
            show-password
            placeholder="请输入旧密码"
          />
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input 
            v-model="passwordForm.newPassword" 
            type="password" 
            show-password
            placeholder="请输入新密码"
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="passwordForm.confirmPassword" 
            type="password" 
            show-password
            placeholder="请再次输入新密码"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm">确认修改</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  <!-- </div> -->
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { updateUserService } from  '@/api/auth.js'
import { useTokenStore } from '@/stores/token'

const router = useRouter()
const passwordFormRef = ref()
const useToken= useTokenStore();

// 表单数据
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const rules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
    { min: 6, message: '密码长度在 5 到 12 个字符', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度在 5 到 12 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 提交表单
const submitForm = async () => {
  if (!passwordFormRef.value) return
  
  try {
    // 表单验证
    await passwordFormRef.value.validate()
    
    // 调用后端API
    const response = await updateUserService({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
      newPassword2: passwordForm.confirmPassword
    }, useToken.token)  // 传递token
       console.log('响应:', response)
    // 根据后端返回结果处理
   if (response.code ==0&&response) {
      ElMessage.success('密码修改成功，请重新登录')
      useToken.removeToken;
       setTimeout(() => {
        window.location.href = '/login'
      }, 1500)  
      return  // ✅ 关键：成功这里就返回，不再往下执行
    }
    
    // ✅ 失败处理（只有 code !== 0 才会执行到这里）
    ElMessage.error(response.message || '修改密码失败，请重试')
    
  } catch (error) {
    console.error('修改密码失败:', error)
    if (error.response && error.response.data) {
      ElMessage.error(error.response.data.message || '修改失败')
    } else {
      ElMessage.error('网络错误，请重试')
    }
  }
}


// 重置表单
const resetForm = () => {
  if (!passwordFormRef.value) return
  passwordFormRef.value.resetFields()
}
</script>

<style scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-form {
  max-width: 600px;
  margin: 0 auto;
}
</style>