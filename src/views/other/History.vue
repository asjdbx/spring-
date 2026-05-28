<template>
  <div class="history-container">
    <el-card class="info-card">
      <template #header>
        <div class="info-header">
          <el-icon class="info-icon"><InfoFilled /></el-icon>
          <span>阅读历史记录</span>
        </div>
      </template>
      <div class="info-content">
        系统会自动记录您的阅读历史，方便您随时查看已阅读过的文章。历史记录仅保存30天，请及时收藏重要内容。
      </div>
    </el-card>

    <div class="history-header">
      <h2 class="page-title">我的阅读历史</h2>
      <el-button 
        type="danger" 
        @click="clearHistory" 
        :disabled="historyList.length === 0"
        class="clear-btn"
      >
        <el-icon><Delete /></el-icon>
        清空历史
      </el-button>
    </div>

    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
      <div class="loading-text">正在加载您的阅读历史...</div>
    </div>

    <div v-else-if="historyList.length === 0" class="empty-history">
      <el-empty description="暂无阅读历史">
        <template #image>
          <el-image src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="empty-image"></el-image>
        </template>
        <el-button type="primary" @click="goToArticles">浏览文章</el-button>
      </el-empty>
    </div>

    <transition-group name="list" tag="div" class="timeline-container">
      <el-timeline v-if="historyList.length > 0">
        <el-timeline-item
          v-for="(item, index) in historyList"
          :key="item.id"
          :timestamp="formatDate(item.readTime)"
          placement="top"
          :type="getTimelineItemType(index)"
          :hollow="index % 2 === 0"
        >
          <el-card 
            class="history-card" 
            shadow="hover" 
            @click="viewArticle(item.articleId)"
          >
            <div class="article-title">{{ item.title }}</div>
            <div class="article-info">
              <div class="info-left">
                <el-tag size="small" effect="plain">{{ item.category || '未分类' }}</el-tag>
                <span class="author"><el-icon><User /></el-icon> {{ item.author || '未知作者' }}</span>
              </div>
              <div class="info-right">
                <el-button size="small" type="primary" text @click.stop="viewArticle(item.articleId)">
                  <el-icon><View /></el-icon> 查看
                </el-button>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </transition-group>

    <el-dialog
      v-model="dialogVisible"
      title="确认清空历史"
      width="30%"
      center
      destroy-on-close
    >
      <div class="dialog-content">
        <el-icon class="warning-icon"><WarningFilled /></el-icon>
        <p>确定要清空所有阅读历史吗？</p>
        <p class="warning-text">此操作不可恢复，清空后将无法找回历史记录。</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmClear()">
            <el-icon><Delete /></el-icon>
            确定清空
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
// 引入依赖
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { historyInsttAll, historyInsttDelete, articleCategoryListService, articlefiId } from '@/api/article'
import { userAll } from '@/api/auth.js';
import { useRouter } from 'vue-router'
import { InfoFilled, Delete, User, View, WarningFilled } from '@element-plus/icons-vue'
import useUserStore from '@/stores/userinfo.js'
const tokenStore = useUserStore();
// 路由实例
const router = useRouter()

// 响应式数据
const historyList = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const categorys = ref([]) // 分类列表
const userList = ref([]) // 新增：用户列表（用于匹配作者名）

// 获取用户列表（用于显示作者信息）
const fetchUserList = async () => {
  try {
    const res = await userAll();
    userList.value = res.data || [];
  } catch (e) { 
    console.error('加载用户列表失败:', e);
    ElMessage.warning('获取作者信息失败');
  }
};

// 获取阅读历史
const fetchHistory = async () => {
  loading.value = true
  try {
    const res = await historyInsttAll(tokenStore.info.id)
    if (res.data) {
      historyList.value = res.data || []
      // 补充文章详情（标题、分类、作者）
      for (const item of historyList.value) {
        try {
          // 获取文章详情
          const articleRes = await articlefiId(item.articleId)
          if (articleRes.data) {
            item.title = articleRes.data.title || `文章ID: ${item.articleId}`
            // 匹配分类名称
            const category = categorys.value.find(c => c.id === articleRes.data.categoryId)
            item.category = category ? category.categoryName : '未分类'
            // 核心修复：匹配作者名称（通过文章的createUser字段关联用户ID）
            const author = userList.value.find(u => u.id === articleRes.data.createUser)
            item.author = author ? author.username : '未知作者'
          }
        } catch (e) {
          console.error(`获取文章${item.articleId}详情失败`, e)
          item.title = `文章ID: ${item.articleId}`
          item.category = '未分类'
          item.author = '未知作者'
        }
      }
      // 按阅读时间倒序（最新的在前面）
      historyList.value.sort((a, b) => new Date(b.readTime) - new Date(a.readTime))
    } else {
      ElMessage.warning('获取历史记录失败')
    }
  } catch (error) {
    console.error('加载历史失败:', error)
    ElMessage.error('加载历史记录失败')
  } finally {
    loading.value = false
  }
}

// 获取分类列表
const articleCategoryList = async () => {
  try {
    const result = await articleCategoryListService()
    categorys.value = result.data || []
  } catch (error) {
    ElMessage.error('获取分类列表失败')
    console.error(error)
  }
}

// 格式化日期
const formatDate = (time) => {
  if (!time) return '未知时间'
  let date = new Date(time)
  if (isNaN(date.getTime())) return '未知时间'
  
  const now = new Date()
  const diffMs = now - date
  const diffMinutes = Math.floor(diffMs / (1000 * 60))
  const diffHours = Math.floor(diffMs / (1000 * 60 * 60))
  const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24))

  if (diffMinutes < 60) return `${diffMinutes}分钟前`
  if (diffHours < 24) return `${diffHours}小时前`
  if (diffDays === 1) return `昨天 ${formatTime(date)}`
  if (diffDays < 30) return `${diffDays}天前 ${formatTime(date)}`
  
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${formatTime(date)}`
}

// 辅助：格式化时间（时分）
const formatTime = (date) => {
  return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 时间线样式
const getTimelineItemType = (index) => {
  const types = ['primary', 'success', 'warning', 'info']
  return types[index % types.length]
}

// 查看文章详情
const viewArticle = (articleId) => {
  router.push({ path: '/art/ArticleAll', query: { id: articleId } })
}

// 跳转文章列表
const goToArticles = () => {
  router.push('/art/ArticleAll')
}

// 清空历史相关
const clearHistory = () => {
  dialogVisible.value = true
}
const confirmClear = async () => {
  try {console.log(tokenStore.info.id)
    const res = await historyInsttDelete(tokenStore.info.id)
    ElMessage.success(res.message?res.message:'清空成功');
    fetchUserList();
  } catch (error) {
    console.error('清空错误:', error)
    ElMessage.error('清空历史失败')
  } finally {
    dialogVisible.value = false
  }
}

// 初始化加载（按依赖顺序执行）
onMounted(async () => {
  // 先加载用户和分类（后续依赖这两个数据）
  await Promise.all([
    fetchUserList(),    // 加载用户列表（用于作者匹配）
    articleCategoryList() // 加载分类列表
  ])
  // 最后加载历史（确保依赖数据就绪）
  await fetchHistory()
})
</script>

<style scoped>
/* 样式保持不变 */
.history-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.info-card {
  margin-bottom: 20px;
}

.info-header {
  display: flex;
  align-items: center;
}

.info-icon {
  margin-right: 8px;
  color: #4096ff;
}

.info-content {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.history-card {
  cursor: pointer;
  transition: all 0.3s;
}

.history-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.article-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  transition: color 0.2s;
}

.history-card:hover .article-title {
  color: #4096ff;
}

.article-info {
  display: flex;
  justify-content: space-between;
  color: #666;
  font-size: 14px;
}

.info-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author {
  display: flex;
  align-items: center;
  gap: 4px;
}

.loading-container {
  padding: 30px 0;
}

.loading-text {
  text-align: center;
  margin-top: 10px;
  color: #666;
}

.empty-history {
  padding: 40px 0;
  text-align: center;
}

.dialog-content {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 0;
}

.warning-icon {
  color: #faad14;
  font-size: 20px;
}

.warning-text {
  color: #f5222d;
  margin-top: 8px;
  font-size: 13px;
}

/* 时间线动画 */
.list-enter-active,
.list-leave-active {
  transition: all 0.3s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>