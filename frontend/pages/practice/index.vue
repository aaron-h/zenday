<template>
  <view class="practice-page">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input
        class="search-input"
        placeholder="搜索功课..."
        v-model="searchKeyword"
        @input="handleSearch"
      />
    </view>

    <!-- 功课列表 -->
    <view class="practice-list">
      <practice-card
        v-for="practice in filteredPractices"
        :key="practice.id"
        :practice="practice"
        @action="handlePracticeAction"
      />
    </view>

    <!-- 空状态 -->
    <view v-if="practices.length === 0" class="empty-state">
      <text class="empty-icon">🌸</text>
      <text class="empty-text">暂无功课</text>
      <text class="empty-hint">点击右下角添加功课</text>
    </view>

    <!-- 浮动按钮组 -->
    <view class="fab-group">
      <view class="fab-btn" @click="handleAddRecommended">
        <text class="fab-icon">💡</text>
        <text class="fab-label">推荐</text>
      </view>
      <view class="fab-btn primary" @click="handleAddPractice">
        <text class="fab-icon">➕</text>
      </view>
    </view>
  </view>
</template>

<script>
import PracticeCard from '@/components/practice-card/practice-card.vue'
import practiceApi from '@/api/practice.js'

export default {
  components: {
    PracticeCard
  },
  data() {
    return {
      userId: 1, // TODO: 从store获取当前用户ID
      practices: [],
      searchKeyword: ''
    }
  },
  computed: {
    filteredPractices() {
      if (!this.searchKeyword) {
        return this.practices
      }
      return this.practices.filter(p =>
        p.name.includes(this.searchKeyword) ||
        (p.category && p.category.includes(this.searchKeyword))
      )
    }
  },
  onLoad() {
    this.loadPractices()
  },
  onShow() {
    // 页面显示时刷新列表
    this.loadPractices()
  },
  methods: {
    async loadPractices() {
      try {
        this.practices = await practiceApi.getUserPractices(this.userId)
      } catch (error) {
        console.error('加载功课失败:', error)
      }
    },

    handleSearch() {
      // 搜索功能已通过computed实现
    },

    handlePracticeAction(practice) {
      // 跳转到执行页面
      uni.navigateTo({
        url: `/pages/execute/index?practiceId=${practice.id}`
      })
    },

    handleAddPractice() {
      // 跳转到编辑页面
      uni.navigateTo({
        url: '/pages/practice/edit'
      })
    },

    async handleAddRecommended() {
      try {
        uni.showLoading({ title: '加载中...' })
        await practiceApi.createRecommendedPractices(this.userId)
        await this.loadPractices()
        uni.hideLoading()
        uni.showToast({
          title: '推荐功课已添加',
          icon: 'success'
        })
      } catch (error) {
        uni.hideLoading()
        console.error('添加推荐功课失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.practice-page {
  min-height: 100vh;
  background: #F2F6FC;
  padding: 24rpx;
  padding-bottom: 160rpx;
}

.search-bar {
  margin-bottom: 24rpx;
}

.search-input {
  width: 100%;
  height: 80rpx;
  background: #FFFFFF;
  border-radius: 40rpx;
  padding: 0 32rpx;
  font-size: 28rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.practice-list {
  padding-bottom: 40rpx;
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

.fab-group {
  position: fixed;
  right: 32rpx;
  bottom: 140rpx;
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.fab-btn {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  background: #FFFFFF;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.12);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.fab-btn.primary {
  background: linear-gradient(135deg, #4A90E2 0%, #67B26F 100%);
}

.fab-btn:active {
  transform: scale(0.95);
}

.fab-icon {
  font-size: 48rpx;
}

.fab-label {
  font-size: 20rpx;
  color: #606266;
  margin-top: 4rpx;
}
</style>
