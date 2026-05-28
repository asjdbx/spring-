<!--
 * @Author: 请设置正确的作者信息
 * @Date: 2025-07-02 18:54:56
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2026-05-02 20:09:02
 * @FilePath: \blong\src\views\argic\ArtiMa.vue
 * @Description: 文章管理页面组件
-->
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra"></div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId" style="width: 100px">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="categoryId = ''; state = ''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
            <el-table-column label="分类" prop="categoryName"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"></el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="show(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />

        <el-dialog 
            v-model="dialogVisible" 
            title="文章详情" 
            width="70%"
            @open="handleDialogOpen"
        >
            <el-form :model="articleDetail" label-width="80px">
                <el-form-item label="文章标题">
                    <el-input v-model="articleDetail.title" disabled />
                </el-form-item>
                <el-form-item label="所属分类">
                    <el-input v-model="articleDetail.categoryName" disabled />
                </el-form-item>
                <el-form-item label="发布状态">
                    <el-input v-model="articleDetail.state" disabled />
                </el-form-item>
                <el-form-item label="发布时间">
                    <el-input v-model="articleDetail.createTime" disabled />
                </el-form-item>
                <el-form-item label="文章封面">
                    <el-upload class="avatar-uploader" :auto-upload="false" :show-file-list="false"
                    action="#"
                    :headers="{'Authorization':tokenStore.token}"
                    :on-success="uploadSuccess"
                    ></el-upload>
                    <el-image 
                        v-if="articleDetail.coverImg" 
                        :src="articleDetail.coverImg" 
                        style="width: 200px; height: 150px"
                    />
                    <span v-else>无封面图</span>
                </el-form-item>
                <el-form-item label="文章内容">
                    <el-input 
                        v-model="articleDetail.content" 
                        type="textarea" 
                        :rows="10" 
                        disabled 
                    />
                </el-form-item>
                <el-form-item label="阅读量">
                <span>{{ articleDetail.reading || 0 }}</span>
                 </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="handleClose">关闭</el-button>
            </template>
        </el-dialog>
    </el-card>
</template>
<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'
import{articleCategoryListService,articleAll,articleReading,historyInstt ,staticTableData, artiUpdaurl} from '@/api/article.js'
import useUserStore from '@/stores/userinfo.js'
import { ElMessage } from 'element-plus';
const tokenStore = useUserStore();

// 文章分类数据模型
const categorys = ref([])

// 用户搜索时选中的分类id
const categoryId = ref('')

// 用户搜索时选中的发布状态
const state = ref('')

// 文章列表数据模型
const articles = ref([])

// 分页条数据模型
const pageNum = ref(1) // 当前页
const total = ref(0) // 总条数
const pageSize = ref(3) // 每页条数

// 弹窗相关变量
const dialogVisible = ref(false);
const articleDetail = ref({});
const currentArticleId = ref(null); // 记录当前查看的文章ID

// 阅读时间相关变量
const startTime = ref(null);

// 当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}

// 当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}

// 获取文章分类列表
const articleCategoryList = async () => {
    try {
        let result = await articleCategoryListService();
        categorys.value = result.data || [];
    } catch (error) {
        ElMessage.error('获取分类列表失败');
        console.error(error);
    }
}

// 获取文章列表
const articleList = async () => {
    try {
        let params = {
            pageNum: pageNum.value,
            pageSize: pageSize.value,
            categoryId: categoryId.value ? categoryId.value : null,
            state: state.value ? state.value : null
        }
        let result = await articleAll(params);
        total.value = result.data.total || 0;
        articles.value = result.data.items || [];

        // 为文章添加分类名称
        articles.value.forEach(article => {
            const category = categorys.value.find(c => c.id === article.categoryId);
            if (category) {
                article.categoryName = category.categoryName;
            }
        });
    } catch (error) {
        ElMessage.error('获取文章列表失败');
        console.error(error);
    }
}

// 图片上传回调函数
const uploadSuccess  = async (result)  => {
   // console.log('上传成功，图片路径:');
    articleDetail.value.coverImg = result.data;
   await artiUpdaurl(result.data);
}

// 弹窗打开时记录开始时间
const handleDialogOpen = () => {
    startTime.value = new Date(); // 正确初始化开始时间
}

// 关闭弹窗时处理
const handleClose = async () => {
    if (startTime.value && currentArticleId.value) {
        // 计算阅读时长（结束时间 - 开始时间）
        const endTime = new Date();
        const readDur = Math.max(0, Math.floor((endTime - startTime.value) / 1000));//秒        
        try {
            // 准备时间参数
           
           const readTime = new Date().toISOString().slice(0, 19);  // 后端LocalDateTime兼容格式 
            
            // 准备参数（修复字段名错误）
            const params = {
                userid: tokenStore.info.id,  // 修复：userid → userId
                articleId: currentArticleId.value,
                readTime: readTime,
                readDuration: readDur,
                readTtype: 1  
            };
             console.log('准备参数：', params);
            // 调用历史记录接口
            await historyInstt(params);
            ElMessage.success('阅读记录已保存');
            
            // 刷新文章列表
            articleList();
        } catch (error) {
            ElMessage.error('保存阅读记录失败');
            console.error('接口调用错误:', error);
        }
    }
    
    // 重置状态
    dialogVisible.value = false;
    startTime.value = null;
    currentArticleId.value = null;
}

// 显示文章详情
const show = async(row) => {
    
        currentArticleId.value = row.id; // 记录当前文章ID
        
        // 处理内容，去除p标签
        const contentWithoutP = row.content
            .replace(/<p>/g, '')
            .replace(/<\/p>/g, '');
            
        articleDetail.value = {
            title: row.title,
            categoryName: row.categoryName,
            state: row.state,
            createTime: row.createTime,
            coverImg: row.coverImg,
            reading: row.reading || 0,
            content: contentWithoutP || '无内容'
        };
        articleList();
        // 打开弹窗（会触发handleDialogOpen记录开始时间）
        dialogVisible.value = true;
        
        // 调用阅读接口
        let result = await articleReading(row.id);
        console.log(result);
            ElMessage.success(result.message?result.message:'已阅读');

             const read = new Date();
            const readDates = read.toISOString().split('T')[0];
         await staticTableData(row.id, readDates,tokenStore.info.id);
            
};

// 初始化加载数据
articleCategoryList();
articleList();
</script>
<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}

/* 上传组件样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>
    