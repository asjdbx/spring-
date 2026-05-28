<template>
  <el-card class="page-container">
    <div class="top-decoration"></div>
    <template #header>
      <div class="header">
        <div class="header-left">
          <span>用户资料中心</span>
        </div>
        <el-tag size="small" effect="plain" type="info">个人信息</el-tag>
      </div>
    </template>

    <div class="profile-layout">
      <!-- 头像区域 - 左侧 -->
      <div class="avatar-section">
        <div class="user-avatar">
          <img :src="userInfo.userPic" alt="用户头像">
          <div class="avatar-edit-btn" @click="openAvatarDialog" style="margin-top: 20px;">
            修改
          </div>
        </div>
        
      </div>
      
      <!-- 用户信息表单 -->
      <div class="form-section">
        <el-form :model="userInfo" :rules="rules" label-width="100px" size="default">
          <el-form-item label="登录名称">
            <el-input v-model="userInfo.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户昵称" prop="nickname">
            <el-input v-model="userInfo.nickname"></el-input>
          </el-form-item>
          <el-form-item label="用户邮箱" prop="email">
            <el-input v-model="userInfo.email"></el-input>
          </el-form-item>
     
          <el-form-item>
            <el-button type="primary" @click="updateUserInfo">保存</el-button>
            <!-- <el-button plain @click="resetUserInfo">重置</el-button> -->
          </el-form-item>
        </el-form>
      </div>
    </div>
    
    <!-- 用户信息页脚 -->
    <div class="info-footer">
      <div class="footer-divider"></div>
      <div class="footer-content">
        <div class="footer-item">更新: {{ userInfo.updateTime }}</div>
        <div class="footer-item">创建: {{ userInfo.createTime }}</div>
      </div>
    </div>
  </el-card>
  
  <el-dialog v-model="avatarDialogVisible" title="修改头像" width="400px">
    <div class="avatar-dialog-content">
      <img :src="imgs" alt="当前头像" class="current-avatar">
      <el-upload class="avatar-uploader" action="/api/upload" :show-file-list="false" :auto-upload="true" 
      name="file"
      :headers="{'Authorization':tokenStore.token}"
      :on-success="uploadSuccess">
        <el-button type="primary">选择新头像</el-button>
      </el-upload>
      <div class="dialog-actions">
        <el-button @click="avatarDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAvatar">保存</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import useUserStore from '@/stores/userinfo.js'
import {useTokenStore} from '@/stores/token.js'
import { updateUserInfoService ,usrtAvatarService} from '@/api/auth.js'
const userInfoStore = useUserStore();
const tokenStore = useTokenStore();
const userInfo = ref({...userInfoStore.info})
const avatarDialogVisible = ref(false)
// console.log(userInfo.value)
const rules = {
    nickname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/,
            message: '昵称必须是2-10位的非空字符串',
            trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
    ]
}

function openAvatarDialog() { avatarDialogVisible.value = true }

const updateUserInfo = async ()=>{
  // console.log(userInfo.value);
  let result = await updateUserInfoService(userInfo.value)
  ElMessage.success(result.message ? result.message : '修改成功');
 userInfoStore.setInfo(userInfo.value)
}
const imgs = ref(userInfoStore.info.userPic)

const uploadSuccess = async (result)=>{
  // this.imgs = URL.createObjectURL(file);
  console.log(1+result.data);
imgs.value = result.data;
}

function handleAvatarChange(file) { console.log('选择的头像文件:', file) }

const saveAvatar = async ()=>{
   console.log(imgs.value);
  let result =await usrtAvatarService(imgs.value)
  ElMessage.success(result.message?result.message:"保存成功");
  avatarDialogVisible.value = false;
  userInfoStore.info.userPic=imgs.value;
}
</script>

<style scoped>
.page-container {
  min-height: calc(100% - 25px);
  position: relative;
  overflow: hidden;
  background-color: #f9fafc;
  display: flex;
  flex-direction: column;
}

.top-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #409EFF, #67C23A, #E6A23C);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.profile-layout {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex: 1;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 140px;
  background-color: #f0f7ff;
  padding: 20px 10px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  height: fit-content;
}

.user-avatar {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  border: 4px solid #e1f0ff;
  position: relative;
  margin-bottom: 10px;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.avatar-edit-btn {
  position: absolute;
  bottom: 5px;
  right: 5px;
  background: #409EFF;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  cursor: pointer;
}

.user-level {
  margin-top: 15px;
  width: 100%;
  padding: 10px;
  background-color: #fff;
  border-radius: 6px;
}

.level-text {
  display: block;
  text-align: center;
  margin-top: 5px;
  font-size: 12px;
  color: #409EFF;
  font-weight: bold;
}

.form-section { 
  flex: 1; 
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.info-footer {
  margin-top: auto;
  padding-top: 20px;
  color: #909399;
}

.footer-divider {
  height: 2px;
  background: linear-gradient(to right, transparent, #409EFF, transparent);
  margin-bottom: 15px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  padding: 12px 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

.footer-item {
  font-size: 13px;
  padding: 4px 8px;
  background-color: #ffffff;
  border-radius: 3px;
  border-left: 3px solid #409EFF;
}

.avatar-dialog-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.current-avatar {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
  margin-bottom: 15px;
}

.dialog-actions {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.mt-10 { margin-top: 10px; }

@media (max-width: 768px) {
  .profile-layout {
    flex-direction: column;
    align-items: center;
  }
}
</style>