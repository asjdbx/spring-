<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文章审核管理</span>
        <div class="extra">
          <el-select v-model="filterStatus" placeholder="审核状态" class="filter-select" @change="handleFilter">
            <el-option label="全部" value="" />
            <el-option label="待审核" value="0" />
            <el-option label="已通过" value="2" />
            <el-option label="已拒绝" value="1" />
          </el-select>
        </div>
      </div>
    </template>
    
    <div class="search-area">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入文章标题搜索"
        class="search-input"
        clearable
        @clear="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="resetFilter">重置</el-button>
    </div>
    
    <el-table
      :data="currentPageData"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="title" label="文章标题" min-width="180" show-overflow-tooltip />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column prop="artiState" label="审核状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.artiState)">
            {{ getStatusText(row.artiState) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="220">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="showArticleDetail(row)">查看</el-button>
          <el-button 
            type="success" 
            size="small" 
            @click="confirmApprove(row)"
            v-if="row.artiState === '1'|| row.artiState === '0'"
          >通过</el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="showRejectDialog(row)"
            v-if="row.artiState === '2'|| row.artiState === '0'"
          >拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalArticles"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <!-- 文章详情抽屉 -->
    <el-drawer v-model="drawerVisible" title="文章详情" direction="rtl" size="50%">
      <div class="article-detail" v-if="currentArticle">
        <h2>{{ currentArticle.title }}</h2>
        <div class="article-meta">
          <span>分类：{{ currentArticle.categoryName }}</span>
          <span>发布时间：{{ currentArticle.createTime }}</span>
          <span>
            状态：
            <el-tag :type="getStatusType(currentArticle.artiState)">
              {{ getStatusText(currentArticle.artiState) }}
            </el-tag>
          </span>
        </div>
        <div class="article-content" v-html="currentArticle.content"></div>
        
        <div class="review-actions" v-if="currentArticle.artiState === '0'">
          <h3>审核操作</h3>
          <el-form :model="reviewForm" ref="reviewFormRef" :rules="reviewRules">
            <el-form-item label="审核意见" prop="comment">
              <el-input 
                v-model="reviewForm.comment" 
                type="textarea" 
                rows="3"
                placeholder="请输入审核意见"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="approveArticle">通过审核</el-button>
              <el-button type="danger" @click="rejectArticle">拒绝发布</el-button>
            </el-form-item>
          </el-form>
        </div>
        
        <!-- <div class="review-result" v-else>
          <h3>审核结果</h3>
          <p><strong>审核人：</strong>{{ currentArticle.reviewer }}</p>
          <p><strong>审核时间：</strong>{{ currentArticle.reviewTime }}</p>
          <p><strong>审核意见：</strong>{{ currentArticle.reviewComment }}</p>
        </div> -->
      </div>
    </el-drawer>
    
    <!-- 拒绝原因对话框 -->
    <el-dialog v-model="rejectDialogVisible" title="拒绝原因" width="30%">
      <el-form :model="rejectForm" ref="rejectFormRef" :rules="rejectRules">
        <el-form-item label="拒绝原因" prop="reason">
          <el-input 
            v-model="rejectForm.reason" 
            type="textarea" 
            rows="3"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rejectDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmReject">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import{articleCategoryListService,articlLists,articleStateService} from '@/api/article.js'

//文章分类数据模型
const categorys = ref([
    {
        "id": 3,
        "categoryName": "美食",
        "categoryAlias": "my",
        "createTime": "2023-09-02 12:06:59",
        "updateTime": "2023-09-02 12:06:59"
    }
])

// 模拟数据 - 实际项目中应该从API获取
const articles = ref([
  { 
    id: 1, 
    title: '如何提高编程效率', 
    categoryName: '技术', 
    author: '张三', 
    createTime: '2023-07-01 10:30:00', 
    status: '0',
    content: '<p>这是一篇关于提高编程效率的文章内容...</p><p>包含了多种提高效率的方法和技巧。</p>'
  },
  { 
    id: 2, 
    title: '人工智能的未来发展', 
    categoryName: '科技', 
    author: '李四', 
    createTime: '2023-07-02 14:20:00', 
    status: '1',
    content: '<p>这是一篇关于人工智能未来发展的文章内容...</p>',
    reviewer: '管理员',
    reviewTime: '2023-07-03 09:15:00',
    reviewComment: '内容符合规范，通过审核'
  }
]);

// 状态和分页
const loading = ref(false);
const searchKeyword = ref('');
const filterStatus = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const totalArticles = ref(0);

// 抽屉和对话框
const drawerVisible = ref(false);
const rejectDialogVisible = ref(false);
const currentArticle = ref(null);

// 表单
const reviewFormRef = ref(null);
const rejectFormRef = ref(null);
const reviewForm = ref({
  comment: '内容符合规范，通过审核'
});
const rejectForm = ref({
  reason: ''
});
const articleCategoryList = async () => {
    let result = await articleCategoryListService();
    categorys.value = result.data;

}
articleCategoryList()
//获取文章列表
const articleList = async () => {
    let state='已发布'
    let result = await articlLists(state);
    articles.value = result.data ;
    // console.log(result.data.items );
    for (let i = 0; i < articles.value.length;i++){
        let categoryId=articles.value[i].categoryId;
        for (let j = 0; j < categorys.value.length;j++){
            if(categoryId==categorys.value[j].id){
                articles.value[i].categoryName=categorys.value[j].categoryName;
            }
        }
    }
    const review = reviewStore.getReview(articles.id);
      if (review) {
        // 合并审核信息到文章数据
        articles.reviewer = review.reviewer;
        articles.reviewTime = review.reviewTime;
        articles.reviewComment = review.reviewComment;
      }
      totalArticles.value = articles.value.length;
}
articleList();

// 表单验证规则
const reviewRules = {
  comment: [{ required: true, message: '请输入审核意见', trigger: 'blur' }]
};
const rejectRules = {
  reason: [{ required: true, message: '请输入拒绝原因', trigger: 'blur' }]
};

// 计算属性：过滤后的文章列表
const filteredArticles = computed(() => {
  let result = articles.value;
  // 按状态筛选
  if (filterStatus.value) {
    result = result.filter(article => article. artiState=== filterStatus.value);
  }
  
  // 按标题搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase();
    result = result.filter(article => 
      article.title.toLowerCase().includes(keyword)
    );
  }
  return result;
});
// 新增：当前页数据（分页截取）
const currentPageData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredArticles.value.slice(start, end);
});

// 获取状态类型和文本
const getStatusType = (status) => {
  // console.log(status)
  switch (status) {
    case '0': return 'warning';
    case '2': return 'success';
    case '1': return 'danger';
    default: return 'info';
  }
};

const getStatusText = (status) => {
  // console.log(1+status)
  switch (status) {
    case '0': return '待审核';
    case '2': return '已通过';
    case '1': return '已拒绝';
    default: return '未知';
  }
};

// 事件处理函数
const handleSearch = () => {
  currentPage.value = 1;
};

const handleFilter = () => {
  currentPage.value = 1;
};

const resetFilter = () => {
  searchKeyword.value = '';
  filterStatus.value = '';
  currentPage.value = 1;
};

const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1;
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  articleList();
};

const showArticleDetail = (article) => {
  currentArticle.value = { ...article };
  drawerVisible.value = true;
  // 关键：每次打开抽屉时，重置审核意见
  reviewForm.value.comment = ''; // 清空意见
  
  // 可选：如果是"待审核"状态，可预设通过意见
  if (article.artiState === '0') {
    reviewForm.value.comment = '内容符合规范，通过审核'; // 预设通过意见
  }
};

// 通过审核（列表页快捷操作）
const confirmApprove = (article) => {
  ElMessageBox.confirm(
    `确定通过文章《${article.title}》的审核吗？`,
    '审核确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success'
    }
  ).then(async () => {
    try {
      loading.value = true;
      await articleStateService(article.id, '2'); // 调用后端接口，状态2表示通过
      ElMessage.success('审核通过成功');
      articleList(); // 刷新列表
    } catch (error) {
      ElMessage.error('审核失败：' + error.message);
    } finally {
      loading.value = false;
    }
  }).catch(() => {});
};

// 打开拒绝审核对话框
const showRejectDialog = (article) => {
  currentArticle.value = { ...article };
  rejectForm.value.reason = '';
  rejectDialogVisible.value = true;
};

// 引入审核 Store
import useReviewStore from '@/stores/user.js';
const reviewStore = useReviewStore();

// 1. 通过审核（详情页）
const approveArticle = () => {
  reviewFormRef.value.validate(async (valid) => {
    if (valid && currentArticle.value) {
      try {
        loading.value = true;
        // 调用后端接口
        await articleStateService(currentArticle.value.id, '2');
        
        // 构造审核信息
        const reviewData = {
          reviewer: '管理员', // 实际项目中可从用户信息中获取
          reviewTime: new Date().toLocaleString(),
          reviewComment: reviewForm.value.comment
        };
        
        // 保存到 Pinia（持久化）
        reviewStore.saveReview(currentArticle.value.id, reviewData);
        
        // 更新本地文章数据
        const article = articles.value.find(a => a.id === currentArticle.value.id);
        if (article) {
          article.artiState = '2';
          Object.assign(article, reviewData); // 合并审核信息
        }
        
        ElMessage.success('审核通过成功');
        drawerVisible.value = false;
      } catch (error) {
        ElMessage.error('操作失败');
      } finally {
        loading.value = false;
      }
    }
  });
};

// 2. 拒绝审核（对话框）
const confirmReject = () => {
  rejectFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true;
        await articleStateService(currentArticle.value.id, '1');
        
        // 构造审核信息
        const reviewData = {
          reviewer: '管理员',
          reviewTime: new Date().toLocaleString(),
          reviewComment: rejectForm.value.reason
        };
        
        // 保存到 Pinia（持久化）
        reviewStore.saveReview(currentArticle.value.id, reviewData);
        
        // 更新本地文章数据
        const article = articles.value.find(a => a.id === currentArticle.value.id);
        if (article) {
          article.artiState = '1';
          Object.assign(article, reviewData);
        }
        
        ElMessage.success('已拒绝发布');
        rejectDialogVisible.value = false;
      } catch (error) {
        ElMessage.error('操作失败');
      } finally {
        loading.value = false;
      }
    }
  });
};
// 详情页拒绝审核（带审核意见）
const rejectArticle = () => {
  reviewFormRef.value.validate(async (valid) => {
    if (valid && reviewForm.value.comment) {
      try {
        loading.value = true;
        await articleStateService(currentArticle.value.id, '1'); // 调用后端接口，状态1表示拒绝
        // 更新本地数据
        const article = articles.value.find(a => a.id === currentArticle.value.id);
        if (article) {
          article.artiState = '1';
          article.reviewer = '管理员';
          article.reviewTime = new Date().toLocaleString();
          article.reviewComment = reviewForm.value.comment;
        }
        ElMessage.success('已拒绝发布该文章');
        drawerVisible.value = false;
      } catch (error) {
        ElMessage.error('操作失败：' + error.message);
      } finally {
        loading.value = false;
      }
    } else {
      ElMessage.warning('请填写拒绝原因');
    }
  });
};
// 生命周期钩子
onMounted(() => {
  // 实际项目中应该在这里调用API获取文章列表
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
  }, 500);
});
</script>

<style scoped>
.page-container {
  margin: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header span {
  font-size: 18px;
  font-weight: bold;
}
.extra {
  display: flex;
  align-items: center;
}
.filter-select {
  width: 120px;
  margin-right: 10px;
}

.search-area {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.search-input {
  width: 300px;
  margin-right: 10px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.article-detail {
  padding: 0 20px;
}

.article-meta {
  margin: 15px 0;
  color: #666;
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.article-content {
  margin: 20px 0;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 4px;
  background-color: #fafafa;
  min-height: 200px;
}

.review-actions, .review-result {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
</style>
