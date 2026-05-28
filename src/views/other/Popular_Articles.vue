<template>
  <div class="popular-articles-page">
    <div class="page-header">
      <h2>热门文章</h2>
      <p>展示平台上阅读量最高的文章</p>
    </div>

    <el-card class="filter-card" shadow="hover">
      <div class="filter-container">
        <div class="filter-item">
          <el-select v-model="categoryId" placeholder="文章分类" clearable @change="loadArticles">
            <el-option v-for="item in categoryList" :key="item.id" :label="item.categoryName" :value="item.id" />
          </el-select>
        </div>
        <div class="filter-item">
          <el-select v-model="timeRange" placeholder="时间范围" @change="loadArticles">
            <el-option label="全部时间" value="" />
            <el-option label="最近一周" value="week" />
            <el-option label="最近一月" value="month" />
            <el-option label="最近三月" value="quarter" />
          </el-select>
        </div>
        <div class="filter-item">
          <el-input v-model="searchKeyword" placeholder="搜索文章标题" clearable @keyup.enter="handleSearch">
            <template #append>
              <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
            </template>
          </el-input>
        </div>
      </div>
    </el-card>

    <el-card class="article-list-card" shadow="hover" v-loading="loading">
      <div v-if="currentPageData.length === 0 && !loading" class="no-data">
        <el-empty description="暂无热门文章" />
      </div>
      <div v-else class="article-list">
        <div v-for="(article, index) in currentPageData" :key="article.id" class="article-item" @click="viewArticleDetail(article.id)">
          <div class="article-rank">
            <div :class="['rank-badge', index < 3 ? `top-${index + 1}` : '']">{{ index + 1 }}</div>
          </div>
          <div class="article-content">
            <h3 class="article-title">{{ article.title }}</h3>
            <div class="article-info">
              <span class="info-item"><el-icon><User /></el-icon>{{ findUserName(article.createUser) }}</span>
              <span class="info-item"><el-icon><Calendar /></el-icon>{{ formatDate(article.createTime) }}</span>
              <span class="info-item"><el-icon><Document /></el-icon>{{ article.reading === null ? '0' : article.reading }} 阅读</span>
              <el-tag size="small" type="info">{{ getCategoryName(article.categoryId) }}</el-tag>
            </div>
            <p class="article-summary">{{ article.content ? formatContent(article.content) : '暂无简介' }}</p>
          </div>
          <div class="article-reading">
            <div class="reading-count">{{ article.reading || 0}}</div>
            <div class="reading-label">阅读量</div>
          </div>
        </div>
      </div>
      <!-- // 分页器 -->
      <div class="pagination-container">
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[5,10,20,50]"
          layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" background />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="文章详情" width="70%" destroy-on-close :before-close="handleDialogClose">
      <div v-if="currentArticle" class="article-detail">
        <h2 class="detail-title">{{ currentArticle.title }}</h2>
        <div class="detail-meta">
          <span class="meta-item"><el-icon><User /></el-icon>{{ findUserName(currentArticle.createUser) }}</span>
          <span class="meta-item"><el-icon><Calendar /></el-icon>{{ formatDate(currentArticle.createTime) }}</span>
          <span class="meta-item"><el-icon><View /></el-icon>{{ currentArticle.reading || 0 }} 阅读</span>
          <el-tag size="small" type="info">{{ getCategoryName(currentArticle.categoryId) }}</el-tag>
        </div>
        <div class="detail-content" v-html="currentArticle.content"></div>
      </div>
      <div v-else class="loading-container"><el-skeleton :rows="10" animated /></div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { Search, User, Calendar, Document, View } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { articleAll, articleCategoryListService, articleReading,historyInstt } from '@/api/article';
import { userAll } from '@/api/auth.js';
import useUserStore from '@/stores/userinfo.js'
const tokenStore = useUserStore();


// 基础参数
const [pageNum, pageSize, total] = [ref(1), ref(10), ref(0)];
const [categoryId, timeRange, searchKeyword] = [ref(''), ref(''), ref('')];
const [articles, categoryList, loading] = [ref([]), ref([]), ref(false)];
const [dialogVisible, currentArticle, userList] = [ref(false), ref(null), ref([])];

// 加载分类
const loadCategories = async () => {
  try {
    const res = await articleCategoryListService();
    categoryList.value = res.data || [];
  } catch (e) { ElMessage.error('加载分类失败'); }
};

// 辅助函数
const getCategoryName = (id) => {
  const cate = categoryList.value.find(i => i.id === id);
  return cate ? cate.categoryName : '未分类';
};
const formatDate = (str) => str ? new Date(str).toLocaleDateString() : '未知日期';
const formatContent = (content) => {
  const text = content?.replace(/<[^>]+>/g, '') || '';
  return text.length > 100 ? text.slice(0, 100) + '...' : text;
};

// 加载文章
const loadArticles = async () => {
  loading.value = true;
  try {
    const res = await articleAll({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      categoryId: categoryId.value || null,
      title: searchKeyword.value.trim() || null
    });
    total.value = res.data.total;
    articles.value = res.data.items.sort((a, b) => b.reading - a.reading);
  } catch (e) {
    console.error(e);
    ElMessage.error('加载文章失败');
  } finally { loading.value = false; }
};

// 筛选与分页
const filteredArticles = computed(() => {
  let res = [...articles.value];
  if (searchKeyword.value) {
    const kw = searchKeyword.value.toLowerCase();
    res = res.filter(a => a.title.toLowerCase().includes(kw));
  }
  if (timeRange.value) {
    const now = new Date();
    if (timeRange.value === 'week') now.setDate(now.getDate() - 7);
    else if (timeRange.value === 'month') now.setMonth(now.getMonth() - 1);
    else if (timeRange.value === 'quarter') now.setMonth(now.getMonth() - 3);
    res = res.filter(a => new Date(a.createTime) >= now);
  }
  return res;
});
const currentPageData = computed(() => {
  const start = (pageNum.value - 1) * pageSize.value;
  return filteredArticles.value.slice(start, start + pageSize.value);
});

// 用户相关
const useralls = async () => {
  try {
    const res = await userAll();
    userList.value = res.data || [];
  } catch (e) { console.error('加载用户失败'); }
};
const findUserName = (id) => {
  const user = userList.value.find(u => u.id === id);
  return user?.username || '未知用户';
};
const startTime = ref(null);
// 文章详情
// const viewArticleDetail = async (id) => {
//   try {
//     const res = await articleReading(id);
//     const article = articles.value.find(a => a.id === id);
//     if (article) {
//       article.reading = res.data.reading;
//       currentArticle.value = { ...article };
//       startTime.value = new Date(); 
//       dialogVisible.value = true;
//     }
//   } catch (e) { ElMessage.error('查看详情失败'); }
// };
const viewArticleDetail = async (id) => {
  try {
    // 1. 先调用阅读量增加接口（如果后端有）
    // 假设后端有一个接口：articleAddRead(id) 或 articleReading(id) 本身会+1
    const res = await articleReading(id);
    
    // 2. 找到当前文章
    const article = articles.value.find(a => a.id === id);
    if (article) {
      // 3. 如果后端返回的是当前阅读量，我们直接加1
      // 或者基于返回的数值更新（取决于后端设计）
      if (res.data && res.data.reading !== undefined) {
        // 后端返回最新阅读量，直接取用
        article.reading = res.data.reading;
      } else {
        // 后端只是确认操作成功，我们需要手动+1
        article.reading = (article.reading || 0) + 1;
      }
      
      // 4. 设置当前文章详情
      currentArticle.value = { ...article };
      startTime.value = new Date(); 
      dialogVisible.value = true;
    }
  } catch (e) { 
    console.error(e);
    ElMessage.error('查看详情失败'); 
  }
};

// 事件处理
const handleSizeChange = (val) => { pageSize.value = val; pageNum.value = 1; loadArticles(); };
const handleSearch = () => { pageNum.value = 1; loadArticles(); };
const handleCurrentChange = (val) => { pageNum.value = val; loadArticles(); };

// 在原有代码的 handleDialogClose 中添加
const handleDialogClose = async () => {
  if (!currentArticle.value || !startTime.value) {
    dialogVisible.value = false;
    return;
  }
  // 1. 计算阅读时长（秒）
  const endTime = new Date();
  const readDur = Math.max(0, Math.floor((endTime - startTime.value) / 1000));
  
  // 2. 准备时间格式（兼容后端 LocalDateTime）
  const readTime = new Date().toISOString().slice(0, 19);
  
  // 3. 准备参数（确保用户已登录）

    const params = {
      userid: tokenStore.info.id,  // 按后端要求传参（若后端用userId则修改）
      articleId: currentArticle.value.id,
      readTime: readTime,
      readDuration: readDur,
      readtType: 1
    };
    // 4. 调用接口保存记录
    try {
      await historyInstt(params);
      ElMessage.success('阅读记录已保存');
    } catch (error) {
      console.error('保存阅读记录失败：', error);
      // 非关键错误，可不提示用户
    }
  
  // 5. 重置弹窗状态
  dialogVisible.value = false;
  currentArticle.value = null;
  startTime.value = null; // 清空开始时间
};

// 初始化
onMounted(() => { loadCategories(); useralls(); loadArticles(); });
</script>

<style scoped>
/* 保持原有样式不变 */
.popular-articles-page { padding: 20px; background: #f5f7fa; min-height: 100vh; }
.page-header { margin-bottom: 20px; }
.page-header h2 { margin: 0 0 10px; color: #1d2129; }
.page-header p { margin: 0; color: #86909c; font-size: 14px; }
.filter-card { margin-bottom: 20px; }
.filter-container { display: flex; flex-wrap: wrap; gap: 15px; }
.filter-item { min-width: 200px; flex: 1; }
.article-list-card { margin-bottom: 20px; }
.no-data { padding: 40px 0; text-align: center; }
.article-list { display: flex; flex-direction: column; gap: 15px; }
.article-item { display: flex; padding: 15px; background: #fff; border-radius: 8px; cursor: pointer; box-shadow: 0 2px 12px rgba(0,0,0,0.05); transition: all 0.3s; }
.article-item:hover { transform: translateY(-3px); box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
.article-rank { padding-right: 15px; }
.rank-badge { width: 30px; height: 30px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: bold; background: #e5e6eb; color: #86909c; }
.top-1 { background: #f5222d; color: #fff; }
.top-2 { background: #fa8c16; color: #fff; }
.top-3 { background: #fadb14; color: #1d2129; }
.article-content { flex: 1; overflow: hidden; }
.article-title { margin: 0 0 10px; font-size: 18px; color: #1d2129; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.article-info { display: flex; flex-wrap: wrap; gap: 15px; margin-bottom: 10px; font-size: 13px; color: #86909c; }
.info-item { display: flex; align-items: center; gap: 5px; }
.article-summary { margin: 0; font-size: 14px; color: #4e5969; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.article-reading { min-width: 80px; padding-left: 20px; display: flex; flex-direction: column; align-items: center; justify-content: center; }
.reading-count { font-size: 24px; font-weight: bold; color: #1d2129; }
.reading-label { font-size: 12px; color: #86909c; }
.pagination-container { margin-top: 20px; display: flex; justify-content: center; }
.article-detail { padding: 20px; }
.detail-title { margin: 0 0 20px; font-size: 24px; color: #1d2129; }
.detail-meta { display: flex; flex-wrap: wrap; gap: 15px; margin-bottom: 20px; font-size: 14px; color: #86909c; }
.meta-item { display: flex; align-items: center; gap: 5px; }
.detail-content { font-size: 16px; line-height: 1.8; color: #1d2129; }
.loading-container { padding: 20px; }
</style>