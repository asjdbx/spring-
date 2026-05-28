import { defineStore } from "pinia";
import { ref } from 'vue';

const useReviewStore = defineStore('review', () => {
  // 存储审核记录：key 是文章 id，value 是审核信息
  const reviewRecords = ref({}); 

  // 保存审核记录
  const saveReview = (articleId, reviewData) => {
    reviewRecords.value[articleId] = {
      reviewer: reviewData.reviewer,    // 审核人
      reviewTime: reviewData.reviewTime, // 审核时间
      reviewComment: reviewData.reviewComment // 审核意见
    };
  };

  // 获取单篇文章的审核记录
  const getReview = (articleId) => {
    return reviewRecords.value[articleId] || null;
  };

  return {
    reviewRecords,
    saveReview,
    getReview
  };
}, {
  persist: true // 持久化到本地存储（刷新不丢失）
});

export default useReviewStore;