<template>
  <view class="breathing-ball-container">
    <view
      class="breathing-ball"
      :class="rhythmClass"
      :style="ballStyle">
      <view class="ball-inner"></view>
    </view>
    <view class="breathing-text">{{ breathingText }}</view>
  </view>
</template>

<script>
export default {
  name: 'BreathingBall',
  props: {
    rhythm: {
      type: String,
      default: 'medium' // slow, medium, fast
    },
    isPlaying: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      isInhaling: true,
      breathingText: '吸气'
    }
  },
  computed: {
    rhythmClass() {
      return `rhythm-${this.rhythm}`
    },
    ballStyle() {
      const colors = {
        slow: 'linear-gradient(135deg, #9B59B6 0%, #E8D5F2 100%)',
        medium: 'linear-gradient(135deg, #4A90E2 0%, #D1E8FF 100%)',
        fast: 'linear-gradient(135deg, #67B26F 0%, #D5F2DB 100%)'
      }
      return {
        background: colors[this.rhythm] || colors.medium
      }
    }
  },
  watch: {
    isPlaying(newVal) {
      if (newVal) {
        this.startBreathing()
      } else {
        this.stopBreathing()
      }
    }
  },
  methods: {
    startBreathing() {
      const durations = {
        slow: 5000,
        medium: 4000,
        fast: 3000
      }
      const duration = durations[this.rhythm] || 4000

      this.breathingInterval = setInterval(() => {
        this.isInhaling = !this.isInhaling
        this.breathingText = this.isInhaling ? '吸气' : '呼气'
      }, duration)
    },
    stopBreathing() {
      if (this.breathingInterval) {
        clearInterval(this.breathingInterval)
        this.breathingInterval = null
      }
    }
  },
  beforeUnmount() {
    this.stopBreathing()
  }
}
</script>

<style scoped>
.breathing-ball-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.breathing-ball {
  width: 300rpx;
  height: 300rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.15);
  position: relative;
}

.ball-inner {
  width: 200rpx;
  height: 200rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  animation: pulse 4s ease-in-out infinite;
}

/* 慢节奏 */
.rhythm-slow .ball-inner {
  animation: pulse 5s ease-in-out infinite;
}

/* 中节奏 */
.rhythm-medium .ball-inner {
  animation: pulse 4s ease-in-out infinite;
}

/* 快节奏 */
.rhythm-fast .ball-inner {
  animation: pulse 3s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(0.8);
    opacity: 0.6;
  }
  50% {
    transform: scale(1.2);
    opacity: 1;
  }
}

.breathing-text {
  margin-top: 60rpx;
  font-size: 36rpx;
  color: #606266;
  font-weight: 500;
}
</style>
