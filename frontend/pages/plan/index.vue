<template>
  <view class="plan-page">
    <!-- 日期选择器 -->
    <view class="date-selector">
      <view class="date-nav" @click="prevDay">
        <text class="nav-icon">◀</text>
      </view>
      <picker mode="date" :value="currentDate" @change="onDateChange">
        <view class="date-display">
          <text class="date-text">{{ formatDate(currentDate) }}</text>
          <text class="calendar-icon">📅</text>
        </view>
      </picker>
      <view class="date-nav" @click="nextDay">
        <text class="nav-icon">▶</text>
      </view>
    </view>

    <!-- 进度条 -->
    <view v-if="plan" class="progress-section">
      <view class="progress-header">
        <text class="progress-label">完成度: {{ plan.completionPercentage.toFixed(0) }}%</text>
        <text class="progress-count">({{ plan.completedCount }}/{{ plan.totalCount }})</text>
      </view>
      <view class="progress-bar">
        <view class="progress-fill" :style="{ width: plan.completionPercentage + '%' }"></view>
      </view>
    </view>

    <!-- 计划详情 -->
    <view v-if="plan" class="plan-details">
      <view class="plan-header">
        <text class="start-time-label">起始时间: {{ plan.startTime }}</text>
        <view class="edit-btn" @click="handleEditPlan">
          <text>📝 编辑</text>
        </view>
      </view>

      <!-- 计划项列表 -->
      <view class="plan-items">
        <view
          v-for="item in plan.items"
          :key="item.id"
          class="plan-item"
          :class="{ 'completed': item.completed }"
          @click="handleToggleComplete(item)"
        >
          <view class="item-checkbox">
            <text v-if="item.completed" class="checkbox-icon">✅</text>
            <text v-else class="checkbox-icon">⬜</text>
          </view>
          <view class="item-content">
            <view class="item-header">
              <text class="item-icon">{{ item.practiceIcon }}</text>
              <text class="item-name">{{ item.practiceName }}</text>
            </view>
            <view class="item-time">
              <text>{{ item.startTime }} - {{ item.endTime }}</text>
              <text class="item-duration">({{ item.duration }}分钟)</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 操作按钮 -->
      <view class="action-buttons">
        <view class="action-btn primary" @click="handleStartPlan">
          <text>🚀 开始今日功课</text>
        </view>
        <view class="action-btn secondary" @click="handleBatchGenerate">
          <text>📋 批量生成</text>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-else class="empty-plan">
      <text class="empty-icon">📅</text>
      <text class="empty-text">今日暂无计划</text>
      <view class="create-btn" @click="handleEditPlan">
        <text>创建计划</text>
      </view>
    </view>
  </view>
</template>

<script>
import planApi from '@/api/plan.js'

export default {
  data() {
    return {
      userId: 1,
      currentDate: this.formatDateString(new Date()),
      plan: null
    }
  },
  onLoad() {
    this.loadPlan()
  },
  methods: {
    async loadPlan() {
      try {
        this.plan = await planApi.getDailyPlan(this.userId, this.currentDate)
      } catch (error) {
        console.error('加载计划失败:', error)
        this.plan = null
      }
    },

    formatDate(dateStr) {
      const date = new Date(dateStr)
      const today = new Date()
      if (this.isSameDay(date, today)) {
        return '今天'
      } else if (this.isSameDay(date, new Date(today.getTime() - 86400000))) {
        return '昨天'
      } else if (this.isSameDay(date, new Date(today.getTime() + 86400000))) {
        return '明天'
      }
      return `${date.getMonth() + 1}月${date.getDate()}日`
    },

    formatDateString(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    isSameDay(date1, date2) {
      return date1.toDateString() === date2.toDateString()
    },

    prevDay() {
      const date = new Date(this.currentDate)
      date.setDate(date.getDate() - 1)
      this.currentDate = this.formatDateString(date)
      this.loadPlan()
    },

    nextDay() {
      const date = new Date(this.currentDate)
      date.setDate(date.getDate() + 1)
      this.currentDate = this.formatDateString(date)
      this.loadPlan()
    },

    onDateChange(e) {
      this.currentDate = e.detail.value
      this.loadPlan()
    },

    async handleToggleComplete(item) {
      if (item.completed) {
        uni.showToast({
          title: '已完成',
          icon: 'none'
        })
        return
      }

      try {
        await planApi.markPlanItemCompleted(item.id)
        await this.loadPlan()
        uni.showToast({
          title: '已标记完成',
          icon: 'success'
        })
      } catch (error) {
        console.error('标记完成失败:', error)
      }
    },

    handleEditPlan() {
      uni.navigateTo({
        url: `/pages/plan/edit?date=${this.currentDate}`
      })
    },

    handleStartPlan() {
      if (!this.plan || this.plan.items.length === 0) {
        uni.showToast({
          title: '计划为空',
          icon: 'none'
        })
        return
      }

      // 找到第一个未完成的项目
      const nextItem = this.plan.items.find(item => !item.completed)
      if (!nextItem) {
        uni.showToast({
          title: '今日计划已全部完成',
          icon: 'success'
        })
        return
      }

      uni.navigateTo({
        url: `/pages/execute/index?practiceId=${nextItem.practiceId}&planItemId=${nextItem.id}`
      })
    },

    handleBatchGenerate() {
      uni.showModal({
        title: '批量生成计划',
        content: '基于今日计划生成未来多少天？',
        editable: true,
        placeholderText: '请输入天数(1-60)',
        success: async (res) => {
          if (res.confirm) {
            const days = parseInt(res.content)
            if (isNaN(days) || days < 1 || days > 60) {
              uni.showToast({
                title: '请输入1-60之间的数字',
                icon: 'none'
              })
              return
            }

            try {
              uni.showLoading({ title: '生成中...' })
              await planApi.batchGeneratePlans(this.userId, this.currentDate, days)
              uni.hideLoading()
              uni.showToast({
                title: `已生成${days}天计划`,
                icon: 'success'
              })
            } catch (error) {
              uni.hideLoading()
              console.error('批量生成失败:', error)
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.plan-page {
  min-height: 100vh;
  background: #F2F6FC;
  padding: 24rpx;
}

.date-selector {
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.date-nav {
  padding: 16rpx;
}

.nav-icon {
  font-size: 32rpx;
  color: #4A90E2;
}

.date-display {
  flex: 1;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16rpx;
}

.date-text {
  font-size: 32rpx;
  font-weight: 500;
  color: #2C3E50;
}

.calendar-icon {
  font-size: 32rpx;
}

.progress-section {
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.progress-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.progress-label {
  font-size: 28rpx;
  font-weight: 500;
  color: #2C3E50;
}

.progress-count {
  font-size: 24rpx;
  color: #909399;
}

.progress-bar {
  height: 16rpx;
  background: #E4E7ED;
  border-radius: 8rpx;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #4A90E2 0%, #67B26F 100%);
  transition: width 0.3s ease;
}

.plan-details {
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 32rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.plan-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.start-time-label {
  font-size: 28rpx;
  color: #606266;
}

.edit-btn {
  color: #4A90E2;
  font-size: 28rpx;
}

.plan-items {
  margin-bottom: 32rpx;
}

.plan-item {
  display: flex;
  align-items: flex-start;
  padding: 24rpx;
  margin-bottom: 16rpx;
  border-radius: 12rpx;
  background: #F8F9FA;
  transition: all 0.3s ease;
}

.plan-item.completed {
  background: rgba(82, 196, 26, 0.1);
}

.item-checkbox {
  margin-right: 24rpx;
}

.checkbox-icon {
  font-size: 40rpx;
}

.item-content {
  flex: 1;
}

.item-header {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.item-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.item-name {
  font-size: 28rpx;
  font-weight: 500;
  color: #2C3E50;
}

.item-time {
  font-size: 24rpx;
  color: #909399;
}

.item-duration {
  margin-left: 16rpx;
}

.action-buttons {
  display: flex;
  gap: 24rpx;
}

.action-btn {
  flex: 1;
  height: 88rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.action-btn.primary {
  background: linear-gradient(135deg, #4A90E2 0%, #67B26F 100%);
  color: #FFFFFF;
}

.action-btn.secondary {
  background: #FFFFFF;
  border: 2rpx solid #4A90E2;
  color: #4A90E2;
}

.empty-plan {
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
  margin-bottom: 48rpx;
}

.create-btn {
  padding: 24rpx 64rpx;
  background: linear-gradient(135deg, #4A90E2 0%, #67B26F 100%);
  color: #FFFFFF;
  border-radius: 48rpx;
  font-size: 28rpx;
}
</style>
