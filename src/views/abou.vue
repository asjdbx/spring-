<template>
  <div class="about-container">
    <div class="profile-card" @mousemove="handleMouseMove" ref="card">
      <div class="card-content">
        <div class="avatar-container">
          <img 
            src="../assets/about.jpg"
            alt="Profile Avatar"
            class="avatar"
          /> 
           da
          <div class="glow"></div>
        </div>
        <h1 class="name">wang</h1>
        <h2 class="title">学生</h2>
        <p class="bio">
          热爱编程，学习技术，相信技术改变世界，
          致力于创造优秀的用户体验。
        </p>
        <div class="skills">
          <span class="skill-tag">Vue.js</span>
          <span class="skill-tag">java</span>
          <span class="skill-tag">Spring</span>
          <span class="skill-tag">MySql</span>
        </div>
        <div class="social-links">
          <a href="#" class="social-link" title="GitHub">
            <i class="fab fa-github"></i>
          </a>
          <a href="#" class="social-link" title="LinkedIn">
            <i class="fab fa-linkedin"></i>
          </a>
          <a href="#" class="social-link" title="Twitter">
            <i class="fab fa-twitter"></i>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const card = ref(null)

const handleMouseMove = (e) => {
  if (!card.value) return
  
  const rect = card.value.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  
  const centerX = rect.width / 2
  const centerY = rect.height / 2
  
  const rotateX = (y - centerY) / 20
  const rotateY = -(x - centerX) / 20
  
  card.value.style.transform = 
    `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`
}

const resetCard = () => {
  if (card.value) {
    card.value.style.transform = 
      'perspective(1000px) rotateX(0deg) rotateY(0deg)'
  }
}

onMounted(() => {
  document.addEventListener('mouseleave', resetCard)
})

onUnmounted(() => {
  document.removeEventListener('mouseleave', resetCard)
})
</script>

<style scoped>
.about-container {
  min-height: 72.5vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #ffffff; /* 改为白色背景 */
  padding: 20px;
  perspective: 700px;
}

.profile-card {
  width: 100%;
  max-width: 340px; /* 缩小卡片尺寸约15% */
  background: #f0f7ff; /* 浅蓝色背景，与系统风格匹配 */
  border-radius: 16px;
  padding: 30px; /* 减小内边距 */
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.15); /* 调整阴影颜色为蓝色系 */
  transition: transform 0.3s ease;
  transform-style: preserve-3d;
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(64, 158, 255, 0.1); /* 添加淡蓝色边框 */
}

.profile-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    45deg,
    transparent 0%,
    rgba(64, 158, 255, 0.05) 45%,
    rgba(64, 158, 255, 0.1) 50%,
    rgba(64, 158, 255, 0.05) 55%,
    transparent 100%
  );
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}

.profile-card:hover::before {
  transform: translateX(100%);
}

.avatar-container {
  position: relative;
  width: 130px; /* 缩小头像尺寸 */
  height: 130px;
  margin: 0 auto 20px;
}

.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid rgba(64, 158, 255, 0.2); /* 蓝色边框 */
  transition: transform 0.3s ease;
}

.glow {
  position: absolute;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 50% 50%, 
    rgba(64, 158, 255, 0.2),
    transparent 70%);
  border-radius: 50%;
  top: 0;
  left: 0;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-container:hover .glow {
  opacity: 1;
}

.avatar-container:hover .avatar {
  transform: scale(1.05);
}

.name {
  font-size: 1.8em; /* 稍微减小字体 */
  color: #303133; /* 深色文字 */
  margin: 0 0 8px;
  text-align: center;
  font-weight: 600;
  text-shadow: none; /* 移除文字阴影 */
}

.title {
  font-size: 1.1em;
  color: #409EFF; /* Element UI 主色 */
  margin: 0 0 18px;
  text-align: center;
  font-weight: 400;
}

.bio {
  color: #606266; /* 深灰色文字 */
  text-align: center;
  line-height: 1.6;
  margin-bottom: 22px;
}

.skills {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px;
  margin-bottom: 22px;
}

.skill-tag {
  background: rgba(64, 158, 255, 0.1);
  color: #409EFF;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 0.85em;
  transition: all 0.3s ease;
}

.skill-tag:hover {
  background: rgba(64, 158, 255, 0.2);
  transform: translateY(-2px);
}

.social-links {
  display: flex;
  justify-content: center;
  gap: 18px;
}

.social-link {
  color: #909399; /* 灰色 */
  font-size: 1.4em;
  transition: all 0.3s ease;
}

.social-link:hover {
  color: #409EFF;
  transform: translateY(-3px);
}

@keyframes float {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-8px); /* 减小浮动幅度 */
  }
  100% {
    transform: translateY(0px);
  }
}

.card-content {
  animation: float 6s ease-in-out infinite;
}
</style>