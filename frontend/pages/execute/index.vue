<template>
  <view class="execute-page">
    <!-- 功课标题 -->
    <view class="practice-title">
      <text class="title-icon">{{ practice.icon }}</text>
      <text class="title-text">{{ practice.name }}</text>
    </view>

    <!-- 呼吸球 -->
    <breathing-ball
      :rhythm="settings.breathingRhythm"
      :isPlaying="isRunning"
    />

    <!-- 计时器 -->
    <view class="timer-section">
      <text class="timer-elapsed">{{ formatTime(elapsedSeconds) }}</text>
      <text class="timer-divider">────</text>
      <text class="timer-total">{{ formatTime(settings.duration * 60) }}</text>
    </view>

    <!-- 设置信息 -->
    <view class="settings-info">
      <view class="setting-item">
        <text class="setting-icon">🔊</text>
        <text class="setting-text">{{ getSoundName(settings.sound) }}</text>
      </view>
      <view class="setting-item">
        <text class="setting-icon">🫁</text>
        <text class="setting-text">{{ getRhythmName(settings.breathingRhythm) }}</text>
      </view>
    </view>

    <!-- 控制按钮 -->
    <view class="control-buttons">
      <view v-if="!isRunning && elapsedSeconds === 0" class="control-btn start" @click="handleStart">
        <text>开始</text>
      </view>
      <template v-else>
        <view class="control-btn pause" @click="handlePauseResume">
          <text>{{ isRunning ? '⏸ 暂停' : '▶ 继续' }}</text>
        </view>
        <view class="control-btn complete" @click="handleComplete">
          <text>✓ 完成</text>
        </view>
      </template>
    </view>

    <!-- 设置按钮 -->
    <view class="settings-btn" @click="showSettings = true">
      <text>⚙ 设置</text>
    </view>

    <!-- 设置弹窗 -->
    <view v-if="showSettings" class="settings-modal" @click="showSettings = false">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">修行设置</text>
          <text class="close-btn" @click="showSettings = false">✕</text>
        </view>

        <view class="modal-body">
          <!-- 时长设置 -->
          <view class="setting-group">
            <text class="group-title">时长</text>
            <view class="duration-options">
              <view
                v-for="d in durationOptions"
                :key="d"
                class="duration-option"
                :class="{ active: settings.duration === d }"
                @click="settings.duration = d"
              >
                <text>{{ d }}分钟</text>
              </view>
            </view>
          </view>

          <!-- 禅音设置 -->
          <view class="setting-group">
            <text class="group-title">禅音</text>
            <view class="sound-options">
              <view
                v-for="s in soundOptions"
                :key="s.value"
                class="sound-option"
                :class="{ active: settings.sound === s.value }"
                @click="settings.sound = s.value"
              >
                <text>{{ s.label }}</text>
              </view>
            </view>
          </view>

          <!-- 呼吸节奏设置 -->
          <view class="setting-group">
            <text class="group-title">呼吸节奏</text>
            <view class="rhythm-options">
              <view
                v-for="r in rhythmOptions"
                :key="r.value"
                class="rhythm-option"
                :class="{ active: settings.breathingRhythm === r.value }"
                @click="settings.breathingRhythm = r.value"
              >
                <text>{{ r.label }}</text>
              </view>
            </view>
          </view>

          <view class="save-btn" @click="handleSaveSettings">
            <text>保存为默认</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import BreathingBall from '@/components/breathing-ball/breathing-ball.vue'
import practiceApi from '@/api/practice.js'
import sessionApi from '@/api/session.js'
import planApi from '@/api/plan.js'

export default {
  components: {
    BreathingBall
  },
  data() {
    return {
      userId: 1,
      practiceId: null,
      planItemId: null,
      practice: {},
      settings: {
        duration: 30,
        sound: 'qing',
        volume: 50,
        breathingRhythm: 'medium'
      },
      isRunning: false,
      elapsedSeconds: 0,
      sessionId: null,
      timer: null,
      showSettings: false,

      durationOptions: [5, 10, 20, 30, 45, 60],
      soundOptions: [
        { value: 'qing', label: '磬声' },
        { value: 'muyu', label: '木鱼' },
        { value: 'bell', label: '钟声' },
        { value: 'wind', label: '风铃' },
        { value: 'guqin', label: '古琴' }
      ],
      rhythmOptions: [
        { value: 'slow', label: '慢节奏' },
        { value: 'medium', label: '中节奏' },
        { value: 'fast', label: '快节奏' }
      ]
    }
  },
  onLoad(options) {
    this.practiceId = parseInt(options.practiceId)
    this.planItemId = options.planItemId ? parseInt(options.planItemId) : null
    this.loadPractice()
  },
  onUnload() {
    this.stopTimer()
  },
  methods: {
    async loadPractice() {
      try {
        this.practice = await practiceApi.getPractice(this.practiceId)
        // 加载默认设置
        this.settings = {
          duration: this.practice.defaultDuration || 30,
          sound: this.practice.defaultSound || 'qing',
          volume: this.practice.defaultVolume || 50,
          breathingRhythm: this.practice.defaultBreathingRhythm || 'medium'
        }
      } catch (error) {
        console.error('加载功课失败:', error)
      }
    },

    async handleStart() {
      try {
        // 创建会话
        const session = await sessionApi.createSession({
          userId: this.userId,
          practiceId: this.practiceId,
          startTime: new Date().toISOString(),
          duration: this.settings.duration,
          sound: this.settings.sound,
          volume: this.settings.volume,
          breathingRhythm: this.settings.breathingRhythm,
          planItemId: this.planItemId
        })

        this.sessionId = session.id
        this.isRunning = true
        this.startTimer()
      } catch (error) {
        console.error('创建会话失败:', error)
      }
    },

    handlePauseResume() {
      this.isRunning = !this.isRunning
      if (this.isRunning) {
        this.startTimer()
      } else {
        this.stopTimer()
      }
    },

    async handleComplete() {
      uni.showModal({
        title: '确认完成',
        content: '确定要完成本次修行吗？',
        success: async (res) => {
          if (res.confirm) {
            await this.completeSession()
          }
        }
      })
    },

    async completeSession() {
      try {
        this.stopTimer()

        // 完成会话
        await sessionApi.completeSession(this.sessionId, {
          endTime: new Date().toISOString(),
          note: ''
        })

        // 如果是从计划执行的，标记计划项完成
        if (this.planItemId) {
          await planApi.markPlanItemCompleted(this.planItemId)
        }

        uni.showToast({
          title: '修行完成',
          icon: 'success'
        })

        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (error) {
        console.error('完成会话失败:', error)
      }
    },

    async handleSaveSettings() {
      try {
        await practiceApi.updatePractice(this.practiceId, {
          ...this.practice,
          defaultDuration: this.settings.duration,
          defaultSound: this.settings.sound,
          defaultVolume: this.settings.volume,
          defaultBreathingRhythm: this.settings.breathingRhythm
        })

        this.showSettings = false
        uni.showToast({
          title: '设置已保存',
          icon: 'success'
        })
      } catch (error) {
        console.error('保存设置失败:', error)
      }
    },

    startTimer() {
      this.timer = setInterval(() => {
        this.elapsedSeconds++
        if (this.elapsedSeconds >= this.settings.duration * 60) {
          this.completeSession()
        }
      }, 1000)
    },

    stopTimer() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },

    formatTime(seconds) {
      const mins = Math.floor(seconds / 60)
      const secs = seconds % 60
      return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
    },

    getSoundName(value) {
      const sound = this.soundOptions.find(s => s.value === value)
      return sound ? sound.label : value
    },

    getRhythmName(value) {
      const rhythm = this.rhythmOptions.find(r => r.value === value)
      return rhythm ? rhythm.label : value
    }
  }
}
</script>

<style scoped>
.execute-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #F2F6FC 0%, #E8F4F8 100%);
  padding: 48rpx 32rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.practice-title {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 48rpx;
}

.title-icon {
  font-size: 80rpx;
  margin-bottom: 16rpx;
}

.title-text {
  font-size: 40rpx;
  font-weight: 500;
  color: #2C3E50;
}

.timer-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 48rpx 0;
}

.timer-elapsed {
  font-size: 80rpx;
  font-weight: bold;
  color: #4A90E2;
  font-family: 'DIN', 'Roboto', monospace;
}

.timer-divider {
  font-size: 24rpx;
  color: #909399;
  margin: 16rpx 0;
}

.timer-total {
  font-size: 32rpx;
  color: #909399;
  font-family: 'DIN', 'Roboto', monospace;
}

.settings-info {
  display: flex;
  gap: 48rpx;
  margin-bottom: 64rpx;
}

.setting-item {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.setting-icon {
  font-size: 32rpx;
}

.setting-text {
  font-size: 28rpx;
  color: #606266;
}

.control-buttons {
  display: flex;
  gap: 32rpx;
  margin-bottom: 48rpx;
}

.control-btn {
  min-width: 200rpx;
  height: 88rpx;
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.12);
}

.control-btn.start {
  background: linear-gradient(135deg, #4A90E2 0%, #67B26F 100%);
  color: #FFFFFF;
  width: 400rpx;
}

.control-btn.pause {
  background: #FFFFFF;
  color: #4A90E2;
}

.control-btn.complete {
  background: linear-gradient(135deg, #52C41A 0%, #73D13D 100%);
  color: #FFFFFF;
}

.settings-btn {
  color: #909399;
  font-size: 28rpx;
}

.settings-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: flex-end;
  z-index: 999;
}

.modal-content {
  width: 100%;
  max-height: 80vh;
  background: #FFFFFF;
  border-radius: 32rpx 32rpx 0 0;
  padding: 48rpx 32rpx;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 48rpx;
}

.modal-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #2C3E50;
}

.close-btn {
  font-size: 48rpx;
  color: #909399;
}

.modal-body {
  max-height: 60vh;
  overflow-y: auto;
}

.setting-group {
  margin-bottom: 48rpx;
}

.group-title {
  font-size: 28rpx;
  font-weight: 500;
  color: #606266;
  margin-bottom: 24rpx;
  display: block;
}

.duration-options,
.sound-options,
.rhythm-options {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.duration-option,
.sound-option,
.rhythm-option {
  padding: 16rpx 32rpx;
  border-radius: 32rpx;
  background: #F2F6FC;
  color: #606266;
  font-size: 28rpx;
  border: 2rpx solid transparent;
}

.duration-option.active,
.sound-option.active,
.rhythm-option.active {
  background: linear-gradient(135deg, rgba(74, 144, 226, 0.1) 0%, rgba(103, 178, 111, 0.1) 100%);
  border-color: #4A90E2;
  color: #4A90E2;
}

.save-btn {
  width: 100%;
  height: 88rpx;
  border-radius: 44rpx;
  background: linear-gradient(135deg, #4A90E2 0%, #67B26F 100%);
  color: #FFFFFF;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  margin-top: 32rpx;
}
</style>
