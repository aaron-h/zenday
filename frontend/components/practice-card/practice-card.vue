<template>
  <view class="practice-card" :class="{ 'completed': practice.completed }">
    <view class="card-header">
      <view class="practice-info">
        <text class="practice-icon">{{ practice.icon || '🧘' }}</text>
        <text class="practice-name">{{ practice.name }}</text>
      </view>
      <view class="action-btn" @click="handleAction">
        <text v-if="practice.completed" class="completed-text">已完成</text>
        <text v-else class="start-text">开始</text>
      </view>
    </view>

    <view class="card-divider"></view>

    <view class="card-body">
      <view class="info-item">
        <text class="info-icon">📂</text>
        <text class="info-text">{{ practice.category || '未分类' }}</text>
      </view>
      <view class="info-item">
        <text class="info-icon">⏱</text>
        <text class="info-text">{{ practice.defaultDuration || 30 }}分钟</text>
      </view>
      <view v-if="practice.defaultReminderTime" class="info-item">
        <text class="info-icon">🔔</text>
        <text class="info-text">{{ practice.defaultReminderTime }}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'PracticeCard',
  props: {
    practice: {
      type: Object,
      required: true
    }
  },
  methods: {
    handleAction() {
      this.$emit('action', this.practice)
    }
  }
}
</script>

<style scoped>
.practice-card {
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 32rpx;
  margin: 24rpx 0;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.practice-card.completed {
  background: linear-gradient(135deg, rgba(82, 196, 26, 0.1) 0%, rgba(82, 196, 26, 0.05) 100%);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.practice-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.practice-icon {
  font-size: 48rpx;
  margin-right: 24rpx;
}

.practice-name {
  font-size: 32rpx;
  font-weight: 500;
  color: #2C3E50;
}

.action-btn {
  padding: 12rpx 32rpx;
  border-radius: 32rpx;
  background: linear-gradient(135deg, #4A90E2 0%, #67B26F 100%);
}

.start-text {
  color: #FFFFFF;
  font-size: 28rpx;
}

.completed-text {
  color: #52C41A;
  font-size: 28rpx;
}

.card-divider {
  height: 2rpx;
  background: #E4E7ED;
  margin: 24rpx 0;
}

.card-body {
  display: flex;
  flex-wrap: wrap;
  gap: 24rpx;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-icon {
  font-size: 24rpx;
  margin-right: 8rpx;
}

.info-text {
  font-size: 24rpx;
  color: #606266;
}
</style>
