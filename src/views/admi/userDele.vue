<!-- 用户信息页面组件 -->
<template>
  <div class="user-info-container">
    <div class="header">
      <h2>用户信息管理</h2>
      <el-button type="primary" @click="handleAdd() ;clearForm()">添加用户</el-button>
    </div>
    
    <!-- 搜索区域 -->
    <div class="search-area">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入用户名或邮箱搜索"
        class="search-input"
        clearable
        @clear="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>
    
    <!-- 用户信息表格 -->
    <el-table
      :data="currentPageData" 
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="username" label="登录名称" width="120" />
      <el-table-column prop="nickname" label="用户昵称" width="120" />
      <el-table-column prop="email" label="用户邮箱" width="180" />
      <el-table-column prop="role" label="用户角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === '2' ? 'danger' : 'success'">
            {{ row.role === '2' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '1' ? 'success' : 'danger' ">
            {{ row.status === '1' ? '正常' :  '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="updateTime" label="更新时间" width="180" />
      <el-table-column label="操作" fixed="right" width="220">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="handleDelete(row)"
            :disabled="row.role === '2'"
          >
            删除
          </el-button>
          <el-button 
          :type="row.status === '0' ? 'success' : 'danger'"
          size="small" 
          @click="handleToggleStatus(row)"
          :disabled="row.role === '2'"
        >
         {{ row.status === '0' ? '正常' : '禁用' }}
        </el-button>
         
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalUsers"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <!-- 用户表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑用户' : '添加用户'"
      width="500px"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userFormRules"
        label-width="100px"
      >
        <el-form-item label="登录名称" prop="username">
          <el-input v-model="userForm.username" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="用户角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择用户角色">
            <el-option label="普通用户" value="1" />
            <el-option label="管理员" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户密码" prop="password" v-if="!isEdit">
          <el-input v-model="userForm.password" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {userAll,userupdateStatus,deleteUserService,userRegisterService,userupdateRole} from '@/api/auth.js';

// 加载状态
const loading = ref(false);

// 用户列表数据
const userList = ref([
  {
    id: 1,
    username: 'admin',
    nickname: '管理员',
    email: 'admin@example.com',
    role: 'admin',
    status: 1,
    createTime: '2023-01-01 00:00:00',
    updateTime: '2023-01-01 00:00:00'
  },
  {
    id: 2,
    username: 'user1',
    nickname: '用户1',
    email: 'user1@example.com',
    role: 'user',
    status: 1,
    createTime: '2023-01-02 00:00:00',
    updateTime: '2023-01-02 00:00:00'
  },
  {
    id: 3,
    username: 'user2',
    nickname: '用户2',
    email: 'user2@example.com',
    role: 'user',
    status: 1,
    createTime: '2023-01-03 00:00:00',
    updateTime: '2023-01-03 00:00:00'
  },
  {
    id: 4,
    username: 'user3',
    nickname: '用户3',
    email: 'user3@example.com',
    role: 'user',
    status: 0,
    createTime: '2023-01-04 00:00:00',
    updateTime: '2023-01-04 00:00:00'
  }
]);

// 搜索关键词
const searchKeyword = ref('');

// 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const totalUsers = ref(0);

// 过滤后的用户列表
const filteredUserList = computed(() => {
  if (!searchKeyword.value) {
    return userList.value;
  }
  const keyword = searchKeyword.value.toLowerCase();
  return userList.value.filter(user => 
    user.username.toLowerCase().includes(keyword) || 
    user.email.toLowerCase().includes(keyword) ||
    user.nickname.toLowerCase().includes(keyword)
  );
});

// 新增：当前页数据（分页截取）
const currentPageData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredUserList.value.slice(start, end);
});

// 对话框相关
const dialogVisible = ref(false);
const isEdit = ref(false);
const userFormRef = ref(null);
const userForm = ref({
  id: '',
  username: '',
  role: '',
  password: '',
  status: ''
});
const  useralls = async () => {
  let result = await userAll();
  userList.value = result.data;
  totalUsers.value = userList.value.length;
}
  useralls();
  
// 表单验证规则
const userFormRules = {
  username: [
    { required: true, message: '请输入登录名称', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择用户角色', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur', validator: (rule, value, callback) => {
      if (isEdit.value) {
        callback();
      } else if (!value) {
        callback(new Error('请输入密码'));
      } else {
        callback();
      }
    }}
  ]
};

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1;
};

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val;
   currentPage.value = 1;
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
};

// 添加用户
const handleAdd = async() => {
  isEdit.value = false;
  dialogVisible.value = true;
  
};

const clearForm = () => {
  userForm.value={
    id: '',
    username: '',
    role: '',
    password: '',
    status: ''
  };
}
  
// 编辑用户
const handleEdit = (row) => {
  isEdit.value = true;
  userForm.value.id = row.id;
  userForm.value.username = row.username;
  userForm.value.role = row.role==='1'?'普通用户':'管理员';
  dialogVisible.value = true;
};

// 删除用户
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除用户 ${row.username} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async() => {
   await deleteUserService(row.id);
    ElMessage.success(`删除${row.username}成功`);
     useralls();
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消'
    })
  });
};

// 切换用户状态
const handleToggleStatus = (row) => {
  console.log(row.status)
  let action = row.status === '1' ? '禁用' : '启用';
let newStatus = row.status === '0' ? '1' : '0';
  
  ElMessageBox.confirm(
    `确定要${action}用户 ${row.username} 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async() => {
    await userupdateStatus(row.id,newStatus); 
    ElMessage.success(`${action}成功`);
    useralls();
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消'
    }) 
  });
};

// 提交表单
const submitForm =async () => {
  if(isEdit.value){
    console.log('提交的表单数据:', userForm.value);
    let result = await userupdateRole(userForm.value.id,userForm.value.role);
    ElMessage.success(result.message ? result.message : '修改成功')
 useralls();
  }else{
    let result = await userRegisterService(userForm.value);
    ElMessage.success(result.message ? result.message : '添加成功')
     useralls();
  }
     dialogVisible.value = false;
 
};
</script>

<style scoped>
.user-info-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.header h2 {
  font-size: 22px;
  color: #303133;
  margin: 0;
  font-weight: 600;
}

.search-area {
  display: flex;
  margin-bottom: 24px;
  background-color: #fff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.03);
}

.search-input {
  width: 300px;
  margin-right: 16px;
  transition: all 0.3s;
}

.search-input:focus {
  width: 350px;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  padding: 16px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.03);
}

/* 表格样式 */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fafafa;
}

/* 按钮样式 */
:deep(.el-button) {
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.3s;
}

:deep(.el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 对话框样式 */
:deep(.el-dialog) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  background-color: #f5f7fa;
  padding: 16px 20px;
  margin-right: 0;
}

:deep(.el-dialog__title) {
  font-weight: 600;
  color: #303133;
}

:deep(.el-dialog__body) {
  padding: 24px;
}

:deep(.el-dialog__footer) {
  padding: 16px 20px;
  border-top: 1px solid #ebeef5;
}

/* 标签样式 */
:deep(.el-tag) {
  border-radius: 4px;
  padding: 0 10px;
  height: 28px;
  line-height: 28px;
  font-weight: 500;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .search-area {
    flex-direction: column;
  }
  
  .search-input {
    width: 100%;
    margin-right: 0;
    margin-bottom: 10px;
  }
  
  .header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .header h2 {
    margin-bottom: 16px;
  }
}
</style>