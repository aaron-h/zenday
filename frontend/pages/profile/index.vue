<template>
  <view class="profile-page">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <view class="user-avatar">{{ user.nickname ? user.nickname.charAt(0) : '禅' }}</view>
      <view class="user-info">
        <text class="user-nickname">{{ user.nickname || '修行者' }}</text>
        <text class="user-stats">累计修行 {{ formatHours(user.totalMinutes) }} 小时</text>
      </view>
    </view>

    <!-- 功能列表 -->
    <view class="menu-section">
      <view class="menu-item" @click="handleViewPractices">
        <text class="menu-icon">📋</text>
        <text class="menu-label">我的功课</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleViewStats">
        <text class="menu-icon">📊</text>
        <text class="menu-label">修行统计</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleSettings">
        <text class="menu-icon">⚙</text>
        <text class="menu-label">系统设置</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleAbout">
        <text class="menu-icon">ℹ</text>
        <text class="menu-label">关于</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <!-- Logo -->
    <view class="app-logo">
      <text class="logo-text">禅修功课</text>
      <text class="version-text">v1.0.0</text>
    </view>
  </view>
</template>

<script>
import statsApi from '@/api/stats.js'

export default {
  data() {
    return {
      userId: 1,
      user: {
        nickname: '修行者',
        totalMinutes: 0
      }
    }
  },
  onLoad() {
    this.loadUserInfo()
  },
  onShow() {
    this.loadUserInfo()
  },
  methods: {
    async loadUserInfo() {
      try {
        const stats = await statsApi.getStats(this.userId, 'all')
        this.user.totalMinutes = stats.totalMinutes || 0
      } catch (error) {
        console.error('加载用户信息失败:', error)
      }
    },

    formatHours(minutes) {
      return (minutes / 60).toFixed(1)
    },

    handleViewPractices() {
      uni.switchTab({
        url: '/pages/practice/index'
      })
    },

    handleViewStats() {
      uni.switchTab({
        url: '/pages/stats/index'
      })
    },

    handleSettings() {
      uni.showToast({
        title: '功能开发中',
        icon: 'none'
      })
    },

    handleAbout() {
      uni.showModal({
        title: '关于禅修功课',
        content: '禅修功课管理系统 v1.0.0\n\n一个面向个人的禅修功课管理工具，支持功课创建、每日计划编排、执行计时与提醒、历史统计等功能。\n\n愿您修行精进，智慧增长。🙏',
        showCancel: false
      })
    }
  }
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: #F2F6FC;
  padding: 24rpx;
}

.user-card {
  background: linear-gradient(135deg, #4A90E2 0%, #67B26F 100%);
  border-radius: 24rpx;
  padding: 48rpx 32rpx;
  margin-bottom: 24rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 8rpx 24rpx rgba(74, 144, 226, 0.3);
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  background: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
  color: #FFFFFF;
  font-weight: bold;
  margin-right: 32rpx;
  backdrop-filter: blur(10rpx);
}

.user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.user-nickname {
  font-size: 36rpx;
  font-weight: 500;
  color: #FFFFFF;
}

.user-stats {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.menu-section {
  background: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 32rpx;
  border-bottom: 1rpx solid #E4E7ED;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 40rpx;
  margin-right: 24rpx;
}

.menu-label {
  flex: 1;
  font-size: 28rpx;
  color: #2C3E50;
}

.menu-arrow {
  font-size: 48rpx;
  color: #C0C4CC;
}

.app-logo {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 48rpx 0;
}

.logo-text {
  font-size: 32rpx;
  font-weight: 500;
  color: #4A90E2;
  margin-bottom: 8rpx;
}

.version-text {
  font-size: 24rpx;
  color: #909399;
}
</style>
