<template>
  <view class="stats-page">
    <!-- 范围选择 -->
    <view class="range-tabs">
      <view
        v-for="r in ranges"
        :key="r.value"
        class="range-tab"
        :class="{ active: currentRange === r.value }"
        @click="handleRangeChange(r.value)"
      >
        <text>{{ r.label }}</text>
      </view>
    </view>

    <!-- 总时长卡片 -->
    <view class="total-card">
      <view class="total-icon">⏰</view>
      <view class="total-content">
        <text class="total-label">总时长</text>
        <text class="total-hours">{{ stats.totalHours ? stats.totalHours.toFixed(1) : 0 }}</text>
        <text class="total-unit">小时</text>
      </view>
      <view class="total-meta">
        <text class="total-minutes">{{ stats.totalMinutes || 0 }} 分钟</text>
        <text class="total-sessions">{{ stats.sessionCount || 0 }} 次修行</text>
      </view>
    </view>

    <!-- 分类统计 -->
    <view v-if="categoryStatsArray.length > 0" class="category-section">
      <view class="section-title">
        <text>功课分布</text>
      </view>
      <view class="category-list">
        <view
          v-for="cat in categoryStatsArray"
          :key="cat.category"
          class="category-item"
        >
          <view class="category-header">
            <text class="category-name">{{ cat.category }}</text>
            <text class="category-percentage">{{ cat.percentage }}%</text>
          </view>
          <view class="category-bar">
            <view class="bar-fill" :style="{ width: cat.percentage + '%' }"></view>
          </view>
          <view class="category-meta">
            <text class="category-minutes">{{ cat.minutes }} 分钟</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 最近会话 -->
    <view v-if="stats.recentSessions && stats.recentSessions.length > 0" class="sessions-section">
      <view class="section-title">
        <text>最近会话</text>
      </view>
      <view class="session-list">
        <view
          v-for="(session, index) in stats.recentSessions"
          :key="index"
          class="session-item"
        >
          <view class="session-dot"></view>
          <view class="session-content">
            <text class="session-date">{{ session.date }}</text>
            <text class="session-name">{{ session.practiceName }}</text>
          </view>
          <text class="session-duration">{{ session.duration }}分钟</text>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="!stats.totalMinutes" class="empty-state">
      <text class="empty-icon">📊</text>
      <text class="empty-text">暂无统计数据</text>
      <text class="empty-hint">开始你的修行之旅吧</text>
    </view>
  </view>
</template>

<script>
import statsApi from '@/api/stats.js'

export default {
  data() {
    return {
      userId: 1,
      currentRange: 'week',
      stats: {},
      ranges: [
        { value: 'today', label: '今日' },
        { value: 'week', label: '本周' },
        { value: 'month', label: '本月' },
        { value: 'all', label: '全部' }
      ]
    }
  },
  computed: {
    categoryStatsArray() {
      if (!this.stats.categoryStats) return []
      return Object.values(this.stats.categoryStats)
        .sort((a, b) => b.minutes - a.minutes)
    }
  },
  onLoad() {
    this.loadStats()
  },
  onShow() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        this.stats = await statsApi.getStats(this.userId, this.currentRange)
      } catch (error) {
        console.error('加载统计失败:', error)
        this.stats = {}
      }
    },

    handleRangeChange(range) {
      this.currentRange = range
      this.loadStats()
    }
  }
}
</script>

<style scoped>
.stats-page {
  min-height: 100vh;
  background: #F2F6FC;
  padding: 24rpx;
}

.range-tabs {
  display: flex;
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 8rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.range-tab {
  flex: 1;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: #606266;
  transition: all 0.3s ease;
}

.range-tab.active {
  background: linear-gradient(135deg, #4A90E2 0%, #67B26F 100%);
  color: #FFFFFF;
}

.total-card {
  background: linear-gradient(135deg, #4A90E2 0%, #67B26F 100%);
  border-radius: 24rpx;
  padding: 48rpx 32rpx;
  margin-bottom: 24rpx;
  color: #FFFFFF;
  box-shadow: 0 8rpx 24rpx rgba(74, 144, 226, 0.3);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.total-icon {
  font-size: 80rpx;
  margin-bottom: 24rpx;
}

.total-content {
  display: flex;
  align-items: baseline;
  gap: 16rpx;
  margin-bottom: 24rpx;
}

.total-label {
  font-size: 28rpx;
  opacity: 0.9;
}

.total-hours {
  font-size: 88rpx;
  font-weight: bold;
  font-family: 'DIN', 'Roboto', monospace;
}

.total-unit {
  font-size: 32rpx;
  opacity: 0.9;
}

.total-meta {
  display: flex;
  gap: 32rpx;
  font-size: 24rpx;
  opacity: 0.8;
}

.category-section,
.sessions-section {
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #2C3E50;
  margin-bottom: 32rpx;
}

.category-item {
  margin-bottom: 32rpx;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.category-name {
  font-size: 28rpx;
  color: #2C3E50;
}

.category-percentage {
  font-size: 28rpx;
  font-weight: 500;
  color: #4A90E2;
}

.category-bar {
  height: 16rpx;
  background: #E4E7ED;
  border-radius: 8rpx;
  overflow: hidden;
  margin-bottom: 8rpx;
}

.bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #4A90E2 0%, #67B26F 100%);
  transition: width 0.3s ease;
}

.category-meta {
  display: flex;
  justify-content: flex-end;
}

.category-minutes {
  font-size: 24rpx;
  color: #909399;
}

.session-item {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #E4E7ED;
}

.session-item:last-child {
  border-bottom: none;
}

.session-dot {
  width: 12rpx;
  height: 12rpx;
  background: #4A90E2;
  border-radius: 50%;
  margin-right: 24rpx;
  flex-shrink: 0;
}

.session-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.session-date {
  font-size: 24rpx;
  color: #909399;
}

.session-name {
  font-size: 28rpx;
  color: #2C3E50;
}

.session-duration {
  font-size: 28rpx;
  color: #4A90E2;
  font-weight: 500;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 32rpx;
}

.empty-text {
  font-size: 32rpx;
  color: #909399;
  margin-bottom: 16rpx;
}

.empty-hint {
  font-size: 24rpx;
  color: #C0C4CC;
}
</style>
