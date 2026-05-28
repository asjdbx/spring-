<template>
  <div class="reading-statistics-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>阅读量统计分析</h2>
      <p>展示平台内容阅读数据总览及趋势</p>
    </div>

    <!-- 统计卡片区域 -->
    <el-row :gutter="20" class="statistics-cards">
      <el-col :span="24" :lg="6">
        <el-card shadow="hover" class="stat-card card-blue">
          <div class="card-header">
            <span>总阅读量</span>
            <div class="icon-container blue-bg">
              <el-icon class="card-icon"><View /></el-icon>
            </div>
          </div>
          <div class="card-value">
            <el-statistic
              :value="totalReading"
              :precision="0"
              value-style="{ fontSize: '28px', color: '#165DFF' }"
              animation-duration="1500"
            >
              <template #prefix>
                <el-icon color="#165DFF"><histogram /></el-icon>
              </template>
              <template #suffix>次</template>
            </el-statistic>
          </div>
          <div class="card-trend">
            <el-icon color="#165DFF"><trend-charts /></el-icon>
            <span>总体趋势</span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" :lg="6">
        <el-card shadow="hover" class="stat-card card-green">
          <div class="card-header">
            <span>今日阅读量</span>
            <div class="icon-container green-bg">
              <el-icon class="card-icon"><sunny /></el-icon>
            </div>
          </div>
          <div class="card-value">
            <el-statistic
              :value="todayReading"
              :precision="0"
              value-style="{ fontSize: '28px', color: '#36D399' }"
              animation-duration="1500"
            >
              <template #prefix>
                <el-icon color="#36D399"><calendar /></el-icon>
              </template>
              <template #suffix>次</template>
            </el-statistic>
          </div>
          <div class="card-trend">
            <el-icon color="#36D399"><data-analysis /></el-icon>
            <span>今日数据</span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" :lg="6">
        <el-card shadow="hover" class="stat-card card-purple">
          <div class="card-header">
            <span>本周阅读量</span>
            <div class="icon-container purple-bg">
              <el-icon class="card-icon"><clock /></el-icon>
            </div>
          </div>
          <div class="card-value">
            <el-statistic
              :value="weekReading"
              :precision="0"
              value-style="{ fontSize: '28px', color: '#6366F1' }"
              animation-duration="1500"
            >
              <template #prefix>
                <el-icon color="#6366F1"><pie-chart /></el-icon>
              </template>
              <template #suffix>次</template>
            </el-statistic>
          </div>
          <div class="card-trend">
            <el-icon color="#6366F1"><data-analysis /></el-icon>
            <span>周度分析</span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" :lg="6">
        <el-card shadow="hover" class="stat-card card-orange">
          <div class="card-header">
            <span>平均阅读量</span>
            <div class="icon-container orange-bg">
              <el-icon class="card-icon"><odometer /></el-icon>
            </div>
          </div>
          <div class="card-value">
            <el-statistic
              :value="avgReading"
              :precision="1"
              value-style="{ fontSize: '28px', color: '#F59E0B' }"
              animation-duration="1500"
            >
              <template #prefix>
                <el-icon color="#F59E0B"><data-analysis /></el-icon>
              </template>
              <template #suffix>次/篇</template>
            </el-statistic>
          </div>
          <div class="card-trend">
            <el-icon color="#F59E0B"><trend-charts /></el-icon>
            <span>平均指标</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 阅读量趋势图展示区域 -->
    <el-card class="chart-card" shadow="hover">
      <div class="chart-header">
        <h3>文章发布趋势分析</h3>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleDateChange"
          :shortcuts="dateShortcuts"
          style="width: 300px"
        ></el-date-picker>
         <span class="date-hint" style="color: #86909c; font-size: 12px;">
      <el-icon size="14"><info /></el-icon>
      <!-- 当天不计入 -->
    </span>
      </div>
      <div class="chart-container" v-loading="loading" element-loading-text="加载中...">
        <!-- 阅读量趋势图容器 -->
        <div id="reading-trend-chart" class="trend-chart-container"></div>
        <div v-if="!hasChartData && !loading" class="no-data-message">
          暂无数据，请选择其他日期范围
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue';

import {
  Calendar,
  Sunny,
  Clock,
  TrendCharts,
  Histogram,
  DataAnalysis,
  PieChart,
  Odometer,
  View
} from '@element-plus/icons-vue';
import * as echarts from 'echarts';
import { ElMessage } from 'element-plus';
import { artAllRead, artiWeekReading, artiAgnReading, artiTimeReading ,artiTodeRead} from '@/api/article';

// 统计数据
const totalReading = ref(0); // 总阅读量
const todayReading = ref(0); // 今日阅读量
const weekReading = ref(0); // 本周阅读量
const avgReading = ref(0); // 平均阅读量
const periodTotal = ref(null); // 所选时间段的总阅读量

// 图表相关
const dateRange = ref([]); // 日期范围
const loading = ref(false); // 加载状态
const chartData = ref([]); // 图表数据
const hasChartData = ref(false); // 是否有图表数据
let trendChart = null; // 图表实例

// 初始化基础统计数据
const getData = async () => {
  try {
    const [totalResult,todayReadin, weekResult, avgResult] = await Promise.all([
      artAllRead(), artiTodeRead(),artiWeekReading(), artiAgnReading()
    ]);
    totalReading.value = totalResult.data || 0;
    todayReading.value = todayReadin.data || 0;
    weekReading.value = weekResult.data || 0;
    avgReading.value = avgResult.data || 0;
  } catch (error) {
    ElMessage.error('基础统计数据加载失败');
    console.error('统计接口错误:', error);
  }
}; 
getData();

// 日期快捷选项
const dateShortcuts = [
  {
    text: '近7天',
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      return [start, end];
    },
  },
  {
    text: '近30天',
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      return [start, end];
    },
  },
  {
    text: '本月',
    value: () => [new Date(new Date().setDate(1)), new Date()],
  },
];

// 获取所选时间段的阅读量数据
const fetchPeriodTotal = async (start, end) => {
  loading.value = true;
  hasChartData.value = false;
  try {
    const formatDate = date => new Date(date).toISOString().split('.')[0];
    const res = await artiTimeReading(formatDate(start), formatDate(end));
    
    if (res && res.data && Array.isArray(res.data)) {
      // 后端返回的List<Integer>即每日阅读量（按日期顺序）
      chartData.value = res.data.map((value, index) => {
        // 生成对应日期（start到end的每一天）
        const currentDate = new Date(start);
        currentDate.setDate(start.getDate() + index);
        return {
          date: currentDate.toISOString().split('T')[0], // 格式化为yyyy-MM-dd
          value: value || 0
        };
      });
      
      // 计算时间段总阅读量（前端累加每日数据）
      periodTotal.value = chartData.value.reduce((sum, item) => sum + item.value, 0);
      hasChartData.value = true;
      nextTick(() => initTrendChart());
    } else {
      ElMessage.error('获取阅读量数据失败');
      // 生成全0数据
      chartData.value = generateChartData(start, end, 0);
      hasChartData.value = true;
      nextTick(() => initTrendChart());
    }
  } catch (error) {
    // 错误处理...
  } finally {
    loading.value = false;
  }
};

// 生成图表数据（当API不返回时间序列数据时使用）
const generateChartData = (start, end, total) => {
  if (!total) {
    // 如果没有数据，生成全部为0的数据点
    const startDate = new Date(start);
    const endDate = new Date(end);
    const daysDiff = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24)) + 1;
    const result = [];
    
    // 生成日期范围内的每一天的数据，值全部为0
    for (let i = 0; i < daysDiff; i++) {
      const currentDate = new Date(startDate);
      currentDate.setDate(startDate.getDate() + i);
      const formattedDate = currentDate.toISOString().split('T')[0];
      result.push({ date: formattedDate, value: 0 });
    }
    return result;
  }
  
  const startDate = new Date(start);
  const endDate = new Date(end);
  const daysDiff = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24)) + 1;
  const avgPerDay = total / daysDiff;
  let sumValues = 0;
  const result = [];
  
  // 生成日期范围内的每一天的数据
  for (let i = 0; i < daysDiff; i++) {
    const currentDate = new Date(startDate);
    currentDate.setDate(startDate.getDate() + i);
    const formattedDate = currentDate.toISOString().split('T')[0];
    
    // 最后一天调整，确保总和等于total
    if (i === daysDiff - 1) {
      const remainingValue = total - sumValues;
      result.push({
        date: formattedDate,
        value: Math.max(Math.round(remainingValue), Math.round(avgPerDay * 0.1))
      });
    } else {
      const value = Math.round(avgPerDay * (0.5 + Math.random()));
      sumValues += value;
      result.push({ date: formattedDate, value });
    }
  }

  return result;
};
// 初始化趋势图
const initTrendChart = () => {
  if (!chartData.value || chartData.value.length === 0) {
    hasChartData.value = false;
    return;
  }
  
  setTimeout(() => {
    const chartDom = document.getElementById('reading-trend-chart');
    if (!chartDom) return;
    
    // 确保容器有宽高
    if (chartDom.clientWidth === 0 || chartDom.clientHeight === 0) {
      chartDom.style.width = '100%';
      chartDom.style.height = '400px';
    }
    
    // 如果图表已经存在，先销毁
    if (trendChart) trendChart.dispose();
    
    try {
      // 创建新图表
      trendChart = echarts.init(chartDom);
      
      // 准备数据
      const dates = chartData.value.map(item => item.date);
      const values = chartData.value.map(item => item.value);
      
      // 检查是否所有值都为0
      const allZeros = values.every(value => value === 0);
      
      // 图表配置
      const option = {
        tooltip: { trigger: 'axis', formatter: '{b}: {c} 次阅读' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: dates,
          axisLabel: {
            rotate: dates.length > 10 ? 45 : 0,
            interval: dates.length > 30 ? 'auto' : 0
          }
        },
        yAxis: {
          type: 'value',
          name: '阅读量',
          nameTextStyle: { padding: [0, 0, 0, 30] },
          // 如果所有值都为0，确保y轴显示0点
          min: 0,
          max: allZeros ? 10 : undefined
        },
        series: [{
          name: '阅读量',
          type: 'line',
          data: values,
          smooth: true,
          symbol: 'circle',
          symbolSize: 6,
          itemStyle: { color: '#165DFF' },
          lineStyle: {
            width: 3,
            shadowColor: 'rgba(22, 93, 255, 0.3)',
            shadowBlur: 10
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(22, 93, 255, 0.5)' },
              { offset: 1, color: 'rgba(22, 93, 255, 0.1)' }
            ])
          }
        }]
      };
      
      // 设置图表选项并添加窗口大小变化时的自适应调整
      trendChart.setOption(option);
      window.addEventListener('resize', () => trendChart && trendChart.resize());
      
      // 手动触发一次resize，确保图表正确渲染
      setTimeout(() => trendChart && trendChart.resize(), 200);
    } catch (error) {
      console.error('图表初始化失败:', error);
    }
  }, 100); // 延迟100ms确保DOM已渲染
};
// 日期范围变化处理
const handleDateChange = val => val?.length === 2 && fetchPeriodTotal(val[0], val[1]);

// 页面初始化时加载近30天数据
onMounted(() => {
  const end = new Date();
  const start = new Date();
  start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
  dateRange.value = [start, end];
  fetchPeriodTotal(start, end);
});

// 组件卸载时清理图表实例
onUnmounted(() => {
  if (trendChart) {
    trendChart.dispose();
    window.removeEventListener('resize', () => trendChart && trendChart.resize());
    trendChart = null;
  }
});
</script>

<style scoped>
.reading-statistics-page { padding: 20px; background-color: #f5f7fa; min-height: 100vh; }
.page-header { margin-bottom: 20px; }
.page-header h2 { margin: 0 0 10px 0; color: #1d2129; }
.page-header p { margin: 0; color: #86909c; font-size: 14px; }
.statistics-cards { margin-bottom: 20px; }
.stat-card { height: 100%; transition: all 0.3s ease; overflow: hidden; }
.stat-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.card-header span { font-size: 14px; color: #86909c; }
.card-icon { color: #c9cdD4; font-size: 18px; }
.card-value { margin-bottom: 10px; }
.chart-card { padding: 20px; margin-bottom: 20px; }
.chart-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 10px; }
.chart-header h3 { margin: 0; color: #1d2129; font-size: 16px; }
.chart-container { width: 100%; min-height: 200px; display: flex; align-items: center; justify-content: center; }
.trend-chart-container { width: 100%; height: 400px; }
.no-data-message { text-align: center; padding: 40px 0; color: #999; font-size: 16px; }
.icon-container { width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; }
.blue-bg { background-color: rgba(22, 93, 255, 0.1); }
.green-bg { background-color: rgba(54, 211, 153, 0.1); }
.purple-bg { background-color: rgba(99, 102, 241, 0.1); }
.orange-bg { background-color: rgba(245, 158, 11, 0.1); }
.card-icon { font-size: 20px; }
.card-blue .card-icon { color: #165DFF; }
.card-green .card-icon { color: #36D399; }
.card-purple .card-icon { color: #6366F1; }
.card-orange .card-icon { color: #F59E0B; }
.card-trend { display: flex; align-items: center; gap: 5px; font-size: 12px; color: #86909c; margin-top: 10px; }
@media (max-width: 1024px) { .statistics-cards .el-col-lg-6 { flex: 0 0 50%; max-width: 50%; } }
@media (max-width: 768px) {
  .statistics-cards .el-col-lg-6 { flex: 0 0 100%; max-width: 100%; }
  .chart-header { flex-direction: column; align-items: flex-start; }
  .el-date-picker { width: 100% !important; }
  .period-total-value { font-size: 36px; }
}
</style>